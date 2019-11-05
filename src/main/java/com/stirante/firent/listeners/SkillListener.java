package com.stirante.firent.listeners;

import com.stirante.firent.MagicPlayer;
import com.stirante.firent.skills.Skill;
import com.stirante.firent.skills.SkillActivationResult;
import com.stirante.firent.utils.PlayerUtils;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class SkillListener implements Listener {

    @EventHandler
    public void onSkillChange(PlayerItemHeldEvent e) {
        if (e.getNewSlot() < 4) {
            e.getPlayer().getInventory().setHeldItemSlot(8);
            e.setCancelled(true);
            MagicPlayer player = MagicPlayer.getPlayer(e.getPlayer());
            Skill fs = player.getSkill(e.getNewSlot());
            if (fs != null) {
                SkillActivationResult result = fs.activateSkill(player);
                if (result == SkillActivationResult.ON_COOLDOWN) {
                    PlayerUtils.sendActionBar(e.getPlayer(), ChatColor.RED + "Cooldown");
                }
                else if (result == SkillActivationResult.NO_ENERGY) {
                    PlayerUtils.sendActionBar(e.getPlayer(), ChatColor.AQUA + "No energy");
                }
                else if (result == SkillActivationResult.FAILURE) {
                    PlayerUtils.sendActionBar(e.getPlayer(), ChatColor.DARK_RED + "Error");
                }
                else if (result == SkillActivationResult.NO_TARGET) {
                    PlayerUtils.sendActionBar(e.getPlayer(), ChatColor.GRAY + "No target");
                }
            }
        }
        else if (e.getNewSlot() != 8) {
            e.getPlayer().getInventory().setHeldItemSlot(8);
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        MagicPlayer.onEntityDamage(e);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        MagicPlayer.onPlayerMove(e);
    }

    @EventHandler
    public void onChooseSkills(PlayerDropItemEvent e) {
        e.setCancelled(true);
        MagicPlayer.getPlayer(e.getPlayer()).chooseSkills();
    }

}
