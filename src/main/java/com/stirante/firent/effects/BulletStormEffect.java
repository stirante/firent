package com.stirante.firent.effects;

import com.stirante.firent.MagicPlayer;
import org.bukkit.util.Vector;

public class BulletStormEffect extends Effect {

    private int ticks = 100;
    private Vector v;

    public BulletStormEffect(MagicPlayer player) {
        super(player);
        v = getBukkitPlayer().getLocation().getDirection();
    }

    @Override
    public void onTick() {
        ticks--;
        if (ticks % 5 == 1) {
            getBukkitPlayer().getWorld()
                    .spawnArrow(getBukkitPlayer().getEyeLocation().clone().add(v.clone().multiply(1.5)), v, 2f, 2f);
        }
        if (ticks <= 0) {
            stopEffect();
        }
    }
}
