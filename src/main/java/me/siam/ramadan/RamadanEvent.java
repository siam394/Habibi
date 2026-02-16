package me.siam.ramadan;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.LocalDate;
import java.time.LocalTime;

public class RamadanEvent extends JavaPlugin {

    private Economy economy;

    // Ramadan Dates (EDIT HERE)
    private final LocalDate RAMADAN_START = LocalDate.of(2026, 3, 1);
    private final LocalDate RAMADAN_END   = LocalDate.of(2026, 3, 30);

    private final LocalTime SEHERI_TIME = LocalTime.of(4, 0);
    private final LocalTime IFTAR_TIME = LocalTime.of(18, 30);

    private LocalDate lastSeheriGiven = null;
    private LocalDate lastIftarGiven = null;
    private boolean ramadanEndRewardGiven = false;

    @Override
    public void onEnable() {

        if (!setupEconomy()) {
            getLogger().warning("Vault not found! Money reward disabled.");
        }

        startRamadanClock();
        getLogger().info("Ramadan Event Plugin Enabled!");
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) return false;
        RegisteredServiceProvider<Economy> rsp =
                getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) return false;
        economy = rsp.getProvider();
        return economy != null;
    }

    private void startRamadanClock() {

        new BukkitRunnable() {
            @Override
            public void run() {

                LocalDate today = LocalDate.now();
                LocalTime now = LocalTime.now();

                if (today.isBefore(RAMADAN_START) || today.isAfter(RAMADAN_END)) {

                    if (today.isAfter(RAMADAN_END) && !ramadanEndRewardGiven) {
                        giveRamadanEndReward();
                        ramadanEndRewardGiven = true;
                    }
                    return;
                }

                if (now.getHour() == SEHERI_TIME.getHour()
                        && now.getMinute() == SEHERI_TIME.getMinute()) {

                    if (!today.equals(lastSeheriGiven)) {
                        giveSeheriReward();
                        lastSeheriGiven = today;
                    }
                }

                if (now.getHour() == IFTAR_TIME.getHour()
                        && now.getMinute() == IFTAR_TIME.getMinute()) {

                    if (!today.equals(lastIftarGiven)) {
                        giveIftarReward();
                        lastIftarGiven = today;
                    }
                }

            }
        }.runTaskTimer(this, 20, 20);
    }

    private void giveSeheriReward() {
        Bukkit.broadcastMessage("§b🌙 Seheri Time! Free food given!");

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 16));
            player.getInventory().addItem(new ItemStack(Material.GOLDEN_CARROT, 8));
        }
    }

    private void giveIftarReward() {
        Bukkit.broadcastMessage("§6🌇 Iftar Time! Free food given!");

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.getInventory().addItem(new ItemStack(Material.COOKED_CHICKEN, 16));
            player.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 4));
        }
    }

    private void giveRamadanEndReward() {

        Bukkit.broadcastMessage("§a🎉 Ramadan Mubarak! Special Rewards Given!");

        for (Player player : Bukkit.getOnlinePlayers()) {

            // OP Loot
            player.getInventory().addItem(new ItemStack(Material.NETHERITE_INGOT, 5));
            player.getInventory().addItem(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 2));

            // Money Reward
            if (economy != null) {
                economy.depositPlayer(player, 10000);
            }
        }
    }
}
