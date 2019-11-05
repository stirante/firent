package com.stirante.firent.effects;

import com.stirante.firent.MagicPlayer;
import com.stirante.firent.utils.ParticleEffect;
import org.bukkit.Location;

public class BuffEffect extends Effect {
    private static final double radius = 1.2;
    private final CageEffect cage;
    private double stage = 0;
    private int rotation = 0;

    public BuffEffect(MagicPlayer player) {
        super(player);
        cage = new CageEffect(player, -1);
        player.addEffect(cage);
    }

    @Override
    public void onTick() {
        for (int i = 0; i < 7; i++) {
            Location loc1 = getBukkitPlayer().getLocation()
                    .clone()
                    .add(Math.sin(Math.toRadians(rotation)) * radius, stage,
                            Math.cos(Math.toRadians(rotation)) * radius);
            Location loc2 = getBukkitPlayer().getLocation()
                    .clone()
                    .add(Math.cos(Math.toRadians(rotation)) * radius, stage,
                            Math.sin(Math.toRadians(rotation)) * radius);
            Location loc3 = getBukkitPlayer().getLocation()
                    .clone()
                    .add(Math.sin(Math.toRadians(rotation + 180)) * radius, stage,
                            Math.cos(Math.toRadians(rotation + 180)) * radius);
            Location loc4 = getBukkitPlayer().getLocation()
                    .clone()
                    .add(Math.cos(Math.toRadians(rotation + 180)) * radius, stage,
                            Math.sin(Math.toRadians(rotation + 180)) * radius);
            ParticleEffect.DRIP_WATER.display(0, 0, 0, 0, 1, loc1, 10);
            ParticleEffect.DRIP_WATER.display(0, 0, 0, 0, 1, loc2, 10);
            ParticleEffect.DRIP_WATER.display(0, 0, 0, 0, 1, loc3, 10);
            ParticleEffect.DRIP_WATER.display(0, 0, 0, 0, 1, loc4, 10);
//            getBukkitPlayer().getWorld().spawnParticle(Particle.DRIP_WATER, loc1, 1, 0, 0, 0, 0);
//            getBukkitPlayer().getWorld().spawnParticle(Particle.DRIP_WATER, loc2, 1, 0, 0, 0, 0);
//            getBukkitPlayer().getWorld().spawnParticle(Particle.DRIP_WATER, loc3, 1, 0, 0, 0, 0);
//            getBukkitPlayer().getWorld().spawnParticle(Particle.DRIP_WATER, loc4, 1, 0, 0, 0, 0);
            rotation += 2;
            if (rotation >= 360) {
                rotation = 0;
            }
            stage += 0.01;
            if (stage >= 3 && rotation == 46) {
                stopEffect();
                return;
            }
        }
    }

    @Override
    public void stopEffect() {
        super.stopEffect();
        cage.stopEffect();
    }
}
