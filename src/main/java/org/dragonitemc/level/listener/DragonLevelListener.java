package org.dragonitemc.level.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.dragonitemc.level.config.DragonLevelConfig;
import org.dragonitemc.level.db.Levels;
import org.dragonitemc.level.repository.LevelRepository;

import javax.inject.Inject;

public class DragonLevelListener implements Listener {

    @Inject
    private LevelRepository levelRepository;

    @Inject
    private DragonLevelConfig config;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        var player = e.getPlayer();
        var user = levelRepository.findById(player.getUniqueId()).orElse(null);
        if (user == null) {
            Levels levels = new Levels();
            levels.setId(player.getUniqueId());
            levels.setLevel(config.defaultLevel);
            levels.setExp(config.defaultExp);
            levelRepository.save(levels);
        }
    }

}
