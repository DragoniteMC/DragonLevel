package org.dragonitemc.level.manager;

import org.dragonitemc.level.api.LevelService;
import org.dragonitemc.level.api.UpdateResult;
import org.dragonitemc.level.config.DragonLevelConfig;
import org.dragonitemc.level.db.Levels;
import org.dragonitemc.level.repository.LevelRepository;

import javax.inject.Inject;
import java.util.UUID;

public class LevelManager implements LevelService {

    @Inject
    private LevelRepository levelRepository;

    @Inject
    private DragonLevelConfig config;

    @Override
    public int getLevel(UUID player) {
        return levelRepository.findById(player).map(Levels::getLevel).orElse(config.defaultLevel);
    }

    @Override
    public double getExp(UUID player) {
        return levelRepository.findById(player).map(Levels::getExp).orElse(config.defaultExp);
    }

    @Override
    public UpdateResult setLevel(UUID player, int level) {
        var user = levelRepository.findById(player).orElseGet(() -> {
            var levels = new Levels();
            levels.setLevel(1);
            levels.setExp(0.0);
            levels.setId(player);
            return levels;
        });
        user.setLevel(level);
        levelRepository.save(user);

        return UpdateResult.SUCCESS;
    }

    @Override
    public UpdateResult addLevel(UUID player, int level) {
        var user = levelRepository.findById(player).orElseGet(() -> {
            var levels = new Levels();
            levels.setLevel(1);
            levels.setExp(0.0);
            levels.setId(player);
            return levels;
        });
        var userLevel = user.getLevel();
        user.setLevel(userLevel + level);
        levelRepository.save(user);

        return UpdateResult.SUCCESS;
    }

    @Override
    public UpdateResult removeLevel(UUID player, int level) {
        var opt = levelRepository.findById(player);
        if (opt.isEmpty()) {
            return UpdateResult.PLAYER_NOT_EXIST;
        }
        var user = opt.get();
        var userLevel = user.getLevel();
        user.setLevel(userLevel - level);
        levelRepository.save(user);

        return UpdateResult.SUCCESS;
    }

    @Override
    public UpdateResult setExp(UUID player, double exp) {
        var user = levelRepository.findById(player).orElseGet(() -> {
            var levels = new Levels();
            levels.setLevel(1);
            levels.setExp(0.0);
            levels.setId(player);
            return levels;
        });
        user.setExp(exp);
        levelRepository.save(user);

        return UpdateResult.SUCCESS;
    }

    @Override
    public UpdateResult addExp(UUID player, double exp) {
        var user = levelRepository.findById(player).orElseGet(() -> {
            var levels = new Levels();
            levels.setLevel(1);
            levels.setExp(0.0);
            levels.setId(player);
            return levels;
        });
        var userExp = user.getExp();
        user.setExp(userExp + exp);
        levelRepository.save(user);

        return UpdateResult.SUCCESS;
    }

    @Override
    public UpdateResult removeExp(UUID player, double exp) {
        var opt = levelRepository.findById(player);
        if (opt.isEmpty()) {
            return UpdateResult.PLAYER_NOT_EXIST;
        }
        var user = opt.get();
        var userExp = user.getExp();
        user.setExp(userExp - exp);
        levelRepository.save(user);

        return UpdateResult.SUCCESS;
    }
}
