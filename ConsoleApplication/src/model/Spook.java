package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Arne Cools
 * 27/10/2021
 */
public class Spook extends MovingBoardPiece{
    private int moveTimer; //The amount of moves a player has to make before the spook moves 1 tile.
    private boolean moveTowardsPlayer;
    private int[] spawnPoint;


    public Spook(int[] spawnPoint, int moveTimer) {
        super(spawnPoint[0], spawnPoint[1]);
        this.spawnPoint = spawnPoint;
        this.moveTimer = moveTimer;
        moveTowardsPlayer = true;
    }

    public void returnToSpawn(){
        super.setxPos(spawnPoint[0]);
        super.setyPos(spawnPoint[1]);
    }

    @Override
    public void move(FieldTileStatus[][] board) {
        // First we check the moves the spook could possibly make
        List<Moves> possibleMoves = new ArrayList();


    }

    private enum Moves  {
        UP, DOWN, LEFT, RIGHT
    }
}
