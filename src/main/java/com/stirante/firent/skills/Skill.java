package com.stirante.firent.skills;

import com.stirante.firent.MagicPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public abstract class Skill {

    public final SkillActivationResult activateSkill(MagicPlayer player) {
        SkillActivationResult result = player.checkSkillUse(this);
        if (result == SkillActivationResult.SUCCESS) {
            result = performSkill(player);
            if (result == SkillActivationResult.SUCCESS) {
                player.markSkillUse(this);
                return result;
            }
            else {
                return result;
            }
        }
        return result;
    }

    protected abstract SkillActivationResult performSkill(MagicPlayer player);

    public abstract float getRequiredEnergy();

    public abstract long getCooldownTime();

    public abstract SkillFamily getFamily();

    public abstract SkillTier getTier();

    public abstract Material getMaterial();

    public abstract String getName();

    public abstract ArrayList<String> getDescription();

    public void passiveTick(MagicPlayer player) {

    }

    public void passiveTick() {

    }

    public ItemStack getItem() {
        ItemStack is = new ItemStack(getMaterial(), 1);
        ItemMeta meta = is.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + getName());
        ArrayList<String> desc = getDescription();
        meta.setLore(desc);
        is.setItemMeta(meta);
        return is;
    }

}
