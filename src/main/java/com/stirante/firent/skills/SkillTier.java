package com.stirante.firent.skills;

public enum SkillTier {
    UTILITY("umiejętność użytkową"),
    ATTACK("umiejętność atakującą"),
    ULTIMATE("superumiejętność");

    private final String name;

    SkillTier(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
