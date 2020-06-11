package model.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DB {
    private static final String DB_PATH = System.getProperty("user.dir") + ".db";
    private static final String SQL_SCORES = "CREATE TABLE IF NOT EXISTS scores \n" +
		"(id INTEGER PRIMARY KEY AUTOINCREMENT, playerName TEXT, score INT)";
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;
    PreparedStatement pstmt = null;
    
    public void initDBConn() {
        try {
            String url = "jdbc:sqlite:" + DB_PATH;
             
            conn = DriverManager.getConnection(url);
            System.out.println("Conectado a SQLite.");

            // deleteScores();

           	stmt = conn.createStatement();
            String sql = SQL_SCORES;

            stmt.executeUpdate(sql);
         	stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        
        }
    }

    public void closeDBConn() {
        try {
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());

            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }  
    }

    public void selectAll(){
        try {
            String sql = "SELECT * FROM scores";

            rs = stmt.executeQuery(sql);

            while(rs.next()) {
                System.out.println(
                    rs.getInt("id") + " " +
                    rs.getString("playerName") + "\t" + 
                    rs.getInt("score")
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
         
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public void insertScore(String playerName, int score){
        try {
            String sql = "INSERT INTO scores (playerName, score) VALUES (?, ?)";
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, playerName);
            pstmt.setInt(2, score);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public void deleteScore(int id){
        try {
            String sql = "DELETE FROM scores WHERE id = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());

            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public void updateScore(int id, String playerName, int score){
        try {
            String sql = "UPDATE scores SET playerName = ?,  score = ? WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, playerName);
            pstmt.setInt(2, score);
            pstmt.setInt(3, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());

            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }

    }

    public void deleteScores() {
        try {
            stmt = conn.createStatement();
            stmt.execute("DROP TABLE scores");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}

