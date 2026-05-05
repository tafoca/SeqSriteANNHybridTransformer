package seqgradualpatternexp.NumExp.composants;

import java.util.List;

/**
 *a pour but de retourner une liste (plutard pour des fin utile updatelist set)
 * et la memory a jour de M1 qui subira des modifications 
 * @author fotso
 */
public class ListMemComp {
    List<Integer> updateSet;
    int[] memory;

    public ListMemComp(List<Integer> updateSet, int[] memory) {
        this.updateSet = updateSet;
        this.memory = memory;
    }

    public List<Integer> getUpdateSet() {
        return updateSet;
    }

    public void setUpdateSet(List<Integer> updateSet) {
        this.updateSet = updateSet;
    }

    public int[] getMemory() {
        return memory;
    }

    public void setMemory(int[] memory) {
        this.memory = memory;
    }
    
    
    
}
