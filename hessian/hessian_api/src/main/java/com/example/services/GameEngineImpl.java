package com.example.services;

import javafx.util.Pair;

enum Level {
    HIGH  (1000),
    MEDIUM(100),
    LOW   (10);

    private int levelVal = 0;

    Level(int pMultiplier){
        levelVal = (int) (Math.random() * pMultiplier + 1);
    }

    public int getLevelVal() throws GameException {
        if(this.levelVal == 0) {
            throw new GameException("No such level");
        }
        return this.levelVal;
    }

}
public class GameEngineImpl implements IGameEngine {

    private boolean isGameStarted = false;
    private boolean win = false;

    private int randomNumber;
    private int retries;
    @Override
    public boolean init(int pLevel) throws GameException {

        Level level = null;
        switch(pLevel) {
            case 1:
                level = Level.LOW;
                break;
            case 2:
                level = Level.MEDIUM;
                break;
            case 3:
                level = Level.HIGH;
                break;
            default:
                throw new GameException("No such level");
        }
        randomNumber = level.getLevelVal();

        if(!isGameStarted) {
            System.out.println(randomNumber);
            isGameStarted = true;
            retries = 5;
            return true;
        }
        return false;
    }

    @Override
    public void restart() {
        isGameStarted = false;
    }

    @Override
    public Pair<String, Integer> guessNumber(int pNumber, int pLevel) {
        try {
            init(pLevel);
        }
        catch (GameException e) {
            e.printStackTrace();
        }

        Pair<String, Integer> info = null;
        if(pNumber < randomNumber) {
            retries--;
            info = new Pair<>("Your guess, " + pNumber + ", is too low." , retries);
            win = false;
        }
        else if(pNumber > randomNumber) {
            retries--;
            info = new Pair<>("Your guess, " + pNumber + ", is too high." , retries);
            win = false;
        }
        else if (pNumber == randomNumber) {
            info = new Pair<>("You Won choosing the number " + pNumber, retries);
            win = true;
        }
        return info;
    }

    @Override
    public boolean hasPlayerWin() {
        return win;
    }

    @Override
    public void exit() {
        System.exit(0);
    }
}
