import java.util.Iterator;
import java.util.Scanner;

public class Main {

    public static void main2(String[] args) {
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

    public static void sautDeLigne() {
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("");
    }

    public static void main(String[] args) {
        ARN<Integer> arb = new ARN<>();
        arb.add(34);
        arb.add(51);
        arb.add(60);
        arb.add(38);
        arb.add(40);
        arb.add(56);
        arb.add(23);
        arb.add(78);
        arb.add(53);
        arb.add(52);
        arb.add(54);


        System.out.println(arb);

        sautDeLigne();


        arb.remove(38);
        arb.remove(23);
        arb.remove(34);

        System.out.println(arb);
        sautDeLigne();

        Iterator<Integer> it = arb.iterator();

        while (it.hasNext()) {
            System.out.println(it.next() + " ");
        }

        System.out.println();

        sautDeLigne();


    }
}