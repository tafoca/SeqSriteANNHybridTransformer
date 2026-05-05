/*
 */
package seqgradualpatternexp.ca.pfv.spmf.algorithms.sequentialpatterns.prefixspan;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Association entre item graduel (ig) list apparition seId et les support associe a chaque ig
 *
 * @author FOTSO
 */
public class ItemGIDsSGs {
    String gp;
    List<Integer> sequenceIDs = new ArrayList<>();
    List<Float> SGByIDs = new ArrayList<>();
    public ItemGIDsSGs(String gp) {
        this.gp = gp;
    }

    public String getGp() {
        return gp;
    }

    public void setGp(String gp) {
        this.gp = gp;
    }

    public List<Integer> getSequenceIDs() {
        return sequenceIDs;
    }

    public void setSequenceIDs(List<Integer> sequenceIDs) {
        this.sequenceIDs = sequenceIDs;
    }

    public List<Float> getSGByIDs() {
        return SGByIDs;
    }

    public void setSGByIDs(List<Float> SGByIDs) {
        this.SGByIDs = SGByIDs;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.gp);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ItemGIDsSGs other = (ItemGIDsSGs) obj;
        if (!Objects.equals(this.gp, other.gp)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ItemGIDsSGs{" + "gp=" + gp + ", sequenceIDs=" + sequenceIDs + ", SGByIDs=" + SGByIDs + '}';
    }
    
    
    
}
