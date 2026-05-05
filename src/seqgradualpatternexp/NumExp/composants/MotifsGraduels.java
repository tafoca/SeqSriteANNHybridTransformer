package seqgradualpatternexp.NumExp.composants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Laurent
 */
public class MotifsGraduels implements Comparable<MotifsGraduels> {

    public String[] motifs;
    public float SG;
    public float SGM;
    public List<Integer> listSeqActive;
    public Integer hisSeqId;
    public boolean isTrSup = false;
    public boolean isTrInf = false;

    public MotifsGraduels(String[] motifs) {
        this.motifs = motifs;
        this.listSeqActive = new ArrayList<>();
        //System.out.println(".. "+Outils.printGrad_Itemset(motifs)+ " , , "+this.toString());
    }

    @Override
    public int compareTo(MotifsGraduels o) {
        int r = -2;
        if (o.motifs.length == motifs.length) {
            int i;
            for (i = 0; i < o.motifs.length; i++) {
                if (motifs[i].equals("-") || motifs[i].equals("+")) {
                    if (!o.motifs[i].equals(this.motifs[i])) {
                        r = (o.motifs[i].compareTo(motifs[i]) < 0) ? -1 : 1;
                        break;
                    }
                } else {
                    //System.out.println(o.motifs[i] + "->" + "<---> " + motifs[i]);
                    if (Integer.parseInt(o.motifs[i]) != Integer.parseInt(motifs[i])) {
                        r = (Integer.parseInt(o.motifs[i]) - (Integer.parseInt(motifs[i])) < 0) ? -1 : 1;
                        break;
                    }
                }
            }
            if (i == o.motifs.length) {
                r = 0;
            }
        } else if (o.motifs.length < motifs.length) {
            r = 1;
        } else if (o.motifs.length > motifs.length) {
            r = -1;
        }
        return r;
    }

    /**
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        MotifsGraduels mg = (MotifsGraduels) obj;
        int i;
        if (mg.motifs.length == this.motifs.length) {
            return this.compareTo(mg) == 0;
        } else {
            return false;
        }

    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Arrays.deepHashCode(this.motifs);
        return hash;
    }

//    public String toString() {
//        return Arrays.toString(motifs);// + "SG: " + SG
//    }
    /**
     * return le motifs graduel sous forme de motif graduel
     *
     * @return
     */
    public String itemsetToStr() {
        StringBuilder r = new StringBuilder();
        for (String motif : motifs) {
            r.append(motif);
        }
        return r.toString();
    }

    @Override
    public String toString() {
        return Arrays.toString(motifs) + "SG: " + SG + " SGM: " + SGM + "id seq: " + hisSeqId + "\n " + " list active: " + listSeqActive + "\n";// + "SG: " + SG
    }

    public static boolean isContainOnList(ArrayList<MotifsGraduels> list, MotifsGraduels mg) {
        boolean b = false;
        for (MotifsGraduels e : list) {
            if (e.compareTo(mg) == 0) {
                b = true;
                break;
            }
        }
        return b;
    }

    public static void PurgebadCandidateMalFomattedInListAtAlevel(ArrayList<MotifsGraduels> list) {
        for (int j = 0; j < list.size(); j++) {
            MotifsGraduels e = list.get(j);
            int i;
            for (i = 0; i < e.motifs.length - 2; i += 2) {
                if (Integer.parseInt(e.motifs[i]) >= Integer.parseInt(e.motifs[i + 2])) {
                    break;
                }
            }
            if (i < e.motifs.length - 2) {
                System.out.println("" + e);
                list.remove(j);
                j--;
            }

        }

    }

    public static boolean isBadFormatOfMotifGradual(MotifsGraduels e) {
        int i;
        boolean b = false;
        for (i = 0; i < e.motifs.length - 2; i += 2) {
            if (Integer.parseInt(e.motifs[i]) >= Integer.parseInt(e.motifs[i + 2])) {
                break;
            }
        }
        if (i < e.motifs.length - 2) {
            b = true;
        }
        return b;

    }

    /*  public static void main(String[] args) {
     String[] m = {"0", "+", "1", "+", "4", "-"};
     String[] m1 = {"0", "+", "1", "+", "4", "+"};
     String[] M = {"0", "+", "1", "+", "2", "+", "3", "+", "4", "-"};
     String[] m4 = {"0", "+", "1", "+"};

     ArrayList<MotifsGraduels> motifSet = new ArrayList<>();
     MotifsGraduels mg1 = new MotifsGraduels(m);
     MotifsGraduels mg11 = new MotifsGraduels(m1);
     MotifsGraduels mg2 = new MotifsGraduels(M);
     MotifsGraduels mg4 = new MotifsGraduels(m4);
     motifSet.add(mg2);
     motifSet.add(mg11);
     motifSet.add(mg1);
     System.out.println("-----------------av -------------------");
     motifSet.forEach(e -> System.out.println(e));

     Collections.sort(motifSet);
     System.out.println("-----------------ap trie -------------------");

     motifSet.forEach(e -> System.out.println(e));

     System.out.println("\n \n-----------------suite ** -------------------");
     System.out.println(Outils.printGrad_Itemset(m));
     System.out.println(motifSet.indexOf(mg1) + "  : conatians m4 ? ");
     System.out.println(mg1);
     System.out.println(Outils.printGrad_Itemset(M));
     System.out.println(mg2);
     int compareTo = mg2.compareTo(mg1);
     System.out.println("Comparaison to : " + compareTo);
     System.out.println("Comparaison equals : " + mg2.equals(mg1));
     System.out.println("Comparaison to : " + mg11.compareTo(mg1));
     System.out.println("Comparaison equals : " + mg1.equals(mg11));
     }*/
}
