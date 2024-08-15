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

import java.util.Collections;
import java.util.List;

public class SelectCommand extends SubCommand {

    private final XDisplays instance;
    private final DisplayManager displayManager;

    public SelectCommand(AdminCommandManager adminCommandManager, XDisplays instance, DisplayManager displayManager) {
        this.instance = instance;
        this.displayManager = displayManager;

        setName("select");
        setDescription("Select a nearby display");
        setSyntax("/" + adminCommandManager.getCommandDisplayName() + " select [range]");
        setColoredSyntax(ChatColor.YELLOW + getSyntax());
        setPermission(adminCommandManager.getPermissionPrefix() + getName().toLowerCase());
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) return;

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
        return Collections.emptyList();
    }
}