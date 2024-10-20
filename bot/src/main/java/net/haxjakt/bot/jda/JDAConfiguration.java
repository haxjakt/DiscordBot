package net.haxjakt.bot.jda;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JDAConfiguration {

    @Value("${discord.token}")
    private String token;

//    @Bean
//    public JDA jda() {
//         return JDABuilder.createDefault(token).build();
//    }

    @Bean
    public String helloWorld() { return token; }

}
