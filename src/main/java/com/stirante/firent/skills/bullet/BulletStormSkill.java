package com.stirante.firent.skills.bullet;

import com.stirante.firent.MagicPlayer;
import com.stirante.firent.effects.BulletStormEffect;
import com.stirante.firent.skills.Skill;
import com.stirante.firent.skills.SkillActivationResult;
import com.stirante.firent.skills.SkillFamily;
import com.stirante.firent.skills.SkillTier;
import com.stirante.firent.utils.StringUtils;
import org.bukkit.Material;

import java.util.ArrayList;

public class BulletStormSkill extends Skill {
    @Override
    protected SkillActivationResult performSkill(MagicPlayer player) {
        player.addEffect(new BulletStormEffect(player));
        return SkillActivationResult.SUCCESS;
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
        return SkillFamily.BULLET;
    }

    @Override
    public SkillTier getTier() {
        return SkillTier.ULTIMATE;
    }

    @Override
    public Material getMaterial() {
        return Material.ARROW;
    }

    @Override
    public String getName() {
        return "Grad strzał";
    }

    @Override
    public ArrayList<String> getDescription() {
        return StringUtils.asList("Szybko wystrzeliwuje 10 strzał w wybranym kierunku", "Kierunek strzelania nie " +
                "zmienia się po rozpoczęciu umiejętności");
    }
}
