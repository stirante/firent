package com.stirante.firent.effects;

import com.stirante.firent.MagicPlayer;
import org.bukkit.event.player.PlayerMoveEvent;

public class CageEffect extends Effect {

    private final int duration;
    private float originalSpeed;
    private int timeCounter = 0;

    public CageEffect(MagicPlayer player, int duration) {
        super(player);
        this.duration = duration;
        originalSpeed = player.getPlayer().getWalkSpeed();
        player.getPlayer().setWalkSpeed(0);
    }

    @Override
    public void onTick() {
        if (duration == -1) {
            return;
        }
        timeCounter++;
        if (timeCounter >= duration) {
            stopEffect();
        }
    }

    @Override
    public void onMove(PlayerMoveEvent e) {
        e.setCancelled(true);
    }

    @Override
    public void stopEffect() {
        if (getPlayer().hasEffect(this)) {
            super.stopEffect();
            getBukkitPlayer().setWalkSpeed(originalSpeed);
        }
    }
}
