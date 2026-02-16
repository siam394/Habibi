package me.siam.ramadan;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RamadanScoreboard {

    private final JavaPlugin plugin;

    public RamadanScoreboard(JavaPlugin plugin) {
        this.plugin = plugin;
        startUpdating();
    }

    private void startUpdating() {

        new BukkitRunnable() {
            @Override
            public void run() {

                for (Player player : Bukkit.getOnlinePlayers()) {
                    updateBoard(player);
                }

            }
        }.runTaskTimer(plugin, 0, 20); // update every second
    }

    private void updateBoard(Player player) {

        ScoreboardManager manager = Bukkit.getScoreboardManager();
        if (manager == null) return;

        Scoreboard board = manager.getNewScoreboard();
        Objective obj = board.registerNewObjective("ramadan", "dummy", "§6§lRAMADAN EVENT");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        LocalDateTime now = LocalDateTime.now();
        String time = now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String date = now.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        obj.getScore("§fTime: §a" + time).setScore(4);
        obj.getScore("§fDate: §b" + date).setScore(3);
        obj.getScore("§fOnline: §e" + Bukkit.getOnlinePlayers().size()).setScore(2);
        obj.getScore("§7ramadan special").setScore(1);

        player.setScoreboard(board);
    }
}
