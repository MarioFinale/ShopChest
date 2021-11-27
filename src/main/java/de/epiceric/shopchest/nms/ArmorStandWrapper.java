package de.epiceric.shopchest.nms;

<<<<<<< Updated upstream
import java.lang.reflect.Field;
import java.lang.reflect.Method;
=======
>>>>>>> Stashed changes
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.inventivetalent.reflection.resolver.minecraft.NMSClassResolver;

import de.epiceric.shopchest.ShopChest;
import org.bukkit.scheduler.BukkitRunnable;

public class ArmorStandWrapper {
<<<<<<< Updated upstream
    private final NMSClassResolver nmsClassResolver = new NMSClassResolver();
    private final Class<?> packetDataSerializerClass = nmsClassResolver.resolveSilent("network.PacketDataSerializer");
    private final Class<?> packetPlayOutEntityDestroyClass = nmsClassResolver.resolveSilent("network.protocol.game.PacketPlayOutEntityDestroy");
    private final Class<?> packetPlayOutEntityMetadataClass = nmsClassResolver.resolveSilent("network.protocol.game.PacketPlayOutEntityMetadata");
    private final Class<?> packetPlayOutEntityTeleportClass = nmsClassResolver.resolveSilent("network.protocol.game.PacketPlayOutEntityTeleport");
    private final Class<?> dataWatcherClass = nmsClassResolver.resolveSilent("network.syncher.DataWatcher");
=======
>>>>>>> Stashed changes

    private UUID uuid;
    private int entityId;

    private ShopChest plugin;
<<<<<<< Updated upstream
=======
    private Entity entity;
>>>>>>> Stashed changes
    private Location location;
    private String customName;

    public ArmorStandWrapper(ShopChest plugin, Location location, String customName) {
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
        armorStand.setCanTick(false);
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
                    armorStand.setCanTick(false);
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
<<<<<<< Updated upstream
        double y = location.getY() + (Utils.getServerVersion().equals("v1_8_R1") ? 0 : 1.975);
        Object packet;

        try {
            if (Utils.getMajorVersion() >= 17) {
                // Empty constructor does not exist anymore in 1.17+ so create packet via serializer
                Class<?> byteBufClass = Class.forName("io.netty.buffer.ByteBuf");
                Class<?> unpooledClass = Class.forName("io.netty.buffer.Unpooled");
                Object buffer = unpooledClass.getMethod("buffer").invoke(null);
                Object serializer = packetDataSerializerClass.getConstructor(byteBufClass).newInstance(buffer);

                Method d = packetDataSerializerClass.getMethod("d", int.class);
                Method writeDouble = packetDataSerializerClass.getMethod("writeDouble", double.class);
                Method writeByte = packetDataSerializerClass.getMethod("writeByte", int.class);
                Method writeBoolean = packetDataSerializerClass.getMethod("writeBoolean", boolean.class);

                d.invoke(serializer, getEntityId());
                writeDouble.invoke(serializer, location.getX());
                writeDouble.invoke(serializer, y);
                writeDouble.invoke(serializer, location.getZ());
                writeByte.invoke(serializer, 0);
                writeByte.invoke(serializer, 0);
                writeBoolean.invoke(serializer, false);

                packet = packetPlayOutEntityTeleportClass.getConstructor(packetDataSerializerClass).newInstance(serializer);
            } else {
                packet = packetPlayOutEntityTeleportClass.getConstructor().newInstance();
                Field[] fields = packetPlayOutEntityTeleportClass.getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                }
    
                boolean isPre9 = Utils.getMajorVersion() < 9;
                fields[0].set(packet, entityId);
    
                if (isPre9) {
                    fields[1].set(packet, (int)(location.getX() * 32));
                    fields[2].set(packet, (int)(y * 32));
                    fields[3].set(packet, (int)(location.getZ() * 32));
                } else {
                    fields[1].set(packet, location.getX());
                    fields[2].set(packet, y);
                    fields[3].set(packet, location.getZ());
                }
                fields[4].set(packet, (byte) 0);
                fields[5].set(packet, (byte) 0);
                fields[6].set(packet, true);
            }

            if (packet == null) {
                plugin.getLogger().severe("Could not set hologram location");
                plugin.debug("Could not set armor stand location: Packet is null");
                return;
            }

            for (Player player : location.getWorld().getPlayers()) {
                Utils.sendPacket(plugin, packet, player);
            }
        } catch (ReflectiveOperationException e) {
            plugin.getLogger().severe("Could not set hologram location");
            plugin.debug("Could not set armor stand location");
            plugin.debug(e);
        }
=======
        entity.teleport(location);
>>>>>>> Stashed changes
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
}
