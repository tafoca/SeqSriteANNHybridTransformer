package seqgradualpatternexp.NumExp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import seqgradualpatternexp.NumExp.composants.Kernel;
import seqgradualpatternexp.NumExp.composants.Methodes;
import seqgradualpatternexp.NumExp.composants.MotifsGraduels;
/**
 *
 * @author fotso strategy de calcul de matrice binaire ou de concordance
 *
 */
public abstract class CommonMethod implements Methodes {

    public static boolean equalOneLineMat(boolean[] m1, boolean[] m2) {
        int i = 0;
        for (; i < m1.length; i++) {
            if (!m1[i] == m2[i]) {
                break;
            }
        }
        return i >= m1.length;
    }

    public ArrayList<MotifsGraduels> Union(ArrayList<MotifsGraduels> l, ArrayList<MotifsGraduels> l1) {
        ArrayList<MotifsGraduels> res = new ArrayList<>();
        for (MotifsGraduels re : l1) {
            l.add(re);
        }
        return l;
    }

    public static boolean equalTwoMat(boolean[][] m1, boolean[][] m2) {
        boolean b = false;
        if (m1.length == m2.length) {
            int i = 0;
            for (; i < m1.length; i++) {
                boolean equalOneLineMat = equalOneLineMat(m1[i], m2[i]);
                // System.out.println(""+equalOneLineMat);
                if (!equalOneLineMat) {
//                    for (int j = 0; j < m1.length; j++) {
//                        System.out.print(" " + m1[i][j]);
//                    }
                    System.out.println("\n no equal line no: " + i+"\n");
//                    for (int j = 0; j < m1.length; j++) {
//                        System.out.print(" " + m2[i][j]);
//                    }
//                    System.out.println("\n");

                    break;
                }
            }
            return i >= m1.length;
        }
        return b;
    }

    protected void creationMatriceBinaireValeurXPlus(int taille1, boolean[][] Contengence2, float[] rescol) {
        for (int j = 0; j < taille1; j++) {
            Contengence2[j][j] = false;
        }
        for (int j = 0; j < taille1; j++) {
            for (int j2 = 0; j2 < taille1; j2++) {
                if (j != j2) {
                    if (rescol[j] > rescol[j2]) {
                        Contengence2[j][j2] = true;
                    } else {
                        Contengence2[j][j2] = false;
                    }
                }
            }
        }
    }

    protected void creationMatriceBinaireValeurTriangulaireXPlus(int taille1, boolean[][] Contengence2, float[] rescol) {
        for (int j = 0; j < taille1; j++) {
            for (int j2 = j + 1; j2 < taille1; j2++) {

                if (rescol[j] > rescol[j2]) {
                    Contengence2[j][j2] = true;
                    Contengence2[j2][j] = false;
                } else {
                    Contengence2[j][j2] = false;
                    Contengence2[j2][j] = rescol[j2] > rescol[j];
                }

            }
        }
    }

    protected void creationMatriceBinaireValeurXmoins(int taille1, boolean[][] Contengence1, float[] rescol) {
        for (int j = 0; j < taille1; j++) {
            Contengence1[j][j] = false;
        }
        for (int j = 0; j < taille1; j++) {
            for (int j2 = 0; j2 < taille1; j2++) {
                if (j != j2) {
                    if (rescol[j] < rescol[j2]) {
                        Contengence1[j][j2] = true;
                    } else {
                        Contengence1[j][j2] = false;
                    }
                }
            }
        }
    }

    protected void creationMatriceBinaireValeurTriangulaireXmoins(int taille1, boolean[][] Contengence1, float[] rescol) {

        for (int j = 0; j < taille1; j++) {
            for (int j2 = j + 1; j2 < taille1; j2++) {

                if (rescol[j] < rescol[j2]) {
                    Contengence1[j][j2] = true;
                    Contengence1[j2][j] = false;
                } else {
                    Contengence1[j][j2] = false;
                    Contengence1[j2][j] = rescol[j2] < rescol[j];
                }

            }
        }
    }

    //TODO proposer une construction de matice binaire d'adjacence (approche le plus proche de la matrice d'adjacence): 2/11/19
    /**
     *
     * @param taille1
     * @param adjacence
     * @param rescol une colonne associe au jeu de donnees concerne par la
     * recherche de l'item graduel X-
     */
    protected void creationMatriceBinaireAdjacence(int taille1, boolean[][] adjacence, float[] rescol) {
//        for (int j = 0; j < taille1; j++) {
//            adjacence[j][j] = false;
//        }

        for (int i = 0; i < taille1; i++) {
            Set<Integer> EL = new HashSet<>();
            for (int j = 0; j < taille1; j++) {
                //if (j != j2) {
                if (rescol[i] > rescol[j]) {
                    adjacence[i][j] = true;
                    //traitement pour determiner les faux positifs ( elt concerner j < i )
                    if (j < i) {
                        //former les objets a exclure : tous les adjacence[j][?] == 1 entraine 0 au meme indice '?' sur  adjacence[i][?] == 0
                        int k = 0;
                        while (k < taille1) {
                            if (EL.contains(k)) {
                                k++;
                            } else {
                                if (adjacence[j][k]) {
                                    EL.add(k);
                                    adjacence[i][k] = false;
                                }

                                k++;
                            }
                        }

                    } else {
                        adjacence[i][j] = true;
                    }

                } else {
                    adjacence[i][j] = false;
                }
                //}
            }
        }
    }

    /**
     *
     * @param objetsCouple liste de couple objet et indice associe
     * @param indices condition de filtrage oui ou non
     * @return
     */
    @Override
    public List<Integer> rechercheDesCoupleCompatibleComplementaire(List<Kernel> objetsCouple, List<Integer> indices) {
        List<Integer> l = new ArrayList<>();
        int i = 0;
        int p = i;
        while (i < objetsCouple.size()) {
            if (indexOfBeginAtPosition(indices, i, p) == -1) {
                p = i;
                l.add(i);
            }
            i++;
        }
        return l;
    }

    int indexOfBeginAtPosition(List<Integer> ensemble, Integer node, int beginPosition) {
        int position = -1;
        for (int k = beginPosition; k < ensemble.size(); k++) {
            if (ensemble.get(k).equals(node)) {
                position = k;
                break;
            }
        }
        return position;
    }

    /**
     * retour l'indice dans le couple objet-indice
     *
     * @param objetsCouple
     * @param node
     * @return
     */
    @Override
    public Integer getIndiceOfObjetInListCouple(List<Kernel> objetsCouple, Integer node) {
        Integer o = null;
        for (int i = 0; i < objetsCouple.size(); i++) {
            if (Objects.equals(objetsCouple.get(i).getNode(), node)) {
                o = objetsCouple.get(i).getNumOrdre();
                break;
            }
        }
        return o;
    }

    /**
     * retour l'objet dans le couple objet-indice
     *
     * @param objetsCouple
     * @param indice
     * @return
     */
    @Override
    public Integer getObjetOfIndiceInListCouple(List<Kernel> objetsCouple, Integer indice) {
        Integer o = null;
        for (int i = 0; i < objetsCouple.size(); i++) {
            if (Objects.equals(objetsCouple.get(i).getNumOrdre(), indice)) {
                o = objetsCouple.get(i).getNode();
                break;
            }
        }
        return o;
    }

    @Override
    public List<Integer> reverseList(List<Integer> list) {
        List<Integer> l = new ArrayList<>();
        for (int i = list.size() - 1; i >= 0; i--) {
            l.add(list.get(i));
        }
        return l;
    }

    @Override
    public void addOrNotNodeToParentSet(List<Integer> peres, boolean[][] tmp, boolean[][] adjM1, boolean[][] adjM2, Integer e, int e1, int e2) {

    }

}
