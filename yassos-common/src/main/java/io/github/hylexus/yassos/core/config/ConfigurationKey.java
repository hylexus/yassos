/*
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package io.github.hylexus.yassos.core.config;


import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * COPIED FROM https://github.com/apereo/java-cas-client/blob/master/cas-client-core/src/main/java/org/jasig/cas/client/configuration/ConfigurationKey.java
 * Holder class to represent a particular configuration key and its optional default value.
 *
 * @author Scott Battaglia
 * @since 3.4.0
 */
public final class ConfigurationKey<E> {

    @Getter
    private final String name;

    @Getter
    private final E defaultValue;

    private final ConfigurationKeyValidator<E> validator;

    public void validate(String name, E value, boolean force) {
        if (this.validator == null) {
            if (force) {
                throw new IllegalArgumentException("[" + name + "] attempt to FORCE validation, but validator is null");
            }
        } else {
            this.validator.validate(name, value);
        }
    }

    public void validate(E value, boolean force) {
        validate(this.name, value, force);
    }

    public void validate() {
        validate(this.defaultValue, false);
    }

    public ConfigurationKey(final String name, final E defaultValue, ConfigurationKeyValidator<E> validator) {
        this.name = Objects.requireNonNull(name, "name must be not null");
        this.defaultValue = defaultValue;
        this.validator = validator;
    }

    @FunctionalInterface
    public interface ConfigurationKeyValidator<E> {
        void validate(String name, E value);

        ConfigurationKeyValidator<String> NOT_EMPTY = (name, value) -> {
            if (value == null) {
                throw new NullPointerException();
            }
            if (StringUtils.isEmpty(value)) {
                throw new IllegalArgumentException(name + " must be not null");
            }
        };
    }
}
