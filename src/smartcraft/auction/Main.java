package smartcraft.auction;

import com.mojang.authlib.GameProfile;
import net.milkbowl.vault.VaultEco;
import net.milkbowl.vault.economy.Economy;
import net.minecraft.server.v1_14_R1.EntityVillager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import smartcraft.auction.Commands.A;
import smartcraft.auction.DB.Mysql;
import smartcraft.auction.Entityes.Assistant;
import smartcraft.auction.Inventory.*;
import smartcraft.auction.Timer.Timer;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main extends JavaPlugin {

  public Mysql mysql;
  public Map<String, InventoryA> inventories = new HashMap<>();
  public Map<String, Timer> timeOut = new HashMap<>();
  private List<Villager> assistants = new ArrayList<>();

  public NamespacedKey sellKey = new NamespacedKey(this, "Sell");
  public NamespacedKey idKey = new NamespacedKey(this, "Id");
  public NamespacedKey priceKey = new NamespacedKey(this, "Price");
  public NamespacedKey buyKey = new NamespacedKey(this, "Buy");
  public NamespacedKey navigationKey = new NamespacedKey(this, "Navigation");
  public NamespacedKey sumKey = new NamespacedKey(this, "Sum");

  private Economy economy = null;

  @Override
  public void onEnable() {

    if (!setupEconomy()) {
      System.out.println("Disabled due to no Vault dependency found!");
      getServer().getPluginManager().disablePlugin(this);
      return;
    }

    getServer().getPluginManager().registerEvents(new Navigation(), this);
    getServer().getPluginManager().registerEvents(new Auction(), this);
    getServer().getPluginManager().registerEvents(new Storage(), this);
    getServer().getPluginManager().registerEvents(new Sell(), this);
    getServer().getPluginManager().registerEvents(new Category(), this);
    getServer().getPluginManager().registerEvents(new Buy(), this);
    getServer().getPluginManager().registerEvents(new Sort(), this);


    getCommand("a").setExecutor(new A(this));

    if (!new File(this.getDataFolder(), "config.yml").exists()) {
      saveDefaultConfig();
    }

    if (getConfig().contains("assistant")) {
      Location location = (Location) getConfig().get("assistant");
      for (Entity entity : location.getWorld().getNearbyEntities(location, 1, 1, 1)) {
        if(entity instanceof Villager) {
          ((Villager) entity).damage(9999);
          entity.remove();
        }
      }
      assistants.add(Assistant.spawn(location));
    }

    mysql = new Mysql(this);
    getLogger().info("Auction is enabled");
  }

  @Override
  public void onDisable() {
    for (Villager villager : assistants) {
      villager.setInvulnerable(false);
      villager.damage(9999);
      villager.remove();
    }
    getLogger().info("Auction is disabled");
  }

  public void setSullTexture(SkullMeta itemMeta, GameProfile profile) {
    try {
      Field profileField = itemMeta.getClass().getDeclaredField("profile");
      profileField.setAccessible(true);
      profileField.set(itemMeta, profile);

    } catch (NoSuchFieldException | IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  private boolean setupEconomy() {
    RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
    if (economyProvider != null) {
      economy = economyProvider.getProvider();
    }

    return (economy != null);
  }

  public Economy getEconomy() {
    return economy;
  }


  public List<Villager> getAssistants() {
    return assistants;
  }
}
