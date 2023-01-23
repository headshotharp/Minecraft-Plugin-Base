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
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import de.headshotharp.plugin.base.command.generic.ExecutableCommand;

/**
 * Base class to register bukkit commands on
 *
 * @param <T> Base class of the implementing
 *            {@link org.bukkit.plugin.java.JavaPlugin JavaPlugin}
 */
public class CommandRegistry<T extends JavaPlugin> extends ExecutableCommand<T> {

    private String name;
    private List<ExecutableCommand<T>> subcommands = new LinkedList<>();

    /**
     * Creates a command registry and scans classpath for bukkit commands
     * implementing
     * {@link de.headshotharp.plugin.base.command.generic.ExecutableCommand
     * ExecutableCommand}. If those commands require any other objects to be created
     * in their constructor, those are injected on the fly, but must be given here
     * as injectable instances.
     *
     * @param name                name of this command
     * @param plugin              base plugin implementation
     * @param pluginClass         class of the plugin implementation
     * @param recursive           scan subpackages
     * @param injectableInstances all additional instances of objects that may be
     *                            later injected into command constructors
     * @throws InstantiationException    thrown if the command cannot be intatiated
     * @throws IllegalAccessException    thrown if the constructor is not accessible
     * @throws InvocationTargetException thrown if the constructor cannot be invoked
     */
    public CommandRegistry(String name, T plugin, Class<T> pluginClass, boolean recursive,
            Object... injectableInstances)
            throws InstantiationException, IllegalAccessException, InvocationTargetException {
        this(name, plugin, pluginClass, pluginClass, recursive, injectableInstances);
    }

    /**
     * Creates a command registry and scans classpath for bukkit commands
     * implementing
     * {@link de.headshotharp.plugin.base.command.generic.ExecutableCommand
     * ExecutableCommand}. If those commands require any other objects to be created
     * in their constructor, those are injected on the fly, but must be given here
     * as injectable instances.
     *
     * @param name                name of this command
     * @param plugin              base plugin implementation
     * @param pluginClass         class of the plugin implementation
     * @param commandBaseClass    any class inside the package to scan for commands
     * @param recursive           scan subpackages
     * @param injectableInstances all additional instances of objects that may be
     *                            later injected into command constructors
     * @throws InstantiationException    thrown if the command cannot be intatiated
     * @throws IllegalAccessException    thrown if the constructor is not accessible
     * @throws InvocationTargetException thrown if the constructor cannot be invoked
     */
    public CommandRegistry(String name, T plugin, Class<T> pluginClass, Class<?> commandBaseClass, boolean recursive,
            Object... injectableInstances)
            throws InstantiationException, IllegalAccessException, InvocationTargetException {
        this(name, plugin, pluginClass, commandBaseClass.getPackageName(), recursive, injectableInstances);
    }

    /**
     * Creates a command registry and scans classpath for bukkit commands
     * implementing
     * {@link de.headshotharp.plugin.base.command.generic.ExecutableCommand
     * ExecutableCommand}. If those commands require any other objects to be created
     * in their constructor, those are injected on the fly, but must be given here
     * as injectable instances.
     *
     * @param name                name of this command
     * @param plugin              base plugin implementation
     * @param pluginClass         class of the plugin implementation
     * @param basePackageName     the package to scan for commands
     * @param recursive           scan subpackages
     * @param injectableInstances all additional instances of objects that may be
     *                            later injected into command constructors
     * @throws InstantiationException    thrown if the command cannot be intatiated
     * @throws IllegalAccessException    thrown if the constructor is not accessible
     * @throws InvocationTargetException thrown if the constructor cannot be invoked
     */
    public CommandRegistry(String name, T plugin, Class<T> pluginClass, String basePackageName, boolean recursive,
            Object... injectableInstances)
            throws InstantiationException, IllegalAccessException, InvocationTargetException {
        this(name, plugin, scanCommands(basePackageName, recursive, plugin, pluginClass, injectableInstances));
    }

    /**
     * Creates a command registry with only the given bukkit commands without any
     * package scanning or object instantiation.
     *
     * @param name        name of this command
     * @param plugin      base plugin implementation
     * @param subcommands the commands of type ExecutableCommand to register
     */
    @SafeVarargs
    public CommandRegistry(String name, T plugin, ExecutableCommand<T>... subcommands) {
        this(name, plugin, Arrays.asList(subcommands));
    }

    /**
     * Creates a command registry with only the given bukkit commands without any
     * package scanning or object instantiation.
     *
     * @param name        name of this command
     * @param plugin      base plugin implementation
     * @param subcommands the commands of type ExecutableCommand to register
     */
    public CommandRegistry(String name, T plugin, List<ExecutableCommand<T>> subcommands) {
        super(plugin);
        this.name = name;
        this.subcommands = subcommands;
    }

    /**
     * Get all registered commands
     *
     * @return List of registered commands
     */
    public List<ExecutableCommand<T>> getCommands() {
        return subcommands;
    }

    @Override
    public boolean execute(CommandSender sender, String bukkitCommand, String... originalArgs) {
        if (originalArgs.length > 0) {
            String cmd = originalArgs[0];
            String[] args = moveArgs(originalArgs);
            for (ExecutableCommand<T> command : subcommands) {
                if (command.isApplicable(sender, cmd, args)) {
                    if (command.isForPlayerOnly() && !(sender instanceof Player)) {
                        sender.sendMessage("The command is for players only");
                    } else if (!command.execute(sender, cmd, args)) {
                        sender.sendMessage(ChatColor.DARK_RED + command.usage());
                    }
                    return true;
                }
            }
        }
        sender.sendMessage(ChatColor.YELLOW + usage());
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String bukkitCommand, String... originalArgs) {
        if (originalArgs.length == 1) {
            return subcommands.stream().map(cmd -> cmd.getName().toLowerCase())
                    .filter(cmd -> cmd.startsWith(originalArgs[0].toLowerCase())).toList();
        } else {
            String cmd = originalArgs[0];
            String[] args = moveArgs(originalArgs);
            for (ExecutableCommand<T> command : subcommands) {
                if (command.isApplicable(sender, cmd, args)) {
                    return command.onTabComplete(sender, cmd, args);
                }
            }
        }
        return new LinkedList<>();
    }

    @Override
    public boolean isForPlayerOnly() {
        return false;
    }

    @Override
    public String usage() {
        return "Available subcommands: " + String.join(", ", subcommands.stream().map(ExecutableCommand::getName).toList());
    }

    @Override
    public String getName() {
        return name;
    }

    /* utilities */

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

    private static <T extends JavaPlugin> List<ExecutableCommand<T>> scanCommands(String packageName, boolean recursive,
            T plugin, Class<T> pluginClass, Object[] injectableInstances)
            throws InstantiationException, IllegalAccessException, InvocationTargetException {
        // build injectables map
        Map<Class<?>, Object> injectables = new HashMap<>();
        injectables.put(pluginClass, plugin);
        for (Object injectableInstance : injectableInstances) {
            injectables.put(injectableInstance.getClass(), injectableInstance);
        }
        // scan classes
        List<ExecutableCommand<T>> commands = new LinkedList<>();
        Set<Class<? extends ExecutableCommand<T>>> commandClasses = findClasses(packageName, recursive);
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
            commands.add(constructor.newInstance(params));
        }
        return commands;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static <T extends JavaPlugin> Set<Class<? extends ExecutableCommand<T>>> findClasses(String packageName,
            boolean recursive) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<? extends ExecutableCommand>> commandClassesRaw = reflections.getSubTypesOf(ExecutableCommand.class);
        if (!recursive) {
            commandClassesRaw = commandClassesRaw.stream().filter(c -> packageName.equals(c.getPackageName()))
                    .collect(Collectors.toSet());
        }
        return commandClassesRaw.stream()
                .map(c -> (Class<? extends ExecutableCommand<T>>) c)
                .filter(c -> !Modifier.isAbstract(c.getModifiers())).collect(Collectors.toSet());
    }
}
