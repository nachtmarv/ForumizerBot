package Discord;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;

/**
 * Handles all file and console interactions.
 * 
 * @author Marvin Weisbrod
 */
public class IO {
	private static Scanner sc = null;
	
	public static String readLineFromConsole() {
		if(sc == null)
			sc = new Scanner(System.in);
		String s = sc.nextLine();
		return s;
	}
	
	public static void printToConsole(String s) {
		writeToFile(s);
		System.out.println(s);
	}
	
	public static void printToConsole(long s) {
		printToConsole(Long.toString(s));
	}
	
	public static void printToConsoleDebug(String s, int debugLevel) {
		if(DataManager.Instance().debugLevel >= debugLevel)
			printToConsole(s);
	}

	public static void printToConsole(boolean bool) {
		printToConsole(Boolean.toString(bool));
	}
	
	public static void writeToFile(String s) {
	    BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(Constants.LOGFILE_NAME, true));
			writer.write(s);
			writer.close();
		} catch (IOException e) {}
	}
	
	public static Vector<String> readFile(String path) {
		BufferedReader br = null;
		FileReader fr = null;
		Vector<String> lines = new Vector<String>();
		try {
			fr = new FileReader(path);
			br = new BufferedReader(fr);

			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {
				lines.add(sCurrentLine);
			}

		} catch (IOException e) {
			printToConsole(e.getMessage());		
		} finally {
			try {
				if (br != null)
					br.close();
				if (fr != null)
					fr.close();
			} catch (IOException e) {
				printToConsole(e.getMessage());		
			}
		}
		return lines;
	}
}
