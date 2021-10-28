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
        playingBoard = new Board(3,1);
        sc = new Scanner(System.in);

    }

    public void run() {
        playingBoard.createFloorPlan();
        playingBoard.draw();
        char move;
        while (isPlayerAlive() && !isWon()) {
            System.out.println("Food left: " + playingBoard.getFoodCount());
            System.out.print("\nNext move: ");
            move = sc.next().charAt(0);
            playingBoard.movePieces(move);
            playingBoard.draw();

        }
    }

    private boolean isPlayerAlive() {
        //TODO make player die
        return true;
    }

    private boolean isWon(){
        return playingBoard.getFoodCount() <= 0;
    }


}
