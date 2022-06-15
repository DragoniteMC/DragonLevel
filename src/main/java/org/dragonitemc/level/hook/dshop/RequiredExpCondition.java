package org.dragonitemc.level.hook.dshop;

import org.bukkit.entity.Player;
import org.dragonitemc.dragonshop.api.Condition;
import org.dragonitemc.level.api.LevelService;

import javax.inject.Inject;

public class RequiredExpCondition extends Condition<Integer> {

    @Inject
    private LevelService levelService;

    public RequiredExpCondition() {
        super("required-exp");
    }

    @Override
    public boolean isMatched(Integer exp, Player player) {
        var playerExp = levelService.getExp(player.getUniqueId());
        return playerExp >= exp;
    }
}
