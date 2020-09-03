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

public class Back implements Item {

  private Main plugin = Main.getPlugin(Main.class);
  private NamespacedKey key = new NamespacedKey(plugin, "Sort");

  @Override
  public ItemStack give() {
    ItemStack item = new ItemStack(Material.BARRIER);
    ItemMeta itemMeta = item.getItemMeta();
    itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    itemMeta.setDisplayName(ChatColor.RED+"Вернуться назад");
    itemMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "Back");
    item.setItemMeta(itemMeta);
    return item;
  }

  @Override
  public boolean click(InventoryA inventoryA) {
    inventoryA.goBack();
    return true;
  }
}
