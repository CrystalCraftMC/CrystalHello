/* Copyright */
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class CrystalHello extends JavaPlugin {
    public void onEnable() {
        getLogger().info(ChatColor.AQUA + "CrystalHello has been initialized!");
    }

    public void onDisable() {
        getLogger().info(ChatColor.AQUA + "CrystalHello has been stopped by the server.");
    }

    // Insert attributes and behaviors here!
}