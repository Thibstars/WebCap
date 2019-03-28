/*
 * Copyright (c) 2019 Thibault Helsmoortel.
 *
 * This file is part of Discord Bot Base.
 *
 * Discord Bot Base is free software: you can redistribute it and/or modify
 *  t under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Discord Bot Base is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Discord Bot Base.  If not, see <https://www.gnu.org/licenses/>.
 */

package be.thibaulthelsmoortel.discordbotbase.commands;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import be.thibaulthelsmoortel.discordbotbase.commands.core.BotCommand;
import be.thibaulthelsmoortel.discordbotbase.config.DiscordBotEnvironment;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.dv8tion.jda.core.events.Event;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import picocli.CommandLine.Command;

/**
 * @author Thibault Helsmoortel
 */
class HelpCommandTest extends CommandBaseTest {

    private List<BotCommand> botCommands;

    @DisplayName("Should send help message.")
    @Test
    void shouldSendHelpMessage() {
        botCommands = new ArrayList<>();
        botCommands.add(new InviteCommand());
        botCommands.add(new GoodBotCommand());

        DiscordBotEnvironment environment = mock(DiscordBotEnvironment.class);
        when(environment.getCommandPrefix()).thenReturn("/");
        HelpCommand command = new HelpCommand(environment, botCommands);
        command.setEvent(messageReceivedEvent);
        String message = (String) command.call();

        Assertions.assertTrue(StringUtils.isNotBlank(message), "Message should not be empty.");
        botCommands.forEach(botCommand -> {
            Command annotation = botCommand.getClass().getAnnotation(Command.class);
            Assertions.assertTrue(StringUtils.contains(message, annotation.name()), "Message should contain command name.");
            Assertions.assertTrue(StringUtils.contains(message, parseDescription(annotation)), "Message should contain command name.");
        });

        verifyOneMessageSent(message);
    }

    @DisplayName("Should not process event.")
    @Test
    void shouldNotProcessEvent() throws Exception {
        HelpCommand command = new HelpCommand(mock(DiscordBotEnvironment.class), botCommands);

        verifyDoNotProcessEvent(command, mock(Event.class));
    }

    private String parseDescription(Command annotation) {
        String array = Arrays.toString(annotation.description());
        return array.substring(1, array.length() - 1);
    }

}
