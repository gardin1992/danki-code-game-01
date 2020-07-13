package com.devdragons.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.devdragons.main.Game;
import com.devdragons.world.Camera;
import com.devdragons.world.World;

public class SkillBullet extends Entity {
	
	private double dx;
	private double dy;
	private double spd = 4;
	
	private int life = 20, curLife = 0;
	
	public SkillBullet(int x, int y, int width, int height, BufferedImage sprite, double dx, double dy) {
		super(x, y, width, height, sprite);
		this.dx = dx;
		this.dy = dy; 
	}
	
	public void tick() {
		if (World.isFreeDynamic((int) (x+(dx*spd)), (int) (y+(dy*spd)), 3,3) ) {
			x += dx*spd;
			y += dy*spd;
		} else {
			World.generateParticles(100, this.getX(), this.getY());
			Game.bullets.remove(this);
			Game.entities.remove(this);
			return ;
		}
		
		curLife++;
		
		if (curLife == life) {
			Game.bullets.remove(this);
			Game.entities.remove(this);
			return;
		}
	}
	
	public void render(Graphics g) {
		g.setColor(Color.yellow);
		g.fillOval(this.getX() - Camera.x, this.getY() - Camera.y, width, height);
	}
}
