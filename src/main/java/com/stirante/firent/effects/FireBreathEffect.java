package com.stirante.firent.effects;

import com.stirante.firent.MagicPlayer;
import com.stirante.firent.utils.ParticleEffect;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.ArrayList;
import java.util.Collection;

public class FireBreathEffect extends Effect {

    private Location startLocation;
    private int ticks = 0;
    private ArrayList<LivingEntity> affected = new ArrayList<>();

    public FireBreathEffect(MagicPlayer player) {
        super(player);
        startLocation = getBukkitPlayer().getEyeLocation();
    }

    private Location getTargetLocation(Location l, double distance) {
        return l.clone().add(l.getDirection().multiply(distance));
    }

    @Override
    public void onTick() {
        ticks++;
        if (ticks > 10) {
            stopEffect();
        }
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                Location l = startLocation.clone();
                l.setYaw(l.getYaw() + (x * 2) - 5);
                l.setPitch(l.getPitch() + (y * 2) - 5);
                l = getTargetLocation(l, ((double) ticks));
//                getBukkitPlayer().getWorld().spawnParticle(Particle.FLAME, l, 2, 0.1f, 0.1f, 0.1f, 0.01f);
                ParticleEffect.FLAME.display(0.1f, 0.1f, 0.1f, 0.01f, 2, l, 10);
            }
        }
        Location l = startLocation.clone();
        l.setYaw(l.getYaw());
        l.setPitch(l.getPitch());
        l = getTargetLocation(l, ((double) ticks));
        Collection<Entity> entities = l.getWorld().getNearbyEntities(l, 0.5, 0.5, 0.5);
        for (Entity entity : entities) {
            if (entity instanceof LivingEntity && entity != getBukkitPlayer() && !affected.contains(entity)) {
                LivingEntity le = (LivingEntity) entity;
                if (entity.getFireTicks() > 0) {
                    ((LivingEntity) entity).damage(4, getBukkitPlayer());
                }
                else {
                    ((LivingEntity) entity).damage(2, getBukkitPlayer());
                }
                le.setFireTicks(60);
                affected.add(le);
            }
        }
    }
}
