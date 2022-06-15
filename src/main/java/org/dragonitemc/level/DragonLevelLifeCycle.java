package org.dragonitemc.level;

import com.ericlam.mc.eld.bukkit.ELDLifeCycle;
import org.bukkit.plugin.java.JavaPlugin;
import org.dragonitemc.dragonshop.api.ShopTaskService;
import org.dragonitemc.level.hook.LevelPlaceholder;
import org.dragonitemc.level.hook.dshop.ExpReward;
import org.dragonitemc.level.hook.dshop.LevelReward;
import org.dragonitemc.level.hook.dshop.RequiredExpCondition;
import org.dragonitemc.level.hook.dshop.RequiredLevelCondition;

import javax.inject.Inject;

public class DragonLevelLifeCycle implements ELDLifeCycle {

    @Inject
    private LevelPlaceholder levelPlaceholder;

    @Inject
    private ShopTaskService taskService;

    @Inject
    private LevelReward levelReward;

    @Inject
    private ExpReward expReward;

    @Inject
    private RequiredLevelCondition requiredLevelCondition;

    @Inject
    private RequiredExpCondition requiredExpCondition;

    @Override
    public void onEnable(JavaPlugin plugin) {
        taskService.addRewardTask(levelReward);
        taskService.addRewardTask(expReward);
        taskService.addCondition(requiredLevelCondition);
        taskService.addCondition(requiredExpCondition);
        levelPlaceholder.register();
    }

    @Override
    public void onDisable(JavaPlugin plugin) {

    }

}
