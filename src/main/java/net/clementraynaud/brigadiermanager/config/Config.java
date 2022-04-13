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

package net.clementraynaud.brigadiermanager.config;

import net.clementraynaud.brigadiermanager.BrigadierManager;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Config {

    private static final String HIDDEN_COMMANDS_FIELD = "hidden-commands";
    private static final String IGNORE_OPS_FIELD = "ignore-ops";
    private static final String RETAIN_BRIGADIER_COMMAND_FOR_OPS_FIELD = "retain-brigadier-command-for-ops";
    private static Set<String> displayedCommands = new HashSet<>();

    private Config() {
    }

    public static Set<String> getDisplayedCommands() {
        return Config.displayedCommands;
    }

    public static void setDisplayedCommands(Set<String> commands) {
        Config.displayedCommands = commands;
    }

    public static Set<String> getHiddenCommands() {
        return new HashSet<>(BrigadierManager.getConfigFile().getStringList(Config.HIDDEN_COMMANDS_FIELD));
    }

    public static void setHiddenCommands(Set<String> commands) {
        BrigadierManager.getConfigFile().set(Config.HIDDEN_COMMANDS_FIELD, new ArrayList<>(commands));
        Config.save();
    }

    public static void clearHiddenCommands() {
        BrigadierManager.getConfigFile().set(Config.HIDDEN_COMMANDS_FIELD, null);
        Config.save();
    }

    public static void addHiddenCommand(String command) {
        Set<String> hiddenCommands = Config.getHiddenCommands();
        hiddenCommands.add(command);
        Config.setHiddenCommands(hiddenCommands);
    }

    public static void removeHiddenCommand(String command) {
        Set<String> hiddenCommands = Config.getHiddenCommands();
        hiddenCommands.remove(command);
        Config.setHiddenCommands(hiddenCommands);
    }

    public static boolean areOperatorsIgnored() {
        return BrigadierManager.getConfigFile().getBoolean(Config.IGNORE_OPS_FIELD);
    }

    public static void setOperatorsIgnored(boolean ignored) {
        BrigadierManager.getConfigFile().set(Config.IGNORE_OPS_FIELD, ignored);
        Config.save();
    }

    public static boolean isBrigadierCommandRetainedForOperators() {
        return BrigadierManager.getConfigFile().getBoolean(Config.RETAIN_BRIGADIER_COMMAND_FOR_OPS_FIELD);
    }

    public static void setBrigadierCommandRetainedForOperators(boolean retained) {
        BrigadierManager.getConfigFile().set(Config.RETAIN_BRIGADIER_COMMAND_FOR_OPS_FIELD, retained);
        Config.save();
    }

    private static void save() {
        BrigadierManager.getPlugin().saveConfig();
        BrigadierManager.getPlugin().getServer().getOnlinePlayers().forEach(Player::updateCommands);
    }
}
