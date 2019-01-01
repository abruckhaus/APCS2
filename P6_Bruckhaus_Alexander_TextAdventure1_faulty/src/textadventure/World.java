package textadventure;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;

import command.Command;
import items.Clothes;
import items.Item;
import items.Scenery;
import items.UselessItem;
/*
 *  To do:
 *  Update "look" command so that it functions two ways
 *  (1) look by itself prints room description
 *  (2) look at [Item] is the same as examine
 */
public class World {

	/* Room String constants
	 * 
	 * Instead of hard-coding everywhere, room names are defined here.
	 * They're also public and static so they can be accessed
	 * anywhere using World.stringName
	 */
	public static final String BEDROOM = "YOUR BEDROOM";
	public static final String HALLWAY = "HALLWAY";
	public static final String BATHROOM = "BATHROOM";
	public static final String KITCHEN = "KITCHEN";
	public static final String HOLDING_ROOM = "HOLDING ROOM";
	public static final String GRANDMAS_ROOM = "GRANDMA'S ROOM";

	/** Line word wrap length for console output */
	public static final int LINE_WRAP_LENGTH = 70;

	/* private World attributes */	
	/** The main player of the game, which is you */
	private Player player;

	/** Keeps track of the Rooms in this World using <Key, Value>
	 *  pairs of <RoomName, RoomObject>
	 */
	private HashMap<String, Room> rooms;

	/** Reads user input */
	private static final Scanner IN = new Scanner(System.in);

	/** Keeps track of time using built-in Java object */
	private Calendar localTime;
	long startMilliseconds;

	/**
	 * Create the game and initialize its internal map.
	 */
	public World() {
		initializeNewGame();
	}

	public void initializeNewGame() {
		// Initialize attributes
		createRooms();
		player = new Player(rooms.get(BEDROOM));
		player.addItem(new Clothes(this, "clothes", 15, Item.TAKEABLE, "Your school clothes for the day."));

		// Start game timer 
		localTime = new GregorianCalendar(2018, Calendar.AUGUST, 14, 7, 15, 0); // August 14, 2018 7:15 A.M.
		startMilliseconds = System.currentTimeMillis();
	}
	
	/**
	 * Print out the opening message for the player.
	 */
	private void printWelcome() {
		print("You are lying in bed drifting in and out of consciousness as the early light of dawn "
			+ "creeps into your room from a nearby window.  It's 7:15 A.M. and your carpool leaves at 7:20 A.M. "
			+ "which means you should probably get up.\n\n");
		print("Welcome to Breakfast Run!\n");
		print("(c) 2018 By Mr. Ferrante\n");
		print("Type 'help' if you need help.\n\n");
		print(player.getCurrentRoom().longDescription());
	}

	/**
	 * Create all the rooms and link their exits together.
	 */
	private void createRooms() {

		// Create a new HashMap to store the rooms in
		rooms = new HashMap<String, Room>();

		// Create rooms and add them to our HashMap
		rooms.put(BEDROOM, new Room(BEDROOM, "Your bedroom is simple yet functional.\n\n"));
		rooms.put(HALLWAY, new Room(HALLWAY, "A short hallway leading to the kitched is covered in family photos.  There is a bathroom to the west.\n\n"));
		rooms.put(BATHROOM, new Room(BATHROOM, "You are inside a simple bathroom with light blue walls.\n\n", Room.LOCKED));
		rooms.put(KITCHEN, new Room(KITCHEN, "The kitchen is void of people.  You'd better eat and run.\n\n"));
		rooms.put(HOLDING_ROOM, new Room(HOLDING_ROOM, "This is a room meant to hold items the player can't see yet. It's not " +
													   "connected to any other rooms and cannot be accessed by the player.\n\n"));
		rooms.put(GRANDMAS_ROOM, new Room(GRANDMAS_ROOM, "This is Grandma's Room"));

		// Define room exits.  Order is north, east, south, west
		rooms.get(BEDROOM).setExits(rooms.get(HALLWAY), null, null, null);
		rooms.get(HALLWAY).setExits(rooms.get(KITCHEN), null, rooms.get(BEDROOM), rooms.get(BATHROOM));
		rooms.get(KITCHEN).setExits(null, null, rooms.get(HALLWAY), null);
		rooms.get(BATHROOM).setExits(null, rooms.get(HALLWAY), null, null);
		rooms.get(HOLDING_ROOM).setExits(null, null, null, null);
		rooms.get(GRANDMAS_ROOM).setExits(rooms.get(HALLWAY), rooms.get(BEDROOM), null, null);

		// Create game items and add them to rooms
		UselessItem cubby = new UselessItem(this, "cubby_hole", 15, Item.NOT_TAKEABLE, "This is where you store important small items.");
		UselessItem rock = new UselessItem(this, "rock", 10, Item.TAKEABLE, "It's just your pet rock with googly eyes and a silly smile.");
		UselessItem keys = new UselessItem(this, "keys", 2, Item.TAKEABLE, "It's a set of keys");
		UselessItem fork = new UselessItem(this, "fork", 2, Item.TAKEABLE, "It's a set of keys");
		UselessItem toaster = new UselessItem(this, "toaster", 2, Item.NOT_TAKEABLE, "It's a set of keys");
		rooms.get(BEDROOM).addItem(cubby);
		rooms.get(BEDROOM).addItem(rock);
		rooms.get(BEDROOM).addItem(keys);
		rooms.get(GRANDMAS_ROOM).addItem(fork);
		rooms.get(GRANDMAS_ROOM).addItem(toaster);
		rooms.get(HALLWAY).addItem(new UselessItem(this, "picture", 15, Item.TAKEABLE, "The picture features you, age five, crying in front of an animatronic Santa Clause."));
		rooms.get(KITCHEN).addItem(new UselessItem(this, "stove", 100, Item.NOT_TAKEABLE, "An unremarkable gas stove stares back at you."));
		
		UselessItem fridge = new UselessItem(this, "refrigerator", 100, Item.NOT_TAKEABLE, "A standard white refrigerator hums quietly.");
		Scenery frontDoor = new Scenery(this, "front_door", 200, "This door exits your house.");

		rooms.get(KITCHEN).addItem(fridge);
		rooms.get(KITCHEN).addItem(frontDoor);
		rooms.get(KITCHEN).addItem(new Scenery(this, "table", 150, "An empty kitchen table."));
		rooms.get(KITCHEN).addItem(new UselessItem(this, "toaster", 5, Item.TAKEABLE, "A black, smudgy toaster."));

		rooms.get(BATHROOM).addItem(new UselessItem(this, "toothbrush", 5, Item.TAKEABLE, "Your trusty toothbrush."));

		rooms.get(HOLDING_ROOM).addItem(new UselessItem(this, "toast", 5, Item.TAKEABLE, "A crunchy piece of toast."));
		rooms.get(GRANDMAS_ROOM).addItem(new UselessItem(this, "fork", 5, Item.TAKEABLE, "A nice fork."));
		rooms.get(GRANDMAS_ROOM).addItem(new UselessItem(this, "toaser", 5, Item.NOT_TAKEABLE, "A nice toaster"));
		
		
	}

	public Room getRoom(String key) {
		return rooms.get(key);
	}

	public Player getPlayer() {
		return player;
	}
	
	/**
	 * Main play routine. Loops until end of play.
	 */
	public void play() {
		printWelcome();
		updateTime();

		// Enter the main command loop. Here we repeatedly read commands and
		// execute them until the game is over.
		boolean finished = false;
		while (!finished) {
			System.out.print("> "); // print prompt
			String command = IN.nextLine();
			finished = processCommand(command);
			finished = finished ? true : updateTime();  // Remove this line if you don't need time in your game
		}
		print("\nThank you for playing!  Good bye!\n\n");
	}

	/**
	 * Updates and prints out the current game time. Every second spent between game
	 * commands becomes a second spent within the game.
	 */
	private boolean updateTime() {
		long endMilliseconds = System.currentTimeMillis();		
		localTime.add(Calendar.SECOND, (int)((endMilliseconds - startMilliseconds)/1000));
		int hour = localTime.get(Calendar.HOUR_OF_DAY);
		int minute = localTime.get(Calendar.MINUTE);
		int second = localTime.get(Calendar.SECOND);		
		startMilliseconds = endMilliseconds;
		System.out.printf("The time is %d:%02d:%02d A.M.\n\n", hour, minute, second);
		if (localTime.get(Calendar.MINUTE) >= 20) {
			print("Ack!  You missed your carpool!  You're so triggered!!\n\n");
			return true;
		}
		return false;
	}
	
	/**
	 * Given a command, process (that is: execute) the command. If this command ends
	 * the game, true is returned, otherwise false is returned.
	 */
	private boolean processCommand(String command) {
		if (new StringTokenizer(command).nextToken().equals("quit"))
			return true;
		else if (!Command.hasValidCommandWord(command)) {
			print("I don't know the command '" + new StringTokenizer(command).nextToken() + "'\n\n");
			return false;
		}
		else {
			Command.doCommand(command, this);
			return false;
		}
	}

	/* The section below is for implementations of user commands */

//	/**
//	 * Print out some help information including a list of the command words.
//	 */
//	private void printHelp() {
//		print("This is a text adventure game.  Your job is to explore your surroundings, " +
//			  "collect and use items, and discover how to win the game.\n\n");
//		print("Standard commands:\n");
//	}

//	/**
//	 * Print out some stronger hint information specific to this game.
//	 */
//	private void printHints() {
//		print("Special commands: (spoiler alert!)\n");
//	}

	/**
	 * Helper method to print any String in a line-wrapped format.
	 * Prints the input String line-wrapped to a column width of LINE_WRAP_LENGTH,
	 * which is a constant defined at the top of this class.
	 * 
	 * Pseudocode and strategy:
	 * There are so many special cases, I could not have written this without planning it out.
	 * I decided to leave the comments here so you can see the strategy.
	 *  - Mr. Ferrante
	 * 
	 * while(length of str >= lengthLimit)
	 * 		Find the first occurrence of \n.
	 * 		If it's < lengthLimit then add substring(0, occurrence + 1) to output and reduce str by same amount.
	 * 		Else if there's a space at lengthLimit then add substring(0, lengthLimit) to output and
	 *  		reduce str by same amount
	 * 		Else find last occurrence of space within substring(0, lengthLimit)
	 * 			If no space anywhere then
	 * 				If there's a space at least somewhere in str, then add substring(0, firstSpace) to
	 * 					output and reduce str by same amount
	 * 				Else (no space anywhere)
	 * 					add rest of str to output and reduce by same amount
	 * 			Else (space somewhere within substring)
	 * 				add str.substring(0, index of last space) to output
	 * 				reduce str by same amount
	 * If there's anything left in str, add it.
	 */
	public static void print(String str) {
		String output = "";
		
		while (str.length() >= LINE_WRAP_LENGTH) {
			int lineBreakIndex = str.indexOf("\n");
			if (lineBreakIndex < LINE_WRAP_LENGTH) {
				output += str.substring(0, lineBreakIndex + 1);
				str = str.substring(lineBreakIndex + 1);
			}
			else if (str.charAt(LINE_WRAP_LENGTH) == ' ') {
				output += str.substring(0, LINE_WRAP_LENGTH);
				str = str.substring(LINE_WRAP_LENGTH + 1);
				if (str.length() > 0)
					output += "\n";
			}
			else {
				int lastSpaceIndex = str.substring(0, LINE_WRAP_LENGTH).lastIndexOf(" ");
				if (lastSpaceIndex == -1) {
					int firstSpaceIndex = str.indexOf(" ");
					if (firstSpaceIndex != -1) {
						output += str.substring(0, firstSpaceIndex);
						str = str.substring(firstSpaceIndex + 1);
						if (str.length() > 0)
							output += "\n";
					}
					else {
						output += str;
						str = "";
					}
				}
				else {
					output += str.substring(0, lastSpaceIndex);
					str = str.substring(lastSpaceIndex + 1);
					if (str.length() > 0)
						output += "\n";
				}
			}
		}
		if (str.length() > 0) {
			output += str;
		}
		System.out.print(output);
	}
}

