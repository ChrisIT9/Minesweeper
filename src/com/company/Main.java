package com.company;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Minesweeper myGame = new Minesweeper(6, 6);
        int row, column;
        char choice;
        Scanner scanner = new Scanner(System.in);

        System.out.println(myGame);

        while (myGame.getGameState() != GameState.OVER) {
            System.out.println("\nChe operazione vuoi eseguire?\n1) Inserisci\n2) Contrassegna\n3) Stampa griglia");
            choice = scanner.next().charAt(0);

            if (choice != '1' && choice != '2' && choice != '3')
                System.out.println("Scelta non valida.");
            else {
                if (choice == '1' || choice == '2') {
                    System.out.println("Inserisci riga e colonna: ");
                    row = scanner.nextInt();
                    column = scanner.nextInt();

                    if (choice == '1')
                        myGame.play(row, column);
                    else
                        myGame.flag(row, column);
                }

                System.out.println("\n" + myGame);
            }
        }

        System.out.println("Risultato: " + myGame.getGameResult());
    }
}
