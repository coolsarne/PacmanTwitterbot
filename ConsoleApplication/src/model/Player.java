package model;

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

    @Override
    public void move(FieldTileStatus[][] board) {

    }
}
