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
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class BrigadierCommand implements CommandExecutor, TabCompleter {

    public static void sendUsage(CommandSender sender) {
        sender.sendMessage(BrigadierManager.PREFIX + ChatColor.GRAY + "Usage: /brigadier <option> [command]");
        sender.sendMessage(ChatColor.GRAY + "Options: " + String.join(", ", BrigadierOption.getList()));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            MessageUtil.sendErrorMessage(sender, "This command is not executable from the console.");
            return true;
        }
        if (args.length == 0 || BrigadierOption.getOption(args[0]) == null) {
            BrigadierCommand.sendUsage(sender);
            return true;
        }
        BrigadierOption.getOption(args[0]).execute(sender, args.length == 1 ? "" : args[1]);
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            args[0] = args[0].toLowerCase();
            return BrigadierOption.getList().stream()
                    .filter(o -> o.toLowerCase().startsWith(args[0]))
                    .collect(Collectors.toList());
        }
        if (args.length == 2) {
            args[1] = args[1].toLowerCase();
            if (args[0].equalsIgnoreCase(BrigadierOption.HIDE.toString())) {
                return Config.getDisplayedCommands().stream()
                        .filter(o -> o.toLowerCase().startsWith(args[1]))
                        .collect(Collectors.toList());
            }
            if (args[0].equalsIgnoreCase(BrigadierOption.UNHIDE.toString())) {
                return Config.getHiddenCommands().stream()
                        .filter(o -> o.toLowerCase().startsWith(args[1]))
                        .collect(Collectors.toList());
            }
        }
        return Collections.emptyList();
    }
}
