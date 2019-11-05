package com.stirante.firent.effects;

import com.stirante.firent.MagicPlayer;
import com.stirante.firent.utils.ParticleEffect;
import com.stirante.firent.utils.PlayerUtils;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LifeDrainEffect extends Effect {
    private int ticks = 0;

    public LifeDrainEffect(MagicPlayer player) {
        super(player);
    }

    @Override
    public void onTick() {
        ticks++;
        if (ticks % 20 == 0) {
            Collection<Entity> entities =
                    getBukkitPlayer().getWorld().getNearbyEntities(getBukkitPlayer().getLocation(), 4, 2, 4);
            for (Entity entity : entities) {
                if (entity instanceof LivingEntity && entity != getBukkitPlayer()) {
                    LivingEntity le = (LivingEntity) entity;
                    le.damage(3, getBukkitPlayer());
                    PlayerUtils.heal(getBukkitPlayer(), 1);
                    List<Location> line = getLine(getBukkitPlayer().getEyeLocation(), le.getEyeLocation());
                    for (Location location : line) {
//                        getBukkitPlayer().getWorld().spawnParticle(Particle.REDSTONE, location, 1, 0f, 0f, 0f, 0);
                        ParticleEffect.REDSTONE.display(0, 0, 0, 0, 1, location, 10);
                    }
                }
            }
        }
        if (ticks >= 100) {
            stopEffect();
        }
    }

    private List<Location> getLine(Location l1, Location l2) {
        List<Location> ls = new ArrayList<Location>();
        ls.add(l1);
        for (int i = 0; i < 15; i++) {
            double x = l1.getX() - (i * ((l1.getX() - l2.getX()) / 15));
            double y = l1.getY() - (i * ((l1.getY() - l2.getY()) / 15));
            double z = l1.getZ() - (i * ((l1.getZ() - l2.getZ()) / 15));
            ls.add(new Location(l1.getWorld(), x, y, z));
        }
        ls.add(l2);
        return ls;
    }
}
