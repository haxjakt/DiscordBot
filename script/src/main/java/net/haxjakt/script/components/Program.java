package net.haxjakt.script.components;

import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

public class Program implements PrintableComponent{

    private final String NL = System.lineSeparator();

    /** The name of the generated class */
    @Getter
    private final String programName;

    /** The fully qualified names of the classes needed to be imported */
    private final Set<String> fqnImports = new TreeSet<>();

    /** interactions declared in the program */
    private final Set<FunctionalBlock> interactions = new TreeSet<>();

    private final Map<String, ProgramVariable> variables = new HashMap<>();

    public Program(final String name) {
        programName = name;
        fqnImports.add("net.dv8tion.jda.api.hooks.ListenerAdapter");
        fqnImports.add("java.util.List");
        fqnImports.add("net.dv8tion.jda.api.interactions.commands.build.CommandData");
    }

    public void doImport(final String fullyQualifiedName) {
        fqnImports.add(fullyQualifiedName);
    }

    public void addVariable(final ProgramVariable variable) {
        variables.put(variable.getVariableName(), variable);
    }

    public void addInteraction(FunctionalBlock block) {
        fqnImports.addAll(block.getRequiredImports());
        interactions.add(block);
    }

    @Override
    public String printComponent(int ident) {
        StringBuilder program = new StringBuilder();

        // print import statements
        fqnImports.forEach(fqnImport -> {
            program.append("import ").append(fqnImport).append(";").append(NL);
        });
        program.append(NL);

        // print class definition: begin
        program.append("public class ").append(programName)
            .append(" extends ListenerAdapter {").append(NL.repeat(2));

        program.append(printCommandData());
        program.append(printSlashCommandListener());
        program.append(printMessageMenuListener());

        // print class definition: end
        program.append("}  // end class: ").append(programName).append(NL.repeat(2));

        return program.toString();
    }

    private String printCommandData() {
        StringBuilder commandData = new StringBuilder(tab(1));
        commandData.append("public List<CommandData> getCommandData() {").append(NL);
        commandData.append(tab(2)).append("return List.of(").append(NL).append(tab(3));

        String delimiter = ',' + NL + tab(3);
        commandData.append(
                interactions.stream().map(FunctionalBlock::getCommandData).collect(Collectors.joining(delimiter))
        );

        commandData.append(NL).append(tab(2)).append(");").append(NL);
        commandData.append(tab(1)).append("}  // end method: getCommandData").append(NL.repeat(2));

        return commandData.toString();
    }

    private String printSlashCommandListener() {
        boolean hasSlashCommands = interactions.stream().anyMatch(interaction -> interaction.getType().equals(BlockType.SLASH_COMMAND));
        if (!hasSlashCommands) return "";

        StringBuilder listener = new StringBuilder();
        listener.append("    @Override").append(NL)
                .append("    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {")
                .append(NL);

        interactions.stream()
                .filter(interaction -> interaction.getType().equals(BlockType.SLASH_COMMAND))
                .forEach(slashCommand -> {
            listener.append(slashCommand.printComponent(2));
        });

        listener.append("    }  // end method: onSlashCommandInteraction").append(NL.repeat(2));
        return listener.toString();
    }

    private String printMessageMenuListener() {
        StringBuilder listener = new StringBuilder();
        listener.append("    @Override").append(NL)
                .append("    public void onMessageContextInteraction(MessageContextInteractionEvent event) {")
                .append(NL);

        interactions
                .stream().filter(interaction -> interaction.getType().equals(BlockType.MESSAGE_MENU))
                .forEach(messageMenu -> {
            listener.append(messageMenu.printComponent(2));
        });

        listener.append("    }  // end method: onMessageMenuInteraction").append(NL.repeat(2));
        return listener.toString();
    }

    private String tab(int ident) {
        return "    ".repeat(ident);
    }
}
