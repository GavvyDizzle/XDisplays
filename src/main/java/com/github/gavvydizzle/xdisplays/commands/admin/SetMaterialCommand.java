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

        setName("material");
        setDescription("Change the display material");
        setSyntax("/" + adminCommandManager.getCommandDisplayName() + " material <material>");
        setColoredSyntax(ChatColor.YELLOW + getSyntax());
        setPermission(adminCommandManager.getPermissionPrefix() + getName().toLowerCase());
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) return;

        if (args.length < 2) {
            sender.sendMessage(getColoredSyntax());
            return;
        }

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


        if (display instanceof BlockDisplay blockDisplay) {
            blockDisplay.setBlock(material.createBlockData());
            sender.sendMessage(ChatColor.GREEN + "Updated block display material to " + material.name());
        }
        else if (display instanceof ItemDisplay itemDisplay) {
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