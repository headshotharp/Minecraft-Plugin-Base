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

import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Abstract class to create bukkit commands for the
 * {@link de.headshotharp.plugin.base.command.CommandRegistry CommandRegistry}.
 *
 * @param <T> Base class for implementing JavaPlugin
 */
public abstract class ExecutableCommand<T extends JavaPlugin>
        implements CommandRunnable, CommandApplicable, CommandTabCompletable {

    private T plugin;

    /**
     * Create command
     *
     * @param plugin the base plugin
     */
    protected ExecutableCommand(T plugin) {
        this.plugin = plugin;
    }

    /**
     * Get current plugin instance
     *
     * @return current plugin instance
     */
    public T getPlugin() {
        return plugin;
    }

    /**
     * Wrapper to get {@link org.bukkit.Server Server} of the current plugin
     * instance
     *
     * @return server of the current plugin
     */
    public Server getServer() {
        return plugin.getServer();
    }

    /**
     * Return true if the command is for players only
     *
     * @return true if players only
     */
    public abstract boolean isForPlayerOnly();

    /**
     * Get the command usage as string
     *
     * @return command usage as string
     */
    public abstract String usage();

    /**
     * Get the name of the command
     *
     * @return name of the command
     */
    public abstract String getName();

    /**
     * Return true if the command is applicable
     */
    @Override
    public boolean isApplicable(CommandSender sender, String command, String... args) {
        return command.equalsIgnoreCase(getName());
    }
}
