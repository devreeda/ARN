import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        ABR<String> a;
        String nb;
        String input;
        boolean b = true;
        Scanner sc = new Scanner(System.in);

        a = new ABR<String>();

        while(b) {
            System.out.println(a);
            System.out.print("a - ajouter | s - supprimer\n> ");
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
}