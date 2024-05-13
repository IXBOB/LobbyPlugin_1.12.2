package com.ixbob.lobbyplugin.event;

import com.ixbob.lobbyplugin.handler.config.LangLoader;
import com.ixbob.lobbyplugin.util.LobbyPlayerDataUtils;
import com.ixbob.lobbyplugin.util.ServerCoinDataUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.UUID;

public class OnPlayerJoinListener implements Listener {
    public final Plugin plugin;
    public OnPlayerJoinListener(Plugin plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        player.teleport(new Location(Bukkit.getWorlds().get(0), 56, 33, 59));
        player.sendTitle(LangLoader.get("join_welcome_title"), LangLoader.get("join_welcome_subtitle"), 10, 120, 10);

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            if (!LobbyPlayerDataUtils.isPlayerDataExist(uuid)) {
                LobbyPlayerDataUtils.createPlayerData(uuid);
            }
            LobbyPlayerDataUtils.initPlayerData(uuid);
            if(!ServerCoinDataUtils.isPlayerDataExist(uuid)) {
                ServerCoinDataUtils.createPlayerCoinData(uuid);
            }
            Bukkit.getServer().getScheduler().runTask(plugin, () -> {
                player.setMetadata("LobbyCoinAmount",   //get data from database
                        new FixedMetadataValue(plugin, ServerCoinDataUtils.getLobbyCoinAmount(uuid)));
                initScBoard();
            });
        });
    }

    public void initScBoard() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Scoreboard scoreboard = player.getServer().getScoreboardManager().getNewScoreboard();
            Objective scoreboardObj = scoreboard.registerNewObjective("lobby_pl_info", "dummy");
            scoreboardObj.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + LangLoader.get("server_name"));
            scoreboardObj.setDisplaySlot(DisplaySlot.SIDEBAR);

            scoreboardObj.getScore(LangLoader.get("lobby_sc_line1")).setScore(0);
            scoreboardObj.getScore(String.format(LangLoader.get("lobby_sc_line2"), player.getMetadata("LobbyCoinAmount").get(0).asInt())).setScore(-1);
            scoreboardObj.getScore(LangLoader.get("lobby_sc_line3")).setScore(-2);
            scoreboardObj.getScore(LangLoader.get("lobby_sc_line4")).setScore(-3);

            player.setScoreboard(scoreboard);
        }
    }
}
