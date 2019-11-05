package com.stirante.firent;

import com.stirante.firent.effects.Effect;
import com.stirante.firent.skills.Skill;
import com.stirante.firent.skills.SkillActivationResult;
import com.stirante.firent.skills.SkillManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class MagicPlayer {

    private static final float MAX_ENERGY = 100f;
    private static final float ENERGY_REGENERATION_PER_TICK = 0.5f;
    private static HashMap<UUID, MagicPlayer> players = new HashMap<>();
    private final Player player;
    private float energy;
    private HashMap<Class<? extends Skill>, Long> lastUsed = new HashMap<>();
    private HashMap<Integer, Class<? extends Skill>> skills = new HashMap<>();
    private ArrayList<Effect> effects = new ArrayList<>();
    private ArrayList<Effect> toRemove = new ArrayList<>();
    private int lazyUpdate = 0;
    private PlayerPhase phase = PlayerPhase.SELECT_1;

    private MagicPlayer(Player player) {
        this.player = player;
    }

    /**
     * Method, which initializes player after they joined
     *
     * @param player player to initialize
     */
    public static void join(Player player) {
        if (!players.containsKey(player.getUniqueId())) {
            players.put(player.getUniqueId(), new MagicPlayer(player));
        }
        players.get(player.getUniqueId()).onJoin();
    }

    /**
     * Method, which updates all players
     */
    public static void onTick() {
        for (MagicPlayer player : players.values()) {
            player.update();
        }
    }

    /**
     * Method, which handles entity damage
     *
     * @param e instance of event
     */
    public static void onEntityDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player pl = ((Player) e.getEntity());
            MagicPlayer player = getPlayer(pl);
            if (player.phase != PlayerPhase.READY) {
                e.setCancelled(true);
            }
            for (Effect effect : player.effects) {
                effect.onDamage(e);
            }
        }
        if (e instanceof EntityDamageByEntityEvent) {
            if (((EntityDamageByEntityEvent) e).getDamager() instanceof Player) {
                Player pl = ((Player) ((EntityDamageByEntityEvent) e).getDamager());
                if (getPlayer(pl).phase != PlayerPhase.READY) {
                    e.setCancelled(true);
                }
                // increase damage from behind
                if (Math.abs(pl.getLocation().getYaw() - e.getEntity().getLocation().getYaw()) < 80) {
                    e.setDamage(e.getDamage() * 1.5);
                }
                for (Effect effect : getPlayer(pl).effects) {
                    effect.onAttack(e);
                }
            }
        }
    }

    /**
     * Method, which handles player movement
     *
     * @param e instance of event
     */
    public static void onPlayerMove(PlayerMoveEvent e) {
        if (e.getFrom().getBlockX() != e.getTo().getBlockX() || e.getFrom().getBlockZ() != e.getFrom().getBlockZ()) {
            for (Effect effect : getPlayer(e.getPlayer()).effects) {
                effect.onMove(e);
            }
        }
    }

    /**
     * Gets instance of MagicPlayer
     *
     * @param player instance of player
     * @return instance of MagicPlayer
     */
    public static MagicPlayer getPlayer(Player player) {
        return players.get(player.getUniqueId());
    }

    /**
     * Method, which handles player cleanup, after they leave
     *
     * @param player instance of player
     */
    public static void quit(Player player) {
        for (Effect effect : getPlayer(player).effects) {
            effect.stopEffect();
        }
        players.remove(player.getUniqueId());
    }

    @Override
    public int hashCode() {
        return player.getUniqueId().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MagicPlayer && ((MagicPlayer) obj).player.getUniqueId().equals(player.getUniqueId());
    }

    private void onJoin() {
        // reset player state
        phase = PlayerPhase.SELECT_1;
        energy = MAX_ENERGY;
        lastUsed.clear();
        effects.clear();
        skills.clear();
        player.getInventory().clear();

        // add player's weapon
        player.getInventory().setItem(8, new ItemStack(Material.IRON_SWORD));

        // add placeholders for player's skills
        ItemStack wool = new ItemStack(Material.WOOL, 1, (short) DyeColor.WHITE.ordinal());
        player.getInventory().setItem(0, wool.clone());
        player.getInventory().setItem(1, wool.clone());
        player.getInventory().setItem(2, wool.clone());
        player.getInventory().setItem(3, wool);

        // add basic armor
        player.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
        player.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
        player.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));

        // set held item to 8th slot (sword)
        player.getInventory().setHeldItemSlot(8);

        // set skills' placeholders to match current skills state
        refreshSkills();
    }

    /**
     * Updates item placeholders for all skills
     */
    private void refreshSkills() {
        for (int i = 0; i < 4; i++) {
            refreshSkill(i);
        }
    }

    /**
     * Gets remaining cooldown for skill
     *
     * @param skill skill instance
     * @return remaining cooldown in milliseconds
     */
    private long getCooldown(Skill skill) {
        // skill is not found, return 0
        if (skill == null) {
            return 0;
        }

        // skill wasn't used, so it's ready
        if (!lastUsed.containsKey(skill.getClass())) {
            return 0;
        }

        // calculate time since last use and subtract it's cooldown to get remaining cooldown
        long result = skill.getCooldownTime() - (System.currentTimeMillis() - lastUsed.get(skill.getClass()));

        // don't return negative time
        return result < 0 ? 0 : result;
    }

    /**
     * Sets skill item placeholder to appropriate state
     *
     * @param s skill index
     */
    private void refreshSkill(int s) {
        Skill skill = getSkill(s);
        ItemStack is = player.getInventory().getItem(s);

        // item is not found
        if (is == null) {
            return;
        }

        // setting wool color depending on skill state
        long cd = getCooldown(skill);
        // if skill is not set, make wool color gray
        if (skill == null) {
            is.setDurability((short) DyeColor.GRAY.ordinal());
        }
        // skill is on cooldown, make wool color red
        else if (cd > 0) {
            is.setDurability((short) DyeColor.RED.ordinal());
        }
        // player doesn't have required energy to use skill, make wool color light blue
        else if (getEnergy() < skill.getRequiredEnergy()) {
            is.setDurability((short) DyeColor.LIGHT_BLUE.ordinal());
        }
        // player can use skill, make wool color lime
        else {
            is.setDurability((short) DyeColor.LIME.ordinal());
        }

        // if there is cooldown, show remaining seconds as item count
        int amount = Math.round((float) cd / 1000f);
        if (amount < 1) {
            amount = 1;
        }
        is.setAmount(amount);

        if (skill != null) {
            ItemMeta meta = is.getItemMeta();
            // set item name to skill name
            meta.setDisplayName(ChatColor.RESET + skill.getName());
            // set item lore to skill description
            meta.setLore(skill.getDescription());
            is.setItemMeta(meta);
        }
    }

    /**
     * Updates magic player
     */
    private void update() {
        //regenerate energy
        if (energy < MAX_ENERGY) {
            energy += ENERGY_REGENERATION_PER_TICK;
            if (energy > MAX_ENERGY) {
                energy = MAX_ENERGY;
            }
        }

        //set exp bar to energy
        player.setExp(getEnergy() / (MAX_ENERGY + 0.1f));
        player.setLevel(0);

        //update all player effects
        for (Effect effect : effects) {
            effect.onTick();
        }
        effects.removeAll(toRemove);
        toRemove.clear();
        refreshSkills();

        // once a second restore full hunger bar
        lazyUpdate++;
        if (lazyUpdate >= 20) {
            lazyUpdate = 0;
            player.setFoodLevel(20);
            player.setSaturation(100);
        }

        // if all skills are chosen, update skill passive on every tick
        if (phase == PlayerPhase.READY) {
            for (Class<? extends Skill> skill : skills.values()) {
                SkillManager.getSkill(skill).passiveTick(this);
            }
        }
    }

    /**
     * Gets current energy level
     *
     * @return energy level
     */
    public float getEnergy() {
        return energy;
    }

    /**
     * Marks skill as used
     *
     * @param s skill to mark
     */
    public void markSkillUse(Skill s) {
        energy -= s.getRequiredEnergy();
        lastUsed.put(s.getClass(), System.currentTimeMillis());
    }

    /**
     * Gets skill at given index
     *
     * @param skill skill index
     * @return skill instance
     */
    public Skill getSkill(int skill) {
        return SkillManager.getSkill(skills.get(skill));
    }

    /**
     * Checks requirements to use skill. If it fails, it plays appropriate sound for player
     *
     * @param s skill instance to use
     * @return skill activation result
     */
    public SkillActivationResult checkSkillUse(Skill s) {
        // check cooldown
        if (!lastUsed.containsKey(s.getClass()) ||
                System.currentTimeMillis() - lastUsed.get(s.getClass()) > s.getCooldownTime()) {
            // check required energy
            if (energy >= s.getRequiredEnergy()) {
                return SkillActivationResult.SUCCESS;
            }
            else {
                player.playSound(player.getLocation(), Sound.WOOD_CLICK, 1, 1);
                return SkillActivationResult.NO_ENERGY;
            }
        }
        else {
            player.playSound(player.getLocation(), Sound.WOOD_CLICK, 1, 1);
            return SkillActivationResult.ON_COOLDOWN;
        }
    }

    /**
     * Gets original player this MagicPlayer instance is based on
     *
     * @return player instance
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Adds effect to player
     *
     * @param e effect instance
     */
    public void addEffect(Effect e) {
        effects.add(e);
    }


    /**
     * Removes effect from player
     *
     * @param e effect instance
     */
    public void removeEffect(Effect e) {
        if (effects.contains(e)) {
            toRemove.add(e);
        }
    }

    /**
     * Gets skill choosing phase
     *
     * @return skill choosing phase
     */
    public PlayerPhase getPhase() {
        return phase;
    }

    /**
     * Handles inventory click when choosing skills
     *
     * @param inv inventory instance
     * @param is  clicked item stack
     */
    public void onInventoryClick(Inventory inv, ItemStack is) {
        if (inv == null || is == null || !is.hasItemMeta()) {
            return;
        }
        if (phase != PlayerPhase.READY) {
            if (inv.getName().startsWith(SkillManager.TITLE)) {
                // ordinal value of phase enum is equal to skill index
                int sn = phase.ordinal();
                // get chosen skill from clicked item
                Skill skill = SkillManager.getByName(is.getItemMeta().getDisplayName());
                if (skill != null) {
                    // set chosen skill
                    skills.put(sn, skill.getClass());
                    // set next phase
                    phase = PlayerPhase.values()[++sn];

                    // close inventory
                    player.closeInventory();
                    // if not all skills are chosen, reopen inventory with another set of skills
                    if (phase != PlayerPhase.READY) {
                        SkillManager.showInventory(this);
                    }
                }
            }
        }
    }

    /**
     * Shows skill choosing gui
     */
    public void chooseSkills() {
        if (phase != PlayerPhase.READY) {
            SkillManager.showInventory(this);
        }
    }

    /**
     * Gets all player's skills
     *
     * @return player's skills
     */
    public Collection<Class<? extends Skill>> getSkills() {
        return skills.values();
    }

    /**
     * Returns if player has given effect
     *
     * @param effect effect instance
     * @return has effect
     */
    public boolean hasEffect(Effect effect) {
        return effects.contains(effect);
    }

    /**
     * Enum with phases for choosing skills. All phases have to be in this exact order to work.
     */
    public enum PlayerPhase {
        SELECT_1,
        SELECT_2,
        SELECT_3,
        SELECT_4,
        READY
    }

}
