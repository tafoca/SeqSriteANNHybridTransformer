package seqgradualpatternexp.common;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author fotso
 */
public class MethodsImpl implements methods{

    public MethodsImpl() {
    }
   
       /**
        * 
        * @param tab 
        */
     @Override
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
      * 
      * @param ds
      * @return 
      */
     @Override
     public SequenceGraph createGraph(List<List<Object>> ds) {
        int indexInO = 0;
        SequenceGraph graph = new SequenceGraph();
        L l = new L();
        l.setStartTime(L.start(ds.get(indexInO), 0));
        while (indexInO < ds.size()) {
            List<Object> record = ds.get(indexInO);
            l.setStartTime(L.start(record, 0));
            if (indexInO + 1 < ds.size()) {
                if (l.getStartTime().equals(L.start(ds.get(indexInO + 1), 0))) {
                    l.add(indexInO);
                } else {
                    l.add(indexInO);
                    graph.addS(l);
                    l = new L(L.start(ds.get(indexInO + 1), 0));
                }
            }
            indexInO++;
        }
        l.add(indexInO - 1);
        graph.addS(l);
        graph.initVertice();
        for (int i = 0; i < graph.sommets.size(); i++) {
            List<Integer> tmpL = graph.sommets.get(i).getVertex();
            for (Integer e : tmpL) {
                for (int j = i; j < graph.sommets.size(); j++) {
                    if ((Integer)graph.sommets.get(j).startTime >= (Integer)L.end(ds.get(e), 0)) {
                       // System.out.println(e+ " ....."+graph.sommets.get(j).getVertex());
                        for (Integer f : graph.sommets.get(j).getVertex()) {
                            graph.arretes.add(new Arret(e, f));
                        }
                    }
                }
            }
        }
        return graph;
    }
     
    /**
     *
     * @param mat
     * @return
     */
    @Override
    public Object[][] duplique(ArrayList<Object[]> mat) {
        Object[][] res = new Object[mat.size()][];
        for (int i = 0; i < mat.size(); i++) {
            res[i] = new Object[mat.get(i).length];
            for (int j = 0; j < mat.get(i).length; j++) {
                res[i][j] = mat.get(i)[j];
            }
        }
        return res;
    }

    /**
     *
     * @param dataset
     * @param fw
     * @throws IOException
     */
    @Override
    public void wrtiteBD(Map<Integer, List<List<Object>>> dataset, FileWriter fw) throws IOException {

        try {
            String sep = " ";
            for (Map.Entry<Integer, List<List<Object>>> entrySet : dataset.entrySet()) {
                Integer key = entrySet.getKey();
                List<List<Object>> value = entrySet.getValue();
                for (List<Object> line : value) {
                    fw.write(key.toString());
                    fw.write(sep);
                    for (Object o : line) {
                        if (o instanceof String) {
                            fw.write(o.toString());
                        } else if (o instanceof Double) {
                            String pattern = "###.##";
                            DecimalFormat myFormatter = new DecimalFormat(pattern);
                            String output = myFormatter.format(o);
                            // System.out.println(o + " " + pattern + " " + output);
                            fw.write(output.toString());
                        }
                        fw.write(sep);
                    }
                    fw.write("\n");
                    fw.flush();
                }
            }
        } catch (Exception e) {
            System.out.println("" + e.getMessage());
        }
    }

    /**
     *
     * @param dataset
     * @param fw
     * @throws IOException
     */
    @Override
    public void wrtiteBD(List<List<Object>> dataset, FileWriter fw) throws IOException {
        try {
            String sep = " ";
            for (List<Object> line : dataset) {
                for (Object o : line) {
                    fw.write(o.toString());
                    fw.write(sep);
                }
                fw.write("\n");
                fw.flush();
            }
        } finally {
        }
    }
     
}
