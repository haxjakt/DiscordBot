package net.haxjakt.script.components;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.tree.Tree;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@RequiredArgsConstructor
public class FunctionalBlock implements PrintableComponent, Comparable<FunctionalBlock> {
    @Getter
    private final BlockType type;
    private final String blockName;
    private final String blockDescription;
    private final String replyMessage;  // TODO replace with instruction list
    private Set<String> requiredImports = new TreeSet<>();
    {
        requiredImports.add("net.dv8tion.jda.api.interactions.commands.build.Commands");
    }

    private static final String BEGIN = "if (\"%s\".equals(event.getName())) {";
    private static final String REPLY = "event.reply(%s).queue();";
    private static final String GUARD = "return;";
    private static final String END = "}  // end fblock: %s";

    @Override
    public String printComponent(int indent) {
        final String NEW_LINE = System.lineSeparator();
        final StringBuilder interaction = new StringBuilder(tab(indent));

        interaction
                .append(BEGIN.formatted(blockName)).append(NEW_LINE)
                .append(tab(indent+1))
                .append(REPLY.formatted(replyMessage)).append(NEW_LINE)
                .append(tab(indent+1))
                .append(GUARD).append(NEW_LINE)
                .append(tab(indent))
                .append(END.formatted(blockName))
                .append(NEW_LINE);

        return interaction.toString();
    }

    public Set<String> getRequiredImports() {
        switch (type) {
            case SLASH_COMMAND -> requiredImports.add("net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent");
            case MESSAGE_MENU -> requiredImports.add("net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent");
            default -> throw new RuntimeException("Unsupported functional block type");
        }
        return requiredImports;
    }

    public String getCommandData() {
        return switch (type) {
            case SLASH_COMMAND -> "Commands.slash(\"%s\", \"%s\")".formatted(blockName, blockDescription);
            case MESSAGE_MENU -> "Commands.message(\"%s\")".formatted(blockName);
            default -> throw new RuntimeException("Invalid functional block type");
        };
    }

    @Override
    public int compareTo(FunctionalBlock o) {
        return 0;
    }

    private String tab(int indent) {
        return "    ".repeat(indent);
    }
}
