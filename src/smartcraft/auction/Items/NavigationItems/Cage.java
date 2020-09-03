package smartcraft.auction.Items.NavigationItems;

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

public class Cage implements Item {
  private Main plugin = Main.getPlugin(Main.class);

  @Override
  public void give(Inventory inventory, InventoryA inv) {
    ItemStack item = new ItemStack(Material.IRON_BARS);
    ItemMeta itemMeta = item.getItemMeta();
    itemMeta.setDisplayName(" ");
    itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    itemMeta.getPersistentDataContainer().set(plugin.navigationKey, PersistentDataType.STRING, "Cage");
    item.setItemMeta(itemMeta);
    for (int i = 36; i < 45; i++) {
      inventory.setItem(i, item);
    }
  }

  @Override
  public boolean click(Player p, InventoryA inv) {
    return true;
  }
}
