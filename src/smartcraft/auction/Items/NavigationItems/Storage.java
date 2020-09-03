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

public class Storage implements Item {

  private Main plugin = Main.getPlugin(Main.class);
  private final int giveSlot = 45;

  @Override
  public void give(Inventory inventory, InventoryA inv) {
    ItemStack Storage = new ItemStack(Material.CHEST);
    ItemMeta StorageMeta = Storage.getItemMeta();
    StorageMeta.setDisplayName(ChatColor.DARK_GRAY + "Склад");
    StorageMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    StorageMeta.getPersistentDataContainer().set(plugin.navigationKey, PersistentDataType.STRING, "Storage");
    Storage.setItemMeta(StorageMeta);
    inventory.setItem(giveSlot, Storage);
  }

  @Override
  public boolean click(Player p, InventoryA inv) {
    smartcraft.auction.Inventory.Storage storage = new smartcraft.auction.Inventory.Storage();
    storage.open(p, "All", 1, inv.isAssistant());
    return true;
  }
}
