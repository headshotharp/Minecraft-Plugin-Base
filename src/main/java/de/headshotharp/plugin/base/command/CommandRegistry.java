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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import de.headshotharp.plugin.base.command.generic.ExecutableCommand;

public class CommandRegistry<T extends JavaPlugin> implements CommandExecutor, TabCompleter {

    private List<ExecutableCommand<T>> commands = new LinkedList<>();

    private T plugin;
    private Map<Class<?>, Object> injectables = new HashMap<>();

    public CommandRegistry(T plugin, Class<T> pluginClass, Object... injectableInstances)
            throws InstantiationException, IllegalAccessException, InvocationTargetException {
        this.plugin = plugin;
        injectables.put(pluginClass, plugin);
        for (Object injectableInstance : injectableInstances) {
            injectables.put(injectableInstance.getClass(), injectableInstance);
        }
        scanCommands(plugin.getClass().getPackageName());
    }

    private void scanCommands(String packageName)
            throws InstantiationException, IllegalAccessException, InvocationTargetException {
        Set<Class<? extends ExecutableCommand<T>>> commandClasses = findClasses(packageName);
        for (Class<? extends ExecutableCommand<T>> clazz : commandClasses) {
            if (clazz.getDeclaredConstructors().length != 1) {
                throw new IllegalStateException("The class " + clazz.getSimpleName()
                        + " must have exactly one constructor for auto-instantiation");
            }
            @SuppressWarnings("unchecked") // this is safe as clazz is Class<? extends ExecutableCommand<T>>
            Constructor<ExecutableCommand<T>> constructor = (Constructor<ExecutableCommand<T>>) clazz
                    .getDeclaredConstructors()[0];
            Object[] params = new Object[constructor.getParameterCount()];
            for (int i = 0; i < params.length; i++) {
                Class<?> paramType = constructor.getParameterTypes()[i];
                Object instance = injectables.get(paramType);
                if (instance != null) {
                    params[i] = paramType.cast(instance);
                } else {
                    throw new IllegalStateException("The class " + clazz.getSimpleName()
                            + " has an invalid constructor param type: " + paramType.getSimpleName());
                }
            }
            ExecutableCommand<T> command = constructor.newInstance(params);
            commands.add(command);
        }
        String allCommands = String.join(", ", commands.stream().map(ExecutableCommand::getName).toList());
        if (plugin.getLogger().isLoggable(Level.INFO)) {
            plugin.getLogger().info(String.format("Registered %d commands: %s", commands.size(), allCommands));
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private Set<Class<? extends ExecutableCommand<T>>> findClasses(String packageName) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<? extends ExecutableCommand>> commandClassesRaw = reflections.getSubTypesOf(ExecutableCommand.class);
        return commandClassesRaw.stream().map(c -> (Class<? extends ExecutableCommand<T>>) c)
                .filter(c -> !Modifier.isAbstract(c.getModifiers())).collect(Collectors.toSet());
    }

    public List<ExecutableCommand<T>> getCommands() {
        return commands;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command bukkitCommand, String label, String[] originalArgs) {
        if (originalArgs.length > 0) {
            String cmd = originalArgs[0];
            String[] args = moveArgs(originalArgs);
            for (ExecutableCommand<T> command : commands) {
                if (command.isApplicable(sender, cmd, args)) {
                    if (command.isForPlayerOnly() && !(sender instanceof Player)) {
                        sender.sendMessage("The command is for players only");
                    } else {
                        command.execute(sender, cmd, args);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command bukkitCommand, String alias,
            String[] originalArgs) {
        if (originalArgs.length == 1) {
            return commands.stream().map(cmd -> cmd.getName().toLowerCase())
                    .filter(cmd -> cmd.startsWith(originalArgs[0].toLowerCase())).toList();
        } else {
            String cmd = originalArgs[0];
            String[] args = moveArgs(originalArgs);
            for (ExecutableCommand<T> command : commands) {
                if (command.isApplicable(sender, cmd, args)) {
                    return command.onTabComplete(sender, cmd, args);
                }
            }
        }
        return new LinkedList<>();
    }

    private String[] moveArgs(String[] args) {
        if (args.length <= 1) {
            return new String[0];
        }
        String[] newArgs = new String[args.length - 1];
        for (int i = 0; i < newArgs.length; i++) {
            newArgs[i] = args[i + 1];
        }
        return newArgs;
    }
}
