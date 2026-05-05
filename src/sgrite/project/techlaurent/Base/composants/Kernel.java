package sgrite.project.techlaurent.Base.composants;

import java.util.Objects;

/**
 *
 * @author fotso
 */
public class Kernel {

    Integer node;
    Integer numOrdre;

    public Kernel(Integer node, Integer numOrdre) {
        this.node = node;
        this.numOrdre = numOrdre;
    }

    public Integer getNode() {
        return node;
    }

    public void setNode(Integer node) {
        this.node = node;
    }

    public Integer getNumOrdre() {
        return numOrdre;
    }

    public void setNumOrdre(Integer numOrdre) {
        this.numOrdre = numOrdre;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Kernel other = (Kernel) obj;
        if (!Objects.equals(this.node, other.node)) {
            return false;
        }
        if (!Objects.equals(this.numOrdre, other.numOrdre)) {
            return false;
        }
        return true;
    }
}
