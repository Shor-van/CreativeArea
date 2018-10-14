package github.shor_van.creativearea;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/**The main class for the plugin*/
public class CreativeArea extends JavaPlugin
{
    private Location creativeArea; //The location of the create area
    private CommandHandler cmdHandler; //The command handler for the plugin
    
    /**Handles the initialization of the plugin.*/
    @Override
    public void onEnable()
    {
        this.getConfig();
        this.saveDefaultConfig();

        cmdHandler = new CommandHandler(this);

        String worldName = this.getConfig().getString("creative-area.world");
        double posX = this.getConfig().getDouble("creative-area.x", 0);
        double posY = this.getConfig().getDouble("creative-area.y", 70);
        double posZ = this.getConfig().getDouble("creative-area.z", 0);

        if (worldName != null && worldName.equals("") == false)
        {
            creativeArea = new Location(Bukkit.getWorld(worldName), posX, posY, posZ);

            this.getCommand("tpcreative").setExecutor(cmdHandler);
            this.getCommand("leavecreative").setExecutor(cmdHandler);
        } 
        else
        {
            getLogger().severe("The world name spesified  in the config is not valid, disabing");
            onDisable();
        }
    }

    /**Handles the disabling of the plugin.*/
    @Override
    public void onDisable()
    {
        cmdHandler = null;
        creativeArea = null;
    }

    /**Retrieves a player's inventory from the config file 
     * @param player the player who's inventory to restore*/
    public void setPlayerInventoryFromFile(Player player)
    {
        String loadPath = "player-inventory-data." + player.getName();
        if (this.getConfig().contains(loadPath) == true)
        {
            player.setLevel(this.getConfig().getInt(loadPath + ".xp-level"));
            player.setExp((float) this.getConfig().getDouble(loadPath + ".xp-points"));

            ConfigurationSection inventorySection = this.getConfig().getConfigurationSection(loadPath + ".inventory");
            Set<String> keys = inventorySection.getKeys(false);
            for (String key : keys)
            {
                int slot = Integer.parseInt(key.split("-")[1]);
                player.getInventory().setItem(slot, this.getConfig().getItemStack(inventorySection.getCurrentPath() + "." + key));
            }
        } 
        else
            this.getLogger().warning("Could not find inventory data for player: " + player.getName());
    }
    
    /**Saves the player's inventory to the config file
     * @param player the player who's inventory we are saving*/
    public void savePlayerInevntoryToFile(Player player)
    {
        String savePath = "player-inventory-data." + player.getName();

        this.getConfig().set(savePath + ".xp-level", player.getLevel());
        this.getConfig().set(savePath + ".xp-points", player.getExp());

        this.getConfig().createSection(savePath + ".inventory");
        ItemStack[] itemStack = player.getInventory().getContents();
        for (int i = 0; i < itemStack.length; i++)
            if (itemStack[i] != null)
                this.getConfig().set(savePath + ".inventory.item-" + i, itemStack[i]);

        this.saveConfig();
    }
    
    /**Gets the location of were the players get teleported to
     * @return the location of the creative area*/
    public Location getCreativeArea() { return creativeArea; }
}