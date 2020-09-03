package smartcraft.auction.Inventory;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
import smartcraft.auction.Items.SellItems.*;
import smartcraft.auction.Main;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class Sell implements Listener {

  private Main plugin = Main.getPlugin(Main.class);
  private String title = ChatColor.GREEN + "Продажа предмета";
  private final Class[] items = new Class[]{
      Add.class, smartcraft.auction.Items.SellItems.Sell.class, Substract.class, Back.class, Unsell.class
  };

  public Inventory open(Player p, ItemStack item, InventoryA inv) {
    inv.setSellItem(item);
    p.getOpenInventory().close();
    int size = 18;
    ItemMeta itemMeta = item.getItemMeta();
    if (itemMeta.getPersistentDataContainer().has(plugin.sellKey, PersistentDataType.INTEGER)) {
      title = ChatColor.RED + "Снять с продажи?";
      size = 9;
    }
    Inventory inventory = plugin.getServer().createInventory(null, size, title);
    inventory.clear();
    loadContent(items, inventory, item, inv);
    p.openInventory(inventory);
    return inventory;
  }

  private void loadContent(Class[] items, Inventory i, ItemStack itemStack, InventoryA inventoryA) {
    for (Class itemClass : items) {
      try {
        Object item = itemClass.getDeclaredConstructor().newInstance();
        ItemStack button = ((Item) item).give(itemStack, i);
        if (button != null) inventoryA.setSellItem(button);
      } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
        e.printStackTrace();
      }
    }
  }


  @EventHandler
  public void onDrugItem(InventoryDragEvent e) {
    if (!e.getInventory().equals(e.getWhoClicked().getInventory())) {
      e.setCancelled(true);
    }
  }

  @EventHandler
  public void onClick(InventoryClickEvent e) {
    ItemStack item = e.getCurrentItem();
    if (item != null) {
      ItemMeta itemMeta = item.getItemMeta();
      if (itemMeta != null) {
        if (plugin.inventories.containsKey(e.getWhoClicked().getUniqueId().toString())) {
          PersistentDataContainer container = itemMeta.getPersistentDataContainer();
          if (container.has(plugin.sellKey, PersistentDataType.STRING)) {
            String className = container.get(plugin.sellKey, PersistentDataType.STRING);
            try {
              Object object = Class.forName("smartcraft.auction.Items.SellItems." + className).getDeclaredConstructor().newInstance();
              InventoryA inventoryA = plugin.inventories.get(e.getWhoClicked().getUniqueId().toString());
              e.setCancelled(((smartcraft.auction.Items.SellItems.Item) object).click((Player) e.getWhoClicked(), inventoryA, e.getCurrentItem()));
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException er) {
              er.printStackTrace();
            }
          }
        }
      }
    }
  }
}
