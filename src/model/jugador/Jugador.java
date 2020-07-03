package model.jugador;

public class Jugador {
    private String nickName;
    private int score;

    public Jugador(String nickName) {
        this.nickName = nickName;
        this.score = 0;
    }

    /*
     * Getters.
     */
    public String getNickName() {
        return nickName;
    }

    public int getScore() {
        return score;
    }

    /*
     * Setters.
     */
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setScore(int score) {
        this.score = score;
    }
}