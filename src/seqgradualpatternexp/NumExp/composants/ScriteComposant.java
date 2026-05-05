/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seqgradualpatternexp.NumExp.composants;

import java.util.ArrayList;

/**
 *
 * @author fotso
 */
public class ScriteComposant {
    String[] motifs; 
    boolean[][] adjMotifs;
    ArrayList<Integer> lIsoleObj;

    public ScriteComposant(String[] motifs, boolean[][] adjMotifs, ArrayList<Integer> lIsoleObj) {
        this.motifs = motifs;
        this.adjMotifs = adjMotifs;
        this.lIsoleObj = lIsoleObj;
    }

    public String[] getMotifs() {
        return motifs;
    }

    public void setMotifs(String[] motifs) {
        this.motifs = motifs;
    }

    public boolean[][] getAdjMotifs() {
        return adjMotifs;
    }

    public void setAdjMotifs(boolean[][] adjMotifs) {
        this.adjMotifs = adjMotifs;
    }

    public ArrayList<Integer> getlIsoleObj() {
        return lIsoleObj;
    }

    public void setlIsoleObj(ArrayList<Integer> lIsoleObj) {
        this.lIsoleObj = lIsoleObj;
    }
    
    
    
    
}
