package smartcraft.auction.Items.NavigationItems;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import smartcraft.auction.Inventory.InventoryA;

public interface Item {


  void give(Inventory inventory, InventoryA inv);

  boolean click(Player p, InventoryA inv);


}
