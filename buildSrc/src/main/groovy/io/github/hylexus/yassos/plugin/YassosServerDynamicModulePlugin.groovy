/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.hylexus.yassos.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.Logger

import static io.github.hylexus.yassos.plugin.YassosServerConfigKey.CONFIG_KEY_SESSION_MANAGER
import static io.github.hylexus.yassos.plugin.YassosServerConfigKey.CONFIG_KEY_USER_LOADER

/**
 * Gradle plugin that automatically updates testCompile dependencies to include
 * the test source sets of project dependencies.
 *
 * @author Phillip Webb
 */
class YassosServerDynamicModulePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        Logger logger = project.getLogger();
        def builtinModuleMapping = [
                'memory-session-manager': ':yassos-server-plugin:yassos-session-manager-memory',
                'redis-session-manager' : ':yassos-server-plugin:yassos-session-manager-redis',
                'file-user-loader'      : ':yassos-server-plugin:yassos-user-loader-file',
                'jdbc-user-loader'      : ':yassos-server-plugin:yassos-user-loader-jdbc',
        ]
        def userLoaderModuleName = System.getProperty("user-loader") ?: "file-user-loader"
        def sessionManagerModuleName = System.getProperty("session-manager") ?: "memory-session-manager"
        logger.quiet("user-loader : {}", userLoaderModuleName)
        logger.quiet("session-manager : {}", sessionManagerModuleName)

        def userLoaderModule = builtinModuleMapping[userLoaderModuleName]
        def sessionManagerModule = builtinModuleMapping[sessionManagerModuleName]

        logger.quiet("l : " + userLoaderModule)
        logger.quiet("s : " + sessionManagerModule)

        project.afterEvaluate {

            project.dependencies.add("implementation", "project(${userLoaderModule})")
            logger.quiet("add module : {}", userLoaderModule)
            project.dependencies.add("implementation", "project(${sessionManagerModule})")
            logger.quiet("add module : {}", sessionManagerModule)
        }
    }

}

enum YassosServerConfigKey {
    CONFIG_KEY_USER_LOADER("user-loader"),
    CONFIG_KEY_SESSION_MANAGER("session-manager"),
    CONFIG_KEY_SESSION_ATTR_CONVERTER("session-attr-converter"),
    CONFIG_KEY_TOKEN_GENERATOR("token-generator"),
    CONFIG_KEY_CREDENTIALS_MATCHER("credentials-matcher"),
    ;
    String keyName;

    YassosServerConfigKey(String keyName) {
        this.keyName = keyName;
    }
}

enum BuiltinYassosModules {

    USER_LOADER_FILE(CONFIG_KEY_USER_LOADER, "file", ":yassos-server-plugin:yassos-user-loader-file"),
    USER_LOADER_JDBC(CONFIG_KEY_USER_LOADER, "jdbc", ":yassos-server-plugin:yassos-user-loader-jdbc"),
    SESSION_MANAGER_MEMORY(CONFIG_KEY_SESSION_MANAGER, "memory", ":yassos-server-plugin:yassos-session-manager-memory"),
    SESSION_MANAGER_REDIS(CONFIG_KEY_SESSION_MANAGER, "redis", ":yassos-server-plugin:yassos-session-manager-redis"),
    ;
    YassosServerConfigKey configKey;
    String alias;
    String projectName;

    BuiltinYassosModules(YassosServerConfigKey configKey, String alias, String projectName) {
        this.configKey = configKey;
        this.alias = alias;
        this.projectName = projectName;
    }

    public static Map<String, Map<String, BuiltinYassosModules>> allBuiltinModules() {

        Map<String, Map<String, BuiltinYassosModules>> mapping = new HashMap<>();

        for (BuiltinYassosModules module : values()) {
            String configKeyName = module.configKey.keyName;

            Map<String, BuiltinYassosModules> map = mapping.computeIfAbsent(configKeyName, { k -> new HashMap<>() });

            map.put(module.alias, module);

            mapping.put(configKeyName, map);
        }
        return mapping;
    }
}


