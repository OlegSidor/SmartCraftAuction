package smartcraft.auction.Inventory;

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
import smartcraft.auction.Items.BuyItems.*;
import smartcraft.auction.Main;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class Buy implements Listener {

  private Main plugin = Main.getPlugin(Main.class);
  private String title = ChatColor.GREEN + "Купить предмет?";
  private final Class[] items = new Class[]{
      Yes.class, No.class
  };

  public Inventory open(Player p, ItemStack item, InventoryA inv) {
    p.getOpenInventory().close();
    Inventory inventory = plugin.getServer().createInventory(null, 9, title);
    inventory.clear();
    loadContent(items, inventory, item);
    p.openInventory(inventory);
    inv.setBuyItem(item);
    return inventory;
  }

  private void loadContent(Class[] items, Inventory i, ItemStack itemStack) {
    for (Class itemClass : items) {
      try {
        Object item = itemClass.getDeclaredConstructor().newInstance();
        ((Item) item).give(itemStack, i);
      } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
        e.printStackTrace();
      }
    }
  }



  @EventHandler
  public void onClick(InventoryClickEvent e) {
    ItemStack item = e.getCurrentItem();
    if (item != null) {
      if(plugin.inventories.containsKey(e.getWhoClicked().getUniqueId().toString())) {
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta != null) {
          PersistentDataContainer container = itemMeta.getPersistentDataContainer();
          if (container.has(plugin.buyKey, PersistentDataType.STRING)) {
            String className = container.get(plugin.buyKey, PersistentDataType.STRING);
            try {
              Object object = Class.forName("smartcraft.auction.Items.BuyItems." + className).getDeclaredConstructor().newInstance();
              InventoryA inventoryA = plugin.inventories.get(e.getWhoClicked().getUniqueId().toString());
              e.setCancelled(((smartcraft.auction.Items.BuyItems.Item) object).click((Player) e.getWhoClicked(), inventoryA));
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException er) {
              er.printStackTrace();
            }
          }
        }
      }
    }
  }
}
