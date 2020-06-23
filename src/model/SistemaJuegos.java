package model;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.entropyinteractive.JGame;

import model.bomberman.Bomberman;
import model.jugador.Jugador;

public class SistemaJuegos extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;

    private Jugador player;
    JGame game;
    Thread t;
    JButton bBomberman, bProx;

    public SistemaJuegos(Jugador player) {
        this.player = player;

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
        this.add(bBomberman);
        this.add(bProx);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand() == bBomberman.getActionCommand()){
            game = new Bomberman();

            t = new Thread() {
                public void run() {
                    game.run(1.0 / 60.0);
                }
            };

            t.start();
        }
    }

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

    /*
        Getters
    */
    public Jugador getPlayer() {
        return player;
    }

    /*
        Setters
    */
    public void setPlayer(Jugador player) {
        this.player = player;
    }
}