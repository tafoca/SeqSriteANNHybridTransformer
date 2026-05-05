/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seqgradualpatternexp.ca.pfv.spmf.algorithms.sequentialpatterns.prefixspan;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 *
 * @author FOTSO
 */
public class Execution {

    public static void main(String[] args) throws UnsupportedEncodingException, IOException {
        // Third example: run the selected algorithm

        System.out.println();
        System.out.println("========= Running the algorithm ========");

        String[] parameters = new String[]{"0.4", "50", "true"};
        String inputFile = "bdsequentiel.txt";//"contextPrefixSpanIG.txt";//"contextPrefixSpanGradual.txt"; small.txt 
        String outputFile = "./output.txt";
        AlgoPrefixSpan algoPrefixSpan = new AlgoPrefixSpan();
       // algoPrefixSpan.runAlgorithm(inputFile, 0.5, null);
        algoPrefixSpan.runAlgorithm(inputFile, 0.5, outputFile);

    }

}
