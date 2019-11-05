package com.stirante.firent.skills.movement;

import com.stirante.firent.MagicPlayer;
import com.stirante.firent.effects.NinjaAttackEffect;
import com.stirante.firent.skills.Skill;
import com.stirante.firent.skills.SkillActivationResult;
import com.stirante.firent.skills.SkillFamily;
import com.stirante.firent.skills.SkillTier;
import com.stirante.firent.utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.ArrayList;
import java.util.Collection;

public class NinjaAttackSkill extends Skill {
    @Override
    protected SkillActivationResult performSkill(MagicPlayer player) {
        Collection<Entity> entities =
                player.getPlayer().getWorld().getNearbyEntities(player.getPlayer().getLocation(), 4, 3, 4);
        for (Entity entity : entities) {
            if (entity instanceof LivingEntity && entity != player.getPlayer()) {
                player.addEffect(new NinjaAttackEffect(player));
                return SkillActivationResult.SUCCESS;
            }
        }
        return SkillActivationResult.NO_TARGET;
    }

    @Override
    public float getRequiredEnergy() {
        return 0;
    }

    @Override
    public long getCooldownTime() {
        return 5000;
    }

    @Override
    public SkillFamily getFamily() {
        return SkillFamily.MOVEMENT;
    }

    @Override
    public SkillTier getTier() {
        return SkillTier.ATTACK;
    }

    @Override
    public Material getMaterial() {
        return Material.CACTUS;
    }

    @Override
    public String getName() {
        return "Ninja";
    }

    @Override
    public ArrayList<String> getDescription() {
        return StringUtils.asList("Teleportuje oraz atakuje do 4 pobliskich graczy");
    }
}
