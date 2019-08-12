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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.File;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.requests.restaction.MessageAction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Thibault Helsmoortel
 */
class CaptureCommandTest extends CommandBaseTest {

    private static final String CAPTURE_PNG = "capture.png";

    private final CaptureCommand captureCommand;

    @Autowired
    CaptureCommandTest(CaptureCommand captureCommand) {
        this.captureCommand = captureCommand;
    }

    @Override
    @BeforeEach
    void setUp() {
        when(messageReceivedEvent.getChannel()).thenReturn(messageChannel);
    }

    @DisplayName("Should send capture message.")
    @Test
    void shouldSendCaptureMessage() throws NoSuchFieldException, IllegalAccessException, MalformedURLException {
        Field urlField = captureCommand.getClass().getDeclaredField("url");
        urlField.setAccessible(true);
        urlField.set(captureCommand, new URL("https://google.com"));

        captureCommand.setEvent(messageReceivedEvent);

        when(messageChannel.sendFile(any(File.class), eq(CAPTURE_PNG), any(Message.class))).thenReturn(mock(MessageAction.class));
        MessageEmbed result = (MessageEmbed) captureCommand.call();
        verify(messageReceivedEvent).getChannel();
        verify(messageChannel).sendFile(any(File.class), eq("capture.png"), any(Message.class));
        verifyNoMoreInteractions(messageChannel);

        Assertions.assertNotNull(result, "Result must not be null.");
        Assertions.assertFalse(StringUtils.isBlank(result.getTitle()), "Title must not be blank.");
        Assertions.assertNotNull(result.getImage(), "Image must not be null.");
    }

    @DisplayName("Should not process event.")
    @Test
    void shouldNotProcessEvent() throws Exception {
        verifyDoNotProcessEvent(captureCommand, mock(Event.class));
    }
}
