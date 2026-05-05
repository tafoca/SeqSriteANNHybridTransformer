package seqgradualpatternexp.common;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author fotso
 */
public interface methods {
    public void affiche(Object[][] tab);
    public SequenceGraph createGraph(List<List<Object>> ds);
    public Object[][] duplique(ArrayList<Object[]> mat);
    public void wrtiteBD(Map<Integer, List<List<Object>>> dataset, FileWriter fw)throws IOException ;
    public void wrtiteBD(List<List<Object>> dataset, FileWriter fw) throws IOException;
    
    
}
