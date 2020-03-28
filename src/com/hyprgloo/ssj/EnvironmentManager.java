package com.hyprgloo.ssj;

import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuadc;

import java.util.ArrayList;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import com.osreboot.ridhvl.HvlCoord2D;
import com.osreboot.ridhvl.HvlMath;

public class EnvironmentManager {
	public static ArrayList<Chunk> chunks;
	public static ArrayList<ShipFriendly> friendlyShips;
	public static Chunk closestChunk;

	public static void init() {
		chunks = new ArrayList<>();
		friendlyShips = new ArrayList<>();
		Chunk startChunk = new Chunk(new HvlCoord2D(0, 0), true);
		chunks.add(startChunk);
		closestChunk = startChunk;
	}

	public static void determineToGen(float x, float y) {
		boolean taken = false;

		for (Chunk c : chunks)
			if (c.loc.x == x && c.loc.y == y)
				taken = true;

		if (!taken)
			chunks.add(new Chunk(new HvlCoord2D(x, y), false));	
		
	}

	public static void update(float delta) {
		for (Chunk c : chunks) {

			float distance = HvlMath.distance(c.loc, Game.player.physicsObject.location);
			if (distance < Chunk.CHUNK_SIZE/2 + Display.getWidth()) {
				if (distance < HvlMath.distance(closestChunk.loc, Game.player.physicsObject.location))
					closestChunk = c;
	
				if(Game.debugCam)
					hvlDrawQuadc(c.loc.x, c.loc.y, Chunk.CHUNK_SIZE, Chunk.CHUNK_SIZE, new Color(0f,1f,0f,0.2f));
				
				c.update(delta);
			}
		}
		
		EnvironmentManager.friendlyShips.removeIf(s -> s.physicsObject.isDead());
		for (ShipFriendly ship : friendlyShips) {
			if (HvlMath.distance(Game.player.physicsObject.location, ship.physicsObject.location) < 1200) {
				ship.update(delta, Game.player);
				ship.draw(delta);
				ship.physicsObject.canDealDamage = true;
				ship.physicsObject.canReceiveDamage = true;
			} else {
				ship.physicsObject.canDealDamage = false;
				ship.physicsObject.canReceiveDamage = false;
			}
		}

		if (Game.player.physicsObject.location.x > closestChunk.loc.x + Chunk.CHUNK_SIZE / 8) // right
			determineToGen(closestChunk.loc.x + Chunk.CHUNK_SIZE, closestChunk.loc.y);
		if (Game.player.physicsObject.location.x < closestChunk.loc.x - Chunk.CHUNK_SIZE / 8) // left
			determineToGen(closestChunk.loc.x - Chunk.CHUNK_SIZE, closestChunk.loc.y);
		if (Game.player.physicsObject.location.y > closestChunk.loc.y + Chunk.CHUNK_SIZE / 8) // down
			determineToGen(closestChunk.loc.x, closestChunk.loc.y + Chunk.CHUNK_SIZE);
		if (Game.player.physicsObject.location.y < closestChunk.loc.y - Chunk.CHUNK_SIZE / 8) // up
			determineToGen(closestChunk.loc.x, closestChunk.loc.y - Chunk.CHUNK_SIZE);

		if (Game.player.physicsObject.location.x > closestChunk.loc.x + Chunk.CHUNK_SIZE / 8
				&& Game.player.physicsObject.location.y > closestChunk.loc.y + Chunk.CHUNK_SIZE / 8) // right and down
			determineToGen(closestChunk.loc.x + Chunk.CHUNK_SIZE, closestChunk.loc.y + Chunk.CHUNK_SIZE);
		if (Game.player.physicsObject.location.x < closestChunk.loc.x - Chunk.CHUNK_SIZE / 8
				&& Game.player.physicsObject.location.y > closestChunk.loc.y + Chunk.CHUNK_SIZE / 8) // left and down
			determineToGen(closestChunk.loc.x - Chunk.CHUNK_SIZE, closestChunk.loc.y + Chunk.CHUNK_SIZE);
		if (Game.player.physicsObject.location.x > closestChunk.loc.x + Chunk.CHUNK_SIZE / 8
				&& Game.player.physicsObject.location.y < closestChunk.loc.y - Chunk.CHUNK_SIZE / 8) // right up
			determineToGen(closestChunk.loc.x + Chunk.CHUNK_SIZE, closestChunk.loc.y - Chunk.CHUNK_SIZE);
		if (Game.player.physicsObject.location.x < closestChunk.loc.x - Chunk.CHUNK_SIZE / 8
				&& Game.player.physicsObject.location.y < closestChunk.loc.y - Chunk.CHUNK_SIZE / 8) // left up
			determineToGen(closestChunk.loc.x - Chunk.CHUNK_SIZE, closestChunk.loc.y - Chunk.CHUNK_SIZE);

	}
}
