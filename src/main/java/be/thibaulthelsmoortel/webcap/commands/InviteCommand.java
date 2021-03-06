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

import be.thibaulthelsmoortel.webcap.commands.candidates.PermissionCandidates;
import be.thibaulthelsmoortel.webcap.commands.converters.PermissionConverter;
import be.thibaulthelsmoortel.webcap.commands.core.BotCommand;
import net.dv8tion.jda.bot.JDABot;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

/**
 * Command providing an invitation url for the bot.
 *
 * @author Thibault Helsmoortel
 */
@Command(name = "invite", description = "Provides an invitation url for the bot.")
@Component
public class InviteCommand extends BotCommand {

    @Option(names = {"-p", "--permission"}, description = "Target bot permission.", arity = "0..1")
    private boolean[] permissionsRequested = new boolean[0];

    @Parameters(paramLabel = "PERMISSION", description = "Target bot permissions. Candidates: ${COMPLETION-CANDIDATES}", arity = "0..*",
        converter = PermissionConverter.class, completionCandidates = PermissionCandidates.class)
    private Permission[] permissions;

    @Override
    public Object call() {
        String message = null;
        if (getEvent() instanceof MessageReceivedEvent) {
            JDABot jdaBot = getEvent().getJDA().asBot();

            if (permissionsRequested.length > 0 && permissions != null && permissions.length > 0) {
                message = jdaBot.getInviteUrl(permissions);
            } else {
                message = jdaBot.getInviteUrl(Permission.EMPTY_PERMISSIONS);
            }

            ((MessageReceivedEvent) getEvent()).getChannel().sendMessage(message).queue();
        }

        reset();

        return message;
    }

    // Visible for testing
    void setPermissionsRequested(boolean[] permissionsRequested) {
        this.permissionsRequested = permissionsRequested;
    }

    // Visible for testing
    void setPermissions(Permission[] permissions) {
        this.permissions = permissions;
    }

    private void reset() {
        permissionsRequested = new boolean[0];
        permissions = null;
    }
}
