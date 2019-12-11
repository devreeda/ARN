import java.util.Scanner;

public class Main {


    public static void main2(String[] args) {
        ARN<String> a;
        String nb;
        String input;
        boolean b = true;
        Scanner sc = new Scanner(System.in);

        a = new ARN<String>();

        while(b) {
            System.out.println(a);
            //System.out.print(ANSI_BLACK+"a - ajouter | s - supprimer\n> "+ANSI_RESET);
            input = sc.nextLine();
            System.out.print("Nombre\n> ");
            nb = sc.nextLine();
            //sc.nextLine();
            switch(input) {
                case "s":
                    a.remove(nb);
                    break;
                case "a":
                    a.add(nb);
                    break;
                default:
                    b = false;
            }
        }

        System.out.println(a.size());
        for(String s: a) System.out.println(s);

        sc.close();
    }

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
                System.out.println("Ajouter un élément: ");
                input = sc.nextLine();
                System.out.println("********** switchez vers le mode supprimer en tapant ':s' || quittez le programme en tapant ':q' **********");
                if (input.equals(":s")) ajouter = false;
                else if (input.equals(":q")) return;
                else a.add(input);
            } else {
                System.out.println("Supprimer un élément: ");
                input = sc.nextLine();
                System.out.println("********** switchez vers le mode ajouter en tapant ':a' || quittez le programme en tapant ':q' **********");
                if (input.equals(":a")) ajouter = true;
                else if (input.equals(":q")) return;
                else a.remove(input);
            }
        }
    }
}