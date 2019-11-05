package com.stirante.firent.skills.boost;

import com.stirante.firent.MagicPlayer;
import com.stirante.firent.effects.GhostEffect;
import com.stirante.firent.skills.Skill;
import com.stirante.firent.skills.SkillActivationResult;
import com.stirante.firent.skills.SkillFamily;
import com.stirante.firent.skills.SkillTier;
import com.stirante.firent.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class GhostSkill extends Skill {

    @Override
    protected SkillActivationResult performSkill(MagicPlayer player) {
        player.addEffect(new GhostEffect(player));
        Player pl = player.getPlayer();
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.hidePlayer(pl);
        }
        pl.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 80, 3, false, false));
        return SkillActivationResult.SUCCESS;
    }

    @Override
    public float getRequiredEnergy() {
        return 60;
    }

    @Override
    public long getCooldownTime() {
        return 7000;
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
        return Material.POWERED_RAIL;
    }

    @Override
    public String getName() {
        return "Drapieżnik";
    }

    @Override
    public ArrayList<String> getDescription() {
        return StringUtils.asList("Ukrywa i przyspiesza gracza do 3 sekund", "Pierwszy atak będzie wzmocniony oraz " +
                "zakończy działanie umiejętności");
    }
}
