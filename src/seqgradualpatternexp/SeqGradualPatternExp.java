package seqgradualpatternexp;

import seqgradualpatternexp.common.SequenceGraph;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sourceforge.jFuzzyLogic.FIS;
import seqgradualpatternexp.common.MethodsImpl;
import static seqgradualpatternexp.test.TestTipper.determineFuzzyItemByValueOfItem;
import static seqgradualpatternexp.test.TestTipper.getFISByFileDesc;

/**
 *
 * @author Laurent
 */
public class SeqGradualPatternExp extends TemplateMethod {
    
    public static String fileName = "fcl/maketGrad.fcl";//maket.fcl;
    static Map<String, Double> valuePrice = new HashMap<>();
    static Map<String, Double> valueConsumption = new HashMap<>();
    static Map<String, Double> valueSales = new HashMap<>();
    Map<Integer, List<List<Object>>> SgmaBD;// my all seq database derive
    Map<Integer, List<List<Object>>> TimeRelatedVarBD;
    String memdegdataseq = "membershipdegdataseq.dat";
    String vardegdataseq = "varationdegdataseq.dat";
    MethodsImpl methods =new MethodsImpl();
    
    public SeqGradualPatternExp() throws IOException {
        this.SgmaBD = new HashMap<>();
        TimeRelatedVarBD = new HashMap<>();
        initMethode();
        try {
            FileWriter fw = new FileWriter(new File(memdegdataseq));
            FileWriter fw2 = new FileWriter(new File(vardegdataseq));
            //wrtiteBD(deltaBD, fw);
            methods.wrtiteBD(SgmaBD, fw);
            methods.wrtiteBD(TimeRelatedVarBD, fw2);
            //TODO createGraph function.
            //Notre approche utilise une structure de graphe pour représenter les séquences autorisées dans une séquence de données.
            SequenceGraph createGraph = methods.createGraph(TimeRelatedVarBD.get(1));
            System.out.println(createGraph);
            //TODO Implementation de la fouille de sequence graduellle
            
        } catch (IOException ex) {
            Logger.getLogger(SeqGradualPatternExp.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    /**
     * determine la valeur de (o,o') a utiliser pour determiner fuzzification
     *
     * @param val1
     * @param val2
     * @return
     */
    double computeValeAttr(double val1, double val2) {
        return (val1 - val2) / 30;
    }
    
    double computeValeAttr(double val) {
        return val;
    }

    /**
     *
     * @param degU
     * @param degV
     * @return
     */
    @Override
    public double variationPositiveDegreeFunction(double degU, double degV) {
        double r = 0;
        if (degU < degV) {
            double tmp = (degU - degV);
            r = Math.abs(tmp);
        }
        return r;
    }
    
    @Override
    public double variationNegativeDegreeFunction(double degU, double degV) {
        double r = 0;
        if (degU > degV) {
            double tmp = (degU - degV);
            r = Math.abs(tmp);
        }
        return r;
    }
    
    public static void main(String[] args) throws IOException {
        SeqGradualPatternExp sgpe = new SeqGradualPatternExp();
    }
    
    public void affcheRecord(Object[] record) {
        for (Object record1 : record) {
            System.out.println(" " + record.toString());
            System.out.println();
        }
        
    }
    public static final String[] items = {"time", "price", "consumption", "sales"};
    
    @Override
    public Object[][] algoTEDOneDataSequence1(Object[][] Qdb, int objet) {
        System.out.println(objet + "  : seqgradualpatternexp.SeqGradualPatternExp.algoTEDOneDataSequence1()" + nbtransaction + "--" + (nbitems - 1));
        List<List<Object>> deltaBD = new ArrayList();
        List<List<Object>> varDegDB = new ArrayList<>();//variation Degree DB for objet o
        FIS fis = getFISByFileDesc(fileName);
        for (int i = 0; i < itemsets.size(); i++) {
            //for (int j = i + 1; j < itemsets.size(); j++) {
            System.out.println("(" + i + "," + ")");
            Object[] record0 = Qdb[i];
            List<Object> deltaRecord = new ArrayList<>();
            String s0 = (String) record0[0];
            deltaRecord.add((s0));
            for (int k = 1; k < nbitems - 1; k++) {
                double computeValeAttr = computeValeAttr((double) record0[k]);
                Map<String, Double> map = determineFuzzyItemByValueOfItem(fis, items[k], computeValeAttr);
                System.out.println("> item :" + items[k] + " : " + computeValeAttr);
                for (Map.Entry<String, Double> entrySet : map.entrySet()) {
                    String key = entrySet.getKey();
                    Double value = entrySet.getValue();
                    System.out.println("> \t fuzy item :" + key + " : " + value);
                    deltaRecord.add(value);
                }
            }
            deltaBD.add(deltaRecord);
        }
        List<List<Object>> put = SgmaBD.put(objet, deltaBD);
        //TODO step 2 of process transformation deltaBD for o object to varDegDB
        buildVarDB(fis, deltaBD, varDegDB, objet, TimeRelatedVarBD);
        return null;
    }
    
    public void buildVarDB(FIS fis, List<List<Object>> deltaBD, List<List<Object>> varDegDB, int objet, Map<Integer, List<List<Object>>> TimeRelatedVarBD) {
        System.out.println(objet + "  : seqgradualpatternexp.SeqGradualPatternExp.buildVarDB()" + objet + "--" + (nbitems - 1));
        int newNbItem = 2 * (nbitems - 1);
        for (int i = 0; i < deltaBD.size(); i++) {
            for (int j = i + 1; j < deltaBD.size(); j++) {
                System.out.println("(" + i + "," + j + ")");
                List<Object> record0 = deltaBD.get(i);
                List<Object> record1 = deltaBD.get(j);
                List<Object> deltaRcord = new ArrayList<>();
                String s0 = (String) record0.get(0);
                String s1 = (String) record1.get(0);
                deltaRcord.add((s0 + '-' + s1));
                for (int k = 1; k < record0.size(); k++) {
                    double va10 = (double) record0.get(k);
                    double va11 = (double) record1.get(k);
                    System.out.println(va10 + " > - - <  " + va11);
                    double upos = variationPositiveDegreeFunction(va10, va11);
                    double uneg = variationNegativeDegreeFunction(va10, va11);
                    System.out.println("> item - :" + items[k] + " : " + uneg);
                    System.out.println("> item + :" + items[k] + " : " + upos);
                    deltaRcord.add(uneg);
                    deltaRcord.add(upos);
                }
                //TODO ADD deta time aussi
                double time = Math.abs((Integer.parseInt(s1) - Integer.parseInt(s0)));
                System.out.println("time diff: " + time);
                Map<String, Double> timefuzzy = determineFuzzyItemByValueOfItem(fis, "duration", Double.valueOf(time));
                for (Map.Entry<String, Double> entrySet : timefuzzy.entrySet()) {
                    String key = entrySet.getKey();
                    Double value = entrySet.getValue();
                    System.out.println("> \t fuzy item :" + key + " : " + value);
                    deltaRcord.add(value);
                }
                varDegDB.add(deltaRcord);
            }
        }
        List<List<Object>> put = TimeRelatedVarBD.put(objet, varDegDB);
    }
    
    @Override
    public Object[][] algoTEDOneDataSequence(Object[][] Qdb, int objet) {
        System.out.println(objet + "  : seqgradualpatternexp.SeqGradualPatternExp.algoTEDOneDataSequence()" + nbtransaction + "--" + (nbitems - 1));
        
        List<List<Object>> deltaBD = new ArrayList();
        FIS fis = getFISByFileDesc(fileName);
        
        for (int i = 0; i < itemsets.size(); i++) {
            for (int j = i + 1; j < itemsets.size(); j++) {
                System.out.println("(" + i + "," + j + ")");
                Object[] record0 = Qdb[i];
                Object[] record1 = Qdb[j];
                List<Object> deltaRecord = new ArrayList<>();
                String s0 = (String) record0[0];
                String s1 = (String) record1[0];
                deltaRecord.add((s0 + '-' + s1));
                for (int k = 1; k < nbitems - 1; k++) {
                    double computeValeAttr = computeValeAttr((double) record0[k], (double) record1[k]);
                    Map<String, Double> map = determineFuzzyItemByValueOfItem(fis, items[k], computeValeAttr);
                    System.out.println("> item :" + items[k] + " : " + computeValeAttr);
                    for (Map.Entry<String, Double> entrySet : map.entrySet()) {
                        String key = entrySet.getKey();
                        Double value = entrySet.getValue();
                        System.out.println("> \t fuzy item :" + key + " : " + value);
                        deltaRecord.add(value);
                    }
                }
                //TODO ADD deta time aussi
                double time = Math.abs((Integer.parseInt(s1) - Integer.parseInt(s0)));
                System.out.println("time diff: " + time);
                Map<String, Double> timefuzzy = determineFuzzyItemByValueOfItem(fis, "duration", Double.valueOf(time));
                for (Map.Entry<String, Double> entrySet : timefuzzy.entrySet()) {
                    String key = entrySet.getKey();
                    Double value = entrySet.getValue();
                    System.out.println("> \t fuzy item :" + key + " : " + value);
                    deltaRecord.add(value);
                }
                deltaBD.add(deltaRecord);
            }
        }
        List<List<Object>> put = SgmaBD.put(objet, deltaBD);
        return null;
    }

    // a is item number a
    @Override
    public Object[] getDataColByCol(Object[][] dataset, Object[] item, int a, int taille) {
        taille = taille;
        item = new Object[taille];
        int l = 0;
        for (int i = 0; i < dataset.length; i++) {
            
            for (int k = 0; k < nbitems; k++) {
                if (l == i && k == a) {
                    item[i] = dataset[i][k];
                    // System.out.println(item[i]+ " ");
                }
            }
            // res.add(item);
            l++;
        }
        
        return item;
    }
    
    @Override
    public void getAllColum(Object[][] dataset, Object[] item, int a, int taille) {
        for (a = 0; a < nbitems; a++) {
            Object[] rescol = getDataColByCol(dataset, item, a, taille);
            afficheColonne(rescol);
        }
    }
    
    public void afficheColonne(Object[] rescol) {
        for (int i = 0; i < rescol.length; i++) {
            System.out.println(rescol[i] + "  ");
        }
        System.out.println();
    }
    
    @Override
    public void algoTED1(List<Integer> listObj) {
        System.out.println("list " + listObj);
        for (int i = 0; i < listObj.size(); i++) {
            Integer j = listObj.get(i);
            itemsets = initBD.get(j);
            dataset = methods.duplique(itemsets);
            affiche(dataset);
            DeltaDb = algoTEDOneDataSequence1(dataset, j);
        }
    }
    
    @Override
    public void algoTED(List<Integer> listObj) {
        System.out.println("list " + listObj);
        for (int i = 0; i < listObj.size(); i++) {
            Integer j = listObj.get(i);
            itemsets = initBD.get(j);
            dataset = methods.duplique(itemsets);
            affiche(dataset);
            DeltaDb = algoTEDOneDataSequence(dataset, j);
        }
    }
    
}
