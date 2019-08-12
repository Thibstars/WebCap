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

package be.thibaulthelsmoortel.webcap.commands;

import be.thibaulthelsmoortel.webcap.commands.converters.URLConverter;
import be.thibaulthelsmoortel.webcap.commands.core.BotCommand;
import java.io.File;
import java.net.URL;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

/**
 * Command covering the main feature; capturing a screenshot from a provided url.
 *
 * @author Thibault Helsmoortel
 */
@Command(name = "capture", description = "Provides command usage help.")
@Component
@Slf4j
public class CaptureCommand extends BotCommand {

    private static final long TIME_OUT_IN_SECONDS = 20;
    private static final String CAPTURE_PNG = "capture.png";

    @SuppressWarnings("UnusedDeclaration") // Picked up by pico cli
    @Parameters(paramLabel = "URL", description = "Url of the page from which to capture a screenshot.", arity = "1", index = "0", converter = URLConverter.class)
    private URL url;

    private final WebDriver webDriver;

    @Autowired
    public CaptureCommand(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    @Override
    public Object call() {
        if (getEvent() instanceof MessageReceivedEvent) {
            log.debug("Navigating to url: {}", url.toString());
            webDriver.get(url.toString());

            log.debug("Preparing instance for capture.");
            webDriver.manage().window().fullscreen();
            Wait<WebDriver> wait = new WebDriverWait(webDriver, TIME_OUT_IN_SECONDS);
            wait.until(d -> ((JavascriptExecutor) d).executeScript("return document.readyState").equals("complete"));

            log.debug("Capturing screenshot.");
            File scrFile = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);

            log.debug("Building messageBuilder.");
            MessageBuilder messageBuilder = new MessageBuilder();
            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("Capture of: " + url.toString(), url.toString());
            builder.setImage("attachment://" + CAPTURE_PNG);
            MessageEmbed embed = builder.build();
            messageBuilder.setEmbed(embed);
            ((MessageReceivedEvent) getEvent()).getChannel().sendFile(scrFile, CAPTURE_PNG, messageBuilder.build()).queue();

            return embed;
        }

        return null;
    }

}
