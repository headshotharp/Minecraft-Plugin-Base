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

public class LoggablePlugin extends JavaPlugin {

    public void info(String msg, Throwable t) {
        getLogger().log(Level.INFO, msg, t);
    }

    public void info(String msg) {
        getLogger().log(Level.INFO, msg);
    }

    public void warn(String msg) {
        getLogger().log(Level.WARNING, msg);
    }

    public void warn(String msg, Throwable t) {
        getLogger().log(Level.WARNING, msg, t);
    }

    public void error(String msg) {
        getLogger().log(Level.SEVERE, msg);
    }

    public void error(String msg, Throwable t) {
        getLogger().log(Level.SEVERE, msg, t);
    }
}
