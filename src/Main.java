import java.util.Iterator;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        /**
         * Arbre affiché
         */
        ARN<String> arbre;
        /**
         * Ce qui sera écrit par l'utilisateur
         */
        String input;
        /**
         * Condition pour que le programme tourne
         */
        boolean run = true;
        /**
         * Le mode de saisie (ajouter par défaut, si false = supprimer)
         */
        boolean ajouter = true;
        /**
         * Scanner pour stocker la saisie de l'utilisateur dans "input"
         */
        Scanner sc = new Scanner(System.in);
        arbre = new ARN<>();
        while(run) {
            System.out.println(arbre);
            if (ajouter) {
                System.out.println("**** AJOUTER UN ÉLÉMENT : (tapez ':s' pour passer vers le mode 'suppression' || ':q' pour quitter le programme) ****");
                input = sc.nextLine();
                if (input.equals(":s")) ajouter = false;
                else if (input.equals(":q")) return;
                else arbre.add(input);
            } else {
                System.out.println("**** SUPPRIMER UN ÉLÉMENT: (tapez ':a' pour passer vers le mode 'ajout' || ':q' pour quitter le programme) ****");
                input = sc.nextLine();
                if (input.equals(":a")) ajouter = true;
                else if (input.equals(":q")) return;
                else arbre.remove(input);
            }
        }
    }
}