# DiscordScript Language Specification

## 1. General script structure

The script must contain the script declaration (`script-decl`) on the first line, followed by blocks describing features of the script (`f-block`).

### 1.1 `script-decl`

This line contains the name of the script, which will be used to identify all the scripts of one user. One user cannot have duplicated scripts with the same name. The syntax is `script <script-name> ;`

### 1.2 `f-block`

Functional blocks are the top level components of the script (excluding the script declaration). There are different types of blocks, covering the scenarios which can be handled by the bot runtime. Currently, the existing functional blocks are (with their specific keyword inside brackets):
- Slash command definition (`command`)
- Message context menu definition (`message-menu`)
- User context menu definition (`user-menu`)
- Modal handler (`modal`)
- Button handler (`button`)
- Generic event handler (`on-event`)

In general, the structure of functional blocks is `<!keyword> <identifier> { instruction+ }`. The exception here is the slash command definition which may have a different structure which will be described in the next chapter. More details about instructions will be presented in the following chapters as well.

## 2. Slash command definition

This functional block is used to define a discord slash command and its behaviour at runtime when it is registered into the bot. The script can have multiple such blocks as long as their identifier is unique. If there is another slash command with the same name as the identifier, the current interaction will not be loaded when the register command is called, without affecting the other slash commands defined in the script.
