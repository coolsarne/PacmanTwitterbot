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
    private int currentSpeed;
    private boolean moveTowardsPlayer;
    private int[] spawnPoint;


    public Spook(int[] spawnPoint, int moveTimer) {
        super(spawnPoint[0], spawnPoint[1]);
        this.spawnPoint = spawnPoint;
        this.moveTimer = moveTimer;
        moveTowardsPlayer = true;
        this.currentSpeed = moveTimer;
    }

    public void returnToSpawn(){
        super.setxPos(spawnPoint[0]);
        super.setyPos(spawnPoint[1]);
    }

    public void move(FieldTileStatus[][] floorPlan, int xPlayer, int yPlayer) {
        // First we check the moves the spook could possibly make
        List<Moves> possibleMoves = new ArrayList();
        System.out.println(getyPos() + " " + getxPos());
        if (floorPlan[getxPos()][getyPos() - 1] != FieldTileStatus.WALL) possibleMoves.add(Moves.UP);
        if (floorPlan[getxPos()][getyPos() + 1] != FieldTileStatus.WALL) possibleMoves.add(Moves.DOWN);
        if (floorPlan[getxPos() - 1][getyPos()] != FieldTileStatus.WALL) possibleMoves.add(Moves.LEFT);
        if (floorPlan[getxPos() + 1][getyPos()] != FieldTileStatus.WALL) possibleMoves.add(Moves.RIGHT);


        Random rd = new Random();
        if (currentSpeed % moveTimer == 0){
            Moves move = possibleMoves.get(rd.nextInt(possibleMoves.size()) - 1);
            setxPos(getxPos() + move.getXChange());
            setyPos(getyPos() + move.getyChange());

        }
        moveTimer++;

    }

    public enum Moves {
        UP(-1,0), DOWN(1,0), LEFT(-1,0), RIGHT(1,0);

        Moves(int xChange, int yChange){
            this.xChange = xChange;
            this.yChange = yChange;
        }

        private int xChange;
        private int yChange;

        public int getXChange() {
            return xChange;
        }

        public int getyChange() {
            return yChange;
        }
    }


}
