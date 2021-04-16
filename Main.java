//Nathaniel Simmons, nis190000, 11/20/2020
//This program allows users to login, add, view, and manipulate orders, display their receipt
//It also allows an admin to log in, print a report, and exit the system.
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
public class Main {

	public static void main(String[] args) throws FileNotFoundException {
		HashMap<String, User> map = new HashMap<String, User>();
		readUserData(map);									//create and fill the HashMap
		Auditorium [] audArray = new Auditorium [3];
		audArray[0] = new Auditorium("A1.txt");
		audArray[1] = new Auditorium("A2.txt");
		audArray[2] = new Auditorium("A3.txt");	//fill auditorium array with auditoriums from files
		Scanner scr = new Scanner(System.in);
		
		while(true) {							//main loop
			System.out.print("Enter username:"); //get username and password from user, allow three tries on password.
			String username = scr.next();
			User user = map.get(username);
			if (user == null)
				continue;
			String password = user.getPassword();
			boolean login = false;
			for (int x = 0; x < 3; x++) {
				System.out.print("Enter password:");
				if (password.compareTo(scr.next()) == 0) {
					login = true;
					break;
				}
				else
					System.out.println("Invalid password");
			}
			if (login == false)
				continue;
			
			if (user.getUsername().compareTo("admin") == 0) {	//send to admin menu
				if (adminMenu(audArray, user, scr))
					break;
			}
			else												//send to user menu
				userMenu(audArray, user, scr);
			continue; //loop	
		}
	}
	public static void userMenu(Auditorium [] audArray, User user, Scanner scr) {
		while(true) {
			int choice;
			while(true) {
				try {
					System.out.println("1. Reserve Seats");			//print the user menu and take in input
					System.out.println("2. View Orders");
					System.out.println("3. Update Order");
					System.out.println("4. Display Receipt");
					System.out.println("5. Log Out");
					String line = scr.next();
					choice = Integer.parseInt(line);
					if (line.length() > 1 || choice < 1 || choice > 5)
						throw new Exception();
					break;
				}
				catch (Exception e) {
					System.out.println("Invalid input");
				}
			}		
			
			switch (choice) {		//direct program to correct function based on input
				case 1:
					//Reserve Seats
					newOrder(audArray, user, scr);				
				break;
				case 2:
					//View Orders
					if (user.orders.size() == 0) {
						System.out.println("No orders");
						break;
					}
					user.viewOrders();
				break;
				case 3:
					//Update Order
					if (user.orders.size() == 0) {
						System.out.println("No orders");
						break;
					}
					updateOrder(audArray, user, scr);
				break;
				case 4:
					//Display Receipt
					user.printReciept();
				break;
				case 5:
					//Log Out
					return;
			}
		}
	}
	public static void updateOrder(Auditorium[] audArray, User user, Scanner scr){
		while(true) {
			int orderChoice;
			while(true) {
				try {
					System.out.println("Which order would you like to update?");	//print orders and take in user input
					int adult, child, senior;
					Seat s;
					Order o;
					for (int x=0; x < user.orders.size(); x++) {
						adult = 0;
						child = 0;
						senior = 0;
						o = user.orders.get(x);
						System.out.println("Order " + (x+1));
						System.out.print("Auditorium: " + o.getAuditorium()+", ");
						for (int y = 0; y < o.seats.size(); y++) {
							s = o.seats.get(y);
							System.out.print(s.getRow());
							System.out.print((char)(s.getSeat()));
							if (y < o.seats.size()-1)
								System.out.print(","); //TODO
							char type = s.getType();
							if (type == 'A')
								adult++;
							else if (type == 'C')
								child++;
							else
								senior++;
						}
						System.out.println();
						System.out.print(adult + " adult, ");
						System.out.print(child + " child, ");
						System.out.println(senior + " senior");
					}
					
					String line = scr.next();
					orderChoice = Integer.parseInt(line);
					if (orderChoice > user.orders.size() || orderChoice < 1)
						throw new Exception();
					break;
				}
				catch (Exception e) {
					System.out.println("Invalid input");
				}
			}
			int choice;
			while(true) {												//print order update options and take in input
				try {
					System.out.println("1. Add tickets to order");
					System.out.println("2. Delete tickets from order");
					System.out.println("3. Cancel Order");
					String line = scr.next();
					choice = Integer.parseInt(line);
					if (choice > 3 || choice < 1)
						throw new Exception();
					break;
				}
				catch (Exception e) {
					System.out.println("Invalid input");
				}
			}	
			Order o = user.orders.get(orderChoice-1);				//direct program to function based on input
			switch (choice) {
				case 1:
					//add tickets
					if (addToOrder(audArray, o, scr))
						return;
					else
						break;
				case 2:
					//delete tickets
					if (deleteFromOrder(audArray, o, scr)) {
						if (o.seats.size() == 0)
							user.cancelOrder(o);
						return;
					}
					else
						break;
				case 3:
					//cancel order
					for (int x = 0; x < o.seats.size(); x++)
						audArray[o.getAuditorium()-1].reserveSeat(o.seats.get(x).getRow(), o.seats.get(x).getSeat()-64, '.');
					user.cancelOrder(o); 
				return;
			}
		}
		
	}
	public static boolean addToOrder(Auditorium [] audArray, Order order, Scanner input) {
		
		Auditorium a = audArray[order.getAuditorium() - 1];	//set the current auditorium
		int row, seat, adult, child, senior;
		char seatLetter;
		String choice;
			a.printAuditorium();					//print the auditorium for the user to see
			while(true) {							//Take in the row, loop until valid
				try {
					System.out.print("Enter row");
					choice = input.next();
					if(choice.length() == 1 && (Integer.parseInt(choice) <= a.getRows() && Integer.parseInt(choice) > 0)) {
						row = Integer.parseInt(choice);
						break;
					}
					else {
						Exception e = new Exception();
						throw e;
					}			
										
				}
				catch(NumberFormatException e) {
					System.out.println("Row must be an integer");
					continue;
				}
				catch (Exception e) {
					System.out.println("Invalid row");
					continue;
				}
			}
			while(true) {							//Take in the seat, loop until valid
				try {
					System.out.print("Enter seat");
					choice = input.next();
					if(choice.length() == 1) {
						seatLetter = choice.charAt(0);
						if (Character.isLowerCase(seatLetter))
							seat = (int)(seatLetter) - 96;
						else
							seat = (int)(seatLetter) - 64;
						if (seat >=0 && seat <= a.getSeats())
							break;
					}
					Exception e = new Exception();
					throw e;				
				}
				catch (Exception e) {
					System.out.println("Invalid seat");
					continue;
				}
			}
			while(true) {							//Take in the adult tickets, loop until valid
				try {
					System.out.print("Enter adult tickets");
					choice = input.next();
					adult = Integer.parseInt(choice);
					if(adult < 0) {
						Exception e = new Exception();
						throw e;
					}
					break;
				}
				catch(NumberFormatException e) {
					System.out.println("Tickets must be an integer");
					continue;
				}
				catch (Exception e) {
					System.out.println("Invalid number");
					continue;
				}
			}
			while(true) {							//Take in the child tickets, loop until valid
				try {
					System.out.print("Enter child tickets");
					choice = input.next();
					child = Integer.parseInt(choice);
					if(child < 0) {
						Exception e = new Exception();
						throw e;
					}
					break;
				}
				catch(NumberFormatException e) {
					System.out.println("Tickets must be an integer");
					continue;
				}
				catch (Exception e) {
					System.out.println("Invalid number");
					continue;
				}
			}
			while(true) {							//Take in the senior tickets, loop until valid
				try {
					System.out.print("Enter senior tickets");
					choice = input.next();
					senior = Integer.parseInt(choice);
					if(senior < 0) {
						Exception e = new Exception();
						throw e;
					}		
					break;
				}
				catch(NumberFormatException e) {
					System.out.println("Tickets must be an integer");
					continue;
				}
				catch (Exception e) {
					System.out.println("Invalid number");
					continue;
				}
			}
			
			//check if seats are reserved. Reserve if available, else display best in auditorium
			if (adult+child+senior > a.getSeats()) {		//if there are more tickets than there are seats, then just say there aren't any available
				System.out.println("seats not available");
				return false;
			}
			boolean isAvailable = true;
			Node<Seat> n1 = new Node<Seat>();
			n1 = a.getFirst();
			for (int x = 1; x < row; x++) {						//move n1 down to correct row
				n1 = n1.getDown();
			}
			if (seat+adult+child+senior <= a.getSeats()+1)		//if the number of seats requested is possible from the seat selected, the move n1 to that seat
				for (int x = 1; x < seat; x++) 
					n1 = n1.getRight();
			else
				isAvailable = false;
			for (int x = 0; x < (adult+child+senior); x++) {	//iterate through number of seats, checking if n1 is reserved
				if (n1.getSeat().getType() != '.')
					isAvailable = false;
				if (x < (adult+child+senior) - 1)
					n1 = n1.getRight();
			}
			for (int x = 0; x < (adult+child+senior)-1; x++)	//move n1 back to the start
				n1 = n1.getLeft();
			if (isAvailable) {									//if available, reserve the seats
				if (adult != 0)
					order.addToOrder(new Seat(row, (char)(seat+64), 'A'), adult, child, senior);	//add seats to order  
				else if (child != 0)
					order.addToOrder(new Seat(row, (char)(seat+64), 'C'), adult, child, senior); 
				else
					order.addToOrder(new Seat(row, (char)(seat+64), 'S'), adult, child, senior); 
				for (int x = 0; x < (adult); x++) 
					a.reserveSeat(row, seat+x, 'A');
				for (int x = 0; x < (child); x++) 
					a.reserveSeat(row, seat+x+adult, 'C');
				for (int x = 0; x < (senior); x++) 
					a.reserveSeat(row, seat+x+adult+child, 'S');
				return true;
			}
			else
				return false;
				
	}
	public static boolean deleteFromOrder(Auditorium [] audArray, Order order, Scanner scr){
		Auditorium a = audArray[order.getAuditorium() - 1];	//set the current auditorium
		int row;
		char seat;
		String input;
		while(true) {		//start reservation process
			try {
				System.out.print("Enter row");
				input = scr.next();
				if(input.length() == 1 && (Integer.parseInt(input) <= a.getRows() && Integer.parseInt(input) > 0)) {
					row = Integer.parseInt(input);
					break;
				}
				else {
					Exception e = new Exception();
					throw e;
				}			
									
			}
			catch(NumberFormatException e) {
				System.out.println("Row must be an integer");
				continue;
			}
			catch (Exception e) {
				System.out.println("Invalid row");
				continue;
			}
		}
		while(true) {							//Take in the seat, loop until valid
			try {
				System.out.print("Enter seat");
				input = scr.next();
				if(input.length() == 1) {
					seat = Character.toUpperCase(input.charAt(0));
					if (seat >= 64 && seat <= a.getSeats() + 64) 
						break;
				}
				Exception e = new Exception();
				throw e;				
			}
			catch (Exception e) {
				System.out.println("Invalid seat");
				continue;
			}
		}
		boolean deleted = false;
		for (int x = 0; x < order.seats.size(); x++) {		//attempt to find seat and delete it
			if (order.seats.get(x).getSeat() == seat && order.seats.get(x).getRow() == row) { 
				a.reserveSeat(row, seat-64, '.');
				order.seats.remove(x);
				deleted = true;
				break;
			}
		}	
		if (!deleted) {		// if seat was not found, output msg and return false
			System.out.print("You have not reserved this seat");
			return false;
		}
		else
			return true;
	}
	public static boolean adminMenu(Auditorium [] audArray, User user, Scanner scr) throws FileNotFoundException {
		while(true) {		//returns true to exit program, false to go back to main menu
			int choice;
			while(true) {
				try {
					System.out.println("1. Print Report");		//print admin menu and take in input
					System.out.println("2. Logout");
					System.out.println("3. Exit");
					String line = scr.next();
					choice = Integer.parseInt(line);
					if (choice > 3 || choice < 1)
						throw new Exception();
					break;
				}
				catch (Exception e) {
					System.out.println("Invalid input");
				}
			}			
			switch (choice) {			
				case 1:
					//print report
					printReport(audArray);
				break;
				case 2:
					//log out
					return false;
				case 3:
					//exit
					audArray[0].printAuditorium("A1Final.txt");	//print each auditorium to hardcoded files
					audArray[1].printAuditorium("A2Final.txt");
					audArray[2].printAuditorium("A3Final.txt");
					return true;
			}
		}
	}
	public static void printReport(Auditorium[] audArray) {
		int child, adult, senior, open, childTotal = 0, adultTotal = 0, seniorTotal = 0, openTotal = 0;
		double sales, salesTotal = 0;
		char type;
		Auditorium aud;
		for (int a = 0; a < audArray.length; a++) {	//go through each auditorium adding up seat types and printing info, adding to totals
			adult = 0;
			child = 0;
			senior = 0;
			open = 0;
			aud = audArray[a];
			Node<Seat> n1 = new Node<Seat>();
			for(int x = 0; x < aud.getRows(); x++) {			//iterate through rows
				n1 = aud.getFirst();
				for (int z = 0; z < x; z++) {					//move n1 to start of correct row
					n1 = n1.getDown();
				}
				for(int y = 0; y < aud.getSeats(); y++) {		//iterate through seats on row, increasing counters
					type = n1.getSeat().getType();
					if (type == 'C')
						child++;
					else if (type == 'A')			
						adult++;
					else if (type == 'S')
						senior++;
					else
						open++;
					if (y < aud.getSeats() - 1)
						n1 = n1.getRight();
				}	
			}
			sales = (adult*10 + child*5 + senior*7.5);					//calculate totalSales and then print everything
			System.out.print("Auditorium " + (a+1));
			System.out.print("	" + open + "	" + (adult+child+senior));
			System.out.print("	" + adult + "	" + child + "	" + senior);
			System.out.print("	$");
			System.out.printf("%.2f", sales);
			System.out.println();
			
			salesTotal += sales;							//add current values to totals
			adultTotal += adult;
			childTotal += child;
			seniorTotal += senior;
			openTotal += open;
		}
		System.out.print("Total	");			//print totals for entire system
		System.out.print("	" + openTotal + "	" + (adultTotal+childTotal+seniorTotal));
		System.out.print("	" + adultTotal + "	" + childTotal + "	" + seniorTotal + "	$");
		System.out.printf("%.2f", salesTotal);
		System.out.println();
		
	}
	public static void newOrder(Auditorium [] audArray, User user, Scanner input) {
		Auditorium a;
		int aud;
		while(true) {
		try {
			System.out.println("1. Auditorium 1");		//print and take in user input
			System.out.println("2. Auditorium 2");
			System.out.println("3. Auditorium 3");
			String line = input.next();
			aud = Integer.parseInt(line);
			a = audArray[aud-1];
			break;			
		}
		catch (Exception e) {
			System.out.println("Invalid input");
		}
		}
		int row, seat, adult, child, senior;
		char seatLetter;
		String choice;
			a.printAuditorium();					//print the auditorium for the user to see
			while(true) {							//Take in the row, loop until valid
				try {
					System.out.print("Enter row");
					choice = input.next();
					if(choice.length() == 1 && (Integer.parseInt(choice) <= a.getRows() && Integer.parseInt(choice) > 0)) {
						row = Integer.parseInt(choice);
						break;
					}
					else {
						Exception e = new Exception();
						throw e;
					}			
										
				}
				catch(NumberFormatException e) {
					System.out.println("Row must be an integer");
					continue;
				}
				catch (Exception e) {
					System.out.println("Invalid row");
					continue;
				}
			}
			while(true) {							//Take in the seat, loop until valid
				try {
					System.out.print("Enter seat");
					choice = input.next();
					if(choice.length() == 1) {
						seatLetter = choice.charAt(0);
						if (Character.isLowerCase(seatLetter))
							seat = (int)(seatLetter) - 96;
						else
							seat = (int)(seatLetter) - 64;
						if (seat >=0 && seat <= a.getSeats())
							break;
					}
					Exception e = new Exception();
					throw e;				
				}
				catch (Exception e) {
					System.out.println("Invalid seat");
					continue;
				}
			}
			while(true) {							//Take in the adult tickets, loop until valid
				try {
					System.out.print("Enter adult tickets");
					choice = input.next();
					adult = Integer.parseInt(choice);
					if(adult < 0) {
						Exception e = new Exception();
						throw e;
					}
					break;
				}
				catch(NumberFormatException e) {
					System.out.println("Tickets must be an integer");
					continue;
				}
				catch (Exception e) {
					System.out.println("Invalid number");
					continue;
				}
			}
			while(true) {							//Take in the child tickets, loop until valid
				try {
					System.out.print("Enter child tickets");
					choice = input.next();
					child = Integer.parseInt(choice);
					if(child < 0) {
						Exception e = new Exception();
						throw e;
					}
					break;
				}
				catch(NumberFormatException e) {
					System.out.println("Tickets must be an integer");
					continue;
				}
				catch (Exception e) {
					System.out.println("Invalid number");
					continue;
				}
			}
			while(true) {							//Take in the senior tickets, loop until valid
				try {
					System.out.print("Enter senior tickets");
					choice = input.next();
					senior = Integer.parseInt(choice);
					if(senior < 0) {
						Exception e = new Exception();
						throw e;
					}		
					break;
				}
				catch(NumberFormatException e) {
					System.out.println("Tickets must be an integer");
					continue;
				}
				catch (Exception e) {
					System.out.println("Invalid number");
					continue;
				}
			}
			
			//check if seats are reserved. Reserve if available, else display best in auditorium
			if (adult+child+senior > a.getSeats()) {		//if there are more tickets than there are seats, then just say there aren't any available
				System.out.println("seats not available");
				return;
			}
			boolean isAvailable = true;
			Node<Seat> n1 = new Node<Seat>();
			n1 = a.getFirst();
			for (int x = 1; x < row; x++) {						//move n1 down to correct row
				n1 = n1.getDown();
			}
			if (seat+adult+child+senior <= a.getSeats()+1)		//if the number of seats requested is possible from the seat selected, the move n1 to that seat
				for (int x = 1; x < seat; x++) 
					n1 = n1.getRight();
			else
				isAvailable = false;
			for (int x = 0; x < (adult+child+senior); x++) {	//iterate through number of seats, checking if n1 is reserved
				if (n1.getSeat().getType() != '.')
					isAvailable = false;
				if (x < (adult+child+senior) - 1)
					n1 = n1.getRight();
			}
			for (int x = 0; x < (adult+child+senior)-1; x++)	//move n1 back to the start
				n1 = n1.getLeft();
			if (isAvailable) {									//if available, reserve the seats
				/*if (adult != 0)
					user.addOrder(new Order(aud, new Seat(row, (char)(seat+64), 'A'), adult, child, senior)); //add the order to the user 
				else if (child != 0)
					user.addOrder(new Order(aud, new Seat(row, (char)(seat+64), 'C'), adult, child, senior)); 
				else
					user.addOrder(new Order(aud, new Seat(row, (char)(seat+64), 'S'), adult, child, senior)); //TODO */
				user.addOrder(new Order(aud, new Seat(row, (char)(seat+64), 'A'), adult, child, senior));
				for (int x = 0; x < (adult); x++) 
					a.reserveSeat(row, seat+x, 'A');
				for (int x = 0; x < (child); x++) 
					a.reserveSeat(row, seat+x+adult, 'C');
				for (int x = 0; x < (senior); x++) 
					a.reserveSeat(row, seat+x+adult+child, 'S');
			}
			else {												//if unavailable, find best available
				n1 = a.getFirst();
				Seat bestSeat = new Seat();
				double distance = -1;
				seat = -1;
				for(int r = 0; r < a.getRows(); r++) {											//iterate through auditorium
					for(int group = 0; group < a.getSeats()-(adult+child+senior)+1; group++) {	//iterate through the row
						while (n1.getLeft() != null)											//reset n1 back to the start of the row
							n1 = n1.getLeft();
						for (int y = 0; y < group; y++)											//move n1 to the start of the selected seats
							n1 = n1.getRight();
						isAvailable = true;
						int z;
						for (z = 0; z < (adult+child+senior); z++) {							//iterate through the seats to check if they are available			
							if (n1.getSeat().getType() != '.') {								//if the seats are taken, set to unavailable and break
								isAvailable = false;
								break;
							}
							if (z < (adult+child+senior) - 1)									//move n1
								n1 = n1.getRight();
						}
						for (int y = 0; y < z-1; y++)											//move n1 to start of group		
							n1 = n1.getLeft();
						if (isAvailable && (getDistance(a, n1.getSeat(), adult+child+senior) < distance || distance == -1)) {	//if the distance is smaller than the current bestSeat, set bestSeat
							distance = getDistance(a, n1.getSeat(), adult+child+senior);
							bestSeat = n1.getSeat();		//this sets distance and bestSeat, so we can compare with other seats						
						}
						else if (isAvailable && getDistance(a, n1.getSeat(), adult+child+senior) == distance) {					// if the seat groups are equidistant
							if (Math.abs((a.getRows()+1)/2 - (r+1)) < Math.abs((a.getRows()+1)/2 - bestSeat.getRow())) {			//if current row is closer to center
								distance = getDistance(a, n1.getSeat(), adult+child+senior);
								bestSeat = n1.getSeat();
							}
							else if (r+1 < bestSeat.getRow()) {																	//if the rows are equidistant but the current one is lower
								distance = getDistance(a, n1.getSeat(), adult+child+senior);
								bestSeat = n1.getSeat();
							}
						}
					}
					while (n1.getLeft() != null)									//reset n1 back to the start of the row
						n1 = n1.getLeft();
					if (r < a.getRows()-1)											//next row
						n1 = n1.getDown();
				}
				if (bestSeat.getRow() != 0) {										//if seats are empty, print info to user
					seat = bestSeat.getSeat()-64;
					row = bestSeat.getRow();						
					System.out.print(bestSeat.getRow());
					System.out.print((char)(bestSeat.getSeat()) + " - ");
					System.out.print(bestSeat.getRow());
					System.out.print((char)(bestSeat.getSeat()+adult+child+senior-1));
					while(true) {													//loop input validation asking for y/n to reserve best seats
						try {
							choice = input.next();
							if(choice.length() != 1 || (choice.charAt(0) != 'Y' && choice.charAt(0) != 'y' && choice.charAt(0) != 'N' && choice.charAt(0) != 'n')) {
								Exception e = new Exception();
								throw e;
							}		
							break;
						}
						catch (Exception e) {
							System.out.println("Invalid entry");
							continue;
						}
						
					}
					if(choice.charAt(0) == 'Y' || choice.charAt(0) == 'y') {		//if user selects y, then reserve seats
						n1 = a.getFirst();
						
						for (int x = 1; x < (row); x++)
							n1 = n1.getDown();
						for (int x = 1; x < (seat); x++)
							n1 = n1.getRight();						
						user.addOrder(new Order(aud, n1.getSeat(), adult, child, senior)); //attribute order to user
						for (int x = 0; x < (adult); x++) 
							a.reserveSeat(row, seat+x, 'A');
						for (int x = 0; x < (child); x++) 
							a.reserveSeat(row, seat+x+adult, 'C');
						for (int x = 0; x < (senior); x++) 
							a.reserveSeat(row, seat+x+adult+child, 'S');
					}					
				}
				else
					System.out.println("seats not available");
			}						
	}
	public static double getDistance(Auditorium a, Seat s1, int tickets) {
		//equation for distance is
		//d = sqrt((rows/2 - row)^2 + (seats/2 - centerseat)^2)
		double x = ((a.getSeats()+1)/2.0) - (s1.getSeat()-65 + ((tickets+1)/2.0));	//horizontal distance (seat)
		double y = ((a.getRows()+1)/2.0) - s1.getRow();								//vertical distance (row)
		return Math.sqrt(Math.pow(x,2) + Math.pow(y,2));							//total distance
	}
	public static void readUserData(HashMap<String, User> map) throws FileNotFoundException {
		Scanner scr = new Scanner(new File("userdb.dat"));		//this function simply reads in user database into the hashMap given to it.
		String s, username, password;
		while (scr.hasNext()) {
			s = scr.nextLine();
			username = s.substring(0, s.indexOf(" "));
			password = s.substring(s.indexOf(" ")+1, s.length());
			User u = new User(username, password);
			map.put(username, u);			
		}
		scr.close();
	}
}
class User{
	//User class holds username, password, and an arraylist of orders
	
	private String username;
	private String password;
	public ArrayList<Order> orders;
	User() {}
	User(String username, String password){	
		this.username = username;
		this.password = password;
		orders = new ArrayList<Order>();
	}
	public void viewOrders(){		//print out all the user's orders
		Order o;
		int adult, child, senior;
		Seat s;
		for (int x=0; x < orders.size(); x++) {
			adult = 0;
			child = 0;
			senior = 0;
			o = orders.get(x);
			o.seats.sort(null);	//sort seats in row then seat order
			System.out.print("Auditorium " + o.getAuditorium()+", ");
			for (int y = 0; y < o.seats.size(); y++) {		//go through the order's seats adding up totals
				s = o.seats.get(y);
				System.out.print(s.getRow());
				System.out.print((char)(s.getSeat()));
				if (y < o.seats.size()-1)
					System.out.print(",");//TODO
				char type = s.getType();
				if (type == 'A')
					adult++;
				else if (type == 'C')
					child++;
				else
					senior++;
				
			}
			System.out.println();
			System.out.print(adult + " adult, ");
			System.out.print(child + " child, ");
			System.out.println(senior + " senior");
		}
	}
	public void printReciept() {		
		Order o;
		int adult, child, senior, adultTotal = 0, childTotal = 0, seniorTotal = 0; 
		double orderTotal, finalTotal = 0;
		Seat s;
		for (int x=0; x < orders.size(); x++) {		//iterate through user's orders to add up totals for printing
			adult = 0;
			child = 0;
			senior = 0;
			o = orders.get(x);
			System.out.print("Auditorium " + o.getAuditorium()+", ");
			for (int y = 0; y < o.seats.size(); y++) {		//iterate through each seat in the order to add up amounts for that order and for totals
				s = o.seats.get(y);
				System.out.print(s.getRow());
				System.out.print((char)(s.getSeat()));
				if (y < o.seats.size()-1)
					System.out.print(","); //TODO
				char type = s.getType();
				if (type == 'A') {
					adult++;
					adultTotal++;
				}
				else if (type == 'C') {
					child++;
					childTotal++;
				}
				else {
					senior++;
					seniorTotal++;
				}
			}
			System.out.println();		//print info for this order
			System.out.print(adult + " adult, ");
			System.out.print(child + " child, ");
			System.out.println(senior + " senior");
			orderTotal = (10.0*adult) + (5.0*child) + (7.5*senior);
			System.out.print("Order Total: $");
			System.out.printf("%.2f", orderTotal);
			System.out.println();
		}
		System.out.println();
		System.out.print("Customer Total: $");	//print totals for user
		finalTotal = (10.0*adultTotal) + (5.0*childTotal) + (7.5*seniorTotal);
		System.out.printf("%.2f", finalTotal);
		System.out.println();
	}
	public void addOrder(Order o) {
		orders.add(o);		
	}
	public void cancelOrder(Order o) {			
		orders.remove(o);
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
		
}
class Order{
	//Order class has an auditorium int indicating which auditorium it belongs to
	//and an arraylist of seats
	
	private int auditorium;
	public ArrayList<Seat> seats;
	Order(int auditorium, Seat start, int adult, int child, int senior){	//given the starting seat, and the number of tickets, it can create an arraylist with seats
		this.auditorium = auditorium;
		seats = new ArrayList<Seat>();
		int x;
		Seat s;
		for (x = 0; x < adult; x++) {
			s = new Seat(start.getRow(), (char)(start.getSeat()+x), 'A');
			seats.add(s);
		}
		for (; x < adult+child; x++) {
			s = new Seat(start.getRow(), (char)(start.getSeat()+x), 'C');
			seats.add(s);
		}
		for (; x < adult+child+senior; x++) { 
			s = new Seat(start.getRow(), (char)(start.getSeat()+x), 'S');
			seats.add(s);
		}
	}
	public void addToOrder(Seat start, int adult, int child, int senior){	//given seat and number of tickets add seats to the already existing seat arraylist
		int x;
		Seat s;
		for (x = 0; x < adult; x++) {
			s = new Seat(start.getRow(), (char)(start.getSeat()+x), 'A');
			seats.add(s);
		}
		for (; x < adult+child; x++) {
			s = new Seat(start.getRow(), (char)(start.getSeat()+x), 'C');
			seats.add(s);
		}
		for (; x < adult+child+senior; x++) { 
			s = new Seat(start.getRow(), (char)(start.getSeat()+x), 'S');
			seats.add(s);
		}
	}
	public int getAuditorium() {
		return auditorium;
	}
	public void setAuditorium(int auditorium) {
		this.auditorium = auditorium;
	}
	
}