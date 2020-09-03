package smartcraft.auction.Items.NavigationItems;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
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

public class Sell implements Item {
  private Main plugin = Main.getPlugin(Main.class);
  private final int giveSlot = 53;

  @Override
  public void give(Inventory inventory, InventoryA inv) {
    ItemStack item = new ItemStack(Material.ENDER_PEARL);
    if (!inv.isStorage()) item.setType(Material.BARRIER);
    ItemMeta itemMeta = item.getItemMeta();
    itemMeta.setDisplayName(ChatColor.AQUA + "Режим продавца");
    if (!inv.isStorage()) {
      List<String> lore = new ArrayList<>();
      lore.add("Доступно только на складе");
      itemMeta.setLore(lore);
    }
    if (inv.isSell()) itemMeta.addEnchant(Enchantment.ARROW_FIRE, 1, false);
    itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
    itemMeta.getPersistentDataContainer().set(plugin.navigationKey, PersistentDataType.STRING, "Sell");
    item.setItemMeta(itemMeta);
    inventory.setItem(giveSlot, item);
  }

  @Override
  public boolean click(Player p, InventoryA inv) {
    if (!inv.isStorage()) return true;
    inv.setSell(!inv.isSell());
    inv.SaveAndRefresh();
    return true;
  }

}
