package Discord;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class DB_Connector {
	private Connection conn = null;
	
	public DB_Connector() {
		boolean result = setup();
		if(result == false)
			System.exit(1);
	}
	
	public boolean setup() {
		try {
            // The newInstance() call is a work around for some
            // broken Java implementations
			
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            return true;
        } catch (Exception ex) {
            return false;
        }
	}
	
	public boolean connect() {
		int tries = 0;
		while(tries < Constants.CONN_TRIES) {
			try {
				tries++;
				conn = DriverManager.getConnection(DataManager.Instance().database_string);
				System.out.println("Connection extablished.");
				return true;
			} catch (SQLException e) {
				IO.printToConsoleDebug("\tERROR: Connection could not be established.", 1);
				IO.printToConsoleDebug("\tSQLException: " + e.getMessage(), 2);
			}
			try {
				Thread.sleep(Constants.CONN_TRY_SLEEP);
			} catch (InterruptedException e) {}
		}
		return false;
	}
	
	public boolean closeConnection() {
		try {
			conn.close();
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
	
	public Set<Long> getChannelIDs() throws SQLException{
		maintainConnection();
		
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
		    stmt = conn.createStatement();
		    rs = stmt.executeQuery("SELECT channelId FROM RoleReactionAssignmentChannel");

		    Set<Long> rSet = new HashSet<Long>(); 
		    while(rs.next()) {
		    	rSet.add(rs.getLong(1));
		    }
		    IO.printToConsoleDebug("CHGET : getting channels successful", 1);
		    return rSet;
		}
		catch (SQLException ex){
			IO.printToConsoleDebug("CHGET : ERROR getting channels", 1);
		}
		finally {
		    if (rs != null) {
		        try {
		            rs.close();
		        } catch (SQLException sqlEx) { } // ignore
		        rs = null;
		    }

		    if (stmt != null) {
		        try {
		            stmt.close();
		        } catch (SQLException sqlEx) { } // ignore
		        stmt = null;
		    }
		}
		return null;
	}
	
	public boolean addChannelId(long id) {
		try {
			maintainConnection();
		} catch (SQLException e1) {
			return false;
		}
		
		Statement stmt = null;
		
		try {
			stmt = conn.createStatement();
			
			String sql = "INSERT INTO RoleReactionAssignmentChannel " +
	                "VALUES ("+ id + ")";
			stmt.executeUpdate(sql);
			IO.printToConsoleDebug("CHEDIT: Added channel " + id, 1);
			return true;
		} catch (SQLException e) {
			IO.printToConsoleDebug("CHEDIT: ERROR adding channel " + id, 1);
			IO.printToConsoleDebug(e.getMessage(), 2);
			return false;
		}
		finally {
		    if (stmt != null) {
		        try {
		            stmt.close();
		        } catch (SQLException sqlEx) { } // ignore

		        stmt = null;
		    }
		}
	}
	
	public boolean deleteChannelId(long id) {
		try {
			maintainConnection();
		} catch (SQLException e1) {
			return false;
		}
		
		Statement stmt = null;
		
		try {
			stmt = conn.createStatement();
			
			String sql = "DELETE FROM RoleReactionAssignmentChannel " +
	                   "WHERE channelId = "+id;
			stmt.executeUpdate(sql);
			IO.printToConsoleDebug("CHEDIT: deleted channel " + id, 1);
			return true;
		} catch (SQLException e) {
			IO.printToConsoleDebug("CHEDIT: ERROR deleting channel " + id, 1);
			IO.printToConsoleDebug(e.getMessage(), 2);
			return false;
		}
		finally {
		    if (stmt != null) {
		        try {
		            stmt.close();
		        } catch (SQLException sqlEx) { } // ignore

		        stmt = null;
		    }
		}
	}
	
	public boolean addEmoji(long channelId, boolean isUnicode, String unicodeString, long emojiId, long roleId) {
		try {
			maintainConnection();
		} catch (SQLException e1) {
			return false;
		}
		java.sql.PreparedStatement stmt = null;
		
		try {
			String sqlString = "INSERT INTO EmojiBinds"
					+ "(channelId, isUnicode, unicodeString, emojiId, roleId) VALUES"
					+ "(?,?,?,?,?)";
			
			stmt = conn.prepareStatement(sqlString);
			
			
			stmt.setLong(1, channelId);
			stmt.setBoolean(2, isUnicode);
			stmt.setString(3, unicodeString);
			stmt.setLong(4, emojiId);
			stmt.setLong(5, roleId);
			
			stmt.executeUpdate();
			
			IO.printToConsoleDebug("EJEDII: database added bound emoji ", 2);
			return true;
		} catch (SQLException e) {
			IO.printToConsoleDebug("CHEDIT: ERROR adding bound emoji to database ", 2);
			IO.printToConsoleDebug(e.getMessage(), 2);
			return false;
		}
		finally {
		    if (stmt != null) {
		        try {
		            stmt.close();
		        } catch (SQLException sqlEx) { } // ignore

		        stmt = null;
		    }
		}
	}
	
	public boolean removeEmoji(long channelId, boolean isUnicode, String unicodeString, long emojiId) {
		try {
			maintainConnection();
		} catch (SQLException e1) {
			return false;
		}
		java.sql.PreparedStatement stmt = null;
		
		try {
			String sqlString_unicode = "DELETE FROM EmojiBinds "
					+ "WHERE channelId = ? AND unicodeString = ?";
			String sqlString_id = "DELETE FROM EmojiBinds "
					+ "WHERE channelId = ? AND emojiId = ?";
			
			if(isUnicode) {
				stmt = conn.prepareStatement(sqlString_unicode);
				stmt.setLong(1, channelId);
				stmt.setString(2, unicodeString);
			} else {
				stmt = conn.prepareStatement(sqlString_id);
				stmt.setLong(1, channelId);
				stmt.setLong(2, emojiId);
			}
			
			stmt.executeUpdate();
			
			IO.printToConsoleDebug("EJEDII: database removed bound emoji ", 2);
			return true;
		} catch (SQLException e) {
			IO.printToConsoleDebug("CHEDIT: ERROR removing bound emoji to database ", 2);
			IO.printToConsoleDebug(e.getMessage(), 2);
			return false;
		}
		finally {
		    if (stmt != null) {
		        try {
		            stmt.close();
		        } catch (SQLException sqlEx) { } // ignore

		        stmt = null;
		    }
		}
	}
	
	public boolean removeAllEmojisOfChannel(long channelId) {
		try {
			maintainConnection();
		} catch (SQLException e1) {
			return false;
		}
		java.sql.PreparedStatement stmt = null;
		
		try {
			String sqlString = "DELETE FROM EmojiBinds "
					+ "WHERE channelId = ?";
			
			stmt = conn.prepareStatement(sqlString);
			stmt.setLong(1, channelId);
			
			
			stmt.executeUpdate();
			
			IO.printToConsoleDebug("EJEDII: database removed all bound emojis from channel "+channelId, 2);
			return true;
		} catch (SQLException e) {
			IO.printToConsoleDebug("CHEDIT: ERROR removing all bound emojis from channel "+channelId, 2);
			IO.printToConsoleDebug(e.getMessage(), 2);
			return false;
		}
		finally {
		    if (stmt != null) {
		        try {
		            stmt.close();
		        } catch (SQLException sqlEx) { } // ignore

		        stmt = null;
		    }
		}
	}
	
	public Map<Long,Vector<BoundEmoji>> getBoundEmojis() throws SQLException {
		maintainConnection();
		
		Statement stmt = null;
		ResultSet rs = null;
		try {
		    stmt = conn.createStatement();
		    rs = stmt.executeQuery("SELECT * FROM EmojiBinds");

		    Map<Long,Vector<BoundEmoji>> rSet = new HashMap<Long,Vector<BoundEmoji>>(); 
		    
		    while(rs.next()) {
		    	long channelId = rs.getLong("channelId");
		    	boolean isUnicode = rs.getBoolean("isUnicode");
		    	String unicodeString = rs.getString("unicodeString");
		    	long emojiId = rs.getLong("emojiId");
		    	long roleId = rs.getLong("roleId");
		    	
		    	BoundEmoji entry = new BoundEmoji(channelId,isUnicode,unicodeString,emojiId,roleId);
		    	
		    	if(rSet.containsKey(channelId)) {
		    		rSet.get(channelId).addElement(entry);
		    	} else {
		    		Vector<BoundEmoji> vec = new Vector<BoundEmoji>();
		    		vec.addElement(entry);
		    		rSet.put(channelId, vec);
		    	}
		    }
		    IO.printToConsoleDebug("CHGET : getting bound emojis successful", 1);
		    return rSet;
		}
		catch (SQLException ex){
			IO.printToConsoleDebug("CHGET : ERROR getting bound emojis channels", 1);
		}
		finally {
		    if (rs != null) {
		        try {
		            rs.close();
		        } catch (SQLException sqlEx) { } // ignore
		        rs = null;
		    }

		    if (stmt != null) {
		        try {
		            stmt.close();
		        } catch (SQLException sqlEx) { } // ignore
		        stmt = null;
		    }
		}
		return null;
	}
	
	private boolean checkConnection() {
		boolean connIsValid = false;
		try {
			connIsValid = conn.isValid(Constants.CONN_CHECK_TIMEOUT);
		} catch (SQLException e) {
			IO.printToConsole("Invalid connection.");
		}
		if(connIsValid)
			return true;
		return false;
	}
	
	private boolean maintainConnection() throws SQLException{
		if(!checkConnection()) {
			boolean renewed = connect();
			if(!renewed) {
				IO.printToConsole("Could not renew connection.");
				throw new SQLException("Bad Dtabase connection. Could not reconnect.");
			}
		}
		return true;
	}
	
}
