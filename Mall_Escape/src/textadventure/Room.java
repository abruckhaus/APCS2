package textadventure;

/*
 * Class Room - a room in an adventure game.
 *
 * Author:  Michael Kolling
 * Version: 1.1
 * Date:    August 2000
 * 
 * This class is part of Zork. Zork is a simple, text based adventure game.
 *
 * "Room" represents one location in the scenery of the game.  It is 
 * connected to at most four other rooms via exits.  The exits are labelled
 * north, east, south, west.  For each direction, the room stores a reference
 * to the neighbouring room, or null if there is no exit in that direction.
 */

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import interfaces.Lockable;
import items.Container;
import items.Food;

public class Room extends Container implements Lockable {

	/** Stores a map of exits this room leads to */
	private HashMap<String, Room> exits;

	/** Whether this Room is locked.  If locked, players can't travel to or from this room. */
	private boolean isLocked;

	/**
	 * Create an unlocked room with the given title name and description.  Titles should
	 * be CAPITALIZED by convention and descriptions should be rich with imagery and
	 * detail - but not overboard.  Also, it's not a good idea to include TAKEABLE
	 * items in the description because if a plpayer takes that item and moves it
	 * to another room, your description will either be wrong or need to be updated.
	 * You can, however, safely include Scenery items and NON-TAKEABLE items.
	 */
	public Room(String name, String description, World world) {
		this(name, description, Lockable.UNLOCKED, world);
	}

	/**
	 * Same as previous constructor but you can choose lockable or unlockable.
	 */
	public Room(String name, String description, boolean isLocked, World world) {
		super(world, name, description);
		this.isLocked = isLocked;
		exits = new HashMap<String, Room>();
	}

	/**
	 * Define the exits of this room. Every direction either leads to another room
	 * or is null (no exit there).
	 */
	public void setExits(Room north, Room east, Room south, Room west) {
		if (north != null)
			exits.put("north", north);
		if (east != null)
			exits.put("east", east);
		if (south != null)
			exits.put("south", south);
		if (west != null)
			exits.put("west", west);
	}
		
	/**
	 * Returns whether this Room is locked or not
	 */
	@Override
	public boolean isLocked() {
		return isLocked;
	}
	
	/**
	 * Locks this Room
	 */
	@Override
	public void doLock() {
		isLocked = Lockable.LOCKED;
	}
	
	/**
	 * Unlocks this Room
	 */
	@Override
	public void doUnlock() {
		isLocked = Lockable.UNLOCKED;
	}

	/**
	 * Return a description of this room, of the form:
	 * Description...
	 * Exits: north west
	 */
	@Override
	public String getDescription() {
		String result = getName() + "\n" + super.getDescription();
		
		if (!getItemString().equals(""))
			result += "You see " + getItemString() + " here.\n\n";
		
		if (exitString().equals("Exits: "))
			result += exitString() + " n/a\n\n";
		else
			result += exitString() + "\n\n";

		return result;
	}
	
	/**
	 * Return a string describing the room's exits, for example "Exits: north west".
	 */
	private String exitString() {
		String returnString = "Exits: ";
		String tempString = "";
		Set<String> keys = exits.keySet();
		for (Iterator<String> iter = keys.iterator(); iter.hasNext();) {
			tempString = (String) iter.next();
			returnString += " " + tempString;
		}
		return returnString;
	}

	/**
	 * Return the room that is reached if we go from this room in direction
	 * "direction". If there is no room in that direction, return null.
	 */
	public Room nextRoom(String direction) {
		return (Room) exits.get(direction);
	}

	/**
	 * This method is called after a player goes to this room.
	 * Does nothing by default but when a player enters a particular room,
	 * you could override this method and do something special.
	 */
	public void doGo() {
		// Default: do nothing
		//World.print(">>> calling doGo() of Room");
	}
}