package com.github.gavvydizzle.xdisplays.commands.admin;

import com.github.gavvydizzle.xdisplays.commands.AdminCommandManager;
import com.github.gavvydizzle.xdisplays.display.DisplayManager;
import com.github.mittenmc.serverutils.Numbers;
import com.github.mittenmc.serverutils.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BrightnessCommand extends SubCommand {

    private final AdminCommandManager adminCommandManager;
    private final DisplayManager displayManager;

    private final ArrayList<String> args2 = new ArrayList<>(Arrays.asList("all", "block", "sky"));

    public BrightnessCommand(AdminCommandManager adminCommandManager, DisplayManager displayManager) {
        this.adminCommandManager = adminCommandManager;
        this.displayManager = displayManager;
    }

    @Override
    public String getName() {
        return "brightness";
    }

    @Override
    public String getDescription() {
        return "Change the brightness";
    }

    @Override
    public String getSyntax() {
        return "/" + adminCommandManager.getCommandDisplayName() + " brightness <all|block|sky> <amount>";
    }

    @Override
    public String getColoredSyntax() {
        return ChatColor.YELLOW + "Usage: " + getSyntax();
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) return;

        if (args.length < 3) {
            sender.sendMessage(getColoredSyntax());
            return;
        }

        Player player = (Player) sender;
        Display display = displayManager.getDisplay(player.getUniqueId());
        if (display == null) {
            sender.sendMessage(ChatColor.RED + "No display selected");
            return;
        }

        int light;
        try {
            light = Integer.parseInt(args[2]);
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "Invalid light level");
            return;
        }
        if (!Numbers.isWithinRange(light, 0, 15)) {
            sender.sendMessage(ChatColor.RED + "Light level must be between 0 and 15");
            return;
        }

        int oldBlockLight = display.getBrightness() != null ? display.getBrightness().getBlockLight() : 0;
        int oldSkyLight = display.getBrightness() != null ? display.getBrightness().getSkyLight() : 0;

        if (args[1].equalsIgnoreCase("block")) {
            display.setBrightness(new Display.Brightness(light, oldSkyLight));
            sender.sendMessage(ChatColor.GREEN + "Updated block light to " + light + " (was block=" + oldBlockLight + " sky=" + oldSkyLight + ")");
        }
        else if (args[1].equalsIgnoreCase("sky")) {
            display.setBrightness(new Display.Brightness(oldBlockLight, light));
            sender.sendMessage(ChatColor.GREEN + "Updated sky light to " + light + " (was block=" + oldBlockLight + " sky=" + oldSkyLight + ")");
        }
        else if (args[1].equalsIgnoreCase("all")) {
            display.setBrightness(new Display.Brightness(light, light));
            sender.sendMessage(ChatColor.GREEN + "Updated block and sky light to " + light + " (was block=" + oldBlockLight + " sky=" + oldSkyLight + ")");
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