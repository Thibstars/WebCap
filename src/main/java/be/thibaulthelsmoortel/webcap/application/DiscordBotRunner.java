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

package be.thibaulthelsmoortel.webcap.application;

import be.thibaulthelsmoortel.webcap.commands.core.CommandExecutor;
import be.thibaulthelsmoortel.webcap.config.DiscordBotEnvironment;
import be.thibaulthelsmoortel.webcap.exceptions.MissingTokenException;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Component running the discord bot.
 *
 * @author Thibault Helsmoortel
 */
@Component
@Slf4j
public class DiscordBotRunner extends ListenerAdapter implements CommandLineRunner {

    private final DiscordBotEnvironment discordBotEnvironment;
    private final CommandExecutor commandExecutor;

    @Autowired
    public DiscordBotRunner(DiscordBotEnvironment discordBotEnvironment,
        CommandExecutor commandExecutor) {
        this.discordBotEnvironment = discordBotEnvironment;
        this.commandExecutor = commandExecutor;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message message = event.getMessage();
        if (processMessage(message)) {
            String msg = message.getContentDisplay();
            handleMessage(event, msg);
        }
    }

    private boolean processMessage(Message message) {
        return (discordBotEnvironment.isProcessBotMessages() && message.getAuthor().isBot()) || !message.getAuthor().isBot();
    }

    private void handleMessage(MessageReceivedEvent event, String msg) {
        if (msg.startsWith(discordBotEnvironment.getCommandPrefix())) {
            event.getChannel().sendTyping().queue();

            String parsedMessage = msg.substring(discordBotEnvironment.getCommandPrefix().length());

            commandExecutor.tryExecute(event, parsedMessage);
        }
    }

    @Override
    public void run(String... args) {
        String token;
        if (StringUtils.isNotBlank(discordBotEnvironment.getToken())) {
            token = discordBotEnvironment.getToken();
        } else {
            // Take token as first run arg (for example for when running from docker with an ENV variable)
            if (args != null && args.length > 0) {
                token = args[0];
            } else {
                token = null;
            }
        }

        if (StringUtils.isBlank(token)) {
            throw new MissingTokenException();
        }

        try {
            new JDABuilder(AccountType.BOT)
                .setToken(token)
                .addEventListener(this)
                .build()
                .awaitReady();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }
}
