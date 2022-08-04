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

package net.clementraynaud.tabcompletermanager.listeners;

import net.clementraynaud.tabcompletermanager.config.Config;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandSendEvent;

import java.util.HashSet;
import java.util.Set;

public class PlayerListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerCommandSend(PlayerCommandSendEvent e) {
        if (!(Config.areOperatorsIgnored() && e.getPlayer().isOp())) {
            e.getCommands().removeIf(s -> Config.getHiddenCommands().contains(s));
        }
        if (e.getPlayer().isOp()) {
            Set<String> commands = new HashSet<>(e.getCommands());
            commands.removeIf(s -> Config.getHiddenCommands().contains(s));
            Config.setDisplayedCommands(commands);
        }
    }
}
