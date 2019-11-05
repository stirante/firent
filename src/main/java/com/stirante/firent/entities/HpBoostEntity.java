package com.stirante.firent.entities;

import com.stirante.firent.SoundPlayer;
import com.stirante.firent.utils.ParticleEffect;
import com.stirante.firent.utils.PlayerUtils;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Collection;

public class HpBoostEntity extends MagicEntity {
    private static final int COOLDOWN = 60000;
    private static final int PARTICLE_SPAWN_DELAY = 10;
    private static final int HEAL_AMOUNT = 6;

    private long lastTaken = -1L;
    private int particleDelayCounter = 0;

    public HpBoostEntity(Location location) {
        super(location);
    }

    @Override
    public void update() {
        if (System.currentTimeMillis() - lastTaken > COOLDOWN) {
            particleDelayCounter++;
            World world = getLocation().getWorld();
            if (particleDelayCounter == PARTICLE_SPAWN_DELAY) {
//                world.spawnParticle(Particle.HEART, getCenterLocation(), 5, 0.2, 0.5, 0.2, 0);
                ParticleEffect.HEART.display(0.2f, 0.5f, 0.2f, 0, 5, getCenterLocation(), 15);
                particleDelayCounter = 0;
            }
            Collection<Entity> nearbyEntities = world.getNearbyEntities(getCenterLocation(), 0.5, 2, 0.5);
            for (Entity entity : nearbyEntities) {
                if (entity instanceof Player) {
                    lastTaken = System.currentTimeMillis();
                    PlayerUtils.heal((Player) entity, HEAL_AMOUNT);
                    SoundPlayer.createNew(getLocation(), Sound.NOTE_PLING)
                            .addNote(SoundPlayer.Tone.C2)
                            .addPause(1)
                            .addNote(SoundPlayer.Tone.E2)
                            .addPause(1)
                            .addNote(SoundPlayer.Tone.G2)
                            .play();
                    return;
                }
            }
        }
    }
}
