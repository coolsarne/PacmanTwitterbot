package model;

import java.util.LinkedList;
import java.util.List;

/**
 * Arne Cools
 * 28/10/2021
 */
public abstract class MovingBoardPiece {
    private int xPos;
    private int yPos;
    private boolean isAlive;
    private List<int[]> path;

    public MovingBoardPiece(int xPos, int yPos){
        this.xPos = xPos;
        this.yPos = yPos;
        isAlive = true;
        this.path = new LinkedList<>();
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
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

    public List<int[]> getPath() {
        return path;
    }

    public void setPath(List<int[]> path) {
        this.path = path;
    }
}
