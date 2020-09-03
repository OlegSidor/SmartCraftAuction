package smartcraft.auction.Entityes;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import net.minecraft.server.v1_14_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import smartcraft.auction.Inventory.Auction;
import smartcraft.auction.Main;

import java.lang.reflect.InvocationTargetException;

public class TemporyAssistant extends BukkitRunnable {

  private EntityVillager assistant;
  private Player player;
  private PacketListener listener;
  private int count = 4;

  public TemporyAssistant(Main plugin, Player p) {
    net.minecraft.server.v1_14_R1.World s = ((CraftWorld) p.getLocation().getWorld()).getHandle();
    EntityVillager villager = new EntityVillager(EntityTypes.VILLAGER, s);

    villager.setLocation(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ(), p.getLocation().getYaw(), 0);
    villager.setCustomName(new ChatComponentText("Персональный аукционист"));
    villager.setCustomNameVisible(true);
    villager.setInvulnerable(true);
    villager.setNoGravity(true);

    assistant = villager;
    player = p;

    PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(villager);
    ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);

    p.playSound(p.getLocation(), Sound.BLOCK_PORTAL_TRIGGER, 1, 15);

    listener = new PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Client.USE_ENTITY) {
      @Override
      public void onPacketReceiving(PacketEvent e) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
          @Override
          public void run() {
            if(e.getPacketType() == PacketType.Play.Client.USE_ENTITY) {
              if(e.getPacket().getIntegers().read(0) == villager.getId()){
                count--;
                if(count == 0){
                  Auction auction = new Auction();
                  auction.open(e.getPlayer(), "All", 1, false);
                  count = 4;
                }
              }
            }
          }
        }, 0);
      }
    };
    ProtocolLibrary.getProtocolManager().addPacketListener(listener);
    this.runTaskLater(plugin, plugin.getConfig().getInt("assistantTimeOut")*20);
  }

  @Override
  public void run() {
    ProtocolLibrary.getProtocolManager().removePacketListener(listener);
    PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(assistant.getId());
    ((CraftPlayer)player).getHandle().playerConnection.sendPacket(destroy);
    player.playSound(player.getLocation(), Sound.BLOCK_PORTAL_TRIGGER, 1, 15);
  }
}
