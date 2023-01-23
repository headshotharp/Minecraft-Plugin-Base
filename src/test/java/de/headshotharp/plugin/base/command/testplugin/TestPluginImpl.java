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
package de.headshotharp.plugin.base.command.testplugin;

import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

public class TestPluginImpl extends JavaPlugin {

    public String msg = null;

    public void someCustomMethod(String msg) {
        this.msg = msg;
        getLogger().info("TestPluginImpl: " + msg);
    }

    @Override
    public Logger getLogger() {
        return Logger.getLogger(TestPluginImpl.class.getName());
    }
}