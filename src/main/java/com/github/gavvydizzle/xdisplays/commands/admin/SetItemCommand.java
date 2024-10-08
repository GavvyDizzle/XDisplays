package com.github.gavvydizzle.xdisplays.commands.admin;

import com.github.gavvydizzle.xdisplays.commands.AdminCommandManager;
import com.github.gavvydizzle.xdisplays.display.DisplayManager;
import com.github.mittenmc.serverutils.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SetItemCommand extends SubCommand {

    private final DisplayManager displayManager;
    private final ArrayList<String> args2 = new ArrayList<>(Arrays.asList("get", "set"));

    public SetItemCommand(AdminCommandManager adminCommandManager, DisplayManager displayManager) {
        this.displayManager = displayManager;

        setName("item");
        setDescription("Edit the item of an item display");
        setSyntax("/" + adminCommandManager.getCommandDisplayName() + " item <get|set>");
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

        if (!(display instanceof ItemDisplay itemDisplay)) {
            sender.sendMessage(ChatColor.RED + "Selected display must be an item display");
            return;
        }

        if (args[1].equalsIgnoreCase("get")) {
            if (player.getInventory().addItem(Objects.requireNonNull(itemDisplay.getItemStack()).clone()).isEmpty()) {
                sender.sendMessage(ChatColor.GREEN + "This display's item has been added to your inventory");
            }
            else {
                sender.sendMessage(ChatColor.RED + "Inventory full");
            }
        }
        else if (args[1].equalsIgnoreCase("set")) {
            ItemStack heldItem = player.getInventory().getItemInMainHand();
            if (heldItem.getType() == Material.AIR) {
                sender.sendMessage(ChatColor.RED + "You must be holding an item in your main hand to copy");
                return;
            }

            ((ItemDisplay) display).setItemStack(heldItem.clone());
            sender.sendMessage(ChatColor.GREEN + "Copied the item in your hand to the display");
        }
        else {
            sender.sendMessage(ChatColor.RED + "Invalid argument");
        }
    }

    @Override
    public List<String> getSubcommandArguments(CommandSender sender, String[] args) {
        ArrayList<String> list = new ArrayList<>();

        if (args.length == 2) {
            StringUtil.copyPartialMatches(args[1], args2, list);
        }

        return list;
    }

}