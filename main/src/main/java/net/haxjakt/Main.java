package net.haxjakt;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.haxjakt.bot.jda.JDAManager;
import net.haxjakt.core.ProgramRepository;
import net.haxjakt.script.api.ProgramTranslator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
//        manager.registerCommand("example", "test");
//        manager.registerListener(new ExampleCommand());
//        var program =
//        """
//        script Test;
//        command yolo1 {
//            print "hello";
//        }
//        """;
//
//        var javaProgram = translator.translate(program);
//        var byteCode = translator.compile(program);
//        var scriptName = translator.getScriptName();
//
//        var programRecord = ProgramRecord.builder()
//                .scriptName(scriptName)
//                .javaSourceCode(javaProgram)
//                .javaByteCodeBase64(new String(Base64.getEncoder().encode(byteCode)))
//                .build();
//        programRecord = repository.save(programRecord);
//        try {
//            manager.registerCommandById(programRecord.getId());
//        } catch (Throwable e) {
//            throw new RuntimeException(e);
//        }
    }

}