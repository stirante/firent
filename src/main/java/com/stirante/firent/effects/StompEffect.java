package com.stirante.firent.effects;

import com.stirante.firent.MagicPlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;

import java.util.Collection;

public class StompEffect extends Effect {

    private int ticks = 0;

    public StompEffect(MagicPlayer player) {
        super(player);
    }

    @Override
    public void onTick() {
        ticks++;
        if (ticks == 5) {
            getBukkitPlayer().setVelocity(new Vector(0, -2, 0));
        }
        else if (ticks == 9) {
            Collection<Entity> entities =
                    getBukkitPlayer().getWorld().getNearbyEntities(getBukkitPlayer().getLocation(), 5, 5, 5);
            for (Entity entity : entities) {
                if (entity != getBukkitPlayer() && entity instanceof LivingEntity) {
                    LivingEntity le = ((LivingEntity) entity);
                    le.damage(2, getBukkitPlayer());
                    double force = 0.5;
                    if (entity.isOnGround()) {
                        force = 1;
                    }
                    le.setVelocity(le.getVelocity().add(new Vector(0, force, 0)));
                }
            }
            getBukkitPlayer().getWorld().playSound(getBukkitPlayer().getLocation(), Sound.ANVIL_LAND, 1, 1);
        }
        else if (ticks > 20) {
            stopEffect();
        }
    }

    @Override
    public void onDamage(EntityDamageEvent e) {
        if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
            e.setCancelled(true);
        }
    }
}
