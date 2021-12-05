 import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;




public class AirplaneSeating {
	
	static int[][] airPlane;
	static int passengerCount ;
	static List<Integer[][]> seatList = new ArrayList<Integer[][]>();
	static String[][] plane;
	
	
	
	public static void main(String[] args) {

		System.out.println("------->Airplane Seating Algorithm<---------");
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Enter number of blocks");
		int blocks=scanner.nextInt();
		airPlane= new int[blocks][2];
		for (int i = 0; i < blocks; i++) {
			System.out.println("Enter block "+ (i+1)+ " value");
            for (int j = 0; j < 2; j++) {
                    airPlane[i][j] = scanner.nextInt();
            }
		}
		System.out.println("Enter passsenger count:");
		passengerCount=scanner.nextInt();
		validateInput(airPlane, passengerCount);
		createSeats();
		
		int seatValue =0;
		//main logic
		for (int blockNumber=0; blockNumber<seatList.size();blockNumber++) {
			Integer[][] block =seatList.get(blockNumber);
			for(int row=0; row<block.length; row++){
				Integer[] rowArray=block[row];
				for(int column=0; column<rowArray.length; column++){
					if(isWindowSeat(blockNumber, row, column)){
						seatValue=1+totalAsile()+windowsFilled(blockNumber, row, column);
						
					}
					else if(isAsileSeat(blockNumber, row, column)&&!isWindowSeat(blockNumber, row, column)){
						seatValue=1+AsileFilled(blockNumber, row, column);
					}
					else{
						
						seatValue=1+totalAsile()+totalWindows()+middleFilled(blockNumber, row, column);
					}
					//checking passenger count
					if(seatValue<=passengerCount)
						block[row][column] = seatValue;
					else
						block[row][column] = null;
				}
			}
		}
		
		System.out.println("------------>Seat Alloted<-------------");
		printPlane();
	}
	
	
	private static void validateInput(int[][] airPlane, int passengerCount) {
		Boolean notValid=false;
		 if(airPlane.length==0) {
			 System.out.println("Airplane cannot be empty. Please enter valid seat values"); notValid=true;
		 }
		 if(airPlane.length<0 || passengerCount<0) {
			 System.out.println("Do not enter negative value"); notValid=true;
		 }
		 for(int row=0; row<airPlane.length; row++){
			 int column= airPlane[row].length;
			 if(column<0||column>2) {
				 System.out.println("Array is not in 2D format. Please check");
				 notValid=true;break;
			 }
		 }
		 
		 if(notValid)
			 System.exit(1);
		}
	

	public static void createSeats(){
		for(int i=0; i<airPlane.length; i++){
					Integer[][] block= new Integer[airPlane[i][1]][airPlane[i][0]];
					seatList.add(block);
				}
				
			}
	
	private static boolean isWindowSeat(int blockNumber, int row, int column) {
			
		if(blockNumber==0 && column ==0 )
			return true;
		else if(blockNumber==seatList.size()-1 ){
			Integer[][] lastBlock= seatList.get(seatList.size()-1);
			Integer[] lastRow = lastBlock[lastBlock.length-1];
			Integer lastColumn = lastRow.length;
			if( column == lastColumn-1 )
				return true;
		}
		return false;
	}
	private static int totalAsile() {
		int totalNoOfAsile=0;
			int numberofBlocks=seatList.size();
		for (int blockNumber=0; blockNumber<numberofBlocks;blockNumber++) {
			Integer[][] block =seatList.get(blockNumber);
			int numberOfRows = block.length;
			if(blockNumber==0 ||blockNumber==numberofBlocks-1)
				totalNoOfAsile+=numberOfRows;
			else	
				totalNoOfAsile+=(numberOfRows*2);
		}
		
		return totalNoOfAsile;
	}
	private static int windowsFilled(int blockNumber, int row, int column) {
		int WFCR=0;
		int WFPR= windowsFilledPrevRow(row);
		if(blockNumber!=0)
			 WFCR=windowsFilledCurrentRow(blockNumber,row);
		int value = WFPR+WFCR;
		return value;
	}
	private static int windowsFilledCurrentRow( int BlockNumber, int rowToFill) {
			int value=0;
			Integer[][] block =seatList.get(0);
			int rowsInFirstBlock = block.length;
			if(rowsInFirstBlock<=rowToFill)
				return value;
			else
				value+=1;
		return value;
	}
	private static int windowsFilledPrevRow(int row) {
		int prevRows = row;
		int value=0; 
		//checking 1st block
		int blockNumber=0;
			Integer[][] block =seatList.get(blockNumber);
			int numberOfRows = block.length;
			if(prevRows<numberOfRows)
				value+=prevRows;
			else
				value+=numberOfRows;
		//checking last block
		 blockNumber=seatList.size()-1;
		 	block =seatList.get(blockNumber);
			numberOfRows = block.length;
			if(prevRows<numberOfRows)
				value+=prevRows;
			else
				value+=numberOfRows;
			//System.out.println("WFPR:" +row+"  "+value);
			
		return value;
	}
	private static boolean isAsileSeat(int blockNumber, int row, int column) {
		int rowLength=seatList.get(blockNumber)[0].length;
		if(column==0)
			return true;
		else if(column==seatList.get(blockNumber)[0].length-1)
			return true;
		return false;
	}
	private static int AsileFilled(int blockNumber, int row, int column) {
		int AFPR= asileFilledPrevRow(row);
		int	AFCR=asileFilledCurrentRow(blockNumber,row,column);
		int value = AFPR+AFCR;
		return value;
	}
	private static int asileFilledCurrentRow(int blockNumber, int rowToFill, int column) {
		int value=0;
		for(int b=0;b<blockNumber;b++){
			
			Integer[][] block =seatList.get(b);
			int rowInBlock=block.length;
			 if(rowInBlock>rowToFill&&b==0)
				value+=1;
			 else if(rowInBlock>rowToFill)
				value+=2;
			}
		if(column!=0&&blockNumber!=0)
			value+=1;
		return value;
	}
	private static int asileFilledPrevRow(int rowToFill) {
		int value=0;
		for (int blockNumber=0; blockNumber<seatList.size();blockNumber++) {
			Integer[][] block =seatList.get(blockNumber);
			int rowInBlock=block.length;
			if(rowInBlock<rowToFill)
				value+=(rowInBlock*2);
			else
				value+=(rowToFill*2);
			
		}
		int windowsSeatsfilled = windowsFilledPrevRow(rowToFill);
		value= value-windowsSeatsfilled;
		return value;
	}
	private static int totalWindows() {
		int value=0; 
		//checking 1st block
		int blockNumber=0;
			Integer[][] block =seatList.get(blockNumber);
			int numberOfRows = block.length;
			value+=numberOfRows;
		//checking last block
		 blockNumber=seatList.size()-1;
		 	block =seatList.get(blockNumber);
			numberOfRows = block.length;
			value+=numberOfRows;
			
		return value;
	}
	private static int middleFilled(int blockNumber, int row, int column) {
		int MFPR= middleFilledPrevRow(row);
		int	MFCR=middleFilledCurrentRow(blockNumber,row,column);
		int value = MFPR+MFCR;
		return value;
	}
	private static int middleFilledCurrentRow(int blockNumber, int rowToFill, int column) {
		int value=0;
		for(int b=0;b<blockNumber;b++){
			
			Integer[][] block =seatList.get(b);
			int rowInBlock=block.length;
			int columnInBLock=block[0].length;
			if(rowInBlock>rowToFill)
				value+=(columnInBLock-2);
			}
		value+=column-1;
		return value;
	}
	private static int middleFilledPrevRow(int rowToFill) {
		int value=0;
		for (int blockNumber=0; blockNumber<seatList.size();blockNumber++) {
			Integer[][] block =seatList.get(blockNumber);
			int rowInBlock=block.length;
			int columnInBLock=block[0].length;
			if(rowInBlock<rowToFill)
				value+=(rowInBlock*(columnInBLock-2));
			else
				value+=(rowToFill*(columnInBLock-2));
			
		}
		return value;
	}
	
	private static void printPlane() {
		int airPlaneWidth=0;
		int airPlaneHeight=0;
		for(int block=0; block<airPlane.length; block++){
			int width= airPlane[block][0];
			int height= airPlane[block][1];
			airPlaneWidth+=width;
			if(airPlaneHeight<height)
				airPlaneHeight=height;
		}
		 plane= new String[airPlaneHeight+1][airPlaneWidth+airPlane.length];
		
		for(int row=0; row<plane.length; row++){
				for(int column=0; column<plane[0].length; column++){
				 plane[row][column]=" ";
				}
			}
		
		int deckseat=0;
		for(int block=0; block<airPlane.length; block++){
			Integer[][] blockVal=seatList.get(block);
			int width= airPlane[block][0];
			int height= airPlane[block][1];
			int seat=0;
			fillFirstRow(deckseat,width, block);
			for(int row=0; row<height; row++){
				seat=0;
				for( seat=0; seat<width; seat++){
					if(row==0)
						fillFirstRow(deckseat, width, block);
					if(String.valueOf(blockVal[row][seat])!="null")
						plane[row+1][deckseat+seat]=String.valueOf(blockVal[row][seat]);
					else
						plane[row+1][deckseat+seat]=" ";
				}
			}
			deckseat+=seat+1;
		}
		
		Arrays.stream(plane).forEach(intarr->{ 
			Arrays.stream(intarr).forEach(element-> {System.out.format("%3s  ",element);});
			System.out.println();
			});
		
		
	}
	private static void fillFirstRow(int deckseat, int width, int block) {
		for( int seat=0; seat<width; seat++){
			int currentseat=deckseat+seat;
			 if(seat==0&&block==0)
				 plane[0][currentseat] = "W";
			 else if(seat==width-1&&block==seatList.size()-1)
				 plane[0][currentseat] = "W";
			 else if(seat==width-1&&block!=seatList.size()-1)
				 plane[0][currentseat] = "A";
			 else if(seat==0&&block!=0)
				 plane[0][currentseat] = "A";
			 else 
				 plane[0][currentseat] = "M";
		
		}
	}
	
	
	
	
	
	
}
