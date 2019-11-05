package com.stirante.firent.skills.fire;

import com.stirante.firent.MagicPlayer;
import com.stirante.firent.effects.FireBreathEffect;
import com.stirante.firent.skills.Skill;
import com.stirante.firent.skills.SkillActivationResult;
import com.stirante.firent.skills.SkillFamily;
import com.stirante.firent.skills.SkillTier;
import com.stirante.firent.utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.Sound;

import java.util.ArrayList;

public class FireBreathSkill extends Skill {

    @Override
    protected SkillActivationResult performSkill(MagicPlayer player) {
        player.addEffect(new FireBreathEffect(player));
        player.getPlayer().getWorld().playSound(player.getPlayer().getLocation(), Sound.GHAST_FIREBALL, 1, 1);
        return SkillActivationResult.SUCCESS;
    }

    @Override
    public float getRequiredEnergy() {
        return 0;
    }

    @Override
    public long getCooldownTime() {
        return 3000;
    }

    @Override
    public SkillFamily getFamily() {
        return SkillFamily.FIRE;
    }

    @Override
    public SkillTier getTier() {
        return SkillTier.ATTACK;
    }

    @Override
    public Material getMaterial() {
        return Material.FIREBALL;
    }

    @Override
    public String getName() {
        return "Ognisty oddech";
    }

    @Override
    public ArrayList<String> getDescription() {
        return StringUtils.asList("Zieje ogniem, zadając obrażenia i podpalając wrogów", "Podpaleni wrogowie " +
                "otrzymują większe obrażenia");
    }
}
