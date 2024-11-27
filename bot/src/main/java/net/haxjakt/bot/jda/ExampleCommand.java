package net.haxjakt.bot.jda;

import lombok.extern.log4j.Log4j2;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

@Log4j2
public class ExampleCommand extends ListenerAdapter {

    // example 1 command data
    public CommandData getCommandData() {
        return Commands.slash("yolo", "generated");
    }

    private void test(JDA jda) {
        jda.updateCommands().addCommands(//Collection<? extends CommandData> commandData
            Commands.slash("","")
        ).queue();
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

    @Override
    public void onMessageContextInteraction(MessageContextInteractionEvent event) {
        if (event.getName().equals("example")) {
            event.reply("hello").queue();
        }
    }

}
