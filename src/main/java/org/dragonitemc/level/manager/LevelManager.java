package org.dragonitemc.level.manager;

import com.dragonite.mc.dnmc.core.main.DragoniteMC;
import com.dragonite.mc.dnmc.core.managers.RedisDataSource;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.dragonitemc.level.api.LevelService;
import org.dragonitemc.level.api.UpdateResult;
import org.dragonitemc.level.config.DragonLevelConfig;
import org.dragonitemc.level.config.DragonLevelData;
import org.dragonitemc.level.config.DragonLevelMessage;
import org.dragonitemc.level.db.Levels;
import org.dragonitemc.level.repository.LevelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class LevelManager implements LevelService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LevelManager.class);

    @Inject
    private LevelRepository levelRepository;

    @Inject
    private DragonLevelConfig config;

    @Inject
    private DragonLevelMessage message;

    @Inject
    private DragonLevelData data;

    @Override
    public int getLevel(UUID player) {
        return levelRepository.findById(player).map(Levels::getLevel).orElse(config.defaultLevel);
    }

    @Override
    public int getExp(UUID player) {
        return levelRepository.findById(player).map(Levels::getExp).orElse(config.defaultExp);
    }

    @Override
    public UpdateResult setLevel(UUID player, int level) {
        var user = levelRepository.findById(player).orElseGet(null);
        if (user == null) {
            return UpdateResult.PLAYER_NOT_EXIST;
        }
        level = Math.max(1, Math.min(config.maxLevel, level));
        int oldLevel = user.getLevel();
        user.setLevel(level);
        int exp = data.Levels.get(String.valueOf(level));
        user.setExp(exp);
        levelRepository.save(user);
        if (level > oldLevel) {
            Bukkit.getPlayer(player).sendMessage("§a你的等级提升到了" + level + "级");
        }
        return UpdateResult.SUCCESS;
    }

    @Override
    public UpdateResult addLevel(UUID player, int level) {
        var user = levelRepository.findById(player).orElseGet(null);
        if (user == null) {
            return UpdateResult.PLAYER_NOT_EXIST;
        }
        if (level != 0) {
            int newLevel = Math.max(1, Math.min(config.maxLevel, user.getLevel() + level));
            if (newLevel != user.getLevel()) {
                int oldLevel = user.getLevel();
                user.setLevel(newLevel);
                user.setExp(data.Levels.get(String.valueOf(oldLevel)));
                if (newLevel > oldLevel) {
                    sendLevelUpMessage(Bukkit.getPlayer(player), newLevel, data.Levels.get(String.valueOf(newLevel + 1)) - user.getExp());
                }
            }
        }
        levelRepository.save(user);
        return UpdateResult.SUCCESS;
    }

    @Override
    public UpdateResult removeLevel(UUID player, int level) {
        addLevel(player, -level);
        return UpdateResult.SUCCESS;
    }

    @Override
    public UpdateResult setExp(UUID player, int exp) {
        var user = levelRepository.findById(player).orElseGet(null);
        if (user == null) {
            return UpdateResult.PLAYER_NOT_EXIST;
        }
        if (user.getExp() != exp) {
            user.setExp(exp);
            int newLevel = calculateNewLevel(Integer.parseInt(String.valueOf(exp)));
            if (user.getLevel() != newLevel) {
                int oldLevel = user.getLevel();
                user.setLevel(newLevel);
                if (newLevel > oldLevel) {
                    sendLevelUpMessage(Bukkit.getPlayer(player), newLevel, data.Levels.get(String.valueOf(newLevel + 1)) - user.getExp());
                }
            }
        }
        levelRepository.save(user);
        return UpdateResult.SUCCESS;
    }

    @Override
    public UpdateResult addExp(UUID player, int exp) {
        var user = levelRepository.findById(player).orElseGet(null);
        if (user == null) {
            return UpdateResult.PLAYER_NOT_EXIST;
        }
        int newExp;
        int oldExp = user.getExp();
        if (oldExp + exp > 2147483647L) {
            newExp = Integer.MAX_VALUE;
        } else {
            newExp = oldExp + exp;
        }
        if (newExp != oldExp) {
            if (newExp > oldExp) {
                if (user.getLevel() >= config.maxLevel) {
                    user.setExp(newExp);
                } else {
                    int requiredExp = data.Levels.get(String.valueOf(user.getLevel() + 1));
                    if (newExp < requiredExp) {
                        user.setExp(newExp);
                    } else {
                        int newLevel = calculateNewLevel(newExp);
                        user.setExp(newExp);
                        if (newLevel != user.getLevel()) {
                            int oldLevel = user.getLevel();
                            user.setLevel(newLevel);
                            if (newLevel > oldLevel) {
                                sendLevelUpMessage(Bukkit.getPlayer(player), newLevel, data.Levels.get(String.valueOf(newLevel + 1)) - newExp);
                            }
                        }
                    }
                }
            } else {
                if (user.getLevel() <= 1) {
                    user.setExp(Math.max(0, newExp));
                } else {
                    int requiredExp = data.Levels.get(String.valueOf(user.getLevel()));
                    if (newExp >= requiredExp) {
                        user.setExp(newExp);
                    } else {
                        int newLevel = calculateNewLevel(newExp);
                        user.setExp(newExp);
                        if (newLevel != user.getLevel()) {
                            int oldLevel = user.getLevel();
                            user.setLevel(newLevel);
                            if (newLevel > oldLevel) {
                                sendLevelDownMessage(Bukkit.getPlayer(player), newLevel, data.Levels.get(String.valueOf(newLevel + 1)) - newExp);
                            }
                        }
                    }
                }
            }
        }
        levelRepository.save(user);
        return UpdateResult.SUCCESS;
    }

    @Override
    public UpdateResult removeExp(UUID player, int exp) {
        addExp(player, -exp);
        return UpdateResult.SUCCESS;
    }

    private int calculateNewLevel(int exp) {
        exp = Math.max(0, exp);
        if (exp == 0) {
            return 1;
        }
        int level = 1;
        for (Map.Entry<String, Integer> entry : data.Levels.entrySet()) {
            if (exp >= entry.getValue() && Integer.parseInt(entry.getKey()) > level) {
                level = Integer.parseInt(entry.getKey());
            }
        }
        return level;
    }

    private void sendLevelUpMessage(Player player, int newLevel, int nextExp) {
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
        message.getLang().getPureList("level.upgrade").forEach(msg ->
                player.sendMessage(MiniMessage.miniMessage().deserialize(msg.replace("{LEVEL}", String.valueOf(newLevel))
                                                                            .replace("{EXP}", String.valueOf(nextExp))))
        );
    }

    private void sendLevelDownMessage(Player player, int newLevel, int nextExp) {
        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_FALL, 1, 1);
        message.getLang().getPureList("level.downgrade").forEach(msg ->
                player.sendMessage(MiniMessage.miniMessage().deserialize(msg.replace("{LEVEL}", String.valueOf(newLevel))
                                                                            .replace("{EXP}", String.valueOf(nextExp))))
        );
    }

    @Override
    public String getEmptyProgressBar() {
        StringBuilder progressBar = new StringBuilder();
        for (int i = 0; i < config.progressBar.amount; i++) {
            progressBar.append(config.progressBar.incomplete);
        }
        return progressBar.toString();
    }

    @Override
    public String getProgressBar(UUID player) {
        var user = levelRepository.findById(player).orElse(null);
        if (user.getLevel() >= config.maxLevel) {
            StringBuilder progressBar = new StringBuilder();
            for (int i = 0; i < config.progressBar.amount; i++) {
                progressBar.append(config.progressBar.complete);
            }
            return progressBar.toString();
        }
        int nextLevelRequiredExperience = data.Levels.get(String.valueOf(user.getLevel() + 1));
        int currentLevelRequiredExperience = data.Levels.get(String.valueOf(user.getLevel()));
        int requiredExperienceInLevel = nextLevelRequiredExperience - currentLevelRequiredExperience;
        int experienceInLevel =  user.getExp() - currentLevelRequiredExperience;
        int bars = (int) Math.floor(experienceInLevel * config.progressBar.amount * 1.0D / requiredExperienceInLevel);
        StringBuilder progressBar = new StringBuilder();
        if (bars == 0) {
            progressBar.append(String.valueOf(config.progressBar.incomplete).repeat(Math.max(0, config.progressBar.amount)));
        } else if (bars == config.progressBar.amount) {
            progressBar.append(String.valueOf(config.progressBar.complete).repeat(Math.max(0, config.progressBar.amount)));
        } else {
            progressBar.append(String.valueOf(config.progressBar.complete).repeat(Math.max(0, bars)));
            progressBar.append(String.valueOf(config.progressBar.incomplete).repeat(Math.max(0, config.progressBar.amount - bars)));
        }
        return progressBar.toString();
    }

    @Override
    public int getProgressPercentage(UUID player) {
        var user = levelRepository.findById(player).orElse(null);
        if (user.getLevel() >= config.maxLevel) {
            return 100;
        }
        int nextLevelRequiredExperience = data.Levels.get(String.valueOf(user.getLevel() + 1));
        int currentLevelRequiredExperience = data.Levels.get(String.valueOf(user.getLevel()));
        int requiredExperienceInLevel = nextLevelRequiredExperience - currentLevelRequiredExperience;
        int experienceInLevel =  user.getExp() - currentLevelRequiredExperience;
        return (int) Math.floor(experienceInLevel * 100.0D / requiredExperienceInLevel);
    }

}
