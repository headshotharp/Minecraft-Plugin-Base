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
package de.headshotharp.plugin.base.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

/**
 * Simple service class to save and load config files in YAML/YML format. Simple
 * POJO class must be given as configuration class.
 *
 *
 * @param <T> Configuration class
 */
public class ConfigService<T> {

    private final ObjectMapper mapper = new YAMLMapper();
    private final Class<T> configClass;
    private final File configFile;

    /**
     * Creates a config service for the given config class. Uses the plugin name to
     * create default configuration path: <code>plugins/{name}/config.yml</code>.
     *
     * @param configClass the configuration class
     * @param pluginName  name of the plugin to create config file path
     */
    public ConfigService(Class<T> configClass, String pluginName) {
        this(configClass, Paths.get("plugins", pluginName, "config.yml").toFile());
    }

    /**
     * Creates a config service for the given config class and given file. The file
     * is relative to the papermc root folder. It is advised to create a folder per
     * plugin inside the servers plugins folder.<br />
     * Example:
     * <code>Paths.get("plugins", pluginName, "my-config-file.yml").toFile()</code>
     *
     * @param configClass the configuration class
     * @param configFile  the configuration file path
     */
    public ConfigService(Class<T> configClass, File configFile) {
        this.configClass = configClass;
        this.configFile = configFile;
    }

    /**
     * Read config from disk.
     *
     * @return loaded config file as pojo
     * @throws IOException may throw IOException
     */
    public T readConfig() throws IOException {
        return mapper.readValue(configFile, configClass);
    }

    /**
     * Save given config to disk.
     *
     * @param config the config pojo to save
     * @throws IOException may throw IOException
     */
    public void saveConfig(T config) throws IOException {
        configFile.getParentFile().mkdirs();
        mapper.writeValue(configFile, config);
    }

    /**
     * Get the given config file upon config service creation.
     *
     * @return current config file of this config service
     */
    public File getConfigFile() {
        return configFile;
    }
}
