package com.ixbob.lobbyplugin.util;

import com.ixbob.lobbyplugin.MongoDB;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bukkit.Bukkit;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

public class LobbyPlayerDataUtils {
    private static final MongoDB dbPlayerData = new MongoDB("lobbyPlayerData");
    public static final SimpleDateFormat simpleDateFormatAll = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final  SimpleDateFormat simpleDateFormatDayOnly = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static boolean isPlayerDataExist(UUID uuid) {
        return dbPlayerData.isFindByUUIDExist(uuid);
    }

    public static String getDateString(String mode) {
        Date date = new Date();
        switch (mode) {
            case "all": return simpleDateFormatAll.format(date);
            case "date": return simpleDateFormatDayOnly.format(date);
            default: return "mode_error";
        }
    }

    public static void createPlayerData(UUID uuid) {
        DBObject obj = new BasicDBObject("uuid", uuid.toString());
        obj.put("PlayerName", Bukkit.getPlayer(uuid).getName());
        obj.put("FirstJoin", getDateString("all"));
        obj.put("LastSignedYear", 0);
        obj.put("LastSignedMonth", 0);
        obj.put("LastSignedDay", 0);
        obj.put("TodaySigned", false);
        dbPlayerData.insert(obj);
    }

    public static void initPlayerData(UUID uuid) {
        LocalDate currentDate = LocalDate.now();
        int year = currentDate.getYear();
        int month = currentDate.getMonthValue();
        int monthLen = currentDate.lengthOfMonth();
        int today = currentDate.getDayOfMonth();

        DBObject playerData = dbPlayerData.findByUUID(uuid);
        System.out.println(dbPlayerData.getCollectionName() + " 123456");
        int lastSignedYear = (int) playerData.get("LastSignedYear");
        int lastSignedMonth = (int) playerData.get("LastSignedMonth");
        int lastSignedDay = (int) playerData.get("LastSignedDay");
        if (year != lastSignedYear || month != lastSignedMonth) {
            playerData.put("CurrentMonthSignedDays", new ArrayList<Boolean>(Collections.nCopies(monthLen ,false)));
        }
        if (today != lastSignedDay) {
            playerData.put("TodaySigned", false);
        }
        playerData.put("LastLoginYear", year);
        playerData.put("LastLoginMonth", month);
        playerData.put("LastLoginDay", today);
        dbPlayerData.update(playerData);
    }

    public static ArrayList<Boolean> getCurrentMonthSignedDays(UUID uuid) {
        DBObject playerData = dbPlayerData.findByUUID(uuid);
        return (ArrayList<Boolean>) playerData.get("CurrentMonthSignedDays");
    }

    public static void playerSignUpdate(UUID uuid) {
        LocalDate currentDate = LocalDate.now();
        int year = currentDate.getYear();
        int month = currentDate.getMonthValue();
        int monthLen = currentDate.lengthOfMonth();
        int today = currentDate.getDayOfMonth();
        DBObject playerData = dbPlayerData.findByUUID(uuid);
        playerData.put("TodaySigned", true);
        ArrayList<Boolean> signList = (ArrayList<Boolean>) playerData.get("CurrentMonthSignedDays");
        signList.set(today - 1, true);
        playerData.put("LastSignedYear", year);
        playerData.put("LastSignedMonth", month);
        playerData.put("LastSignedDay", today);
        dbPlayerData.update(playerData);
    }

    public static boolean isTodaySigned(UUID uuid) {
        LocalDate currentDate = LocalDate.now();
        int year = currentDate.getYear();
        int month = currentDate.getMonthValue();
        int today = currentDate.getDayOfMonth();
        DBObject playerData = dbPlayerData.findByUUID(uuid);
        return (int) playerData.get("LastSignedYear") == year
                && (int) playerData.get("LastSignedMonth") == month
                && (int) playerData.get("LastSignedDay") == today;
    }
}
