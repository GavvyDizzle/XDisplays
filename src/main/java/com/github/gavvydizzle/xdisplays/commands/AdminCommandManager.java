package com.github.gavvydizzle.xdisplays.commands;

import com.github.gavvydizzle.xdisplays.XDisplays;
import com.github.gavvydizzle.xdisplays.commands.admin.*;
import com.github.gavvydizzle.xdisplays.display.DisplayManager;
import com.github.mittenmc.serverutils.Colors;
import com.github.mittenmc.serverutils.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdminCommandManager implements TabExecutor {

    private final PluginCommand command;
    private final ArrayList<SubCommand> subcommands = new ArrayList<>();
    private final ArrayList<String> subcommandStrings = new ArrayList<>();
    private final String commandDisplayName, helpCommandPadding;

    private final ArrayList<String> materialList = new ArrayList<>();

    public AdminCommandManager(PluginCommand command, XDisplays instance, DisplayManager displayManager) {
        this.command = command;
        command.setExecutor(this);

        for (Material material : Material.values()) {
            materialList.add(material.name());
        }

        subcommands.add(new AdminHelpCommand(this));
        subcommands.add(new BrightnessCommand(this, displayManager));
        subcommands.add(new CloneCommand(this, displayManager));
        subcommands.add(new CreateCommand(this, displayManager));
        subcommands.add(new DeleteCommand(this, displayManager));
        subcommands.add(new GlowCommand(this, instance));
        subcommands.add(new MoveCommand(this, displayManager));
        subcommands.add(new ResetCommand(this, displayManager));
        subcommands.add(new RotateCommand(this, displayManager));
        subcommands.add(new SelectCommand(this, instance, displayManager));
        subcommands.add(new SetInterpolationDelayCommand(this, displayManager));
        subcommands.add(new SetInterpolationDurationCommand(this, displayManager));
        subcommands.add(new SetItemCommand(this, displayManager));
        subcommands.add(new SetMaterialCommand(this, displayManager));
        subcommands.add(new SizeCommand(this, displayManager));
        subcommands.add(new TeleportHereCommand(this, displayManager));
        subcommands.add(new TransformCommand(this, displayManager));
        Collections.sort(subcommands);

        for (SubCommand subCommand : subcommands) {
            subcommandStrings.add(subCommand.getName());
        }

        commandDisplayName = command.getName();
        helpCommandPadding = Colors.conv("&6-----(" + instance.getName() + " Commands)-----");
    }

    public String getCommandDisplayName() {
        return commandDisplayName;
    }

    public String getHelpCommandPadding() {
        return helpCommandPadding;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length > 0) {
            for (int i = 0; i < getSubcommands().size(); i++) {
                if (args[0].equalsIgnoreCase(getSubcommands().get(i).getName())) {

                    SubCommand subCommand = subcommands.get(i);

                    subCommand.perform(sender, args);
                    return true;
                }
            }
            sender.sendMessage(ChatColor.RED + "Invalid command");
        }
        sender.sendMessage(ChatColor.YELLOW + "Use '/" + commandDisplayName + " help' to see a list of valid commands");

        return true;
    }

    public ArrayList<SubCommand> getSubcommands(){
        return subcommands;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (args.length == 1) {
            ArrayList<String> subcommandsArguments = new ArrayList<>();

            StringUtil.copyPartialMatches(args[0], subcommandStrings, subcommandsArguments);

            return subcommandsArguments;
        }
        else if (args.length >= 2) {
            for (SubCommand subcommand : subcommands) {
                if (args[0].equalsIgnoreCase(subcommand.getName())) {
                    return subcommand.getSubcommandArguments(sender, args);
                }
            }
        }

        return null;
    }

    public ArrayList<String> getMaterialList() {
        return materialList;
    }

    public PluginCommand getCommand() {
        return command;
    }
}