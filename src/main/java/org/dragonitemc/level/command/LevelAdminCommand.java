package org.dragonitemc.level.command;

import com.ericlam.mc.eld.annotations.Commander;
import com.ericlam.mc.eld.bukkit.CommandNode;
import org.bukkit.command.CommandSender;

@Commander(
        name = "leveladmin",
        description = "等級系統管理員指令",
        permission = "dragonlevel.admin"
)
public class LevelAdminCommand implements CommandNode {

    @Override
    public void execute(CommandSender sender) {

    }

}
