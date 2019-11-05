package com.stirante.firent.effects;

import com.stirante.firent.MagicPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffectType;

public class GhostEffect extends Effect {

    private int ticks = 0;

    public GhostEffect(MagicPlayer player) {
        super(player);
    }

    @Override
    public void onAttack(EntityDamageEvent e) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.showPlayer(getBukkitPlayer());
        }
        getBukkitPlayer().removePotionEffect(PotionEffectType.SPEED);
        e.setDamage(e.getDamage() + 4);
        stopEffect();
    }

    @Override
    public void onTick() {
        ticks++;
        if (ticks >= 80) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.showPlayer(getBukkitPlayer());
            }
            stopEffect();
        }
    }
}
