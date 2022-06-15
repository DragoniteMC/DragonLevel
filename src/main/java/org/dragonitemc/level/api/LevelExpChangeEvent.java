package org.dragonitemc.level.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class LevelExpChangeEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private Player player;

    private int oldLevel;

    private int newLevel;

    private int oldExp;

    private int newExp;

    public LevelExpChangeEvent(Player player, int oldLevel, int newLevel, int oldExp, int newExp) {
        super(true);
        this.player = player;
        this.oldLevel = oldLevel;
        this.newLevel = newLevel;
        this.oldExp = oldExp;
        this.newExp = newExp;
    }

    public Player getPlayer() {
        return player;
    }

    public int getOldLevel() {
        return oldLevel;
    }

    public int getNewLevel() {
        return newLevel;
    }

    public int getOldExp() {
        return oldExp;
    }

    public int getNewExp() {
        return newExp;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
