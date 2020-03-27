package com.hyprgloo.ssj;

public class Game {
	
	public static Player player;
	
	public static void reset() {
		player = new Player();
	}
	
	public static void update(float delta) {
		player.update(delta);
		player.draw(delta);
	}
}
