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

package be.thibaulthelsmoortel.webcap.config;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

/**
 * @author Thibault Helsmoortel
 */
class WebDriverDestroyerTest {

    private WebDriver webDriver;
    private WebDriverDestroyer destroyer;

    @BeforeEach
    void setUp() {
        this.webDriver = mock(WebDriver.class);
        this.destroyer = new WebDriverDestroyer(webDriver);
    }

    @DisplayName("Should quit driver.")
    @Test
    void shouldQuitDriver() {
        destroyer.destroy();
        verify(webDriver).quit();
    }
}
