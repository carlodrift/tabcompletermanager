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
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TcmCommand implements CommandExecutor, TabCompleter {

    public static void sendUsage(CommandSender sender) {
        Set<String> list = TcmOption.getList();
        String options = list.size() == 1
                ? String.join("/", list)
                : "<" + String.join("/", list) + ">";
        sender.sendMessage(TabCompleterManager.PREFIX + ChatColor.GRAY +
                "To use this command, type \"" + ChatColor.YELLOW + "/tcm " + options + " [command]" + ChatColor.GRAY + "\"."
        );
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            MessageUtil.sendErrorMessage(sender, "This command is only executable by players.");
            return true;
        }
        if (sender.isOp()) {
            if (args.length == 0 || TcmOption.getOption(args[0]) == null) {
                TcmCommand.sendUsage(sender);
                return true;
            }
            TcmOption.getOption(args[0]).execute(sender, args.length == 1 ? "" : args[1]);
        } else {
            MessageUtil.sendErrorMessage(sender, "This command is only executable by operators.");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            args[0] = args[0].toLowerCase();
            return TcmOption.getList().stream()
                    .filter(option -> option.toLowerCase().startsWith(args[0]))
                    .collect(Collectors.toList());
        }
        if (args.length == 2) {
            args[1] = args[1].toLowerCase();
            if (args[0].equalsIgnoreCase(TcmOption.HIDE.toString())) {
                return Config.getDisplayedCommands().stream()
                        .filter(cmd -> cmd.toLowerCase().startsWith(args[1]))
                        .collect(Collectors.toList());
            }
            if (args[0].equalsIgnoreCase(TcmOption.UNHIDE.toString())) {
                return Config.getHiddenCommands().stream()
                        .filter(cmd -> cmd.toLowerCase().startsWith(args[1]))
                        .collect(Collectors.toList());
            }
        }
        return Collections.emptyList();
    }
}
