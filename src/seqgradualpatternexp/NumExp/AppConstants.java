package seqgradualpatternexp.NumExp;

/**
 *
 * @author fotso
 */
public class AppConstants {
//    
//    public static final String TRANSACTIONFILE = "F20Att200Li.dat";// F20Att200Li.dat F20Att500Li.dat ; TMET10I10.dat; temp.dat, test_merge.dat,test_equal.dat  | F30Att100Li.dat ,meteo.csv, test.dat, test3,test2,test2_0-5.
//    public static final double MIN = (float) 0.05;//0.03;
//    public static final double MAX = (float) 0.31;//0.8
//    public static final double STEP = (float) 0.01;//0.01
//    public static final double THREHOLD = (float) 0.01;//0.01; 0.09
    // public static final String TRANSACTIONFILE = "test.dat";
//meteo3-4-11-2020.dat
//meteo3-4-11-2020_469Li-nxt2.dat
//meteo3-4-11-2020_600Li.dat
//meteo3-4-11-2020_601Li-nxt.dat
//    public static final String TRANSACTIONFILE = "meteo3-4-11-2020_600Li.dat";//winequality-red.data"dataset4.dat";//; "F30Att100Li.dat" ;"F20Att200Li.dat";//"dataset4.dat";//test.dat";
//    public static final double MIN = (float) 0.04;//0.02 0.05  0.02; 0.03;0.08
//    public static final double MAX = (float) 0.25;// 0.2 0.32 0.8, 0.5; 0.6
//    public static final double STEP = (float) 0.01;//0.01
//    public static final double THREHOLD = (float) 0.05;// 0.1 0.15;;0.05 ;0.03; 0.01; 0.09
//    public static String OUPUTMFSFILE = "out_GritestrictMaximaux.dat";
//    public static String SEP = ";";
//    public static final int NbmaxBorneSupInteret = 2; //10
//    public static boolean IsAScFindMethod = false;
//    public static boolean IsTwoWayFindMethod = true;
//    public static int NBMAXEXTRAIT = 0; //10
//    public static final int MEMORY = 8;
//    public static int NBTRANS = 0;

    public static final String TRANSACTIONFILE = "D1-efwseq2014-2019.csv";//"bdsalaire.csv";// D1-efwseq2014-2019.csv "fundamentals40A300T.csv";//;"test.dat";//bdsalaire.csv C250-A100-50.dat meteo3-4-11-2020_600Li.dat TMET10I10.dat C250-A100-50.dat;winequality-red.data"dataset4.dat";//; "F30Att100Li.dat" ;"F20Att200Li.dat";//"dataset4.dat";//test.dat";
    /*case C250-A100-50.dat
    public static final double MIN = (float) 0.05;//0.04  0.02 0.05  0.02; 0.03;0.08
    public static final double MAX = (float) 0.5;//0.16 0.21 0.32 0.8, 0.5; 0.6
    public static final double STEP = (float) 0.04;//0.01
    public static final double THREHOLD = (float) 0.05;// 0.02 0.1 0.15;;0.05 ;0.03; 0.01; 0.09*/
 /* not this C250-A100-50.dat ; sauf ce jeu*/
    public static final double MIN = (float) 0.05;//0.05 0.04  0.02 0.05  0.02; 0.03;0.08
    public static final double MAX = (float) 0.21;//0.16 0.21 0.32 0.8, 0.5; 0.6
    public static final double STEP = (float) 0.02;//0.01
    public static final double THREHOLD = (float) 0.1;//0.24 0.2 0.3 0.8 0.05; 0.02 0.1 0.15;;0.05 ;0.03; 0.01; 0.09
    public static String OUPUTMFSFILE = "TimeMPSGrite";
    public static String SEP = " "; //;
    public static final int NbmaxBorneSupInteret = 1; //15 9; 11
    public static boolean IsAScFindMethod = true;
    public static boolean IsTwoWayFindMethod = false;
//    public static boolean IsAScFindMethod = true;
//    public static boolean IsTwoWayFindMethod = false;
    public static int NBMAXEXTRAIT = 0; //10
    public static final int MEMORY = 8;
    public static int NBTRANS = 0;
    public static int NBTITEMPART1 = 35;//8 20;//8 //10
    public static int NBTITEMPART2 = 28;//4 7;//2 //2
    //memory used saving
    public static double USEDMEMORY = 0;//7;//2 //2
    public static double THREHOLD2 = (float) 0.01;// extract mean pattern 
    public static String SEP_MEASURE = ";";
    public static boolean SAVE_MEASURE = true;
    public static final double THREHOLDWGS = (float) 0.05;
}