package org.dragonitemc.level.command.leveladmin;

import com.ericlam.mc.eld.annotations.CommandArg;
import com.ericlam.mc.eld.annotations.Commander;
import com.ericlam.mc.eld.bukkit.CommandNode;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.dragonitemc.level.api.AsyncLevelService;
import org.dragonitemc.level.config.DragonLevelMessage;

import javax.inject.Inject;

@Commander(
        name = "addlevel",
        description = "增加玩家等級",
        permission = "dragonlevel.admin"
)
public class AddLevelCommand implements CommandNode {

    @Inject
    private DragonLevelMessage message;

    @Inject
    private AsyncLevelService levelService;

    @CommandArg(order = 1, labels = "玩家")
    private OfflinePlayer player;

    @CommandArg(order = 2, labels = "等級")
    private int level;

    @Override
    public void execute(CommandSender sender) {

        levelService.addLevel(player.getUniqueId(), level)
                .thenRunSync(result -> sender.sendMessage(message.getResultMessage(result)))
                .joinWithCatch(ex -> sender.sendMessage(message.getErrorMessage(ex)));

    }
}
