package com.hyprgloo.ssj.projectile;

import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuadc;

import org.newdawn.slick.Color;

import com.hyprgloo.ssj.Main;
import com.hyprgloo.ssj.PhysicsObject.Alliance;
import com.hyprgloo.ssj.Projectile;
import com.osreboot.ridhvl.HvlCoord2D;

public class ProjectileMerchantGunner extends Projectile{

	public static final float SIZE = 8f;
	public static final float DURATION_LIFE = 1f;
	
	public static final Color COLOR_SPARKS = new Color(0.5f, 0.5f, 1f);
	
	private float life;
	
	public ProjectileMerchantGunner(HvlCoord2D locationArg, HvlCoord2D speedArg, float rotationArg){
		super(locationArg, speedArg, rotationArg, SIZE / 2f, true, COLOR_SPARKS);
		physicsObject.alliance = Alliance.FRIENDLY;
		physicsObject.damage = 10f;
		life = DURATION_LIFE;
	}

	@Override
	public void update(float delta){
		physicsObject.update(delta);
		life -= delta;
		if(life == 0) physicsObject.hurt(100f);
	}

	@Override
	public void draw(float delta){
		hvlDrawQuadc(physicsObject.location.x, physicsObject.location.y, SIZE, SIZE, Main.getTexture(Main.INDEX_FRIENDLY_BULLET));
	}
	
	@Override
	public void drawEmissive(float delta){
		hvlDrawQuadc(physicsObject.location.x, physicsObject.location.y, SIZE, SIZE, Main.getTexture(Main.INDEX_FRIENDLY_BULLET_EMISSIVE));
	}

}
