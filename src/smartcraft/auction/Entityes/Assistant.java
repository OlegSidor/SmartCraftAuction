package smartcraft.auction.Entityes;

import net.minecraft.server.v1_14_R1.AttributeRanged;
import net.minecraft.server.v1_14_R1.IAttribute;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import smartcraft.auction.Main;

public class Assistant {

  private static Main plugin = Main.getPlugin(Main.class);
  public static Villager spawn(Location loc){
    Villager villager = (Villager) loc.getWorld().spawnEntity(loc, EntityType.VILLAGER);
    villager.setCustomName("Аукционист");
    villager.setCustomNameVisible(true);
    villager.setInvulnerable(true);
    villager.setVelocity(new Vector());
    villager.setGravity(false);
    PotionEffect slow = new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 255);
    villager.addPotionEffect(slow, true);
    villager.setMetadata("MainAssistant", new FixedMetadataValue(plugin, true));
    villager.setSilent(true);
    return villager;
  }
}
