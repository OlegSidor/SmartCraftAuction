package smartcraft.auction.Inventory;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import smartcraft.auction.Main;
import smartcraft.auction.Items.NavigationItems.*;
import smartcraft.auction.Items.NavigationItems.Auction;
import smartcraft.auction.Items.NavigationItems.Category;
import smartcraft.auction.Items.NavigationItems.Sell;
import smartcraft.auction.Items.NavigationItems.Sort;
import smartcraft.auction.Items.NavigationItems.Storage;

import java.lang.reflect.InvocationTargetException;

public class Navigation implements Listener {

  private Main plugin = Main.getPlugin(Main.class);

  private final Class[] items = new Class[]{
      Storage.class, Auction.class, Cage.class, Category.class, Page.class, PrevArrow.class, NextArrow.class, Sort.class, Sell.class, Status.class
  };


  public void create(Inventory inventory, InventoryA inv) {
    for (Class itemClass : items) {
      try {
        Object item = itemClass.getDeclaredConstructor().newInstance();
        ((Item) item).give(inventory, inv);
      } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
        e.printStackTrace();
      }
    }
  }

  public static void remove(Inventory inventory, Listener navigation) {
    if(inventory.getSize() == 54) {
      for (int i = 36; i < 54; i++) {
        if (inventory.getItem(i) != null)
          inventory.removeItem(inventory.getItem(i));
      }
    }
    HandlerList.unregisterAll(navigation);
  }

  public static void clear(Inventory inventory) {
    if(inventory.getSize() == 54) {
      for (int i = 36; i < 54; i++) {
        if (inventory.getItem(i) != null)
          inventory.removeItem(inventory.getItem(i));
      }
    }
  }

  @EventHandler
  public void click(InventoryClickEvent e) {
    ItemStack item = e.getCurrentItem();
    if (item != null) {
      if (plugin.inventories.containsKey(e.getWhoClicked().getUniqueId().toString())) {
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta != null) {
          PersistentDataContainer container = itemMeta.getPersistentDataContainer();
          if (container.has(plugin.navigationKey, PersistentDataType.STRING)) {
            String className = container.get(plugin.navigationKey, PersistentDataType.STRING);
            try {
              Object object = Class.forName("smartcraft.auction.Items.NavigationItems." + className).getDeclaredConstructor().newInstance();

              InventoryA inv = plugin.inventories.get(e.getWhoClicked().getUniqueId().toString());
              e.setCancelled(((Item) object).click((Player) e.getWhoClicked(), inv));
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException er) {
              er.printStackTrace();
            }
          }
        }
      }
    }
  }
}
