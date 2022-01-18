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

package net.clementraynaud.brigadiermanager.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandSendEvent;

import java.util.ArrayList;
import java.util.List;

import static net.clementraynaud.brigadiermanager.main.Config.*;

public class PlayerCommandSendListener implements Listener {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerCommandSendEvent(PlayerCommandSendEvent e) {
        if (!(areOperatorsIgnored() && e.getPlayer().isOp())) {
            e.getCommands().removeIf(s -> getHiddenCommands().contains(s));
        }
        if (e.getPlayer().isOp()) {
            List<String> commands = new ArrayList<>(e.getCommands());
            commands.removeIf(s -> getHiddenCommands().contains(s));
            setDisplayedCommands(commands);
            if (isBrigadierCommandRetainedForOperators()) {
                e.getCommands().add("brigadier");
            }
        }
    }
}
