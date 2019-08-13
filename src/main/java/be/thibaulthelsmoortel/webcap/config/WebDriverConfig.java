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

import io.github.bonigarcia.wdm.WebDriverManager;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @author Thibault Helsmoortel
 */
@Configuration
@Slf4j
public class WebDriverConfig {

    private static final String INCOGNITO_ARGUMENT = "--incognito"; // Use incognito for the most clean slate
    private static final String START_MAXIMIZED_ARGUMENT = "--start-maximized"; // Start maximized
    private static final String HEADLESS_ARGUMENT = "--headless"; // Run headless
    private static final String DISABLE_EXTENSIONS_ARGUMENT = "--disable-extensions";
    private static final String NO_SANDBOX_ARGUMENT = "--no-sandbox";
    private static final String DISABLE_GPU_RENDERING_ARGUMENT = "--disable-gpu"; // Don't use GPU rendering

    private static final String LINUX_DEPLOY_PROFILE = "linuxDeploy";

    private final Environment environment;

    @Autowired
    public WebDriverConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean(destroyMethod = "destroy")
    public WebDriverDestroyer webDriverDestroyer() {
        return new WebDriverDestroyer(webDriver());
    }

    @Bean
    public WebDriver webDriver() {
        log.info("Initialising driver...");

        WebDriverManager.chromedriver().setup();

        // Initialise the web driver
        ChromeOptions options = new ChromeOptions();

        if (Arrays.asList(environment.getActiveProfiles()).contains(LINUX_DEPLOY_PROFILE)) {
            options.addArguments("--whitelisted-ips");
        }

        options.addArguments(INCOGNITO_ARGUMENT, START_MAXIMIZED_ARGUMENT);

        // Make lightweight
        options.addArguments(HEADLESS_ARGUMENT, DISABLE_EXTENSIONS_ARGUMENT, NO_SANDBOX_ARGUMENT, DISABLE_GPU_RENDERING_ARGUMENT);

        log.info("Driver initialised!");

        return new ChromeDriver(options);
    }

}
