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

import java.util.List;

import org.bukkit.command.CommandSender;

/**
 * Functional interface to complete tab requests
 */
@FunctionalInterface
public interface CommandTabCompletable {

    /**
     * Function to execute on tab completion
     *
     * @param sender  command sender
     * @param command given command
     * @param args    arguemnts to given command
     * @return returns a list of tab completions
     */
    public List<String> onTabComplete(CommandSender sender, String command, String... args);
}
