package com.github.gavvydizzle.xdisplays.commands.admin;

import com.github.gavvydizzle.xdisplays.commands.AdminCommandManager;
import com.github.gavvydizzle.xdisplays.display.DisplayManager;
import com.github.mittenmc.serverutils.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.util.Transformation;
import org.joml.Vector3f;

import java.util.Collections;
import java.util.List;

public class TransformCommand extends SubCommand {

    private final AdminCommandManager adminCommandManager;
    private final DisplayManager displayManager;

    public TransformCommand(AdminCommandManager adminCommandManager, DisplayManager displayManager) {
        this.adminCommandManager = adminCommandManager;
        this.displayManager = displayManager;
    }

    @Override
    public String getName() {
        return "transform";
    }

    @Override
    public String getDescription() {
        return "Transform the display";
    }

    @Override
    public String getSyntax() {
        return "/" + adminCommandManager.getCommandDisplayName() + " transform <x,y,z> <x,y,z> <x,y,z> <x,y,z>";
    }

    @Override
    public String getColoredSyntax() {
        return ChatColor.YELLOW + "Usage: " + getSyntax();
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) return;

        if (args.length < 5) {
            sender.sendMessage(getColoredSyntax());
            return;
        }

        Player player = (Player) sender;
        Display display = displayManager.getDisplay(player.getUniqueId());
        if (display == null) {
            sender.sendMessage(ChatColor.RED + "No display selected");
            return;
        }

        try {
            String[] args1 = args[1].split(",");
            String[] args2 = args[2].split(",");
            String[] args3 = args[3].split(",");
            String[] args4 = args[4].split(",");

            display.setTransformation(new Transformation(
                    display.getTransformation().getTranslation().add(Float.parseFloat(args1[0]), Float.parseFloat(args1[1]), Float.parseFloat(args1[2])),
                    display.getTransformation().getLeftRotation().rotateXYZ(Float.parseFloat(args2[0]), Float.parseFloat(args2[1]), Float.parseFloat(args2[2])),
                    new Vector3f(1,1,1).mul(Float.parseFloat(args3[0]), Float.parseFloat(args3[1]), Float.parseFloat(args3[2])),
                    display.getTransformation().getRightRotation().rotateXYZ(Float.parseFloat(args4[0]), Float.parseFloat(args4[1]), Float.parseFloat(args4[2]))
            ));
            sender.sendMessage(ChatColor.GREEN + "Successfully transformed display");

        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "Probably invalid syntax");
        }
    }

    @Override
    public List<String> getSubcommandArguments(CommandSender sender, String[] args) {
        if (args.length == 2) {
            return Collections.singletonList("0,0,0");
        }
        else if (args.length == 3) {
            return Collections.singletonList("0,0,0");
        }
        else if (args.length == 4) {
            return Collections.singletonList("1,1,1");
        }
        else if (args.length == 5) {
            return Collections.singletonList("0,0,0");
        }
        return Collections.emptyList();
    }

}