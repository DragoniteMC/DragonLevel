package org.dragonitemc.level.listener;

import com.ericlam.mc.eld.services.ScheduleService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.dragonitemc.level.DragonLevel;
import org.dragonitemc.level.config.DragonLevelConfig;
import org.dragonitemc.level.db.Levels;
import org.dragonitemc.level.repository.LevelRepository;

import javax.inject.Inject;

public class DragonLevelListener implements Listener {

    @Inject
    private LevelRepository levelRepository;

    @Inject
    private ScheduleService scheduleService;

    @Inject
    private DragonLevel dragonlevel;

    @Inject
    private DragonLevelConfig config;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        var player = e.getPlayer();

        scheduleService.runAsync(dragonlevel, () -> {

            var user = levelRepository.findById(player.getUniqueId()).orElseGet(() -> {
                var level = new Levels();
                level.setId(player.getUniqueId());
                level.setLevel(config.defaultLevel);
                level.setExp(config.defaultExp);
                return level;
            });

            if (user.getId().equals(player.getUniqueId())) {
                return;
            }

            levelRepository.save(user);
            dragonlevel.getLogger().info("Saved user " + user.getId() + " to database");

        }).joinWithCatch(ex -> {
            dragonlevel.getLogger().warning(ex.getMessage());
            ex.printStackTrace();
        });

    }

}
