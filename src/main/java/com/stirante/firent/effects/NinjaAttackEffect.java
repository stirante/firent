package com.stirante.firent.effects;

import com.stirante.firent.MagicPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;

public class NinjaAttackEffect extends Effect {

    private int tick = 0;
    private ArrayList<Entity> damaged = new ArrayList<>();

    public NinjaAttackEffect(MagicPlayer player) {
        super(player);
    }

    @Override
    public void onTick() {
        if (tick % 10 == 0) {
            Collection<Entity> entities =
                    getBukkitPlayer().getWorld().getNearbyEntities(getBukkitPlayer().getLocation(), 4, 3, 4);
            for (Entity entity : entities) {
                if (entity instanceof LivingEntity && getBukkitPlayer() != entity && !damaged.contains(entity)) {
                    getBukkitPlayer().teleport(getTpLocation(getBukkitPlayer(), entity));
                    damaged.add(entity);
                    ((LivingEntity) entity).damage(3, getBukkitPlayer());
                }
            }
        }
        if (tick >= 40) {
            stopEffect();
        }
        tick++;
    }

    public Location getTpLocation(Player pl, Entity e) {
        Location clone = e.getLocation().clone();
        clone.setDirection(pl.getLocation().getDirection());
        clone = clone.add(0, 1.6, 0);
        return clone;
    }

}
