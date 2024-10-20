package net.haxjakt.bot.jda;

import lombok.extern.log4j.Log4j2;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

@Log4j2
public class ExampleCommand extends ListenerAdapter {

    public CommandData getCommandData() {
        return Commands.slash("yolo", "generated");
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("example")) {
            log.error("Received undeclared slash command with name: " + event.getName());
            return;
        }
        log.info("Received slash command!");
        event.reply("working!").queue();
    }

}
