package net.haxjakt.bot.jda;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.haxjakt.bot.dynamic.CustomClassLoader;
import net.haxjakt.core.ProgramRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Base64;

@Component
@RequiredArgsConstructor
@Log4j2
public class JDAManager {
    @Value("${discord.testGuild}")
    private String testGuildId;

    public final JDA jda;
    public final ProgramRepository repository;

    @PostConstruct
    public void listMethods() {

    }

    public void registerCommandById(String id) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        log.info(STR."Registering command defined in program with id: \{id}");
        var maybeProgram = repository.findById(id);
        if (maybeProgram.isEmpty()) {
            log.error(STR."Cannot find program with id: \{id}");
            return;
        }
        var program = maybeProgram.get();
        log.info("Found program with name: {} matching id: {}", program.getScriptName(), id);
        
        byte[] byteCode = Base64.getDecoder().decode(program.getJavaByteCodeBase64().getBytes());
        CustomClassLoader loader = new CustomClassLoader();
        Class<?> clazz = loader.defineClass(program.getScriptName(), byteCode);
        ListenerAdapter instance = (ListenerAdapter) clazz.getDeclaredConstructor().newInstance();
        Method dataMethod = clazz.getDeclaredMethod("getCommandData");

        CommandData commandData = (CommandData) dataMethod.invoke(instance);

        addDynamicCommand(commandData, instance);

        System.out.println(STR."Registered program with id: \{id}");
    }

    private addDynamicCommand(CommandData commandData, ListenerAdapter adapter) {
        jda.getGuildById(testGuildId).upsertCommand(commandData).queue();
        jda.addEventListener(adapter);
    }
    
    public void registerCommand(String name, String description) {
        jda.getGuildById(testGuildId).upsertCommand(Commands.slash(name, description)).queue();
    }

    public void registerListener(ListenerAdapter adapter) {
        jda.addEventListener(adapter);
    }
}
