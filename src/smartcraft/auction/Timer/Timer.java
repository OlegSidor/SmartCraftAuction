package smartcraft.auction.Timer;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import smartcraft.auction.Main;

public class Timer extends BukkitRunnable {

  private Main plugin = Main.getPlugin(Main.class);
  private int time = plugin.getConfig().getInt("timeOut");
  private Player player;

  public Timer(Player p){
    player = p;
  }

  @Override
  public void run() {
    time--;
    if(time == 0){
      stop();
    }
  }

  public void start(){
    plugin.timeOut.put(player.getUniqueId().toString(), this);
    this.runTaskTimer(plugin, 0, 20);
  }

  public void stop(){
    if(plugin.timeOut.containsKey(player.getUniqueId().toString())){
      plugin.timeOut.remove(player.getUniqueId().toString());
    }
    if(!isCancelled()) {
      this.cancel();
    }
  }

  public int getTime() {
    return time;
  }
}
