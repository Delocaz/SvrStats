package me.Delocaz.SvrStats;

import org.bukkit.plugin.java.JavaPlugin;

public class SvrStats extends JavaPlugin {
	public void onEnable() {
		getConfig().addDefault("url", "localhost");
		getConfig().addDefault("port", "3306");
		getConfig().addDefault("database", "minecraft");
		getConfig().addDefault("username", "root");
		getConfig().addDefault("password", "password");
		getConfig().options().copyDefaults(true);
		saveConfig();
		getServer().getPluginManager().registerEvents(new SSListener(this), this);
	}
	public void onDisable() {
	}
}
