package org.dragonitemc.level.config;

import com.ericlam.mc.eld.annotations.Resource;
import com.ericlam.mc.eld.components.Configuration;

@Resource(locate = "config.yml")
public class DragonLevelConfig extends Configuration {

    public int defaultLevel;

    public int defaultExp;

    public int maxLevel;

    public ProgressBar progressBar;

    public static class ProgressBar {

        public int amount;

        public String complete;

        public String incomplete;

    }

}
