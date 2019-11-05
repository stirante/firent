package com.stirante.firent.skills.boost;

import com.stirante.firent.MagicPlayer;
import com.stirante.firent.effects.ChargeEffect;
import com.stirante.firent.skills.Skill;
import com.stirante.firent.skills.SkillActivationResult;
import com.stirante.firent.skills.SkillFamily;
import com.stirante.firent.skills.SkillTier;
import com.stirante.firent.utils.StringUtils;
import org.bukkit.Material;

import java.util.ArrayList;

public class ChargeSkill extends Skill {

    @Override
    protected SkillActivationResult performSkill(MagicPlayer player) {
        player.addEffect(new ChargeEffect(player));
        player.getPlayer().setVelocity(player.getPlayer().getLocation().getDirection().multiply(5D).setY(0));
        return SkillActivationResult.SUCCESS;
    }

    @Override
    public float getRequiredEnergy() {
        return 60;
    }

    @Override
    public long getCooldownTime() {
        return 2000;
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
        return Material.GOLD_BOOTS;
    }

    @Override
    public String getName() {
        return "Szarża";
    }

    @Override
    public ArrayList<String> getDescription() {
        return StringUtils.asList("Szarżuje do przodu oraz zatrzymuje się na pierwszym napotkanym przeciwniku",
                "Napotkani wrogowie zostaną wyrzuceni w powietrze");
    }
}
