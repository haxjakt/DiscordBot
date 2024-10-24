package net.haxjakt.bot.jda;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.haxjakt.core.ProgramRecord;
import net.haxjakt.core.ProgramRepository;
import net.haxjakt.script.engine.ProgramTranslator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
@RequiredArgsConstructor
@Log4j2
public class CompileInteraction extends ListenerAdapter {
    private final String COMMAND_NAME = "Compile";
    private final JDA jda;
    private final ProgramTranslator translator;
    private final ProgramRepository repository;
    @Value("${discord.testGuild}")
    private String testGuild;

    @PostConstruct
    public void registerCommand() {
        jda.getGuildById(testGuild).upsertCommand(
                Commands.message(COMMAND_NAME)
        ).queue();
        jda.addEventListener(this);
        log.info("Registered command '" + COMMAND_NAME + "'");
    }

    @Override
    public void onMessageContextInteraction(final MessageContextInteractionEvent event) {
        if (!event.getName().equals(COMMAND_NAME)) { return; }
        log.info("Received compile command request");

        String message = event.getTarget().getContentRaw();
        if (!message.startsWith("```rofl") || !message.endsWith("```")) {
            event.reply("This message does not cotain ROFL code").queue();
            return;
        }

        String id = translateAndSaveScript(message.substring(7, message.length() - 3));
        event.reply("Successfully compiled and saved program with id: " + id).queue();
    }

    private String translateAndSaveScript(final String scriptContent) {
        var javaProgram = translator.translate(scriptContent);
        var byteCode = translator.compile(scriptContent);
        var scriptName = translator.getScriptName();

        ProgramRecord record = ProgramRecord.builder()
                .scriptName(scriptName)
                .javaSourceCode(javaProgram)
                .javaByteCodeBase64(new String(Base64.getEncoder().encode(byteCode)))
                .build();
        record = repository.save(record);

        return record.getId();
    }

}
