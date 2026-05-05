package seqgradualpatternexp.NumExp;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import seqgradualpatternexp.NumExp.composants.NodeWeight;

/**
 *
 * @author fotso
 */
public class Utilitaire {

    public Utilitaire() {
    }

    /**
     * les ancetre d'un noeud
     *
     * @param mat
     * @param node
     * @return
     */
    static List<Integer> ancetre(boolean[][] mat, int node) {
        List<Integer> l = new ArrayList<>();
        for (int i = 0; i < mat.length; i++) {
            if (mat[i][node]) {
                l.add(i);
            }
        }
        return l;
    }

    public static float supportCalculation(int nb, int total) {
        return (float) nb / total;
    }

    /**
     * ancetre qui sont les panents
     *
     * @param mat
     * @param A
     * @return
     */
    static List<Integer> ancetreValid(boolean[][] mat, List<Integer> inits) {
        List<Integer> valid = new ArrayList<>();

        inits.forEach(e -> {
            findIsparentElt(inits, mat, e, valid);
        });

        /*for (int i = 0; i < inits.size(); i++) {
         findIsParentNode(inits, i, mat, valid);
         }*/
        return valid;
    }

    public static void findIsparentElt(List<Integer> inits, boolean[][] mat, Integer e, List<Integer> valid) {
        int k = 0;
        boolean corespond = false;
        for (int j = 0; j < inits.size(); j++) {
            int cible = inits.get(j);
            boolean b = mat[e][cible];
            k = j;
            if (b) {
                corespond = true;
                break;
            }
        }
        if (k == inits.size() - 1 && !corespond) {
            valid.add(e);
        }
    }

    public static void findIsParentNode(List<Integer> inits, int i, boolean[][] mat, List<Integer> valid) {
        int e = inits.get(i);
        int k = 0;
        boolean corespond = false;
        for (int j = 0; j < inits.size(); j++) {
            int cible = inits.get(j);
            boolean b = mat[e][cible];
            k = j;
            if (b) {
                corespond = true;
                break;
            }
        }
        if (k == inits.size() - 1 && !corespond) {
            valid.add(inits.get(i));
        }
    }

    static boolean isAncetre(boolean[][] mat, List<Integer> A, int o) {
        return A.contains(o);
    }

    /**
     * mes parents directs
     *
     * @param mat
     * @param x
     * @param A
     * @return
     */
    static List<Integer> parentNode(boolean[][] mat, int x) {
        return ancetreValid(mat, ancetre(mat, x));
    }

    //determination des fils directs --------------------------------------------------------
    public static List<Integer> getDescendanceNode(boolean[][] boolMatrix, int item) {
        ArrayList<Integer> sons = new ArrayList<>();
        for (int i = 0; i < boolMatrix.length; i++) {
            if (boolMatrix[item][i] == true) {
                sons.add(i);
            }
        }
        return sons;

    }

    static List<Integer> descendantValid(boolean[][] boolMatrix, List<Integer> inits) {
        List<Integer> valid = new ArrayList<>();

        inits.forEach(e -> {
            findIsSonElt(inits, boolMatrix, e, valid);
        });

        /*for (int i = 0; i < inits.size(); i++) {
         findIsParentNode(inits, i, mat, valid);
         }*/
        return valid;
    }

    public static void findIsSonElt(List<Integer> inits, boolean[][] mat, Integer e, List<Integer> valid) {
        int k = 0;
        boolean corespond = false;
        for (int j = 0; j < inits.size(); j++) {
            int cible = inits.get(j);
            boolean b = mat[cible][e];
            k = j;
            if (b) {
                corespond = true;
                break;
            }
        }
        if (k == inits.size() - 1 && !corespond) {
            valid.add(e);
        }
    }

    static List<Integer> getSons(boolean[][] mat, int x) {
        return descendantValid(mat, getDescendanceNode(mat, x));
    }

    //---------------------------fin --------------
    /**
     * verifie si un noeud est une feuille
     */
    static boolean isALeaf(boolean[][] mat, int i) {
        boolean b = false;
        boolean[] tab = mat[i];
        int j = 0;
        for (; j < tab.length; j++) {
            if (tab[j]) {
                break;
            }
        }
        if (j >= tab.length - 1) {
            b = true;
        }
        return b;
    }

    static List<Integer> feuillles(boolean[][] mat) {
        List<Integer> r = new ArrayList<>();
        for (int i = 0; i < mat.length; i++) {
            if (isALeaf(mat, i)) {
                r.add(i);
            }
        }
        return r;
    }

    static List<Integer> feuilllesSort(boolean[][] mat) {
        List<Integer> r = new ArrayList<>();
        List<NodeWeight> tmp = new ArrayList<>();
        for (int i = 0; i < mat.length; i++) {
            if (isALeaf(mat, i)) {
                tmp.add(new NodeWeight(i, getColumSum(mat, i)));

            }
        }
        Collections.sort(tmp);
        for (int i = tmp.size() - 1; i >= 0; i--) {
            r.add(tmp.get(i).getNode());
        }
        return r;
    }

    static Integer getColumSum(boolean[][] dataset, int col) {
        int sum = 0;
        for (int i = 0; i < dataset.length; i++) {
            if (dataset[i][col]) {
                sum += 1;
            }
        }

        return sum;
    }

    static Object genTabCouple(boolean[][] mat, List<Integer> A, int x) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    static void computeLTS(boolean[][] mat, int[] memory) {
        List<Integer> feuillles = feuillles(mat);
        bodyCodeLtsCalc(feuillles, memory, mat);

    }

    /**
     * traitement a partir de noeudsM_M1
     *
     * @param mat
     * @param memory
     * @param noeudsM_M1
     */
    static void computeLTS(boolean[][] mat, int[] memory, List<Integer> noeudsM_M1) {
        //List<Integer> feuillles = feuillles(mat);
        bodyCodeLtsCalcNodesPasUtil(noeudsM_M1, memory, mat);

    }

    public static void bodyCodeLtsCalcNodesPasUtil(List<Integer> noeudsM_M1, int[] memory, boolean[][] mat) {
        //System.out.println("noeuds de base noeuds(M,M1) : "+ noeudsM_M1);
        Stack stack = new Stack();
        stack.addAll(noeudsM_M1);
        if (!stack.isEmpty()) {
            int part = (int) stack.pop();
            //raison non gestion des cas a une feuille ici pop avant tantque reste vide 
            while (stack.size() >= 0) {
                List<Integer> hisParts = parentNode(mat, part);
                //add list valid ancerter/parent
                for (int i = 0; i < hisParts.size(); i++) {
                    int a = memory[part];
                    int b = memory[(int) hisParts.get(i)];
                    if (memory[(int) hisParts.get(i)] < memory[part] + 1) {
                        memory[(int) hisParts.get(i)] = memory[part] + 1;
                        stack.push(hisParts.get(i));
                    }

                }
                if (stack.isEmpty()) {
                    break;
                } else {
                    part = (int) stack.pop();
                }

            }

        }
    }
    //SG1 methode remplace stack by queue an conseve same change contexte of value
    public static void bodyCodeLtsCalc(List<Integer> feuillles, int[] memory, boolean[][] mat) {
        // System.out.println("feuilles : " + feuillles);
        Queue queue = new ArrayDeque();
        queue.addAll(feuillles);
        queue.stream().forEach(e -> {
            memory[(int) e] = 1;
        });
        if (!queue.isEmpty()) {
            int part = (int) queue.poll();
            //raison non gestion des cas a une feuille ici pop avant tantque reste vide 
            while (queue.size() >= 0) {
                List<Integer> hisParts = parentNode(mat, part);
                //add list valid ancerter/parent
                for (int i = 0; i < hisParts.size(); i++) {
                    int a = memory[part];
                    int b = memory[(int) hisParts.get(i)];
                    if (memory[(int) hisParts.get(i)] < memory[part] + 1) {
                        memory[(int) hisParts.get(i)] = memory[part] + 1;
                        queue.add(hisParts.get(i));
                    }
                }
//            hisParts.forEach(e->{
//
//            });
//            hisParts.stream().filter(e->memory[part] < memory[e] +1).forEach(p->{
//                memory[part] = memory[p] +1;
//                stack.push(p);
//            });
                if (queue.isEmpty()) {
                    break;
                } else {
                    part = (int) queue.poll();
                }
            }
        }
    }

    /**
     * calcul ensemble trier au preallable
     *
     * @param mat
     * @param memory
     */
    static void computeLTSForSortLeafs(boolean[][] mat, int[] memory) {
        List<Integer> feuillles = feuilllesSort(mat);
        bodyCodeLtsCalc(feuillles, memory, mat);

    }

    static int maxElementMemory(int[] memory) {
        int max = 0;
        if (memory.length > 0) {
            max = memory[0];
            for (int i = 1; i < memory.length; i++) {
                if (memory[i] > max) {
                    max = memory[i];
                }
            }
//        System.out.print("The maximum value is : ");
//
//        System.out.print("->   " + max);
            //new Utilitaire().affichage(memory);
        }
        return max;
    }
    //SG1 methode computing method remplace stack by queue an conseve same change contexte of value
    public static int computeSupport(boolean[][] mat, int[] memory) {
        computeLTS(mat, memory);
        return maxElementMemory(memory);
    }

    /**
     * approche 2 calcul du support en conservant etat memeory passe
     *
     * @param mat
     * @param memory
     * @param noeudsM_M1
     * @return
     */
    static int computeSupport(boolean[][] mat, int[] memory, List<Integer> noeudsM_M1) {
        computeLTS(mat, memory, noeudsM_M1);
        return maxElementMemory(memory);
    }

//    static int computeSortLeafSupport(boolean[][] mat, int[] memory) {
//        computeLTSForSortLeafs(mat, memory);
//        return maxElementMemory(memory);
//    }
    void affichage(boolean[][] m) {
        for (boolean[] m1 : m) {
            for (boolean n : m1) {
                int a = (n) ? 1 : 0;
                System.out.print(" " + a);

            }
            System.out.println();
        }
    }

    void affichage(int m[]) {
        for (int n : m) {
            System.out.print(" " + n);
        }
        System.out.println();
    }

   /* public static void main(String[] args) {

        boolean[][] mat = {{false, true, true, false, false, false, false, false, false, false},
        {false, false, false, true, false, false, false, false, false, false},
        {false, false, false, true, false, false, false, false, false, false},
        {false, false, false, false, true, true, false, false, false, false},
        {false, false, false, false, false, false, false, false, true, false},
        {false, false, false, false, false, false, true, false, false, false},
        {false, false, false, false, false, false, false, true, false, false},
        {false, false, false, false, false, false, false, false, true, false},
        {false, false, false, false, false, false, false, false, false, false},
        {false, false, false, false, false, false, true, false, false, false}};
        
        boolean[][] mat1
                = {{false, true, true, true, true, false, false, true, true, true},
                {false, false, false, true, true, false, false, true, true, true},
                {false, false, false, true, true, false, false, true, true, true},
                {false, false, false, false, true, false, false, true, true, true},
                {false, false, false, false, false, false, false, false, false, true},
                {false, false, true, true, true, false, false, true, true, true},
                {false, false, false, true, true, false, false, true, true, true},
                {false, false, false, false, false, false, false, false, true, false},
                {false, false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false, false}};

        boolean[][] mat2
                = {
                    {false, false, true, false, false, false, false, false, false},
                    {false, false, true, false, false, false, false, false, false},
                    {false, false, false, true, true, false, false, false, false},
                    {false, false, false, false, false, false, false, true, false},
                    {false, false, false, false, false, true, false, false, false},
                    {false, false, false, false, false, false, true, false, false},
                    {false, false, false, false, false, false, false, true, false},
                    {false, false, false, false, false, false, false, false, false},
                    {false, false, false, false, false, true, false, false, false}};
        //jeux test fonction fils direct
        System.out.println("affichage de M1");
        new Utilitaire().affichage(mat1);
        int x = 3;//0 ok , 5 ok, 6 ok, 1 ok, 2 ok, 3 ok
        List<Integer> descendanceNode = Utilitaire.getDescendanceNode(mat1,x);
        System.out.println("affichage de la descendance du noeud 0 dans M");
         System.out.println("\n"+descendanceNode);
        System.out.println("affichage des fils du noeud 0 dans M");
        System.out.println("\n"+Utilitaire.getSons(mat1, x));
    }*/

}