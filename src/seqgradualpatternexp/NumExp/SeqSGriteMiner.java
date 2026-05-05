package seqgradualpatternexp.NumExp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.python.util.PythonInterpreter;
import seqgradualpatternexp.NumExp.composants.MotifsGraduels;
import seqgradualpatternexp.TemplateMethod;
import static seqgradualpatternexp.TemplateMethod.initBD;
import static seqgradualpatternexp.TemplateMethod.itemsets;
import seqgradualpatternexp.Tools;
import seqgradualpatternexp.ca.pfv.spmf.algorithms.sequentialpatterns.prefixspan.AlgoPrefixSpan;
import seqgradualpatternexp.ca.pfv.spmf.algorithms.sequentialpatterns.prefixspan.AlgoPrefixSpanGS;

/**
 *
 * @author fotso
 */
public class SeqSGriteMiner extends TemplateMethod {

	public static Map<Integer, Map<Integer, ArrayList<MotifsGraduels>>> objetMotifsGradfrequentsBylevel = new HashMap<>();// M.G.
																															// place
																															// par
																															// niveau
																															// de
																															// decouverte
	Map<Integer, ArrayList<MotifsGraduels>> motifsGraduelSequences = new HashMap<>();
	public String fileData = "ou-stmf/out/USA.csv";/*"ou-stmf/out/NOR.csv";/*D1-efwseq2014-2019.csv";/*""ue_583-589_MED3_2022-bin.csv";/* ue_565-574_MED1_2022.csv
	ue_565-574_MED1_2022-bin.csv    ue_583-589_MED3_2022.csv	ue_583-589_MED3_2022-bin.csv*/
	/* 0.4 MIN SUP
	ou-stmf/out/AUS.csv   ou-stmf/out/AUT.csv
ou-stmf/out/BEL.csv   ou-stmf/out/BGR.csv  ou-stmf/out/CAN.csv ou-stmf/out/CHE.csv ou-stmf/out/CHL.csv ou-stmf/out/CZE.csv

ou-stmf/out/DEUTNP.csv        ou-stmf/out/DNK.csv
ou-stmf/out/ESP.csv           ou-stmf/out/EST.csv
ou-stmf/out/FIN.csv ou-stmf/out/FRATNP.csv
ou-stmf/out/GBR_NIR.csv ou-stmf/out/GBR_SCO.csv ou-stmf/out/GBRTENW.csv ou-stmf/out/GRC.csv
ou-stmf/out/HRV.csv ou-stmf/out/HUN.csv
ou-stmf/out/ISL.csv ou-stmf/out/ISR.csv          ou-stmf/out/ITA.csv
ou-stmf/out/KOR.csv
ou-stmf/out/LTU.csv  ou-stmf/out/LUX.csv  ou-stmf/out/LVA.csv
ou-stmf/out/NOR.csv                ou-stmf/out/NZL_NP.csv
ou-stmf/out/POL.csv  ou-stmf/out/PRT.csv
     ou-stmf/out/RUS.csv
ou-stmf/out/SVK.csv  ou-stmf/out/SVN.csv  ou-stmf/out/SWE.csv
ou-stmf/out/TWN.csv
      ou-stmf/out/USA.csv
	 */
														// stmf-AUS2015-2020.csv stmf-PTR2015-2020.csv
														// stmf-PTR2015-2020.csv stmf-USA2015-2020.csv
														// //D2-efwseq2014-2019.csv "efwseq2014-2019.csv"; //
														// customersale.dat "eco-informel.csv";// "test.dat"
	Map<Integer, ArrayList<MotifsGraduels>> mgfsBysession = new HashMap<>();
	public String outData = "outseqDB.txt";
	/**
	 * writer to write output file
	 */
	BufferedWriter writer = null;

	public SeqSGriteMiner() throws IOException, Exception {
		FileWriter fw = new FileWriter(new File("datasets.csv"));
		fw.close();

		long startTime = System.currentTimeMillis();
		initMethode();
		if (writer != null) {
			writer.close();
		}

		System.out.println();
		System.out.println("========= Running the algorithm ========");

		String[] parameters = new String[] { "0.4", "50", "true" };
		String inputFile = "bdsequentiel.txt";// "contextPrefixSpanIG.txt";//"contextPrefixSpanGradual.txt"; small.txt
		String outputFile = "./output.txt";
		String outputfile2 = "./output2.txt";
		// AlgoPrefixSpan algoPrefixSpan = new AlgoPrefixSpan();
		AlgoPrefixSpanGS algoPrefixSpan = new AlgoPrefixSpanGS();
		// algoPrefixSpan.runAlgorithm(inputFile, 0.5, null);
		double minsupRelative = 0.4; //0.2 seuil minimum support motif sequentiel
		algoPrefixSpan.runAlgorithm(inputFile, minsupRelative, outputFile);
		/*
		 * PythonInterpreter pythonInterpreter =new PythonInterpreter();
		 * pythonInterpreter.execfile(System.getProperty("user.dir") +
		 * "/pythonScript/ann.py");
		 */
		givenPythonScript_whenPythonProcessInvoked_thenSuccess();
		long endTime = System.currentTimeMillis() - startTime;
		// TODO CREATION DES STTISTIQUES
		saveStatisticTime(minsupRelative, endTime);
		int nbpatterns1 = countNbPatternsInFile(outputFile);
		int nbpatterns2 = countNbPatternsInFile(outputfile2);
		saveStatisticNbPatterns(minsupRelative, nbpatterns1, nbpatterns2);
	}

	public SeqSGriteMiner(double min, double max, double pas) throws IOException, Exception {

		for (double i = min; i <= max; i = (i + pas)) {
			FileWriter fw = new FileWriter(new File("datasets.csv"));
			fw.close();
			long startTime = System.currentTimeMillis();
			initMethode();
			if (writer != null) {
				writer.close();
			}
			System.out.println();
			System.out.println("========= Running the algorithm ========");
			String[] parameters = new String[] { "0.4", "50", "true" };
			String inputFile = "bdsequentiel.txt";// "contextPrefixSpanIG.txt";//"contextPrefixSpanGradual.txt";
													// small.txt
			String outputFile = "./output.txt";
			String outputfile2 = "./output2.txt";
			AlgoPrefixSpanGS algoPrefixSpan = new AlgoPrefixSpanGS();
			double minsupRelative = i; // seuil minimum support motif sequentiel
			algoPrefixSpan.runAlgorithm(inputFile, minsupRelative, outputFile);
			givenPythonScript_whenPythonProcessInvoked_thenSuccess();
			long endTime = System.currentTimeMillis() - startTime;
			// TODO CREATION DES STTISTIQUES
			saveStatisticTime(minsupRelative, endTime);
			int nbpatterns1 = countNbPatternsInFile(outputFile);
			int nbpatterns2 = countNbPatternsInFile(outputfile2);
			saveStatisticNbPatterns(minsupRelative, nbpatterns1, nbpatterns2);
		}

	}

	/**
	 * 
	 * @param minsupRelative
	 * @param endTime
	 * @throws IOException
	 */
	public void saveStatisticTime(double minsupRelative, long endTime) throws IOException {
		FileWriter fw = new FileWriter(new File("TimeSeqSGriteMinerANN"), true);
		//fw.write(AppConstants.THREHOLD + AppConstants.SEP + minsupRelative + AppConstants.SEP + endTime / 1000.0);
		fw.write( minsupRelative + AppConstants.SEP + endTime / 1000.0);
		fw.flush();
		fw.write("\n");
		fw.close();
	}

	/**
	 * 
	 * @param minsupRelative
	 * @param nbpatterns1
	 * @param nbpatterns2
	 * @throws IOException
	 */
	public void saveStatisticNbPatterns(double minsupRelative, int nbpatterns1, int nbpatterns2) throws IOException {
		FileWriter fw = new FileWriter(new File("#SeqSGriteMiner"), true);
		fw.write(minsupRelative + AppConstants.SEP + nbpatterns1);
		fw.flush();
		fw.write("\n");
		fw.close();
		FileWriter fw2 = new FileWriter(new File("#SeqSGriteMinerANN"), true);
		fw2.write(minsupRelative + AppConstants.SEP + nbpatterns2);
		fw2.flush();
		fw2.write("\n");
		fw2.close();
	}

	/**
	 * 
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public int countNbPatternsInFile(String filename) throws IOException {
		int countTrans = 0;
		BufferedReader data_in;
		data_in = new BufferedReader(new FileReader(filename));
		while (data_in.ready()) {
			String line = data_in.readLine();
			if (line.matches("\\s*")) {
				continue; // be friendly with empty lines
			}
			countTrans++;
		}
		data_in.close();
		return countTrans;
	}

	/**
	 * execution pyhton approach 1 by process call
	 *
	 * @throws Exception pythonScript
	 */
	public void givenPythonScript_whenPythonProcessInvoked_thenSuccess() throws Exception {
		System.out.println(System.getProperty("user.dir"));

		ProcessBuilder processBuilder = new ProcessBuilder("python3",
				System.getProperty("user.dir") + "/pythonScript/ann.py");
		processBuilder.redirectErrorStream(true);
		Process process = processBuilder.start();
		InputStream inputStream = process.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		String lines = null;

		while ((lines = reader.readLine()) != null) {
			System.out.println("lines: " + lines);
		}

	}

	/**
	 *
	 * @param lineSession
	 * @throws IOException
	 */
	private void savePatterns(ArrayList<MotifsGraduels> lineSession) throws IOException {
		if (writer != null) {
			// create a StringBuilder
			StringBuilder r = new StringBuilder();
			for (MotifsGraduels m : lineSession) {
				int t = m.motifs.length / 2;
				StringBuilder ritem = new StringBuilder();
				// construction motif graduel chaine
				for (int i = 0; i < t; i++) {

					ritem.append(m.motifs[(2 * i)]);
					ritem.append(m.motifs[(2 * i + 1)]);
					if (!AppConstants.SAVE_MEASURE) {
						ritem.append(" ");
					}

				}
				r.append(ritem.toString());
				// TODO SAUVEGARDE DU SUPPORT DS BD sequentiel public static String SAVE_MEASURE
				// = true;
				if (AppConstants.SAVE_MEASURE) {
					r.append(";");
					r.append(m.SG / 100);
					r.append(";");
					r.append(m.SGM / 100);
					r.append(" ");
				}
				r.append("-1 ");

			}
			r.append("-2");
			// write the string to the file
			writer.write(r.toString());
			// start a new line
			writer.newLine();
			System.out.println("\n :: " + r.toString());
		}
	}

	/**
	 * Initialisation de structure necessaire pour fouille les sequentiel graduel a
	 * partir de BD numerique
	 *
	 * @throws IOException
	 */
	@Override
	public void initMethode() throws IOException {
		// TODO code application logic here
		Tools myTools = new Tools();
		myTools.initParameter(fileData);
		taille = myTools.getNbTransaction();
		nbtransaction = myTools.getNbTransaction();
		nbitems = myTools.getItemNembers();
		// construct db
		getconfig();
		itemsets.clear();
		itemsets = getDataSet(fileData);
		// end of construction db
		item = null;
		dataset = methods.duplique(itemsets);
		// affiche(dataset);
		// getAllColum(dataset, item, 1, taille);
		// DeltaDb = algoTEDOneDataSequence(dataset, 1);
		Set<Integer> keySet = initBD.keySet();
		List<Integer> l = new ArrayList<>(keySet);
		Collections.sort(l);
		// algoTED(l);
		// algoTED1(l);
		mgfsBysession = algoTED1V2(l);

		// writer = new BufferedWriter(new FileWriter(outData));
		writer = new BufferedWriter(new FileWriter("bdsequentiel.txt"));
		mgfsBysession.entrySet().stream().map((entry) -> {
			Integer key = entry.getKey();
			return entry;
		}).forEachOrdered((entry) -> {
			ArrayList<MotifsGraduels> value = entry.getValue();
			try {
				savePatterns(value);
			} catch (IOException ex) {
				Logger.getLogger(SeqSGriteMiner.class.getName()).log(Level.SEVERE, null, ex);
			}
		});

	}

	@Override
	public Object[][] algoTEDOneDataSequence(Object[][] Qdb, int objet) {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
																		// Tools | Templates.
	}

	@Override
	public void algoTED(List<Integer> listObj) {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
																		// Tools | Templates.
	}

	@Override
	public Object[][] algoTEDOneDataSequence1(Object[][] Qdb, int objet) {
		System.out.println("Session no : " + objet + "  : algoTEDOneDataSequence1() nbT: " + nbtransaction + "-- nbI: "
				+ (nbitems - 1));
		// TODO Extraction des frequents d'une session
		try {
			float[][] reduceDataColunm = reduceDataColunm(Qdb);
			System.out.println("\n \n ---- > Session no : " + objet + " \t item : " + (nbitems - 2) + " tr : "
					+ reduceDataColunm.length);
			SgriteOpt sg = new SgriteOpt(nbitems - 2, reduceDataColunm.length, reduceDataColunm, objet);
			objetMotifsGradfrequentsBylevel.put(objet, new HashMap<>(SgriteOpt.motifsGradfrequentsBylevel));
			SgriteOpt.motifsGradfrequentsBylevel.clear();
		} catch (IOException ex) {
			Logger.getLogger(SeqSGriteMiner.class.getName()).log(Level.SEVERE, null, ex);
		}
		return Qdb;
	}

	@Override
	public void algoTED1(List<Integer> listObj) {
		System.out.println("list sessions:  " + listObj);
		for (int i = 0; i < listObj.size(); i++) {
			Integer j = listObj.get(i);
			itemsets = initBD.get(j);
			dataset = methods.duplique(itemsets);
			// affiche(dataset);
			DeltaDb = algoTEDOneDataSequence1(dataset, j);
		}
		// System.out.println("**** " + objetMotifsGradfrequentsBylevel);
		// TODO Determiner les models commun aux differentes sessions + determination
		// des frequents retenue
		Map<Integer, ArrayList<MotifsGraduels>> out = detFreqSameLevel(objetMotifsGradfrequentsBylevel, listObj);
		System.out.println("<>" + out);

	}

	/**
	 * generation des frequents pour chacune des sessions
	 *
	 * @param listObj
	 * @return
	 */
	public Map<Integer, ArrayList<MotifsGraduels>> algoTED1V2(List<Integer> listObj) {
		System.out.println("list sessions:  " + listObj);
		for (int i = 0; i < listObj.size(); i++) {
			Integer j = listObj.get(i);
			itemsets = initBD.get(j);
			dataset = methods.duplique(itemsets);
			// affiche(dataset);
			DeltaDb = algoTEDOneDataSequence1(dataset, j);
		}
		// System.out.println("**** " + objetMotifsGradfrequentsBylevel);
		// TODO Determiner les models commun aux differentes sessions + determination
		// des frequents retenue
		Map<Integer, ArrayList<MotifsGraduels>> out = BuildFrequentsOrderBySession(objetMotifsGradfrequentsBylevel,
				listObj);
		System.out.println("<>" + out);
		return out;
	}

	/**
	 * construire les frequents de chaque sessions
	 *
	 * @param objetMotifsGradfrequentsBylevel
	 * @param listObj
	 * @return
	 */
	public Map<Integer, ArrayList<MotifsGraduels>> BuildFrequentsOrderBySession(
			Map<Integer, Map<Integer, ArrayList<MotifsGraduels>>> objetMotifsGradfrequentsBylevel,
			List<Integer> listObj) {
		Map<Integer, ArrayList<MotifsGraduels>> out = new HashMap<>();

		for (Map.Entry<Integer, Map<Integer, ArrayList<MotifsGraduels>>> entry : objetMotifsGradfrequentsBylevel
				.entrySet()) {
			Integer key = entry.getKey();
			out.put(key, new ArrayList<>());
			Map<Integer, ArrayList<MotifsGraduels>> value = entry.getValue();
			for (Map.Entry<Integer, ArrayList<MotifsGraduels>> entry1 : value.entrySet()) {
				Integer key1 = entry1.getKey();
				if (key1 > 1) {
					ArrayList<MotifsGraduels> value1 = entry1.getValue();
					out.get(key).addAll(value1);
				} else {
					ArrayList<MotifsGraduels> value1 = new ArrayList<>();
					int t = entry1.getValue().size() / 2;
					for (int i = 0; i < t; i++) {
						value1.add(entry1.getValue().get(2 * i + 1));
					}
					out.get(key).addAll(value1);
				}

			}
		}
		return out;
	}

	/**
	 * Determinaation des motifs graduels frequents de meme niveaux
	 *
	 * @param knowledgesG
	 * @param listObj
	 * @return
	 */
	public Map<Integer, ArrayList<MotifsGraduels>> detFreqSameLevel(
			Map<Integer, Map<Integer, ArrayList<MotifsGraduels>>> knowledgesG, List<Integer> listObj) {
		createUnionSetOfDiffLevel(listObj, knowledgesG);
		// TODO ensemble final de frequents par niveau
		Map<Integer, ArrayList<MotifsGraduels>> out = getSetofFrequentGlobal(listObj);
		return out;

	}

	/**
	 * TODO Construction de la D' BD de sequence graduel generer
	 *
	 * @param knowledgesG
	 * @param listObj
	 * @return
	 */
	public Map<Integer, ArrayList<MotifsGraduels>> buildsDBSameLevelAsSequence(
			Map<Integer, Map<Integer, ArrayList<MotifsGraduels>>> knowledgesG, List<Integer> listObj) {
		createUnionSetOfDiffLevel(listObj, knowledgesG);
		// TODO ensemble final de frequents par niveau
		// Map<Integer, ArrayList<MotifsGraduels>> out =
		return null;// out;

	}

	public Map<Integer, ArrayList<MotifsGraduels>> getSetofFrequentGlobal(List<Integer> listObj) {
		Map<Integer, ArrayList<MotifsGraduels>> out = new HashMap<>();
		for (Map.Entry<Integer, ArrayList<MotifsGraduels>> entrySet : motifsGraduelSequences.entrySet()) {
			Integer key = entrySet.getKey();
			ArrayList<MotifsGraduels> value = entrySet.getValue();
			ArrayList<MotifsGraduels> inputs = new ArrayList<>();
			boolean hasOneElt = false;
			if (value.size() >= 2) {
				MotifsGraduels m = value.get(0);
				MotifsGraduels m1 = new MotifsGraduels(m.motifs);
				m1.listSeqActive.add(m.hisSeqId);
				int i = 1, cpt = 1;
				float sG = m.SG;
				while (i <= value.size()) {
					System.out.println(i + ": i --------------: taille " + value.size());
					while (m.equals(value.get(i))) {
						System.out.println(key + ">>> " + value.get(i));
						sG += value.get(i).SG;
						m1.listSeqActive.add(value.get(i).hisSeqId);
						cpt++;
						i++;

						if (i >= value.size()) {
							break;
						}
					}
					if (i >= value.size()) {
						float support = (float) cpt / (float) listObj.size();
						if (support >= AppConstants.THREHOLD2) {
							m1.SGM = sG / cpt;
							inputs.add(m1);
							System.out.println("m1  : " + m1);
						}
						m1 = new MotifsGraduels(m.motifs);
						sG = m.SG;
						m = value.get(i - 1);
						System.out.println("m  : " + m);
						System.out.println("cpt : " + cpt);
						cpt = 0;
						break;
					} else {
						System.out.println("m  : " + m + " cpt : " + cpt + "taille: " + listObj.size());
						float support = (float) cpt / (float) listObj.size();
						if (support >= AppConstants.THREHOLD2) {
							m1.SGM = sG / cpt;
							inputs.add(m1);
							System.out.println("m1  : " + m1);
						}
						m = value.get(i);
						m1 = new MotifsGraduels(m.motifs);
						sG = 0;
						System.out.println("m  : " + m);
						System.out.println("cpt : " + cpt);
						cpt = 0;
					}
				}
			} else {
				if (true) { // si la liste n'a qu'un element
					System.out.println("size : " + value.size() + "   value [0] " + value.get(0));
					// TODO traitement des cas avec une seul occurence d'apparition fct du seuil de
					// ce candidat
					int cpt = 1;
					final MotifsGraduels m = value.get(0);
					System.out.println("m  : " + m + " cpt : " + cpt + "taille: " + listObj.size() + " cpt " + cpt);
					float support1 = (float) cpt / (float) listObj.size();
					System.out.println(" support ... " + support1);
					if (support1 >= AppConstants.THREHOLD2) {
						m.listSeqActive.add(m.hisSeqId);
						inputs.add(m);
						System.out.println("m1  : " + m);
						out.put(key, inputs);
					}
					break;
				}
			}
			out.put(key, inputs);
		}
		return out;
	}

	/**
	 *
	 * @param listObj
	 * @param knowledgesG
	 */
	public void createUnionSetOfDiffLevel(List<Integer> listObj,
			Map<Integer, Map<Integer, ArrayList<MotifsGraduels>>> knowledgesG) {
		int taille = listObj.size();
		int level = 1;
		while (level <= taille) {
			// TODO list = Union (des list du meme niveau ordonnee)
			ArrayList<MotifsGraduels> list = new ArrayList<>();
			for (Map.Entry<Integer, Map<Integer, ArrayList<MotifsGraduels>>> entrySet : knowledgesG.entrySet()) {
				Integer key = entrySet.getKey();
				Map<Integer, ArrayList<MotifsGraduels>> value = entrySet.getValue();
				if (value.containsKey(level)) {
					System.out.println("key :" + level);
					list.addAll(value.get(level));
				} else {
					System.out.println("not found key :" + level);// because some session can haven't a level
				}
				// System.out.println(level + " list : [] "+list);
			}
//            System.out.println(level + " list av  : [] " + list);
//            for (MotifsGraduels list1 : list) {
//                System.out.println("\n > " + list1 + " \n");
//            }
			Collections.sort(list);
//            System.out.println(level + " list ap  : [] " + list);
//            for (MotifsGraduels list1 : list) {
//                System.out.println("\n > " + list1 + " \n");
//            }
			motifsGraduelSequences.put(level, list);
			level++;
		}
		System.out.println(" motifsGraduelSequences >> \n " + motifsGraduelSequences);

	}

	@Override
	public double variationPositiveDegreeFunction(double degU, double degV) {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
																		// Tools | Templates.
	}

	@Override
	public double variationNegativeDegreeFunction(double degU, double degV) {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
																		// Tools | Templates.
	}

	@Override
	public void getAllColum(Object[][] dataset, Object[] item, int a, int taille) {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
																		// Tools | Templates.
	}

	@Override
	public Object[] getDataColByCol(Object[][] dataset, Object[] item, int a, int taille) {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
																		// Tools | Templates.
	}

	public static void main(String[] args) throws IOException, Exception {
		SeqSGriteMiner griteMiner = new SeqSGriteMiner();

		// SeqSGriteMiner
		// SeqSGriteMiner griteMiner = new SeqSGriteMiner(0.3, 0.7,0.02);
	}

}
