package smartcraft.auction.Items.NavigationItems;

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

public class Auction implements Item {
  private Main plugin = Main.getPlugin(Main.class);
  private final int giveSlot = 46;

  @Override
  public void give(Inventory inventory, InventoryA inv) {
    ItemStack item = new ItemStack(Material.GOLD_INGOT);
    ItemMeta itemMeta = item.getItemMeta();
    itemMeta.setDisplayName(ChatColor.GOLD + "Аукцион");
    itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    itemMeta.getPersistentDataContainer().set(plugin.navigationKey, PersistentDataType.STRING, "Auction");
    item.setItemMeta(itemMeta);
    inventory.setItem(giveSlot, item);
  }

  @Override
  public boolean click(Player p, InventoryA inv) {
    smartcraft.auction.Inventory.Auction auction = new smartcraft.auction.Inventory.Auction ();
    auction.open(p, "All", 1, inv.isAssistant());
    return true;
  }

}
