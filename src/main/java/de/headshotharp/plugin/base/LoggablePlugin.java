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
package de.headshotharp.plugin.base;

import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Simple {@link org.bukkit.plugin.java.JavaPlugin JavaPlugin} extension to
 * provide easy logging methods.
 *
 */
public class LoggablePlugin extends JavaPlugin {

    /**
     * Log info
     *
     * @param msg Text to log
     */
    public void info(String msg) {
        getLogger().log(Level.INFO, msg);
    }

    /**
     * Log info
     *
     * @param msg Text to log
     * @param t   Exception to log
     */
    public void info(String msg, Throwable t) {
        getLogger().log(Level.INFO, msg, t);
    }

    /**
     * Log warning
     *
     * @param msg Text to log
     */
    public void warn(String msg) {
        getLogger().log(Level.WARNING, msg);
    }

    /**
     * Log warning
     *
     * @param msg Text to log
     * @param t   Exception to log
     */
    public void warn(String msg, Throwable t) {
        getLogger().log(Level.WARNING, msg, t);
    }

    /**
     * Log error
     *
     * @param msg Text to log
     */
    public void error(String msg) {
        getLogger().log(Level.SEVERE, msg);
    }

    /**
     * Log error
     *
     * @param msg Text to log
     * @param t   Exception to log
     */
    public void error(String msg, Throwable t) {
        getLogger().log(Level.SEVERE, msg, t);
    }
}
