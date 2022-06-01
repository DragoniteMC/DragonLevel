package org.dragonitemc.level.api;

import com.ericlam.mc.eld.services.ScheduleService;

import java.util.UUID;

public interface AsyncLevelService {

    ScheduleService.BukkitPromise<Integer> getLevel(UUID player);

    ScheduleService.BukkitPromise<Double> getExp(UUID player);

    ScheduleService.BukkitPromise<UpdateResult> setLevel(UUID player, int level);

    ScheduleService.BukkitPromise<UpdateResult> addLevel(UUID player, int level);

    ScheduleService.BukkitPromise<UpdateResult> removeLevel(UUID player, int level);

    ScheduleService.BukkitPromise<UpdateResult> setExp(UUID player, double exp);

    ScheduleService.BukkitPromise<UpdateResult> addExp(UUID player, double exp);

    ScheduleService.BukkitPromise<UpdateResult> removeExp(UUID player, double exp);

}
