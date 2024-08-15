package com.github.gavvydizzle.xdisplays.commands.admin;

import com.github.gavvydizzle.xdisplays.commands.AdminCommandManager;
import com.github.gavvydizzle.xdisplays.display.DisplayManager;
import com.github.mittenmc.serverutils.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CloneCommand extends SubCommand {

    private final DisplayManager displayManager;

    public CloneCommand(AdminCommandManager adminCommandManager, DisplayManager displayManager) {
        this.displayManager = displayManager;

        setName("clone");
        setDescription("Clone your selected display");
        setSyntax("/" + adminCommandManager.getCommandDisplayName() + " clone");
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

        Location newLocation = player.getLocation().clone();
        newLocation.setPitch(display.getLocation().getPitch());
        newLocation.setYaw(display.getLocation().getYaw());

        if (display instanceof BlockDisplay) {
            BlockDisplay blockDisplay = (BlockDisplay) player.getWorld().spawnEntity(player.getLocation(), EntityType.BLOCK_DISPLAY);
            blockDisplay.setBlock(((BlockDisplay) display).getBlock());
            blockDisplay.setTransformation(display.getTransformation());
            blockDisplay.teleport(newLocation);
            displayManager.selectDisplay(player, blockDisplay);
            sender.sendMessage(ChatColor.GREEN + "Cloned selected block display and selected the new one");
        }
        else if (display instanceof ItemDisplay) {
            ItemDisplay itemDisplay = (ItemDisplay) player.getWorld().spawnEntity(player.getLocation(), EntityType.ITEM_DISPLAY);
            itemDisplay.setItemStack(Objects.requireNonNull(((ItemDisplay) display).getItemStack()).clone());
            itemDisplay.setTransformation(display.getTransformation());
            itemDisplay.teleport(newLocation);
            displayManager.selectDisplay(player, itemDisplay);
            sender.sendMessage(ChatColor.GREEN + "Cloned selected item display and selected the new one");
        }
        else {
            sender.sendMessage(ChatColor.RED + "Invalid display type");
        }
    }

    @Override
    public List<String> getSubcommandArguments(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }

}