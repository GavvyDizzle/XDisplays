package com.github.gavvydizzle.xdisplays.commands.admin;

import com.github.gavvydizzle.xdisplays.commands.AdminCommandManager;
import com.github.gavvydizzle.xdisplays.display.DisplayManager;
import com.github.mittenmc.serverutils.Numbers;
import com.github.mittenmc.serverutils.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class SetTeleportDurationCommand extends SubCommand {

    private final DisplayManager displayManager;

    public SetTeleportDurationCommand(AdminCommandManager adminCommandManager, DisplayManager displayManager) {
        this.displayManager = displayManager;

        setName("setTeleportDuration");
        setDescription("Change the teleport duration");
        setSyntax("/" + adminCommandManager.getCommandDisplayName() + " setTeleportDuration <ticks>");
        setColoredSyntax(ChatColor.YELLOW + getSyntax());
        setPermission(adminCommandManager.getPermissionPrefix() + getName().toLowerCase());
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) return;

        if (args.length < 1) {
            sender.sendMessage(getColoredSyntax());
            return;
        }

        Display display = displayManager.getDisplay(player.getUniqueId());
        if (display == null) {
            sender.sendMessage(ChatColor.RED + "No display selected");
            return;
        }

        int duration;
        try {
            duration = Integer.parseInt(args[1]);
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "Invalid duration");
            return;
        }
        duration = Numbers.constrain(duration, 0, 59);

        int oldDuration = display.getTeleportDuration();

        display.setTeleportDuration(duration);
        sender.sendMessage(ChatColor.GREEN + "Updated teleport duration to " + duration + " (was " + oldDuration + ")");
    }

    @Override
    public List<String> getSubcommandArguments(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }

}