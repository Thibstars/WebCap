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

import be.thibaulthelsmoortel.webcap.BaseTest;
import net.dv8tion.jda.core.Permission;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author Thibault Helsmoortel
 */
class PermissionConverterTest extends BaseTest {

    private PermissionConverter permissionConverter;

    @BeforeEach
    void setUp() {
        this.permissionConverter = new PermissionConverter();
    }

    @DisplayName("Should convert permission.")
    @Test
    void shouldConvertPermission() {
        Permission permissionToConvert = Permission.MESSAGE_READ;
        Permission permission = permissionConverter.convert(permissionToConvert.toString());

        Assertions.assertEquals(permissionToConvert, permission, "Permission should be converted correctly.");
    }

    @DisplayName("Should not convert unexpected value.")
    @Test
    void shouldNotConvertUnexpectedValue() {
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> permissionConverter.convert("someUnexpectedValue"),
            "Conversion should fail.");
    }
}
