package org.dragonitemc.level.command;

import com.ericlam.mc.eld.annotations.CommandArg;
import com.ericlam.mc.eld.annotations.Commander;
import com.ericlam.mc.eld.bukkit.CommandNode;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.dragonitemc.level.api.AsyncLevelService;
import org.dragonitemc.level.config.DragonLevelMessage;

import javax.inject.Inject;

@Commander(
        name = "level",
        alias = "lv",
        description = "查詢等級"
)
public class LevelCommand implements CommandNode {

    @Inject
    private DragonLevelMessage message;

    @Inject
    private AsyncLevelService levelService;

    @CommandArg(order = 1, optional = true, labels = "玩家名稱")
    private OfflinePlayer player;

    @Override
    public void execute(CommandSender sender) {
        if (player == null) {
            if (sender instanceof ConsoleCommandSender) {
                sender.sendMessage(message.getLang().get("no-player-arg-in-console"));
                return;
            }
            player = (OfflinePlayer) sender;
        }

        levelService.getLevel(player.getUniqueId())
                .thenRunSync(level -> sender.sendMessage(message.getLang().get("level", player.getName(), level)))
                .joinWithCatch(ex -> sender.sendMessage(message.getErrorMessage(ex)));
    }
}
