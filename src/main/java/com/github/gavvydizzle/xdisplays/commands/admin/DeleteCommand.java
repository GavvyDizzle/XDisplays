package com.github.gavvydizzle.xdisplays.commands.admin;

import com.github.gavvydizzle.xdisplays.commands.AdminCommandManager;
import com.github.gavvydizzle.xdisplays.display.DisplayManager;
import com.github.mittenmc.serverutils.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class DeleteCommand extends SubCommand {

    private final AdminCommandManager adminCommandManager;
    private final DisplayManager displayManager;

    public DeleteCommand(AdminCommandManager adminCommandManager, DisplayManager displayManager) {
        this.adminCommandManager = adminCommandManager;
        this.displayManager = displayManager;
    }

    @Override
    public String getName() {
        return "delete";
    }

    @Override
    public String getDescription() {
        return "Delete your selected display";
    }

    @Override
    public String getSyntax() {
        return "/" + adminCommandManager.getCommandDisplayName() + " delete";
    }

    @Override
    public String getColoredSyntax() {
        return ChatColor.YELLOW + "Usage: " + getSyntax();
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) return;

        Player player = (Player) sender;
        Display display = displayManager.getDisplay(player.getUniqueId());
        if (display == null) {
            sender.sendMessage(ChatColor.RED + "No display selected");
            return;
        }

        displayManager.removeDisplay(player);
        String name = display instanceof ItemDisplay ? "item display" : "block display";
        sender.sendMessage(ChatColor.YELLOW + "Deleted " + name);
        display.remove();
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