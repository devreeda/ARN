import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>
 * Implantation de l'interface Collection basée sur les arbres binaires de
 * recherche. Les éléments sont ordonnés soit en utilisant l'ordre naturel (cf
 * Comparable) soit avec un Comparator fourni à la création.
 * </p>
 *
 * <p>
 * Certaines méthodes de AbstractCollection doivent être surchargées pour plus
 * d'efficacité.
 * </p>
 *
 * @param <E> le type des clés stockées dans l'arbre
 */
public class ABR<E> extends AbstractCollection<E> {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    private Noeud racine;
    private int taille;
    private Comparator<? super E> cmp;
    private Noeud sentinelle;

    private class Noeud {

        E cle;
        Noeud gauche;
        Noeud droit;
        Noeud pere;
        String couleur;

        Noeud(E cle) {
            pere = droit = gauche = sentinelle;
            this.cle = cle;
            this.couleur = "N";
        }

        /**
         * donne l'élément avec la clé max
         * @return
         */
        Noeud maximum() {
            Noeud n = this;
            while (n.droit != sentinelle) n = n.droit;
            return n;
        }

        /**
         * donne l'élément avec la clé min
         * @return
         */
        Noeud minimum() {
            Noeud n = this;
            while (n.gauche != sentinelle) n = n.gauche;
            return n;
        }

        /**
         * Donne l'élément dont la clé suit la clé de this
         * @return
         */
        Noeud suivant() {
            Noeud x;

            x = this;
            if (droit != sentinelle) return droit.minimum();
            while (x.pere != sentinelle && x == x.pere.droit) x = x.pere;
            return x;
        }

        @Override
        public String toString() {
            return cle.toString();
        }
    }

    // Constructeurs
    public ABR() {
        sentinelle = new Noeud((E)"nil");
        racine = sentinelle;
        cmp = (x, y) -> ((Comparable<E>) x).compareTo(y);
        taille = 0;
    }

    public ABR(Comparator<? super E> cmp) {
        sentinelle = new Noeud(null);
        racine = sentinelle;
        taille = 0;
        this.cmp = cmp;
    }

    public ABR(Collection<? extends E> c) {
        this();
        for (E e : c) this.add(e);
    }

    @Override
    public Iterator<E> iterator() {
        return new ABRIterator();
    }

    @Override
    public int size() {
        return taille;
    }

    // Quelques méthodes utiles
    private Noeud rechercher(E k) {
        Noeud x;

        x = racine;
        while (x != sentinelle && !x.cle.equals(k)) {
            if (cmp.compare(k, x.cle) < 0) x = x.gauche;
            else x = x.droit;
        }
        return x;
    }

    private Noeud supprimer(Noeud z) {
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
        if (y.couleur == "N") suppressionCorrection(x);
        return null;
    }

    private void suppressionCorrection(Noeud x) {
        Noeud w;
        while (x != racine && x.couleur == "N") {
            if (x == x.pere.gauche) {
                w = x.pere.droit; //frere de x
                if (w.couleur == "R") {
                    w.couleur = "N";
                    x.pere.couleur = "R";
                    rotationGauche(x.pere);
                    w = x.pere.droit;
                }
                if (w.gauche.couleur == "N" && w.droit.couleur == "N") {
                    w.couleur = "R";
                    x = x.pere;
                } else {
                    if (w.droit.couleur == "N") {
                        w.gauche.couleur = "N";
                        w.couleur = "R";
                        rotationDroite(w);
                        w = x.pere.droit;
                    }
                    w.couleur = x.pere.couleur;
                    x.pere.couleur = "N";
                    w.droit.couleur = "N";
                    rotationGauche(x.pere);
                    x = racine;
                }
            } else {
                w = x.pere.gauche; //frere de x
                if (w.couleur == "R") {
                    w.couleur = "N";
                    x.pere.couleur = "R";
                    rotationDroite(x.pere);
                    w = x.pere.gauche;
                }
                if (w.droit.couleur == "N" && w.gauche.couleur == "N") {
                    w.couleur = "R";
                    x = x.pere;
                } else {
                    if (w.gauche.couleur == "N") {
                        w.droit.couleur = "N";
                        w.couleur = "R";
                        rotationGauche(w);
                        w = x.pere.gauche;
                    }
                    w.couleur = x.pere.couleur;
                    x.pere.couleur = "N";
                    w.gauche.couleur = "N";
                    rotationDroite(x.pere);
                    x = racine;
                }
            }
        }
        x.couleur = "N";
    }

    private class ABRIterator implements Iterator<E> {
        Noeud n, p;

        public ABRIterator() {
            super();
            n = sentinelle;
            if (racine != sentinelle) p = racine.minimum();
            else p = null;
        }

        @Override
        public boolean hasNext() {
            return p != sentinelle;
        }

        @Override
        public E next() {
            if (p == null) throw new NoSuchElementException();
            n = p;
            p = p.suivant();
            return n.cle;
        }

        @Override
        public void remove() {
            if (n == null) throw new IllegalStateException();
            supprimer(n);
            n = null;
        }
    }

    // Pour un "joli" affichage

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        toString(racine, buf, "", maxStrLen(racine));
        return buf.toString();
    }

    private void toString(Noeud x, StringBuffer buf, String path, int len) {
        if (x == null)
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
        if (x.couleur == "R") buf.append("-- "+ ANSI_RED + x.cle.toString() + ANSI_RESET);
        else buf.append("-- " + x.cle.toString());
        if (x.gauche != null || x.droit != null) {
            buf.append(" --");
            for (int j = x.cle.toString().length(); j < len; j++)
                buf.append('-');
            buf.append('|');
        }
        buf.append("\n");
        toString(x.gauche, buf, path + "G", len);
    }

    private int maxStrLen(Noeud x) {
        return x == sentinelle ? 0 : Math.max(x.cle.toString().length(),
                Math.max(maxStrLen(x.gauche), maxStrLen(x.droit)));
    }

    @Override
    public boolean isEmpty() {
        return taille == 0;
    }

    @Override
    public boolean contains(Object o) {
        return rechercher((E) o) != sentinelle;
    }

    @Override
    public Object[] toArray() {
        Object[] retour = new Object[taille];
        recArray(racine, retour);
        return retour;
    }

    private void recArray(Noeud n, Object[] t) {
        if (n != null) {
            recArray(n.gauche, t);
            ajout(t, n);
            recArray(n.droit, t);
        }
    }

    private void ajout(Object[] t, Object o) {
        int i;
        for (i = 0; t[i] != sentinelle; ++i) ;
        t[i] = o;
    }

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

    @Override
    public boolean add(E e) {
        System.out.println("add début");
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
        z.couleur = "R";
        insertionCorrection(z);
        System.out.println("add fin");
        return true;
    }

    private void insertionCorrection(Noeud z) {
        System.out.println("add correction début");
        Noeud y;
        while (z.pere.couleur == "R") {
            System.out.println("?");
            if (z.pere == z.pere.pere.gauche) {
                y = z.pere.pere.droit;
                if (y.couleur == "R") {
                    z.pere.couleur = "N";
                    y.couleur = "N";
                    z.pere.pere.couleur = "R";
                    z = z.pere.pere;
                } else {
                    if (z==z.pere.pere.droit) {
                        z = z.pere;
                        rotationGauche(z);
                    }
                    z.pere.couleur = "N";
                    z.pere.pere.couleur = "R";
                    rotationDroite(z.pere.pere);
                }
            } else {
                y = z.pere.pere.gauche;
                if (y.couleur == "R") {
                    z.pere.couleur = "N";
                    y.couleur = "N";
                    z.pere.pere.couleur = "R";
                    z = z.pere.pere;
                } else {
                    if (z==z.pere.gauche) {
                        z = z.pere;
                        rotationDroite(z);
                    }
                    z.pere.couleur = "N";
                    z.pere.pere.couleur = "R";
                    rotationGauche(z.pere.pere);
                }
            }
        }
        racine.couleur = "N";
        System.out.println("add correction fin");
    }

    @Override
    public boolean remove(Object o) {
        Noeud z = rechercher((E) o);
        if (z == sentinelle) return false;
        supprimer(z);
        return true;
    }

}
/*
 * package ArbreBinaire;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>
 * Implantation de l'interface Collection basée sur les arbres binaires de
 * recherche. Les éléments sont ordonnés soit en utilisant l'ordre naturel (cf
 * Comparable) soit avec un Comparator fourni à la création.
 * </p>
 *
 * <p>
 * Certaines méthodes de AbstractCollection doivent être surchargées pour plus
 * d'efficacité.
 * </p>
 *
 * @param <E>
 *            le type des clés stockées dans l'arbre
 *
public class ABR<E> extends AbstractCollection<E> {
    private Noeud racine;
    private int taille;
    private Comparator<? super E> cmp;

    private class Noeud {
        E cle;
        Noeud gauche;
        Noeud droit;
        Noeud pere;

        Noeud(E cle) {
            pere = droit = gauche = null;
            this.cle = cle;
        }

        Noeud maximum() {
            Noeud n = this;
            while(n.droit != null) n = n.droit;
            return n;
        }

        Noeud minimum() {
            Noeud n = this;
            while(n.gauche != null) n = n.gauche;
            return n;
        }

        Noeud suivant() {
            Noeud x;

            x = this;
            if(droit != null) return droit.minimum();
            while(x.pere != null && x == x.pere.droit) x = x.pere;
            return x.pere;
        }

        @Override
        public String toString() {
            return cle.toString();
        }
    }

    // Consructeurs
    public ABR() {
        racine = null;
        cmp = (x, y) -> ((Comparable<E>)x).compareTo(y);
        taille = 0;
    }

    public ABR(Comparator<? super E> cmp) {
        racine = null;
        taille = 0;
        this.cmp = cmp;
    }

    public ABR(Collection<? extends E> c) {
        this();
        for(E e: c) this.add(e);
    }

    @Override
    public Iterator<E> iterator() {
        return new ABRIterator();
    }

    @Override
    public int size() {
        return taille;
    }

    // Quelques méthodes utiles
    private Noeud rechercher(E k) {
        Noeud x;

        x = racine;
        while(x != null && !x.cle.equals(k)) {
            if(cmp.compare(k, x.cle) < 0) x = x.gauche;
            else x = x.droit;
        }
        return x;
    }

    private Noeud supprimer(Noeud z) {
        Noeud s;
        if(z != null) {
            if(z.droit != null) {
                s = z.suivant();
                z.cle = s.cle;
                donPere(s, s.droit);
                s.gauche = s.droit = s.pere = null;
            } else {
                donPere(z, z.gauche);
                z.gauche = z.droit = z.pere = null;
            }
        }
        return null;
    }

    private void donPere(Noeud ancienFils, Noeud nouveauFils) {
        Noeud pere;
        pere = ancienFils.pere;
        if(pere != null) {
            if(pere.gauche == ancienFils) pere.gauche = nouveauFils;
            else pere.droit = nouveauFils;
        } else racine = nouveauFils;
        if(nouveauFils != null) nouveauFils.pere = pere;
    }

    private void changementPere(Noeud fils, Noeud pere) {
        if(cmp.compare(pere.cle, fils.cle) < 0) pere.droit = fils;
        else pere.gauche = fils;
        fils.pere = pere;
    }

    private class ABRIterator implements Iterator<E> {
        Noeud n, p;
        public ABRIterator() {
            super();
            n = null;
            if(racine != null) p = racine.minimum();
            else p = null;
        }

        @Override
        public boolean hasNext() {
            return p != null;
        }
        @Override
        public E next() {
            if(p == null) throw new NoSuchElementException();
            n = p;
            p = p.suivant();
            return n.cle;
        }
        @Override
        public void remove() {
            if(n == null) throw new IllegalStateException();
            supprimer(n);
            n = null;
        }
    }

    // Pour un "joli" affichage

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        toString(racine, buf, "", maxStrLen(racine));
        return buf.toString();
    }

    private void toString(Noeud x, StringBuffer buf, String path, int len) {
        if (x == null)
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
        buf.append("-- " + x.cle.toString());
        if (x.gauche != null || x.droit != null) {
            buf.append(" --");
            for (int j = x.cle.toString().length(); j < len; j++)
                buf.append('-');
            buf.append('|');
        }
        buf.append("\n");
        toString(x.gauche, buf, path + "G", len);
    }

    private int maxStrLen(Noeud x) {
        return x == null ? 0 : Math.max(x.cle.toString().length(),
                Math.max(maxStrLen(x.gauche), maxStrLen(x.droit)));
    }

    @Override
    public boolean isEmpty() {
        return taille == 0;
    }

    @Override
    public boolean contains(Object o) {
        return rechercher((E)o) != null;
    }

    @Override
    public Object[] toArray() {
        Object[] retour = new Object[taille];
        recArray(racine, retour);
        return retour;
    }

    private void recArray(Noeud n, Object[] t) {
        if(n != null) {
            recArray(n.gauche, t);
            ajout(t, n);
            recArray(n.droit, t);
        }
    }

    private void ajout(Object[] t, Object o) {
        int i;
        for(i = 0; t[i] != null; ++i);
        t[i] = o;
    }

    @Override
    public boolean add(E e) {
        Noeud x, y, z;

        y = null;
        x = racine;
        while(x != null) {
            y = x;
            if(cmp.compare(e, (E) x.cle) < 0) x = x.gauche;
            else x = x.droit;
        }

        z = new Noeud(e);
        z.pere = y;
        if(y == null) racine = z;
        else if(cmp.compare(e, y.cle) < 0) y.gauche = z;
        else y.droit = z;
        z.gauche = z.droit = null;

        taille++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        Noeud z = rechercher((E)o);
        if(z == null) return false;
        supprimer(z);
        return true;
    }
}*/
