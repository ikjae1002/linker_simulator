/*
 * Programmer/Student: Ikjae Jung
 * Date Last Modified: Feb. 7 2017
 * Operating Systems Lab 1 Modules
 */

public class Texts{
	private String symbol;
	private int address;
		public Texts(String symbol, int address){
			this.symbol = symbol;
			this.address = address;
		}
		
		public String getSymb(){
			return symbol;
		}
		
		public int getAdd(){
			return address;
		}
}