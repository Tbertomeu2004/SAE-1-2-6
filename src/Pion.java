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
        while ((plate.pawnAt(pawn_x, pawn_y + counter) == null || (plate.pawnAt(pawn_x, pawn_y + counter) != null && !plate.pawnAt(pawn_x, pawn_y + counter).getVivant())) && pawn_y + counter < 8) {
            ArrayList<Integer> coordinates = new ArrayList<Integer>();
            coordinates.add(pawn_x);
            coordinates.add(pawn_y + counter);
            legalMoves.add(coordinates);
            counter++;
        }

        // Search South
        counter = 1;
        while ((plate.pawnAt(pawn_x, pawn_y - counter) == null || (plate.pawnAt(pawn_x, pawn_y - counter) != null && !plate.pawnAt(pawn_x, pawn_y - counter).getVivant())) && pawn_y - counter > 0) {
            ArrayList<Integer> coordinates = new ArrayList<Integer>();
            coordinates.add(pawn_x);
            coordinates.add(pawn_y - counter);
            legalMoves.add(coordinates);
            counter++;
        }

        // Search East
        counter = 1;
        while ((plate.pawnAt(pawn_x + counter, pawn_y) == null || (plate.pawnAt(pawn_x + counter, pawn_y) != null && !plate.pawnAt(pawn_x + counter, pawn_y).getVivant())) && pawn_x + counter < 8) {
            ArrayList<Integer> coordinates = new ArrayList<Integer>();
            coordinates.add(pawn_x + counter);
            coordinates.add(pawn_y);
            legalMoves.add(coordinates);
            counter++;
        }

        // Search West
        counter = 1;
        while ((plate.pawnAt(pawn_x - counter, pawn_y) == null || (plate.pawnAt(pawn_x - counter, pawn_y) != null && !plate.pawnAt(pawn_x - counter, pawn_y).getVivant())) && pawn_x - counter > 0) {
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

    public ArrayList<Pion> nearbyPawns(char player, Plateau plate) {
        ArrayList<Pion> nearbyPawns = new ArrayList<>();
        switch (player) {
            case 'A':
                if (plate.pawnAt(this.getX() + 1, this.getY()) instanceof Defenseur) {
                    nearbyPawns.add(plate.pawnAt(this.getX() + 1, this.getY()));
                }
                if (plate.pawnAt(this.getX() - 1, this.getY()) instanceof Defenseur){
                    nearbyPawns.add(plate.pawnAt(this.getX() - 1, this.getY()));
                }
                if (plate.pawnAt(this.getX(), this.getY() + 1) instanceof Defenseur) {
                    nearbyPawns.add(plate.pawnAt(this.getX(), this.getY() + 1));
                }
                if (plate.pawnAt(this.getX(), this.getY() - 1) instanceof Defenseur){
                    nearbyPawns.add(plate.pawnAt(this.getX(), this.getY() - 1));
                }
                break;
            case 'D':
                if (plate.pawnAt(this.getX() + 1, this.getY()) instanceof Attaquant) {
                    nearbyPawns.add(plate.pawnAt(this.getX() + 1, this.getY()));
                }
                if (plate.pawnAt(this.getX() - 1, this.getY()) instanceof Attaquant){
                    nearbyPawns.add(plate.pawnAt(this.getX() - 1, this.getY()));
                }
                if (plate.pawnAt(this.getX(), this.getY() + 1) instanceof Attaquant) {
                    nearbyPawns.add(plate.pawnAt(this.getX(), this.getY() + 1));
                }
                if (plate.pawnAt(this.getX(), this.getY() - 1) instanceof Attaquant){
                    nearbyPawns.add(plate.pawnAt(this.getX(), this.getY() - 1));
                }
                break;
        }
        return nearbyPawns;
    }

    /**
     * Returns the relative position of a pawn, considering it is next to this other pawn (else it will return false)
     * @return
     */
    public String relativePosition(Pion pawn) {
        if (this.getX() == pawn.getX() && this.getY() - pawn.getY() == - 1) {
            return "above";
        } else if (this.getX() == pawn.getX() && this.getY() - pawn.getY() == 1) {
            return "below";
        } else if (this.getY() == pawn.getY() && this.getX() - pawn.getX() == 1) {
            return "right";
        } else if (this.getY() == pawn.getY() && this.getX() - pawn.getX() == - 1) {
            return "left";
        } else {
            return null;
        }
    }

    /**
     * Returns true if current pawn is between pawn and another ennemy based on its relative position to the mentionned pawn
     * @return
     */
    public boolean betweenPawnAndOtherEnnemy(char player, Plateau plate, Pion pawn) {
        switch (player) {
            case 'A':
                switch (this.relativePosition(pawn)) {
                    case "above":
                        return plate.pawnAt(this.getX(), this.getY() - 1) instanceof Attaquant;
                    case "below":
                        return plate.pawnAt(this.getX(), this.getY() + 1) instanceof Attaquant;
                    case "left":
                        return plate.pawnAt(this.getX() - 1, this.getY()) instanceof Attaquant;
                    case "right":
                        return plate.pawnAt(this.getX() + 1, this.getY()) instanceof Attaquant;
                }
            case 'D':
                switch (this.relativePosition(pawn)) {
                    case "above":
                        return plate.pawnAt(this.getX(), this.getY() - 1) instanceof Defenseur;
                    case "below":
                        return plate.pawnAt(this.getX(), this.getY() + 1) instanceof Defenseur;
                    case "left":
                        return plate.pawnAt(this.getX() - 1, this.getY()) instanceof Defenseur;
                    case "right":
                        return plate.pawnAt(this.getX() + 1, this.getY()) instanceof Defenseur;
                }
        }
        return false;
    }

    /**
     * Returns true if player's pawn is between ennemy pawn and center
     * @return
     */
    public boolean betweenEmptyThroneAndEnnemy(char player, Plateau plate) {
        if (plate.pawnAt(4, 4) != null) {
            System.out.println("Throne occupied");
            return false;
        } else if (this.nearbyPawns(player, plate).size() == 0) {
            System.out.println("No pawn nearby");
            return false;
        }
        switch (player) {
            case 'D':
                System.out.println("Looking if Defender pawn is between ennemy and center");
                return (
                        (
                            ( plate.pawnAt(this.getX(), this.getY() - 1) instanceof Attaquant && (this.getX() == 4 && this.getY() + 1 == 4) )
                                ||
                            ( plate.pawnAt(this.getX(), this.getY() + 1) instanceof Attaquant && (this.getX() == 4 && this.getY() - 1 == 4) )
                        ) || (
                            ( plate.pawnAt(this.getX() - 1, this.getY()) instanceof Attaquant && (this.getX() + 1 == 4 && this.getY() == 4) )
                                ||
                            ( plate.pawnAt(this.getX() + 1, this.getY()) instanceof Attaquant && (this.getX() - 1 == 4 && this.getY() == 4) )
                        )
                );
            case 'A':
                System.out.println("Looking if Attacker pawn is between ennemy and center");
                return (
                        (
                                ( plate.pawnAt(this.getX(), this.getY() - 1) instanceof Defenseur && (this.getX() == 4 && this.getY() + 1 == 4) )
                                        ||
                                        ( plate.pawnAt(this.getX(), this.getY() + 1) instanceof Defenseur && (this.getX() == 4 && this.getY() - 1 == 4) )
                        ) || (
                                ( plate.pawnAt(this.getX() - 1, this.getY()) instanceof Defenseur && (this.getX() + 1 == 4 && this.getY() == 4) )
                                        ||
                                        ( plate.pawnAt(this.getX() + 1, this.getY()) instanceof Defenseur && (this.getX() - 1 == 4 && this.getY() == 4) )
                        )
                );
        }
        return false;
    }
}
