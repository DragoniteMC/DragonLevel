package org.dragonitemc.level.api;

import java.util.UUID;

public interface LevelService {

    int getLevel(UUID player);

    int getExp(UUID player);

    UpdateResult setLevel(UUID player, int level);

    UpdateResult addLevel(UUID player, int level);

    UpdateResult removeLevel(UUID player, int level);

    UpdateResult setExp(UUID player, int exp);

    UpdateResult addExp(UUID player, int exp);

    UpdateResult removeExp(UUID player, int exp);

    String getProgressBar(UUID player);

    int getProgressPercentage(UUID player);

}
