package com.github.gavvydizzle.xdisplays.commands;

import com.github.gavvydizzle.xdisplays.XDisplays;
import com.github.gavvydizzle.xdisplays.commands.admin.*;
import com.github.gavvydizzle.xdisplays.display.DisplayManager;
import com.github.mittenmc.serverutils.CommandManager;
import com.github.mittenmc.serverutils.command.HelpCommand;
import org.bukkit.Material;
import org.bukkit.command.PluginCommand;

import java.util.ArrayList;
import java.util.Arrays;

public class AdminCommandManager extends CommandManager {

    private final ArrayList<String> materialList;

    public AdminCommandManager(PluginCommand command, XDisplays instance, DisplayManager displayManager) {
        super(command);

        materialList = new ArrayList<>();
        materialList.addAll(Arrays.stream(Material.values()).map(Material::name).toList());

        registerCommand(new HelpCommand.HelpCommandBuilder(this).build());
        registerCommand(new BrightnessCommand(this, displayManager));
        registerCommand(new CloneCommand(this, displayManager));
        registerCommand(new CreateCommand(this, displayManager));
        registerCommand(new DeleteCommand(this, displayManager));
        registerCommand(new GlowCommand(this, instance));
        registerCommand(new MoveCommand(this, displayManager));
        registerCommand(new ResetCommand(this, displayManager));
        registerCommand(new RotateCommand(this, displayManager));
        registerCommand(new SelectCommand(this, instance, displayManager));
        registerCommand(new SetInterpolationDelayCommand(this, displayManager));
        registerCommand(new SetInterpolationDurationCommand(this, displayManager));
        registerCommand(new SetItemCommand(this, displayManager));
        registerCommand(new SetMaterialCommand(this, displayManager));
        registerCommand(new SetTeleportDurationCommand(this, displayManager));
        registerCommand(new SizeCommand(this, displayManager));
        registerCommand(new TeleportHereCommand(this, displayManager));
        registerCommand(new TransformCommand(this, displayManager));
    }

    public ArrayList<String> getMaterialList() {
        return materialList;
    }
}