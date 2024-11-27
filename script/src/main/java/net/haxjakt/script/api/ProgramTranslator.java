package net.haxjakt.script.api;


import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import net.haxjakt.script.components.Program;
import net.haxjakt.script.engine.ProgramVisitor;
import net.haxjakt.script.jcompiler.CustomFilemanager;
import net.haxjakt.script.jcompiler.SourceFile;
import net.haxjakt.script.grammar.DiscordScriptBaseVisitor;
import net.haxjakt.script.grammar.DiscordScriptLexer;
import net.haxjakt.script.grammar.DiscordScriptParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.springframework.stereotype.Component;

import javax.tools.*;
import java.util.*;

@Log4j2
@Component
public class ProgramTranslator {
    private ProgramDTO programDTO = new ProgramDTO();

    public void init(final String programText) {
        programDTO.setProgramText(programText);
    }

    public void run() {
        Program program = translate(programDTO.getProgramText());
        programDTO.setProgramName(program.getProgramName());
        programDTO.setProgramJava(program.printComponent(0));

        log.info(programDTO.getProgramJava());

        programDTO.setProgramByte(compile(program));
    }

    public ProgramDTO getResult() {
        return programDTO;
    }

    private Program translate(String programText) {
        CharStream cs = CharStreams.fromString(programText);
        DiscordScriptLexer lexer = new DiscordScriptLexer(cs);
        CommonTokenStream cts = new CommonTokenStream(lexer);

        DiscordScriptParser parser = new DiscordScriptParser(cts);

        ProgramVisitor programVisitor = new ProgramVisitor();
        return programVisitor.visitProgram(parser.program());
    }

    private byte[] compile(Program program) {
        JavaFileObject sourceFile = new SourceFile(program.getProgramName(), program.printComponent(0));

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager stdFileManager = compiler.getStandardFileManager(null, null, null);

        CustomFilemanager fileManager = new CustomFilemanager(stdFileManager);

        List<String> options = new ArrayList<>();
        options.add("-classpath");
        options.add(System.getProperty("java.class.path"));
        options.add("-proc:full");

        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, null, options, null, List.of(sourceFile));
        if (!task.call()) {
            throw new RuntimeException("Compilation Failed");
        }

        return fileManager.getByteCode(program.getProgramName());
    }

}
