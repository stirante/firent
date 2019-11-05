package com.stirante.firent.effects;

import com.stirante.firent.MagicPlayer;
import com.stirante.firent.utils.ParticleEffect;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collection;

public class TornadoEffect extends Effect {

    private Location startLocation;
    private Vector direction;
    private int tick = 0;
    private ArrayList<Entity> damaged = new ArrayList<>();

    public TornadoEffect(MagicPlayer player) {
        super(player);
        startLocation = getBukkitPlayer().getLocation().clone().add(0, 1.5, 0);
        direction = startLocation.getDirection().multiply(1.5).setY(0);
    }

    @Override
    public void onTick() {
        tick++;
        startLocation = startLocation.add(direction);
//        getBukkitPlayer().getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, startLocation, 80, 0.7f, 1.5f, 0.7f, 0);
        ParticleEffect.EXPLOSION_NORMAL.display(0.7f, 1.5f, 0.7f, 0, 80, startLocation, 10);
        Collection<Entity> entities = getBukkitPlayer().getWorld().getNearbyEntities(startLocation, 0.5, 3, 0.5);
        for (Entity entity : entities) {
            if (entity instanceof LivingEntity && getBukkitPlayer() != entity && !damaged.contains(entity)) {
                damaged.add(entity);
                ((LivingEntity) entity).damage(1, getBukkitPlayer());
                entity.setVelocity(new Vector(0, 1, 0));
            }
        }
        if (tick >= 20) {
            stopEffect();
        }
    }
}
