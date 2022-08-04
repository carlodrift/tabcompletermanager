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

package net.clementraynaud.tabcompletermanager.util;

import net.clementraynaud.tabcompletermanager.TabCompleterManager;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public interface MessageUtil {

    @SuppressWarnings("deprecation")
    static void setHoverEvent(TextComponent message, String hover) {
        try {
            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(hover)));
        } catch (NoClassDefFoundError e) {
            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hover).create()));
        }
    }

    static void sendMessage(CommandSender sender, String message, ChatColor color) {
        sender.sendMessage(TabCompleterManager.PREFIX + color + message);
    }

    static void sendErrorMessage(CommandSender sender, String message) {
        MessageUtil.sendMessage(sender, message, ChatColor.RED);
    }

    static void sendValidationMessage(CommandSender sender, String message) {
        MessageUtil.sendMessage(sender, message, ChatColor.GREEN);
    }
}
