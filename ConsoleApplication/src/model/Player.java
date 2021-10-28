package model;

/**
 * Arne Cools
 * 27/10/2021
 */
public class Player {
    private int lives;
    private int xPos;
    private int yPos;

    public Player(int xPos, int yPos){
        this.lives = 3;
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public int getxPos() {
        return xPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getLives() {
        return lives;
    }
}
