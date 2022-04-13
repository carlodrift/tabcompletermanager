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

package net.clementraynaud.brigadiermanager.commands.brigadier;

import net.clementraynaud.brigadiermanager.BrigadierManager;
import net.clementraynaud.brigadiermanager.config.Config;
import net.clementraynaud.brigadiermanager.util.MessageUtil;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.text.CaseUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum BrigadierOption {
    DISPLAYED {
        @Override
        public void execute(CommandSender sender, String arg) {
            TextComponent message = new TextComponent(BrigadierManager.PREFIX + ChatColor.GRAY + "Move your mouse over this message to see the displayed commands.");
            MessageUtil.setHoverEvent(message, Config.getDisplayedCommands().toString());
            sender.spigot().sendMessage(message);
        }
    },
    HIDDEN {
        @Override
        public void execute(CommandSender sender, String arg) {
            TextComponent message = new TextComponent(BrigadierManager.PREFIX + ChatColor.GRAY + "Move your mouse over this message to see the hidden commands.");
            MessageUtil.setHoverEvent(message, Config.getHiddenCommands().toString());
            sender.spigot().sendMessage(message);
        }
    },
    HIDE {
        @Override
        public void execute(CommandSender sender, String arg) {
            if (arg.isEmpty()) {
                BrigadierCommand.sendUsage(sender);
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
    RETAIN_BRIGADIER_COMMAND_FOR_OPS {
        @Override
        public void execute(CommandSender sender, String arg) {
            if (Config.isBrigadierCommandRetainedForOperators()) {
                MessageUtil.sendValidationMessage(sender, "/brigadier is no longer retained for operators.");
            } else {
                MessageUtil.sendValidationMessage(sender, "/brigadier is now retained for operators.");
            }
            Config.setBrigadierCommandRetainedForOperators(!Config.isBrigadierCommandRetainedForOperators());
        }
    },
    UNHIDE {
        @Override
        public void execute(CommandSender sender, String arg) {
            if (arg.isEmpty()) {
                BrigadierCommand.sendUsage(sender);
                return;
            }
            if (!Config.getHiddenCommands().contains(arg)) {
                MessageUtil.sendErrorMessage(sender, "/" + arg + " is not hidden.");
                return;
            }
            MessageUtil.sendValidationMessage(sender, "/" + arg + " is now displayed.");
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
        return Stream.of(BrigadierOption.values())
                .map(Enum::toString)
                .collect(Collectors.toSet());
    }

    public static BrigadierOption getOption(String option) {
        return Stream.of(BrigadierOption.values())
                .filter(o -> o.toString().equalsIgnoreCase(option))
                .findFirst().orElse(null);
    }

    public abstract void execute(CommandSender sender, String arg);

    @Override
    public String toString() {
        return CaseUtils.toCamelCase(super.toString(), false, '_');
    }
}
