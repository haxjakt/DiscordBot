package net.haxjakt.script.engine;

import lombok.RequiredArgsConstructor;
import net.haxjakt.script.components.Program;
import net.haxjakt.script.components.SlashCommand;
import net.haxjakt.script.grammar.DiscordScriptBaseVisitor;
import net.haxjakt.script.grammar.DiscordScriptParser;

@RequiredArgsConstructor
public class CommandVisitor  extends DiscordScriptBaseVisitor<SlashCommand> {
    private final Program program;
    private SlashCommand command = null;
    @Override
    public SlashCommand visitCommand(DiscordScriptParser.CommandContext ctx) {
        command = new SlashCommand(program, ctx.commandIdentifiers().IDENTIFIER().toString());
        command.setReplyMessage(ctx.printString().STRING().toString());
        return command;
    }

}
