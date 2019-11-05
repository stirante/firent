package com.stirante.firent.skills.bullet;

import com.stirante.firent.MagicPlayer;
import com.stirante.firent.effects.BuffEffect;
import com.stirante.firent.skills.Skill;
import com.stirante.firent.skills.SkillActivationResult;
import com.stirante.firent.skills.SkillFamily;
import com.stirante.firent.skills.SkillTier;
import com.stirante.firent.utils.StringUtils;
import org.bukkit.Material;

import java.util.ArrayList;

public class TestSkill extends Skill {
    @Override
    protected SkillActivationResult performSkill(MagicPlayer player) {
        player.addEffect(new BuffEffect(player));
        return SkillActivationResult.SUCCESS;
    }

    @Override
    public float getRequiredEnergy() {
        return 0;
    }

    @Override
    public long getCooldownTime() {
        return 500;
    }

    @Override
    public SkillFamily getFamily() {
        return SkillFamily.BULLET;
    }

    @Override
    public SkillTier getTier() {
        return SkillTier.UTILITY;
    }

    @Override
    public Material getMaterial() {
        return Material.COMMAND;
    }

    @Override
    public String getName() {
        return "Staza";
    }

    @Override
    public ArrayList<String> getDescription() {
        return StringUtils.asList("Staza");
    }
}
