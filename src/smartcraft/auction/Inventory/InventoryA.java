package smartcraft.auction.Inventory;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import ru.tehkode.permissions.bukkit.PermissionsEx;
import smartcraft.auction.DB.Mysql;
import smartcraft.auction.Main;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public abstract class InventoryA {

  private String category = "All";
  private String sort = "New2Old";
  private boolean storage = false;
  private boolean sell = false;
  private boolean closed = false;
  private boolean assistant = false;
  private int page = 1;
  private Navigation navigation;
  private Player p;
  private UUID playerUUID;
  private String title;
  private Main plugin;
  private Listener listener;
  private InventoryA inventoryA;
  private Inventory inventory;
  private ItemStack buyItem;
  private ItemStack sellItem;

  public InventoryA open(Player p, UUID uuid, String title, String category, boolean storage, int page, Main plugin, Listener listener, InventoryA inv, boolean assistant) {
    if (!p.getOpenInventory().getTitle().equalsIgnoreCase(title)) {
      this.p = p;
      this.title = title;
      this.plugin = plugin;
      this.listener = listener;
      this.inventoryA = inv;
      this.playerUUID = uuid;
      inv.storage = storage;
      this.assistant = assistant;
      inv.setCategory(category);

      p.getOpenInventory().close();

      plugin.inventories.put(p.getUniqueId().toString(), this);
      setInventory(plugin.getServer().createInventory(null, 54, title));
      inventory.clear();
      loadInventory(inventory, getUUID(), plugin, category, page, sort);

      setNavigation(new Navigation());
      navigation.create(inventory, inventoryA);
      p.openInventory(inventory);
    }
    return this;
  }
  public InventoryA open(Player p, String title, String category, boolean storage, int page, Main plugin, Listener listener, InventoryA inv, boolean assistant) {
    return open(p, null, title, category, storage, page, plugin, listener, inv, assistant);
  }

  public void goBack() {
    setClosed(false);
    open(this.p, playerUUID, this.title, this.getCategory(), this.isStorage(), this.page, this.plugin, this.listener, this.inventoryA, this.isAssistant());
  }

  public void saveStorage(Inventory inventory, Player p) {
    if(!isStorage()) return;
    Navigation.clear(inventory);
    Set<ItemStack> addItems = new HashSet<>(Arrays.asList(inventory.getContents()));
    Set<ItemStack> list = new HashSet<>(plugin.mysql.getItems(getUUID(), getCategory(), getPage(), getSort(), true));
    Set<ItemStack> removeList = new HashSet<>(list);

    removeList.removeAll(addItems);
    addItems.removeAll(list);

    List<Integer> removeIds = new ArrayList<>();
    for (ItemStack item : removeList) {
      if (item == null) continue;
      ItemMeta itemMeta = item.getItemMeta();
      if (itemMeta.getPersistentDataContainer().has(plugin.idKey, PersistentDataType.INTEGER)) {
        removeIds.add(itemMeta.getPersistentDataContainer().get(plugin.idKey, PersistentDataType.INTEGER));
      } else continue;
    }

    plugin.mysql.addToStorage(getUUID(), addItems, removeIds);
    inventory.clear();
    for (ItemStack itemStack : p.getInventory().getContents()) {
      if (itemStack == null) continue;
      ItemMeta itemMeta = itemStack.getItemMeta();
      PersistentDataContainer container = itemMeta.getPersistentDataContainer();
      if (container.has(plugin.idKey, PersistentDataType.INTEGER)) {
        container.remove(plugin.idKey);
        itemStack.setItemMeta(itemMeta);
      }
    }
  }

  public void SaveAndRefresh() {
    saveStorage(getInventory(), p);
    getInventory().clear();
    loadInventory(inventory, getUUID(), plugin, category, page, sort);
    navigation.create(inventory, inventoryA);
  }


  public void refresh() {
    getInventory().clear();
    loadInventory(inventory, getUUID(), plugin, category, page, sort);
    navigation.create(inventory, inventoryA);
  }

  public String getUUID(){
    return playerUUID == null ? p.getUniqueId().toString() : playerUUID.toString();
  }

  public int getMaxPage(boolean storage) {
    if (!storage) {
      return plugin.mysql.getPages(getCategory());
    } else {
      List<String> list = PermissionsEx.getUser(p).getPermissions(p.getWorld().getName());
      for (String perm : list) {
        if (perm.startsWith("smartcraft.auction.storageMax")) {
          return Integer.parseInt(perm.replace("smartcraft.auction.storageMax.", ""));
        }
      }
    }
    return 1;
  }

  public void nextPage() {
    int max_page = getMaxPage(isStorage());
    if (page < max_page) {
      page++;
      refresh();
    }
  }

  public void prevPage() {
    if (page > 1) {
      page--;
      refresh();
    }
  }

  public void sellItem(int id, int price, Player p) {
    if (plugin.mysql.exist(id)) {
      plugin.mysql.sell(id, price);
      p.sendMessage(ChatColor.GREEN + "Товар выставлен на продажу");
    } else {
      p.sendMessage(ChatColor.RED + "Ошибка! Предмет не найден.");
    }
  }

  public void buyItem(int id, Player p) {
    if (plugin.mysql.exist(id, true)) {
      if (plugin.mysql.getItemsCount(p.getUniqueId().toString()) >= 36 * getMaxPage(true)) {
        p.sendMessage(ChatColor.RED + "У вас нету свободного места на складе!");
        return;
      }
      ResultSet result = plugin.mysql.getItem(id);
      if (result != null) {
        Economy economy = plugin.getEconomy();
        try {
          int price = result.getInt("price");
          double balance = economy.getBalance(p);
          if (balance < price) {
            p.sendMessage(ChatColor.RED + "На счету недостаточно средств");
            return;
          }
          EconomyResponse response = economy.withdrawPlayer(p, price);
          if (response.transactionSuccess()) {
            OfflinePlayer player = Bukkit.getPlayer(UUID.fromString(result.getString("UUID")));
            if (player != null) {
              EconomyResponse resp = economy.depositPlayer(player, price);
              if (!resp.transactionSuccess()) {
                plugin.getLogger().warning(ChatColor.RED + "Ошибка перевода средств");
              }
              if (player.isOnline()) {
                ((Player) player).sendMessage(ChatColor.GREEN + "Предмет со склада успешно продан");
              }
            } else plugin.getLogger().warning(ChatColor.RED + "Ошибка перевода средств");
            ItemStack item = Mysql.fromBase64(result.getString("item"));
//            p.getInventory().addItem(item);
            Set<ItemStack> item_ = new HashSet<>();
            item_.add(item);
            plugin.mysql.addToStorage(p.getUniqueId().toString(), item_, new ArrayList<>());
            plugin.mysql.removeItem(id);
            p.sendMessage(ChatColor.GREEN + "Спасибо за покупку. Товар добавлен вам на склад");
          } else {
            p.sendMessage(ChatColor.RED + "Произошла ощибка при покупке.");
          }
        } catch (SQLException | IOException e) {
          e.printStackTrace();
        }
      } else p.sendMessage(ChatColor.RED + "Ошибка! Предмет не найден.");
    } else
      p.sendMessage(ChatColor.RED + "Предмет был снят с продажи или удадлен");
  }

  public void unSell(int id, Player p) {
    if (plugin.mysql.exist(id)) {
      plugin.mysql.unSell(id);
      p.sendMessage(ChatColor.GREEN + "Товар снят из продажи");
    } else {
      p.sendMessage(ChatColor.RED + "Ошибка! Предмет не найден.");
    }
  }


  public void loadInventory(Inventory i, String UUID, Main plugin, String category, int page, String sort) {
    List<ItemStack> list = plugin.mysql.getItems(UUID, category, page, sort, isStorage());
    ItemStack[] items = new ItemStack[list.size()];
    list.toArray(items);
    i.setContents(items);
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public boolean isStorage() {
    return storage;
  }

  public int getPage() {
    return page;
  }

  public void setPage(int page) {
    this.page = page;
  }

  public Navigation getNavigation() {
    return navigation;
  }

  public void setNavigation(Navigation navigation) {
    this.navigation = navigation;
  }

  public Inventory getInventory() {
    return inventory;
  }

  public void setInventory(Inventory inventory) {
    this.inventory = inventory;
  }

  public String getSort() {
    return sort;
  }

  public void setSort(String sort) {
    this.sort = sort;
  }

  public boolean isSell() {
    return sell;
  }

  public void setSell(boolean sell) {
    this.sell = sell;
  }

  public Player getPlayer() {
    return this.p;
  }

  public ItemStack getBuyItem() {
    return buyItem;
  }

  public void setBuyItem(ItemStack buyItem) {
    this.buyItem = buyItem;
  }

  public ItemStack getSellItem() {
    return sellItem;
  }

  public String getTitle() {
    return this.title;
  }

  public void setSellItem(ItemStack sellItem) {
    this.sellItem = sellItem;
  }

  public boolean isClosed() {
    return closed;
  }

  public void setClosed(boolean closed) {
    this.closed = closed;
  }

  public boolean isAssistant() {
    return assistant;
  }
}
