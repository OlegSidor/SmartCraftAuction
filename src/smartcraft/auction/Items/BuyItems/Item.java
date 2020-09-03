package smartcraft.auction.Items.BuyItems;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import smartcraft.auction.Inventory.InventoryA;

public interface Item {

  public ItemStack give(ItemStack item, Inventory i);

  boolean click(Player p, InventoryA inventoryA);

}
