package com.hyprgloo.ssj.merchant;

import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuadc;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlResetRotation;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlRotate;

import com.hyprgloo.ssj.Main;
import com.hyprgloo.ssj.PhysicsObject.Alliance;
import com.hyprgloo.ssj.ShipFriendly;

public class ShipFriendlyTrader extends ShipFriendly{

	public static final float SIZE = 16f;

	public ShipFriendlyTrader(float xArg, float yArg, float angleArg){
		super(xArg, yArg, angleArg, SIZE);
		physicsObject.alliance = Alliance.FRIENDLY;
		physicsObject.damage = 100f;
	}


	@Override
	public void draw(float delta){
		hvlRotate(physicsObject.location.x, physicsObject.location.y, physicsObject.getVisualAngle());
		hvlDrawQuadc(physicsObject.location.x, physicsObject.location.y, physicsObject.radius * 2f, physicsObject.radius * 2f, Main.getTexture(Main.INDEX_FRIENDLY_SHIP_0));
		hvlResetRotation();
	}

	@Override
	public void updateConnected(float delta) {
	
	}
}
