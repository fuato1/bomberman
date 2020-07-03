package model;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.net.URL;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.entropyinteractive.JGame;

import model.bomberman.Bomberman;
import model.jugador.Jugador;
import model.properties.controller.SettingsController;
import model.properties.view.SettingsView;
import model.properties.view.views_listeners.ScreenStateListener;

public class SistemaJuegos extends JPanel implements ActionListener, ScreenStateListener {
    private static final long serialVersionUID = 1L;

    /*
     * Instancia del jugador.
     */
    public static Jugador player;

    private JGame game;
    private Thread t;
    private JButton bBomberman, bProx;

    public static void main(String[] args) {
        SettingsController.initView();
        
        SistemaJuegos gs = new SistemaJuegos();
        gs.launch();
    }

    public SistemaJuegos() {
        player = new Jugador("Player");

        int rows = 0;
        int columns = 1;
        int separator = 10;

        this.setLayout(new GridLayout(rows, columns, separator, separator));

        bBomberman = new JButton();
        bProx = new JButton();

        try {
            URL iconURL = getClass().getResource("/imagenes/logo_b.png");
            ImageIcon icon = new ImageIcon(iconURL);
            bBomberman.setIcon(icon);

            iconURL = getClass().getResource("/imagenes/logo_prox.png");
            icon = new ImageIcon(iconURL);
            bProx.setIcon(icon);
        } catch (Exception e) {
            System.out.println(e);
        }

        bBomberman.addActionListener(this);
        SettingsView.getMainView().addOptionsPanelSSL(this);

        this.add(bBomberman);
        this.add(bProx);
    }

    /*
     * Getters.
     */
    public static Jugador getPlayer() {
        return player;
    }

    /*
     * Setters.
     */
    public static void setPlayer(Jugador player) {
        SistemaJuegos.player = player;
    }

    /*
     * Metodo que inicia el sistema de juegos.
     */
    public void launch() {
        JFrame frame = new JFrame("Sistema de Juegos");

        try {
            URL iconURL = getClass().getResource("/imagenes/logo_sj.png");
            ImageIcon icon = new ImageIcon(iconURL);
            frame.setIconImage(icon.getImage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.add(this);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() == bBomberman.getActionCommand()) {
            SettingsController.closeConfig();
            game = new Bomberman();

            t = new Thread() {
                public void run() {
                    game.run(1.0 / 60.0);
                }
            };

            t.start();
        }
    }

    @Override
    public void stateChanged(boolean state) {
        try {
            FileReader reader = new FileReader("jgame.properties");

            Properties p = new Properties();
            p.load(reader);
            p.setProperty("fullScreen", String.valueOf(state));
            p.store(new FileOutputStream("jgame.properties"), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}