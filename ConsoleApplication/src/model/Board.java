package model;

import java.util.List;

/**
 * Arne Cools
 * 27/10/2021
 */
public class Board {
    private FieldCellStatus[][] floorPlan;
    private Player player;
    private List<Spook> spooks;
    private int foodCount;

    public Board() {
        this.floorPlan = new FieldCellStatus[10][13];
        this.player = new Player(1, 1);
        foodCount = 0;
    }

    public void createFloorPlan() {
        //Outside walls and corners
        for (int i = 0; i < floorPlan.length; i++) {
            for (int j = 0; j < floorPlan[i].length; j++) {
                if (i == 0 || i == floorPlan.length - 1) floorPlan[i][j] = FieldCellStatus.WALL;
                else if (j == 0 || j == floorPlan[i].length - 1) floorPlan[i][j] = FieldCellStatus.WALL;
                else floorPlan[i][j] = FieldCellStatus.FOOD;

            }
        }
        floorPlan[0][0] = FieldCellStatus.WALL;
        floorPlan[floorPlan.length - 1][0] = FieldCellStatus.WALL;
        floorPlan[0][floorPlan[floorPlan.length - 1].length - 1] = FieldCellStatus.WALL;
        floorPlan[floorPlan.length - 1][floorPlan[floorPlan.length - 1].length - 1] = FieldCellStatus.WALL;

        //Layout: inside the field
        innerFieldLayout();

        floorPlan[player.getyPos()][player.getxPos()] = FieldCellStatus.PLAYER;

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

    public void updateFoodCount(){
        int count = 0;
        for (int i = 0; i < floorPlan.length; i++) {
            for (int j = 0; j < floorPlan[i].length; j++) {
                if (floorPlan[i][j] == FieldCellStatus.FOOD) count++;
            }
        }
        foodCount = count;
    }

    public void movePlayer(char move){
//        floorPlan[player.getyPos()][player.getxPos()] = FieldCellStatus.FREE;
        int newXpos = player.getxPos();
        int newYpos = player.getyPos();
        switch(move){
            case 'w': newYpos = player.getyPos() - 1; newXpos = player.getxPos(); break;
            case 'a': newXpos = player.getxPos() - 1; newYpos = player.getyPos(); break;
            case 's': newYpos = player.getyPos() + 1; newXpos = player.getxPos(); break;
            case 'd': newXpos = player.getxPos() + 1; newYpos = player.getyPos(); break;

        }
        if (floorPlan[newYpos][newXpos] != FieldCellStatus.WALL){
            if (floorPlan[newYpos][newXpos] == FieldCellStatus.FOOD) foodCount--;
            floorPlan[player.getyPos()][player.getxPos()] = FieldCellStatus.FREE;
            player.setxPos(newXpos);
            player.setyPos(newYpos);
            floorPlan[player.getyPos()][player.getxPos()] = FieldCellStatus.PLAYER;
        }
        updateFoodCount();

    }

    public void innerFieldLayout() { //TODO get rid of hardcode
        floorPlan[2][2] = FieldCellStatus.WALL;
        floorPlan[3][2] = FieldCellStatus.WALL;
        floorPlan[5][2] = FieldCellStatus.WALL;
        floorPlan[6][2] = FieldCellStatus.WALL;
        floorPlan[7][2] = FieldCellStatus.WALL;
        floorPlan[5][3] = FieldCellStatus.WALL;
        floorPlan[1][4] = FieldCellStatus.WALL;
        floorPlan[2][4] = FieldCellStatus.WALL;
        floorPlan[3][4] = FieldCellStatus.WALL;
        floorPlan[5][4] = FieldCellStatus.WALL;
        floorPlan[7][4] = FieldCellStatus.WALL;
        floorPlan[7][5] = FieldCellStatus.WALL;
        floorPlan[2][6] = FieldCellStatus.WALL;
        floorPlan[3][6] = FieldCellStatus.WALL;
        floorPlan[4][6] = FieldCellStatus.WALL;
        floorPlan[5][6] = FieldCellStatus.WALL;
        floorPlan[6][6] = FieldCellStatus.WALL;
        floorPlan[7][6] = FieldCellStatus.WALL;
        floorPlan[2][7] = FieldCellStatus.WALL;
        floorPlan[2][8] = FieldCellStatus.WALL;
        floorPlan[4][8] = FieldCellStatus.WALL;
        floorPlan[6][8] = FieldCellStatus.WALL;
        floorPlan[7][8] = FieldCellStatus.WALL;
        floorPlan[8][8] = FieldCellStatus.WALL;
        floorPlan[4][9] = FieldCellStatus.WALL;
        floorPlan[2][10] = FieldCellStatus.WALL;
        floorPlan[3][10] = FieldCellStatus.WALL;
        floorPlan[4][10] = FieldCellStatus.WALL;
        floorPlan[6][10] = FieldCellStatus.WALL;
        floorPlan[7][10] = FieldCellStatus.WALL;

    }

    public int getFoodCount() {
        return foodCount;
    }

    public Player getPlayer() {
        return player;
    }
}
