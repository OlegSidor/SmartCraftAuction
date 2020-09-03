package smartcraft.auction.Commands;

import org.apache.logging.log4j.core.config.plugins.convert.TypeConverters;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import smartcraft.auction.Entityes.Assistant;
import smartcraft.auction.Entityes.TemporyAssistant;
import smartcraft.auction.Inventory.Auction;
import smartcraft.auction.Inventory.Storage;
import smartcraft.auction.Main;
import smartcraft.auction.Timer.Timer;
import smartcraft.defender.check;
import smartcraft.defender.main;

import java.util.UUID;

public class A implements CommandExecutor {

  private Main plugin;

  public A(Main main) {
    plugin = main;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String str, String[] args) {
    if (sender instanceof Player) {
      Player p = (Player) sender;
      check check = new check();
      if (args.length == 0) {
        if (check.can(Main.getPlugin(main.class), p.getLocation(), p, true)
            || p.hasPermission("smartcraft.auction.region")) {
          if (!plugin.timeOut.containsKey(p.getUniqueId().toString())) {
            if (p.hasPermission("smartcraft.auction.admin")) {
              new Auction().open(p, "All", 1, false);
              return true;
            }
            new Timer(p).start();
            new TemporyAssistant(plugin, p);
            return true;
          } else {
            Timer timer = plugin.timeOut.get(p.getUniqueId().toString());
            p.sendMessage(ChatColor.RED + "Аукционист отдыхает. Подождите еще " + timer.getTime() + " секунд");
            return true;
          }
        } else sender.sendMessage(ChatColor.RED + "Использовать аукцион можно только в своем регионе!");
      } else if (args.length == 2) {
        if (args[0].equalsIgnoreCase("clear")) {
          for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
            if (player.getName().equalsIgnoreCase(args[1])) {
              plugin.mysql.clearStorage(player.getUniqueId().toString(), "All", true);
              sender.sendMessage(ChatColor.GREEN + "Склад игрока " + args[1] + " был очищен");
              return true;
            }
          }
          sender.sendMessage(ChatColor.RED + "Игрок не найден");
          return true;
        } else if (args[0].equalsIgnoreCase("open")) {
          for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
            String name = player.getName();
            assert name != null;
            if (name.equalsIgnoreCase(args[1])) {
              new Storage().adminOpen(p, player.getUniqueId(), "All", 1);
              return true;
            }
          }
          sender.sendMessage(ChatColor.RED + "Игрок не найден");
          return true;
        }
      } else if (args.length == 1) {
        if (args[0].equalsIgnoreCase("spawn")) {
          plugin.getAssistants().add(Assistant.spawn(p.getLocation()));
        } else if(args[0].equalsIgnoreCase("setspawn")){
          plugin.getConfig().set("assistant", p.getLocation());
          plugin.saveConfig();
        }
      }
    }
    return true;
  }
}
