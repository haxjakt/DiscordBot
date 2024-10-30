package net.haxjakt.script.engine;

import lombok.RequiredArgsConstructor;
import net.haxjakt.script.components.MessageMenu;
import net.haxjakt.script.components.Program;
import net.haxjakt.script.grammar.DiscordScriptBaseVisitor;
import net.haxjakt.script.grammar.DiscordScriptParser;

@RequiredArgsConstructor
public class MessageMenuVisitor extends DiscordScriptBaseVisitor<MessageMenu> {
    private final Program program;
    @Override
    public MessageMenu visitMessageMenu(DiscordScriptParser.MessageMenuContext context) {
        MessageMenu menu = new MessageMenu(program, context.IDENTIFIER().toString());
        menu.setReplyMessage(context.printString().STRING().toString());
        return null;
    }
}
