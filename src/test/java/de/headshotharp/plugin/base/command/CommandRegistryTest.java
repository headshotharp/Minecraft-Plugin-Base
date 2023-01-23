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

import org.bukkit.command.defaults.BukkitCommand;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.bridge.SLF4JBridgeHandler;

import de.headshotharp.plugin.base.command.testplugin.TestDataSource;
import de.headshotharp.plugin.base.command.testplugin.TestPluginImpl;

class CommandRegistryTest {

    static {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    @Test
    void testCommandImplInstanciation()
            throws InstantiationException, IllegalAccessException, InvocationTargetException {
        // prepare objects
        TestDataSource datasource = new TestDataSource();
        // prepare mocks
        TestPluginImpl pluginMock = Mockito.mock(TestPluginImpl.class);
        Mockito.when(pluginMock.getLogger()).thenCallRealMethod();
        doCallRealMethod().when(pluginMock).someCustomMethod(anyString());
        BukkitCommand bukkitCommand = Mockito.mock(BukkitCommand.class);
        Mockito.when(bukkitCommand.getName()).thenReturn("test");
        // create registry
        CommandRegistry<TestPluginImpl> registry = new CommandRegistry<>("test", pluginMock, TestPluginImpl.class, true,
                datasource);
        // execute command
        registry.onCommand(null, bukkitCommand, null, new String[] { "test", "Hello", "World!" });
        // check
        assertThat(pluginMock.msg, is(equalTo("[MC] Hello World!")));
    }
}
