package me.Delocaz.SvrStats;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SSMySQL {
	Connection con;
	public SSMySQL(SvrStats svrStats) {
		try {
			con = DriverManager.getConnection("jdbc:mysql://"+svrStats.getConfig().getString("url")+":"+svrStats.getConfig().getString("port")+"/"+svrStats.getConfig().getString("database"), svrStats.getConfig().getString("username"), svrStats.getConfig().getString("password"));
			edit("CREATE TABLE IF NOT EXISTS `stats` (`player` varchar(16) NOT NULL, `joins` int(11) NOT NULL, `lastjoin` int(11) NOT NULL, `playtime` int(11) NOT NULL, `firstjoin` int(11) NOT NULL, `chatchar` int(11) NOT NULL, `chats` int(11) NOT NULL, `placed` int(11) NOT NULL, `broke` int(11) NOT NULL) ENGINE=MyISAM DEFAULT CHARSET=latin1;");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public ResultSet query(String q) {
		Statement s;
		try {
			s = con.createStatement();
			return s.executeQuery(q);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public void edit(String q) {
		try {
			Statement s = con.createStatement();
			s.executeUpdate(q);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
