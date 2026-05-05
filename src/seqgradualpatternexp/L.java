package seqgradualpatternexp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author fotso
 */
public class L {

    Object startTime;
    List<Integer> vertex;//record sime start time

    public L(Object startTime) {
        this.startTime = startTime;
         vertex=new ArrayList<>();
    }

    public L() {
        vertex=new ArrayList<>();
    }

    public int size() {
        return vertex.size();
    }

    public boolean isEmpty() {
        return vertex.isEmpty();
    }

    public boolean contains(Object o) {
        return vertex.contains(o);
    }

    public Iterator<Integer> iterator() {
        return vertex.iterator();
    }

    public Object[] toArray() {
        return vertex.toArray();
    }

    public boolean add(Integer e) {
        return vertex.add(e);
    }

    public boolean remove(Object o) {
        return vertex.remove(o);
    }

    public void clear() {
        vertex.clear();
    }

    public Integer get(int index) {
        return vertex.get(index);
    }

    public void add(int index, Integer element) {
        vertex.add(index, element);
    }

    public Integer remove(int index) {
        return vertex.remove(index);
    }

    public int indexOf(Object o) {
        return vertex.indexOf(o);
    }

    public List<Integer> subList(int fromIndex, int toIndex) {
        return vertex.subList(fromIndex, toIndex);
    }

    public Object getStartTime() {
        return startTime;
    }

    public void setStartTime(Object startTime) {
        this.startTime = startTime;
    }

    public List<Integer> getVertex() {
        return vertex;
    }

    public void setVertex(List<Integer> vertex) {
        this.vertex = vertex;
    }
    /**
     * 
     * @param r
     * @return 
     */
    public static Object start(List<Object> r,int posTimeAttr) {
        String s = ((String) r.get(posTimeAttr)).split("-")[0];
      //  System.out.println("start :" + s);
        return Integer.parseInt(s);

    }
    /**
     * 
     * @param r
     * @return 
     */
    public static Object end(List<Object> r,int posTimeAttr) {
        String s = ((String) r.get(posTimeAttr)).split("-")[1];
       // System.out.println("end :" + s);
        return Integer.parseInt(s);

    }
    @Override
    public String toString() {
        return "L{" + startTime + "," + vertex + '}';
    }    

}
