package smartcraft.auction.Items.BuyItems;

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

public class No implements Item {

  private Main plugin = Main.getPlugin(Main.class);

  @Override
  public ItemStack give(ItemStack itemStack, Inventory i) {
    ItemStack item = new ItemStack(Material.RED_STAINED_GLASS_PANE);
    ItemMeta itemMeta = item.getItemMeta();
      itemMeta.setDisplayName(ChatColor.RED+"Отмена");
    itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    itemMeta.getPersistentDataContainer().set(plugin.buyKey, PersistentDataType.STRING, "No");
    item.setItemMeta(itemMeta);
      i.setItem(5, item);
      i.setItem(6, item);
      i.setItem(7, item);
      i.setItem(8, item);
    return item;
  }

  @Override
  public boolean click(Player p, InventoryA inventoryA) {
    inventoryA.goBack();
    return true;
  }
}
