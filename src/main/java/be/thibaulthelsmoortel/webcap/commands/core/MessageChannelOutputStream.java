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

import java.io.OutputStream;
import net.dv8tion.jda.core.entities.MessageChannel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * OutputStream able to write to a set message channel.
 *
 * @author Thibault Helsmoortel
 */
@Component
public class MessageChannelOutputStream extends OutputStream {

    private MessageChannel messageChannel;

    @Override
    public void write(int b) {
        // It's tempting to use writer.write((char) b), but that may get the encoding wrong
        // This is inefficient, but it works
        write(new byte[] {(byte) b}, 0, 1);
    }

    @Override
    public void write(byte b[], int off, int len) {
        String content = b != null ? new String(b, off, len) : null;
        if (StringUtils.isNotBlank(content)) {
            messageChannel.sendMessage(content).queue();
        }
    }

    public MessageChannel getMessageChannel() {
        return messageChannel;
    }

    public void setMessageChannel(MessageChannel messageChannel) {
        this.messageChannel = messageChannel;
    }
}
