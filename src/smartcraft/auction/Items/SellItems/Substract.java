package smartcraft.auction.Items.SellItems;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
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

public class Substract implements Item {
  private Main plugin = Main.getPlugin(Main.class);
  private NamespacedKey key = new NamespacedKey(plugin, "Sell");
  private NamespacedKey Sumkey = new NamespacedKey(plugin, "Sum");

  @Override
  public ItemStack give(ItemStack sellItem, Inventory i) {
    ItemMeta sellItemMeta = sellItem.getItemMeta();
    if (sellItemMeta.getPersistentDataContainer().has(plugin.sellKey, PersistentDataType.INTEGER)) {
      return null;
    }
    ItemStack item = new ItemStack(Material.PLAYER_HEAD);
    SkullMeta itemMeta = (SkullMeta) item.getItemMeta();
    GameProfile profile = new GameProfile(UUID.randomUUID(), null);
    profile.getProperties().put("textures", new Property("textures", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTIzNzZlZWNhYjVmNjE3YjhjNzFjZmNlZTRiZGZjNmNiMWI2NzFkN2JlNDkyMTQ3NGM5Y2I5ZmIxMzQxNDEifX19"));
    plugin.setSullTexture(itemMeta, profile);
    itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    itemMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "Substract");
    itemMeta.setDisplayName(ChatColor.RED + "Опустить цену на 1");
    itemMeta.getPersistentDataContainer().set(Sumkey, PersistentDataType.INTEGER, 1);
    item.setItemMeta(itemMeta);
    i.setItem(5, item);

    itemMeta.setDisplayName(ChatColor.RED + "Опустить цену на 10");
    itemMeta.getPersistentDataContainer().set(Sumkey, PersistentDataType.INTEGER, 10);
    item.setItemMeta(itemMeta);
    i.setItem(6, item);

    itemMeta.setDisplayName(ChatColor.RED + "Опустить цену на 100");
    itemMeta.getPersistentDataContainer().set(Sumkey, PersistentDataType.INTEGER, 100);
    item.setItemMeta(itemMeta);
    i.setItem(7, item);

    itemMeta.setDisplayName(ChatColor.RED + "Опустить цену на 1000");
    itemMeta.getPersistentDataContainer().set(Sumkey, PersistentDataType.INTEGER, 1000);
    item.setItemMeta(itemMeta);
    i.setItem(8, item);
    return null;
  }

  @Override
  public boolean click(Player p, InventoryA inventoryA, ItemStack currentItem) {
    ItemStack item = inventoryA.getSellItem();
    ItemMeta itemMeta = item.getItemMeta();
    ItemMeta currentItemMeta = currentItem.getItemMeta();
    if (itemMeta.getPersistentDataContainer().has(plugin.priceKey, PersistentDataType.INTEGER)) {
      if (currentItemMeta.getPersistentDataContainer().has(Sumkey, PersistentDataType.INTEGER)) {
        int newPrice = itemMeta.getPersistentDataContainer().get(plugin.priceKey, PersistentDataType.INTEGER) -
            currentItemMeta.getPersistentDataContainer().get(Sumkey, PersistentDataType.INTEGER);
        if (newPrice < 1) newPrice = 1;
        itemMeta.getPersistentDataContainer().set(plugin.priceKey, PersistentDataType.INTEGER, newPrice);
        List<String> lore = itemMeta.getLore();
        lore.set(0, "Цена: "+newPrice);
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        p.getOpenInventory().setItem(4, item);
      }
    }
    return true;
  }
}
