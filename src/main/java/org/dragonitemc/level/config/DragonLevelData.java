package org.dragonitemc.level.config;

import com.ericlam.mc.eld.annotations.Resource;
import com.ericlam.mc.eld.components.Configuration;

import java.util.Map;

@Resource(locate = "levels.yml")
public class DragonLevelData extends Configuration {

    public Map<String, Integer> Levels;

}
