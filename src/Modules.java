/*
 * Programmer/Student: Ikjae Jung
 * Date Last Modified: Feb. 7 2017
 * Operating Systems Lab 1 Modules
 */


import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class Modules {
	private ArrayList<Module> arrMods;
	/*Module Line*/
	private int numOfMods;
	
	
	/*Module Declaration*/
	private class Module{
		Map<String, ArrayList<Integer>> inst = new HashMap<String, ArrayList<Integer>>();
		int numOfDef;
		int numOfUsed;
		int numOfTexts;
		// ArrayList for def
		ArrayList<String> definitions = new ArrayList<String>();
		/*ArrayList for TextTable*/
		ArrayList<TextTable> table = new ArrayList<TextTable>();
		
		void addDefinition(String def){
			definitions.add(def);
		}
		ArrayList<String> getDefs(){
			return definitions;
		}
		int getDefNum(){
			return numOfDef;
		}
		void setDefNum(int num){
			numOfDef = num;
		}
		int getUsedNum(){
			return numOfUsed;
		}
		void setUsedNum(int num){
			numOfUsed = num;
		}
		void setInst(String def, ArrayList<Integer> arr){
			inst.put(def, arr);
		}
		int getTextNum(){
			return numOfTexts;
		}
		void setTextNum(int num){
			numOfTexts = num;
		}
		void addText(String type, int address){
			TextTable temp = new TextTable();
			temp.setType(type);
			temp.setAdd(address);
			table.add(temp);
		}
		ArrayList<TextTable> getTable(){
			return table;
		}
		public String toString(){
			String definition;
			String instruct;
			String symbols;
			definition = map.toString();
			instruct = inst.toString();
			symbols = table.toString();
			
			return "\nModlue:\n" + definition + "\n" + instruct + "\n" + symbols;
		}
	}
	/*Instruction Class*/
	private class TextTable{
		/*Variables*/
		String type;
		int address;
		boolean used = false;
		
		/*Getters and setters*/
		void changeUsed(){
			used = true;
		}
		boolean getUsed(){
			return used;
		}
		void setType(String type){
			this.type = type;
		}
		void setAdd(int address){
			this.address = address;
		}
		String getType(){
			return type;
		}
		int getAdd(){
			return address;
		}
		
		public String toString(){
			return type + ": " + address;
		}
		
		public int anotherString(){
			return address;
		}
	}
	/*First Instruction*/
	String def;
	int defNum;
	Map<String, Integer> map = new HashMap<String, Integer>();
	ArrayList<String> sorted = new ArrayList<String>();
	/*Second Instruction*/
	/*Third Instruction*/
	private int absoluteAdd = 0;
	private String text;
	private int address;
	private int addressLoc = 0;

	public int entriesIndex = 1;
	ArrayList<Map<String, String>> errors = new ArrayList<Map<String, String>>();
	Map<String, String> unused = new HashMap<String, String>();
	Map<String, String> multi = new HashMap<String, String>();
	Map<String, String> limit = new HashMap<String, String>();
	Map<String, String> exceeds = new HashMap<String, String>();
	Map<String, String> undefined = new HashMap<String, String>();
	Map<String, String> relLimit = new HashMap<String, String>();
	Map<String, String> multiUsed = new HashMap<String, String>();
	
	
	public Modules(ArrayList<String> entries) throws FileNotFoundException{
		errors.add(unused);
		errors.add(multi);
		errors.add(limit);
		errors.add(exceeds);
		errors.add(undefined);
		errors.add(relLimit);
		errors.add(multiUsed);
		numOfMods = Integer.parseInt(entries.get(0));
		arrMods = new ArrayList<Module>(numOfMods);
		for (int i = 0; i<numOfMods; i++){
			/*Set module*/
			Module tempMod = new Module();
			/*First Instruction*/
			tempMod.setDefNum(Integer.parseInt(entries.get(entriesIndex)));
			entriesIndex++;
			if (tempMod.getDefNum() > 0){
				for (int j = 0; j < tempMod.getDefNum(); j++){
					def = entries.get(entriesIndex);
					if(map.containsKey(def)){
						if(!errors.get(0).containsKey(def)){
							errors.get(0).put(def, "Error: This variable is multiply defined; first value used.");
							entriesIndex++;
							entriesIndex++;
						}else{
							entriesIndex++;
							entriesIndex++;
						}
					}else{
						sorted.add(def);
						entriesIndex++;
						defNum = Integer.parseInt(entries.get(entriesIndex));
						entriesIndex++;
						/*Error for multiple definition*/
						map.put(def, defNum + absoluteAdd);
						tempMod.addDefinition(def);
						errors.get(1).put(def, "Warning: " + def+ " was defined in module " + (i+1) +" but never used.\n");
					}
				}
			}
			tempMod.setUsedNum(Integer.parseInt(entries.get(entriesIndex)));
			entriesIndex++;
			if (tempMod.getUsedNum() != 0){
				for(int j = 0; j <tempMod.getUsedNum(); j++){
					def = entries.get(entriesIndex);
					entriesIndex++;
					ArrayList<Integer> instructions = new ArrayList<Integer>();
					while(Integer.parseInt(entries.get(entriesIndex)) != -1){
						instructions.add(Integer.parseInt(entries.get(entriesIndex)));
						entriesIndex++;
					}
					entriesIndex++;
					tempMod.setInst(def, instructions);
				}
			}
			tempMod.setTextNum(Integer.parseInt(entries.get(entriesIndex)));
			
			absoluteAdd += tempMod.getTextNum();
			entriesIndex++;
			if (tempMod.getTextNum() !=0){
				for(int j = 0; j < tempMod.getTextNum(); j++){
					text = entries.get(entriesIndex);
					entriesIndex++;
					address = Integer.parseInt(entries.get(entriesIndex));
					entriesIndex++;
					tempMod.addText(text, address);
				}
			}
			arrMods.add(tempMod);
		}
		ArrayList<String> allKeys = new ArrayList<String>();
		ArrayList<String> errorKey = new ArrayList<String>();
		allKeys.addAll(map.keySet());
		for (int i = 0; i < allKeys.size(); i++){
			if(map.get(allKeys.get(i)) >= absoluteAdd){
				errorKey.add(allKeys.get(i));
			}
		}
		for (int i = 0; i < numOfMods; i++){
			for (int j = 0; j < errorKey.size(); j++){
				for (int k = 0; k < arrMods.get(i).getDefs().size(); k ++){
					if (errorKey.contains(arrMods.get(i).getDefs().get(k))){
						if (map.containsKey(arrMods.get(i).getDefs().get(k))){
							map.remove(errorKey.get(j));
							map.put(errorKey.get(j), addressLoc);
							if(!errors.get(0).containsKey(errorKey.get(j))){
								errors.get(0).put(errorKey.get(j), "Error: Definitnion exceeds module size: first word in module used.");
							}
						}
					}
				}
			}
			addressLoc += arrMods.get(i).getTable().size();
		}
		addressLoc = 0;
		for (int i = 0; i < numOfMods; i++){
			ArrayList<String> instList = new ArrayList<String>();
			instList.addAll(arrMods.get(i).inst.keySet());
			for (int j = 0; j < instList.size(); j++){
				for (int k = 0; k < arrMods.get(i).inst.get(instList.get(j)).size(); k++){
					if(arrMods.get(i).inst.get(instList.get(j)).get(k) > arrMods.get(i).getTable().size()){
						errors.get(3).put(instList.get(j), "Error: Use of " + instList.get(j) + " in module " + (i+1) + " exceeds module size; use ignored");
						System.out.println(errors.get(3).get(instList.get(j)));
					}
				}
			}
			int count = 0;
			for (int j = 0; j < arrMods.get(i).getTable().size(); j++){
				if (arrMods.get(i).getTable().get(j).getType().equals("R")){
					int temp = arrMods.get(i).getTable().get(j).getAdd();
					int counter = 0;
					while (temp >= 1000){
						counter += 1000;
						temp -= 1000;
					}
					if (temp + addressLoc >= absoluteAdd){
						String tempo = (count+addressLoc) + "";
						errors.get(5).put(tempo,  "Error: Relative address exceeds module size; zero used.");
						arrMods.get(i).getTable().get(j).setAdd(counter);
					}else{
						arrMods.get(i).getTable().get(j).setAdd(temp + addressLoc);
					}
				} else if(arrMods.get(i).getTable().get(j).getType().equals("E")){
					int temp = arrMods.get(i).getTable().get(j).getAdd();
					String now = j + "";
					String visited = "";
					for (int a = 0; a < instList.size(); a++){
						if(arrMods.get(i).inst.get(instList.get(a)).contains(j) && !visited.equals(now)){
							int digit = temp/1000;
							digit = digit * 1000;
							int addition = 0;
							String tempo = (count+addressLoc) + "";
							if(!map.containsKey(instList.get(a))){
								errors.get(4).put(tempo, "Error: " + instList.get(a) + " is not defined; zero used");
							}else{
								addition = map.get(instList.get(a));
							}
							errors.get(1).remove(instList.get(a));
							arrMods.get(i).getTable().get(j).setAdd(digit+addition);
							visited = j + "";
						}	
						else if(arrMods.get(i).inst.get(instList.get(a)).contains(Integer.parseInt(visited))){
							System.out.println("There should be only one");
							String tempo = (count+addressLoc) +"";
							if(!errors.get(6).containsKey(tempo)){
								errors.get(6).put(tempo, "Error: Multiple variables used in instruction; all but first ignored.");
							}
						}
						arrMods.get(i).getTable().get(j).changeUsed();
					}						
				} else if(arrMods.get(i).getTable().get(j).getType().equals("A")){
					int temp = arrMods.get(i).getTable().get(j).getAdd();
					int counter = 0;
					while(temp >= 1000){
						counter += 1000;
						temp -= 1000;
					}
					if(temp >= 200){
						String tempo = (count+addressLoc) + ""; 
						errors.get(2).put(tempo, "Error: Absolute address exceeds machine size: zero used.");
						arrMods.get(i).getTable().get(j).setAdd(counter);
					}
				}
				count++;
			}
			addressLoc += arrMods.get(i).getTable().size();
		}
	}
	
	public String toString(){
		/*
		String output = "";
		for (int i = 0; i < numOfMods; i++){
			output += arrMods.get(i).toString();
		}
		return output;*/
		
		String output = "";
		output += "Symbol Table\n";
		for(int i = 0; i < sorted.size(); i++){
			if(!errors.isEmpty()){
				if(errors.get(0).containsKey(sorted.get(i))){
					output += sorted.get(i) + "=" + map.get(sorted.get(i)) + "\t" + errors.get(0).get(sorted.get(i))+ "\n";				
				} else{
					output += sorted.get(i) + "=" + map.get(sorted.get(i)) + "\n";
				}
			}else{
				output += sorted.get(i) + "=" + map.get(sorted.get(i)) + "\n";
			}
		}
		output += "\nMemory Map\n";
		ArrayList<Integer> outputAdd = new ArrayList<Integer>();
		for(int i = 0; i < numOfMods; i++){
			for(int j = 0; j < arrMods.get(i).getTable().size(); j++){
				outputAdd.add(arrMods.get(i).getTable().get(j).getAdd());
			}
		}
		for(int i = 0; i < absoluteAdd; i++){
			String casting = i + "";
			if(errors.get(2).containsKey(casting)){
				output += i + ":" + "\t" + outputAdd.get(i) + "\t" + errors.get(2).get(casting) + "\n";
			}else if (errors.get(4).containsKey(casting)){
				output += i + ":" + "\t" + outputAdd.get(i) + "\t" + errors.get(4).get(casting) + "\n";
			}else if (errors.get(5).containsKey(casting)){
				output += i + ":" + "\t" + outputAdd.get(i) + "\t" + errors.get(5).get(casting) + "\n";
			}else if (errors.get(6).containsKey(casting)){
				output += i + ":" + "\t" + outputAdd.get(i) + "\t" + errors.get(6).get(casting) + "\n";
			}
			else{
				output += i + ":" + "\t" + outputAdd.get(i) + "\n";
			}
		}
		output += "\n";
		ArrayList<String> instrList = new ArrayList<String>();
		instrList.addAll(errors.get(3).keySet());
		for(int i = 0; i < errors.get(3).size(); i++){
			output += errors.get(3).get(instrList.get(i));
		}
		instrList.clear();
		instrList.addAll(errors.get(1).keySet());
		instrList.sort(null);
		for(int i = 0; i < errors.get(1).size(); i++){
			output += errors.get(1).get(instrList.get(i));
		}
		return output;
	}
	
}
