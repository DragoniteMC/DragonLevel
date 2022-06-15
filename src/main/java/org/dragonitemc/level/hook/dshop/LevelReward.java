package org.dragonitemc.level.hook.dshop;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.dragonitemc.dragonshop.api.AsyncRewardTask;
import org.dragonitemc.level.api.LevelService;
import org.dragonitemc.level.config.DragonLevelMessage;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

public class LevelReward extends AsyncRewardTask<Integer> {

    @Inject
    private LevelService levelService;

    @Inject
    private DragonLevelMessage message;

    public LevelReward() {
        super("level");
    }

    @Override
    public CompletableFuture<Void> giveRewardAsync(Integer level, Player player) {
        return CompletableFuture.runAsync(() -> {
            var result = levelService.addLevel(player.getUniqueId(), level);
            player.sendMessage(MiniMessage.miniMessage().deserialize(message.getResultMessage(result)));
        });
    }

}
