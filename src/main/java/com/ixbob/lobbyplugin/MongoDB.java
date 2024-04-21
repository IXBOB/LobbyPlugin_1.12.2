package com.ixbob.lobbyplugin;

import com.mongodb.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.net.UnknownHostException;
import java.util.UUID;
import java.util.logging.Level;

public class MongoDB {
    private DBCollection collection;
    private static DB mcserverdb;
    private static MongoClient client;
    public MongoDB(String collectionName) {
        System.out.println("init");
        collection = mcserverdb.getCollection(collectionName);
    }
    public MongoDB() {}
    public void setCollection(String collectionName) {
        collection = mcserverdb.getCollection(collectionName);
    }
    public void connect(String ip, int port, Plugin plugin) {
        try {
            client = new MongoClient(ip, port);
            plugin.getLogger().log(Level.INFO, "database connected!");
            mcserverdb = client.getDB("mcserver");
        } catch (UnknownHostException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not connect to database!", e);
        }
    }
    public long getCollectionSize() {
        return collection.getCount();
    }
    public String getCollectionName() {
        return collection.getName();
    }
    public void insert(DBObject object) {
        collection.insert(object);
    }

    public void update(DBObject object) {
        BasicDBObject query = new BasicDBObject("uuid", object.get("uuid"));
        collection.update(query, object);
    }

//    public void insertTest() {
//        DBObject obj = new BasicDBObject("test_key", "123456");
//        obj.put("test_key2", "555555");
//        collection.insert(obj);
//    }

    public DBObject findByUUID (UUID uuid) {
        DBObject r = new BasicDBObject("uuid", uuid.toString());
        DBObject found = collection.findOne(r);
        if (found == null) {
            Bukkit.getLogger().log(Level.SEVERE, "found nothing. Error uuid parameter.");
            new NullPointerException().printStackTrace();
        }
        return found;
    }

    public boolean isFindByUUIDExist (UUID uuid) {
        DBObject r = new BasicDBObject("uuid", uuid.toString());
        DBObject found = collection.findOne(r);
        return found != null;
    }
}
