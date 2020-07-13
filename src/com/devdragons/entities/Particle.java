package com.devdragons.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.devdragons.main.Game;
import com.devdragons.world.Camera;

public class Particle extends Entity {

	public int lifeTime = 15;
	public int curLife = 0;
	
	public int spd = 2;
	public double dx = 0, dy = 0;
	
	public Particle(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		dx = Game.rand.nextGaussian();
		dy = Game.rand.nextGaussian();
	}
	
	public void tick() {
		x += dx*spd;
		y += dy*spd;
		
		curLife++;
		if (lifeTime==curLife) {
			Game.entities.remove(this);
		}
	}
	
	public void render(Graphics g) {
		g.setColor(Color.RED);
		g.fillRect(this.getX() - Camera.x, this.getY() - Camera.y, width, height);
	}
}
