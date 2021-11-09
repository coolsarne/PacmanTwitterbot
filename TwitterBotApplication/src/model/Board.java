package model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Arne Cools
 * 27/10/2021
 */
public class Board {
    private FieldTileStatus[][] floorPlan;
    private Player player;
    private List<Ghost> ghosts;
    private int foodEaten;
    private int totalFood;

    public Board(int playerLives, int amountOfGhosts) {
        this.floorPlan = new FieldTileStatus[10][13];
        this.player = new Player(1, 1, playerLives);
        ghosts = new ArrayList<>();
        int foodEaten = 0;
        if (amountOfGhosts > 3) amountOfGhosts = 3;
        if (amountOfGhosts < 0) amountOfGhosts = 0;
        switch (amountOfGhosts) {
            case 3:
                ghosts.add(new Ghost(new int[]{1, floorPlan[1].length - 2}, 1));
            case 2:
                ghosts.add(new Ghost(new int[]{floorPlan.length - 2, 1}, 1));

            case 1:
                ghosts.add(new Ghost(new int[]{floorPlan.length - 2, floorPlan[floorPlan.length - 2].length - 2}, 1));

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
        for (Ghost ghost : ghosts) {
            floorPlan[ghost.getxPos()][ghost.getyPos()] = FieldTileStatus.LIVINGGHOST;
        }

    }

    public String draw() {
        StringBuilder sb = new StringBuilder();
        sb.append("⭐️" + player.getScore() + "\n");
        for (int i = 0; i < floorPlan.length; i++) {
            for (int j = 0; j < floorPlan[i].length; j++) {
                switch (floorPlan[i][j]) {
                    case FOOD:
                        sb.append("✳️");
                        break;
                    case FREE:
                        sb.append("\uD83D\uDFE9");
                        break;

                    case LIVINGPLAYER:
                        sb.append("\uD83D\uDE0B");
                        break;

                    case WALL:
                        sb.append("⬛️");
                        break;

                    case DEADPLAYER: {
                        sb.append("☠️");
                        break;
                    }
                    case LIVINGGHOST:
                        sb.append("\uD83D\uDC7B");
                        break;
                }
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    public void update() {
        int oldFoodEaten = getFoodEaten();
        updatePlayerPosition();
        updateGhostPosition();
        collisionDetectionPlayerGhost();
        //now we update the players score

        player.setScore(player.getScore() + (foodEaten - oldFoodEaten) * 100);
        if (foodEaten - oldFoodEaten == 0) {
            player.setScore(player.getScore() - 10);
        }
    }


    public void updateGhostPosition() {
        for (Ghost ghost : ghosts) {
            List<FieldTileStatus> fieldTileStatusList = new ArrayList<>(ghost.getPathHashMap().values());
            List<int[]> pathList = new ArrayList<>(ghost.getPathHashMap().keySet());
            int previousPosistion = pathList.size() - 2;
            floorPlan[pathList.get(previousPosistion)[0]][pathList.get(previousPosistion)[1]] = fieldTileStatusList.get(previousPosistion);
            if (floorPlan[pathList.get(previousPosistion)[0]][pathList.get(previousPosistion)[1]] == FieldTileStatus.LIVINGGHOST) {
                floorPlan[ghost.getxPos()][ghost.getyPos()] = fieldTileStatusList.get(previousPosistion - 1);
            }
            if (floorPlan[pathList.get(previousPosistion)[0]][pathList.get(previousPosistion)[1]] == FieldTileStatus.LIVINGPLAYER) {
                floorPlan[pathList.get(previousPosistion)[0]][pathList.get(previousPosistion)[1]] = FieldTileStatus.FREE;
            }
            floorPlan[ghost.getxPos()][ghost.getyPos()] = FieldTileStatus.LIVINGGHOST;


        }
    }

    private void updatePlayerPosition() {
        List<int[]> pathList = new ArrayList<>(player.getPathHashMap().keySet());
        int pos = pathList.size() - 2;
        if (player.getLastEaten().equals(FieldTileStatus.FOOD)) {
            foodEaten++;
        }
        floorPlan[pathList.get(pos)[1]][pathList.get(pos)[0]] = FieldTileStatus.FREE;

        floorPlan[player.getyPos()][player.getxPos()] = FieldTileStatus.LIVINGPLAYER;

    }

    private void collisionDetectionPlayerGhost() {
        boolean playerFound = false;
        for (int i = 0; i < floorPlan.length; i++) {
            for (int j = 0; j < floorPlan[i].length; j++) {
                if (floorPlan[i][j].equals(FieldTileStatus.LIVINGPLAYER)) playerFound = true;
            }
        }
        if (!playerFound) {
            player.setAlive(false);
            List<int[]> pathList = new ArrayList<>(player.getPathHashMap().keySet());
            floorPlan[pathList.get(pathList.size() - 1)[1]][pathList.get(pathList.size() - 1)[0]] = FieldTileStatus.DEADPLAYER;
        }
    }

    public boolean isWon(int bonus) {
        boolean won = getFoodEaten() >= getTotalFood();
        if (won) player.setScore(player.getScore() + bonus);
        return won;
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
        totalFood = 11 * 8 - 30 - 1;

    }

    public int getFoodEaten() {
        return foodEaten;
    }

    public int getTotalFood() {
        return totalFood;
    }

    public Player getPlayer() {
        return player;
    }


    public List<Ghost> getGhosts() {
        return ghosts;
    }


    public FieldTileStatus[][] getFloorPlan() {
        return floorPlan;
    }
}
