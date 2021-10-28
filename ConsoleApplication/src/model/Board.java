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
    private FieldTileStatus[][] foodPlan;
    private Player player;
    private List<Spook> spooks;
    private int foodCount;

    public Board(int playerLives, int amountOfSpooks) {
        this.floorPlan = new FieldTileStatus[10][13];
        this.foodPlan = new FieldTileStatus[floorPlan.length][floorPlan[0].length];
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
                else {
                    floorPlan[i][j] = FieldTileStatus.FOOD;
                }

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

    public void updateFloorplan(){
        updatePlayerPosition();
        updateSpookPosition();
        updateFoodCount();
        for (Spook spook : spooks) {
            System.out.println("spookPos = [" + spook.getyPos() + ", " + spook.getxPos() + "]");
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


    private void updateSpookPosition() {
        for (Spook spook : spooks) {
            List<FieldTileStatus> fieldTileStatusList = new ArrayList<FieldTileStatus>(spook.getPathHashMap().values());
            List<int[]> pathList = new ArrayList<int[]>(spook.getPathHashMap().keySet());
            int pos = pathList.size() - 2;
            floorPlan[pathList.get(pos)[0]][pathList.get(pos)[1]] = fieldTileStatusList.get(pos);
            if (floorPlan[pathList.get(pos)[0]][pathList.get(pos)[1]] == FieldTileStatus.LIVINGSPOOK){
                floorPlan[spook.getxPos()][spook.getyPos()] = fieldTileStatusList.get(pos - 1);
            }
            if (floorPlan[pathList.get(pos)[0]][pathList.get(pos)[1]] == FieldTileStatus.LIVINGPLAYER){
                floorPlan[pathList.get(pos)[0]][pathList.get(pos)[1]] = FieldTileStatus.FREE;
            }
            floorPlan[spook.getxPos()][spook.getyPos()] = FieldTileStatus.LIVINGSPOOK;
        }
    }

    private void updatePlayerPosition() {
        List<int[]> pathList = new ArrayList<int[]>(player.getPathHashMap().keySet());
        int pos = pathList.size() - 2;
        floorPlan[pathList.get(pos)[1]][pathList.get(pos)[0]] = FieldTileStatus.FREE;

        floorPlan[player.getyPos()][player.getxPos()] = FieldTileStatus.LIVINGPLAYER;

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


    public List<Spook> getSpooks() {
        return spooks;
    }


    public FieldTileStatus[][] getFloorPlan() {
        return floorPlan;
    }
}
