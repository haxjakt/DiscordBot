package net.haxjakt;

import lombok.AllArgsConstructor;
import net.haxjakt.bot.jda.ExampleCommand;
import net.haxjakt.bot.jda.JDAManager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@AllArgsConstructor
public class Main implements CommandLineRunner {
    public final JDAManager manager;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) {
        manager.registerCommand("example", "test");
        manager.registerListener(new ExampleCommand());
    }

}