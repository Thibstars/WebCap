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
import java.net.URL;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author Thibault Helsmoortel
 */
class URLConverterTest extends BaseTest {

    private URLConverter urlConverter;

    @BeforeEach
    void setUp() {
        this.urlConverter = new URLConverter();
    }

    @DisplayName("Should convert url.")
    @Test
    void shouldConvertUrl() throws Exception {
        URL urlToConvert = new URL("https://google.com");
        URL url = urlConverter.convert(urlToConvert.toString());

        Assertions.assertEquals(urlToConvert, url, "Url must be converted correctly.");
    }

    @DisplayName("Should convert url with missing protocol.")
    @Test
    void shouldConvertUrlWithMissingProtocol() throws Exception {
        String protocol = "http://";
        String host = "fridayyet.net";
        URL urlToConvert = new URL(protocol + host);
        URL url = urlConverter.convert(host);

        Assertions.assertEquals(urlToConvert, url, "Url must be converted correctly.");
    }
}
