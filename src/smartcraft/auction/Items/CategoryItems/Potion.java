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

public class Potion implements Item {

  private Main plugin = Main.getPlugin(Main.class);
  private NamespacedKey key = new NamespacedKey(plugin, "Category");

  @Override
  public ItemStack give() {
    ItemStack item = new ItemStack(Material.POTION);
    ItemMeta itemMeta = item.getItemMeta();
    itemMeta.setDisplayName(ChatColor.GREEN + "Зелье");
    itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
    itemMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "Potion");
    item.setItemMeta(itemMeta);
    return item;
  }

  @Override
  public boolean click(InventoryA inventory) {
    inventory.setCategory("Potion");
    inventory.goBack();
    return true;
  }
}

