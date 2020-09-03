package smartcraft.auction.Items.CategoryItems;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import smartcraft.auction.Inventory.InventoryA;
import smartcraft.auction.Main;

public class All implements Item {

  private Main plugin = Main.getPlugin(Main.class);
  private NamespacedKey key = new NamespacedKey(plugin, "Category");

  @Override
  public ItemStack give() {
    ItemStack item = new ItemStack(Material.HOPPER);
    ItemMeta itemMeta = item.getItemMeta();
    itemMeta.setDisplayName(ChatColor.GREEN + "Все");
    itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
    itemMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "All");
    item.setItemMeta(itemMeta);
    return item;
  }

  @Override
  public boolean click(InventoryA inventory) {
    inventory.setCategory("All");
    inventory.goBack();
    return true;
  }
}
