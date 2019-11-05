package com.stirante.firent.utils;

import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class PlayerUtils {

    public static void heal(Player pl, double amount) {
        double v = pl.getHealth() + amount;
        if (pl.getMaxHealth() < v) {
            pl.setHealth(pl.getMaxHealth());
        }
        else {
            pl.setHealth(v);
        }
    }

    public static void sendActionBar(Player player, String message){
//        PacketPlayOutChat packet = new PacketPlayOutChat(new ChatComponentText(message), (byte)2);
//        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        try {
            Object chatComponentText =
                    ReflectionUtils.instantiateObject("ChatComponentText", ReflectionUtils.PackageType.MINECRAFT_SERVER, message, (byte) 2);
            Object packetPlayOutChat =
                    ReflectionUtils.instantiateObject("PacketPlayOutChat", ReflectionUtils.PackageType.MINECRAFT_SERVER, chatComponentText);
            Method getHandle =
                    ReflectionUtils.getMethod("CraftPlayer", ReflectionUtils.PackageType.CRAFTBUKKIT_ENTITY, "getHandle");
            Field playerConnection =
                    ReflectionUtils.getField("EntityPlayer", ReflectionUtils.PackageType.MINECRAFT_SERVER, true, "playerConnection");
            Class packetClass =
                    ReflectionUtils.PackageType.MINECRAFT_SERVER.getClass("Packet");
            Method sendPacket =
                    ReflectionUtils.getMethod("PlayerConnection", ReflectionUtils.PackageType.MINECRAFT_SERVER, "sendPacket", packetClass);
            sendPacket.invoke(playerConnection.get(getHandle.invoke(player)), packetPlayOutChat);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
