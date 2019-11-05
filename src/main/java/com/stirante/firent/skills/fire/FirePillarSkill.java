package com.stirante.firent.skills.fire;

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
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.ArrayList;
import java.util.Collection;

public class FirePillarSkill extends Skill {

    @Override
    protected SkillActivationResult performSkill(MagicPlayer player) {
        Location center = player.getPlayer().getLocation();
        Location centerB = player.getPlayer().getLocation().getBlock().getLocation();
        double offX = center.getX() - centerB.getBlockX();
        double offZ = center.getZ() - centerB.getBlockZ();
        for (double x = centerB.getBlockX() - 3; x < centerB.getBlockX() + 3; x += 0.5) {
            for (double z = centerB.getBlockZ() - 3; z < centerB.getBlockZ() + 3; z += 0.5) {
                Location l = new Location(centerB.getWorld(), x + offX, centerB.getBlockY() + 0.2f, z + offZ);
                if (l.distanceSquared(center) <= 5) {
//                    player.getPlayer()
//                            .getWorld()
//                            .spawnParticle(Particle.FLAME, l.add(0, 2, 0), 20, 0.2f, 2f, 0.2f, 0.01f);
                    ParticleEffect.FLAME.display(0.2f, 2f, 0.2f, 0.01f, 20, l.add(0, 2, 0), 20);
                }
            }
        }
        player.getPlayer().getWorld().playSound(player.getPlayer().getLocation(), Sound.GHAST_FIREBALL, 1, 1);
        Collection<Entity> entities = center.getWorld().getNearbyEntities(center, 3, 2, 3);
        for (Entity entity : entities) {
            if (entity instanceof LivingEntity && entity != player.getPlayer() &&
                    entity.getLocation().distanceSquared(center) <= 5) {
                if (entity.getFireTicks() > 0) {
                    ((LivingEntity) entity).damage(2, player.getPlayer());
                }
                else {
                    ((LivingEntity) entity).damage(1, player.getPlayer());
                }
                entity.setFireTicks(100);
            }
        }
        return SkillActivationResult.SUCCESS;
    }

    @Override
    public float getRequiredEnergy() {
        return 0;
    }

    @Override
    public long getCooldownTime() {
        return 6000;
    }

    @Override
    public SkillFamily getFamily() {
        return SkillFamily.FIRE;
    }

    @Override
    public SkillTier getTier() {
        return SkillTier.ULTIMATE;
    }

    @Override
    public Material getMaterial() {
        return Material.FIREBALL;
    }

    @Override
    public String getName() {
        return "Ognista pożoga";
    }

    @Override
    public ArrayList<String> getDescription() {
        return StringUtils.asList("Tworzy słup ognia, który rani oraz podpala pobliskich graczy", "Podpaleni gracze " +
                "otrzymują większe obrażenia");
    }
}
