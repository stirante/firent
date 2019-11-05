package com.stirante.firent.entities;

import com.stirante.firent.SoundPlayer;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class SpeedBoostEntity extends MagicEntity {

    private static HashMap<UUID, Long> miniCooldowns = new HashMap<>();
    private Vector direction;


    public SpeedBoostEntity(Location location) {
        super(location);

        byte data = location.getBlock().getData();
        BlockFace face = BlockFace.SELF;
        switch (data) {
            case 0:
                face = BlockFace.NORTH;
                break;
            case 1:
                face = BlockFace.EAST;
                break;
            case 2:
                face = BlockFace.SOUTH;
                break;
            case 3:
                face = BlockFace.WEST;
                break;
        }
        direction = new Vector(face.getModX(), face.getModY(), face.getModZ()).multiply(2);
    }

    @Override
    public void update() {
        World world = getLocation().getWorld();
        Collection<Entity> nearbyEntities = world.getNearbyEntities(getCenterLocation(), 0.5, 2, 0.5);
        for (Entity entity : nearbyEntities) {
            if (entity instanceof Player) {
                Player pl = (Player) entity;
                if (miniCooldowns.containsKey(pl.getUniqueId()) &&
                        System.currentTimeMillis() - miniCooldowns.get(pl.getUniqueId()) < 1000) {
                    continue;
                }
                pl.setVelocity(entity.getVelocity().add(direction));
                SoundPlayer.createNew(getLocation(), Sound.NOTE_PLING)
                        .addNote(SoundPlayer.Tone.C2)
                        .addNote(SoundPlayer.Tone.C2)
                        .addNote(SoundPlayer.Tone.C2)
                        .play();
                miniCooldowns.put(pl.getUniqueId(), System.currentTimeMillis());
                return;
            }
        }
    }
}
