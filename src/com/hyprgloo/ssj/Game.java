package com.hyprgloo.ssj;

import java.util.ArrayList;

public class Game {
	
	public static Player player;
	
	public static ArrayList<ShipFriendly> idleShips;
	
	public static void reset(){
		player = new Player();
		idleShips = new ArrayList<>();
		
		// Spawn idle ships
		idleShips.add(new ShipFriendly(500f, 500f, 0f, 16f));
		idleShips.add(new ShipFriendly(300f, 500f, 0f, 16f));
		idleShips.add(new ShipFriendly(500f, 300f, 0f, 16f));
		idleShips.add(new ShipFriendly(400f, 400f, 0f, 16f));
	}
	
	public static void update(float delta){
		// Attach ships to the player if they collide
		ArrayList<ShipFriendly> connectedShips = new ArrayList<>();	
		for(ShipFriendly ship : idleShips){
			if(player.collidesWith(ship.physicsObject)){
				player.connectShip(ship);
				connectedShips.add(ship);
			}
		}
		for(ShipFriendly ship : connectedShips)
			idleShips.remove(ship);
		
		// Update and draw all idle ships
		for(ShipFriendly ship : idleShips){
			ship.update(delta, player);
			ship.draw(delta);
		}
		
		// Update and draw the player
		player.update(delta);
		player.draw(delta);
	}
}
