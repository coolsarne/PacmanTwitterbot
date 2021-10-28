package model;

import java.util.Scanner;

/**
 * Arne Cools
 * 27/10/2021
 */
public class PacMan {
    private Board playingBoard;
    private Scanner sc;


    public PacMan() {
        playingBoard = new Board(3,3);
        sc = new Scanner(System.in);

    }

    public void run() {
        playingBoard.createFloorPlan();
        playingBoard.draw();
        char move;
        while (playingBoard.getPlayer().isAlive() && !playingBoard.isWon(0)) {
            System.out.print("\nNext move: ");
            move = sc.next().charAt(0);
            playingBoard.getPlayer().move(playingBoard.getFloorPlan(), move);
            for (Ghost ghost : playingBoard.getGhosts()) {
                ghost.move(playingBoard.getFloorPlan(), playingBoard.getPlayer().getxPos(), playingBoard.getPlayer().getyPos());
                playingBoard.updateGhostPosition();
            }
            playingBoard.update();
            playingBoard.draw();
        }
        if (!playingBoard.getPlayer().isAlive()) System.out.println("You lost :(");
        if (playingBoard.isWon(1000)) System.out.println("Congratulations! You Won!");
        System.out.println("Total score: " + playingBoard.getPlayer().getScore());


    }




}
