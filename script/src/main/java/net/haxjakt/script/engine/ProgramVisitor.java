package net.haxjakt.script.engine;

import net.haxjakt.script.components.Program;
import net.haxjakt.script.grammar.DiscordScriptBaseVisitor;
import net.haxjakt.script.grammar.DiscordScriptParser;

public class ProgramVisitor extends DiscordScriptBaseVisitor<Program> {
    Program program = null;

    @Override
    public Program visitProgram(DiscordScriptParser.ProgramContext ctx) {
        program = new Program(ctx.scriptDeclaration().IDENTIFIER().toString());
        FunctionalBlockVisitor visitor = new FunctionalBlockVisitor(program);

        ctx.block().forEach(block -> {
            switch (block.getChild(0)) {
                case DiscordScriptParser.CommandContext command -> program.addInteraction(visitor.visitCommand(command));
                case DiscordScriptParser.MessageMenuContext message -> program.addInteraction(visitor.visitMessageMenu(message));
                default -> throw new IllegalStateException("Unexpected value: " + block.getChild(0));
            }
        });

        return program;
    }

}
