package net.haxjakt.bot.jda;

import lombok.extern.log4j.Log4j2;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@Log4j2
public class ExampleCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("example")) {
            log.error("Received undeclared slash command with name: " + event.getName());
        }
        log.info("Received slash command!");
        event.reply("working!").queue();
    }

}
