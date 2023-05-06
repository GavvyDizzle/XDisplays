package com.github.gavvydizzle.xdisplays.commands.admin;

import com.github.gavvydizzle.xdisplays.commands.AdminCommandManager;
import com.github.gavvydizzle.xdisplays.display.DisplayManager;
import com.github.mittenmc.serverutils.ConfigUtils;
import com.github.mittenmc.serverutils.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateCommand extends SubCommand {

    private final AdminCommandManager adminCommandManager;
    private final DisplayManager displayManager;

    private final ArrayList<String> args2 = new ArrayList<>(Arrays.asList("block", "item"));

    public CreateCommand(AdminCommandManager adminCommandManager, DisplayManager displayManager) {
        this.adminCommandManager = adminCommandManager;
        this.displayManager = displayManager;
    }

    @Override
    public String getName() {
        return "create";
    }

    @Override
    public String getDescription() {
        return "Create a new display";
    }

    @Override
    public String getSyntax() {
        return "/" + adminCommandManager.getCommandDisplayName() + " create <type> [material]";
    }

    @Override
    public String getColoredSyntax() {
        return ChatColor.YELLOW + "Usage: " + getSyntax();
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) return;

        if (args.length < 2) {
            sender.sendMessage(getColoredSyntax());
            return;
        }

        Material material = Material.AIR;
        if (args.length >= 3) {
            material = ConfigUtils.getMaterial(args[2].toUpperCase(), Material.DIRT);
            if (material == Material.DIRT && !args[2].equalsIgnoreCase("dirt")) {
                sender.sendMessage(ChatColor.RED + "Invalid material: " + args[2]);
                return;
            }
        }


        Player player = (Player) sender;

        if (args[1].equalsIgnoreCase("block")) {
            BlockDisplay blockDisplay = (BlockDisplay) player.getWorld().spawnEntity(player.getLocation(), EntityType.BLOCK_DISPLAY);
            try {
                blockDisplay.setBlock(material.createBlockData());
            } catch (Exception e) {
                sender.sendMessage(ChatColor.RED + "Cannot set block display to " + material.name());
                blockDisplay.remove();
                return;
            }
            displayManager.selectDisplay(player, blockDisplay);
            sender.sendMessage(ChatColor.GREEN + "Created and selected a new block display");
        }
        else if (args[1].equalsIgnoreCase("item")) {
            ItemDisplay itemDisplay = (ItemDisplay) player.getWorld().spawnEntity(player.getLocation(), EntityType.ITEM_DISPLAY);
            itemDisplay.setItemStack(new ItemStack(material));
            displayManager.selectDisplay(player, itemDisplay);
            sender.sendMessage(ChatColor.GREEN + "Created and selected a new item display");
        }
        else {
            sender.sendMessage(ChatColor.RED + "Invalid display type");
        }
    }

    @Override
    public List<String> getSubcommandArguments(CommandSender sender, String[] args) {
        ArrayList<String> list = new ArrayList<>();

        if (args.length == 2) {
            StringUtil.copyPartialMatches(args[1], args2, list);
        }
        else if (args.length == 3) {
            StringUtil.copyPartialMatches(args[2], adminCommandManager.getMaterialList(), list);
        }

        return list;
    }

}