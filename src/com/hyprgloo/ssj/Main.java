package com.hyprgloo.ssj;

import com.osreboot.ridhvl.display.collection.HvlDisplayModeDefault;
import com.osreboot.ridhvl.template.HvlTemplateInteg2D;

public class Main extends HvlTemplateInteg2D{

	public static void main(String args []) {
		new Main();
	}
	
	public static final int
	INDEX_FONT = 0,
	INDEX_SPLASH = 1;
	
	public Main() {
		super(144, 1280, 720, "TITLE by HYPRGLOO", new HvlDisplayModeDefault());
	}
	
	@Override
	public void initialize() {
		getTextureLoader().loadResource("Font");//0
		getTextureLoader().loadResource("HYPRGLOO");//1
		
		MenuManager.init();
	}

	@Override
	public void update(float delta) {
		MenuManager.update(delta);
	}

}
