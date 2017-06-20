package fr.sayro.Sayro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class main extends JavaPlugin implements Listener, CommandExecutor
{
	public static Logger logger = Logger.getLogger("Minecraft");

	  ItemStack bate = new ItemStack(Material.STICK, 1);
	  ItemMeta batem = this.bate.getItemMeta();
	  public static Inventory shop;
	  public static final transient HashMap<Player, ArrayList<Block>> minigame = new HashMap();
	  public static final transient HashMap<Player, ArrayList<Block>> kitbasico = new HashMap();

	  public void onDisable()
	  {
	    PluginDescriptionFile pdfFile = getDescription();

	    logger.info(pdfFile.getName() + " its getting desactivated!");
	    shop = Bukkit.createInventory(null, 9, "*Newenv*");

	    for (Player player : Bukkit.getOnlinePlayers()) {
	      Location lobby = new Location(player.getWorld(), getConfig().getInt("lobbyx"), getConfig().getInt("lobbyy"), getConfig().getInt("lobbyz"));
	      if (minigame.containsKey(Bukkit.getPlayer(player.getName()))) {
	        player.teleport(lobby);
	        player.getInventory().clear();
	        ItemStack aire = new ItemStack(Material.AIR, 1);
	        player.getInventory().setHelmet(aire);
	        player.getInventory().setChestplate(aire);
	        player.getInventory().setLeggings(aire);
	        player.getInventory().setBoots(aire);
	      }
	    }
	  }

	  public void onEnable()
	  {
	    PluginManager pm = getServer().getPluginManager();
	    PluginDescriptionFile pdf = getDescription();
	    pm.registerEvents(this, this);
	    getConfig().options().copyDefaults();
	    saveConfig();
	  }

	  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	  {
	    Player player = (Player)sender;
	    World mundo = player.getWorld();
	    Location location = player.getLocation();

	    if (commandLabel.equalsIgnoreCase("tfil")) {
	      if (args.length < 1) {
	        player.sendMessage(ChatColor.GOLD + "-The Floor Is Lava-");
	        player.sendMessage("Commands of *The Floor is Lava*");
	        player.sendMessage("To join a match use /tfil join");
	        player.sendMessage("The match NEVER stops");
	        player.sendMessage("To leave a match use /tfil leave");
	      }
	      else if ((args.length == 1) && (player.getWorld().getName().toString().equalsIgnoreCase(getConfig().getString("World")))) {
	        if ((args[0].equalsIgnoreCase("join")) && (!minigame.containsKey(Bukkit.getPlayer(player.getName())))) {
	          World world = player.getWorld();

	          ItemStack bate = new ItemStack(Material.STICK, 1);
	          ItemMeta batem = bate.getItemMeta();
	          batem.setDisplayName(ChatColor.GOLD + "Bat");
	          ArrayList batel = new ArrayList();
	          batel.add(ChatColor.DARK_GREEN + "How to use: Left Click");
	          batem.addEnchant(Enchantment.KNOCKBACK, 1, true);
	          batem.setLore(batel);
	          bate.setItemMeta(batem);

	          double random = Math.random();

	          double x = random * getConfig().getInt("maxarenas");

	          int y = (int)x;
	          byte bit = (byte)y;
	          player.getInventory().clear();
	          Location arena = new Location(player.getWorld(), getConfig().getInt("arena" + y + "x"), getConfig().getInt("arena" + y + "y"), getConfig().getInt("arena" + y + "z"));
	          minigame.put(Bukkit.getPlayer(player.getName()), null);
	          player.sendMessage(ChatColor.GOLD + "[The Floor Is Lava]" + ChatColor.WHITE + " You joined the arena " + y + "....");
	          player.teleport(arena);
	          player.getInventory().addItem(new ItemStack[] { bate });
	        }

	        if (args[0].equalsIgnoreCase("leave")) {
	          Location lobby = new Location(player.getWorld(), getConfig().getInt("lobbyx"), getConfig().getInt("lobbyy"), getConfig().getInt("lobbyz"));
	          if (minigame.containsKey(Bukkit.getPlayer(player.getName()))) {
	            World world = player.getWorld();
	            player.sendMessage(ChatColor.GOLD + "[The Floor Is Lava]" + ChatColor.WHITE + " You left the match");
	            minigame.remove(Bukkit.getPlayer(player.getName()));

	            minigame.remove(Bukkit.getPlayer(player.getName()));
	            player.teleport(lobby);
	          } else {
	            player.sendMessage(ChatColor.GOLD + "[The Floor Is Lava]" + ChatColor.WHITE + " You no are playing!");
	          }

	        }

	        return false;
	      }
	    }
	    return false;
	  }

	  @EventHandler(priority=EventPriority.NORMAL)
	  public void onPlayerMoveEvent(PlayerMoveEvent event) {
	    Player player = event.getPlayer();
	    Location loc = player.getPlayer().getLocation();
	    loc.setY(loc.getY() - 1.0D);
	    Location loc2 = player.getPlayer().getLocation();
	    loc2.setY(loc2.getY() + 1.0D);
	    World world = player.getWorld();

	    Location lobby = player.getPlayer().getLocation();
	    if (minigame.containsKey(Bukkit.getPlayer(player.getName()))) {
	      int block = loc.getWorld().getBlockTypeIdAt(loc);

	      if (block == 35)
	      {
	        player.sendMessage(ChatColor.RED + "¡You fell onto the lava!");
	        Bukkit.getServer().broadcastMessage(ChatColor.DARK_RED + "¡" + player.getName() + " fell onto the lava!");
	        player.setHealth(0.0D);
	        player.getWorld().playEffect(loc2, Effect.STEP_SOUND, 55);
	        minigame.remove(Bukkit.getPlayer(player.getName()));
	        player.getWorld().createExplosion(loc2, 0.0F);
	      }
	    } else {
	      minigame.containsKey(Bukkit.getPlayer(player.getName()));
	    }
	  }

	  public void onPlayerMove(EntityDeathEvent ev)
	  {
	    if ((ev.getEntity() instanceof Player)) {
	      Player player = (Player)ev.getEntity();
	      Location lobby = new Location(player.getWorld(), getConfig().getInt("lobbyx"), getConfig().getInt("lobbyy"), getConfig().getInt("lobbyz"));
	      if (minigame.containsKey(Bukkit.getPlayer(player.getName()))) {
	        ev.getDrops().clear();
	        minigame.remove(Bukkit.getPlayer(player.getName()));
	        player.teleport(lobby);
	      }
	    }
	  }

	  public void onPlayerMove(PlayerInteractEvent ev)
	  {
	    Player player = ev.getPlayer();
	    if (minigame.containsKey(Bukkit.getPlayer(player.getName())))
	      ev.setCancelled(true);
	    else
	      minigame.containsKey(Bukkit.getPlayer(player.getName()));
	  }

	  @EventHandler(priority=EventPriority.NORMAL)
	  public void onPlayerBreakEvent(BlockBreakEvent event)
	    throws InterruptedException
	  {
	    Player player = event.getPlayer();

	    Location lobby = player.getPlayer().getLocation();
	    minigame.containsKey(Bukkit.getPlayer(player.getName()));
	  }
}
