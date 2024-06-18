package com.ixbob.lobbyplugin.event;

import com.ixbob.lobbyplugin.handler.config.LangLoader;
import com.ixbob.lobbyplugin.MenuItems;
import com.ixbob.lobbyplugin.util.LobbyPlayerDataUtils;
import com.ixbob.lobbyplugin.util.ServerCoinDataUtils;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class OnInventoryClickListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (Objects.equals(event.getView().getTitle(), LangLoader.get("qiandao_gui_title"))) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            int slot = event.getSlot();
            LocalDate currentDate = LocalDate.now();
            int today = currentDate.getDayOfMonth();
            Inventory inventory = event.getInventory();
            if (slot + 1 == today && inventory.getItem(slot).getType() != MenuItems.ITEM_SIGNED.getItemStack().getType()) {
                inventory.setItem(slot, MenuItems.ITEM_SIGNED.getItemStack());
                UUID uuid = player.getUniqueId();
                LobbyPlayerDataUtils.playerSignUpdate(uuid);
                final int addCoin = 200;
                ServerCoinDataUtils.addLobbyCoin(uuid, addCoin);
                ServerCoinDataUtils.updateLobbyCoinScoreboard(uuid, addCoin);
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                player.sendMessage(String.format(LangLoader.get("sign_success_message"), addCoin));
                return;
            }
        }
        if (!event.getWhoClicked().isOp()) {
            event.setCancelled(true);
        }
    }
}
