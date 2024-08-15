package com.github.gavvydizzle.xdisplays.commands.admin;

import com.github.gavvydizzle.xdisplays.commands.AdminCommandManager;
import com.github.gavvydizzle.xdisplays.display.DisplayManager;
import com.github.mittenmc.serverutils.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.util.StringUtil;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResetCommand extends SubCommand {

    private final DisplayManager displayManager;
    private final ArrayList<String> args2 = new ArrayList<>(Arrays.asList("rotation", "size"));

    public ResetCommand(AdminCommandManager adminCommandManager, DisplayManager displayManager) {
        this.displayManager = displayManager;

        setName("reset");
        setDescription("Reset properties of the display");
        setSyntax("/" + adminCommandManager.getCommandDisplayName() + " reset [arg]");
        setColoredSyntax(ChatColor.YELLOW + getSyntax());
        setPermission(adminCommandManager.getPermissionPrefix() + getName().toLowerCase());
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) return;

        Display display = displayManager.getDisplay(player.getUniqueId());
        if (display == null) {
            sender.sendMessage(ChatColor.RED + "No display selected");
            return;
        }

        if (args.length == 1) {
            display.setTransformation(new Transformation(new Vector3f(), new Quaternionf(), new Vector3f(1,1,1), new Quaternionf()));
            sender.sendMessage(ChatColor.YELLOW + "Reset the display back to its default state");
            return;
        }

        if (args[1].equalsIgnoreCase("rotation")) {
            display.setTransformation(new Transformation(
                    display.getTransformation().getTranslation(),
                    new Quaternionf(),
                    display.getTransformation().getScale(),
                    new Quaternionf())
            );
            sender.sendMessage(ChatColor.YELLOW + "Reset the display's rotation back to its default state");
        }
        else if (args[1].equalsIgnoreCase("size")) {
            display.setTransformation(new Transformation(
                    display.getTransformation().getTranslation(),
                    display.getTransformation().getLeftRotation(),
                    new Vector3f(),
                    display.getTransformation().getRightRotation())
            );
            sender.sendMessage(ChatColor.YELLOW + "Reset the display's size back to its default state");
        }
        else {
            sender.sendMessage(ChatColor.RED + "Invalid argument");
        }
    }

    @Override
    public List<String> getSubcommandArguments(CommandSender sender, String[] args) {
        ArrayList<String> list = new ArrayList<>();

        if (args.length == 2) {
            StringUtil.copyPartialMatches(args[1], args2, list);
        }

        return list;
    }

}