package seqgradualpatternexp.NumExp.composants;

/**
 *
 * @author fotso
 */
public class NodeWeight implements Comparable<NodeWeight> {

    Integer node;
    Integer poids;

    public NodeWeight(Integer node, int poids) {
        this.node = node;
        this.poids = poids;
    }

    public Integer getNode() {
        return node;
    }

    public void setNode(Integer node) {
        this.node = node;
    }

    public int getPoids() {
        return poids;
    }

    public void setPoids(int poids) {
        this.poids = poids;
    }

    @Override
    public int compareTo(NodeWeight o) {
        return poids.compareTo(o.getPoids());
    }

}
