package com.devdragons.entities;

import java.awt.image.BufferedImage;

import com.devdragons.main.Game;
import com.devdragons.world.World;

public class EnemySlime extends Enemy {
	
	public EnemySlime(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, null);
		
		this.maxIndex = 3;
		this.rightSprites = new BufferedImage[4];
		this.leftSprites = new BufferedImage[4];
		
		this.rightDamage = SLIME_RIGHT_FB;
		this.leftDamage = SLIME_LEFT_FB;
		
		for(int i = 0; i < 4; i++) {
			this.rightSprites[i] = Game.spritesheet.getSprite(0 + (i*World.TILE_SIZE), 64, World.TILE_SIZE, World.TILE_SIZE);
			this.leftSprites[i] = Game.spritesheet.getSprite(0 + (i*World.TILE_SIZE), 80, World.TILE_SIZE, World.TILE_SIZE);
		}
	}
}
