package com.stirante.firent.skills;


import org.bukkit.Material;

public enum SkillFamily {
    MOVEMENT(Material.GOLD_BOOTS),
    FIRE(Material.FIRE),
    BOOST(Material.DIAMOND_BOOTS),
    BULLET(Material.ARROW);

    private final Material material;

    SkillFamily(Material material) {
        this.material = material;
    }

    public Material getMaterial() {
        return material;
    }
}
