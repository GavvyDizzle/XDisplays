package com.github.gavvydizzle.xdisplays.commands.admin;

import com.github.gavvydizzle.xdisplays.XDisplays;
import com.github.gavvydizzle.xdisplays.commands.AdminCommandManager;
import com.github.mittenmc.serverutils.Numbers;
import com.github.mittenmc.serverutils.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GlowCommand extends SubCommand {

    private final AdminCommandManager adminCommandManager;
    private final XDisplays instance;

    public GlowCommand(AdminCommandManager adminCommandManager, XDisplays instance) {
        this.adminCommandManager = adminCommandManager;
        this.instance = instance;
    }

    @Override
    public String getName() {
        return "glow";
    }

    @Override
    public String getDescription() {
        return "Make nearby displays glow";
    }

    @Override
    public String getSyntax() {
        return "/" + adminCommandManager.getCommandDisplayName() + " glow [range] [seconds]";
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
        range = Numbers.constrain(range, 1, 250);

        int seconds = 3;
        if (args.length > 2) {
            try {
                seconds = Integer.parseInt(args[2]);
            } catch (Exception e) {
                sender.sendMessage(ChatColor.RED + "Invalid seconds");
                return;
            }
        }
        seconds = Numbers.constrain(seconds, 1, 10);

        ArrayList<Display> arr = new ArrayList<>();
        for (Entity e : Objects.requireNonNull(player.getLocation().getWorld()).getNearbyEntities(player.getLocation(), range, range, range).stream()
                .filter(e -> e.getType() == EntityType.BLOCK_DISPLAY || e.getType() == EntityType.ITEM_DISPLAY).collect(Collectors.toList())) {
            arr.add((Display) e);
            e.setGlowing(true);
        }

        Bukkit.getScheduler().runTaskLater(instance, () -> {
            for (Entity entity : arr) {
                if (!entity.isDead()) {
                    entity.setGlowing(false);
                }
            }
        }, seconds * 20L);

        if (arr.size() > 0) {
            sender.sendMessage(ChatColor.GREEN + "Found " + arr.size() + " display(s) within " + range + " blocks");
            sender.sendMessage(ChatColor.GREEN + "They will glow for " + seconds + " seconds");
        }
        else {
            sender.sendMessage(ChatColor.YELLOW + "No displays found within " + range + " blocks");
        }
    }

    @Override
    public List<String> getSubcommandArguments(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }
}