package com.github.gavvydizzle.xdisplays.display;

import com.github.mittenmc.serverutils.Numbers;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class DisplayManager implements Listener {

    private final HashMap<UUID, Display> selectedDisplays;

    public DisplayManager() {
        selectedDisplays = new HashMap<>();
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent e) {
        selectedDisplays.remove(e.getPlayer().getUniqueId());
    }

    /**
     * Gets the closest display to this location
     * @param location The center of the checking range
     * @param range The range, must be between 1 and 500
     * @return The nearest Display or null if none exists
     */
    @Nullable
    public Display getClosestDisplay(Location location, int range) {
        range = Numbers.constrain(range, 1, 500);

        Entity entity = null;
        double distSquared = Integer.MAX_VALUE;

        for (Entity e : Objects.requireNonNull(location.getWorld()).getNearbyEntities(location, range, range, range).stream()
                .filter(e -> e.getType().equals(EntityType.ITEM_DISPLAY) || e.getType().equals(EntityType.BLOCK_DISPLAY)).collect(Collectors.toList())) {

            double d = e.getLocation().toVector().distanceSquared(location.toVector());
            if (d < distSquared) {
                distSquared = d;
                entity = e;
            }
        }

        return (Display) entity;
    }

    /**
     * Gets the closest BlockDisplay to this location
     * @param location The center of the checking range
     * @param range The range, must be between 1 and 500
     * @return The nearest BlockDisplay or null if none exists
     */
    @Nullable
    public BlockDisplay getClosestBlockDisplay(Location location, int range) {
        range = Numbers.constrain(range, 1, 500);

        Entity entity = null;
        double distSquared = Integer.MAX_VALUE;

        for (Entity e : Objects.requireNonNull(location.getWorld()).getNearbyEntities(location, range, range, range).stream()
                .filter(e -> e.getType().equals(EntityType.BLOCK_DISPLAY)).collect(Collectors.toList())) {

            double d = e.getLocation().toVector().distanceSquared(location.toVector());
            if (d < distSquared) {
                distSquared = d;
                entity = e;
            }
        }

        return (BlockDisplay) entity;
    }

    /**
     * Gets the closest ItemDisplay to this location
     * @param location The center of the checking range
     * @param range The range, must be between 1 and 500
     * @return The nearest ItemDisplay or null if none exists
     */
    @Nullable
    public ItemDisplay getClosestItemDisplay(Location location, int range) {
        range = Numbers.constrain(range, 1, 500);

        Entity entity = null;
        double distSquared = Integer.MAX_VALUE;

        for (Entity e : Objects.requireNonNull(location.getWorld()).getNearbyEntities(location, range, range, range).stream()
                .filter(e -> e.getType().equals(EntityType.ITEM_DISPLAY)).collect(Collectors.toList())) {

            double d = e.getLocation().toVector().distanceSquared(location.toVector());
            if (d < distSquared) {
                distSquared = d;
                entity = e;
            }
        }

        return (ItemDisplay) entity;
    }


    public void selectDisplay(Player player, Display display) {
        selectedDisplays.put(player.getUniqueId(), display);
    }

    public void removeDisplay(Player player) {
        selectedDisplays.remove(player.getUniqueId());
    }

    @Nullable
    public Display getDisplay(UUID uuid) {
        return selectedDisplays.get(uuid);
    }

}
