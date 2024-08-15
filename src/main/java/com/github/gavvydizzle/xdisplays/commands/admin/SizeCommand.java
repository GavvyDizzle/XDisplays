package com.github.gavvydizzle.xdisplays.commands.admin;

import com.github.gavvydizzle.xdisplays.commands.AdminCommandManager;
import com.github.gavvydizzle.xdisplays.display.DisplayManager;
import com.github.mittenmc.serverutils.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.util.Transformation;
import org.joml.Vector3f;

import java.util.Collections;
import java.util.List;

public class SizeCommand extends SubCommand {

    private final DisplayManager displayManager;

    public SizeCommand(AdminCommandManager adminCommandManager, DisplayManager displayManager) {
        this.displayManager = displayManager;

        setName("size");
        setDescription("Change or view display size");
        setSyntax("/" + adminCommandManager.getCommandDisplayName() + " size [size] [y-size] [z-size]");
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

        Vector3f oldSize = display.getTransformation().getScale();

        if (args.length == 1) {
            sender.sendMessage(ChatColor.YELLOW + "Current size: " + oldSize);
        }
        else if (args.length == 2) {
            float size;
            try {
                size = Float.parseFloat(args[1]);
                display.setTransformation(new Transformation(
                        display.getTransformation().getTranslation(),
                        display.getTransformation().getLeftRotation(),
                        new Vector3f(1,1,1).mul(size), // Update the size
                        display.getTransformation().getRightRotation()
                ));
                sender.sendMessage(ChatColor.YELLOW + "Updated the size to " + size + " (was " + oldSize + ")");
            } catch (Exception e) {
                sender.sendMessage("Invalid size");
            }
        }
        else if (args.length == 4) {
            try {
                Vector3f newSize = new Vector3f(Float.parseFloat(args[1]), Float.parseFloat(args[2]), Float.parseFloat(args[3]));
                display.setTransformation(new Transformation(
                        display.getTransformation().getTranslation(),
                        display.getTransformation().getLeftRotation(),
                        newSize, // Update the size
                        display.getTransformation().getRightRotation()
                ));
                sender.sendMessage(ChatColor.YELLOW + "Updated the size to " + newSize + " (was " + oldSize + ")");
            } catch (Exception e) {
                sender.sendMessage("Invalid size");
            }
        }
        else {
            sender.sendMessage(ChatColor.RED + "Invalid size arguments. You must provide 0, 1, or 3");
        }
    }

    @Override
    public List<String> getSubcommandArguments(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }

}