package smartcraft.auction.Items.NavigationItems;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import smartcraft.auction.Inventory.InventoryA;
import smartcraft.auction.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NextArrow implements Item {

  private Main plugin = Main.getPlugin(Main.class);
  private final int giveSlot = 50;


  @Override
  public void give(Inventory inventory, InventoryA inv) {
    ItemStack item = new ItemStack(Material.PLAYER_HEAD);
    SkullMeta itemMeta = (SkullMeta) item.getItemMeta();
    GameProfile profile = new GameProfile(UUID.randomUUID(), null);
    profile.getProperties().put("textures", new Property("textures", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGVmMzU2YWQyYWE3YjE2NzhhZWNiODgyOTBlNWZhNWEzNDI3ZTVlNDU2ZmY0MmZiNTE1NjkwYzY3NTE3YjgifX19"));
    plugin.setSullTexture(itemMeta, profile);
    List<String> lore = new ArrayList<>();
    lore.add("("+inv.getPage()+"/"+inv.getMaxPage(inv.isStorage())+")");
    itemMeta.setLore(lore);
    itemMeta.setDisplayName(ChatColor.GREEN + "Следующая страница");
    itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    itemMeta.getPersistentDataContainer().set(plugin.navigationKey, PersistentDataType.STRING, "NextArrow");
    item.setItemMeta(itemMeta);
    inventory.setItem(giveSlot, item);
  }

  @Override
  public boolean click(Player p, InventoryA inv) {
    inv.nextPage();
    return true;
  }
}