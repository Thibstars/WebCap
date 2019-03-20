package be.thibaulthelsmoortel.discordbotbase.config;

import be.thibaulthelsmoortel.discordbotbase.exceptions.MissingTokenException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Discord bot environment properties.
 *
 * @author Thibault Helsmoortel
 */
@ConfigurationProperties(prefix = "bot")
public class DiscordBotEnvironment implements InitializingBean {

    private String token;

    private String author;

    private String version;

    private String commandPrefix;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCommandPrefix() {
        return commandPrefix;
    }

    public void setCommandPrefix(String commandPrefix) {
        this.commandPrefix = commandPrefix;
    }

    @Override
    public void afterPropertiesSet() {
        if (StringUtils.isBlank(token)) {
            throw new MissingTokenException();
        }
    }
}
