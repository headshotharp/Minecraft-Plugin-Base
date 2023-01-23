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

import java.util.LinkedList;
import java.util.List;

import org.bukkit.command.CommandSender;

import de.headshotharp.plugin.base.command.generic.ExecutableCommand;

public class TestCommandImpl extends ExecutableCommand<TestPluginImpl> {

    private TestDataSource dataSource;

    public TestCommandImpl(TestPluginImpl plugin, TestDataSource dataSource) {
        super(plugin);
        this.dataSource = dataSource;
    }

    @Override
    public boolean execute(CommandSender sender, String command, String... args) {
        getPlugin().someCustomMethod("[" + dataSource.loadServerName() + "] " + String.join(" ", args));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String command, String... args) {
        return new LinkedList<>();
    }

    @Override
    public boolean isForPlayerOnly() {
        return false;
    }

    @Override
    public String usage() {
        return "/test <msg>";
    }

    @Override
    public String getName() {
        return "test";
    }

}