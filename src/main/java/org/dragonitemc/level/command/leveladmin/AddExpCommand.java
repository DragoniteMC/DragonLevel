package org.dragonitemc.level.command.leveladmin;

import com.ericlam.mc.eld.annotations.CommandArg;
import com.ericlam.mc.eld.annotations.Commander;
import com.ericlam.mc.eld.bukkit.CommandNode;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.dragonitemc.level.api.AsyncLevelService;
import org.dragonitemc.level.config.DragonLevelMessage;

import javax.inject.Inject;

@Commander(
        name = "addexp",
        description = "增加玩家經驗值",
        permission = "dragonlevel.admin"
)
public class AddExpCommand implements CommandNode {

    @Inject
    private DragonLevelMessage message;

    @Inject
    private AsyncLevelService levelService;

    @CommandArg(order = 1, labels = "玩家")
    private OfflinePlayer player;

    @CommandArg(order = 2, labels = "經驗值")
    private int exp;

    @Override
    public void execute(CommandSender sender) {

        levelService.addExp(player.getUniqueId(), exp)
                .thenRunSync(result -> sender.sendMessage(MiniMessage.miniMessage().deserialize(message.getResultMessage(result))))
                .joinWithCatch(ex -> sender.sendMessage(MiniMessage.miniMessage().deserialize(message.getErrorMessage(ex))));

    }

}
