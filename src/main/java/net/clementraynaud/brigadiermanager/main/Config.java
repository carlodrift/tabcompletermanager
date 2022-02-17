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

package net.clementraynaud.brigadiermanager.main;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static net.clementraynaud.brigadiermanager.BrigadierManager.getInstance;

public abstract class Config {
    private static final String HIDDEN_COMMANDS_FIELD = "hidden-commands";
    private static final String IGNORE_OPS_FIELD = "ignore-ops";
    private static final String RETAIN_BRIGADIER_COMMAND_FOR_OPS_FIELD = "retain-brigadier-command-for-ops";
    private static List<String> displayedCommands = new ArrayList<>();

    private Config() {
    }

    public static Collection<String> getDisplayedCommands() {
        return displayedCommands;
    }

    public static void setDisplayedCommands(List<String> commands) {
        displayedCommands = commands;
    }

    public static Collection<String> getHiddenCommands() {
        return getInstance().getConfig().getStringList(HIDDEN_COMMANDS_FIELD);
    }

    public static void setHiddenCommands(Collection<String> commands) {
        getInstance().getConfig().set(HIDDEN_COMMANDS_FIELD, commands);
    }

    public static void clearHiddenCommands() {
        getInstance().getConfig().set(HIDDEN_COMMANDS_FIELD, null);
    }

    public static void addHiddenCommand(String command) {
        Collection<String> hiddenCommands = getHiddenCommands();
        hiddenCommands.add(command);
        setHiddenCommands(hiddenCommands);
    }

    public static void removeHiddenCommand(String command) {
        Collection<String> hiddenCommands = getHiddenCommands();
        hiddenCommands.remove(command);
        setHiddenCommands(hiddenCommands);
    }

    public static boolean areOperatorsIgnored() {
        return getInstance().getConfig().getBoolean(IGNORE_OPS_FIELD);
    }

    public static void setOperatorsIgnored(boolean ignored) {
        getInstance().getConfig().set(IGNORE_OPS_FIELD, ignored);
    }

    public static boolean isBrigadierCommandRetainedForOperators() {
        return getInstance().getConfig().getBoolean(RETAIN_BRIGADIER_COMMAND_FOR_OPS_FIELD);
    }

    public static void setBrigadierCommandRetainedForOperators(boolean retained) {
        getInstance().getConfig().set(RETAIN_BRIGADIER_COMMAND_FOR_OPS_FIELD, retained);
    }

    public static void save() {
        getInstance().saveConfig();
        getInstance().getServer().getOnlinePlayers().forEach(Player::updateCommands);
    }
}
