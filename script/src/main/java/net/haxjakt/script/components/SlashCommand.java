package net.haxjakt.script.components;

import lombok.Getter;
import lombok.Setter;

public class SlashCommand implements PrintableComponent, Comparable<SlashCommand> {

    @Getter
    private final String name;

    @Setter
    private String replyMessage;

    private Program parent;


    public SlashCommand(final Program program, final String commandName) {
        parent = program;
        name = commandName;
        replyMessage = "";
    }

    public String getCommandDataDeclaration() {
        return "Commands.slash(\"%s\", \"generated\")".formatted(name);
    }

    @Override
    public String printComponent(int ident) {
        final String nl = System.lineSeparator();
        final StringBuilder command = new StringBuilder(tab(ident));

        command.append("if (\"").append(name).append("\".equals(event.getName())) {").append(nl);

        // for each instruction, print it in the program
        command.append(tab(ident+1))
                .append("event.reply(").append(replyMessage).append(").queue();").append(nl);
        command.append(tab(ident+1))
                .append("return;").append(nl);

        command.append(tab(ident)).append("}  // end slash-command: ").append(name).append(nl);
        return command.toString();
    }

    private String tab(int ident) {
        return "    ".repeat(ident);
    }

    @Override
    public int compareTo(SlashCommand o) {
        return name.compareTo(o.getName());
    }
}
