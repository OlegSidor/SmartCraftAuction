package smartcraft.auction.Items.SellItems;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import smartcraft.auction.Inventory.InventoryA;
import smartcraft.auction.Main;

import java.util.ArrayList;
import java.util.List;

public class Sell implements Item {

  private Main plugin = Main.getPlugin(Main.class);

  @Override
  public ItemStack give(ItemStack item, Inventory i) {
    ItemMeta itemMeta = item.getItemMeta();
    if (itemMeta.getPersistentDataContainer().has(plugin.sellKey, PersistentDataType.INTEGER)) {
      return null;
    }
    List<String> lore = new ArrayList<>();
    lore.add("Цена: 1");
    lore.add("Кликните чтобы подтвердить продажу");
    itemMeta.setLore(lore);
    itemMeta.getPersistentDataContainer().set(plugin.sellKey, PersistentDataType.STRING, "Sell");
    itemMeta.getPersistentDataContainer().set(plugin.priceKey, PersistentDataType.INTEGER, 1);
    item.setItemMeta(itemMeta);
    i.setItem(4, item);
    return item;
  }

  @Override
  public boolean click(Player p, InventoryA inventoryA, ItemStack currentItem) {
    ItemMeta itemMeta = inventoryA.getSellItem().getItemMeta();
    if(itemMeta.getPersistentDataContainer().has(plugin.idKey, PersistentDataType.INTEGER)){
      int id = itemMeta.getPersistentDataContainer().get(plugin.idKey, PersistentDataType.INTEGER);
      int price = itemMeta.getPersistentDataContainer().get(plugin.priceKey, PersistentDataType.INTEGER);
      inventoryA.sellItem(id, price, p);
      inventoryA.goBack();
    }
    return true;
  }
}
