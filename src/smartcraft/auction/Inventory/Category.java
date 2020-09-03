package smartcraft.auction.Inventory;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import smartcraft.auction.Items.CategoryItems.*;
import smartcraft.auction.Main;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class Category implements Listener {

  private Main plugin = Main.getPlugin(Main.class);
  private String title = ChatColor.LIGHT_PURPLE + "Категории";
  private NamespacedKey key = new NamespacedKey(plugin, "Category");
  private final Class[] items = new Class[]{
      All.class, Weapon.class, Armor.class, Block.class, Tool.class, Potion.class, Back.class
  };

  public Inventory open(Player p) { p.getOpenInventory().close();
    Inventory inventory = plugin.getServer().createInventory(null, 9, title);
    inventory.clear();
    loadContent(items, inventory);
    p.openInventory(inventory);
    return inventory;
  }

  public void loadContent(Class[] items, Inventory i) {
    for (Class itemClass : items) {
      try {
        Object item = itemClass.getDeclaredConstructor().newInstance();
        i.addItem(((Item) item).give());
      } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
        e.printStackTrace();
      }
    }
  }

  @EventHandler
  public void onClick(InventoryClickEvent e) {
    ItemStack item = e.getCurrentItem();
    if (item != null) {
      if (plugin.inventories.containsKey(e.getWhoClicked().getUniqueId().toString())) {
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta != null) {
          PersistentDataContainer container = itemMeta.getPersistentDataContainer();
          if (container.has(key, PersistentDataType.STRING)) {
            String className = container.get(key, PersistentDataType.STRING);
            try {
              Object object = Class.forName("smartcraft.auction.Items.CategoryItems." + className).getDeclaredConstructor().newInstance();
              InventoryA inventory = plugin.inventories.get(e.getWhoClicked().getUniqueId().toString());
              e.setCancelled(((smartcraft.auction.Items.CategoryItems.Item) object).click(inventory));
              inventory.setPage(1);
              inventory.refresh();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException er) {
              er.printStackTrace();
            }
          }
        }
      }
    }
  }
}
