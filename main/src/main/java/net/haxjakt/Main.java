package net.haxjakt;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.haxjakt.bot.jda.ExampleCommand;
import net.haxjakt.bot.jda.JDAManager;
import net.haxjakt.core.ProgramRecord;
import net.haxjakt.core.ProgramRepository;
import net.haxjakt.script.engine.ProgramTranslator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.reflect.InvocationTargetException;
import java.util.Base64;

@SpringBootApplication
@AllArgsConstructor
@Log4j2
public class Main implements CommandLineRunner {
    public final JDAManager manager;
    public final ProgramTranslator translator;
    public final ProgramRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) {
        manager.registerCommand("example", "test");
        manager.registerListener(new ExampleCommand());
        var program =
        """
        script Test;
        command yolo1 {
            print "hello";
        }
        """;

        var javaProgram = translator.translate(program);
        var byteCode = translator.compile(program);
        var scriptName = translator.getScriptName();

        var programRecord = ProgramRecord.builder()
                .scriptName(scriptName)
                .javaSourceCode(javaProgram)
                .javaByteCodeBase64(new String(Base64.getEncoder().encode(byteCode)))
                .build();
//        System.out.println("Record before saving: " + programRecord);
        programRecord = repository.save(programRecord);
//        System.out.println("Record after savind: " + programRecord);
//        var mongoProgram = repository.findByScriptName(programRecord.getScriptName());
//        System.out.println("Read program with ID: " + mongoProgram.get().getId());
//        System.out.println("Full mongo program: " + mongoProgram.get().getJavaByteCodeBase64());

//        var dbProgram = repository.findById(programRecord.getId());
        try {
            manager.registerCommandById(programRecord.getId());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

}