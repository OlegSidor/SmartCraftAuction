package smartcraft.auction.Items.NavigationItems;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import smartcraft.auction.Inventory.InventoryA;
import smartcraft.auction.Main;

import java.lang.reflect.InvocationTargetException;

public class Sort implements Item {
  private final int giveSlot = 51;

  private Main plugin = Main.getPlugin(Main.class);

  @Override
  public void give(Inventory inventory, InventoryA inv) {
    ItemStack item = getItem(inv.getSort());
    assert item != null;
    ItemMeta itemMeta = item.getItemMeta();
    assert itemMeta != null;
    itemMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Сортировка (" + itemMeta.getDisplayName() + ChatColor.LIGHT_PURPLE + ")");
    itemMeta.addEnchant(Enchantment.ARROW_FIRE, 1, false);
    itemMeta.getPersistentDataContainer().set(plugin.navigationKey, PersistentDataType.STRING, "Sort");
    item.setItemMeta(itemMeta);
    inventory.setItem(giveSlot, item);
  }

  @Override
  public boolean click(Player p, InventoryA inv) {
    smartcraft.auction.Inventory.Sort sort = new smartcraft.auction.Inventory.Sort();
    sort.open(p, inv);
    return true;
  }


  private ItemStack getItem(String sort) {
    try {
      Object object = Class.forName("smartcraft.auction.Items.SortItems." + sort).getDeclaredConstructor().newInstance();
      return ((smartcraft.auction.Items.SortItems.Item) object).give();
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException er) {
      er.printStackTrace();
    }
    return null;
  }
}
