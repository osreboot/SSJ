package com.hyprgloo.ssj;

import com.osreboot.ridhvl.display.collection.HvlDisplayModeDefault;
import com.osreboot.ridhvl.painter.HvlShader;
import com.osreboot.ridhvl.painter.painter2d.HvlFontPainter2D;
import com.osreboot.ridhvl.template.HvlTemplateInteg2D;

public class Main extends HvlTemplateInteg2D{

	public static void main(String args []) {
		new Main();
	}
	
	public static final int
	INDEX_FONT = 0,
	INDEX_SPLASH = 1,
	INDEX_PLAYER_SHIP = 2,
	INDEX_FRIENDLY_SHIP_0 = 3,
	INDEX_ASTEROID = 4,
	INDEX_PLASMA = 5,
	INDEX_PLAYER_SHIP_EMISSIVE = 6,
	INDEX_VIGNETTE = 7,
	INDEX_CONVOY_ENEMY = 8;
	
	public static HvlFontPainter2D font;
	
	public static HvlShader emissive;
	
	public Main() {
		super(144, 1280, 720, "TITLE by HYPRGLOO", new HvlDisplayModeDefault());
	}
	
	@Override
	public void initialize() {
		getTextureLoader().loadResource("Font");//0
		getTextureLoader().loadResource("HYPRGLOO");//1
		getTextureLoader().loadResource("HeroShip32");//2
		getTextureLoader().loadResource("CargoShip");//3
		getTextureLoader().loadResource("Asteroid");//4
		getTextureLoader().loadResource("Plasma");//5
		getTextureLoader().loadResource("HeroShip32Emissive");//6
		getTextureLoader().loadResource("Vignette");//7
		getTextureLoader().loadResource("ConvoyEnemy");//8
		
		ArtManager.init();
		
		font = new HvlFontPainter2D(getTexture(INDEX_FONT), HvlFontPainter2D.Preset.FP_AGOFFICIAL);
		font.setCharSpacing(16f);
		
		MenuManager.init();
	}

	@Override
	public void update(float delta) {
		MenuManager.update(delta);
	}

}
