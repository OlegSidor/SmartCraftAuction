package smartcraft.auction.Items.SellItems;

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

public class Back implements Item {

  private Main plugin = Main.getPlugin(Main.class);
  private NamespacedKey key = new NamespacedKey(plugin, "Sell");

  @Override
  public ItemStack give(ItemStack itemStack, Inventory i) {
    ItemStack item = new ItemStack(Material.BARRIER);
    ItemMeta sellItemMeta = itemStack.getItemMeta();
    boolean isUnsell = sellItemMeta.getPersistentDataContainer().has(plugin.sellKey, PersistentDataType.INTEGER);
    ItemMeta itemMeta = item.getItemMeta();
    itemMeta.setDisplayName(ChatColor.RED+"Вернуться назад");
    if (isUnsell) {
      item.setType(Material.RED_STAINED_GLASS_PANE);
      itemMeta.setDisplayName(ChatColor.RED+"Отмена");
    }
    itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    itemMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "Back");
    item.setItemMeta(itemMeta);
    if(isUnsell){
      i.setItem(5, item);
      i.setItem(6, item);
      i.setItem(7, item);
      i.setItem(8, item);
    }else i.setItem(17, item);
    return null;
  }

  @Override
  public boolean click(Player p, InventoryA inventoryA, ItemStack currentItem) {
    inventoryA.goBack();
    return true;
  }
}
