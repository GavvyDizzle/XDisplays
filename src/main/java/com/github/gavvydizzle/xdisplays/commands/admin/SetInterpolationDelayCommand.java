package com.github.gavvydizzle.xdisplays.commands.admin;

import com.github.gavvydizzle.xdisplays.commands.AdminCommandManager;
import com.github.gavvydizzle.xdisplays.display.DisplayManager;
import com.github.mittenmc.serverutils.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class SetInterpolationDelayCommand extends SubCommand {

    private final DisplayManager displayManager;

    public SetInterpolationDelayCommand(AdminCommandManager adminCommandManager, DisplayManager displayManager) {
        this.displayManager = displayManager;

        setName("setInterpolationDelay");
        setDescription("Change the interpolation delay");
        setSyntax("/" + adminCommandManager.getCommandDisplayName() + " setInterpolationDelay <ticks>");
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

        int delay;
        try {
            delay = Integer.parseInt(args[1]);
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "Invalid delay");
            return;
        }
        if (delay < 0) {
            sender.sendMessage(ChatColor.RED + "Invalid delay");
            return;
        }

        int oldDelay = display.getInterpolationDelay();

        display.setInterpolationDelay(delay);
        sender.sendMessage(ChatColor.GREEN + "Updated interpolation delay to " + delay + " (was " + oldDelay + ")");
    }

    @Override
    public List<String> getSubcommandArguments(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }

}