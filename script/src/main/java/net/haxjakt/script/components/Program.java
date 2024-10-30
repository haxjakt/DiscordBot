package net.haxjakt.script.components;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Program implements PrintableComponent{

    private final String NL = System.lineSeparator();

    /** The name of the generated class */
    @Getter
    private final String programName;

    /** The fully qualified names of the classes needed to be imported */
    private final Set<String> fqnImports = new TreeSet<>();

    /** interactions declared in the program */
    private final Set<SlashCommand> slashCommands = new TreeSet<>();
    private final Set<MessageMenu> messageMenus = new TreeSet<>();

    public Program(final String name) {
        programName = name;
        fqnImports.add("net.dv8tion.jda.api.hooks.ListenerAdapter");
        fqnImports.add("java.util.List");
    }

    public void doImport(final String fullyQualifiedName) {
        fqnImports.add(fullyQualifiedName);
    }

    public void addSlashCommand(final SlashCommand slashCommand) {
        doImport("net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent");
        doImport("net.dv8tion.jda.api.interactions.commands.build.CommandData");
        doImport("net.dv8tion.jda.api.interactions.commands.build.Commands");
        slashCommands.add(slashCommand);
    }

    public void addMessageMenu(final MessageMenu messageMenu) {
        doImport("");
        doImport("net.dv8tion.jda.api.interactions.commands.build.CommandData");
        doImport("net.dv8tion.jda.api.interactions.commands.build.Commands");
        messageMenus.add(messageMenu);
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

        // print class definition: end
        program.append("}  // end class: ").append(programName).append(NL.repeat(2));

        return program.toString();
    }

    private String printCommandData() {
        StringBuilder commandData = new StringBuilder(tab(1));
        commandData.append("public List<CommandData> getCommandData() {").append(NL);
        commandData.append(tab(2)).append("return List.of(").append(NL).append(tab(3));

        String delimiter = ',' + NL + tab(3);
        List<String> slashCommandData = slashCommands.stream().map(SlashCommand::getCommandDataDeclaration)
                .toList();
        List<String> messageCommandData = messageMenus.stream().map(MessageMenu::getCommandDataDeclaration)
                .toList();

        List<String> allCommands = new ArrayList<>();
        allCommands.addAll(slashCommandData);
        allCommands.addAll(messageCommandData);
        String commandDataRawList = String.join(delimiter, allCommands);

        commandData.append(commandDataRawList).append(NL).append(tab(2)).append(");").append(NL);
        commandData.append(tab(1)).append("}  // end method: getCommandData").append(NL.repeat(2));

        return commandData.toString();
    }

    private String printSlashCommandListener() {
        StringBuilder listener = new StringBuilder();
        listener.append("    @Override").append(NL)
                .append("    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {")
                .append(NL);

        slashCommands.forEach(slashCommand -> {
            listener.append(slashCommand.printComponent(2));
        });

        listener.append("    }  // end method: onSlashCommandInteraction").append(NL.repeat(2));
        return listener.toString();
    }

    private String pringMessageMenuListener() {
        StringBuilder listener = new StringBuilder();
        listener.append("    @Override").append(NL)
                .append("    public void onMessageContextInteraction(MessageContextInteractionEvent event) {")
                .append(NL);

        messageMenus.forEach(messageMenu -> {
            listener.append(messageMenu.printComponent(2));
        });

        listener.append("    }  // end method: onMessageMenuInteraction").append(NL.repeat(2));
        return listener.toString();
    }

    private String tab(int ident) {
        return "    ".repeat(ident);
    }
}
