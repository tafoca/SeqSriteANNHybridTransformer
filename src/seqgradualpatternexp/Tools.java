package seqgradualpatternexp;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
/**
 * @author tabueu Fotso Laurent
 * @copyright GNU General Public License v3 No reproduction in whole or part
 * without maintaining this copyright notice and imposing this condition on any
 * subsequent users.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.StringTokenizer;

public class Tools {

    /**
     * @category configuration property file
     */
    int itemNembers;
    /**
     * @category configuration property file
     */
    int nbTransaction;
    String filename;
    Map<Integer, List<Integer>> mapNodeSons;

    public Map<Integer, List<Integer>> getMapNodeSons() {
        return mapNodeSons;
    }

    public void setMapNodeSons(Map<Integer, List<Integer>> mapNodeSons) {
        this.mapNodeSons = mapNodeSons;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public boolean[] getIsfeuilles() {
        return isfeuilles;
    }

    public void setIsfeuilles(boolean[] isfeuilles) {
        this.isfeuilles = isfeuilles;
    }
    
    
    

    public int getItemNembers() {
        return itemNembers;
    }

    public void setItemNembers(int itemNembers) {
        this.itemNembers = itemNembers;
    }

    public int getNbTransaction() {
        return nbTransaction;
    }

    public void setNbTransaction(int nbTransaction) {
        this.nbTransaction = nbTransaction;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * Determine le nombre d'attribut et le nombre de transaction
     *
     * @param filename
     */
    public void initParameter(String filename) {
        int countTrans = 0;
        int countAttr = 0;
        setFilename(filename);
        boolean drapeau = true;
        BufferedReader data_in;
        try {
            data_in = new BufferedReader(new FileReader(filename));
            try {
                while (data_in.ready()) {
                    String line = data_in.readLine();
                    if (line.matches("\\s*")) {
                        continue; // be friendly with empty lines
                    }
                    countTrans++;
                    if (drapeau) {
                        StringTokenizer t = new StringTokenizer(line, " ");
                        countAttr = t.countTokens();
                        drapeau = false;
                    }

                }
                setItemNembers(countAttr);
                setNbTransaction(countTrans);
                System.out.println(toString());
                ;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }

    }

    @Override
    public String toString() {
        return "Config [itemNembers=" + itemNembers + ", nbTransaction=" + nbTransaction + ", filename=" + filename
                + "]";
    }

    int i;
    public int[] memory; //public pour un acces or du package 
    public boolean[] isfeuilles;
    int sizeMat = 0;

    public Tools() {
        super();
        this.mapNodeSons = new HashMap<>();
        initMemory();
    }

    public int getSizeMat() {
        return sizeMat;
    }

    public void setSizeMat(int sizeMat) {
        this.sizeMat = sizeMat;
    }

    public boolean lexicalComparaison(String[] pattern1, String[] pattern2) {

        // case of 1-gradualItemset
        if (pattern1.length == pattern2.length && pattern1.length == 2) {
            if (pattern1[0] == pattern2[0]) {
                return false;
            }
            return true;
        }
        // case pattern of size 2
        if (pattern1.length != pattern2.length) {
            return false;
        } else {
            for (i = 0; i < pattern2.length - 3; i++) {
                if (pattern1[i] == pattern2[i]) {
                    continue;
                } else {
                    break;
                }
            }
            if (i == pattern2.length - 3 && pattern1[pattern1.length - 2] != pattern2[pattern1.length - 2]) {
                return true;
            } else {
                return false;
            }
        }
    }

    public String[] lexicalFusion(String[] pattern1, String[] pattern2) {
        String[] fusion = null;
        int i;
        if (lexicalComparaison(pattern1, pattern2)) {
            fusion = new String[pattern1.length + 2];
            // fusion = pattern1 +pattern2;
            for (i = 0; i < pattern1.length; i++) {

                fusion[i] = pattern1[i];
            }
            fusion[pattern1.length] = pattern2[pattern1.length - 2];
            fusion[pattern1.length + 1] = pattern2[pattern1.length - 1];
            return fusion;
        }
        return fusion;// null value
    }

    public String printGrad_Itemset(String[] setGrd) {
        String String, res = "";
        for (int i = 0; i < setGrd.length; i++) {

            if (i % 2 != 0) {
                String = setGrd[i].toString() + " ";
            } else {
                String = setGrd[i].toString();
            }
            res += String;
        }
        return res;
    }

    /**
     *
     * @param boolMatrix
     * @param item variation between 0 to nbItems -1
     * @return
     */
    public ArrayList<Integer> getSonNode(boolean[][] boolMatrix, int item) {
        ArrayList<Integer> sons = new ArrayList<>();
        for (int i = 0; i < boolMatrix.length; i++) {
            if (boolMatrix[item][i] == true) {
                sons.add(i);
            }
        }
        return sons;

    }

    /**
     *
     * @param boolMatrix
     * @param pattern
     * @param memory
     * @param item root node to determine size of different way
     * @return
     */
    public void initMemory() {
        memory = new int[sizeMat];
        isfeuilles = new boolean[sizeMat];
        for (int i = 0; i < memory.length; i++) {
            memory[i] = -1;
            //isfeuilles[i] = false;
        }
    }

    /**
     * Methode recursiveCorvering
     *
     * @param boolMatrix it's the graph
     * @param item is a root node of your graph
     * @return
     */
    public int sizeMaxWay(boolean[][] boolMatrix, /* String[] pattern, */ int item) {
        // intialisation of memory table

        ArrayList<Integer> sons = getSonNode(boolMatrix,
                item); /* tous les o ′ de valeur 1 à la ligne o */

        if (sons.size() == 0) {
            memory[item] = 1;
        } else {
            for (Object element : sons) {
                int son = (int) element;
                if (memory[son] == -1) {// pattern,memory,
                    sizeMaxWay(boolMatrix, son);
                }
            }

            for (Object element : sons) {
                int son = (int) element;
                memory[item] = max(memory[item], memory[son] + 1);
            }
        }
        return memory[item];

    }

    /**
     * Methode IterativeCorvering (iterative version)
     *
     * @param boolMatrix it's the graph
     * @param item is a root node of your graph
     * @return fin implementation 12/11/19 02:40
     */
    public int sizeMaxWayIterative(boolean[][] boolMatrix, /* String[] pattern, */ int item) {
        // intialisation of memory table
        Map<Integer, List<Integer>> sonsOfAllNode = getSonsOfAllNode(boolMatrix);

        int courantNode = item;
        int precedent = 0;
        int son = -1;
        List<Integer> sons = sonsOfAllNode.get(courantNode);
        Stack<Integer> pile = new Stack<>();
        pile.add(item);

        if (!sons.isEmpty()) {
            son = sons.get(0);
        }

        if (sons.isEmpty()) {
            memory[item] = 1;
        } else {
            while (pile.size() >= 0) {
                if (!pile.isEmpty()) {
                    courantNode = pile.pop();
                } else {
                    break;
                }
                if (memory[son] != -1) {
                    memory[courantNode] = max(memory[courantNode], memory[son] + 1);
                }
                sons = sonsOfAllNode.get(courantNode);
                //boolean empty = getSonNode(boolMatrix, courantNode).isEmpty();
                boolean empty = isfeuilles[courantNode];
                if (sons.isEmpty() && empty) {
                    //il s'agit veritable feuillle
                    memory[courantNode] = 1;
                }
                if (sons.isEmpty() && !empty) {
                    //il s'agit noueud interne avec tous ces fils traites
                    son = courantNode;
                }
                if (!sons.isEmpty()) {
                    son = (int) sons.get(0);
                    if (memory[son] == -1) {
                        pile.add(courantNode);
                        pile.add(son);
                        //  sonsOfAllNode.get(courantNode).remove((Integer) son);
                    } else {
                        memory[courantNode] = max(memory[courantNode], memory[son] + 1);
                        // sonsOfAllNode.get(courantNode).remove((Integer) son);
                        son = courantNode;
                    }
                }
            }

        }
        return memory[item];

    }

    public Map<Integer, List<Integer>> getSonsOfAllNode(boolean mat[][]) {
        int n = mat.length;
        Map<Integer, List<Integer>> list = new HashMap<>();
        for (int j = 0; j < n; j++) {
            ArrayList<Integer> sonNode = getSonNode(mat, j);
            isfeuilles[j] = sonNode.isEmpty(); //determine si c'est une feuille
            list.put(j, sonNode);
        }
        return list;

    }

    public ArrayList<Object> getColum(boolean[][] dataset, int col) {
        ArrayList<Object> item = new ArrayList<>();

        for (int i = 0; i < dataset.length; i++) {
            item.add(dataset[i][col]);
        }

        return item;
    }

    // return all root nodes of graph
    public ArrayList<Object> getRoots(boolean[][] src) {
        ArrayList<Object> res = new ArrayList<>();
        ArrayList<Object> out = new ArrayList<>();
        int j;

        for (int i = 0; i < src.length; i++) {
            res = getColum(src, i);
            boolean sentinnel = false;
            for (j = 0; j < res.size(); j++) {
                if ((boolean) res.get(j) == true) {
                    sentinnel = true;
                }
            }
            if (!sentinnel) {
                out.add(i);
            }
        }
        return out;

    }

    private int max(int j, int k) {
        if (j > k) {
            return j;
        } else {
            return k;
        }
    }

    // computation of support of all contengence matrix boolmatrix
    public int maximumSupport(boolean[][] boolMatrix/*, String[] pattern*/, int[] memory) { // pattern,
        int max = 0;
        // memory,
        if (getRoots(boolMatrix).size() > 0) {
            //max = sizeMaxWay(boolMatrix, (int) getRoots(boolMatrix).get(0));
            for (int i = 0; i < getRoots(boolMatrix).size(); i++) {// , pattern,
                // memory,
                int tmp = sizeMaxWay(boolMatrix, (int) getRoots(boolMatrix).get(i));//fix bug 10:51 26/10/19
                if (max < tmp) {
                    max = tmp;
                }
            }

        } else if (getRoots(boolMatrix).isEmpty()) {
            max = 0;
        }
        return max;

    }

    //calcul min support
    public int maximumSupport_iter(boolean[][] boolMatrix/*, String[] pattern*/, int[] memory) { // pattern,
        int max = 0;
        // memory,
        if (getRoots(boolMatrix).size() > 0) {
            // max = sizeMaxWayIterative(boolMatrix, (int) getRoots(boolMatrix).get(0));
            for (int i = 0; i < getRoots(boolMatrix).size(); i++) {// , pattern,
                // memory,
                int tmp = sizeMaxWayIterative(boolMatrix, (int) getRoots(boolMatrix).get(i));//fix bug 10:51 26/10/19
                if (max < tmp) {
                    max = tmp;
                }
            }

        } else if (getRoots(boolMatrix).isEmpty()) {
            max = 0;
        }
        return max;

    }

    public float supportCalculation(int nb, int total) {
        return (float) nb / total;
    }

    public boolean Apartient(ArrayList<Integer> tampon, int val) {
        boolean ret = false;
        for (int i = 0; i < tampon.size(); i++) {
            if (tampon.get(i) == val) {
                ret = true;
                break;
            }
        }
        return ret;

    }

    public int Recherche(ArrayList<Integer> tampon, int val) {
        int ret = -1;
        for (int i = 0; i < tampon.size(); i++) {
            if (tampon.get(i) == val) {
                ret = i;
                break;
            }
        }
        return ret;
    }

    /**
     *
     * @param tampon0 list object viables,corrects
     * @param tampon1 list index obj to remove
     * @return
     */
    public ArrayList<Integer> rmApartientList(ArrayList<Integer> tampon0, ArrayList<Integer> tampon1) {
        for (int i = 0; i < tampon1.size(); i++) {
            tampon0.remove(tampon1.get(i));
        }
        return tampon0;

    }

    /**
     * we changed Character[] by String[] due more than 9 attribut this
     * sequential representation begin impossible
     *
     * @param nbitem
     * @return liste of items here represented by sequence :1,2,3,....
     */
    public static String[] attributenames(int nbitem) {
        String[] attrList = new String[nbitem];
        String c = "0";
        for (int i = 0; i < attrList.length; i++) {
            attrList[i] = c;
            c = "" + (Integer.parseInt(c) + 1);
        }
        return attrList;
    }

    public void writeFileOut(ArrayList<String[]> semantique) {

        for (Iterator iterator = semantique.iterator(); iterator.hasNext();) {
            String[] strings = (String[]) iterator.next();

        }

    }

    public ArrayList<String[]> genIndexUsed(ArrayList<String[]> unitem, ArrayList<String[]> unitemFreq) {
        int n1 = unitem.size(), n2 = unitemFreq.size();
        ArrayList<String[]> valrtn = new ArrayList<>();
        System.out.println(n1 + "  --  " + n2);
        switch (n1) {
            case 4:
                for (int i = 0; i <= 1; i++) {
                    for (int j = 2; j < 4; j++) {
                        // condition unitem[i] et unitem[j] arptienne a unitemFreq
                        // ==>true sinon false
                        String[] tm = unitem.get(i);
                        String tmst1 = tm[0];
                        String tmst2 = unitem.get(j)[0];
                        if (!(tmst1 == tmst2)) {
                            valrtn.add(lexicalFusion(unitem.get(i), unitem.get(j)));
                        }
                    }
                }
                break;

            case 6:
                for (int i = 0; i < 2; i++) {
                    for (int j = 2; j < 4; j++) {
                        for (int k = 4; k < 6; k++) {
                            // condition unitem[i] et unitem[j] arptienne a
                            // unitemFreq
                            // ==>true sinon false
                            String[] tm = unitem.get(i);
                            String tmst1 = tm[0];
                            String tmst2 = unitem.get(j)[0];
                            String tmst3 = unitem.get(k)[0];

                            if (testitem(tmst1, tmst2, tmst3)) {
                                String[] val = lexicalFusion(unitem.get(i), unitem.get(j));
                                String[] val3 = unitem.get(k);
                                String[] add = Fusion(val, val3);
                                valrtn.add(add);
                            }
                        }

                    }
                }
                break;

            case 8:
                for (int i = 0; i < 2; i++) {
                    for (int j = 2; j < 4; j++) {
                        for (int k = 4; k < 6; k++) {
                            for (int l = 6; l < 8; l++) {
                                String tmst1 = unitem.get(i)[0];
                                String tmst2 = unitem.get(j)[0];
                                String tmst3 = unitem.get(k)[0];
                                String tmst4 = unitem.get(l)[0];
                                boolean b = testitem(tmst1, tmst2, tmst3, tmst4);
                                if (b) {
                                    String[] add_f, val = lexicalFusion(unitem.get(i), unitem.get(j));
                                    String[] val3 = unitem.get(k);
                                    String[] val4 = unitem.get(l);
                                    String[] add = Fusion(val, val3);
                                    add_f = Fusion(add, val4);
                                    valrtn.add(add_f);
                                }
                            }
                        }

                    }
                }
                break;

            case 10:
                for (int i = 0; i < 2; i++) {
                    for (int j = 2; j < 4; j++) {
                        for (int k = 4; k < 6; k++) {
                            for (int l = 6; l < 8; l++) {
                                for (int l2 = 8; l2 < 10; l2++) {
                                    String tmst1 = unitem.get(i)[0];
                                    String tmst2 = unitem.get(j)[0];
                                    String tmst3 = unitem.get(k)[0];
                                    String tmst4 = unitem.get(l)[0];
                                    String tmst5 = unitem.get(l2)[0];
                                    boolean b = testitem(tmst1, tmst2, tmst3, tmst4, tmst5);
                                    if (b) {
                                        String[] add_f, add_f1, val = lexicalFusion(unitem.get(i), unitem.get(j));
                                        String[] val3 = unitem.get(k);
                                        String[] val4 = unitem.get(l);
                                        String[] val5 = unitem.get(l2);
                                        String[] add = Fusion(val, val3);
                                        add_f = Fusion(add, val4);
                                        add_f1 = Fusion(add_f, val5);
                                        valrtn.add(add_f1);
                                    }
                                }
                            }
                        }

                    }
                }
                break;

            case 12:
                for (int i = 0; i < 2; i++) {
                    for (int j = 2; j < 4; j++) {
                        for (int k = 4; k < 6; k++) {
                            for (int l = 6; l < 8; l++) {
                                for (int l2 = 8; l2 < 10; l2++) {
                                    for (int m = 10; m < 12; m++) {
                                        /*
                                         * String tmst1 = unitem.get(i)[0]; String
                                         * tmst2 = unitem.get(j)[0]; String tmst3 =
                                         * unitem.get(k)[0]; String tmst4 =
                                         * unitem.get(l)[0]; String tmst5 =
                                         * unitem.get(l2)[0];
                                         */
                                        boolean b = testitem(unitem.get(i)[0], unitem.get(j)[0], unitem.get(k)[0],
                                                unitem.get(l)[0], unitem.get(l2)[0], unitem.get(m)[0]);
                                        if (b) {
                                            String[] add_f, add_f1, add2, val = lexicalFusion(unitem.get(i), unitem.get(j));
                                            /*
                                             * String[] val3 = unitem.get(k);
                                             * String[] val4 = unitem.get(l);
                                             * String[] val5 = unitem.get(l2);
                                             * String[] add = Fusion(val, val3);
                                             * add_f = Fusion(add, val4); add_f1 =
                                             * Fusion(add_f, val5); add2 =
                                             * Fusion(add_f1, unitem.get(m));
                                             */
                                            add2 = Fusion(Fusion(Fusion(
                                                    Fusion(lexicalFusion(unitem.get(i), unitem.get(j)), unitem.get(k)),
                                                    unitem.get(l)), unitem.get(l2)), unitem.get(m));

                                            valrtn.add(add2);
                                        }
                                    }
                                }
                            }
                        }

                    }
                }
                break;

            case 14:
                for (int i = 0; i < 2; i++) {
                    for (int j = 2; j < 4; j++) {
                        for (int k = 4; k < 6; k++) {
                            for (int l = 6; l < 8; l++) {
                                for (int l2 = 8; l2 < 10; l2++) {
                                    for (int m = 10; m < 12; m++) {
                                        for (int m2 = 12; m2 < 14; m2++) {
                                            boolean b = testitem(unitem.get(i)[0], unitem.get(j)[0], unitem.get(k)[0],
                                                    unitem.get(l)[0], unitem.get(l2)[0], unitem.get(m)[0],
                                                    unitem.get(m2)[0]);
                                            if (b) {
                                                String[] add_f;

                                                add_f = Fusion(Fusion(
                                                        Fusion(Fusion(Fusion(lexicalFusion(unitem.get(i), unitem.get(j)),
                                                                                unitem.get(k)), unitem.get(l)), unitem.get(l2)),
                                                        unitem.get(m)), unitem.get(m2));

                                                valrtn.add(add_f);
                                            }
                                        }

                                    }
                                }
                            }
                        }

                    }
                }
                break;

            case 16:
                for (int i = 0; i < 2; i++) {
                    for (int j = 2; j < 4; j++) {
                        for (int k = 4; k < 6; k++) {
                            for (int l = 6; l < 8; l++) {
                                for (int l2 = 8; l2 < 10; l2++) {
                                    for (int m = 10; m < 12; m++) {
                                        for (int m2 = 12; m2 < 14; m2++) {
                                            for (int n = 14; n < 16; n++) {
                                                boolean b = testitem(unitem.get(i)[0], unitem.get(j)[0], unitem.get(k)[0],
                                                        unitem.get(l)[0], unitem.get(l2)[0], unitem.get(m)[0],
                                                        unitem.get(m2)[0], unitem.get(n)[0]);
                                                if (b) {
                                                    String[] add_f;

                                                    add_f = Fusion(Fusion(Fusion(
                                                            Fusion(Fusion(
                                                                            Fusion(lexicalFusion(unitem.get(i), unitem.get(j)),
                                                                                    unitem.get(k)),
                                                                            unitem.get(l)), unitem.get(l2)),
                                                            unitem.get(m)), unitem.get(m2)), unitem.get(n));

                                                    valrtn.add(add_f);
                                                }
                                            }

                                        }

                                    }
                                }
                            }
                        }

                    }
                }
                break;

            case 18:
                for (int i = 0; i < 2; i++) {
                    for (int j = 2; j < 4; j++) {
                        for (int k = 4; k < 6; k++) {
                            for (int l = 6; l < 8; l++) {
                                for (int l2 = 8; l2 < 10; l2++) {
                                    for (int m = 10; m < 12; m++) {
                                        for (int m2 = 12; m2 < 14; m2++) {
                                            for (int n = 14; n < 16; n++) {
                                                for (int n3 = 16; n3 < 18; n3++) {
                                                    boolean b = testitem(unitem.get(i)[0], unitem.get(j)[0],
                                                            unitem.get(k)[0], unitem.get(l)[0], unitem.get(l2)[0],
                                                            unitem.get(m)[0], unitem.get(m2)[0], unitem.get(n)[0],
                                                            unitem.get(n3)[0]);
                                                    if (b) {
                                                        String[] add_f;

                                                        add_f = Fusion(
                                                                Fusion(Fusion(
                                                                                Fusion(Fusion(
                                                                                                Fusion(Fusion(
                                                                                                                lexicalFusion(unitem.get(i),
                                                                                                                        unitem.get(j)),
                                                                                                                unitem.get(k)), unitem.get(l)),
                                                                                                unitem.get(l2)), unitem.get(m)),
                                                                                unitem.get(m2)), unitem.get(n)),
                                                                unitem.get(n3));

                                                        valrtn.add(add_f);
                                                    }
                                                }
                                            }

                                        }

                                    }
                                }
                            }
                        }

                    }
                }
                break;

            case 20:
                for (int i = 0; i < 2; i++) {
                    for (int j = 2; j < 4; j++) {
                        for (int k = 4; k < 6; k++) {
                            for (int l = 6; l < 8; l++) {
                                for (int l2 = 8; l2 < 10; l2++) {
                                    for (int m = 10; m < 12; m++) {
                                        for (int m2 = 12; m2 < 14; m2++) {
                                            for (int n = 14; n < 16; n++) {
                                                for (int n3 = 16; n3 < 18; n3++) {
                                                    for (int o = 18; o < 20; o++) {
                                                        boolean b = testitem(unitem.get(i)[0], unitem.get(j)[0],
                                                                unitem.get(k)[0], unitem.get(l)[0], unitem.get(l2)[0],
                                                                unitem.get(m)[0], unitem.get(m2)[0], unitem.get(n)[0],
                                                                unitem.get(n3)[0], unitem.get(o)[0]);
                                                        if (b) {
                                                            String[] add_f, add_fnxt;

                                                            add_f = Fusion(
                                                                    Fusion(Fusion(
                                                                                    Fusion(Fusion(
                                                                                                    Fusion(Fusion(
                                                                                                                    lexicalFusion(unitem.get(i),
                                                                                                                            unitem.get(j)),
                                                                                                                    unitem.get(k)), unitem.get(l)),
                                                                                                    unitem.get(l2)), unitem.get(m)),
                                                                                    unitem.get(m2)), unitem.get(n)),
                                                                    unitem.get(n3));
                                                            add_fnxt = Fusion(add_f, unitem.get(o));

                                                            valrtn.add(add_fnxt);
                                                        }
                                                    }
                                                }
                                            }

                                        }

                                    }
                                }
                            }
                        }

                    }
                }
                break;

            case 22:
                for (int i = 0; i < 2; i++) {
                    for (int j = 2; j < 4; j++) {
                        for (int k = 4; k < 6; k++) {
                            for (int l = 6; l < 8; l++) {
                                for (int l2 = 8; l2 < 10; l2++) {
                                    for (int m = 10; m < 12; m++) {
                                        for (int m2 = 12; m2 < 14; m2++) {
                                            for (int n = 14; n < 16; n++) {
                                                for (int n3 = 16; n3 < 18; n3++) {
                                                    for (int o = 18; o < 20; o++) {
                                                        for (int o2 = 20; o2 < 22; o2++) {
                                                            boolean b = testitem(unitem.get(i)[0], unitem.get(j)[0],
                                                                    unitem.get(k)[0], unitem.get(l)[0], unitem.get(l2)[0],
                                                                    unitem.get(m)[0], unitem.get(m2)[0], unitem.get(n)[0],
                                                                    unitem.get(n3)[0], unitem.get(o)[0], unitem.get(o2)[0]);
                                                            if (b) {
                                                                String[] add_f, add_fnxt;

                                                                add_f = Fusion(
                                                                        Fusion(Fusion(
                                                                                        Fusion(Fusion(
                                                                                                        Fusion(Fusion(
                                                                                                                        lexicalFusion(unitem.get(i),
                                                                                                                                unitem.get(j)),
                                                                                                                        unitem.get(k)),
                                                                                                                unitem.get(l)),
                                                                                                        unitem.get(l2)), unitem.get(m)),
                                                                                        unitem.get(m2)), unitem.get(n)),
                                                                        unitem.get(n3));
                                                                add_fnxt = Fusion(Fusion(add_f, unitem.get(o)),
                                                                        unitem.get(o2));

                                                                valrtn.add(add_fnxt);
                                                            }
                                                        }
                                                    }
                                                }
                                            }

                                        }

                                    }
                                }
                            }
                        }

                    }
                }
                break;

            default:
                break;
        }
        return valrtn;

    }

    public boolean testitem(String... listItem) {
        int taille = listItem.length;
        boolean b = true;
        String[] tabitem = new String[taille];
        for (int i = 0; i < taille; i++) {
            tabitem[i] = listItem[i];
        }
        for (int i = 0; i < tabitem.length; i++) {
            for (int j = i + 1; j < tabitem.length; j++) {
                b = b && !(tabitem[i] == tabitem[j]);
            }
        }
        return b;

    }

    private String[] Fusion(String[] val, String[] val3) {
        int j = 0, p = val.length, q = val3.length;
        String[] r = new String[p + q];
        for (j = 0; j < p; j++) {
            r[j] = val[j];
        }
        for (int i = 0; i < val3.length; i++) {
            r[j + i] = val3[i];
        }
        return r;
    }

    public int[] getMemory() {
        return memory;
    }

    public void setMemory(int[] memory) {
        this.memory = memory;
    }

}
