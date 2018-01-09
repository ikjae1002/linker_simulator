/*
 * Programmer/Student: Ikjae Jung
 * Date Last Modified: Feb. 7 2017
 * Operating Systems Lab 1 Modules
 */


import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;


public class Linker {
	
	
	/*
	 * Arguments for file intakes
	 * arg[0] contains input file name
	 * arg[1] contains output file name
	 */
	public static void main(String[] args) throws FileNotFoundException {
		if (args.length < 2){
			System.err.println("Error: Missing arguments");
		}
		
		File fileName = new File(args[0]);
		
		if (!fileName.canRead()) {
			System.err.printf("Error: cannot read from file %s\n.",
					fileName.getAbsolutePath());
			System.exit(0);
		}
		
		Scanner dataInput = null;
		try {
			dataInput = new Scanner(fileName);
		}
		catch (FileNotFoundException e) {
			System.err.printf("Error: cannot open file %s for reading\n.",
					fileName.getAbsolutePath());
			System.exit(0);
		}
		
		ArrayList<String> inputList = new ArrayList<String>();
		while(dataInput.hasNext()){
			inputList.add(dataInput.next());
		}
		
		Modules mod = new Modules(inputList);
		System.out.println(mod.toString());
		
		PrintWriter output = null;
		File outputFile = new File(args[1]);

		try {
			output = new PrintWriter(outputFile);
		}
		catch (FileNotFoundException e) {
			System.err.printf("Error: cannot open file %s for writing.\n.",
					outputFile.getAbsolutePath());
		}
		output.write(mod.toString());
		output.close();
	}
}
