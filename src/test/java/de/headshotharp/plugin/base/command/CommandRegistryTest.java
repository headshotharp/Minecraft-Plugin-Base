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
package de.headshotharp.plugin.base.command;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doCallRealMethod;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.bridge.SLF4JBridgeHandler;

import de.headshotharp.plugin.base.command.generic.ExecutableCommand;

class CommandRegistryTest {

    static {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    @Test
    void testCommandImplInstanciation()
            throws InstantiationException, IllegalAccessException, InvocationTargetException {
        // prepare objects
        DataSource datasource = new DataSource();
        // prepare mocks
        TestPluginImpl pluginMock = Mockito.mock(TestPluginImpl.class);
        Mockito.when(pluginMock.getLogger()).thenCallRealMethod();
        doCallRealMethod().when(pluginMock).someCustomMethod(anyString());
        // create registry
        CommandRegistry<TestPluginImpl> registry = new CommandRegistry<>(pluginMock, TestPluginImpl.class, datasource);
        // execute command
        registry.onCommand(null, null, null, new String[] { "test", "Hello", "World!" });
        // check
        assertThat(pluginMock.msg, is(equalTo("[MC] Hello World!")));
    }

    public static class TestCommandImpl extends ExecutableCommand<TestPluginImpl> {

        private DataSource dataSource;

        protected TestCommandImpl(TestPluginImpl plugin, DataSource dataSource) {
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

    public static class TestPluginImpl extends JavaPlugin {

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

    public static class DataSource {

        public String loadServerName() {
            return "MC";
        }

        @Override
        public String toString() {
            return "DataSource []";
        }
    }
}
