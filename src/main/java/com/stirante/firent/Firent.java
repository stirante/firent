package com.stirante.firent;

import com.stirante.firent.entities.HpBoostEntity;
import com.stirante.firent.entities.MagicEntity;
import com.stirante.firent.entities.SpeedBoostEntity;
import com.stirante.firent.listeners.PlayerListener;
import com.stirante.firent.listeners.SkillListener;
import com.stirante.firent.skills.SkillManager;
import com.stirante.firent.utils.JarScanner;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public class Firent extends JavaPlugin implements Runnable {

    private static Firent instance;
    public HashMap<UUID, Class<? extends MagicEntity>> spawnProcess = new HashMap<>();

    public static Firent getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        JarScanner.registerListeners(this, getFile(), "com.stirante.firent.listeners");
        SkillManager.init(this, getFile(), "com.stirante.firent.skills");
        getServer().getScheduler().runTaskTimer(this, this, 1L, 1L);
    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
    }

    @Override
    public void run() {
        MagicWorld.onTick();
        MagicPlayer.onTick();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("resetskill")) {
            if (sender instanceof Player) {
                MagicPlayer.join((Player) sender);
                return true;
            }
            else if (sender instanceof ConsoleCommandSender) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    MagicPlayer.join(player);
                }
                return true;
            }
        }
        else if (label.equalsIgnoreCase("mspawn") && sender.isOp() && sender instanceof Player) {
            Player pl = (Player) sender;
            if (args.length == 0) {
                pl.sendMessage(ChatColor.GOLD + "Usage: /mspawn <hp|boost>");
            }
            else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("hp")) {
                    spawnProcess.put(pl.getUniqueId(), HpBoostEntity.class);
                    pl.sendMessage(ChatColor.GREEN + "Right click block");
                }
                else if (args[0].equalsIgnoreCase("boost")) {
                    spawnProcess.put(pl.getUniqueId(), SpeedBoostEntity.class);
                    pl.sendMessage(ChatColor.GREEN + "Right click block");
                }
            }
        }
        return super.onCommand(sender, command, label, args);
    }
}
