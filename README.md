# SentinelBot
Features: Role management and Polls.
This bot allows for easy setup of role management via reactions and additionaly provides some poll functionality that is continuously expanded upon.

### Discord Server
Join our development server to discuss features & bugs or simply try out the commands:
https://discord.gg/gNrEeBg

### Bot Invite link
If you want to add the bot to your server, here you go:
https://discordapp.com/api/oauth2/authorize?client_id=276714267388936192&permissions=268789824&scope=bot

### Install
Install gradle using you favourite package manager.
In Eclipse: Open *Help* -> *Eclipse Marketplace...*, search for "Buildship Gradle Integration 2.0" and install the plugin.
Import this project by selecting *File* -> *Import* -> *Gradle* -> *Existing Gradle Project*.

### Database
I am using a mysql database for several features.
Some of them need unicode emoji support so make sure you configured your database correctly.
Here are the necessary tables:
```
EmojiBinds
  pK: int, autogenerate
  channelId: bigInt
  isUnicode: bit
  unicodeString: varchar utf8mb4
  emojiId: bigInt
  roleId: bigInt
```

```
RoleReactionAssignmentChannel
  channelId: bigInt 
```

### Config File
Run
```
cp bot.properties.template bot.properties
```
Edit the bot.properties with your correct database string, id's and tokens.
