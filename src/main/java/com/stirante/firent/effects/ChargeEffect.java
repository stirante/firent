package com.stirante.firent.effects;

import com.stirante.firent.MagicPlayer;
import com.stirante.firent.utils.ParticleEffect;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.Collection;

public class ChargeEffect extends Effect {

    private static final long EFFECT_LIFETIME = 1000;

    private long startTime = System.currentTimeMillis();

    public ChargeEffect(MagicPlayer player) {
        super(player);
    }

    @Override
    public void onTick() {
//        getBukkitPlayer().getWorld()
//                .spawnParticle(Particle.FLAME, getBukkitPlayer().getLocation(), 10, 0.5f, 1f, 0.5f, 0);
        ParticleEffect.FLAME.display(0.5f, 1, 0.5f, 0f, 10, getBukkitPlayer().getLocation(), 10);
        Collection<Entity> entities =
                getBukkitPlayer().getWorld().getNearbyEntities(getBukkitPlayer().getLocation(), 1, 3, 1);
        boolean stop = false;
        for (Entity entity : entities) {
            if (entity instanceof LivingEntity && entity != getBukkitPlayer()) {
                stop = true;
                ((LivingEntity) entity).damage(1, getBukkitPlayer());
                entity.setVelocity(new Vector(0, 1, 0));
            }
        }
        if (stop || System.currentTimeMillis() - startTime > EFFECT_LIFETIME) {
            stopEffect();
            if (stop) {
                getBukkitPlayer().setVelocity(new Vector(0, 0, 0));
                getBukkitPlayer().getWorld()
                        .playSound(getBukkitPlayer().getLocation(), Sound.FIREWORK_BLAST, 1, 1);
//                getBukkitPlayer().getWorld()
//                        .spawnParticle(Particle.FIREWORKS_SPARK, getBukkitPlayer().getEyeLocation(), 100, 1f, 3f, 1f, 0.01);
                ParticleEffect.FIREWORKS_SPARK.display(1, 3, 1, 0.01f, 100, getBukkitPlayer().getEyeLocation(), 10);
            }
        }
    }
}
