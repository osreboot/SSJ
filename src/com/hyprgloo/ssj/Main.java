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
	INDEX_FRIENDLY_SHIP_0_EMISSIVE = 9,
	INDEX_ENEMY_BULLET = 10,
	INDEX_ENEMY_BULLET_EMISSIVE = 11,
	INDEX_ENEMY_GUNNER_SHIP = 12,
	INDEX_FRIENDLY_BULLET = 13,
	INDEX_FRIENDLY_BULLET_EMISSIVE = 14,
	INDEX_MENU_BUTT = 15,
	INDEX_SPARK = 16,
	INDEX_SPARK_EMISSIVE = 17,
	INDEX_PROG_BAR = 18,
	INDEX_SCRAP_0 = 19,
	INDEX_ENEMY_CONVOY = 20,
	INDEX_ENEMY_CONVOY_EMISSIVE = 21,
	INDEX_ENEMY_GUNNER_SHIP_EMISSIVE = 22,
	INDEX_FRIENDLY_MISSILE = 23,
	INDEX_FRIENDLY_MISSILE_EMISSIVE = 24,
	INDEX_FRIENDLY_GUN_SHIP = 25,
	INDEX_FRIENDLY_GUN_SHIP_EMMISSIVE = 26,
	INDEX_FRIENDLY_GUN = 27,
	INDEX_FRIENDLY_MISSILE_SHIP = 28,
	INDEX_FRIENDLY_MISSILE_SHIP_EMMISSIVE = 29,
	INDEX_FRIENDLY_MISSILE_LAUNCH = 30;

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
		getTextureLoader().loadResource("CargoShip32");//3
		getTextureLoader().loadResource("Asteroid");//4
		getTextureLoader().loadResource("Plasma");//5
		getTextureLoader().loadResource("HeroShip32Emissive");//6
		getTextureLoader().loadResource("Vignette");//7
		getTextureLoader().loadResource("ConvoyEnemy");//8 // TODO remove this
		getTextureLoader().loadResource("CargoShip32Emissive");//9
		getTextureLoader().loadResource("EnemyBullet");//10
		getTextureLoader().loadResource("EnemyBulletEmissive");//11
		getTextureLoader().loadResource("EnemyGunner");//12
		getTextureLoader().loadResource("FriendlyBullet");//13
		getTextureLoader().loadResource("FriendlyBulletEmissive");//14
		getTextureLoader().loadResource("menuButton");//15
		getTextureLoader().loadResource("Spark");//16
		getTextureLoader().loadResource("SparkEmissive");//17
		getTextureLoader().loadResource("progBar");//18
		getTextureLoader().loadResource("Scrap0");//19
		getTextureLoader().loadResource("EnemyConvoy");//20
		getTextureLoader().loadResource("EnemyConvoyEmissive");//21
		getTextureLoader().loadResource("EnemyGunnerEmissive");//22
		getTextureLoader().loadResource("Missile");//23
		getTextureLoader().loadResource("MissileEmissive");//24
		getTextureLoader().loadResource("GunnerShip");//25
		getTextureLoader().loadResource("GunnerShipEmissive");//26
		getTextureLoader().loadResource("GunnerShipWeapon");//27
		getTextureLoader().loadResource("MissileShip");//28
		getTextureLoader().loadResource("MissileShipEmissive");//29
		getTextureLoader().loadResource("MissileLauncher");//30

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
