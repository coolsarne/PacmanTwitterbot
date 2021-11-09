package model;

import java.util.*;

/**
 * Arne Cools
 * 27/10/2021
 */
public class Ghost extends MovingBoardPiece {
    private int moveTimer; //The amount of moves a player has to make before the ghost moves 1 tile.
    private int currentSpeed;
    private boolean moveTowardsPlayer;
    private int[] spawnPoint;


    public Ghost(int[] spawnPoint, int moveTimer) {
        super(spawnPoint[0], spawnPoint[1]);
        this.spawnPoint = spawnPoint;
        this.moveTimer = moveTimer;
        moveTowardsPlayer = true;
        this.currentSpeed = moveTimer;
        getPathHashMap().put(new int[]{spawnPoint[0], spawnPoint[1]}, FieldTileStatus.FOOD);
        getPathHashMap().put(new int[]{spawnPoint[0], spawnPoint[1]}, FieldTileStatus.FOOD);
    }

    public void returnToSpawn() {
        super.setxPos(spawnPoint[0]);
        super.setyPos(spawnPoint[1]);
    }

    public void move(FieldTileStatus[][] floorPlan, int xPlayer, int yPlayer) {
        // First we check the moves the ghost could possibly make
        List<Moves> possibleMoves = new ArrayList();
        if (floorPlan[getxPos()][getyPos() - 1] != FieldTileStatus.WALL) possibleMoves.add(Moves.UP);
        if (floorPlan[getxPos()][getyPos() + 1] != FieldTileStatus.WALL) possibleMoves.add(Moves.DOWN);
        if (floorPlan[getxPos() - 1][getyPos()] != FieldTileStatus.WALL) possibleMoves.add(Moves.LEFT);
        if (floorPlan[getxPos() + 1][getyPos()] != FieldTileStatus.WALL) possibleMoves.add(Moves.RIGHT);

        //Next, the ghost gets a random move to make except for going back
        Random rd = new Random();
        if (currentSpeed % moveTimer == 0) {
            List<int[]> pathList = new ArrayList<>(getPathHashMap().keySet());
            for (Iterator<Moves> iterator = possibleMoves.iterator(); iterator.hasNext(); ) {
                Moves next =  iterator.next();
                int[] futurePos = new int[]{getxPos() + next.getXChange(), getyPos() + next.getyChange()};
                if (Arrays.equals(futurePos, pathList.get(pathList.size() - 2))) {
                    iterator.remove();
                }

            }

            Moves move = possibleMoves.get(rd.nextInt(possibleMoves.size()));
            int[] futurePos = new int[]{getxPos() + move.getXChange(), getyPos() + move.getyChange()};
            // If there is a ghost at the next tile, go to other direction
            if (floorPlan[futurePos[0]][futurePos[1]].equals(FieldTileStatus.LIVINGGHOST)){
                setxPos(pathList.get(pathList.size() - 2)[0]);
                setyPos(pathList.get(pathList.size() - 2)[1]);
            } else {
                setxPos(getxPos() + move.getXChange());
                setyPos(getyPos() + move.getyChange());
            }

            getPathHashMap().put(new int[]{getxPos(), getyPos()}, floorPlan[getxPos()][getyPos()]);
        }
        currentSpeed++;

    }

    public enum Moves {
        UP(0, -1), DOWN(0, 1), LEFT(-1, 0), RIGHT(1, 0);

        Moves(int xChange, int yChange) {
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
