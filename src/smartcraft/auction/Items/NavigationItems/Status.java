package smartcraft.auction.Items.NavigationItems;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import smartcraft.auction.Inventory.InventoryA;
import smartcraft.auction.Main;

public class Status implements Item {

  private Main plugin = Main.getPlugin(Main.class);
  private final int giveSlot = 52;

  @Override
  public void give(Inventory inventory, InventoryA inv) {
    ItemStack item = new ItemStack(Material.EMERALD);
    ItemMeta itemMeta = item.getItemMeta();

    Economy economy = plugin.getEconomy();
    double balance = economy.getBalance(inv.getPlayer());
    itemMeta.setDisplayName(ChatColor.DARK_AQUA + "На вашем счету: "+ChatColor.RED+""+(int) balance);
    itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    itemMeta.getPersistentDataContainer().set(plugin.navigationKey, PersistentDataType.STRING, "Status");
    item.setItemMeta(itemMeta);
    inventory.setItem(giveSlot, item);
  }

  @Override
  public boolean click(Player p, InventoryA inv) {
    return true;
  }

}
