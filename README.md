# SentinelBot
Code for the Sentinel bot we use on our discord server

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
