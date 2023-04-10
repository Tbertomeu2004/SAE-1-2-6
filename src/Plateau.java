import java.util.ArrayList;
import java.util.List;

public class Plateau {

    int board_size = 7;
    ArrayList<Pion> pions;

    public Plateau() {}

    public List<Pion> initGame() {
        pions = new ArrayList<>();
        int center = board_size/2 + 1;

        Branan branan = new Branan(center, center, true);

        Defenseur d1 = new Defenseur(center+1, center, true);
        Defenseur d2 = new Defenseur(center-1, center, true);
        Defenseur d3 = new Defenseur(center, center+1, true);
        Defenseur d4 = new Defenseur(center, center-1, true);

        Attaquant a1 = new Attaquant(center+2, center, true);
        Attaquant a2 = new Attaquant(center+3, center, true);
        Attaquant a3 = new Attaquant(center-2, center, true);
        Attaquant a4 = new Attaquant(center-3, center, true);
        Attaquant a5 = new Attaquant(center, center+2, true);
        Attaquant a6 = new Attaquant(center, center+3, true);
        Attaquant a7 = new Attaquant(center, center-2, true);
        Attaquant a8 = new Attaquant(center, center-3, true);

        pions.add(branan);

        pions.add(d1);
        pions.add(d2);
        pions.add(d3);
        pions.add(d4);

        pions.add(a1);
        pions.add(a2);
        pions.add(a3);
        pions.add(a4);
        pions.add(a5);
        pions.add(a6);
        pions.add(a7);
        pions.add(a8);

        return pions;
    }

    public void show() {
        for (Pion pion : pions) {
            System.out.println(pion.x + ", " + pion.y + ", " + pion.vivant);
        }
    }

    public boolean brananInCorner() {
        Pion branan = getBranan();
        return branan.getX() == 1 && branan.getY() == 1
                || branan.getX() == 7 && branan.getY() == 1
                || branan.getX() == 7 && branan.getY() == 7
                || branan.getX() == 1 && branan.getY() == 7;
    }

    public boolean brananBetweenEnnemies() {
        Pion branan = getBranan();
        return this.pawnAt(branan.getX() + 1, branan.getY()) instanceof Attaquant
                && this.pawnAt(branan.getX() - 1, branan.getY()) instanceof Attaquant
                && this.pawnAt(branan.getX(), branan.getY() + 1) instanceof Attaquant
                && this.pawnAt(branan.getX(), branan.getY() - 1) instanceof Attaquant;
    }

    public Pion getBranan() {
        for (Pion pion : pions) {
            if (pion instanceof Branan) {
                return pion;
            }
        }
        return null;
    }

    public Pion pawnAt(int x, int y) {
        for (Pion pion : pions) {
            if (pion.getX() == x && pion.getY() == y) {
                return pion;
            }
        }
        return null;
    }


    public void displayBoard() {
        System.out.println("   1 2 3 4 5 6 7");
        for (int i = 0; i < board_size; i++) {
            System.out.print(i+1 + "  ");
            for (int j = 0; j < board_size; j++) {
                if (pawnAt(j+1, i+1) == null || !pawnAt(j+1, i+1).getVivant()) {
                    System.out.print('.');
                } else if ((pawnAt(j+1, i+1) instanceof Attaquant) && (pawnAt(j + 1, i + 1).getVivant())) {
                    System.out.print('A');
                } else if (pawnAt(j+1, i+1) instanceof Defenseur && (pawnAt(j + 1, i + 1).getVivant())) {
                    if (pawnAt(j+1, i+1) instanceof Branan) {
                        System.out.print('B');
                    } else {
                        System.out.print('D');
                    }
                }
                System.out.print(' ');
            }
            System.out.println();
        }
    }
}
