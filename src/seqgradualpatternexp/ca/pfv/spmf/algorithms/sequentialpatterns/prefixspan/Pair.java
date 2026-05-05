package seqgradualpatternexp.ca.pfv.spmf.algorithms.sequentialpatterns.prefixspan;

import java.util.ArrayList;
import java.util.List;
import seqgradualpatternexp.NumExp.AppConstants;

/**
 * This class represents a pair of an (1) item (2) if it is contained in an
 * itemset that was cut or not (a postfix). and (3) its projected database
 *
 * This class is used by PrefixSpan.
 *
 * It is used for calculating the support of an item in a database.
 *
 * Copyright (c) 2021-2022 Tabueu Fotso Laurent
 *
 */
public class Pair {
    // the item

    protected final String item;

    // List of the pseudosequences of the projection with this item .
    private List<PseudoSequence> pseudoSequences = new ArrayList<PseudoSequence>();

    /**
     * Constructor
     *
     * @param postfix indicate if this is the case of an item appearing in an
     * itemset that is cut at the left because of a projection
     * @param item the gradual item
     */
    public Pair(String item) {
        this.item = item;
    }

    /**
     * Check if two pairs are equal (same item and both appears in a postfix or
     * not).
     *
     * @param object
     * @return true if equals.
     */
    @Override
    public boolean equals(Object object) {
        Pair pair = (Pair) object;
        if (AppConstants.SAVE_MEASURE) {
           return (pair.item == null ? this.item == null : pair.item.split(";")[0].equals(this.item.split(";")[0])); 
        }else{
           return (pair.item == null ? this.item == null : pair.item.equals(this.item)); 
        }
        
    }

    /**
     * Method to calculate an hashcode (because pairs are stored in a map).
     *
     * @return
     */
    @Override
    public int hashCode() {// Ex: 127333,P,X,1  127333,N,Z,2
        // transform it into a string
        // then use the hashcode method from the string class
        return item.hashCode() + "".hashCode();
    }

    /**
     * Get the item represented by this pair
     *
     * @return the item.
     */
    public String getItem() {
        return item;
    }

    /**
     * Get the support of this item (the number of sequences containing it).
     *
     * @return the support (an integer)
     */
    public int getCount() {
        return pseudoSequences.size();
    }

    /**
     * Get the list of sequence IDs associated with this item.
     *
     * @return the list of sequence IDs.
     */
    public List<PseudoSequence> getPseudoSequences() {
        return pseudoSequences;
    }

    @Override
    public String toString() {
        return "" + item + " => " + pseudoSequences+
                "\n";
    }
}
