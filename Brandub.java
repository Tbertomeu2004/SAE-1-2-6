import java.util.*;

import static java.lang.Integer.parseInt;

public class Brandub {

    public static void main(String[] args) {

        final Scanner input = new Scanner(System.in);

        Plateau plate = new Plateau();
        plate.initGame();

        int round = 0;
        char current_player = 'A';
        char next_player = 'D';
        char tmp;

        String source;
        String dest;
        int source_x;
        int source_y;
        int dest_x;
        int dest_y;

        do {
            round++;
            // While inputed values are not allowed
            do {
                System.out.println("Tour " + round);

                // Ask player input
                do {
                    plate.displayBoard();
                    System.out.printf("Source : ");
                    source = input.nextLine();
                    System.out.printf("Destination : ");
                    dest = input.nextLine();
                    System.out.println("Input Source : " + checkSource(source, plate));
                    System.out.println("Input Destination : " + checkDestination(dest));
                } while (!checkSource(source, plate) || !checkDestination(dest));

                source_x = Integer.parseInt(String.valueOf(source.charAt(0)));
                source_y = Integer.parseInt(String.valueOf(source.charAt(1)));
                dest_x = Integer.parseInt(String.valueOf(dest.charAt(0)));
                dest_y = Integer.parseInt(String.valueOf(dest.charAt(1)));
                System.out.println("Move possible : " + plate.pawnAt(source_x, source_y).checkMove(dest_x, dest_y, plate, current_player));
            } while (!plate.pawnAt(source_x, source_y).checkMove(dest_x, dest_y, plate, current_player));

            // Move pawn
            plate.pawnAt(source_x, source_y).move(dest_x, dest_y);

            // Check if any pawn has been captured
            for (int i = 0; i < plate.pions.size(); i++) {
                if (plate.pions.get(i).captured(plate, current_player)) {
                    System.out.println("Pawn captured : " + plate.pions.get(i).captured(plate, current_player));
                    plate.pions.get(i).setVivant(false);
                }
            }
            if (plate.brananBetweenEnnemies()) {
                plate.getBranan().setVivant(false);
            }

            // Switch player
            tmp = current_player;
            current_player = next_player;
            next_player = tmp;
            System.out.println("Branan between ennemies : " + plate.brananBetweenEnnemies());
        } while (plate.getBranan().getVivant() || !plate.brananInCorner());
    }

    public static boolean checkSource(String source, Plateau plate) {
        // Check if inputs are numeric with accepted values
        if (!isAcceptedNumeric(source.charAt(0)) || !isAcceptedNumeric(source.charAt(1))) {
            return false;
        }
        // Check if pawn at source exists
        int source_x = Integer.parseInt(String.valueOf(source.charAt(0)));
        int source_y = Integer.parseInt(String.valueOf(source.charAt(1)));
        if (plate.pawnAt(source_x, source_y) == null) {
            return false;
        }
        return true;
    }

    public static boolean checkDestination(String dest) {
        // Check if inputs are numeric with accepted values
        return isAcceptedNumeric(dest.charAt(0)) && isAcceptedNumeric(dest.charAt(1));
    }

    private static boolean isAcceptedNumeric(char c) {
        return c > '0' && c < '8';
    }

}

// javac Brandub.java Plateau.java Branan.java Defenseur.java Attaquant.java Pion.java
