package smartcraft.auction.Items.SellItems;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import smartcraft.auction.Inventory.InventoryA;
import smartcraft.auction.Main;

import java.util.List;
import java.util.UUID;

public class Add implements Item {

  private Main plugin = Main.getPlugin(Main.class);

  @Override
  public ItemStack give(ItemStack sellItem, Inventory i) {
    ItemMeta sellItemMeta = sellItem.getItemMeta();
    if (sellItemMeta.getPersistentDataContainer().has(plugin.sellKey, PersistentDataType.INTEGER)) {
      return null;
    }
    ItemStack item = new ItemStack(Material.PLAYER_HEAD);
    SkullMeta itemMeta = (SkullMeta) item.getItemMeta();
    GameProfile profile = new GameProfile(UUID.randomUUID(), null);
    profile.getProperties().put("textures", new Property("textures", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2Q2OTVkMzM1ZTZiZThjYjJhMzRlMDVlMThlYTJkMTJjM2IxN2I4MTY2YmE2MmQ2OTgyYTY0M2RmNzFmZmFjNSJ9fX0="));
    plugin.setSullTexture(itemMeta, profile);
    itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    itemMeta.getPersistentDataContainer().set(plugin.sellKey, PersistentDataType.STRING, "Add");
    itemMeta.setDisplayName(ChatColor.GREEN + "Поднять цену на 1000");
    itemMeta.getPersistentDataContainer().set(plugin.sumKey, PersistentDataType.INTEGER, 1000);
    item.setItemMeta(itemMeta);
    i.setItem(0, item);

    itemMeta.setDisplayName(ChatColor.GREEN + "Поднять цену на 100");
    itemMeta.getPersistentDataContainer().set(plugin.sumKey, PersistentDataType.INTEGER, 100);
    item.setItemMeta(itemMeta);
    i.setItem(1, item);

    itemMeta.setDisplayName(ChatColor.GREEN + "Поднять цену на 10");
    itemMeta.getPersistentDataContainer().set(plugin.sumKey, PersistentDataType.INTEGER, 10);
    item.setItemMeta(itemMeta);
    i.setItem(2, item);

    itemMeta.setDisplayName(ChatColor.GREEN + "Поднять цену на 1");
    itemMeta.getPersistentDataContainer().set(plugin.sumKey, PersistentDataType.INTEGER, 1);
    item.setItemMeta(itemMeta);
    i.setItem(3, item);
    return null;
  }

  @Override
  public boolean click(Player p, InventoryA inventoryA, ItemStack currentItem) {
    ItemStack item = inventoryA.getSellItem();
    ItemMeta itemMeta = item.getItemMeta();
    ItemMeta currentItemMeta = currentItem.getItemMeta();
    if (itemMeta.getPersistentDataContainer().has(plugin.priceKey, PersistentDataType.INTEGER)) {
      if (currentItemMeta.getPersistentDataContainer().has(plugin.sumKey, PersistentDataType.INTEGER)) {
        int newPrice = itemMeta.getPersistentDataContainer().get(plugin.priceKey, PersistentDataType.INTEGER) +
            currentItemMeta.getPersistentDataContainer().get(plugin.sumKey, PersistentDataType.INTEGER);
        if (newPrice > 9999999) newPrice = 9999999;
        itemMeta.getPersistentDataContainer().set(plugin.priceKey, PersistentDataType.INTEGER, newPrice);
        List<String> lore = itemMeta.getLore();
        lore.set(0, "Цена: " + newPrice);
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        inventoryA.setSellItem(item);
        p.getOpenInventory().setItem(4, item);
      }
    }
    return true;
  }
}
