package me.Delocaz.SvrStats;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class SSListener implements Listener {
	SSMySQL sql;
	SvrStats par;
	
	public SSListener(SvrStats svrStats) {
		sql = new SSMySQL(svrStats);
		par = svrStats;
	}
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) throws SQLException {
			String name = e.getPlayer().getName();
			ResultSet pData = sql.query("SELECT * FROM stats WHERE player='"+name+"'");
			if (!pData.first()) {
				sql.edit("INSERT INTO stats VALUES ('"+name+"', 1, "+(System.currentTimeMillis()/1000)+", 0, "+(System.currentTimeMillis()/1000)+", 0, 0, 0, 0)");
			} else {
				sql.edit("UPDATE stats SET joins="+(pData.getInt("joins")+1)+", lastjoin="+(System.currentTimeMillis()/1000)+" WHERE player='"+name+"'");
			}
	}
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) throws SQLException {
		String name = e.getPlayer().getName();
		ResultSet pData = sql.query("SELECT * FROM stats WHERE player='"+name+"'");
		pData.first();
		long ses = (System.currentTimeMillis()/1000)-pData.getInt("lastjoin");
		sql.edit("UPDATE stats SET playtime="+(pData.getInt("playtime")+ses)+" WHERE player='"+name+"'");
	}
	@EventHandler
	public void onPlayerChat(PlayerChatEvent e) throws SQLException {
		int length = e.getMessage().length();
		String name = e.getPlayer().getName();
		ResultSet pData = sql.query("SELECT * FROM stats WHERE player='"+name+"'");
		pData.first();
		sql.edit("UPDATE stats SET chatchar="+(pData.getInt("chatchar")+length)+", chats="+(pData.getInt("chats")+1)+" WHERE player='"+name+"'");
	}
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) throws SQLException {
		String name = e.getPlayer().getName();
		ResultSet pData = sql.query("SELECT * FROM stats WHERE player='"+name+"'");
		pData.first();
		sql.edit("UPDATE stats SET placed="+(pData.getInt("placed")+1)+" WHERE player='"+name+"'");
	}
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) throws SQLException {
		String name = e.getPlayer().getName();
		ResultSet pData = sql.query("SELECT * FROM stats WHERE player='"+name+"'");
		pData.first();
		sql.edit("UPDATE stats SET broke="+(pData.getInt("broke")+1)+" WHERE player='"+name+"'");
	}
	@EventHandler
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent e) throws SQLException {
		String name = e.getPlayer().getName();
		ResultSet pData = sql.query("SELECT * FROM stats WHERE player='"+name+"'");
		pData.first();
		sql.edit("UPDATE stats SET cmds="+(pData.getInt("cmds")+1)+" WHERE player='"+name+"'");
	}
}
