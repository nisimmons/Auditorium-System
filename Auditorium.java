//Nathaniel Simmons, nis190000
import java.util.Scanner;
import java.io.*;
public class Auditorium {
	private int rows;
	private int seats;
	private Node<Seat> first;
	Auditorium(){}
	Auditorium(String filename) throws FileNotFoundException{
		String line;
		Scanner scr = new Scanner(new File(filename));
		line = scr.nextLine();				//take in the first row and set the first node/seat
		seats = line.length();
		rows++;
		Node<Seat> n1 = new Node<Seat>();
		Seat s1 = new Seat();
		s1.setType(line.charAt(0));
		s1.setRow(1);
		s1.setSeat('A');
		n1.setSeat(s1);
		first = n1;
		Node<Seat> last = first;
		for (int x = 1; x < line.length(); x++) { 		//iterate through seats on row 1, setting the seat and linking nodes back and forth
			Node<Seat> n = new Node<Seat>();
			Seat s = new Seat();
			s.setType(line.charAt(x));
			s.setRow(1);
			s.setSeat((char)(x+65));
			n.setSeat(s);
			n.setLeft(last);
			last.setRight(n);
			last = n;
		}
		Node<Seat> up = new Node<Seat>();
		up = first;
		while(scr.hasNext()) { 							//iterate through all the other rows, connect left right up and down
			rows++;
			line = scr.nextLine();
			n1 = new Node<Seat>();						//these lines of code set the first seat because it is an edge case
			s1 = new Seat();							
			s1.setRow(rows);
			s1.setSeat((char)(65));
			s1.setType((line.charAt(0)));
			n1.setSeat(s1);
			up.setDown(n1);
			n1.setUp(up);
			last = n1;
			up = up.getRight();
			for (int x = 1; x < line.length(); x++) { 	//iterate through seats, linking left and back, and up and back and setting seat info
				Node<Seat> n = new Node<Seat>();
				Seat s = new Seat();
				s.setType(line.charAt(x));
				s.setRow(rows);
				s.setSeat((char)(x+65));
				n.setSeat(s);
				n.setLeft(last);
				last.setRight(n);
				n.setUp(up);
				up.setDown(n);
				up = up.getRight();
				last = n;				
			} 
			for (int x = 1; x < seats; x++) {			//loop for num seats and move back to that start to set up and last
				last = last.getLeft();
			}
			up = last;			
		}
	}
	public void printAuditorium() {
		//this version of printAuditorium prints to console for the user to see
		System.out.print("  ");
		for (int x = 0; x < seats; x++) { 						//prints the letters at the top of the auditorium
			char temp = (char) (x+65);
			System.out.print(temp);
		}
		Node<Seat> n1 = new Node<Seat>();
		char type;
		for(int x = 0; x < rows; x++) {							//iterate through rows
			System.out.println();
			System.out.print((x+1) + " ");					//print the row number at the start
			n1 = first;
			for (int z = 0; z < x; z++) {						//move n1 to correct row
				n1 = n1.getDown();
			}
			for(int y = 0; y < seats; y++) {					//iterate through seats on that row, printing and moving n1
				type = n1.getSeat().getType();
				if (type == 'A' || type == 'C' || type == 'S')			
					System.out.print('#');
				else
					System.out.print('.');
				if (y < seats - 1)
					n1 = n1.getRight();
			}		
		}
		System.out.println();
	}
	public void reserveSeat(int row, int seat, char type) {
		//simply sets the row/seat node to the type given
		Node<Seat> n = new Node<Seat>();
		n = first;
		for (int r = 1; r < row; r++)
			n = n.getDown();
		for (int s = 1; s < seat; s++)
			n = n.getRight();
		n.getSeat().setType(type);
	}
	public boolean seatEmpty(int row, int seat) {
		//returns false if the seat is already reserved, otherwise it returns true
		Node<Seat> n = new Node<Seat>();
		n = first;
		for (int r = 0; r < row; r++)			//moves n1 to the correct seat
			n = n.getDown();
		for (int s = 0; s < seat; s++)
			n = n.getRight();
		if	(n.getSeat().getType() == '.')		//checks if n1 is empty
			return true;
		else
			return false;		
	}
	
	public void printAuditorium(String filename) throws FileNotFoundException {
		//this version of printAuditorium prints to a given file
		FileOutputStream out = new FileOutputStream(filename);
		PrintWriter output = new PrintWriter(out);
		Node<Seat> n1 = new Node<Seat>();					//n1 will point to the node of interest for printing
		for(int x = 0; x < rows; x++) {						//iterate through rows
			n1 = first;
			for (int z = 0; z < x; z++) {					//move n1 to start of current row
				n1 = n1.getDown();
			}
			for(int y = 0; y < seats; y++) {				//iterate through seats, printing and moving n1
				output.print(n1.getSeat().getType());
				if (y < seats - 1)							
					n1 = n1.getRight();
			}
			if (x < rows - 1)								//move to newline
				output.println();		
		}
		output.close();
	}
	public int getRows() {
		return rows;
	}
	public int getSeats() {
		return seats;
	}
	public Node<Seat> getFirst() {
		return first;
	}
	public void setFirst(Node<Seat> first) {
		this.first = first;
	}
	
}
