package github.shor_van.creativearea;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**Listens for game events to trigger*/
public class EventListener implements Listener
{
    private final JavaPlugin plugin; //Reference to the base plugin

    /**Creates a new instance of the event listener, there should only be one
     * @param plugin the plugin, this should be of type CreativeArea*/
    public EventListener(JavaPlugin plugin)
    {
        if(plugin instanceof CreativeArea)
            this.plugin = plugin;
        else
            throw new IllegalArgumentException("This class should only be created/passed the creative area plugin's main class");
    }
    
    /**Triggered when a player dies
     * @param event the PlayerDeathEvent that was triggered*/
    @SuppressWarnings("unchecked")
    public void onPlayerDeath(PlayerDeathEvent event)
    {
        Player player = event.getEntity();
        List<String> playersInCreative = (List<String>) plugin.getConfig().getList("players-in-creative");
        if (playersInCreative.contains(player.getName()) == true)
        {
            
        }
    }
}
