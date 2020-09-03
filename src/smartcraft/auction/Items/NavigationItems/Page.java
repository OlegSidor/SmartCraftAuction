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

import java.util.ArrayList;
import java.util.List;

public class Page implements Item {

  private Main plugin = Main.getPlugin(Main.class);
  private final int giveSlot = 49;


  @Override
  public void give(Inventory inventory, InventoryA inv) {
    ItemStack item = new ItemStack(Material.MAP);
    ItemMeta itemMeta = item.getItemMeta();
    itemMeta.setDisplayName(ChatColor.GREEN + "" + inv.getPage() + " Строница");
    itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    List<String> lore = new ArrayList<>();
    lore.add("Нажмите чтобы обновить предметы");
    itemMeta.setLore(lore);
    itemMeta.getPersistentDataContainer().set(plugin.navigationKey, PersistentDataType.STRING, "Page");
    item.setItemMeta(itemMeta);
    inventory.setItem(giveSlot, item);
  }

  @Override
  public boolean click(Player p, InventoryA inv) {
    if (inv.isStorage()) {
      inv.SaveAndRefresh();
    } else inv.refresh();
    return true;
  }
}
