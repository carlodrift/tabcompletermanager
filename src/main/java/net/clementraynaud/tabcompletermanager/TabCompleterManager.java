/*
 * Copyright 2020, 2021, 2022 Clément "carlodrift" Raynaud and contributors
 *
 * This file is part of TabCompleterManager.
 *
 * TabCompleterManager is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TabCompleterManager is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with TabCompleterManager.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.clementraynaud.tabcompletermanager;

import net.clementraynaud.tabcompletermanager.commands.tcm.TcmCommand;
import net.clementraynaud.tabcompletermanager.listeners.PlayerListener;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class TabCompleterManager extends JavaPlugin {

    public static final String PREFIX = ChatColor.LIGHT_PURPLE + "TabCompleterManager " + ChatColor.DARK_GRAY + "• " + ChatColor.RESET;
    private static final long TICKS_BETWEEN_VERSION_CHECKING = 720000L;
    private static final long TICKS_BEFORE_VERSION_CHECKING = 6000L;
    private static final String PLUGIN_DOWNLOAD_URL = "https://www.spigotmc.org/resources/tabcompletermanager.83140/";
    private static FileConfiguration config;
    private static JavaPlugin plugin;

    public static FileConfiguration getConfigFile() {
        return TabCompleterManager.config;
    }

    public static void setConfigFile(FileConfiguration config) {
        TabCompleterManager.config = config;
    }

    public static JavaPlugin getPlugin() {
        return TabCompleterManager.plugin;
    }

    public static void setPlugin(JavaPlugin plugin) {
        TabCompleterManager.plugin = plugin;
    }

    @Override
    public void onEnable() {
        TabCompleterManager.setPlugin(this);
        TabCompleterManager.setConfigFile(this.getConfig());
        this.saveDefaultConfig();

        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);

        TcmCommand tcmCommand = new TcmCommand();
        this.getCommand("tcm").setExecutor(tcmCommand);
        this.getCommand("tcm").setTabCompleter(tcmCommand);

        new Metrics(this, 16021);

        Updater updater = new Updater(this, this.getFile().getAbsolutePath(), this.getName(), TabCompleterManager.PLUGIN_DOWNLOAD_URL);
        this.getServer().getScheduler().runTaskTimer(this, updater::checkVersion, TabCompleterManager.TICKS_BEFORE_VERSION_CHECKING, TabCompleterManager.TICKS_BETWEEN_VERSION_CHECKING);
    }
}
