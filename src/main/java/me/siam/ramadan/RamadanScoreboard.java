package me.siam.ramadan;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class RamadanScoreboard {

    private final RamadanEvent plugin;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");

    public RamadanScoreboard(RamadanEvent plugin) {
        this.plugin = plugin;
        startUpdating();
    }

    private void startUpdating() {

        new BukkitRunnable() {
            @Override
            public void run() {

                for (var player : Bukkit.getOnlinePlayers()) {

                    ScoreboardManager manager = Bukkit.getScoreboardManager();
                    Scoreboard board = manager.getNewScoreboard();

                    Objective obj = board.registerNewObjective("ramadan", "dummy", "§a🌙 Ramadan Time");
                    obj.setDisplaySlot(DisplaySlot.SIDEBAR);

                    String time = LocalTime.now().format(formatter);

                    obj.getScore("§fCurrent Time:").setScore(4);
                    obj.getScore("§e" + time).setScore(3);
                    obj.getScore(" ").setScore(2);
                    obj.getScore("§bSeheri: 4:00 AM").setScore(1);
                    obj.getScore("§6Iftar: 6:30 PM").setScore(0);

                    player.setScoreboard(board);
                }
            }
        }.runTaskTimer(plugin, 0, 20); 
    }
}
