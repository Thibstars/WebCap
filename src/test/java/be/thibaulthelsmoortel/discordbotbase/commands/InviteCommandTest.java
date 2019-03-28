/*
 * Copyright (c) 2019 Thibault Helsmoortel.
 *
 * This file is part of Discord Bot Base.
 *
 * Discord Bot Base is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
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
 *
 */

package be.thibaulthelsmoortel.discordbotbase.commands;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import be.thibaulthelsmoortel.discordbotbase.BaseTest;
import net.dv8tion.jda.bot.JDABot;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.requests.restaction.MessageAction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Thibault Helsmoortel
 */
class InviteCommandTest extends CommandBaseTest {

    private static final String EXPECTED_SCOPE = "scope=bot";
    private static final String PERMISSIONS_PARAM = "permissions=";

    private static final String INVITE_URL_WITH_PERMISSIONS = "https://discordapp.com/oauth2/authorize?scope=bot&client_id=446990121802618890&permissions=3072";
    private static final String INVITE_URL_NO_PERMISSIONS = "https://discordapp.com/oauth2/authorize?scope=bot&client_id=446990121802618890";

    private final InviteCommand inviteCommand;

    @Mock
    private JDABot jdaBot;

    @Autowired
    InviteCommandTest(InviteCommand inviteCommand) {
        this.inviteCommand = inviteCommand;
    }

    @BeforeEach
    void setUp() {
        JDA jda = mock(JDA.class);
        when(messageReceivedEvent.getJDA()).thenReturn(jda);
        when(jda.asBot()).thenReturn(jdaBot);
        when(jdaBot.getInviteUrl(Permission.EMPTY_PERMISSIONS)).thenReturn(INVITE_URL_NO_PERMISSIONS);
        when(messageReceivedEvent.getChannel()).thenReturn(messageChannel);
        when(messageChannel.sendMessage(anyString())).thenReturn(mock(MessageAction.class));
    }

    @DisplayName("Should return invite url without permissions.")
    @Test
    void shouldReturnInviteUrlWithoutPermissions() {
        inviteCommand.setEvent(messageReceivedEvent);

        String message = (String) inviteCommand.call();

        Assertions.assertNotNull(message, "Invite url must not be null.");
        Assertions.assertTrue(message.contains(EXPECTED_SCOPE), "Scope must be correct.");
        Assertions.assertFalse(message.contains(PERMISSIONS_PARAM), "No permissions should be provided.");

        verifyOneMessageSent();
    }

    @DisplayName("Should return invite url with permissions.")
    @Test
    void shouldReturnInviteUrlWithPermissions() {
        when(jdaBot.getInviteUrl(any(Permission[].class))).thenReturn(INVITE_URL_WITH_PERMISSIONS);
        inviteCommand.setEvent(messageReceivedEvent);

        String message = (String) inviteCommand.call();
        Assertions.assertNotNull(message, "Invite url must not be null.");
        Assertions.assertTrue(message.contains(EXPECTED_SCOPE), "Scope must be correct.");
        Assertions.assertTrue(message.contains(PERMISSIONS_PARAM), "Permissions should be provided.");

        verifyOneMessageSent();
    }

    @DisplayName("Should not process event.")
    @Test
    void shouldNotProcessEvent() throws Exception {
        verifyDoNotProcessEvent(inviteCommand, mock(Event.class));
    }

    private void verifyOneMessageSent() {
        verify(messageReceivedEvent).getChannel();
        verify(messageChannel).sendMessage(anyString());
        verifyNoMoreInteractions(messageChannel);
    }
}
