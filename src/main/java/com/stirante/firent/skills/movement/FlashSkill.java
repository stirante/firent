package com.stirante.firent.skills.movement;

import com.stirante.firent.Firent;
import com.stirante.firent.MagicPlayer;
import com.stirante.firent.skills.Skill;
import com.stirante.firent.skills.SkillActivationResult;
import com.stirante.firent.skills.SkillFamily;
import com.stirante.firent.skills.SkillTier;
import com.stirante.firent.utils.ParticleEffect;
import com.stirante.firent.utils.StringUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class FlashSkill extends Skill {

    @Override
    protected SkillActivationResult performSkill(final MagicPlayer player) {
        final Vector direction = player.getPlayer().getLocation().getDirection().clone();
        player.getPlayer().setVelocity(player.getPlayer().getLocation().getDirection().multiply(3D));
        final Location l = getTargetLocation(player.getPlayer().getLocation());
        new BukkitRunnable() {
            public void run() {
                Player pl = player.getPlayer();
                Location loc = pl.getLocation();
                pl.getWorld().playSound(loc, Sound.ENDERMAN_TELEPORT, 1, 1);
                pl.getWorld().playSound(l, Sound.ENDERMAN_TELEPORT, 1, 1);
                List<Location> ls1 = getOval(pl.getEyeLocation().clone().add(0, -.5D, 0), 1, .5D);
                for (Location l : ls1) {
                    ParticleEffect.DRIP_WATER.display(0f, 0f, 0f, 0f, 1, l, 10);
//                    player.getPlayer().getWorld().spawnParticle(Particle.DRIP_WATER, l, 1, 0f, 0f, 0f, 0f);
                }
                l.setDirection(loc.getDirection());
                pl.teleport(l);
                List<Location> ls2 = getOval(pl.getEyeLocation().clone().add(0, -.5D, 0), 1, .5D);
                for (Location l : ls2) {
                    ParticleEffect.DRIP_LAVA.display(0f, 0f, 0f, 0f, 1, l, 10);
//                    player.getPlayer().getWorld().spawnParticle(Particle.DRIP_LAVA, l, 1, 0f, 0f, 0f, 0f);
                }
                pl.setVelocity(direction.multiply(0.5D));
            }
        }.runTaskLater(Firent.getInstance(), 2);
        return SkillActivationResult.SUCCESS;
    }


    private List<Location> getOval(Location center, double height, double width) {
        List<Location> ls = new ArrayList<>();
        for (int i = 0; i < 360; i += 10) {
            double angle = Math.toRadians(i);
            double yaw = (center.getYaw() * Math.PI) / 180;
            double x = Math.cos(angle) * width;
            double z = Math.sin(yaw) * x + center.getZ();
            x = Math.cos(yaw) * x + center.getX();
            double y = Math.sin(angle) * height + center.getY();
            ls.add(new Location(center.getWorld(), x, y, z));
        }
        return ls;
    }

    private Location getTargetLocation(Location l) {
        Location clone = l.clone();
        clone.setPitch(0);
        Vector direction = clone.getDirection();
        double distance = 5;
        l = l.clone().add(direction.multiply(distance));
        ArrayList<Location> ls = new ArrayList<>();
        for (int i = -15; i < 15; i++) {
            if (l.getBlockY() + i > 255) {
                continue;
            }
            Location l1 = l.clone().add(0, i, 0);
            if (isSafe(l1)) {
                ls.add(l1);
            }
        }
        double dist = 1000D;
        Location l2 = null;
        for (Location l1 : ls) {
            if (l1.distanceSquared(l) < dist) {
                dist = l1.distanceSquared(l);
                l2 = l1;
            }
        }
        if (l2 != null) {
            return l2.getBlock().getRelative(BlockFace.UP).getLocation().setDirection(direction);
        }
        return l.getWorld().getHighestBlockAt(l).getRelative(BlockFace.UP).getLocation().setDirection(direction);
    }

    private boolean isSafe(Location l) {
        return l.getBlock().getType() != Material.AIR && !l.getBlock().getRelative(BlockFace.UP).getType().isSolid() &&
                l.getBlock().getRelative(BlockFace.UP).getRelative(BlockFace.UP).getType() == Material.AIR;
    }

    @Override
    public float getRequiredEnergy() {
        return 50;
    }

    @Override
    public long getCooldownTime() {
        return 3000;
    }

    @Override
    public SkillFamily getFamily() {
        return SkillFamily.MOVEMENT;
    }

    @Override
    public SkillTier getTier() {
        return SkillTier.UTILITY;
    }

    @Override
    public Material getMaterial() {
        return Material.FISHING_ROD;
    }

    @Override
    public String getName() {
        return "Flash";
    }

    @Override
    public ArrayList<String> getDescription() {
        return StringUtils.asList("Teleportuje gracza parę bloków do przodu", "Możliwe jest teleportowanie się przez " +
                "ściany");
    }
}
