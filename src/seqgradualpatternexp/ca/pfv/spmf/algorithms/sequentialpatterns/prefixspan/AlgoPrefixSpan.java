package seqgradualpatternexp.ca.pfv.spmf.algorithms.sequentialpatterns.prefixspan;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import seqgradualpatternexp.ca.pfv.spmf.patterns.itemset_list_integers_without_support.Itemset;
import seqgradualpatternexp.ca.pfv.spmf.tools.MemoryLogger;

/**
 * *
 * This is a 2022 implementation of the PrefixSpan adapt for finding gradual
 * sequential pattern algorithm. PrefixSpan was proposed by Pei et al. 2001.
 *
 * NOTE: This implementation saves the pattern to a file as soon as they are
 * found or can keep the pattern into memory, depending on what the user choose.
 *
 * This implementation was done in 2022. It is different than the previous
 * implementation in SPMF which was implemented in 2016
 *
 */
public class AlgoPrefixSpan {

    /**
     * for statistics *
     */
    long startTime;
    long endTime;

    /**
     * the number of pattern found
     */
    int patternCount;

    /**
     * absolute minimum support
     */
    private int minsuppAbsolute;

    /**
     * writer to write output file
     */
    BufferedWriter writer = null;

    /**
     * The sequential patterns that are found (if the user want to keep them
     * into memory)
     */
    private SequentialPatterns patterns = null;

    /**
     * maximum pattern length in terms of item count
     */
    private int maximumPatternLength = 1000;

    /**
     * if true, sequence identifiers of each pattern will be shown
     */
    boolean showSequenceIdentifiers = false;

    /**
     * buffer for storing the current pattern that is mined when performing
     * mining the idea is to always reuse the same buffer to reduce memory
     * usage. *
     */
    final int BUFFERS_SIZE = 2000;
    private String[] patternBuffer = new String[BUFFERS_SIZE];

    /**
     * original sequence count *
     */
    int sequenceCount = 0;

    /**
     * the sequence database *
     */
    SequenceDatabase sequenceDatabase;

    /**
     * boolean indicating whether this database contains itemsets with multiple
     * items or not
     */
    boolean containsItemsetsWithMultipleItems = false;
    /**
     * sauvegarde par sous abre
     */
    Map<Integer, List<String>> mapSeqByPartSubTree = new HashMap<>();
    int level = 1;

    /**
     * Default constructor
     */
    public AlgoPrefixSpan() {
    }

    /**
     * Run the algorithm
     *
     * @param inputFile : a sequence database
     * @param minsupRelative : the minimum support as a percentage (e.g. 50%) as
     * a value in [0,1]
     * @param outputFilePath : the path of the output file to save the result or
     * null if you want the result to be saved into memory
     * @return return the result, if saved into memory, otherwise null
     * @throws IOException exception if error while writing the file
     */
    public SequentialPatterns runAlgorithm(String inputFile, double minsupRelative, String outputFilePath) throws IOException {
        // record start time
        startTime = System.currentTimeMillis();

        // reset memory
        MemoryLogger.getInstance().reset();

        // Load the sequence database
        sequenceDatabase = new SequenceDatabase();
        sequenceDatabase.loadFile(inputFile);
        sequenceCount = sequenceDatabase.size();

        System.out.println("begin ===== data base out put =====");
        sequenceDatabase.print();
        sequenceDatabase.printDatabaseStats();
        System.out.println("end ===== data base out put =====");
        // convert to a absolute minimum support
        this.minsuppAbsolute = (int) Math.ceil(minsupRelative * sequenceCount);
        if (this.minsuppAbsolute == 0) { // protection
            this.minsuppAbsolute = 1;
        }

        // run the algorithm
        prefixSpan(sequenceDatabase, outputFilePath);

        sequenceDatabase = null;

        // record end time
        endTime = System.currentTimeMillis();
        // close the output file if the result was saved to a file
        if (writer != null) {
            writer.close();
        }
        return patterns;
    }

    /**
     * Run the algorithm
     *
     * @param database : a sequence database
     * @param minsupPercent : the minimum support as an integer
     * @param outputFilePath : the path of the output file to save the result or
     * null if you want the result to be saved into memory
     * @return return the result, if saved into memory, otherwise null
     * @throws IOException exception if error while writing the file
     */
    public SequentialPatterns runAlgorithm(String inputFile, String outputFilePath, int minsup) throws IOException {
        // initialize variables for statistics
        patternCount = 0;
        MemoryLogger.getInstance().reset();
        // save the minsup chosen  by the user
        this.minsuppAbsolute = minsup;
        // save the start time
        startTime = System.currentTimeMillis();

        // Load the sequence database
        sequenceDatabase = new SequenceDatabase();
        sequenceDatabase.loadFile(inputFile);

        // run the algorithm
        prefixSpan(sequenceDatabase, outputFilePath);

        sequenceDatabase = null;

        // save the end time
        endTime = System.currentTimeMillis();
        // close the output file if the result was saved to a file
        if (writer != null) {
            writer.close();
        }

        return patterns;
    }

    /**
     * This is the main method for the PrefixSpan algorithm that is called to
     * start the algorithm
     *
     * @param outputFilePath an output file path if the result should be saved
     * to a file or null if the result should be saved to memory.
     * @param sequenceDatabase a sequence database
     * @throws IOException exception if an error while writing the output file
     */
    private void prefixSpan(SequenceDatabase sequenceDatabase, String outputFilePath) throws IOException {
        // if the user want to keep the result into memory
        if (outputFilePath == null) {
            writer = null;
            patterns = new SequentialPatterns("FREQUENT SEQUENTIAL PATTERNS");
        } else { // if the user want to save the result to a file
            patterns = null;
            writer = new BufferedWriter(new FileWriter(outputFilePath));
        }

        sequenceCount = sequenceDatabase.size();

        //============== CALCULATE FREQUENCY OF SINGLE ITEMS =============
        // We have to scan the database to find all frequent sequential patterns of size 1.
        // We note the sequences in which the items appear.
        Map<String, List<Integer>> mapSequenceID = findSequencesContainingItems();

        //====== Remove infrequent items and explore each projected database ================
        // if this database have multiple items per itemset
        if (containsItemsetsWithMultipleItems) {
            prefixspanWithMultipleItems(mapSequenceID);
        } else {
            // if this database does not have multiple items per itemset
            // we use an optimize version of the same code
            prefixspanWithSingleItems(mapSequenceID);
        }

        displayMapBySubTree();
        elagageByMapByAllSubTree();
        System.out.println("+++++++++++++++++++++++++++ ( after )+++++++++++++++++++++++++++");
        displayMapBySubTree();
        elagageByMapAllIntrsSubTree();
        System.out.println("+++++++++++++++++++++++++++ ( after 2nd prunning)+++++++++++++++++++++++++++");
        displayMapBySubTree();

    }

    public void elagageByMapAllIntrsSubTree() {
        Set<Integer> keySet = mapSeqByPartSubTree.keySet();

        for (Integer key : keySet) {
            // System.out.println("+++++++++++++++++++++++++++ (" + key + " )+++++++++++++++++++++++++++");
            elagageByMapIntraSubTree(key);

        }

    }

    public void elagageByMapByAllSubTree() {
        Set<Integer> keySet = mapSeqByPartSubTree.keySet();

        for (Integer key : keySet) {
            elagageByMapBySubTree(key);
            //   System.out.println("+++++++++++++++++++++++++++ (" +key +" )+++++++++++++++++++++++++++");

        }

    }

    /**
     * conversion d'un motifs graduel en tableau d'item gaduel
     *
     * @param mg
     * @return
     */
    List<String> explode(String mg) {
        List<String> tabItemG = new ArrayList<>();
        String trim = mg.trim();
        //System.out.println(mg + " >>>>> " + trim.length());
        for (int i = 0; i < trim.length() / 2; i++) {
            StringBuilder r = new StringBuilder();
            r.append(trim.charAt(2 * i));
            r.append(trim.charAt(2 * i + 1));
            tabItemG.add(r.toString());
        }
        return tabItemG;
    }

    /**
     * affichage d'une sequence representer par tab
     *
     * @param tab
     */
    void displayTab(String[] tab) {
        int i = 1;
        for (String e : tab) {
            if (i < tab.length) {
                System.out.print("(" + e + ") ");
            }
            i++;
        }
    }

    /**
     * test de inclusion entre 2 sequence ici represente par list 1 et list 2
     *
     * @param list1
     * @param list2
     * @return
     */
    boolean checkEqs(String[] list1, String[] list2) {
        int i = 0, j = 0, NB = 0;

        while (i < list1.length - 1 && j < list2.length - 1) {
            List<String> tabL1 = new ArrayList<>();
            tabL1 = explode(list1[i]);
            int k = 0;
            int nbOK = 0;
            while (k < tabL1.size()) {
                int ret = list2[j].indexOf(tabL1.get(k));
                if (ret != -1) {
                    nbOK++;
                    k++;
                } else {
                    break;
                }

            }
            if (nbOK != tabL1.size()) {
                j++;

            } else {
                NB++;
                i++;
                j++;
            }
        }
        boolean r = NB == list1.length - 1;
        /* displayTab(list1);
        System.out.print(" C ");
        displayTab(list2);
        System.out.println(" : " + r);*/
        return r;
    }

    public void displayMapBySubTree() {
        //       System.out.println("\n -------->   \n map: \n"+mapSeqByPartSubTree+"\n");
        mapSeqByPartSubTree.entrySet().stream().map((entry) -> {
            Integer key = entry.getKey();
            return entry;
        }).forEachOrdered((entry) -> {
            List<String> value = entry.getValue();
            value.forEach((_item) -> {
                System.out.println("\n" + _item);
            });
            System.out.println("=================================================");
        });
    }

    public void elagageByMapBySubTree(int level) {
        //   boolean remove = keySet.remove(level);
        List<String> levelToTraited = mapSeqByPartSubTree.get(level);// les sequentiel graduel frequent d'une memme sous-abre
        String[] split_e, split_e1 = null, split_e2 = null;
        for (int i = 0; i < levelToTraited.size(); i++) {
            for (int j = 0; j < levelToTraited.size(); j++) {
                if (i != j) {
                    String e = levelToTraited.get(i);
                    // System.out.println(i + ": >  " + e + "\n");
                    String e2 = levelToTraited.get(j);
                    //System.out.println(j + ": <  " + e2 + "\n");
                    //TODO Verfification inclusion entre coule du produit catessien
                    split_e1 = e.split("-1");
                    split_e2 = e2.split("-1");
                    boolean checkEqs = checkEqs(split_e1, split_e2);
                    if (checkEqs) {
                        String remove = levelToTraited.remove(i);
                        // System.out.println("Is removed ? : " + remove);
                        i = 0;
                        j = 0;
                    }

                }
            }
        }

        //  boolean checkEqs = checkEqs(split_e1, split_e2);
        /*  int i = 0;
        for (String e : levelToTraited) {
            System.out.println(i+": \n >  " + e);
           /* if (i == 4) {
                split_e1 = e.split("-1");
            } else if (i == 3) {
                split_e2 = e.split("-1");
            } else {
                split_e = e.split("-1");
            }
            for (String e2 : levelToTraited) {
                 boolean checkEqs = checkEqs(split_e1, split_e2);
            }
           
            i++;
        }*/
        //  boolean checkEqs = checkEqs(split_e1, split_e2);
    }

    public void elagageByMapIntraSubTree(int level) {
        //   boolean remove = keySet.remove(level);
        Set<Integer> keySet = mapSeqByPartSubTree.keySet();
        List<Integer> levels = new ArrayList<>(keySet);
        boolean remove = levels.remove((Integer) level);
        List<String> levelToTraited = mapSeqByPartSubTree.get(level);// les sequentiel graduel frequent d'une memme sous-abre
        String[] split_e, split_e1 = null, split_e2 = null;
        for (int i = 0; i < levelToTraited.size(); i++) {
            boolean bskip = false;
            int j = 0;
            while (j < levels.size()) {
                int otherLevel = levels.get(j);
                List<String> get = mapSeqByPartSubTree.get(otherLevel);
                if (levelToTraited.size() == 0) {
                    break;
                }
                String e = levelToTraited.get(i);

                for (int k = 0; k < get.size(); k++) {
                    String e2 = get.get(k);
                    /*System.out.println(i + ": >  " + e + "\n");
                    System.out.println(j + ": <  " + e2 + "\n");*/
                    split_e1 = e.split("-1");
                    split_e2 = e2.split("-1");
                    boolean checkEqs1 = checkEqs(split_e1, split_e2);
                    boolean checkEqs2 = checkEqs(split_e2, split_e1);
                    if (checkEqs1) {
                        String remove1 = levelToTraited.remove(i);
                        // System.out.println("Is removed ? : " + remove1);
                        if (levelToTraited.size() == 0) {
                            break;

                        } else {
                            i = 0;
                        }

                        //    j = 0;
                        bskip = true;
                        break;
                    } else {
                        bskip = false;
                    }
                    if (checkEqs2) {
                        String remove1 = get.remove(k);
                        i = 0;
                        //  System.out.println("Is removed ? : " + remove1);
                        /* i = 0;
                        j = j-1;*/
                        bskip = true;
                        break;
                    } else {
                        bskip = false;
                    }
                }
                j++;
                if (bskip) {
                    // i = 0;
                    j = j - 1;
                }
            }
        }
    }

    /**
     * Remove infrequent items and explore each projected databas for itemsets
     * of size 1
     *
     * @param mapSequenceID the set of items with their frequencies
     * @throws IOException if error writing to file
     */
    private void prefixspanWithSingleItems(
            Map<String, List<Integer>> mapSequenceID) throws IOException {
        //=============== REMOVE INFREQUENT ITEMS ========================
        // We scan the database to remove infrequent items  and resize sequences after removal
        // for each sequence in the current database
        for (int i = 0; i < sequenceDatabase.size(); i++) {
            String[] sequence = sequenceDatabase.getSequences().get(i);

            // we will copy the frequent items one by one but not those items that are infrequent
            // The following variable will be used to remember the position were to copy (we start at 0).
            int currentPosition = 0;

            // for each token in this sequence (item, separator between itemsets (-1) or end of sequence (-2)
            for (int j = 0; j < sequence.length; j++) {
                String token = sequence[j];

                // if it is an item because -1 and -2 are separators
                if (token.charAt(0) != '-') {
                    boolean isFrequent = mapSequenceID.get(token).size() >= minsuppAbsolute;

                    // if the item is frequent
                    if (isFrequent) {
                        // copy the item to the current position
                        sequence[currentPosition] = token;
                        // increment the current position
                        currentPosition++;
                    }
                } else if ("-2".equals(token)) {
                    // if the sequence is not empty after having removed the infrequent items
                    if (currentPosition > 0) {
                        // copy the item to the current position
                        sequence[currentPosition] = "-2";

                        // now replace the previous array with the new array
                        String[] newSequence = new String[currentPosition + 1];
                        System.arraycopy(sequence, 0, newSequence, 0, currentPosition + 1);
                        sequenceDatabase.getSequences().set(i, newSequence);
                        // continue to next sequence
                        continue;
                    } else {
                        // if the sequence is  empty, delete this sequence by replacing it with null
                        sequenceDatabase.getSequences().set(i, null);
                    }
                }
            }
        }

        //============= WE EXPLORE EACH PROJECTED DATABASE  ================================
        // For each frequent item
        for (Entry<String, List<Integer>> entry : mapSequenceID.entrySet()) {
            mapSeqByPartSubTree.put(level, new ArrayList<>());
            int support = entry.getValue().size();
            // if the item is frequent  (has a support >= minsup)
            if (support >= minsuppAbsolute) {
                String item = entry.getKey();

                // The prefix is a frequent sequential pattern.
                // We save it in the result.
                savePattern(item, support, entry.getValue());

                // We make a recursive call to try to find larger sequential
                // patterns starting with this prefix
                if (maximumPatternLength > 1) {

                    // Create the prefix for this projected database by copying the item in the buffer
                    patternBuffer[0] = item;

                    // build the projected database for that item
                    List<PseudoSequence> projectedDatabase = buildProjectedDatabaseSingleItems(item, entry.getValue());

                    // recursive call
                    recursionSingleItems(projectedDatabase, 2, 0);
                }
                level++;
            }
        }
    }

    /**
     * Remove infrequent items and explore each projected databas for itemsets
     * of size 1
     *
     * @param mapSequenceID the set of items with their frequencies
     * @throws IOException
     */
    private void prefixspanWithMultipleItems(Map<String, List<Integer>> mapSequenceID) throws IOException {

        //=============== REMOVE INFREQUENT ITEMS ========================
        // We scan the database to remove infrequent items  and resize sequences after removal
        // for each sequence in the current database
        for (int i = 0; i < sequenceDatabase.size(); i++) {
            String[] sequence = sequenceDatabase.getSequences().get(i);

            // we will copy the frequent items one by one but not those items that are infrequent
            // The following variable will be used to remember the position were to copy (we start at 0).
            int currentPosition = 0;
            // variable to count the number of items in the current itemset (after removing infrequent items)
            int currentItemsetItemCount = 0;

            // for each token in this sequence (item, separator between itemsets (-1) or end of sequence (-2)
            for (int j = 0; j < sequence.length; j++) {
                String token = sequence[j];

                // if it is an item
                if ('-' != token.charAt(0)) {
                    boolean isFrequent = mapSequenceID.get(token).size() >= minsuppAbsolute;

                    // if the item is frequent
                    if (isFrequent) {
                        // copy the item to the current position
                        sequence[currentPosition] = token;
                        // increment the current position
                        currentPosition++;
                        // increment the number of items in the current itemset
                        currentItemsetItemCount++;
                    }
                } else if ("-1".equals(token)) {
                    // if this itemset is not empty after having removed the infrequent items
                    if (currentItemsetItemCount > 0) {
                        // copy the itemset separator (-1) to the current position
                        sequence[currentPosition] = "-1";
                        // increment the current position
                        currentPosition++;
                        // reset the number of items in the current itemset for the next itemset
                        currentItemsetItemCount = 0;
                    }
                } else if ("-2".equals(token)) {
                    // if the sequence is not empty after having removed the infrequent items
                    if (currentPosition > 0) {
                        // copy the item to the current position
                        sequence[currentPosition] = "-2";

                        // now replace the previous array with the new array
                        String[] newSequence = new String[currentPosition + 1];
                        System.arraycopy(sequence, 0, newSequence, 0, currentPosition + 1);
                        sequenceDatabase.getSequences().set(i, newSequence);
                        // continue to next sequence
                        continue;
                    } else {
                        // if the sequence is  empty, delete this sequence by replacing it with null
                        sequenceDatabase.getSequences().set(i, null);
                    }
                }
            }
        }

        //============= WE EXPLORE EACH PROJECTED DATABASE  ================================
        // For each frequent item
        //TODO 3 ajouter initialisation et construction des # niveau de la map mapSeqByPartSubTree
        for (Entry<String, List<Integer>> entry : mapSequenceID.entrySet()) {
            int support = entry.getValue().size();
            // if the item is frequent  (has a support >= minsup)
            if (support >= minsuppAbsolute) {
                mapSeqByPartSubTree.put(level, new ArrayList<>());
                String item = entry.getKey();

                // The prefix is a frequent sequential pattern.
                // We save it in the result.
                savePattern(item, support, entry.getValue());

                // We make a recursive call to try to find larger sequential
                // patterns starting with this prefix
                if (maximumPatternLength > 1) {

                    // Create the prefix for this projected database by copying the item in the buffer
                    patternBuffer[0] = item;

                    // build the projected database for that item
                    List<PseudoSequence> projectedDatabase = buildProjectedDatabaseFirstTimeMultipleItems(item, entry.getValue());

                    // recursive call
                    recursion(patternBuffer, projectedDatabase, 2, 0);
                }
                level++;
            }

        }
    }

    /**
     * This method saves a sequential pattern containing a single item to the
     * output file or in memory, depending on if the user provided an output
     * file path or not when he launched the algorithm
     *
     * @param item the pattern to be saved.
     * @param support the support of this item
     * @param sequenceIDs the list of sequences containing this item
     * @throws IOException exception if error while writing the output file.
     */
    private void savePattern(String item, int support, List<Integer> sequenceIDs) throws IOException {
        // increase the number of pattern found for statistics purposes
        patternCount++;

        // if the result should be saved to a file
        if (writer != null) {
            // create a StringBuilder
            StringBuilder r = new StringBuilder();
            r.append(item);
            r.append(" -1 #SUP: ");
            r.append(support);
            mapSeqByPartSubTree.get(level).add(r.toString());

            if (showSequenceIdentifiers) {
                r.append(" #SID: ");
                for (Integer sid : sequenceIDs) {
                    r.append(sid);
                    r.append(" ");
                }
            }
            // write the string to the file
            writer.write(r.toString());
            // start a new line
            writer.newLine();
            System.out.println("\n :: " + r.toString());
        } // otherwise the result is kept into memory
        else {
            SequentialPattern pattern = new SequentialPattern();
            pattern.addItemset(new Itemset(item));
            pattern.setSequenceIDs(sequenceIDs);
            patterns.addSequence(pattern, 1);
        }
    }

    /**
     * Save a pattern containing two or more items to the output file (or in
     * memory, depending on what the user prefer)
     *
     * @param lastBufferPosition the last position in the buffer for this
     * pattern
     * @param pseudoSequences the list of pseudosequences where this pattern
     * appears.
     * @param length the pattern length in terms of number of items.
     * @throws IOException if error when writing to file
     */
    private void savePattern(int lastBufferPosition, List<PseudoSequence> pseudoSequences) throws IOException {
        // increase the number of pattern found for statistics purposes
        patternCount++;

        // if the result should be saved to a file
        if (writer != null) {

            // create a StringBuilder
            StringBuilder r = new StringBuilder();
            for (int i = 0; i <= lastBufferPosition; i++) {
                r.append(patternBuffer[i]);
                r.append(" ");
            }
            //-------------------------------------
            // PHILIPPE: BUG FIX 2017-10 : some -1 were missing in the output file
            // for some patterns. This fixes the problem.
            if (!"-1".equals(patternBuffer[lastBufferPosition])) {
                r.append("-1 ");
            }
            //-------------------------------------
            r.append("#SUP: ");
            r.append(pseudoSequences.size());
            mapSeqByPartSubTree.get(level).add(r.toString());
            if (showSequenceIdentifiers) {
                r.append(" #SID: ");
                for (PseudoSequence sequence : pseudoSequences) {
                    r.append(sequence.sequenceID);
                    r.append(" ");
                }
            }
            // write the string to the file
            writer.write(r.toString());
            // start a new line
            writer.newLine();
            System.out.println("\n :: " + r.toString());
        } // otherwise the result is kept into memory
        else {
            SequentialPattern pattern = new SequentialPattern();
            int itemsetCount = 0;
            Itemset currentItemset = new Itemset();
            for (int i = 0; i <= lastBufferPosition; i++) {
                String token = patternBuffer[i];
                if (token.charAt(0) != '-') {
                    currentItemset.addItem(token);
                } else if ("-1".equals(token)) {
                    pattern.addItemset(currentItemset);
                    currentItemset = new Itemset();
                    itemsetCount++;
                }
            }
            pattern.addItemset(currentItemset);
            itemsetCount++;

            List<Integer> sequencesIDs = new ArrayList<Integer>(pseudoSequences.size());
            for (int i = 0; i < pseudoSequences.size(); i++) {
                sequencesIDs.add(pseudoSequences.get(i).sequenceID);
            }
            pattern.setSequenceIDs(sequencesIDs);
//			System.out.println(pattern);
            patterns.addSequence(pattern, itemsetCount);
        }
    }

    /**
     * For each item, calculate the sequence id of sequences containing that
     * item
     *
     * @return Map of items to sequence IDs that contains each item
     */
    private Map<String, List<Integer>> findSequencesContainingItems() {
        // number of items in the current itemset 
        int itemCountInCurrentItemset;
        // We use a map to store the sequence IDs where an item appear
        // Key : item   Value :  a set of sequence IDs
        Map<String, List<Integer>> mapSequenceID = new HashMap<String, List<Integer>>();
        // for each sequence in the current database
        for (int i = 0; i < sequenceDatabase.size(); i++) {
            String[] sequence = sequenceDatabase.getSequences().get(i);

            itemCountInCurrentItemset = 0;

            // for each token in this sequence (item, separator between itemsets (-1) or end of sequence (-2)
            for (String token : sequence) {
                // if it is an item
                if ('-' != token.charAt(0)) {
                    // get the set of sequence IDs for this item until now
                    List<Integer> sequenceIDs = mapSequenceID.get(token);
                    if (sequenceIDs == null) {
                        // if the set does not exist, create one
                        sequenceIDs = new ArrayList<Integer>();
                        mapSequenceID.put(token, sequenceIDs);
                    }
                    // add the sequence ID to the 
                    // set of sequences IDs of this item
                    // if it is not already there
                    if (sequenceIDs.size() == 0 || sequenceIDs.get(sequenceIDs.size() - 1) != i) {
                        sequenceIDs.add(i);
                    }
                    itemCountInCurrentItemset++;
                    // if this itemset contains more than 1 item, we will remember that this database
                    // contains sequence with multiple items for optimization purpose.
                    if (itemCountInCurrentItemset > 1) {
                        containsItemsetsWithMultipleItems = true;
                    }
                } else if ("-1".equals(token)) {
                    itemCountInCurrentItemset = 0;
                }
            }
        }
//		System.out.println(mapSequenceID);
        return mapSequenceID;
    }

    /**
     * Create a projected database by pseudo-projection with the initial
     * database and a given item.
     *
     * @param item The item to use to make the pseudo-projection
     * @param sequenceDatabase2 The current database.
     * @param list The set of sequence ids containing the item
     * @return the projected database.
     */
    private List<PseudoSequence> buildProjectedDatabaseSingleItems(String item, List<Integer> sequenceIDs) {
        // We create a new projected database
        List<PseudoSequence> projectedDatabase = new ArrayList<PseudoSequence>();

        // for each sequence that contains the current item
        loopSeq:
        for (int sequenceID : sequenceIDs) {
            String[] sequence = sequenceDatabase.getSequences().get(sequenceID);

            // for each token in this sequence (item  or end of sequence (-2)
            for (int j = 0; !"-2".equals(sequence[j]); j++) {
                String token = sequence[j];

                // if it is the item that we want to use for projection
                if (token.equals(item)) {
                    // if it is not the end of the sequence
                    if (!"-2".equals(sequence[j + 1])) {
                        PseudoSequence pseudoSequence = new PseudoSequence(sequenceID, j + 1);
                        projectedDatabase.add(pseudoSequence);
                    }

                    // break because we have found what we have created the pseudosequence for the current sequence
                    continue loopSeq;
                }
            }
        }
        return projectedDatabase; // return the projected database
    }

    /**
     * Create a projected database by pseudo-projection with the initial
     * database and a given item.
     *
     * @param item The item to use to make the pseudo-projection
     * @param sequenceDatabase2 The current database.
     * @param list The set of sequence ids containing the item
     * @return the projected database.
     */
    private List<PseudoSequence> buildProjectedDatabaseFirstTimeMultipleItems(String item, List<Integer> sequenceIDs) {
        // We create a new projected database
        List<PseudoSequence> projectedDatabase = new ArrayList<PseudoSequence>();

        // for each sequence that contains the current item
        loopSeq:
        for (int sequenceID : sequenceIDs) {
            String[] sequence = sequenceDatabase.getSequences().get(sequenceID);

            // for each token in this sequence (item, separator between itemsets (-1) or end of sequence (-2)
            for (int j = 0; !"-2".equals(sequence[j]); j++) {
                String token = sequence[j];

                // if it is the item that we want to use for projection
                if (token.equals(item)) {
                    // if it is not the end of the sequence
                    boolean isEndOfSequence = "-1".equals(sequence[j + 1]) && "-2".equals(sequence[j + 2]);
                    if (isEndOfSequence == false) {
                        PseudoSequence pseudoSequence = new PseudoSequence(sequenceID, j + 1);
                        projectedDatabase.add(pseudoSequence);
                    }

                    // break because we have found what we have created the pseudosequence for the current sequence
                    continue loopSeq;
                }
            }
        }

        return projectedDatabase; // return the projected database
    }

    /**
     * Method to recursively grow a given sequential pattern.
     *
     * @param database the current projected sequence database
     * @param k the prefix length in terms of items
     * @param lastBufferPosition the last position used in the buffer for
     * storing the current prefix
     * @throws IOException exception if there is an error writing to the output
     * file
     */
    private void recursionSingleItems(List<PseudoSequence> database, int k, int lastBufferPosition) throws IOException {
//		for(int i=0; i<= lastBufferPosition; i++){
//			System.out.print(patternBuffer[i] + " ");
//		}
//		System.out.println();
//		System.out.println("---");

        // find frequent items of size 1 in the current projected database, and at the same
        // time create their respective projected databases
        Map<String, List<PseudoSequence>> itemsPseudoSequences = findAllFrequentPairsSingleItems(database, lastBufferPosition);

        // release the memory used by the database
        database = null;

//		for(Pair pair : pairs){
//			System.out.print(pair.item + " isPostfix? " + pair.isPostfix() + "    " );
//			for(PseudoSequence seq: pair.getPseudoSequences()){
//				System.out.print( seq.getOriginalSequenceID() + "," + seq.indexFirstItem + " ");
//			}
//			System.out.println();
//		}
//		System.out.println("DEBUG");
        // For each pair found (a pair is an item with a boolean indicating if it
        // appears in an itemset that is cut (a postfix) or not, and the sequence IDs
        // where it appears in the projected database).
        for (Entry<String, List<PseudoSequence>> entry : itemsPseudoSequences.entrySet()) {
            // if the item is frequent in the current projected database
            if (entry.getValue().size() >= minsuppAbsolute) {

                //Create the new pattern by appending the item as a new itemset to the sequence
                patternBuffer[lastBufferPosition + 1] = "-1";
                patternBuffer[lastBufferPosition + 2] = entry.getKey();

                // save the pattern
                savePattern(lastBufferPosition + 2, entry.getValue());

                // make a recursive call
                if (k < maximumPatternLength) {
                    recursionSingleItems(entry.getValue(), k + 1, lastBufferPosition + 2);
                }
            }
        }

        // check the current memory usage
        MemoryLogger.getInstance().checkMemory();
    }

    /**
     * Method to recursively grow a given sequential pattern.
     *
     * @param database the current projected sequence database
     * @param k the prefix length in terms of items
     * @param patternBuffer the current sequential pattern that we want to try
     * to grow
     * @param lastBufferPosition the last position used in the buffer for
     * storing the current prefix
     * @throws IOException exception if there is an error writing to the output
     * file
     */
    private void recursion(String[] patternBuffer, List<PseudoSequence> database, int k, int lastBufferPosition) throws IOException {
//		for(int i=0; i<= lastBufferPosition; i++){
//			System.out.print(patternBuffer[i] + " ");
//		}
//		System.out.println();
//		System.out.println("---");

        // FIND FREQUENT PAIRS
        // find frequent items of size 1 in the current projected database, and at the same
        // time create their respective projected databases
        // We create some maps of pairs for storing the frequent items. The following object
        // contains two maps. The first one is for item extending the current pattern as an i-extension,
        // while the second is for item extending the current pattern as an s-extension.
        MapFrequentPairs mapsPairs = findAllFrequentPairs(database, lastBufferPosition);

        //TOTEST 
        displayTraceExecprojectedDB(lastBufferPosition, patternBuffer, database, mapsPairs);

        // release the memory used by the database
        database = null;

        for (Entry<Pair, Pair> entry : mapsPairs.mapPairsInPostfix.entrySet()) {
            Pair pair = entry.getKey();

            // if the item is frequent in the current projected database
            if (pair.getCount() >= minsuppAbsolute) {

                int newBuferPosition = lastBufferPosition;
                // we append it to the last itemset of the prefix
                newBuferPosition++;
                patternBuffer[newBuferPosition] = pair.item;

                // save the pattern
                savePattern(newBuferPosition, pair.getPseudoSequences());

                // make a recursive call
                if (k < maximumPatternLength) {
                    recursion(patternBuffer, pair.getPseudoSequences(), k + 1, newBuferPosition);
                }
            }
        }

        // For each pair found representing an item that is not in a postfix
        for (Entry<Pair, Pair> entry : mapsPairs.mapPairs.entrySet()) {
            Pair pair = entry.getKey();
            // if the item is frequent in the current projected database
            if (pair.getCount() >= minsuppAbsolute) {

                int newBuferPosition = lastBufferPosition;
                // we append it to the last itemset of the prefix
                newBuferPosition++;
                patternBuffer[newBuferPosition] = "-1";
                newBuferPosition++;
                patternBuffer[newBuferPosition] = pair.item;

                // save the pattern
                savePattern(newBuferPosition, pair.getPseudoSequences());

                // make a recursive call
                if (k < maximumPatternLength) {
                    recursion(patternBuffer, pair.getPseudoSequences(), k + 1, newBuferPosition);
                }
            }
        }

        // check the current memory usage
        MemoryLogger.getInstance().checkMemory();
    }

    public void displayTraceExecprojectedDB(int lastBufferPosition, String[] patternBuffer1, List<PseudoSequence> database, MapFrequentPairs mapsPairs) {
        //TODO test print ponint 1
        String curentPatterns = "";
        String concat = null;
        if (lastBufferPosition >= 0) {

            // create a StringBuilder
            StringBuilder r = new StringBuilder();
            for (int i = 0; i <= lastBufferPosition; i++) {
                if (!patternBuffer1[i].equals("-1")) {
                    r.append("(");
                    r.append(patternBuffer[i]);
                    r.append(")");
                }

            }
            concat = r.toString();
            System.out.println("DB->" + concat + " \n " + database.toString() + "\n");
            //  System.out.println("DB-> \n " + database.toString() + "\n");
        }

        System.out.println("  \n mapPairs => \n " + mapsPairs.mapPairs.keySet() + "\n");
        System.out.println("  \n mapPairsInPostfix => \n " + mapsPairs.mapPairsInPostfix.keySet() + "\n");

//		for(Pair pair : pairs){
//			System.out.print(pair.item + " isPostfix? " + pair.isPostfix() + "    " );
//			for(PseudoSequence seq: pair.getPseudoSequences()){
//				System.out.print( seq.getOriginalSequenceID() + "," + seq.indexFirstItem + " ");
//			}
//			System.out.println();
//		}
//		System.out.println("DEBUG");
// For each pair found that is in a postfix itemset(a pair is an item with a boolean indicating if it
// appears in an itemset that is cut (a postfix) or not, and the sequence IDs
// where it appears in the projected database).
    }

    /**
     * Method to find all frequent gradual items in a projected sequence
     * database
     *
     * @param sequences the set of sequences
     * @param patternBuffer the current sequential pattern that we want to try
     * to grow
     * @param lastBufferPosition the last position used in the buffer for
     * storing the current prefix
     * @return A list of pairs, where a pair is an item with (1) a boolean
     * indicating if it is in an itemset that is "cut" and (2) the sequence IDs
     * where it occurs.
     */
    protected Map<String, List<PseudoSequence>> findAllFrequentPairsSingleItems(List<PseudoSequence> sequences, int lastBufferPosition) {
        // We use a Map the store the pairs.
        Map<String, List<PseudoSequence>> mapItemsPseudoSequences = new HashMap<>();

        // for each sequence
        for (PseudoSequence pseudoSequence : sequences) {

            // for each sequence
            int sequenceID = pseudoSequence.getOriginalSequenceID();
            String[] sequence = sequenceDatabase.getSequences().get(sequenceID);

            // for each token in this sequence 
            for (int i = pseudoSequence.indexFirstItem; !"-2".equals(sequence[i]); i++) {
                String token = sequence[i];

                // if it is an item
                if (token.charAt(0) != '-') {
                    // get the pair object stored in the map if there is one already
                    List<PseudoSequence> listSequences = mapItemsPseudoSequences.get(token);
                    // if there is no pair object yet
                    if (listSequences == null) {
                        listSequences = new ArrayList<>();
                        // store the pair object that we created
                        mapItemsPseudoSequences.put(token, listSequences);
                    }

                    // Check if that sequence as already been added to the projected database of this item
                    boolean ok = true;
                    if (listSequences.size() > 0) {
                        ok = listSequences.get(listSequences.size() - 1).sequenceID != sequenceID;
                    }
                    // if not we add it
                    if (ok) {
                        listSequences.add(new PseudoSequence(sequenceID, i + 1));
                    }
                }
            }
        }
        MemoryLogger.getInstance().checkMemory();  // check the memory for statistics.
        // return the map of pairs
        return mapItemsPseudoSequences;
    }

    /**
     * This class contains two maps, which are used for counting the frequencies
     * of items, whether in a postfix itemset or a normal itemset.
     */
    public class MapFrequentPairs {

        public final Map<Pair, Pair> mapPairs = new HashMap<>();
        public final Map<Pair, Pair> mapPairsInPostfix = new HashMap<>();
    };

    /**
     * Method to find all frequent items in a projected sequence database
     *
     * @param sequences the set of sequences
     * @param lastBufferPosition the last position used in the buffer for
     * storing the current prefix
     * @return A list of pairs, where a pair is an item with (1) a boolean
     * indicating if it is in an itemset that is "cut" and (2) the sequence IDs
     * where it occurs.
     */
    protected MapFrequentPairs findAllFrequentPairs(List<PseudoSequence> sequences, int lastBufferPosition) {
        // We use an object containing two maps the store the pairs.
        MapFrequentPairs mapsPairs = new MapFrequentPairs();

        // find the position of the first item of the last itemset of the current sequential pattern that is grown
        int firstPositionOfLastItemsetInBuffer = lastBufferPosition;
        while (lastBufferPosition > 0) {
            firstPositionOfLastItemsetInBuffer--;
            if (firstPositionOfLastItemsetInBuffer < 0 || "-1".equals(patternBuffer[firstPositionOfLastItemsetInBuffer])) {
                firstPositionOfLastItemsetInBuffer++;
                break;
            }
        };

        // use a variable to try to match the last itemset of the pattern in the buffer
        int positionToBeMatched = firstPositionOfLastItemsetInBuffer;

        // for each sequence
        for (PseudoSequence pseudoSequence : sequences) {

            // for each sequence
            int sequenceID = pseudoSequence.getOriginalSequenceID();
            String[] sequence = sequenceDatabase.getSequences().get(sequenceID);

            // check if the first itemset of that sequence is a postfix
            // It is a postfix (the itemset is cut) if the previous item is a -1 indicating
            // the end of an itemset
            String previousItem = sequence[pseudoSequence.indexFirstItem - 1];
            boolean currentItemsetIsPostfix = (!"- 1".equals(previousItem));
            boolean isFirstItemset = true;

            // for each token in this sequence (item, separator between itemsets (-1) or end of sequence (-2)
            for (int i = pseudoSequence.indexFirstItem; !"-2".equals(sequence[i]); i++) {
                String token = sequence[i];

                // if it is an item
                if (token.charAt(0) != '-') {

                    // create the pair corresponding to this item
                    Pair pair = new Pair(token);
                    // get the pair object store in the map if there is one already
                    Pair oldPair;
                    if (currentItemsetIsPostfix) {
                        oldPair = mapsPairs.mapPairsInPostfix.get(pair);
                    } else {
                        oldPair = mapsPairs.mapPairs.get(pair);
                    }
                    // if there is no pair object yet
                    if (oldPair == null) {
                        // store the pair object that we created
                        if (currentItemsetIsPostfix) {
                            mapsPairs.mapPairsInPostfix.put(pair, pair);
                        } else {
                            mapsPairs.mapPairs.put(pair, pair);
                        }
                    } else {
                        // otherwise use the old one
                        pair = oldPair;
                    }

                    // Check if that sequence as already been added to the projected database of this item
                    boolean ok = true;
                    if (pair.getPseudoSequences().size() > 0) {
                        ok = pair.getPseudoSequences().get(pair.getPseudoSequences().size() - 1).sequenceID != sequenceID;
                    }
                    // if not we add it
                    if (ok) {
                        pair.getPseudoSequences().add(new PseudoSequence(sequenceID, i + 1));
                    }

                    ///////// ====== IMPORTANT =========
                    // if the current itemset is a postfix and it is not the first itemset
                    // we must also consider that it may not be a postfix for extending the current prefix
                    if (currentItemsetIsPostfix && isFirstItemset == false) {
                        // create the pair corresponding to this item
                        pair = new Pair(token);     // FALSE !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                        // get the pair object store in the map if there is one already
                        oldPair = mapsPairs.mapPairs.get(pair);
                        // if there is no pair object yet
                        if (oldPair == null) {
                            // store the pair object that we created
                            mapsPairs.mapPairs.put(pair, pair);
                        } else {
                            // otherwise use the old one
                            pair = oldPair;
                        }

                        // Check if that sequence as already been added to the projected database of this item
                        ok = true;
                        if (pair.getPseudoSequences().size() > 0) {
                            ok = pair.getPseudoSequences().get(pair.getPseudoSequences().size() - 1).sequenceID != sequenceID;
                        }
                        // if not we add it
                        if (ok) {
                            pair.getPseudoSequences().add(new PseudoSequence(sequenceID, i + 1));
                        }

                    }
                    //////////////////////////////////////////////////////////

                    //  try to match this item with the last itemset in the prefix
                    if (currentItemsetIsPostfix == false && patternBuffer[positionToBeMatched] == token) {
                        positionToBeMatched++;
                        if (positionToBeMatched > lastBufferPosition) {
                            currentItemsetIsPostfix = true;
                        }
                    }

                } else if ("-1".equals(token)) {
                    isFirstItemset = false;
                    currentItemsetIsPostfix = false;
                    positionToBeMatched = firstPositionOfLastItemsetInBuffer;
                }
            }
        }
        MemoryLogger.getInstance().checkMemory();  // check the memory for statistics.
        // return the map of pairs
        return mapsPairs;
    }

    /**
     * Print statistics about the algorithm execution to System.out.
     *
     * @param size the size of the database
     */
    public void printStatistics() {
        StringBuilder r = new StringBuilder(200);
        r.append("=============  PREFIXSPAN 0.99-2016 - STATISTICS =============\n Total time ~ ");
        r.append(endTime - startTime);
        r.append(" ms\n");
        r.append(" Frequent sequences count : " + patternCount);
        r.append('\n');
        r.append(" Max memory (mb) : ");
        r.append(MemoryLogger.getInstance().getMaxMemory());
        r.append('\n');
        r.append(" minsup = " + minsuppAbsolute + " sequences.");
        r.append('\n');
        r.append(" Pattern count : ");
        r.append(patternCount);
        r.append('\n');
        r.append("===================================================\n");
        // if the result was save into memory, print it
        if (patterns != null) {
            patterns.printFrequentPatterns(sequenceCount, showSequenceIdentifiers);
        }
        System.out.println(r.toString());
    }

    /**
     * Get the maximum length of patterns to be found (in terms of item count)
     *
     * @return the maximumPatternLength
     */
    public int getMaximumPatternLength() {
        return maximumPatternLength;
    }

    /**
     * Set the maximum length of patterns to be found (in terms of item count)
     *
     * @param maximumPatternLength the maximumPatternLength to set
     */
    public void setMaximumPatternLength(int maximumPatternLength) {
        this.maximumPatternLength = maximumPatternLength;
    }

    /**
     * Set that the sequence identifiers should be shown (true) or not (false)
     * for each pattern found
     *
     * @param showSequenceIdentifiers true or false
     */
    public void setShowSequenceIdentifiers(boolean showSequenceIdentifiers) {
        this.showSequenceIdentifiers = showSequenceIdentifiers;
    }

}
