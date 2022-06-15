package org.dragonitemc.level.command.leveladmin;

import com.ericlam.mc.eld.annotations.Commander;
import com.ericlam.mc.eld.bukkit.CommandNode;
import org.bukkit.command.CommandSender;
import org.dragonitemc.level.config.DragonLevelConfig;
import org.dragonitemc.level.config.DragonLevelMessage;

import javax.inject.Inject;

@Commander(
        name = "reload",
        description = "重載配置文件",
        permission = "dragonlevel.admin"
)
public class ReloadCommand implements CommandNode {

    @Inject
    private DragonLevelConfig config;

    @Inject
    private DragonLevelMessage message;

    @Override
    public void execute(CommandSender commandSender) {
        config.getController().reload();
        message.getController().reload();
        commandSender.sendMessage("§aReload success!");
    }

}
