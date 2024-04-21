package com.ixbob.lobbyplugin.command;

import com.ixbob.lobbyplugin.handler.config.LangLoader;
import com.ixbob.lobbyplugin.MenuItems;
import com.ixbob.lobbyplugin.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;

import java.time.LocalDate;
import java.util.ArrayList;

public class CommandQiandao implements CommandExecutor {
    private final ItemStack item_outdated;
    private final ItemStack item_signed;
    private final ItemStack item_unsigned;
    private final ItemStack item_today_unsigned;
    private Inventory inventory;
    private LocalDate currentDate;
    private Player player;

    public CommandQiandao() {
        this.item_outdated = MenuItems.ITEM_OUTDATED.getItemStack();
        this.item_signed = MenuItems.ITEM_SIGNED.getItemStack();
        this.item_unsigned = MenuItems.ITEM_UNSIGNED.getItemStack();
        this.item_today_unsigned = MenuItems.ITEM_TODAY_UNSIGNED.getItemStack();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            this.player = (Player) commandSender;
            this.inventory = Bukkit.createInventory(null, 54, LangLoader.get("qiandao_gui_title"));
            this.currentDate = LocalDate.now();
            initItems();
            player.openInventory(inventory);
        }
        return true;
    }

    private void initItems() {
        int monthLen = currentDate.lengthOfMonth();
        int month = currentDate.getMonthValue();
        int today = currentDate.getDayOfMonth();
        ArrayList<Boolean> currentMonthSignedDays = Utils.getCurrentMonthSignedDays(player.getUniqueId());
        for (int i = 0; i <= monthLen - 1; i++) {
            if (i < today - 1) {
                if (!currentMonthSignedDays.get(i)) {
                    inventory.setItem(i, item_outdated);
                    continue;
                }
                inventory.setItem(i, item_signed);
                continue;
            }
            if (i == today - 1) {
                if(Utils.isTodaySigned(player.getUniqueId())) {
                    inventory.setItem(i, item_signed);
                    continue;
                }
                item_today_unsigned.addUnsafeEnchantment(Enchantment.LUCK, 1);
                inventory.setItem(i, item_today_unsigned);
                continue;
            }
            PotionMeta meta = (PotionMeta) item_unsigned.getItemMeta();
            meta.setColor(Color.BLUE);
            item_unsigned.setItemMeta(meta);
            inventory.setItem(i, item_unsigned);
        }
        for (int i = 0; i <= monthLen - 1; i++) {
            ItemStack item = inventory.getItem(i);
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(ChatColor.RESET + "" + ChatColor.GRAY + String.format(LangLoader.get("date_mm_dd"), month, i + 1) + " " + ChatColor.WHITE + itemMeta.getDisplayName());
            item.setItemMeta(itemMeta);
        }
    }
}
