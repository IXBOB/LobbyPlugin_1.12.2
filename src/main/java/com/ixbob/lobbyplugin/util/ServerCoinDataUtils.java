package com.ixbob.lobbyplugin.util;

import com.ixbob.lobbyplugin.MongoDB;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import java.util.UUID;

public class ServerCoinDataUtils {
    public static final MongoDB dbPlayerCoin = new MongoDB("ServerPlayerCoinData");
    public static boolean isPlayerDataExist(UUID uuid) {
        return dbPlayerCoin.isFindByUUIDExist(uuid);
    }
    public static void createPlayerCoinData(UUID uuid) {
        DBObject obj = new BasicDBObject("uuid", uuid.toString());
        obj.put("LobbyCoinAmount", 0);
        dbPlayerCoin.insert(obj);
    }
    public static DBObject getPlayerCoinDataObj(UUID uuid) {
        return dbPlayerCoin.findByUUID(uuid);
    }
    public static int getLobbyCoinAmount(UUID uuid) {
        return (int) getPlayerCoinDataObj(uuid).get("LobbyCoinAmount");
    }
}
