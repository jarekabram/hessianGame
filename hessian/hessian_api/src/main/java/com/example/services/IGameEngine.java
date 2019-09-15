package com.example.services;

import javafx.util.Pair;

public interface IGameEngine {

    Pair<String, Integer> guessNumber(int number, int pLevel);

    boolean init(int pNumber) throws GameException;

    boolean hasPlayerWin();

    void restart();

    void exit();
}
