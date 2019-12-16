import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <h1>IMPLANTATION DE L'ARBRE ROUGE ET NOIR</h1>
 * <h3>Par GHOUTI TERKI Rida et PERONNY Thomas</h3>
 * <p>
 * Implantation de l'interface Collection basée sur les arbres rouge et noirs.
 * Les éléments sont ordonnés soit en utilisant l'ordre naturel (cf
 * Comparable) soit avec un Comparator fourni à la création.
 * </p>
 *
 * @param <E> le type des clés stockées dans l'arbre
 */
public class ARN<E> extends AbstractCollection<E> {
    /**
     * Permettent de colorier en rouge les noeuds correspondants.
     */
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";

    /**
     * Taille de l'arbre
     */
    private int taille;
    /**
     * Racine de l'arbre, l'élément le plus à gauche dans la représentation,
     * le "père de tous les noeuds"
     */
    private Noeud racine;
    /**
     * Permet de comparer deux clés des noeuds afin de les classer dans l'arbre
     */
    private Comparator<? super E> cmp;
    /**
     * Correspond aux feuilles de l'arbre, elles comblent les "trous" afin de que
     * chaque noeud possède bien deux fils
     */
    private Noeud sentinelle;

    /**
     * La classe Noeud représente les éléments de l'arbre, ils possèdent une clé qui
     * permet de les classer dans l'arbre, un fils gauche et droit, un père et une
     * couleur, qui est spécifique  aux arbre rouge-noir et qui va permettre d'équilibrer
     * l'arbre à l'aide de règles qui répartissent réorganisent les noeuds en gardant
     * ses propriétés d'arbre binaire.
     */
    private class Noeud {

        /**
         * Cle du noeud qui permet l'insertion, la suppression ou la recherche de l'élément.
         */
        E cle;
        /**
         * Fils gauche du noeud
         */
        Noeud gauche;
        /**
         * Fils droit du noeud
         */
        Noeud droit;
        /**
         * Père du noeud
         */
        Noeud pere;
        /**
         * Couleur du noeud (rouge ou noir)
         */
        char couleur;

        /**
         * Constructeur par la clé, celui que l'on utilisera pour insérer un noeud
         * @param cle Clé du noeud
         */
        Noeud(E cle) {
            pere = droit = gauche = sentinelle;
            this.cle = cle;
            this.couleur = 'N';
        }

        /**
         * Donne l'élément avec la clé maximale
         * @return Le noeud ayant la clé maximale
         */
        Noeud maximum() {
            Noeud n = this;
            while (n.droit != sentinelle) n = n.droit;
            return n;
        }

        /**
         * Donne l'élément avec la clé minimale
         * @return Le noeud ayant la clé minimale
         */
        Noeud minimum() {
            Noeud n = this;
            while (n.gauche != sentinelle) n = n.gauche;
            return n;
        }

        /**
         * Donne le noeud qui suit la clé de this / du Noeud appelant
         * @return Le noeud suivant
         */
        Noeud suivant() {
            Noeud x = this;
            if (droit != sentinelle) return droit.minimum();
            Noeud y = x.pere;
            while (y != sentinelle && x == y.droit) {
                x=y;
                y=y.pere;
            }
            return y;
        }

        /**
         * Affiche la clé
         * @return un String de la clé
         */
        @Override
        public String toString() {
            return cle.toString();
        }
    }

    /**
     * La classe Iterateur de l'arbre pour pouvoir le parcourir
     */
    private class ABRIterator implements Iterator<E> {
        /**
         * Noeud courant
         */
        Noeud n;
        /**
         * Noeud suivant
         */
        Noeud s;

        public ABRIterator() {
            super();
            n = sentinelle;
            if (racine != sentinelle) s = racine.minimum();
            else s = sentinelle;
        }

        /**
         * Vérifie si le Noeud possède un successeur
         * @return retourne le successeur ou la sentinelle par défaut
         */
        @Override
        public boolean hasNext() {
            return s != sentinelle;
        }

        /**
         * Surcharge de la méthode next de l'interface Iterator. Appelle la méthode suivant de la classe Noeud.
         * @return L'élément suivant si il existe, une exception sinon
         */
        @Override
        public E next() {
            if (s == null) throw new NoSuchElementException();
            n = s;
            s = s.suivant();
            return n.cle;
        }

        /**
         * Surcharge de la méthode remove de l'interface Iterator.
         * Appelle la méthode supprimer de la classe Noeud, renvoie une exception si l'élément est null.
         */
        @Override
        public void remove() {
            if (n == null) throw new IllegalStateException();
            supprimer(n);
            n = null;
        }
    }

    /**
     * Constructueur par défaut, construit un arbre avec une sentinelle comme racine
     */
    public ARN() {
        sentinelle = new Noeud((E)"nil");
        racine = sentinelle;
        cmp = (x, y) -> ((Comparable<E>) x).compareTo(y);
        taille = 0;
    }

    /**
     * Constructeur par comparator, si on veut personnaliser la relation d'ordre
     * @param cmp comparateur
     */
    public ARN(Comparator<? super E> cmp) {
        sentinelle = new Noeud(null);
        racine = sentinelle;
        taille = 0;
        this.cmp = cmp;
    }

    /**
     * Constructeur avec une collection d'éléments en paramètre, que l'on ajoute dans l'arbre en tant que clé
     * @param c collection d'élément
     */
    public ARN(Collection<? extends E> c) {
        this();
        for (E e : c) this.add(e);
    }

    /**
     * Surcharge de la méthode iterator()
     * @return renvoie une instance de l'interface ABRIterator
     */
    @Override
    public Iterator<E> iterator() {
        return new ABRIterator();
    }

    /**
     *
     * @return Retourne la taille de l'arbre
     */
    @Override
    public int size() {
        return taille;
    }

    /**
     * Recherche un Noeud selon la clé
     * @param k la clé
     * @return Le noeud si la clé existe, sinon la sentinelle
     */
    private Noeud rechercher(E k) {
        Noeud x;

        x = racine;
        while (x != sentinelle && !x.cle.equals(k)) {
            if (cmp.compare(k, x.cle) < 0) x = x.gauche;
            else x = x.droit;
        }
        return x;
    }

    /**
     * Suppression d'un Noeud si il existe dans l'arbre
     * @param z le Noeud à supprimer
     */
    private void supprimer(Noeud z) {
        Noeud x, y;
        if (z.gauche == sentinelle || z.droit == sentinelle) y = z;
        else y = z.suivant();
        if (y.gauche != sentinelle) x = y.gauche;
        else x = y.droit;
        x.pere = y.pere;
        if (y.pere == sentinelle) racine = x;
        else {
            if (y == y.pere.gauche) y.pere.gauche = x;
            else y.pere.droit = x;
        }
        if (y!=z) z.cle = y.cle;
        if (y.couleur == 'N') suppressionCorrection(x);
    }

    /**
     * Corrige la structure de l'arbre de sorte à ce qu'il garde ses propriétés d'arbre rouge et noir
     * @param x Noeud autour duquel il faut corriger la suppression
     */
    private void suppressionCorrection(Noeud x) {
        Noeud w;
        while (x != racine && x.couleur == 'N') {
            if (x == x.pere.gauche) {
                w = x.pere.droit; //frere de x
                if (w.couleur == 'R') {
                    w.couleur = 'N';
                    x.pere.couleur = 'R';
                    rotationGauche(x.pere);
                    w = x.pere.droit;
                }
                if (w.gauche.couleur == 'N' && w.droit.couleur == 'N') {
                    w.couleur = 'R';
                    x = x.pere;
                } else {
                    if (w.droit.couleur == 'N') {
                        w.gauche.couleur = 'N';
                        w.couleur = 'R';
                        rotationDroite(w);
                        w = x.pere.droit;
                    }
                    w.couleur = x.pere.couleur;
                    x.pere.couleur = 'N';
                    w.droit.couleur = 'N';
                    rotationGauche(x.pere);
                    x = racine;
                }
            } else {
                w = x.pere.gauche; //frere de x
                if (w.couleur == 'R') {
                    w.couleur = 'N';
                    x.pere.couleur = 'R';
                    rotationDroite(x.pere);
                    w = x.pere.gauche;
                }
                if (w.droit.couleur == 'N' && w.gauche.couleur == 'N') {
                    w.couleur = 'R';
                    x = x.pere;
                } else {
                    if (w.gauche.couleur == 'N') {
                        w.droit.couleur = 'N';
                        w.couleur = 'R';
                        rotationGauche(w);
                        w = x.pere.gauche;
                    }
                    w.couleur = x.pere.couleur;
                    x.pere.couleur = 'N';
                    w.gauche.couleur = 'N';
                    rotationDroite(x.pere);
                    x = racine;
                }
            }
        }
        x.couleur = 'N';
    }

    /**
     * Méthode de rotation à gauche autour d'un noeud qui permet le recalibrage d'un ARN afin de respecter ses propriétés
     * lors de l'insertion ou de la suppression.
     * @param n Un Noeud
     */
    private void rotationGauche(Noeud n) {
        Noeud y = n.droit;
        n.droit = y.gauche;

        if(y.gauche != sentinelle) {
            y.gauche.pere = n;
        }

        y.pere = n.pere;

        if(n.pere == sentinelle) racine = y;
        else {
            if(n.pere.gauche == n) n.pere.gauche = y;
            else n.pere.droit = y;
        }
        y.gauche = n;
        n.pere = y;
    }

    /**
     * Méthode de rotation à droite autour d'un noeud qui permet le recalibrage d'un ARN afin de respecter ses propriétés
     * lors de l'insertion ou de la suppression.
     * @param n Un Noeud
     */
    private void rotationDroite(Noeud n) {
        Noeud y = n.gauche;
        n.gauche = y.droit;

        if(y.droit != sentinelle) y.droit.pere = n;

        y.pere = n.pere;

        if(n.pere == sentinelle) racine = y;
        else {
            if(n.pere.droit == n) n.pere.droit = y;
            else n.pere.gauche = y;
        }
        y.droit = n;
        n.pere = y;
    }

    /**
     * Méthode d'ajout d'un Noeud dans un tableau, appelle la méthode insertionCorrection afin de respecter
     * les propriétés de l'ARN.
     * @param e La clé du Noeud à ajouter
     */
    private void ajouter(E e) {
        Noeud z = new Noeud(e);
        Noeud y = sentinelle;
        Noeud x = racine;
        while (x != sentinelle) {
            y = x;
            if (cmp.compare(z.cle ,x.cle) < 0) x = x.gauche;
            else x = x.droit;
        }
        z.pere = y;
        if (y==sentinelle) racine = z;
        else if (cmp.compare(z.cle, y.cle) < 0) y.gauche = z;
        else y.droit = z;
        z.gauche = sentinelle;
        z.droit = sentinelle;
        z.couleur = 'R';
        insertionCorrection(z);
    }

    /**
     * Surcharge de la méthode add de l'interface AbstractCorrection qui appelle la méthode privée ajouter
     * quand le Noeud à ajouter n'est pas null.
     * @param e Clé du Noeud à ajouter
     * @return Un boolean égal à vrai quand le noeud a bien été ajouté
     */
    @Override
    public boolean add(E e) {
        Noeud z = new Noeud(e);
        if (z == null) return false;
        ajouter(e);
        return true;
    }

    /**
     * Méthode de correction de l'insertion afin que celle-ci respecte les propriétés d'un ARN
     * @param z Le Noeud inséré dont il faut corriger l'insertion
     */
    private void insertionCorrection(Noeud z) {
        Noeud y;
        while (z.pere.couleur == 'R') {
            if (z.pere == z.pere.pere.gauche) {
                y = z.pere.pere.droit;
                if (y.couleur == 'R') {
                    z.pere.couleur = 'N';
                    y.couleur = 'N';
                    z.pere.pere.couleur = 'R';
                    z = z.pere.pere;
                } else {
                    if (z==z.pere.pere.droit) {
                        z = z.pere;
                        rotationGauche(z);
                    }
                    z.pere.couleur = 'N';
                    z.pere.pere.couleur = 'R';
                    rotationDroite(z.pere.pere);
                }
            } else {
                y = z.pere.pere.gauche;
                if (y.couleur == 'R') {
                    z.pere.couleur = 'N';
                    y.couleur = 'N';
                    z.pere.pere.couleur = 'R';
                    z = z.pere.pere;
                } else {
                    if (z==z.pere.gauche) {
                        z = z.pere;
                        rotationDroite(z);
                    }
                    z.pere.couleur = 'N';
                    z.pere.pere.couleur = 'R';
                    rotationGauche(z.pere.pere);
                }
            }
        }
        racine.couleur = 'N';
    }

    /**
     * Surcharge de la méthode remove qui appelle la méthode privée supprimer quand le noeud en question n'est pas
     * une sentinelle.
     * @param o Objet à ajouter
     * @return Un boolean égal à vrai si la suppression a bien eu lieu
     */
    @Override
    public boolean remove(Object o) {
        Noeud z = rechercher((E) o);
        if (z == sentinelle) return false;
        supprimer(z);
        return true;
    }

    // Pour un "joli" affichage

    /**
     * Affichage de l'arbre sans passer de paramètre.
     * @return Un String de l'arbre complet
     */
    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        toString(racine, buf, "", maxStrLen(racine));
        return buf.toString();
    }

    /**
     * Affichage de l'arbre en passant des parmètres
     * @param x Racine du sous-arbre/arbre
     * @param buf Le StringBuffer
     * @param path Le chemin
     * @param len La longueur de l'arbre
     */
    private void toString(Noeud x, StringBuffer buf, String path, int len) {
        if (x == sentinelle)
            return;
        toString(x.droit, buf, path + "D", len);
        for (int i = 0; i < path.length(); i++) {
            for (int j = 0; j < len + 6; j++)
                buf.append(' ');
            char c = ' ';
            if (i == path.length() - 1)
                c = '+';
            else if (path.charAt(i) != path.charAt(i + 1))
                c = '|';
            buf.append(c);
        }
        if (x.couleur == 'R') buf.append("-- "+ ANSI_RED + "R|"+x.cle.toString() + ANSI_RESET);
        else buf.append("-- " + "N|"+x.cle.toString());
        if (x.gauche != null || x.droit != null) {
            buf.append(" --");
            for (int j = x.cle.toString().length(); j < len; j++)
                buf.append('-');
            buf.append('|');
        }
        buf.append("\n");
        toString(x.gauche, buf, path + "G", len);
    }

    /**
     * Affiche la hauteur de l'arbre de racine x
     * @param x La racine de l'arbre/sous-arbre
     * @return La longueur max de l'arbre = sa hauteur
     */
    private int maxStrLen(Noeud x) {
        return x == sentinelle ? 0 : Math.max(x.cle.toString().length(),
                Math.max(maxStrLen(x.gauche), maxStrLen(x.droit)));
    }

    /**
     * Vérifie si l'arbre est vide
     * @return Un boolean égal à true si l'arbre est vide/la hauteur est nulle
     */
    @Override
    public boolean isEmpty() {
        return taille == 0;
    }

    /**
     * Surcharge de la méthode contains de la classe abstraite AbstractCollection.
     * Vérifie si l'arbre contient un Noeud passé en paramètre.
     * @param o Un Noeud
     * @return Un boolean égal à vrai si la recherche renvoie un Noeud non sentinelle.
     */
    @Override
    public boolean contains(Object o) {
        return rechercher((E) o) != sentinelle;
    }

    /**
     * Surcharge de la méthode toArray de la classe abstraite AbstractCollection.
     * Appelle la méthode privée recArray sur un Array de taille = hauteur de l'arbre.
     * @return un tableau d'éléments
     */
    @Override
    public Object[] toArray() {
        Object[] retour = new Object[taille];
        recArray(racine, retour);
        return retour;
    }

    /**
     * Ajoute les éléments de l'arbre dans un tableau dans l'ordre croissant du comparateur.
     * @param n Un Noeud
     * @param t Un tableau
     */
    private void recArray(Noeud n, Object[] t) {
        if (n != sentinelle) {
            recArray(n.gauche, t);
            ajout(t, n);
            recArray(n.droit, t);
        }
    }

    /**
     * Ajout de Noeud dans un tableau passé en paramètre
     * @param t Tableau
     * @param o Noeud
     */
    private void ajout(Object[] t, Object o) {
        int i;
        for (i = 0; t[i] != sentinelle; ++i) ;
        t[i] = o;
    }

}
