package smartcraft.auction.Inventory;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import smartcraft.auction.Main;

import java.util.Objects;

public class Auction extends InventoryA implements Listener {

  private Main plugin = Main.getPlugin(Main.class);
  private String title = ChatColor.GOLD + "Ауцион";

  public InventoryA open(Player p, String category, int page, boolean assistant) {
    return super.open(p, title, category, false, page, plugin, this, this, assistant);
  }

  @EventHandler
  public void close(InventoryCloseEvent e) {
    if (!plugin.inventories.containsKey(e.getPlayer().getUniqueId().toString())) return;
    InventoryA inventory = plugin.inventories.get(e.getPlayer().getUniqueId().toString());
    if (!inventory.isStorage() && !inventory.isClosed()) {
      Navigation.remove(e.getInventory(), inventory.getNavigation());
      e.getInventory().clear();
      if (e.getView().getTitle().equalsIgnoreCase(inventory.getTitle())) {
        inventory.setClosed(true);
      }
    }
  }


  @EventHandler
  public void onClick(InventoryClickEvent e) {
    if (plugin.inventories.containsKey(e.getWhoClicked().getUniqueId().toString())) {
      InventoryA inventory = plugin.inventories.get(e.getWhoClicked().getUniqueId().toString());
      if (!inventory.isStorage() && !inventory.isClosed()) {
        ItemStack item = e.getCurrentItem();
        if (item != null) {
          ItemMeta itemMeta = item.getItemMeta();
          assert itemMeta != null;
          PersistentDataContainer container = itemMeta.getPersistentDataContainer();
          if (container.has(plugin.idKey, PersistentDataType.INTEGER)) {
            Buy buy = new Buy();
            buy.open((Player) e.getWhoClicked(), item, inventory);
            e.setCancelled(true);
          }
        }


        if (e.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)) e.setCancelled(true);
        if (!Objects.equals(e.getClickedInventory(), e.getWhoClicked().getInventory())) {
          e.setCancelled(true);
        }
      }
    }
  }

  @EventHandler
  public void onInteract(PlayerInteractEntityEvent e) {
    if (e.getRightClicked().hasMetadata("MainAssistant")) {
      Auction auction = new Auction();
      auction.open(e.getPlayer(), "All", 1, true);
      e.setCancelled(true);
    }
    if (e.getRightClicked().hasMetadata("Assistant")) {
      Auction auction = new Auction();
      auction.open(e.getPlayer(), "All", 1, false);
      e.setCancelled(true);
    }
  }

  @EventHandler
  public void onDamage(EntityDamageByEntityEvent e) {
    if (e.getEntity().hasMetadata("Assistant")) {
      if (e.getDamager() instanceof Player) {
        if (!e.getDamager().hasPermission("smartcraft.auction.damageAssistant"))
          e.setCancelled(true);
      }
    }
  }
}
