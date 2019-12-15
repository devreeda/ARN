import java.util.Iterator;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        ARN<String> a;
        String input;
        boolean b = true;
        boolean ajouter = true;
        Scanner sc = new Scanner(System.in);
        a = new ARN<>();
        while(b) {
            System.out.println(a);
            if (ajouter) {
                System.out.println("**** AJOUTER UN ÉLÉMENT : (tapez ':s' pour passer vers le mode 'suppression' || ':q' pour quitter le programme) ****");
                input = sc.nextLine();
                if (input.equals(":s")) ajouter = false;
                else if (input.equals(":q")) return;
                else a.add(input);
            } else {
                System.out.println("**** SUPPRIMER UN ÉLÉMENT: (tapez ':a' pour passer vers le mode 'ajout' || ':q' pour quitter le programme) ****");
                input = sc.nextLine();
                if (input.equals(":a")) ajouter = true;
                else if (input.equals(":q")) return;
                else a.remove(input);
            }
        }
    }
}