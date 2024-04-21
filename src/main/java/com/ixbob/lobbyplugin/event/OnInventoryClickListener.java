package com.ixbob.lobbyplugin.event;

import com.ixbob.lobbyplugin.handler.config.LangLoader;
import com.ixbob.lobbyplugin.MenuItems;
import com.ixbob.lobbyplugin.util.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.time.LocalDate;
import java.util.Objects;

public class OnInventoryClickListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (Objects.equals(event.getInventory().getName(), LangLoader.get("qiandao_gui_title"))) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            int slot = event.getSlot();
            LocalDate currentDate = LocalDate.now();
            int today = currentDate.getDayOfMonth();
            Inventory inventory = event.getInventory();
            if (slot + 1 == today) {
                inventory.setItem(slot, MenuItems.ITEM_SIGNED.getItemStack());
                Utils.playerSignUpdate(player.getUniqueId());
            }
        }
    }
}
