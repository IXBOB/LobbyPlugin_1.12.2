package com.ixbob.lobbyplugin.util;

import com.ixbob.lobbyplugin.Main;
import com.ixbob.lobbyplugin.MongoDB;
import com.ixbob.lobbyplugin.handler.config.LangLoader;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.UUID;

public class ServerCoinDataUtils {
    public static final MongoDB dbPlayerCoin = new MongoDB("ServerPlayerCoinData");
    public static boolean isPlayerDataExist(UUID uuid) {
        return dbPlayerCoin.isFindByUUIDExist(uuid);
    }
    public static void createPlayerCoinData(UUID uuid) {
        DBObject obj = new BasicDBObject("uuid", uuid.toString());
        obj.put("PlayerName", Bukkit.getPlayer(uuid).getName());
        obj.put("LobbyCoinAmount", 0);
        dbPlayerCoin.insert(obj);
    }
    public static DBObject getPlayerCoinDataObj(UUID uuid) {
        return dbPlayerCoin.findByUUID(uuid);
    }
    public static int getLobbyCoinAmount(UUID uuid) {
        return (int) getPlayerCoinDataObj(uuid).get("LobbyCoinAmount");
    }
    public static void addLobbyCoin(UUID uuid, int addAmount) {
        DBObject found = dbPlayerCoin.findByUUID(uuid);
        int newAmount = ((int)found.get("LobbyCoinAmount")) + addAmount;
        found.put("LobbyCoinAmount", newAmount );
        dbPlayerCoin.update(found);
        Player player = Bukkit.getPlayer(uuid);
        player.setMetadata("LobbyCoinAmount", new FixedMetadataValue(Main.plugin, newAmount));
    }
    public static void updateLobbyCoinScoreboard(UUID uuid, int addedCoinAmount) {
        Player player = Bukkit.getPlayer(uuid);
        Scoreboard scoreboard = player.getScoreboard();
        Objective scoreboardObj = scoreboard.getObjective("lobby_pl_info");
        String textBefore = String.format(LangLoader.get("lobby_sc_line2"), player.getMetadata("LobbyCoinAmount").get(0).asInt() - addedCoinAmount);
        String textAfter = String.format(LangLoader.get("lobby_sc_line2"), player.getMetadata("LobbyCoinAmount").get(0).asInt());
        scoreboardObj.getScoreboard().resetScores(textBefore);
        scoreboardObj.getScore(textAfter).setScore(-1);
        player.setScoreboard(scoreboardObj.getScoreboard());

    }
}
