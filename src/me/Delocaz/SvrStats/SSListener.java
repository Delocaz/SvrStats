package me.Delocaz.SvrStats;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class SSListener implements Listener {
	SSMySQL sql;
	SvrStats par;

	public SSListener(SvrStats svrStats) {
		sql = new SSMySQL(svrStats);
		par = svrStats;
	}
	public void increment(String name, String value, int amount) throws SQLException {
		ResultSet pData = sql.query("SELECT * FROM stats WHERE player='"+name+"'");
		pData.first();
		sql.edit("UPDATE stats SET "+value+"="+(pData.getInt(value)+amount)+" WHERE player='"+name+"'");
	}
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) throws SQLException {
		String name = e.getPlayer().getName();
		ResultSet pData = sql.query("SELECT * FROM stats WHERE player='"+name+"'");
		if (!pData.first()) {
			sql.edit("INSERT INTO stats VALUES ('"+name+"', 1, "+(System.currentTimeMillis()/1000)+", 0, "+(System.currentTimeMillis()/1000)+", 0, 0, 0, 0, 0, 0, 0, 0)");
		} else {
			increment(name, "joins", 1);
			increment(name, "lastjoin", (int) System.currentTimeMillis()/1000);
		}
	}
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) throws SQLException {
		ResultSet pData = sql.query("SELECT * FROM stats WHERE player='"+e.getPlayer().getName()+"'");
		pData.first();
		long ses = (System.currentTimeMillis()/1000)-pData.getInt("lastjoin");
		increment(e.getPlayer().getName(), "playtime", (int) ses);
	}
	@EventHandler
	public void onPlayerChat(PlayerChatEvent e) throws SQLException {
		int length = e.getMessage().length();
		increment(e.getPlayer().getName(), "chatchar", length);
		increment(e.getPlayer().getName(), "chats", 1);
	}
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) throws SQLException {
		increment(e.getPlayer().getName(), "placed", 1);
		if (e.getBlockPlaced().getType() == Material.TORCH) {
			increment(e.getPlayer().getName(), "torches", 1);
		}
	}
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) throws SQLException {
		increment(e.getPlayer().getName(), "broke", 1);
	}
	@EventHandler
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent e) throws SQLException {
		increment(e.getPlayer().getName(), "cmds", 1);
	}
	@EventHandler
	public void onEntityDeath(EntityDeathEvent e) throws SQLException {
		if (e.getEntity().getKiller() instanceof Player) {
			increment(e.getEntity().getKiller().getName(), "kills", 1);
		}
	}
	@EventHandler
	public void onPlayerPortal(PlayerPortalEvent e) throws SQLException {
		increment(e.getPlayer().getName(), "portal", 1);
	}
}
