package org.dragonitemc.level.config;

import com.ericlam.mc.eld.annotations.Resource;
import com.ericlam.mc.eld.components.Configuration;

@Resource(locate = "config.yml")
public class DragonLevelConfig extends Configuration {

    public int defaultLevel;

    public double defaultExp;

}
