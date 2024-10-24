package net.haxjakt.bot.jda;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Log4j2
class RegisterCommand extends ListenerAdapter {
    private final String COMMAND_NAME = "register";
    private final String COMMAND_ARG = "id";
    private final JDA jda;
    private final JDAManager manager;
    @Value("${discord.testGuild}")
    private String testGuild;

    @PostConstruct
    public void registerCommand() {
        jda.getGuildById(testGuild).upsertCommand(
                Commands.slash(COMMAND_NAME, "Registers the following command")
                        .addOption(OptionType.STRING, COMMAND_ARG, "The id of the program")
        ).queue();
        jda.addEventListener(this);
        log.info("Registered command '" + COMMAND_NAME + "'");
    }

    @Override
    public void onSlashCommandInteraction(final SlashCommandInteractionEvent event) {
        if (!event.getName().equals(COMMAND_NAME)) {
            return;
        }
        log.info("Received register command request");

        try {
            String id = Optional.ofNullable(event.getOption(COMMAND_ARG)).orElseThrow().getAsString();
            manager.registerCommandById(id);
            event.reply("Command registered! **it may take some minutes for Discord to update the context")
                    .queue();
        } catch (Throwable t) {
            event.reply("Could not register program").queue();
        }
    }
}
