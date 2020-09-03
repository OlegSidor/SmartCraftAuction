package smartcraft.auction.Items.SortItems;

import org.bukkit.inventory.ItemStack;
import smartcraft.auction.Inventory.InventoryA;

public interface Item {

  public ItemStack give();
  public boolean click(InventoryA inventory);
}
