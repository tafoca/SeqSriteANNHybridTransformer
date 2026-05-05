package seqgradualpatternexp;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fotso
 */
public class SequenceGraph {
    // gradual records  is edge
     List<L> sommets;
     List<Integer> vertices;
    //arêtes représentent des séquences
    List<Arret> arretes;

    public SequenceGraph() {
        sommets=new ArrayList<>();
        arretes= new ArrayList<>();
        vertices= new ArrayList<>();
    }
    
    public void initVertice(){
        for (L sommetG : sommets) {
            List<Integer> vertex = sommetG.getVertex();
            for (Integer o : vertex) {
                vertices.add(o);
            }
        }
    }

    public List<L> getSommets() {
        return sommets;
    }

    public void setSommets(List<L> sommets) {
        this.sommets = sommets;
    }

    public List<Arret> getArretes() {
        return arretes;
    }

    public void setArretes(List<Arret> arretes) {
        this.arretes = arretes;
    }

    public int sizeS() {
        return sommets.size();
    }

    public boolean isEmptyS() {
        return sommets.isEmpty();
    }

    public boolean containsS(Object o) {
        return sommets.contains(o);
    }

    public boolean addS(L e) {
        return sommets.add(e);
    }

    public boolean removeS(Object o) {
        return sommets.remove(o);
    }

    public L getS(int index) {
        return sommets.get(index);
    }

    public L setS(int index, L element) {
        return sommets.set(index, element);
    }

    public void addS(int index, L element) {
        sommets.add(index, element);
    }

    public L removeS(int index) {
        return sommets.remove(index);
    }

    public int indexOfS(Object o) {
        return sommets.indexOf(o);
    }

    @Override
    public String toString() {
        return "\n SequenceGraph{" + "sommets=" + sommets + ",\n arretes=" + arretes + '}';
    }
   
    
}
