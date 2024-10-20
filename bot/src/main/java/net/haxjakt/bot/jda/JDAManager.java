package net.haxjakt.bot.jda;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JDAManager {
    @Value("${discord.testGuild}")
    private String testGuildId;

    public final JDA jda;

    @PostConstruct
    public void listMethods() {

    }

    public void registerCommand(String name, String description) {
        jda.getGuildById(testGuildId).upsertCommand(Commands.slash(name, description)).queue();
    }

    public void registerListener(ListenerAdapter adapter) {
        jda.addEventListener(adapter);
    }
}
