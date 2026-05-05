package seqgradualpatternexp.test;

import java.util.HashMap;
import java.util.Map;
import net.sourceforge.jFuzzyLogic.FIS;

/**
 *
 * @author fotso
 */
public class TestTipper {

    public static String fileName = "fcl/maket.fcl";
    static Map<String, Double> valuePrice = new HashMap<>();
    static Map<String, Double> valueConsumption = new HashMap<>();
    static Map<String, Double> valueSales = new HashMap<>();

    public static void main(String[] args) throws Exception {
       /* FIS fis = getFISByFileDesc(fileName);

        determineFuzzyItemByValueOfItem(fis, "price", 0.5);
        determineFuzzyItemByValueOfItem(fis, "consumption", 0.5);
        determineFuzzyItemByValueOfItem(fis, "sales", 0.5);

         // Evaluate
        // fis.evaluate();*/
    }

    public static  Map<String, Double> determineFuzzyItemByValueOfItem(FIS fis, String item, Double valueOfItem) {
        // Set inputs
        fis.setVariable(item, valueOfItem);
        Map<String, Double> valuePrice1 = saveMembershipValues(fis, item);
        return valuePrice1;
    }

    public static FIS getFISByFileDesc(String fileName) {
        // Load from 'FCL' file
        FIS fis = FIS.load(fileName, true);
        // Error while loading?
        if (fis == null) {
            System.err.println("Can't load file: '"
                    + fileName + "'");
        }
        return fis;
    }

    /**
     *
     * @param fis
     * @param item item floue
     */
    public static void displayMembershipValues(FIS fis, String item) {
        // Show output variable's chart
        // fis.getVariable("tip").chartDefuzzifier(true);
        fis.getVariable(item).getLinguisticTerms().values().stream().forEach(lt -> {
            System.out.println(item + "| " + lt.getTermName() + " :" + fis.getVariable(item).getMembership(lt.getTermName()));
        });

        // Print ruleSet
        //System.out.println(fis);
    }

    public static Map<String, Double> saveMembershipValues(FIS fis, String item) {
        Map<String, Double> mapFuzzyValueItem = new HashMap<>();
        // Show output variable's chart
        // fis.getVariable("tip").chartDefuzzifier(true);
        fis.getVariable(item).getLinguisticTerms().values().stream().forEach(lt -> {
            mapFuzzyValueItem.put(lt.getTermName(), fis.getVariable(item).getMembership(lt.getTermName()));

            //System.out.println(item + "| " + lt.getTermName() + " :" + fis.getVariable(item).getMembership(lt.getTermName()));
        });
      //  mapFuzzyValueItem.put(item, fis.getVariable(item).getValue());
        // Print ruleSet
        System.out.println(mapFuzzyValueItem);
        return mapFuzzyValueItem;
    }

}
