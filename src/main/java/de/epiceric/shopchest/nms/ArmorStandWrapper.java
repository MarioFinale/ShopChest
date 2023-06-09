package de.epiceric.shopchest.nms;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import de.epiceric.shopchest.ShopChest;
import org.bukkit.scheduler.BukkitRunnable;

public class ArmorStandWrapper {

    private UUID uuid;
    private int entityId;

    private ShopChest plugin;
    private Entity entity;
    private Location location;
    private String customName;

    public ArmorStandWrapper(ShopChest plugin, Location location, String customName, boolean interactable) {
        this.plugin = plugin;
        this.location = location;
        this.customName = customName;
        entity = location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        this.entityId = entity.getEntityId();
        this.uuid = entity.getUniqueId();
        ArmorStand armorStand = (ArmorStand) entity;
        armorStand.setVisible(false);
        armorStand.setCustomName("");
        armorStand.setCustomNameVisible(false);
        armorStand.setInvulnerable(true);
        armorStand.setPersistent(false);
        armorStand.setRemoveWhenFarAway(true);
    }

    public void setVisible(Player player, boolean visible) {
        if (visible){
            new BukkitRunnable(){
                @Override
                public void run() {
                    if (!(entity == null)){
                        entity.remove();
                    }
                    entity = location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
                    entityId = entity.getEntityId();
                    uuid = entity.getUniqueId();
                    ArmorStand armorStand = (ArmorStand) entity;
                    armorStand.setVisible(false);
                    armorStand.setCustomName(customName);
                    armorStand.setCustomNameVisible(true);
                    armorStand.setInvulnerable(true);
                    armorStand.setPersistent(false);
                    armorStand.setRemoveWhenFarAway(true);
                }
            }.runTask(plugin);
        }else{
            new BukkitRunnable(){
                @Override
                public void run() {
                    entity.remove();
                }
            }.runTask(plugin);
        }

    }

    public void setLocation(Location location) {
        this.location = location;
        entity.teleport(location);
    }

    public void setCustomName(String customName) {
        entity.setCustomName(customName);
    }

    public void remove() {
        new BukkitRunnable(){
            @Override
            public void run() {
                entity.remove();
            }
        }.runTask(plugin);
    }

    public int getEntityId() {
        return entityId;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Location getLocation() {
        return location.clone();
    }

    public String getCustomName() {
        return customName;
    }

    public Object getEntity() {
        return entity;
    }
}
