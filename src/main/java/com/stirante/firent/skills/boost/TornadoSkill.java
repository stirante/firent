package com.stirante.firent.skills.boost;

import com.stirante.firent.MagicPlayer;
import com.stirante.firent.effects.TornadoEffect;
import com.stirante.firent.skills.Skill;
import com.stirante.firent.skills.SkillActivationResult;
import com.stirante.firent.skills.SkillFamily;
import com.stirante.firent.skills.SkillTier;
import com.stirante.firent.utils.StringUtils;
import org.bukkit.Material;

import java.util.ArrayList;

public class TornadoSkill extends Skill {
    @Override
    protected SkillActivationResult performSkill(MagicPlayer player) {
        player.addEffect(new TornadoEffect(player));
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
        return SkillFamily.BOOST;
    }

    @Override
    public SkillTier getTier() {
        return SkillTier.ATTACK;
    }

    @Override
    public Material getMaterial() {
        return Material.FEATHER;
    }

    @Override
    public String getName() {
        return "Tornado";
    }

    @Override
    public ArrayList<String> getDescription() {
        return StringUtils.asList("Tworzy tornado w wybranym kierunku, które zadaje obrażenia i wyrzuca przeciwników " +
                "w powietrze");
    }
}
