package org.dragonitemc.level.hook;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.dragonitemc.level.DragonLevel;
import org.dragonitemc.level.api.AsyncLevelService;
import org.dragonitemc.level.api.LevelExpChangeEvent;
import org.dragonitemc.level.api.LevelService;
import org.dragonitemc.level.config.DragonLevelConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class LevelPlaceholder extends PlaceholderExpansion implements Listener {

    @Inject
    private DragonLevel plugin;

    @Inject
    private DragonLevelConfig config;

    @Inject
    private AsyncLevelService levelService;

    private final Map<UUID, Integer> levelCache = new ConcurrentHashMap<>();

    private final Map<UUID, Integer> expCache = new ConcurrentHashMap<>();

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        return switch (params.toLowerCase(Locale.ROOT)) {
            case "level" -> String.valueOf(this.levelCache.getOrDefault(player.getUniqueId(), config.defaultLevel));
            case "exp" -> String.valueOf(this.expCache.getOrDefault(player.getUniqueId(), config.defaultExp));
            case "progressbar" -> levelService.getProgressBar(player.getUniqueId());
            case "percent" -> String.valueOf(levelService.getProgressPercentage(player.getUniqueId()));
            default -> null;
        };
    }

    @EventHandler
    public void onLevelExpChange(LevelExpChangeEvent event) {
        levelService.getLevel(event.getPlayer().getUniqueId()).thenRunSync(level -> {
            this.levelCache.put(event.getPlayer().getUniqueId(), level);
            plugin.getLogger().info("Level cache updated: " + event.getPlayer().getUniqueId());
        }).joinWithCatch(ex -> {
            plugin.getLogger().warning("Failed to update level cache: "+ex.getMessage());
            ex.printStackTrace();
        });
        levelService.getExp(event.getPlayer().getUniqueId()).thenRunSync(exp -> {
            this.expCache.put(event.getPlayer().getUniqueId(), exp);
            plugin.getLogger().info("Exp cache updated: " + event.getPlayer().getUniqueId());
        }).joinWithCatch(ex -> {
            plugin.getLogger().warning("Failed to update exp cache: " + ex.getMessage());
            ex.printStackTrace();
        });
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        levelService.getLevel(e.getPlayer().getUniqueId()).thenRunSync(level -> {
            this.levelCache.put(e.getPlayer().getUniqueId(), level);
            plugin.getLogger().info("Level cache updated: " + e.getPlayer().getUniqueId());
        }).joinWithCatch(ex -> {
            plugin.getLogger().warning("Failed to update level cache: " + ex.getMessage());
            ex.printStackTrace();
        });
        levelService.getExp(e.getPlayer().getUniqueId()).thenRunSync(exp -> {
            this.expCache.put(e.getPlayer().getUniqueId(), exp);
            plugin.getLogger().info("Exp cache updated: " + e.getPlayer().getUniqueId());
        }).joinWithCatch(ex -> {
            plugin.getLogger().warning("Failed to update exp cache: " + ex.getMessage());
            ex.printStackTrace();
        });
    }

    @Override
    public @NotNull String getIdentifier() {
        return plugin.getName().toLowerCase(Locale.ROOT);
    }

    @Override
    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

}
