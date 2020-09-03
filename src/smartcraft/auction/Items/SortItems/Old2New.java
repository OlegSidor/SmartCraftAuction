package smartcraft.auction.Items.SortItems;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import smartcraft.auction.Inventory.InventoryA;
import smartcraft.auction.Main;

public class Old2New implements Item {

  private Main plugin = Main.getPlugin(Main.class);
  private NamespacedKey key = new NamespacedKey(plugin, "Sort");

  @Override
  public ItemStack give() {
    ItemStack item = new ItemStack(Material.DAMAGED_ANVIL);
    ItemMeta itemMeta = item.getItemMeta();
    itemMeta.setDisplayName(ChatColor.RED + "От старых к новым");
    itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
    itemMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "Old2New");
    item.setItemMeta(itemMeta);
    return item;
  }

  @Override
  public boolean click(InventoryA inventory) {
    inventory.setSort("Old2New");
    inventory.goBack();
    return true;
  }
}
