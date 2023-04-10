import java.util.ArrayList;
import java.util.Arrays;

public class Pion {

    int x;
    int y;
    boolean vivant;

    // CONSTRUCTORS

    /**
     * Create an empty instance of a pawn
     */
    public Pion() {}

    /**
     * Create an instance of a pawn with given parameters
     */
    public Pion(int x, int y, boolean vivant) {
        this.x = x;
        this.y = y;
        this.vivant = vivant;
    }

    // GETTERS

    /**
     * Return the cuurent X coordinate of the pawn
     * @return x
     */
    public int getX() {
        return x;
    }

    /**
     * Return the current Y coordinate of the pawn
     * @return y
     */
    public int getY() {
        return y;
    }

    /**
     * Return true if the pawn is alive
     * @return vivant
     */
    public boolean getVivant() {
        return vivant;
    }

    // SETTERS

    /**
     * Set the X coordinate of the pawn
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Set the Y coordinate of the pawn
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Set the state of the pawn to 1 (alive) or 0 (dead)
     */
    public void setVivant(boolean vivant) {
        this.vivant = vivant;
    }

    // GAME

    /**
     * Move the pawn to coordinates X and Y
     */
    public void move(int x, int y) {
        this.setX(x);
        this.setY(y);
    }

    /**
     * Searches for all legal moves playable by the current pawn
     * Legal moves are all positions:
     * - reachable in any cardinal direction
     * - reachable without juming over a pawn
     * - rechable under certain other circumstances:
     *      - only the Branan can stop on the central square
     *      - the Branan can only move one square at a time
     * @return list of all legal moves for the current pawn
     */
    public ArrayList<ArrayList<Integer>> legalMoves(Plateau plate) {
        ArrayList<ArrayList<Integer>> legalMoves = new ArrayList<ArrayList<Integer>>();

        int pawn_x = this.getX();
        int pawn_y = this.getY();

        // Search North
        int counter = 1;
        while (plate.pawnAt(pawn_x, pawn_y + counter) == null && pawn_y + counter < 8) {
            ArrayList<Integer> coordinates = new ArrayList<Integer>();
            coordinates.add(pawn_x);
            coordinates.add(pawn_y + counter);
            legalMoves.add(coordinates);
            counter++;
        }

        // Search South
        counter = 1;
        while (plate.pawnAt(pawn_x, pawn_y - counter) == null && pawn_y - counter > 0) {
            ArrayList<Integer> coordinates = new ArrayList<Integer>();
            coordinates.add(pawn_x);
            coordinates.add(pawn_y - counter);
            legalMoves.add(coordinates);
            counter++;
        }

        // Search East
        counter = 1;
        while (plate.pawnAt(pawn_x + counter, pawn_y) == null && pawn_x + counter < 8) {
            ArrayList<Integer> coordinates = new ArrayList<Integer>();
            coordinates.add(pawn_x + counter);
            coordinates.add(pawn_y);
            legalMoves.add(coordinates);
            counter++;
        }

        // Search West
        counter = 1;
        while (plate.pawnAt(pawn_x - counter, pawn_y) == null && pawn_x - counter > 0) {
            ArrayList<Integer> coordinates = new ArrayList<Integer>();
            coordinates.add(pawn_x - counter);
            coordinates.add(pawn_y);
            legalMoves.add(coordinates);
            counter++;
        }

        // Remove center square if pawn not Branan
        if (!(this instanceof Branan)) {
            if ((x != 4 || y != 4) && legalMoves.contains(Arrays.asList(4, 4))) {
                legalMoves.remove(Arrays.asList(4, 4));
            }
        }

        return legalMoves;
    }

    public boolean checkMove(int dest_x, int dest_y, Plateau plate, char player) {
        int pawn_x = this.getX();
        int pawn_y = this.getY();
        if (plate.pawnAt(pawn_x, pawn_y) == null) {
            return false;
        }
        // Check if pawn belongs to player
        switch (player) {
            case 'A':
                if (!(this instanceof Attaquant)) {
                    return false;
                }
                break;
            case 'D':
                if (!(this instanceof Defenseur)) {
                    return false;
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + player);
        }
        // Check if destination is part of legal moves
        ArrayList<ArrayList<Integer>> legalMoves = plate.pawnAt(pawn_x, pawn_y).legalMoves(plate);
        if (legalMoves == null || !(legalMoves.contains(Arrays.asList(dest_x, dest_y)))) {
            return false;
        }
        return true;
    }

    // TODO: Check if between pawn and center
    public boolean pawnBetweenEnemies(Plateau plate) {
        if (this instanceof Defenseur) {
            return (plate.pawnAt(this.getX() + 1, this.getY()) instanceof Attaquant
                    && plate.pawnAt(this.getX() - 1, this.getY()) instanceof Attaquant)
                    || (plate.pawnAt(this.getX(), this.getY() + 1) instanceof Attaquant
                    && plate.pawnAt(this.getX(), this.getY() - 1) instanceof Attaquant);
        } else if (this instanceof Attaquant) {
            return (plate.pawnAt(this.getX()+1, this.getY()) instanceof Defenseur
                    && plate.pawnAt(this.getX()-1, this.getY()) instanceof Defenseur)
                    || (plate.pawnAt(this.getX(), this.getY()+1) instanceof Defenseur
                    && plate.pawnAt(this.getX(), this.getY()-1) instanceof Defenseur);
        } else {
            return false;
        }
    }

    public boolean captured(Plateau plate, char current_player) {
        return switch (current_player) {
            case 'A' -> !(this instanceof Branan) && this instanceof Defenseur && this.pawnBetweenEnemies(plate) && this.getVivant();
            case 'D' -> this instanceof Attaquant && this.pawnBetweenEnemies(plate) && this.getVivant();
            default -> throw new IllegalStateException("Unexpected value: " + current_player);
        };
    }
}
