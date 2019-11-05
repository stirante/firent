package com.stirante.firent;

import com.stirante.firent.entities.MagicEntity;
import com.stirante.firent.skills.Skill;
import com.stirante.firent.skills.SkillManager;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

public class MagicWorld {

    private static final HashMap<UUID, MagicWorld> worlds = new HashMap<>();
    private final World world;
    private ArrayList<MagicEntity> entities = new ArrayList<>();
    private ArrayList<SoundPlayer> sounds = new ArrayList<>();

    private MagicWorld(World world) {
        this.world = world;
    }

    /**
     * Gets instance of MagicWorld of given world
     *
     * @param w world
     * @return instance of MagicWorld
     */
    public static MagicWorld getWorld(World w) {
        if (!worlds.containsKey(w.getUID())) {
            worlds.put(w.getUID(), new MagicWorld(w));
        }
        return worlds.get(w.getUID());
    }

    /**
     * Method executed on every tick, which updates all worlds
     */
    public static void onTick() {
        for (MagicWorld world : worlds.values()) {
            world.update();
        }
    }

    /**
     * Adds MagicEntity to world
     *
     * @param e instance of MagicEntity to add
     */
    public void addEntity(MagicEntity e) {
        if (e.getLocation().getWorld() != world) {
            throw new IllegalArgumentException("Invalid world!");
        }
        if (!e.isAlive()) {
            throw new IllegalArgumentException("Can't add dead entity!");
        }
        entities.add(e);
    }

    /**
     * Method executed on every tick, which updates this world
     */
    private void update() {
        // update all skills' passives
        for (Skill skill : SkillManager.getSkills()) {
            skill.passiveTick();
        }

        // update all magic entities
        Iterator<MagicEntity> it = entities.iterator();
        while (it.hasNext()) {
            MagicEntity e = it.next();
            if (!e.isAlive()) {
                // remove dead entities
                it.remove();
            }
            else {
                e.update();
            }
        }

        // update all sound players
        Iterator<SoundPlayer> it1 = sounds.iterator();
        while (it1.hasNext()) {
            SoundPlayer s = it1.next();
            if (s.isFinished()) {
                it1.remove();
            }
            else {
                s.update();
            }
        }
    }

    /**
     * Adds sound player to world
     *
     * @param soundPlayer instance of sound player
     */
    public void addSound(SoundPlayer soundPlayer) {
        sounds.add(soundPlayer);
    }
}
