package model;

import java.util.LinkedHashMap;
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
    private LinkedHashMap<int[], FieldTileStatus> pathHashMap;


    public MovingBoardPiece(int xPos, int yPos){
        this.xPos = xPos;
        this.yPos = yPos;
        isAlive = true;
        pathHashMap = new LinkedHashMap<>();
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

    public LinkedHashMap<int[], FieldTileStatus> getPathHashMap() {
        return pathHashMap;
    }


}
