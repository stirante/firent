package com.stirante.firent.effects;

import com.stirante.firent.MagicPlayer;
import com.stirante.firent.utils.ParticleEffect;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class MissileEffect extends Effect {

    private static final float SPEED = 0.1f;
    private ArrayList<Missile> missiles = new ArrayList<>();

    public MissileEffect(MagicPlayer player) {
        super(player);
        Collection<Entity> entities =
                getBukkitPlayer().getWorld().getNearbyEntities(getBukkitPlayer().getLocation(), 8, 3, 8);
        for (Entity entity : entities) {
            if (entity instanceof LivingEntity && getBukkitPlayer() != entity) {
                missiles.add(new Missile(((LivingEntity) entity)));
            }
        }
    }

    @Override
    public void onTick() {
        Iterator<Missile> it = missiles.iterator();
        while (it.hasNext()) {
            Missile missile = it.next();
            for (int i = 0; i < 7; i++) {
                missile.update();
            }
            if (!missile.isAlive) {
                it.remove();
            }
        }
        if (missiles.isEmpty()) {
            stopEffect();
        }
    }

    private class Missile {
        private Location location;
        private Vector direction;
        private LivingEntity target;
        private boolean isAlive = true;

        public Missile(LivingEntity entity) {
            target = entity;
            direction = new Vector(0, 1, 0);
            location = getBukkitPlayer().getEyeLocation().clone().add(0, 1, 0);
        }

        public void update() {
            if (!isAlive) {
                return;
            }
            if (target == null || (target instanceof Player && !((Player) target).isOnline()) || target.isDead()) {
                isAlive = false;
                return;
            }
            location = location.add(direction.clone().multiply(SPEED));
            if (location.toVector().subtract(target.getEyeLocation().toVector()).lengthSquared() < 1) {
                target.damage(3, getBukkitPlayer());
                isAlive = false;
                for (int i = 0; i < 20; i++) {
//                    getBukkitPlayer().getWorld()
//                            .spawnParticle(Particle.REDSTONE, location.clone()
//                                    .add(Math.random(), Math.random(), Math.random()), 1, 0f, 0f, 0f, 0);
                    ParticleEffect.REDSTONE.display(0, 0, 0, 0, 1, location.clone()
                            .add(Math.random(), Math.random(), Math.random()), 10f);
                }
            }
            else {
//                getBukkitPlayer().getWorld().spawnParticle(Particle.REDSTONE, location, 1, 0f, 0f, 0f, 0);
                ParticleEffect.REDSTONE.display(0, 0, 0, 0, 1, location, 10f);
            }
            Vector targetDirection = target.getEyeLocation().toVector().subtract(location.toVector()).normalize();
            direction.setX(direction.getX() - ((direction.getX() - targetDirection.getX()) * 0.01));
            direction.setY(direction.getY() - ((direction.getY() - targetDirection.getY()) * 0.01));
            direction.setZ(direction.getZ() - ((direction.getZ() - targetDirection.getZ()) * 0.01));
        }

    }

}
