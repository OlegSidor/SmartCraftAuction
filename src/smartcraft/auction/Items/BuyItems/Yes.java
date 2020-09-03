package smartcraft.auction.Items.BuyItems;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import smartcraft.auction.Inventory.InventoryA;
import smartcraft.auction.Main;

public class Yes implements Item {

  private Main plugin = Main.getPlugin(Main.class);
  private NamespacedKey key = new NamespacedKey(plugin, "Buy");

  @Override
  public ItemStack give(ItemStack itemStack, Inventory i) {
    ItemStack item = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
    ItemMeta itemMeta = item.getItemMeta();
    itemMeta.setDisplayName(ChatColor.GREEN + "Подтвердить");
    itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    itemMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "Yes");
    item.setItemMeta(itemMeta);
    i.setItem(0, item);
    i.setItem(1, item);
    i.setItem(2, item);
    i.setItem(3, item);
    return item;
  }

  @Override
  public boolean click(Player p, InventoryA inventoryA) {
    ItemMeta itemMeta = inventoryA.getBuyItem().getItemMeta();
    if (itemMeta.getPersistentDataContainer().has(plugin.idKey, PersistentDataType.INTEGER)) {
      int id = itemMeta.getPersistentDataContainer().get(plugin.idKey, PersistentDataType.INTEGER);
      inventoryA.buyItem(id, p);
      inventoryA.goBack();
    }
    return true;
  }
}