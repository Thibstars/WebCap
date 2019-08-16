# WebCap [![Build Status](https://travis-ci.org/Thibstars/Discord-Bot-Base.svg?branch=master)](https://travis-ci.org/Thibstars/WebCap) [![codecov](https://codecov.io/gh/Thibstars/WebCap/branch/master/graph/badge.svg)](https://codecov.io/gh/Thibstars/WebCap) # 
Discord bot capturing screenshots of web pages.

## Features ##
At the moment, WebCap contains only one main feature; the capability of capturing a screenshot from a specified url.

Send the message `/capture <url>` to a channel with the active bot to retrieve a screen capture of the provided website.

## Hosting ##
### Public ###
This bot is currently not publicly hosted.

### Private ###
This bot can be self-hosted.

#### Usage ####

In order to run the application, one must first add a Discord bot token to `bot.token` in the `token.properties` file.
**Note that a bot token should never be committed in git!**

When running directly using `java -jar` you can also pass your token as a first run argument instead. This is also the used approach in the `Dockerfile`.

##### Docker #####
When running the application from the `Dockerfile` make sure to add a new `BOT_TOKEN` environment variable with the bot token as value so it can be picked up 
in the underlying `java -jar` entrypoint command.

Do note that your docker instance must have access to sufficient disk space, since an instance of `chromium-browser` will be installed during deploy.