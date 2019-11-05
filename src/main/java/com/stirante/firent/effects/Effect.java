package com.stirante.firent.effects;

import com.stirante.firent.MagicPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public abstract class Effect {

    private final MagicPlayer player;

    public Effect(MagicPlayer player) {
        this.player = player;
    }

    public void onTick() {

    }

    public void onDamage(EntityDamageEvent e) {

    }

    public void onAttack(EntityDamageEvent e) {

    }

    public void onMove(PlayerMoveEvent e) {

    }

    public MagicPlayer getPlayer() {
        return player;
    }

    public Player getBukkitPlayer() {
        return player.getPlayer();
    }

    public void stopEffect() {
        getPlayer().removeEffect(this);
    }

}
