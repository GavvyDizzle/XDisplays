package com.github.gavvydizzle.xdisplays.commands.admin;

import com.github.gavvydizzle.xdisplays.commands.AdminCommandManager;
import com.github.gavvydizzle.xdisplays.display.DisplayManager;
import com.github.mittenmc.serverutils.Numbers;
import com.github.mittenmc.serverutils.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TeleportHereCommand extends SubCommand {

    private final AdminCommandManager adminCommandManager;
    private final DisplayManager displayManager;

    public TeleportHereCommand(AdminCommandManager adminCommandManager, DisplayManager displayManager) {
        this.adminCommandManager = adminCommandManager;
        this.displayManager = displayManager;
    }

    @Override
    public String getName() {
        return "tph";
    }

    @Override
    public String getDescription() {
        return "Teleport display to you";
    }

    @Override
    public String getSyntax() {
        return "/" + adminCommandManager.getCommandDisplayName() + " tph <copyRotation>";
    }

    @Override
    public String getColoredSyntax() {
        return ChatColor.YELLOW + "Usage: " + getSyntax();
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) return;

        Player player = (Player) sender;
        Display display = displayManager.getDisplay(player.getUniqueId());
        if (display == null) {
            sender.sendMessage(ChatColor.RED + "No display selected");
            return;
        }

        Location newLocation = player.getLocation().clone();
        if (args.length > 1 && args[1].equalsIgnoreCase("copyRotation")) {
            // Reset the rotation, so it takes the player's pitch and yaw correctly
            display.setTransformation(new Transformation(
                    display.getTransformation().getTranslation(),
                    new Quaternionf(),
                    display.getTransformation().getScale(),
                    new Quaternionf()
            ));
            display.teleport(newLocation);
            sender.sendMessage(ChatColor.YELLOW + "The rotation reset so the pitch/yaw take effect");
        }
        else {
            newLocation.setPitch(display.getLocation().getPitch());
            newLocation.setYaw(display.getLocation().getYaw());
            display.teleport(newLocation);
        }

        sender.sendMessage(ChatColor.YELLOW + "Updated display location to your location (" + Numbers.round(player.getLocation().getX(), 4) +
                ", " + Numbers.round(player.getLocation().getY(), 4) + ", " + Numbers.round(player.getLocation().getZ(), 4) + ")");
    }

    @Override
    public List<String> getSubcommandArguments(CommandSender sender, String[] args) {
        ArrayList<String> list = new ArrayList<>();

        if (args.length == 2) {
            StringUtil.copyPartialMatches(args[1], Collections.singleton("copyRotation"), list);
        }

        return list;
    }

}