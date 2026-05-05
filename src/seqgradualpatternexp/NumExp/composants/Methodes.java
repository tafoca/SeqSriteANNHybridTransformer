package seqgradualpatternexp.NumExp.composants;

import java.util.List;

/**
 *
 * @author fotso
 */
public interface Methodes {

    List<Integer> rechercheDesCoupleCompatibleComplementaire(List<Kernel> objetsCouple, List<Integer> indices);

    Integer getIndiceOfObjetInListCouple(List<Kernel> objetsCouple, Integer node);

    Integer getObjetOfIndiceInListCouple(List<Kernel> objetsCouple, Integer indice);

    List<Integer> reverseList(List<Integer> list);

    void addOrNotNodeToParentSet(List<Integer> peres,boolean[][] tmp, boolean[][] adjM1, boolean[][] adjM2, Integer e, int e1, int e2);
}
