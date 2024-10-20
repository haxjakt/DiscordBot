package net.haxjakt.script.engine;


import net.haxjakt.script.grammar.DiscordScriptBaseVisitor;
import net.haxjakt.script.grammar.DiscordScriptLexer;
import net.haxjakt.script.grammar.DiscordScriptParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.springframework.stereotype.Component;

import javax.tools.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.*;

@Component
public class ProgramTranslator extends DiscordScriptBaseVisitor<String> {
    private String scriptName;
    private String commandName;
    private String responseMessage;

    public String getScriptName() { return scriptName; }

    public String translate(String program) {
        CharStream cs = CharStreams.fromString(program);
        DiscordScriptLexer lexer = new DiscordScriptLexer(cs);
        CommonTokenStream cts = new CommonTokenStream(lexer);
        DiscordScriptParser parser = new DiscordScriptParser(cts);
        return visitProgram(parser.program());
    }

    public byte[] compile(String program) {
        var sourceCode = translate(program);
        JavaFileObject sourceFile = new SourceFile(scriptName, sourceCode);

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager stdFileManager = compiler.getStandardFileManager(null, null, null);

        CustomFilemanager fileManager = new CustomFilemanager(stdFileManager);

        List<String> options = new ArrayList<>();
        options.add("-classpath");
        options.add(System.getProperty("java.class.path"));
        options.add("-proc:full");
        options.add("-parameters");

        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, null, options, null, List.of(sourceFile));
        if (!task.call()) {
            throw new RuntimeException("Compilation Failed");
        }

        return fileManager.getByteCode(scriptName);
    }

    @Override
    public String visitProgram(DiscordScriptParser.ProgramContext ctx) {
        scriptName = ctx.scriptDeclaration().IDENTIFIER().toString();
        visit(ctx.command());

        return
        """
        import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
        import net.dv8tion.jda.api.interactions.commands.build.CommandData;
        import net.dv8tion.jda.api.interactions.commands.build.Commands;
        import net.dv8tion.jda.api.hooks.ListenerAdapter;
        
        public class %s extends ListenerAdapter {
            
            public CommandData getCommandData() {
                return Commands.slash("%s", "generated");
            }
                
            @Override
            public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
                if (!"%s".contains(event.getName())) return;
                event.reply(%s).queue();
            }
        }
        """.formatted(scriptName, commandName, commandName, responseMessage);
    }

    @Override
    public String visitCommand(DiscordScriptParser.CommandContext ctx) {
        commandName = ctx.commandIdentifiers().IDENTIFIER().toString();
        responseMessage = ctx.printString().STRING().toString();
        return "";
    }

}
