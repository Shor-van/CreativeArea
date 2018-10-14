package github.shor_van.creativearea;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class EventListener implements Listener
{
    private final JavaPlugin plugin;

    public EventListener(JavaPlugin plugin)
    {
        this.plugin = plugin;
    }
}
