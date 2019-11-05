package com.stirante.firent.skills.boost;

import com.stirante.firent.MagicPlayer;
import com.stirante.firent.effects.StompEffect;
import com.stirante.firent.skills.Skill;
import com.stirante.firent.skills.SkillActivationResult;
import com.stirante.firent.skills.SkillFamily;
import com.stirante.firent.skills.SkillTier;
import com.stirante.firent.utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class StompSkill extends Skill {

    @Override
    protected SkillActivationResult performSkill(MagicPlayer player) {
        player.addEffect(new StompEffect(player));
        player.getPlayer().setVelocity(new Vector(0, 1, 0));
        return SkillActivationResult.SUCCESS;
    }

    @Override
    public float getRequiredEnergy() {
        return 60;
    }

    @Override
    public long getCooldownTime() {
        return 10000;
    }

    @Override
    public SkillFamily getFamily() {
        return SkillFamily.BOOST;
    }

    @Override
    public SkillTier getTier() {
        return SkillTier.ULTIMATE;
    }

    @Override
    public Material getMaterial() {
        return Material.ANVIL;
    }

    @Override
    public String getName() {
        return "Mocne lądowanie";
    }

    @Override
    public ArrayList<String> getDescription() {
        return StringUtils.asList("Gracz skacze wysoko do góry, a spadając zadaje obrażenia pobliskim wrogom",
                "Wrogowie w powietrzu otrzymują mniejsze obrażenia");
    }
}
