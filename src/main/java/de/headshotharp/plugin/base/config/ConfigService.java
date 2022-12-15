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

public class ConfigService<T> {

    private final ObjectMapper mapper = new YAMLMapper();
    private final Class<T> configClass;
    private final File configFile;

    public ConfigService(Class<T> configClass, String pluginName) {
        this(configClass, Paths.get("plugins", pluginName, "config.yml").toFile());
    }

    public ConfigService(Class<T> configClass, File configFile) {
        this.configClass = configClass;
        this.configFile = configFile;
    }

    public T readConfig() throws IOException {
        return mapper.readValue(configFile, configClass);
    }

    public void saveConfig(T config) throws IOException {
        configFile.getParentFile().mkdirs();
        mapper.writeValue(configFile, config);
    }

    public File getConfigFile() {
        return configFile;
    }
}
