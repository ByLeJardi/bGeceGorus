package com.bGeceGorus;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class bGeceGorus extends JavaPlugin implements Listener {

    private int nightVisionDuration;
    private int nightVisionAmplifier;
    private String nightVisionMessage;

    @Override
    public void onEnable() {
        // Config dosyasını yükle
        saveDefaultConfig();
        reloadConfig();
        FileConfiguration config = getConfig();

        // Config değerlerini al
        nightVisionDuration = config.getInt("night-vision-duration", 999999);
        nightVisionAmplifier = config.getInt("night-vision-amplifier", 0);
        nightVisionMessage = ChatColor.translateAlternateColorCodes('&', config.getString("night-vision-message", "&aSınırsız gece görüşü aktifleştirildi!"));

        // Etkinleştirildiğinde tüm oyunculara gece görüşü ver
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        Bukkit.getOnlinePlayers().forEach(this::giveNightVision);
        getLogger().info("bGeceGorus eklentisi etkinleştirildi!");
    }

    @Override
    public void onDisable() {
        // Devre dışı bırakıldığında gece görüşünü kaldır
        Bukkit.getOnlinePlayers().forEach(player -> player.removePotionEffect(PotionEffectType.NIGHT_VISION));
        getLogger().info("bGeceGorus eklentisi devre dışı bırakıldı.");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        giveNightVision(event.getPlayer());
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> giveNightVision(event.getPlayer()), 1L);
    }

    private void giveNightVision(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, nightVisionDuration * 20, nightVisionAmplifier, false, false));
        player.sendMessage(nightVisionMessage);
    }
}
