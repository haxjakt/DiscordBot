package net.haxjakt.script.engine;

import lombok.RequiredArgsConstructor;
import net.haxjakt.script.components.BlockType;
import net.haxjakt.script.components.FunctionalBlock;
import net.haxjakt.script.components.Program;
import net.haxjakt.script.grammar.DiscordScriptBaseVisitor;
import net.haxjakt.script.grammar.DiscordScriptParser;

@RequiredArgsConstructor
public class FunctionalBlockVisitor extends DiscordScriptBaseVisitor<FunctionalBlock> {
    private final Program program;

    @Override
    public FunctionalBlock visitCommand(DiscordScriptParser.CommandContext ctx) {
        String blockName = ctx.IDENTIFIER().toString();
        String description = "generated";  // TODO grammar rule for description
        String replyMessage = ctx.instruction().replyDirective().STRING().toString();

        FunctionalBlock block = new FunctionalBlock(BlockType.SLASH_COMMAND, blockName, description, replyMessage);
        return block;
    }

    @Override
    public FunctionalBlock visitMessageMenu(DiscordScriptParser.MessageMenuContext ctx) {
        String blockName = ctx.IDENTIFIER().toString();
        String description = "";
        String replyMessage = ctx.instruction().replyDirective().STRING().toString();

        FunctionalBlock block = new FunctionalBlock(BlockType.MESSAGE_MENU, blockName, description, replyMessage);
        return block;
    }

}
