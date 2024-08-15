package com.github.gavvydizzle.xdisplays;

import com.github.gavvydizzle.xdisplays.commands.AdminCommandManager;
import com.github.gavvydizzle.xdisplays.display.DisplayManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class XDisplays extends JavaPlugin {

    @Override
    public void onEnable() {
        DisplayManager displayManager = new DisplayManager();
        getServer().getPluginManager().registerEvents(displayManager, this);

        new AdminCommandManager(getCommand("xdisplay"), this, displayManager);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
