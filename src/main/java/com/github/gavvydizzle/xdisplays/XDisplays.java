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

        try {
            new AdminCommandManager(Objects.requireNonNull(getCommand("xdisplay")), this, displayManager);
        } catch (NullPointerException e) {
            getLogger().severe("The admin command name was changed in the plugin.yml file. Please make it \"xdisplay\" and restart the server. You can change the aliases but NOT the command name.");
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
