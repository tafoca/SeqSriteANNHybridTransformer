package seqgradualpatternexp;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import seqgradualpatternexp.NumExp.SgriteOpt;
import seqgradualpatternexp.common.MethodsImpl;

/**
 *
 * @author Laurent
 */
public abstract class TemplateMethod {

    /**
     *
     * @author Laurent
     */
    protected static int taille;
    protected static int nbitems, nbtransaction;
    protected static String[] attrList;
    protected static ArrayList<String[]> semantique = new ArrayList<>();
    public static ArrayList<Object[]> itemsets = new ArrayList<>();
    public static Map<Integer, ArrayList<Object[]>> initBD = new HashMap<>();
    public static Object[][] dataset;//1st column string date and 3 other float
    public static float[][] effDataset; // remove from test 1st colunm and conserve 3 last attributes
    protected static Object[][] DeltaDb;
    public static Object[] item;
    public MethodsImpl methods = new MethodsImpl();

    /**
     * construis seulement pour un objet fixe de la base globale
     *
     * @param Qdb data sequence
     * @param objet objet courant de O toute la bd
     * @return
     */
    public abstract Object[][] algoTEDOneDataSequence(Object[][] Qdb, int objet);

    /**
     * Implementation de l'algorithme TED
     *
     * @param listObj
     */
    public abstract void algoTED(List<Integer> listObj);

    public abstract Object[][] algoTEDOneDataSequence1(Object[][] Qdb, int objet);

    public abstract void algoTED1(List<Integer> listObj);

    public abstract double variationPositiveDegreeFunction(double degU, double degV);

    public abstract double variationNegativeDegreeFunction(double degU, double degV);
    /**
     * 
     * @param in
     * @return 
     */
    public float[][] reduceDataColunm(Object[][] in) {
        //affiche(in);
        float[][] out = new float[in.length][nbitems-2];
        int i = 0;
        for (Object[] e : in) {
            for (int j = 1; j < e.length; j++) {
                //System.out.println(":::::  "+ e[j]);
                out[i][j-1] = ((Double) e[j]).floatValue();
            }
         // System.out.println("i :::::  "+ i);
            i++;
        }
        
        //SgriteOpt.affiche(out);
        return out;
        
    }

    public void affiche(Object[][] tab) {
        for (Object[] d : tab) {
            System.out.println();
            for (Object v : d) {
                System.out.print(v + " ");
            }
        }
        System.out.println();
    }

    /**
     * Initialisation de structure necessaire pour fouille les sequentiel
     * graduel a partir de BD floue
     *
     * @throws IOException
     */
    public void initMethode() throws IOException {
        // TODO code application logic here
        Tools myTools = new Tools();
        myTools.initParameter("test.dat");
        taille = myTools.nbTransaction;
        nbtransaction = myTools.nbTransaction;
        nbitems = myTools.itemNembers;
        // construct db
        getconfig();
        itemsets = getDataSet("test.dat");
        itemsets.clear();
        // end of construction db
        item = null;
        dataset = methods.duplique(itemsets);
        affiche(dataset);
        // getAllColum(dataset, item, 1, taille);

        //       DeltaDb = algoTEDOneDataSequence(dataset, 1);
        Set<Integer> keySet = initBD.keySet();
        List<Integer> l = new ArrayList<>(keySet);
        Collections.sort(l);
        // algoTED(l);
        algoTED1(l);
//        FileWriter fw = new FileWriter(new File("out.dat"));
//        fw.write("seuil" + "     " + "items" + " " + "transaction" + "  " + "duree" + " " + "  nombre de motif" + "\n");
//        fw.flush();
//        fw.write("\n");
//        fw.flush();
    }

    /**
     *
     * @throws IOException
     */
    protected static void getconfig() throws IOException {
        attrList = Tools.attributenames(nbitems);// new String[nbitems];
        // output configuration of the user
        System.out.print(
                "\n [ configuration: " + nbitems + " items, and  " + nbtransaction + " transactions,mes item sont : ");
        for (int j = 0; j < attrList.length; j++) {
            System.out.print(" ," + attrList[j]);
        }
        System.out.println("]");
        System.out.println();
        for (int i = 0; i < semantique.size(); i++) {
            System.out.println(semantique.get(i) + "  ");
        }
    }

    /**
     *
     * @param dataset
     * @param item
     * @param a
     * @param taille
     */
    public abstract void getAllColum(Object[][] dataset, Object[] item, int a, int taille);

    /**
     *
     * @param dataset
     * @param item
     * @param a numero colonne
     * @param taille
     * @return
     */
    public abstract Object[] getDataColByCol(Object[][] dataset, Object[] item, int a, int taille);

    protected static ArrayList<Object[]> getDataSet(String source) throws IOException {
        BufferedReader data_in;
        String oneLine = "";
        data_in = new BufferedReader(new InputStreamReader(new FileInputStream(source)));
        getDataSetImpl(data_in);
        data_in.close();
        return itemsets;
    }

    /**
     * construction en memoire du jeux de donnees tableau de transactions
     *
     * @param data_in
     * @throws NumberFormatException
     * @throws IOException
     */
    public static void getDataSetImpl(BufferedReader data_in) throws NumberFormatException, IOException {
        String oneLine;
        Object oId = null;
        itemsets.clear();
        for (int i = 0; i < nbtransaction; i++) {
            Object[] tmp = new Object[nbitems];
            oneLine = data_in.readLine(); // one transaction
            StringTokenizer transaction = new StringTokenizer(oneLine, " ");
            Object val;
            int index = 0;
            int noCol = 0;
            while (transaction.hasMoreElements()) {
                Object object = transaction.nextElement();
                if (noCol == 0) {
                    val = Integer.parseInt((String) object);
                } else if (noCol == 1) {
                    val = (String) object;
                } else {
                    val = Double.parseDouble((String) object);
                }
                tmp[index] = (val);
                index++;
                noCol++;
            }
            itemsets.add(tmp);

        }
        initBD = getMyMapToListKeyIdObj(itemsets);
        // itemsets = initBD.get(1);
    }

    /**
     * renvoie la map en provenance de la liste de toute la BD en classant
     * chacun associe a un objet
     *
     * @param itemsets
     * @return
     */
    private static Map<Integer, ArrayList<Object[]>> getMyMapToListKeyIdObj(ArrayList<Object[]> itemsets) {
        Map<Integer, ArrayList<Object[]>> map = new HashMap<>();
        ArrayList<Object[]> l = new ArrayList<>();
        Integer oid = null;
        for (int i = 0; i < itemsets.size(); i++) {
            Object[] fst = itemsets.get(i);
            oid = (Integer) fst[0];
            Object[] second = null;
            Integer noid;
            if (i < itemsets.size() - 1) {
                second = itemsets.get(i + 1);
                noid = (Integer) second[0];
            } else {
                noid = oid;
            }
           // System.out.println(oid + " ******* " + noid);
            if (oid.equals(noid)) {
                l.add(getLastKmunusOneElement(fst));
            } else {
                l.add(getLastKmunusOneElement(fst));
                map.put((Integer) fst[0], l);
                l = new ArrayList<>();
            }
        }
        map.put(oid, l);
        return map;
    }

    static Object[] getLastKmunusOneElement(Object[] l) {
        Object[] lr = new Object[l.length - 1];
        for (int i = 1; i < l.length; i++) {
            lr[i - 1] = l[i];
        }
        return lr;
    }

}
