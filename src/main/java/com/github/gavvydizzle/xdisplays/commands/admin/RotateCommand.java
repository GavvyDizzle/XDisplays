package com.github.gavvydizzle.xdisplays.commands.admin;

import com.github.gavvydizzle.xdisplays.commands.AdminCommandManager;
import com.github.gavvydizzle.xdisplays.display.DisplayManager;
import com.github.mittenmc.serverutils.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.bukkit.util.Transformation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RotateCommand extends SubCommand {

    private final AdminCommandManager adminCommandManager;
    private final DisplayManager displayManager;

    private final ArrayList<String> args2 = new ArrayList<>(Arrays.asList("0", "90", "180", "270"));

    public RotateCommand(AdminCommandManager adminCommandManager, DisplayManager displayManager) {
        this.adminCommandManager = adminCommandManager;
        this.displayManager = displayManager;
    }

    @Override
    public String getName() {
        return "rotate";
    }

    @Override
    public String getDescription() {
        return "Rotate this display (left)";
    }

    @Override
    public String getSyntax() {
        return "/" + adminCommandManager.getCommandDisplayName() + " rotate <x> <y> <z>";
    }

    @Override
    public String getColoredSyntax() {
        return ChatColor.YELLOW + "Usage: " + getSyntax();
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) return;

        if (args.length < 4) {
            sender.sendMessage(getColoredSyntax());
            return;
        }

        Player player = (Player) sender;
        Display display = displayManager.getDisplay(player.getUniqueId());
        if (display == null) {
            sender.sendMessage(ChatColor.RED + "No display selected");
            return;
        }

        float x,y,z;
        try {
            x = Float.parseFloat(args[1]);
            y = Float.parseFloat(args[2]);
            z = Float.parseFloat(args[3]);
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "Invalid rotation amount(s)");
            return;
        }

        display.setTransformation(new Transformation(
                display.getTransformation().getTranslation(),
                display.getTransformation().getLeftRotation().rotateXYZ((float) Math.toRadians(x), (float) Math.toRadians(y), (float) Math.toRadians(z)),
                display.getTransformation().getScale(),
                display.getTransformation().getRightRotation()
        ));
        sender.sendMessage(ChatColor.YELLOW + "Updated display rotation");
    }

    @Override
    public List<String> getSubcommandArguments(CommandSender sender, String[] args) {
        ArrayList<String> list = new ArrayList<>();

        if (args.length == 2) {
            StringUtil.copyPartialMatches(args[1], args2, list);
        }
        else if (args.length == 3) {
            StringUtil.copyPartialMatches(args[2], args2, list);
        }
        else if (args.length == 4) {
            StringUtil.copyPartialMatches(args[3], args2, list);
        }

        return list;
    }

}