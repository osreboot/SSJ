package com.hyprgloo.ssj.projectile;

import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuadc;

import org.newdawn.slick.Color;

import com.hyprgloo.ssj.Main;
import com.hyprgloo.ssj.PhysicsObject.Alliance;
import com.hyprgloo.ssj.Projectile;
import com.osreboot.ridhvl.HvlCoord2D;

public class ProjectileEnemyGunner extends Projectile{

	public static final float SIZE = 8f;
	public static final float DURATION_LIFE = 3f;
	
	public static final Color COLOR_SPARKS = new Color(0.2f, 1f, 0.2f);
	
	private float life;
	
	public ProjectileEnemyGunner(HvlCoord2D locationArg, HvlCoord2D speedArg, float rotationArg){
		super(locationArg, speedArg, rotationArg, SIZE / 2f, false, COLOR_SPARKS);
		physicsObject.alliance = Alliance.ENEMY;
		physicsObject.damage = 100f;
		
		life = DURATION_LIFE;
	}

	@Override
	public void update(float delta){
		physicsObject.update(delta);
		life -= delta;
		if(life <= 0) physicsObject.hurt(100f);
	}

	@Override
	public void draw(float delta){
		hvlDrawQuadc(physicsObject.location.x, physicsObject.location.y, SIZE, SIZE, Main.getTexture(Main.INDEX_ENEMY_BULLET));
	}

	@Override
	public void drawEmissive(float delta){
		hvlDrawQuadc(physicsObject.location.x, physicsObject.location.y, SIZE, SIZE, Main.getTexture(Main.INDEX_ENEMY_BULLET_EMISSIVE));
	}

}
