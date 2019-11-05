package com.stirante.firent.skills.movement;

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

import java.util.ArrayList;
import java.util.HashMap;

public class RewindSkill extends Skill {

    private HashMap<MagicPlayer, ArrayList<Location>> trail = new HashMap<>();
    private int timer = 0;

    @Override
    protected SkillActivationResult performSkill(final MagicPlayer player) {
        ArrayList<Location> locations = trail.get(player);
        if (locations == null || locations.isEmpty()) {
            return SkillActivationResult.FAILURE;
        }
        player.getPlayer().teleport(locations.get(locations.size() - 1));
        locations.clear();
        player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 1f);
        return SkillActivationResult.SUCCESS;
    }

    @Override
    public float getRequiredEnergy() {
        return 50;
    }

    @Override
    public long getCooldownTime() {
        return 3000;
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
        return Material.WATCH;
    }

    @Override
    public String getName() {
        return "Timeheist";
    }

    @Override
    public void passiveTick() {
        timer++;
        if (timer > 20) {
            timer = 1;
        }
    }

    @Override
    public void passiveTick(MagicPlayer player) {
        if (!trail.containsKey(player)) {
            trail.put(player, new ArrayList<>());
        }
        ArrayList<Location> arr = trail.get(player);
        while (arr.size() > 60) {
            arr.remove(arr.size() - 1);
        }
        arr.add(0, player.getPlayer().getLocation());
        for (Location location : arr) {
//            player.getPlayer().spawnParticle(Particle.REDSTONE, location.clone().add(0, 1, 0), 1, 0, 0, 0, 0);
            ParticleEffect.REDSTONE.display(0, 0, 0, 0, 1, location.clone().add(0, 1, 0), player.getPlayer());
        }
    }

    @Override
    public ArrayList<String> getDescription() {
        return StringUtils.asList("Cofa pozycję gracza o parę sekund");
    }
}
