package model.tests;

import model.bomberman.Bomberman;

public class TestBomberman {
    public static void main(String[] args) {
        Bomberman bm = new Bomberman();
        bm.run(1.0 / 60.0);
        System.exit(0);
    }
}