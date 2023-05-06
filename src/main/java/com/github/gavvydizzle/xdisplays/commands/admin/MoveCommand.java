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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MoveCommand extends SubCommand {

    private final AdminCommandManager adminCommandManager;
    private final DisplayManager displayManager;

    public MoveCommand(AdminCommandManager adminCommandManager, DisplayManager displayManager) {
        this.adminCommandManager = adminCommandManager;
        this.displayManager = displayManager;
    }

    @Override
    public String getName() {
        return "move";
    }

    @Override
    public String getDescription() {
        return "Move this display";
    }

    @Override
    public String getSyntax() {
        return "/" + adminCommandManager.getCommandDisplayName() + " move <x> <y> <z>";
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

        boolean relX = false, relY = false, relZ = false;
        if (args[1].startsWith("~")) {
            relX = true;
            args[1] = args[1].substring(1);
        }
        if (args[2].startsWith("~")) {
            relY = true;
            args[2] = args[2].substring(1);
        }
        if (args[3].startsWith("~")) {
            relZ = true;
            args[3] = args[3].substring(1);
        }

        float x,y,z;
        try {
            x = args[1].length() > 0 ? Float.parseFloat(args[1]) : 0;
            y = args[2].length() > 0 ? Float.parseFloat(args[2]) : 0;
            z = args[3].length() > 0 ? Float.parseFloat(args[3]) : 0;
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "Invalid location arguments");
            return;
        }

        Location newLoc = new Location(display.getWorld(),
                relX ? display.getLocation().getX() + x : x,
                relY ? display.getLocation().getY() + y : y,
                relZ ? display.getLocation().getZ() + z : z
        );
        display.teleport(newLoc);

        sender.sendMessage(ChatColor.YELLOW + "Updated display location to (" + Numbers.round(newLoc.getX(), 4) +
                ", " + Numbers.round(newLoc.getY(), 4) + ", " + Numbers.round(newLoc.getZ(), 4) + ")");
    }

    @Override
    public List<String> getSubcommandArguments(CommandSender sender, String[] args) {
        ArrayList<String> list = new ArrayList<>();

        if (!(sender instanceof Player)) return list;
        Player player = (Player) sender;

        if (args.length == 2) {
            StringUtil.copyPartialMatches(args[1], Collections.singleton(String.valueOf(player.getLocation().getX())), list);
        }
        else if (args.length == 3) {
            StringUtil.copyPartialMatches(args[2], Collections.singleton(String.valueOf(player.getLocation().getY())), list);
        }
        else if (args.length == 4) {
            StringUtil.copyPartialMatches(args[3], Collections.singleton(String.valueOf(player.getLocation().getZ())), list);
        }

        return list;
    }

}