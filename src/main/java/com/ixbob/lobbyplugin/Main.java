package com.ixbob.lobbyplugin;

import com.ixbob.lobbyplugin.command.CommandQiandao;
import com.ixbob.lobbyplugin.event.OnInventoryClickListener;
import com.ixbob.lobbyplugin.event.OnPlayerJoinListener;
import com.ixbob.lobbyplugin.handler.config.LangLoader;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class Main extends JavaPlugin {
    public static Plugin plugin;
    @Override
    public void onEnable() {
        plugin = this;
        MongoDB mongoDB = new MongoDB();
        mongoDB.connect("127.0.0.1", 27017, this);

        LangLoader.init(this);

        this.getCommand("qiandao").setExecutor(new CommandQiandao());

        Listener onInventoryClickListener = new OnInventoryClickListener();
        getServer().getPluginManager().registerEvents(onInventoryClickListener, this);

        Listener onPlayerJoinListener = new OnPlayerJoinListener(this);
        getServer().getPluginManager().registerEvents(onPlayerJoinListener, this);

    }

}
