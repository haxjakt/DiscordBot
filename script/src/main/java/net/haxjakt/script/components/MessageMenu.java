package net.haxjakt.script.components;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class MessageMenu implements PrintableComponent, Comparable<MessageMenu> {
    private final Program program;
    @Getter
    private final String name;
    @Setter
    private String replyMessage = null;

    public String getCommandDataDeclaration() {
        return "Commands.message(\"%s\")".formatted(name);
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

        command.append(tab(ident)).append("}  // end message-menu: ").append(name).append(nl);
        return command.toString();
    }

    private String tab(int ident) {
        return "    ".repeat(ident);
    }

    @Override
    public int compareTo(MessageMenu o) {
        return name.compareTo(o.getName());
    }
}
