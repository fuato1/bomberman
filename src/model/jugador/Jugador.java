package model.jugador;

public class Jugador {
    private String nickName = "Player";
    private Puntaje score;

    public Jugador(String nickName) {
        this.nickName = nickName;
    }

    /*
        Getters
    */
    public String getNickName() {
        return nickName;
    }

    public Puntaje getScore() {
        return score;
    }

    /*
        Setters
    */
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setScore(Puntaje score) {
        this.score = score;
    }
}