package net.haxjakt.script.engine;

import net.haxjakt.script.components.Program;
import net.haxjakt.script.grammar.DiscordScriptBaseVisitor;
import net.haxjakt.script.grammar.DiscordScriptParser;

public class ProgramVisitor extends DiscordScriptBaseVisitor<Program> {
    Program program = null;

    @Override
    public Program visitProgram(DiscordScriptParser.ProgramContext ctx) {
        program = new Program(ctx.scriptDeclaration().IDENTIFIER().toString());

        CommandVisitor visitor = new CommandVisitor(program);
        program.addSlashCommand(visitor.visitCommand(ctx.command()));

        return program;
    }

}
