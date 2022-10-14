# minecraft-authlib
This is the source code of Minecraft's `authlib 1.6.25` which you can use from Minecraft 1.12.2 up to Minecraft 1.16.3 inclusive (*1.16.4 introduced social interactions so this library got changed to v2*) both on the client and the server.

## Why does this even exist?
Well, looking on things like [authlib-injector](https://github.com/yushijinhun/authlib-injector/) and so on I've figured out "why inject when can change directly?".

Modifying and using this library allows you to use skins and capes from websites other than "mojang.com" and "minecraft.net" and pretty much alter Minecraft's authentication capabilities.

> See [WHITELISTED_DOMAINS](https://github.com/tinytengu/minecraft-authlib/blob/main/src/main/java/com/mojang/authlib/yggdrasil/YggdrasilMinecraftSessionService.java#L48) and [skin signature verification](https://github.com/tinytengu/minecraft-authlib/blob/main/src/main/java/com/mojang/authlib/yggdrasil/YggdrasilMinecraftSessionService.java#L127)

## What is up with this particular version?
`authlib 1.5` was introduced in Minecraft 1.7.6 and is used in all release versions up to 1.15.2, just with different patch versions.

Minecraft 1.16 introduced `authlib 1.6.25` which differs from the previous minor version ONLY by the ability to specify the URLs of *auth*, *session* and *accounts* hosts directly from Java properties (-D) when launching Minecraft instead of having them hardcoded in `YggdrasilMinecraftSessionService`, this allows to use custom website for handling all authentication stuff, including logging in on serves with `online=true`.

Example:
```bash
java -Dminecraft.api.session.host=https://your.site -Dminecraft.api.account.host=https://your.site -Dminecraft.api.auth.host=https://your.site -Djava.library.path=...
```

## How do I use this version instead of vanilla 1.5 version in Minecraft release prior to 1.16.4?
### Client
On the client, this authlib thing sits in `.minecraft/libraries/com/mojang/authlib/SOMEVERSION/authlib-SOMEVERSION.jar` so you can just replace this jar file with yours, there's no difference between them whatsoever apart from the environment stuff, so Minecraft won't care and will just use this new one library.
### Server
On the server, you just open `server.jar` (or whatever you named it) with an archive manager (i.e. WinRar) and follow `com/mojang/authlib` path, you'll see literally the same files as in the client, so you just need to replace all the files with 1.6.25 ones, that's it. Now you can launch your 1.12.2 server with `-Dminecraft.api.session...` configs with no problem at all.

### License
I don't own anything of this library, so if something happens, Mojang will sue you 🗿