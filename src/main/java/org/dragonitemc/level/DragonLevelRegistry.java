package org.dragonitemc.level;

import com.ericlam.mc.eld.bukkit.CommandNode;
import com.ericlam.mc.eld.bukkit.ComponentsRegistry;
import com.ericlam.mc.eld.registration.CommandRegistry;
import com.ericlam.mc.eld.registration.ListenerRegistry;
import org.bukkit.event.Listener;
import org.dragonitemc.level.command.LevelAdminCommand;
import org.dragonitemc.level.command.LevelCommand;
import org.dragonitemc.level.command.leveladmin.*;

public class DragonLevelRegistry implements ComponentsRegistry {

    @Override
    public void registerCommand(CommandRegistry<CommandNode> commandRegistry) {

        commandRegistry.command(LevelCommand.class);

        commandRegistry.command(LevelAdminCommand.class, sub -> {

            sub.command(AddExpCommand.class);

            sub.command(RemoveExpCommand.class);

            sub.command(SetExpCommand.class);

            sub.command(AddLevelCommand.class);

            sub.command(RemoveLevelCommand.class);

            sub.command(SetLevelCommand.class);

        });

    }

    @Override
    public void registerListeners(ListenerRegistry<Listener> listenerRegistry) {

    }
}
