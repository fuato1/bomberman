package model.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.Ranking;
import model.jugador.Jugador;

public class DB {
    /*
     * Variables para conexion y creacion de las tabla con los puntajes.
     */
    private static final String DB_PATH = System.getProperty("user.dir") + ".db";
    private static final String SQL_SCORES = "CREATE TABLE IF NOT EXISTS scores \n"
            + "(id INTEGER PRIMARY KEY AUTOINCREMENT, playerName TEXT, score INT)";

    /*
     * Variables para uso en las consultas.
     */
    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet rs = null;
    private PreparedStatement pstmt = null;

    /*
     * Metodo para iniciar la conexion a la BD.
     */
    public void initDBConn() {
        try {
            String url = "jdbc:sqlite:" + DB_PATH;

            conn = DriverManager.getConnection(url);

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

    /*
     * Metodo usado para cargar el ranking de jugadores.
     */
    public void selectAllInto(Ranking ranking) {
        try {
            rs = stmt.executeQuery("SELECT * FROM scores ORDER BY score DESC LIMIT 10");

            while (rs.next()) {
                Jugador player = new Jugador(rs.getString("playerName"));
                player.setScore(rs.getInt("score"));

                ranking.getScores().add(player);
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

    /*
     * Metodo usado para insertar el puntaje de un jugador al finalizar el juego.
     */
    public void insertScore(String playerName, int score) {
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
}
