package com.stirante.firent.skills.movement;

import com.stirante.firent.MagicPlayer;
import com.stirante.firent.skills.Skill;
import com.stirante.firent.skills.SkillActivationResult;
import com.stirante.firent.skills.SkillFamily;
import com.stirante.firent.skills.SkillTier;
import com.stirante.firent.utils.StringUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.ArrayList;
import java.util.Collection;

public class BackstabSkill extends Skill {

    @Override
    protected SkillActivationResult performSkill(MagicPlayer player) {
        for (int i = 1; i < 6; i++) {
            Collection<Entity> entities = player.getPlayer()
                    .getWorld()
                    .getNearbyEntities(getTargetLocation(player.getPlayer().getLocation(), i), 0.6, 2, 0.6);
            for (Entity entity : entities) {
                if (entity != player.getPlayer() && entity instanceof LivingEntity) {
                    Location nLoc = entity.getLocation()
                            .add(0, 1.5, 0)
                            .add(player.getPlayer().getLocation().getDirection().multiply(1.5));
                    nLoc.setYaw(player.getPlayer().getLocation().getYaw() + 180);
                    nLoc.setPitch(player.getPlayer().getLocation().getPitch());
                    player.getPlayer().teleport(nLoc);
                    return SkillActivationResult.SUCCESS;
                }
            }
        }
        return SkillActivationResult.NO_TARGET;
    }

    private Location getTargetLocation(Location l, double distance) {
        return l.clone().add(l.getDirection().multiply(distance));
    }

    @Override
    public float getRequiredEnergy() {
        return 0;
    }

    @Override
    public long getCooldownTime() {
        return 2000;
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
        return Material.WOOD_SWORD;
    }

    @Override
    public String getName() {
        return "Brutus";
    }

    @Override
    public ArrayList<String> getDescription() {
        return StringUtils.asList("Teleportuje za wybranego gracza", "Przeciwnicy atakowani z tyłu otrzymują większe " +
                "obrażenia");
    }
}
