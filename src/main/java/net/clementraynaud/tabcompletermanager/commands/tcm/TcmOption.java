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

package net.clementraynaud.tabcompletermanager.commands.tcm;

import net.clementraynaud.tabcompletermanager.TabCompleterManager;
import net.clementraynaud.tabcompletermanager.config.Config;
import net.clementraynaud.tabcompletermanager.util.MessageUtil;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.text.CaseUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum TcmOption {
    DISPLAYED {
        @Override
        public void execute(CommandSender sender, String arg) {
            TextComponent message = new TextComponent(TabCompleterManager.PREFIX + ChatColor.GRAY + "Move your mouse over this message to see the displayed commands.");
            MessageUtil.setHoverEvent(message, Config.getDisplayedCommands().toString());
            sender.spigot().sendMessage(message);
        }
    },
    HIDDEN {
        @Override
        public void execute(CommandSender sender, String arg) {
            TextComponent message = new TextComponent(TabCompleterManager.PREFIX + ChatColor.GRAY + "Move your mouse over this message to see the hidden commands.");
            MessageUtil.setHoverEvent(message, Config.getHiddenCommands().toString());
            sender.spigot().sendMessage(message);
        }
    },
    HIDE {
        @Override
        public void execute(CommandSender sender, String arg) {
            if (arg.isEmpty()) {
                TcmCommand.sendUsage(sender);
                return;
            }
            if (!Config.getDisplayedCommands().contains(arg)) {
                MessageUtil.sendErrorMessage(sender, "/" + arg + " is already hidden or does not exist.");
                return;
            }
            MessageUtil.sendValidationMessage(sender, "/" + arg + " is now hidden.");
            Config.addHiddenCommand(arg);
        }
    },
    HIDE_ALL {
        @Override
        public void execute(CommandSender sender, String arg) {
            if (Config.getDisplayedCommands().isEmpty()) {
                MessageUtil.sendErrorMessage(sender, "There are no displayed commands.");
                return;
            }
            MessageUtil.sendValidationMessage(sender, "All displayed commands are now hidden.");
            Config.setHiddenCommands(Stream.concat(
                    Config.getDisplayedCommands().stream(),
                    Config.getHiddenCommands().stream()).collect(Collectors.toSet()));
        }
    },
    IGNORE_OPS {
        @Override
        public void execute(CommandSender sender, String arg) {
            if (Config.areOperatorsIgnored()) {
                MessageUtil.sendValidationMessage(sender, "Operators are now affected by hidden commands.");
            } else {
                MessageUtil.sendValidationMessage(sender, "Operators are no longer affected by hidden commands.");
            }
            Config.setOperatorsIgnored(!Config.areOperatorsIgnored());
        }
    },
    UNHIDE {
        @Override
        public void execute(CommandSender sender, String arg) {
            if (arg.isEmpty()) {
                TcmCommand.sendUsage(sender);
                return;
            }
            if (!Config.getHiddenCommands().contains(arg)) {
                String permissionIndication = "";
                if (TcmOption.permissionIndication(arg) != null) {
                    permissionIndication = String.format(" Players without permission %s cannot see it though.", TcmOption.permissionIndication(arg));
                }
                MessageUtil.sendErrorMessage(sender, "/" + arg + " is not hidden." + permissionIndication);
                return;
            }
            String permissionIndication = "";
            if (TcmOption.permissionIndication(arg) != null) {
                permissionIndication = " to players with the permission " + TcmOption.permissionIndication(arg);
            }
            MessageUtil.sendValidationMessage(sender, "/" + arg + " is now displayed" + permissionIndication + ".");
            Config.removeHiddenCommand(arg);
        }
    },
    UNHIDE_ALL {
        @Override
        public void execute(CommandSender sender, String arg) {
            if (Config.getHiddenCommands().isEmpty()) {
                MessageUtil.sendErrorMessage(sender, "There are no hidden commands.");
                return;
            }
            MessageUtil.sendValidationMessage(sender, "All hidden commands are now displayed.");
            Config.clearHiddenCommands();
        }
    };

    public static Set<String> getList() {
        return Stream.of(TcmOption.values())
                .map(Enum::toString)
                .collect(Collectors.toSet());
    }

    public static TcmOption getOption(String option) {
        return Stream.of(TcmOption.values())
                .filter(opt -> opt.toString().equalsIgnoreCase(option))
                .findFirst().orElse(null);
    }

    private static String permissionIndication(String arg) {
        PluginCommand command = TabCompleterManager.getPlugin().getCommand(arg);
        if (command != null) {
            return command.getPermission();
        }
        return null;
    }

    public abstract void execute(CommandSender sender, String arg);

    @Override
    public String toString() {
        return CaseUtils.toCamelCase(super.toString(), false, '_');
    }
}
