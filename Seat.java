//Nathaniel Simmons, nis190000
public class Seat implements Comparable<Seat> {
	//this class simply holds the seat information, including seat letter, the type (if reserved), and the row.
	private int row;
	private char seat;
	private char type;
	Seat(){}
	Seat(int row, char seat, char type){
		this.setRow(row);	
		this.setSeat(seat);
		this.setType(type);
	}
	@Override
	public int compareTo(Seat s) {
		if (this.row > s.getRow())
			return 1;
		else if (this.row < s.getRow())
			return -1;
		else if (this.seat > s.getSeat())
			return 1;
		else
			return -1;
		
	}
	public void setType(char type){
		this.type = type;
	}
	public char getType() {
		return type;
	}
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public int getSeat() {
		return seat;
	}
	public void setSeat(char seat) {
		this.seat = seat;
	}
	
	
}
