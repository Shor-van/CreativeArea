package github.shor_van.creativearea;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**Handles the execution of commands for the creative area plugin*/
public class CommandHandler implements CommandExecutor
{
    private final JavaPlugin plugin; //Reference to the base plugin
    
    /**Creates a new instance of the command handler, there should only be one
     * @param plugin the plugin, this should be of type CreativeArea*/
    public CommandHandler(JavaPlugin plugin)
    {
        this.plugin = plugin;
    }

    /**Handles the /tpcreative /leavecreative commands, triggered when any of these commands are sent to the server
     * @param sender the CommandSender object that sent the command
     * @param cmd the command that was sent, in this case it should only be /shop
     * @param label the CommandSender object that sent the command
     * @param args the arguments that the user sent with the command*/
    @Override
    @SuppressWarnings({ "unchecked", "deprecation" })
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (cmd.getName().equalsIgnoreCase("tpcreative")) //if its the /tpcreative command
        {
            if (sender.isOp() == true && sender.hasPermission("creativearea.command.gocreative") == true)
            {
                if (args.length >= 1)
                {
                    Entity[] targets;
                    if (args[0].startsWith("@p"))
                        targets = CommandUtils.getTargets(sender, args[0]);
                    else if (Bukkit.getPlayerExact(args[0]) != null)
                    {
                        targets = new Entity[1];
                        targets[0] = Bukkit.getPlayerExact(args[0]);
                    } 
                    else
                    {
                        sender.sendMessage(ChatColor.RED + "Target " + args[0] + " not found!");
                        return true;
                    }

                    for (Entity target : targets)
                    {
                        if (target instanceof Player)
                        {
                            Player targetPlayer = ((Player) target);
                            List<String> playersInCreative = (List<String>) plugin.getConfig().getList("players-in-creative");
                            if (playersInCreative.contains(targetPlayer.getName()) == false)
                            {
                                playersInCreative.add(targetPlayer.getName());
                                ((CreativeArea) plugin).savePlayerInevntoryToFile(targetPlayer);
                                targetPlayer.getInventory().clear();

                                targetPlayer.setSleepingIgnored(true);
                                targetPlayer.teleport(((CreativeArea) plugin).getCreativeArea());
                                targetPlayer.setGameMode(GameMode.CREATIVE);

                                plugin.getConfig().set("players-in-creative", playersInCreative);
                                plugin.saveConfig();

                                targetPlayer.sendMessage(ChatColor.GOLD + "Teleporting to the creative zone.");
                                plugin.getLogger().info(targetPlayer.getName() + " teleported to the creative area.");
                            }
                            else
                            {
                                plugin.getLogger().warning("Target " + targetPlayer.getName() + " is already in the creative area!");
                                sender.sendMessage(ChatColor.RED + "Target " + targetPlayer.getName() + " is already in the creative area");
                                return true;
                            }
                        }
                    }
                    return true;
                } 
                else
                {
                    sender.sendMessage(ChatColor.AQUA + "/tpcreative <player>");
                    return true;
                }
            } 
            else
            {
                sender.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
                return true;
            }
        } 
        else if (cmd.getName().equalsIgnoreCase("leavecreative")) //if its the leave creative command
        {
            if (sender.isOp() == true && sender.hasPermission("creativearea.command.gocreative") == true)
            {
                if (args.length >= 1)
                {
                    Entity[] targets;
                    if (args[0].startsWith("@p"))
                        targets = CommandUtils.getTargets(sender, args[0]);
                    else if (Bukkit.getPlayerExact(args[0]) != null)
                    {
                        targets = new Entity[1];
                        targets[0] = Bukkit.getPlayerExact(args[0]);
                    } 
                    else
                    {
                        sender.sendMessage(ChatColor.RED + "Target " + args[0] + " not found!");
                        return true;
                    }

                    for (Entity target : targets)
                    {
                        if (target instanceof Player)
                        {
                            Player targetPlayer = ((Player) target);
                            List<String> playersInCreative = (List<String>) plugin.getConfig().getList("players-in-creative");
                            if (playersInCreative.contains(targetPlayer.getName()) == true)
                            {
                                playersInCreative.remove(targetPlayer.getName());

                                targetPlayer.getInventory().clear();
                                ((CreativeArea) plugin).setPlayerInventoryFromFile(targetPlayer);

                                if (targetPlayer.hasPermission("admintools.ignoredforsleep") == false)
                                    targetPlayer.setSleepingIgnored(false);

                                targetPlayer.teleport(target.getWorld().getSpawnLocation());

                                targetPlayer.setGameMode(GameMode.SURVIVAL);

                                plugin.getConfig().set("players-in-creative", playersInCreative);
                                plugin.saveConfig();

                                targetPlayer.sendMessage(ChatColor.GOLD + "Leaving the creative zone.");
                                plugin.getLogger().info(targetPlayer.getName() + " left the creative area.");
                            } 
                            else
                            {
                                sender.sendMessage(ChatColor.RED + "Target " + targetPlayer.getName() + " is not in the creative zone.");
                                sender.sendMessage(ChatColor.RED + "Target " + targetPlayer.getName() + " is not in the creative area");
                                return true;
                            }
                        }
                    }
                    return true;
                } 
                else
                {
                    sender.sendMessage(ChatColor.AQUA + "/leavecreative <player>");
                    return true;
                }
            } 
            else
            {
                sender.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
                return true;
            }
        }
        return false;
    }
}
