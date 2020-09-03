package smartcraft.auction.Inventory;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import smartcraft.auction.Main;
import smartcraft.hub.InventorySaver.InventorySaver;


import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Storage extends InventoryA implements Listener {


  private Main plugin = Main.getPlugin(Main.class);
  private String title = ChatColor.DARK_GRAY + "Склад";

  public InventoryA adminOpen(Player p, UUID uuid, String category, int page) {
    String owner = Bukkit.getOfflinePlayer(uuid).getName();
    title = title + ChatColor.RED + "(" + owner + ")";
    return super.open(p, uuid, title, category, true, page, plugin, this, this, false);
  }

  public InventoryA open(Player p, String category, int page, boolean assistant) {
    if (assistant) InventorySaver.loadInventoryOnly(p);
    return super.open(p, title, category, true, page, plugin, this, this, assistant);
  }

  @EventHandler
  public void close(InventoryCloseEvent e) {
    if (e.getView().getTitle().startsWith(title)) {
      if (!plugin.inventories.containsKey(e.getPlayer().getUniqueId().toString())) return;
      InventoryA inventory = plugin.inventories.get(e.getPlayer().getUniqueId().toString());
      Navigation.remove(e.getView().getTopInventory(), inventory.getNavigation());
      inventory.saveStorage(e.getView().getTopInventory(), (Player) e.getPlayer());
      List<String> worlds = plugin.getConfig().getStringList("blockedWorld");
      if (inventory.isAssistant())
        InventorySaver.saveInventoryOnly((Player) e.getPlayer());
      if (e.getView().getTitle().startsWith(inventory.getTitle())) {
        inventory.setClosed(true);
      }
    }
  }

  @EventHandler
  public void onDrugItem(InventoryDragEvent e) {
    if (!plugin.inventories.containsKey(e.getWhoClicked().getUniqueId().toString())) return;
    InventoryA inventory = plugin.inventories.get(e.getWhoClicked().getUniqueId().toString());
    if (!e.getInventory().equals(e.getWhoClicked().getInventory())) {
      if ((inventory.isSell() && inventory.isStorage()) || !inventory.isStorage()) {
        e.setCancelled(true);
      }
    }
  }

  @EventHandler
  public void onClick(InventoryClickEvent e) {
    if (plugin.inventories.containsKey(e.getWhoClicked().getUniqueId().toString())) {
      InventoryA inventory = plugin.inventories.get(e.getWhoClicked().getUniqueId().toString());
      if (inventory.isStorage() && !inventory.isClosed()) {
        if (!Objects.equals(e.getClickedInventory(), e.getWhoClicked().getInventory())) {

          ItemStack item = e.getCurrentItem();
          if (item != null) {
            PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
            if (container.has(plugin.idKey, PersistentDataType.INTEGER)) {
              if (!plugin.mysql.exist(container.get(plugin.idKey, PersistentDataType.INTEGER))) {
                e.setCancelled(true);
                e.getWhoClicked().sendMessage(ChatColor.RED + "Предмет был куплен или удален");
                e.getClickedInventory().removeItem(item);
                return;
              }
            }
            if (container.has(plugin.sellKey, PersistentDataType.INTEGER) && !inventory.isSell()) {
              e.setCancelled(true);
              e.getWhoClicked().sendMessage(ChatColor.RED + "Предмет выставлен на продажу, чтобы забрать его сначала снимите с продажи");
              return;
            }
          }

          if (inventory.isSell() && item != null) {
            PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
            if (container.has(plugin.idKey, PersistentDataType.INTEGER)) {
              Sell sell = new Sell();
              sell.open((Player) e.getWhoClicked(), item, inventory);
              e.setCancelled(true);
            }
          }
        }
        if (e.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY) && inventory.isSell())
          e.setCancelled(true);
        if (!Objects.equals(e.getClickedInventory(), e.getWhoClicked().getInventory()) && inventory.isSell()) {
          e.setCancelled(true);
        }
      }
    }
  }
}
