package smartcraft.auction.DB;

import net.minecraft.server.v1_14_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;
import smartcraft.auction.Main;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Mysql {

  private Main plugin;
  private Connection connection;
  private String host, db, user, password;
  private int port;

  public Mysql(Main plugin) {
    host = plugin.getConfig().getString("host");
    port = plugin.getConfig().getInt("port");
    db = plugin.getConfig().getString("db");
    user = plugin.getConfig().getString("user");
    password = plugin.getConfig().getString("password");
    this.plugin = plugin;
    try {
      synchronized (this) {
        if (connection != null && !connection.isClosed()) {
          return;
        }
        Class.forName("com.mysql.jdbc.Driver");

        setConnection(DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + db, user, password));

        Bukkit.getLogger().info("Auction: MySQL Connected");
      }
    } catch (SQLException | ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  public void setConnection(Connection connection) {
    this.connection = connection;
  }

  public Connection getConnection() {
    return connection;
  }

  public void clearStorage(String playerUUID, String category) {
    clearStorage(playerUUID, category, false);
  }
  public void clearStorage(String playerUUID, String category, boolean fullClear) {
    if (category == "All") {
      category = "";
    } else category = "AND category = \'" + category + "\'";
    try {
      PreparedStatement statement = connection.prepareStatement("DELETE FROM Auction WHERE UUID=? AND sell=FALSE " + category);
      if(fullClear) statement = connection.prepareStatement("DELETE FROM Auction WHERE UUID=?");
      statement.setString(1, playerUUID);

      statement.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  public void removeItem(int id){
    try {
      PreparedStatement statement = connection.prepareStatement("DELETE FROM Auction WHERE id=?");
      statement.setInt(1, id);

      statement.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void addToStorage(String playerUUID, Set<ItemStack> items, List<Integer> removeItems) {
    try {
      PreparedStatement statement = connection.prepareStatement("DELETE FROM Auction WHERE id=?");
      for (Integer i : removeItems) {
        statement.setInt(1, i);
        statement.addBatch();
      }
      statement.executeBatch();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    try {
      PreparedStatement statement = connection.prepareStatement("INSERT INTO Auction(UUID, item, category) VALUES (?,?,?)");
      for (ItemStack item : items) {
        if (item == null) continue;
        String category = "All";
        if (CraftItemStack.asNMSCopy(item).getItem() instanceof ItemArmor) category = "Armor";
        if (CraftItemStack.asNMSCopy(item).getItem() instanceof ItemBlock) category = "Block";
        if (CraftItemStack.asNMSCopy(item).getItem() instanceof ItemTool) category = "Tool";
        if (CraftItemStack.asNMSCopy(item).getItem() instanceof ItemPotion) category = "Potion";
        if (CraftItemStack.asNMSCopy(item).getItem() instanceof ItemSword) category = "Weapon";
        if (CraftItemStack.asNMSCopy(item).getItem() instanceof ItemBow) category = "Weapon";

        statement.setString(1, playerUUID);
        statement.setString(2, toBase64(item));
        statement.setString(3, category);
        statement.addBatch();
      }
      statement.executeBatch();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public int getItemsCount(String UUID) {
    try {
    PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) as count FROM Auction WHERE UUID = ?");
    statement.setString(1, UUID);
    ResultSet results = statement.executeQuery();
    results.next();
    return results.getInt("count");
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return 0;
  }
  public int getPages(String category) {
    if (category == "All") {
      category = "";
    } else category = "WHERE category = \'" + category + "\'";
    try {
      PreparedStatement statement = connection.prepareStatement("SELECT CEIL(COUNT(id)/36) as count FROM Auction " + category);
      ResultSet results = statement.executeQuery();
      results.next();
      return results.getInt("count");
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return 1;
  }

  private String getSort(String sort) {
    switch (sort) {
      case "New2Old":
        sort = "ORDER BY id DESC";
        break;
      case "Old2New":
        sort = "ORDER BY id";
        break;
      case "Exp2Che":
        sort = "ORDER BY price DESC";
        break;
      case "Che2Exp":
        sort = "ORDER BY price";
        break;
    }
    return sort;
  }

  public ResultSet getItem(int id) {
    try {
      PreparedStatement statement = connection.prepareStatement("SELECT * FROM Auction WHERE id = ?");
      statement.setInt(1, id);
      ResultSet results = statement.executeQuery();
      results.next();
      return results;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public List<ItemStack> getItems(String playerUUID, String category, int page, String sort, boolean storage) {
    if (category == "All") {
      category = "";
    } else category = "AND category = \'" + category + "\'";
    sort = getSort(sort);
    List<ItemStack> items = new ArrayList<ItemStack>();
    try {
      PreparedStatement statement = connection.prepareStatement("SELECT item, id, UUID, price FROM Auction WHERE UUID <> ? AND sell = TRUE " + category + " " + sort + " LIMIT " + (page - 1) * 36 + ", " + page * 36);
      if (storage) {
        statement = connection.prepareStatement("SELECT item, sell, id, price FROM Auction WHERE UUID=? " + category + " " + sort + " LIMIT " + (page - 1) * 36 + ", " + page * 36);
      }
      statement.setString(1, playerUUID);
      ResultSet results = statement.executeQuery();
      while (results.next()) {
        String item_raw = results.getString("item");
        ItemStack item = fromBase64(item_raw);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.getPersistentDataContainer().set(plugin.idKey, PersistentDataType.INTEGER, results.getInt("id"));
        if (storage && results.getBoolean("sell")) {
          if (itemMeta == null) continue;
          List<String> lore = new ArrayList<>();
          if (itemMeta.hasLore()) lore = itemMeta.getLore();
          lore.add("Товар в продаже");
          lore.add("Цена: "+results.getInt("price"));
          itemMeta.setLore(lore);
          itemMeta.getPersistentDataContainer().set(plugin.sellKey, PersistentDataType.INTEGER, 1);
        }
        if(!storage){
          if (itemMeta == null) continue;
          List<String> lore = new ArrayList<>();
          if (itemMeta.hasLore()) lore = itemMeta.getLore();
          lore.add("Цена: "+results.getInt("price"));

          String seller = "";
          OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(results.getString("UUID")));
          if(player != null) seller = player.getName();
          lore.add("Продавец: "+seller);
          itemMeta.setLore(lore);
        }
        item.setItemMeta(itemMeta);
        items.add(item);
      }
    } catch (SQLException | IOException e) {
      e.printStackTrace();
    }

    return items;
  }

  public static String toBase64(ItemStack item) throws IllegalStateException {
    try {
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

      dataOutput.writeObject(item);

      dataOutput.close();
      return Base64Coder.encodeLines(outputStream.toByteArray());
    } catch (Exception e) {
      throw new IllegalStateException("Unable to save item stacks.", e);
    }
  }

  public void sell(int id, int price) {
    try {
      PreparedStatement statement = connection.prepareStatement("UPDATE Auction SET sell=TRUE, price=? WHERE id=?");
      statement.setInt(1, price);
      statement.setInt(2, id);

      statement.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void unSell(int id) {
    try {
      PreparedStatement statement = connection.prepareStatement("UPDATE Auction SET sell=FALSE WHERE id=?");
      statement.setInt(1, id);

      statement.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public boolean exist(int id) {
    return exist(id, false);
  }
  public boolean exist(int id, boolean sell) {
    String sellQ = "";
    if(sell) sellQ = "AND sell = TRUE";
    try {
      PreparedStatement statement = connection.prepareStatement("SELECT COUNT(id) as count FROM Auction WHERE id = ? "+sellQ);
      statement.setInt(1, id);
      ResultSet results = statement.executeQuery();
      results.next();
      return results.getInt("count") > 0;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  public static ItemStack fromBase64(String data) throws IOException {
    try {
      ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
      BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
      ItemStack item;

      item = (ItemStack) dataInput.readObject();

      dataInput.close();
      return item;
    } catch (ClassNotFoundException e) {
      throw new IOException("Unable to decode class type.", e);
    }
  }
}
