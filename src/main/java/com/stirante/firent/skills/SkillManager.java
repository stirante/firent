package com.stirante.firent.skills;

import com.stirante.firent.MagicPlayer;
import com.stirante.firent.skills.boost.ChargeSkill;
import com.stirante.firent.skills.boost.GhostSkill;
import com.stirante.firent.skills.boost.StompSkill;
import com.stirante.firent.skills.boost.TornadoSkill;
import com.stirante.firent.skills.bullet.BulletStormSkill;
import com.stirante.firent.skills.bullet.LifeDrainSkill;
import com.stirante.firent.skills.bullet.MissileSkill;
import com.stirante.firent.skills.bullet.TestSkill;
import com.stirante.firent.skills.fire.FireBreathSkill;
import com.stirante.firent.skills.fire.FirePillarSkill;
import com.stirante.firent.skills.movement.BackstabSkill;
import com.stirante.firent.skills.movement.FlashSkill;
import com.stirante.firent.skills.movement.NinjaAttackSkill;
import com.stirante.firent.skills.movement.RewindSkill;
import com.stirante.firent.utils.JarScanner;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class SkillManager {

    public static final String TITLE = ChatColor.RESET + "Wybierz ";
    private static final HashMap<Class<? extends Skill>, Skill> skills = new HashMap<>();

    /**
     * Registers skill
     *
     * @param s skill instance
     */
    public static void registerSkill(Skill s) {
        skills.put(s.getClass(), s);
    }

    /**
     * Gets skill by class
     *
     * @param skillType skill class
     * @return skill instance
     */
    @SuppressWarnings("unchecked")
    public static <T extends Skill> T getSkill(Class<T> skillType) {
        return (T) skills.get(skillType);
    }

    /**
     * Gets collection of all registered skills
     *
     * @return collection of all registered skills
     */
    public static Collection<Skill> getSkills() {
        return skills.values();
    }

    /**
     * Shows to player inventory for choosing skill
     *
     * @param player player, to whom inventory should be shown
     */
    public static void showInventory(MagicPlayer player) {
        MagicPlayer.PlayerPhase phase = player.getPhase();
        String title = TITLE;
        if (phase == MagicPlayer.PlayerPhase.SELECT_1) {
            title += SkillTier.UTILITY.getName();
        }
        if (phase == MagicPlayer.PlayerPhase.SELECT_2) {
            title += SkillTier.UTILITY.getName();
        }
        else if (phase == MagicPlayer.PlayerPhase.SELECT_3) {
            title += SkillTier.ATTACK.getName();
        }
        if (phase == MagicPlayer.PlayerPhase.SELECT_4) {
            title += SkillTier.ULTIMATE.getName();
        }
        Inventory inv = Bukkit.createInventory(player.getPlayer(), InventoryType.CHEST, title);
        for (Skill skill : skills.values()) {
            if (player.getSkills().contains(skill.getClass())) {
                continue;
            }
            if (skill.getTier() == SkillTier.UTILITY && phase == MagicPlayer.PlayerPhase.SELECT_1) {
                inv.addItem(skill.getItem());
            }
            else if (skill.getTier() == SkillTier.UTILITY && phase == MagicPlayer.PlayerPhase.SELECT_2) {
                inv.addItem(skill.getItem());
            }
            else if (skill.getTier() == SkillTier.ATTACK && phase == MagicPlayer.PlayerPhase.SELECT_3) {
                inv.addItem(skill.getItem());
            }
            else if (skill.getTier() == SkillTier.ULTIMATE && phase == MagicPlayer.PlayerPhase.SELECT_4) {
                inv.addItem(skill.getItem());
            }
        }
        player.getPlayer().openInventory(inv);
    }

    /**
     * Gets skill by name
     * @param displayName skill name from an item
     * @return skill instance
     */
    public static Skill getByName(String displayName) {
        for (Skill skill : skills.values()) {
            if (displayName.equals(ChatColor.RESET + skill.getName())) {
                return skill;
            }
        }
        return null;
    }

    /**
     * Registers all listeners
     *
     * @param plugin         instance of plugin
     * @param file           jar file to scan
     * @param scannedPackage package to search
     */
    public static void init(JavaPlugin plugin, File file, String scannedPackage) {
        //get all classes inside specified package and filter only those, which extend Skill class and aren't abstract
        List<Class<?>> classes =
                JarScanner.scan(plugin, file, scannedPackage)
                        .stream()
                        .filter(cls -> Skill.class.isAssignableFrom(cls) && cls != Skill.class &&
                                !Modifier.isAbstract(cls.getModifiers()))
                        .collect(Collectors.toList());
        for (Class<?> aClass : classes) {
            try {
                //create instance of Skill
                Skill skill =
                        (Skill) aClass.newInstance();
                //register Skill
                registerSkill(skill);
                plugin.getLogger().info("Registered skill " + aClass.getSimpleName());
            } catch (InstantiationException | IllegalAccessException e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to instantiate class " + aClass.getSimpleName(), e);
            }
        }
    }
}
