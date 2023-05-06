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
import java.util.List;

public class SetMaterialCommand extends SubCommand {

    private final AdminCommandManager adminCommandManager;
    private final DisplayManager displayManager;

    public SetMaterialCommand(AdminCommandManager adminCommandManager, DisplayManager displayManager) {
        this.adminCommandManager = adminCommandManager;
        this.displayManager = displayManager;
    }

    @Override
    public String getName() {
        return "material";
    }

    @Override
    public String getDescription() {
        return "Change the display material";
    }

    @Override
    public String getSyntax() {
        return "/" + adminCommandManager.getCommandDisplayName() + " material <material>";
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

        Player player = (Player) sender;
        Display display = displayManager.getDisplay(player.getUniqueId());
        if (display == null) {
            sender.sendMessage(ChatColor.RED + "No display selected");
            return;
        }

        Material material = ConfigUtils.getMaterial(args[1], Material.DIRT);
        if (material == Material.DIRT && !args[1].equalsIgnoreCase("dirt")) {
            sender.sendMessage(ChatColor.RED + "Invalid material: " + args[1]);
            return;
        }


        if (display instanceof BlockDisplay) {
            BlockDisplay blockDisplay = (BlockDisplay) display;
            blockDisplay.setBlock(material.createBlockData());
            sender.sendMessage(ChatColor.GREEN + "Updated block display material to " + material.name());
        }
        else if (display instanceof ItemDisplay) {
            ItemDisplay itemDisplay = (ItemDisplay) display;
            itemDisplay.setItemStack(new ItemStack(material));
            sender.sendMessage(ChatColor.GREEN + "Updated item display material to " + material.name());
        }
        else {
            sender.sendMessage(ChatColor.RED + "Invalid display type");
        }
    }

    @Override
    public List<String> getSubcommandArguments(CommandSender sender, String[] args) {
        ArrayList<String> list = new ArrayList<>();

        if (args.length == 2) {
            StringUtil.copyPartialMatches(args[1], adminCommandManager.getMaterialList(), list);
        }

        return list;
    }

}