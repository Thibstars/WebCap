/*
 * Copyright (c) 2019 Thibault Helsmoortel.
 *
 * This file is part of WebCap.
 *
 * WebCap is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * WebCap is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with WebCap.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package be.thibaulthelsmoortel.webcap.commands.converters;

import java.net.URL;
import org.apache.commons.lang3.StringUtils;
import picocli.CommandLine.ITypeConverter;

/**
 * String to {@link URL} converter.
 *
 * @author Thibault Helsmoortel
 */
public class URLConverter implements ITypeConverter<URL> {

    @Override
    public URL convert(String value) throws Exception {
        if (!StringUtils.startsWith(value, "http")) {
            value = "http://" + value;
        }
        return new URL(value);
    }
}
