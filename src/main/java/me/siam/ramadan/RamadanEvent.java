package me.siam.ramadan;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.LocalDate;
import java.time.LocalTime;

public class RamadanEvent extends JavaPlugin {

    private final LocalTime SEHERI_TIME = LocalTime.of(4, 0);   // 04:00 AM
    private final LocalTime IFTAR_TIME = LocalTime.of(18, 30);  // 06:30 PM

    private LocalDate lastSeheriGiven = null;
    private LocalDate lastIftarGiven = null;

    @Override
    public void onEnable() {
        startRamadanClock();
        getLogger().info("Ramadan Event Plugin Enabled!");
    }

    private void startRamadanClock() {

        new BukkitRunnable() {
            @Override
            public void run() {

                LocalTime now = LocalTime.now();
                LocalDate today = LocalDate.now();

                // Seheri Check
                if (now.getHour() == SEHERI_TIME.getHour()
                        && now.getMinute() == SEHERI_TIME.getMinute()) {

                    if (lastSeheriGiven == null || !lastSeheriGiven.equals(today)) {
                        giveSeheriReward();
                        lastSeheriGiven = today;
                    }
                }

                // Iftar Check
                if (now.getHour() == IFTAR_TIME.getHour()
                        && now.getMinute() == IFTAR_TIME.getMinute()) {

                    if (lastIftarGiven == null || !lastIftarGiven.equals(today)) {
                        giveIftarReward();
                        lastIftarGiven = today;
                    }
                }

            }
        }.runTaskTimer(this, 20, 20); // Check every second
    }

    private void giveSeheriReward() {

        Bukkit.broadcastMessage("§b🌙 Seheri Time! Free food has been given!");

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 16));
            player.getInventory().addItem(new ItemStack(Material.GOLDEN_CARROT, 8));
        }
    }

    private void giveIftarReward() {

        Bukkit.broadcastMessage("§6🌇 Iftar Time! Free food has been given!");

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.getInventory().addItem(new ItemStack(Material.COOKED_CHICKEN, 16));
            player.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 4));
        }
    }
}
