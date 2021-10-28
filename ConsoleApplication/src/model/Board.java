package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Arne Cools
 * 27/10/2021
 */
public class Board {
    private FieldTileStatus[][] floorPlan;
    private Player player;
    private List<Spook> spooks;
    private int foodCount;

    public Board(int playerLives, int amountOfSpooks) {
        this.floorPlan = new FieldTileStatus[10][13];
        this.player = new Player(1, 1, playerLives);
        foodCount = 0;
        spooks = new ArrayList<>();
        if (amountOfSpooks > 3) amountOfSpooks = 3;
        if (amountOfSpooks < 0) amountOfSpooks = 0;
        switch (amountOfSpooks) {
            case 3:
                spooks.add(new Spook(new int[]{1, floorPlan[1].length - 2}, 1));
            case 2:
                spooks.add(new Spook(new int[]{floorPlan.length - 2, 1}, 1));
            case 1:
                spooks.add(new Spook(new int[]{floorPlan.length - 2, floorPlan[floorPlan.length - 2].length - 2}, 1));
        }



    }

    public void createFloorPlan() {
        //Outside walls and corners
        for (int i = 0; i < floorPlan.length; i++) {
            for (int j = 0; j < floorPlan[i].length; j++) {
                if (i == 0 || i == floorPlan.length - 1) floorPlan[i][j] = FieldTileStatus.WALL;
                else if (j == 0 || j == floorPlan[i].length - 1) floorPlan[i][j] = FieldTileStatus.WALL;
                else floorPlan[i][j] = FieldTileStatus.FOOD;

            }
        }
        floorPlan[0][0] = FieldTileStatus.WALL;
        floorPlan[floorPlan.length - 1][0] = FieldTileStatus.WALL;
        floorPlan[0][floorPlan[floorPlan.length - 1].length - 1] = FieldTileStatus.WALL;
        floorPlan[floorPlan.length - 1][floorPlan[floorPlan.length - 1].length - 1] = FieldTileStatus.WALL;

        //Layout: inside the field
        innerFieldLayout();

        //Print moving boardpieces
        floorPlan[player.getyPos()][player.getxPos()] = FieldTileStatus.LIVINGPLAYER;
        for (Spook spook : spooks) {
            floorPlan[spook.getxPos()][spook.getyPos()] = FieldTileStatus.LIVINGSPOOK;
        }

        updateFoodCount();
    }

    public void draw() {
        for (int i = 0; i < floorPlan.length; i++) {
            for (int j = 0; j < floorPlan[i].length; j++) {
                System.out.print(floorPlan[i][j].getValue() + "\t");
            }
            System.out.print('\n');
        }
    }

    public void updateFoodCount() {
        int count = 0;
        for (int i = 0; i < floorPlan.length; i++) {
            for (int j = 0; j < floorPlan[i].length; j++) {
                if (floorPlan[i][j] == FieldTileStatus.FOOD) count++;
            }
        }
        foodCount = count;
    }

    public void movePieces(char playerMove) {
        //First the player moves a tile
        int newXpos = player.getxPos();
        int newYpos = player.getyPos();
        switch (playerMove) {
            case 'w':
                newYpos = player.getyPos() - 1;
                newXpos = player.getxPos();
                break;
            case 'a':
                newXpos = player.getxPos() - 1;
                newYpos = player.getyPos();
                break;
            case 's':
                newYpos = player.getyPos() + 1;
                newXpos = player.getxPos();
                break;
            case 'd':
                newXpos = player.getxPos() + 1;
                newYpos = player.getyPos();
                break;

        }
        if (floorPlan[newYpos][newXpos] != FieldTileStatus.WALL) {
            if (floorPlan[newYpos][newXpos] == FieldTileStatus.FOOD) foodCount--;
            floorPlan[player.getyPos()][player.getxPos()] = FieldTileStatus.FREE;
            player.setxPos(newXpos);
            player.setyPos(newYpos);
            floorPlan[player.getyPos()][player.getxPos()] = FieldTileStatus.LIVINGPLAYER;
        }

        //Then the spooks will move a tile
        Random rd = new Random();
        for (Spook spook : spooks) {
            spook.move(floorPlan);
        }




        updateFoodCount();
    }




    private void innerFieldLayout() { //TODO get rid of hardcode
        floorPlan[2][2] = FieldTileStatus.WALL;
        floorPlan[3][2] = FieldTileStatus.WALL;
        floorPlan[5][2] = FieldTileStatus.WALL;
        floorPlan[6][2] = FieldTileStatus.WALL;
        floorPlan[7][2] = FieldTileStatus.WALL;
        floorPlan[5][3] = FieldTileStatus.WALL;
        floorPlan[1][4] = FieldTileStatus.WALL;
        floorPlan[2][4] = FieldTileStatus.WALL;
        floorPlan[3][4] = FieldTileStatus.WALL;
        floorPlan[5][4] = FieldTileStatus.WALL;
        floorPlan[7][4] = FieldTileStatus.WALL;
        floorPlan[7][5] = FieldTileStatus.WALL;
        floorPlan[2][6] = FieldTileStatus.WALL;
        floorPlan[3][6] = FieldTileStatus.WALL;
        floorPlan[4][6] = FieldTileStatus.WALL;
        floorPlan[5][6] = FieldTileStatus.WALL;
        floorPlan[6][6] = FieldTileStatus.WALL;
        floorPlan[7][6] = FieldTileStatus.WALL;
        floorPlan[2][7] = FieldTileStatus.WALL;
        floorPlan[2][8] = FieldTileStatus.WALL;
        floorPlan[4][8] = FieldTileStatus.WALL;
        floorPlan[6][8] = FieldTileStatus.WALL;
        floorPlan[7][8] = FieldTileStatus.WALL;
        floorPlan[8][8] = FieldTileStatus.WALL;
        floorPlan[4][9] = FieldTileStatus.WALL;
        floorPlan[2][10] = FieldTileStatus.WALL;
        floorPlan[3][10] = FieldTileStatus.WALL;
        floorPlan[4][10] = FieldTileStatus.WALL;
        floorPlan[6][10] = FieldTileStatus.WALL;
        floorPlan[7][10] = FieldTileStatus.WALL;

    }

    public int getFoodCount() {
        return foodCount;
    }

    public Player getPlayer() {
        return player;
    }
}
