package org.dragonitemc.level.hook;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.dragonitemc.level.DragonLevel;
import org.dragonitemc.level.api.LevelService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;
import java.util.Locale;

public class LevelPlaceholder extends PlaceholderExpansion {

    @Inject
    private DragonLevel plugin;

    @Inject
    private LevelService levelService;

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        return switch (params.toLowerCase(Locale.ROOT)) {
            case "level" -> String.valueOf(levelService.getLevel(player.getUniqueId()));
            case "exp" -> String.valueOf(levelService.getExp(player.getUniqueId()));
            case "progressbar" -> levelService.getProgressBar(player.getUniqueId());
            case "percent" -> String.valueOf(levelService.getProgressPercentage(player.getUniqueId()));
            default -> null;
        };
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
