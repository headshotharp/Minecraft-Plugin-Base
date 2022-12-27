/**
 * Minecraft Plugin Base
 * Copyright © 2022 headshotharp.de
 *
 * This file is part of Minecraft Plugin Base.
 *
 * Minecraft Plugin Base is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Minecraft Plugin Base is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Minecraft Plugin Base. If not, see <https://www.gnu.org/licenses/>.
 */
package de.headshotharp.plugin.base.command.generic;

import org.bukkit.command.CommandSender;

/**
 * Functional interface to check if a command is applicable to its
 * implementation
 */
@FunctionalInterface
public interface CommandApplicable {

    /**
     * Function to check if a command applies
     *
     * @param sender  the command sender
     * @param command the base command
     * @param args    the arguments to the given command
     * @return true if the command applies
     */
    public boolean isApplicable(CommandSender sender, String command, String... args);
}
