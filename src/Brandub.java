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
                // Ask player input
                do {
                    System.out.println("Tour " + round);
                    System.out.println("Joueur " + current_player);
                    plate.displayBoard();
                    System.out.printf("Source (XY) : ");
                    source = input.nextLine();
                    System.out.printf("Destination (XY) : ");
                    dest = input.nextLine();
                } while (!checkSource(source, plate) || !checkDestination(dest));

                source_x = Integer.parseInt(String.valueOf(source.charAt(0)));
                source_y = Integer.parseInt(String.valueOf(source.charAt(1)));
                dest_x = Integer.parseInt(String.valueOf(dest.charAt(0)));
                dest_y = Integer.parseInt(String.valueOf(dest.charAt(1)));
            } while (!plate.pawnAt(source_x, source_y).checkMove(dest_x, dest_y, plate, current_player));

            // Move pawn
            plate.pawnAt(source_x, source_y).move(dest_x, dest_y);
            for (int i = 0; i < plate.pawnAt(dest_x, dest_y).nearbyPawns(current_player, plate).size(); i++) {
                if (plate.pawnAt(dest_x, dest_y).nearbyPawns(current_player, plate).get(i).betweenPawnAndOtherEnnemy(current_player, plate, plate.pawnAt(dest_x, dest_y))) {
                    plate.pawnAt(dest_x, dest_y).nearbyPawns(current_player, plate).get(i).setVivant(false);
                    System.out.println("Pion du joueur " + next_player + " capturé");
                } else if (plate.pawnAt(dest_x, dest_y).nearbyPawns(current_player, plate).get(i).betweenEmptyThroneAndEnnemy(next_player, plate)) {
                    plate.pawnAt(dest_x, dest_y).nearbyPawns(current_player, plate).get(i).setVivant(false);
                    System.out.println("Pion du joueur " + next_player + " capturé");
                }
            }

            if (plate.brananBetweenEnnemies()) {
                System.out.println("Branan capturé");
                plate.getBranan().setVivant(false);
            }

            // Switch player
            tmp = current_player;
            current_player = next_player;
            next_player = tmp;
        } while (plate.getBranan().getVivant() && !plate.brananInCorner());
        System.out.println("Partie terminée (tour " + (round-1) + ")");
        System.out.println("Gagnant: joueur " + next_player);
        plate.displayBoard();
    }

    public static boolean checkSource(String source, Plateau plate) {
        if (source.length() != 2) {
            System.out.println("La source doit contenir 2 charactères");
            return false;
        }
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
        if (dest.length() != 2) {
            System.out.println("La destination doit contenir 2 charactères");
            return false;
        }
        // Check if inputs are numeric with accepted values
        return isAcceptedNumeric(dest.charAt(0)) && isAcceptedNumeric(dest.charAt(1));
    }

    private static boolean isAcceptedNumeric(char c) {
        return c > '0' && c < '8';
    }

}

// javac Brandub.java Plateau.java Branan.java Defenseur.java Attaquant.java Pion.java
