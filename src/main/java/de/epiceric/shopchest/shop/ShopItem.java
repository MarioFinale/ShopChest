package de.epiceric.shopchest.shop;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.epiceric.shopchest.ShopChest;
import de.epiceric.shopchest.utils.Utils;
import org.bukkit.scheduler.BukkitRunnable;

public class ShopItem {
    private final ShopChest plugin;
    private final Set<UUID> viewers = ConcurrentHashMap.newKeySet();
    private final ItemStack itemStack;
    private final Location location;
    private UUID uuid;
    private int entityId;
    private Entity entity;
    private boolean exists;


    public ShopItem(ShopChest plugin, ItemStack itemStack, Location location) {
        this.plugin = plugin;
        this.itemStack = itemStack;
        this.location = location;
        entity = location.getWorld().dropItem(location,itemStack);
        uuid = entity.getUniqueId();
        entityId = entity.getEntityId();
        Item item = (Item) entity;
        item.setGravity(true);
        item.setWillAge(true);
        item.setTicksLived(4800);
        item.setPickupDelay(32767);
        item.setInvulnerable(true);
        item.setCanPlayerPickup(false);
        item.teleport(location);
        entity.teleport(location);
        exists = true;

    }

    /**
     * @return Clone of the location, where the shop item should be (it could have been moved by something, even though it shouldn't)
     */
    public Location getLocation() {
        return location.clone();
    }

    /**
     * @return A clone of this Item's {@link ItemStack}
     */
    public ItemStack getItemStack() {
        return itemStack.clone();
    }

    /**
     * @param p Player to check
     * @return Whether the item is visible to the player
     */
    public boolean isVisible(Player p) {
        return viewers.contains(p.getUniqueId());
    }

    /**
     * @param p Player to which the item should be shown
     */
    public void showPlayer(Player p) {

        showPlayer(p, false);
    }

    /**
     * @param p Player to which the item should be shown
     * @param force whether to force or not
     */
    public void showPlayer(Player p, boolean force) {
            new BukkitRunnable(){
                @Override
                public void run() {
                    if ((entity != null && entity.isValid() && !(entity.isDead()))){
                        entity.teleport(location);
                    }else {
                        if (entity != null){entity.remove();}
                        entity = location.getWorld().dropItem(location,itemStack);
                        uuid = entity.getUniqueId();
                        entityId = entity.getEntityId();
                        Item item = (Item) entity;
                        item.setGravity(true);
                        item.setWillAge(true);
                        item.setTicksLived(4800);
                        item.setPickupDelay(32767);
                        item.setInvulnerable(true);
                        item.setCanPlayerPickup(false);
                        entity.teleport(location);
                        item.teleport(location);
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
                            @Override
                            public void run(){
                                List<Entity> ent = entity.getNearbyEntities(1,1,1);
                                for (Entity tent: ent ) {
                                    if (tent.getType() == EntityType.DROPPED_ITEM){
                                        Material mat = ((Item) tent).getItemStack().getType();
                                        if (mat == itemStack.getType()){
                                            if(tent.getEntityId() != entityId){
                                                tent.remove();
                                            }
                                        }
                                    }
                                }
                            }
                        }, 20L);
                    }
                }
            }.runTask(plugin);
    }

    /**
     * @param p Player from which the item should be hidden
     */
    public void hidePlayer(Player p) {
        hidePlayer(p, false);
    }

    /**
     * @param p Player from which the item should be hidden
     * @param force whether to force or not
     */
    public void hidePlayer(Player p, boolean force) {
        //always visible;
    }

    public void resetVisible(Player p) {
        showPlayer(p);
    }

    /**
     * Removes the item. <br>
     * Item will be hidden from all players
     */
    public void remove() {
        new BukkitRunnable(){
            @Override
            public void run() {
                entity.remove();
            }
        }.runTask(plugin);
    this.exists = false;
    }

    /**
     * Respawns the item at the set location for a player
     * @param p Player, for which the item should be reset
     */
    public void resetForPlayer(Player p) {
        new BukkitRunnable(){
            @Override
            public void run() {
                if (!(entity == null)){
                    entity.remove();
                }
                entity = location.getWorld().dropItem(location,itemStack);
                uuid = entity.getUniqueId();
                entityId = entity.getEntityId();
                Item item = (Item) entity;
                item.setGravity(true);
                item.setWillAge(true);
                item.setTicksLived(4800);
                item.setPickupDelay(32767);
                item.setInvulnerable(true);
                item.setCanPlayerPickup(false);
                entity.teleport(location);
                item.teleport(location);
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
                    @Override
                    public void run(){
                        List<Entity> ent = entity.getNearbyEntities(1,1,1);
                        for (Entity tent: ent ) {
                            if (tent.getType() == EntityType.DROPPED_ITEM){
                                Material mat = ((Item) tent).getItemStack().getType();
                                if (mat == itemStack.getType()){
                                    if(tent.getEntityId() != entityId){
                                        tent.remove();
                                    }
                                }
                            }
                        }
                    }
                }, 20L);
            }
        }.runTask(plugin);
    }

    public boolean exists(){
        return this.exists;
    }
}
