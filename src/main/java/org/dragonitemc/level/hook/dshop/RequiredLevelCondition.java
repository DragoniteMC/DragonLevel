package org.dragonitemc.level.hook.dshop;

import org.bukkit.entity.Player;
import org.dragonitemc.dragonshop.api.Condition;
import org.dragonitemc.level.api.LevelService;

import javax.inject.Inject;

public class RequiredLevelCondition extends Condition<Integer> {

    @Inject
    private LevelService levelService;

    public RequiredLevelCondition() {
        super("required-level");
    }

    @Override
    public boolean isMatched(Integer requiredLevel, Player player) {
        var playerLevel = levelService.getLevel(player.getUniqueId());
        return playerLevel >= requiredLevel;
    }

}
