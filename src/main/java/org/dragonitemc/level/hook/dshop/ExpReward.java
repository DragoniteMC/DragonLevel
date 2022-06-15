package org.dragonitemc.level.hook.dshop;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.dragonitemc.dragonshop.api.AsyncRewardTask;
import org.dragonitemc.level.api.LevelService;
import org.dragonitemc.level.config.DragonLevelMessage;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

public class ExpReward extends AsyncRewardTask<Integer> {

    @Inject
    private LevelService levelService;

    @Inject
    private DragonLevelMessage message;

    public ExpReward() {
        super("exp");
    }

    @Override
    public CompletableFuture<Void> giveRewardAsync(Integer exp, Player player) {
        return CompletableFuture.runAsync(() -> {
            var result = levelService.addExp(player.getUniqueId(), exp);
            player.sendMessage(MiniMessage.miniMessage().deserialize(message.getResultMessage(result)));
        });
    }

}
