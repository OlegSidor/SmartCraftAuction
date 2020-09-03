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

public class Category implements Item {
  private final int giveSlot = 47;

  private Main plugin = Main.getPlugin(Main.class);

  @Override
  public void give(Inventory inventory, InventoryA inv) {
    ItemStack item = getItem(inv.getCategory());
    assert item != null;
    ItemMeta itemMeta = item.getItemMeta();
    assert itemMeta != null;
    itemMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Категории (" + itemMeta.getDisplayName() + ChatColor.LIGHT_PURPLE + ")");
    itemMeta.addEnchant(Enchantment.ARROW_FIRE, 1, false);
    itemMeta.getPersistentDataContainer().set(plugin.navigationKey, PersistentDataType.STRING, "Category");
    item.setItemMeta(itemMeta);
    inventory.setItem(giveSlot, item);
  }

  @Override
  public boolean click(Player p, InventoryA inv) {
    smartcraft.auction.Inventory.Category category = new smartcraft.auction.Inventory.Category();
    category.open(p);
    return true;
  }


  private ItemStack getItem(String category) {
    try {
      Object object = Class.forName("smartcraft.auction.Items.CategoryItems." + category).getDeclaredConstructor().newInstance();
      return ((smartcraft.auction.Items.CategoryItems.Item) object).give();
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException er) {
      er.printStackTrace();
    }
    return null;
  }
}
