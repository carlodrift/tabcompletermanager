/*
 * Copyright 2022 Clément "carlodrift" Raynaud and contributors
 *
 * This file is part of BrigadierManager.
 *
 * BrigadierManager is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BrigadierManager is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with BrigadierManager.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.clementraynaud.brigadiermanager;

import net.clementraynaud.brigadiermanager.commands.brigadier.BrigadierCommand;
import net.clementraynaud.brigadiermanager.listeners.PlayerCommandSendListener;
import net.clementraynaud.brigadiermanager.util.UpdateUtil;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class BrigadierManager extends JavaPlugin {

    public static final String PREFIX = ChatColor.LIGHT_PURPLE + "BrigadierManager " + ChatColor.DARK_GRAY + "• " + ChatColor.RESET;
    private static FileConfiguration config;
    private static JavaPlugin plugin;

    public static FileConfiguration getConfigFile() {
        return BrigadierManager.config;
    }

    public static void setConfigFile(FileConfiguration config) {
        BrigadierManager.config = config;
    }

    public static JavaPlugin getPlugin() {
        return BrigadierManager.plugin;
    }

    public static void setPlugin(JavaPlugin plugin) {
        BrigadierManager.plugin = plugin;
    }

    @Override
    public void onEnable() {
        BrigadierManager.setPlugin(this);
        BrigadierManager.setConfigFile(this.getConfig());
        this.saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(new PlayerCommandSendListener(), this);
        BrigadierCommand brigadierCommand = new BrigadierCommand();
        this.getCommand("brigadier").setExecutor(brigadierCommand);
        this.getCommand("brigadier").setTabCompleter(brigadierCommand);
        new Metrics(this, 13996);
        new UpdateUtil(this, 83140).getVersion(version -> {
            if (!this.getDescription().getVersion().equals(version)) {
                this.getLogger().warning("You are using an outdated version (" + this.getDescription().getVersion() + "). Download the latest version (" + version + ") here: https://spigotmc.org/resources/brigadiermanager.83140.");
            }
        });
    }
}
