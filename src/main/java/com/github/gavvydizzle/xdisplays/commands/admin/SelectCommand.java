package com.github.gavvydizzle.xdisplays.commands.admin;

import com.github.gavvydizzle.xdisplays.XDisplays;
import com.github.gavvydizzle.xdisplays.commands.AdminCommandManager;
import com.github.gavvydizzle.xdisplays.display.DisplayManager;
import com.github.mittenmc.serverutils.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SelectCommand extends SubCommand {

    private final AdminCommandManager adminCommandManager;
    private final XDisplays instance;
    private final DisplayManager displayManager;

    public SelectCommand(AdminCommandManager adminCommandManager, XDisplays instance, DisplayManager displayManager) {
        this.adminCommandManager = adminCommandManager;
        this.instance = instance;
        this.displayManager = displayManager;
    }

    @Override
    public String getName() {
        return "select";
    }

    @Override
    public String getDescription() {
        return "Select a nearby display";
    }

    @Override
    public String getSyntax() {
        return "/" + adminCommandManager.getCommandDisplayName() + " select [range]";
    }

    @Override
    public String getColoredSyntax() {
        return ChatColor.YELLOW + "Usage: " + getSyntax();
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) return;
        Player player = (Player) sender;

        int range = 3;
        if (args.length > 1) {
            try {
                range = Integer.parseInt(args[1]);
            } catch (Exception e) {
                sender.sendMessage(ChatColor.RED + "Invalid range");
                return;
            }
        }

        Display display = displayManager.getClosestDisplay(player.getLocation(), range);


        if (display == null) {
            sender.sendMessage(ChatColor.RED + "No display found with in " + range + " blocks");
            return;
        }

        displayManager.selectDisplay(player, display);
        display.setGlowing(true);

        Bukkit.getScheduler().runTaskLater(instance, () -> {
            if (!display.isDead()) {
                display.setGlowing(false);
            }
        }, 40L);

        String name = display instanceof ItemDisplay ? "item display" : "block display";
        sender.sendMessage(ChatColor.GREEN + "Selected " + name + ". It will glow for 2 seconds");
    }

    @Override
    public List<String> getSubcommandArguments(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }
}