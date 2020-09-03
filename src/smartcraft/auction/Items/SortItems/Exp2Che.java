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

public class Exp2Che implements Item {

  private Main plugin = Main.getPlugin(Main.class);
  private NamespacedKey key = new NamespacedKey(plugin, "Sort");

  @Override
  public ItemStack give() {
    ItemStack item = new ItemStack(Material.DIAMOND);
    ItemMeta itemMeta = item.getItemMeta();
    itemMeta.setDisplayName(ChatColor.RED + "От дорогих к дешевым");
    itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
    itemMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "Exp2Che");
    item.setItemMeta(itemMeta);
    return item;
  }

  @Override
  public boolean click(InventoryA inventory) {
    inventory.setSort("Exp2Che");
    inventory.goBack();
    return true;
  }
}
