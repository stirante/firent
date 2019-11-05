package com.stirante.firent.listeners;

import com.stirante.firent.Firent;
import com.stirante.firent.MagicPlayer;
import com.stirante.firent.MagicWorld;
import com.stirante.firent.entities.HpBoostEntity;
import com.stirante.firent.entities.MagicEntity;
import com.stirante.firent.entities.SpeedBoostEntity;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.util.Vector;

public class PlayerListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        e.getPlayer().setAllowFlight(true);
        MagicPlayer.join(e.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        MagicPlayer.quit(e.getPlayer());
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        MagicPlayer.join(e.getPlayer());
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        e.setCancelled(true);
        if (e.getClick().isLeftClick() && e.getWhoClicked() instanceof Player && e.getCurrentItem() != null) {
            Player player = (Player) e.getWhoClicked();
            MagicPlayer sPlayer = MagicPlayer.getPlayer(player);
            sPlayer.onInventoryClick(e.getClickedInventory(), e.getCurrentItem());
        }
    }

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent e) {
        e.setCancelled(true);
        e.getItem().remove();
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        e.getDrops().clear();
        e.setDroppedExp(0);
        if (e.getEntity() instanceof Player) {
            MagicPlayer.quit((Player) e.getEntity());
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (!e.getPlayer().isOp()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if (!e.getPlayer().isOp()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onMegumin(EntityExplodeEvent e) {
        e.blockList().clear();
    }

    @EventHandler
    public void onFlightAttempt(PlayerToggleFlightEvent event) {
        if (event.isFlying() && event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            event.getPlayer().setVelocity(event.getPlayer().getVelocity().add(new Vector(0, 0.25, 0)));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onGameModeChange(PlayerGameModeChangeEvent e) {
        if (e.getNewGameMode() == GameMode.SURVIVAL) {
            e.getPlayer().setAllowFlight(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player pl = e.getPlayer();
            if (Firent.getInstance().spawnProcess.containsKey(pl.getUniqueId())) {
                Class<? extends MagicEntity> clz = Firent.getInstance().spawnProcess.get(pl.getUniqueId());
                MagicWorld world = MagicWorld.getWorld(e.getClickedBlock().getWorld());
                if (clz == HpBoostEntity.class) {
                    world.addEntity(new HpBoostEntity(e.getClickedBlock().getLocation()));
                }
                else if (clz == SpeedBoostEntity.class) {
                    world.addEntity(new SpeedBoostEntity(e.getClickedBlock().getLocation()));
                }
                Firent.getInstance().spawnProcess.remove(e.getPlayer().getUniqueId());
                e.getPlayer().sendMessage(ChatColor.GREEN + "Done");
            }
        }
    }

}
