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

package be.thibaulthelsmoortel.webcap.commands.core;

import java.io.PrintStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Help.Ansi;

/**
 * Class responsible for command execution.
 *
 * @author Thibault Helsmoortel
 */
@Component
@Slf4j
public class CommandExecutor {

    private final List<BotCommand> botCommands;
    private final MessageChannelOutputStream messageChannelOutputStream;
    private final PrintStream printStream;

    @Autowired
    public CommandExecutor(List<BotCommand> botCommands, MessageChannelOutputStream messageChannelOutputStream) {
        this.botCommands = botCommands;
        this.messageChannelOutputStream = messageChannelOutputStream;
        printStream = new PrintStream(messageChannelOutputStream);
    }

    /**
     * Tries to execute a command.
     *
     * @param event the raised JDA event
     * @param commandMessage the command message (stripped from its prefix)
     * @return true if the command was executed, false if otherwise
     */
    public boolean tryExecute(MessageReceivedEvent event, String commandMessage) {
        AtomicBoolean commandRecognised = new AtomicBoolean(false);

        if (StringUtils.isNotBlank(commandMessage)) {
            botCommands.forEach(command -> {
                Command commandType = command.getClass().getAnnotation(Command.class);
                String commandName = commandType.name();

                if (commandMessage.split(" ")[0].equals(commandName)) {
                    commandRecognised.set(true);
                    command.setEvent(event);
                    String args = commandMessage.substring(commandMessage.indexOf(commandType.name()) + commandType.name().length()).trim();

                    messageChannelOutputStream.setMessageChannel(event.getChannel());
                    if (StringUtils.isNotBlank(args)) {
                        CommandLine.call(command, printStream, printStream, Ansi.OFF, args.split(" "));
                    } else {
                        CommandLine.call(command, printStream, printStream, Ansi.OFF);
                    }
                }
            });

            if (commandRecognised.get()) {
                log.debug("Executed command: {}.", commandMessage);
            } else {
                log.debug("Command not recognized: {}.", commandMessage);
                event.getChannel().sendMessage("Command not recognized...").queue();
            }
        }

        return commandRecognised.get();
    }

}
