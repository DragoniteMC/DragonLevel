package org.dragonitemc.level.api;

import java.util.UUID;

public interface LevelService {

    int getLevel(UUID player);

    double getExp(UUID player);

    UpdateResult setLevel(UUID player, int level);

    UpdateResult addLevel(UUID player, int level);

    UpdateResult removeLevel(UUID player, int level);

    UpdateResult setExp(UUID player, double exp);

    UpdateResult addExp(UUID player, double exp);

    UpdateResult removeExp(UUID player, double exp);

}
