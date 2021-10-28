package model;

import java.util.LinkedList;
import java.util.List;

/**
 * Arne Cools
 * 27/10/2021
 */
public class Player extends MovingBoardPiece{
    private int score;
    private int lives;


    public Player(int xPos, int yPos, int lives){
        super(xPos, yPos);
        this.lives = lives;
        this.score = 0;
        getPathHashMap().put(new int[]{1,1}, FieldTileStatus.FREE);
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getLives() {
        return lives;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }



    public void move(FieldTileStatus[][] floorPlan, char playerChoice) {
        int newXpos = getxPos();
        int newYpos = getyPos();
        switch (playerChoice) {
            case 'w':
                newYpos = getyPos() - 1;
                newXpos = getxPos();
                break;
            case 'a':
                newXpos = getxPos() - 1;
                newYpos = getyPos();
                break;
            case 's':
                newYpos = getyPos() + 1;
                newXpos = getxPos();
                break;
            case 'd':
                newXpos = getxPos() + 1;
                newYpos = getyPos();
                break;

        }
        if (floorPlan[newYpos][newXpos] != FieldTileStatus.WALL) {
            getPathHashMap().put(new int[]{newXpos, newYpos}, floorPlan[newYpos][newXpos]);
            setxPos(newXpos);
            setyPos(newYpos);
        }

    }
}
