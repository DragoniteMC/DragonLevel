package org.dragonitemc.level.manager;

import com.ericlam.mc.eld.services.ScheduleService;
import org.dragonitemc.level.DragonLevel;
import org.dragonitemc.level.api.AsyncLevelService;
import org.dragonitemc.level.api.LevelService;
import org.dragonitemc.level.api.UpdateResult;

import javax.inject.Inject;
import java.util.UUID;

public class AsyncLevelManager implements AsyncLevelService {

    @Inject
    private LevelService levelService;

    @Inject
    private ScheduleService scheduleService;

    @Inject
    private DragonLevel dragonlevel;

    @Override
    public ScheduleService.BukkitPromise<Integer> getLevel(UUID player) {
        return scheduleService.callAsync(dragonlevel, () -> levelService.getLevel(player));
    }

    @Override
    public ScheduleService.BukkitPromise<Integer> getExp(UUID player) {
        return scheduleService.callAsync(dragonlevel, () -> levelService.getExp(player));
    }

    @Override
    public ScheduleService.BukkitPromise<UpdateResult> setLevel(UUID player, int level) {
        return scheduleService.callAsync(dragonlevel, () -> levelService.setLevel(player, level));
    }

    @Override
    public ScheduleService.BukkitPromise<UpdateResult> addLevel(UUID player, int level) {
        return scheduleService.callAsync(dragonlevel, () -> levelService.addLevel(player, level));
    }

    @Override
    public ScheduleService.BukkitPromise<UpdateResult> removeLevel(UUID player, int level) {
        return scheduleService.callAsync(dragonlevel, () -> levelService.removeLevel(player, level));
    }

    @Override
    public ScheduleService.BukkitPromise<UpdateResult> setExp(UUID player, int exp) {
        return scheduleService.callAsync(dragonlevel, () -> levelService.setExp(player, exp));
    }

    @Override
    public ScheduleService.BukkitPromise<UpdateResult> addExp(UUID player, int exp) {
        return scheduleService.callAsync(dragonlevel, () -> levelService.addExp(player, exp));
    }

    @Override
    public ScheduleService.BukkitPromise<UpdateResult> removeExp(UUID player, int exp) {
        return scheduleService.callAsync(dragonlevel, () -> levelService.removeExp(player, exp));
    }

}
