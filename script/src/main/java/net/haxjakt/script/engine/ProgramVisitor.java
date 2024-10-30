package net.haxjakt.script.engine;

import net.haxjakt.script.components.MessageMenu;
import net.haxjakt.script.components.Program;
import net.haxjakt.script.grammar.DiscordScriptBaseVisitor;
import net.haxjakt.script.grammar.DiscordScriptParser;

public class ProgramVisitor extends DiscordScriptBaseVisitor<Program> {
    Program program = null;

    @Override
    public Program visitProgram(DiscordScriptParser.ProgramContext ctx) {
        program = new Program(ctx.scriptDeclaration().IDENTIFIER().toString());


        ctx.block().forEach(block -> {
            switch (block.getChild(0)) {
                case DiscordScriptParser.CommandContext command -> {
                    CommandVisitor visitor = new CommandVisitor(program);
                    program.addSlashCommand(visitor.visitCommand(command));
                }
                case DiscordScriptParser.MessageMenuContext message -> {
                    MessageMenuVisitor visitor = new MessageMenuVisitor(program);
                    program.addMessageMenu(visitor.visitMessageMenu(message));
                }
                default -> throw new IllegalStateException("Unexpected value: " + block.getChild(0));
            }

        });

        return program;
    }

}
