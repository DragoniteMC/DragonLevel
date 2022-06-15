package org.dragonitemc.level.command;

import com.ericlam.mc.eld.annotations.CommandArg;
import com.ericlam.mc.eld.annotations.Commander;
import com.ericlam.mc.eld.bukkit.CommandNode;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.dragonitemc.level.api.LevelService;
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
    private LevelService levelService;

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

        message.getLang().getPureList("level.stats").forEach(message -> {
            sender.sendMessage(MiniMessage.miniMessage().deserialize(
                    message.replace("{LEVEL}", String.valueOf(levelService.getLevel(player.getUniqueId())))
                           .replace("{EXP}", String.valueOf(levelService.getExp(player.getUniqueId())))
                           .replace("{PROGRESS}", levelService.getProgressBar(player.getUniqueId()))
                           .replace("{PERCENT}", String.valueOf(levelService.getProgressPercentage(player.getUniqueId())))
            ));
        });
    }
}
