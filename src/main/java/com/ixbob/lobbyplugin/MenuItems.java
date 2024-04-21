package com.ixbob.lobbyplugin;

import com.ixbob.lobbyplugin.handler.config.LangLoader;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;

public enum MenuItems {
    ITEM_OUTDATED(Material.GLASS_BOTTLE, 1, LangLoader.get("item_outdated")),
    ITEM_SIGNED(Material.EXP_BOTTLE, 1, LangLoader.get("item_signed")),
    ITEM_UNSIGNED(Material.POTION, 1, LangLoader.get("item_unsigned")),
    ITEM_TODAY_UNSIGNED(Material.POTION, 1, LangLoader.get("item_today_unsigned"));

    private final Material material;
    private final int amount;
    private final String name;

    MenuItems(Material material, int amount, String name) {
        this.material = material;
        this.amount = amount;
        this.name = name;
    }

    public ItemStack getItemStack() {
        ItemStack itemStack = new ItemStack(material, amount);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(name);
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
