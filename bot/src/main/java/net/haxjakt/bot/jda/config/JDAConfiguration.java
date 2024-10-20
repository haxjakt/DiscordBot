package net.haxjakt.bot.jda.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.haxjakt.bot.jda.ExampleCommand;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@Log4j2
public class JDAConfiguration {

    @Value("${discord.token}")
    private String token;

    @Bean
    public JDA jda() {
        JDA jda = null;
        try {
            jda = JDABuilder
                .createDefault(token)
                .build().awaitReady();
            log.info("Created JDA Bean");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return jda;
    }

}
