package com.devdragons.entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.devdragons.main.Game;
import com.devdragons.world.World;

public class NPC extends Entity {
	
	public String[] frases = new String[2];
	public boolean showMessage = false;
	public boolean show = false;
	public int curIndexMsg = 0;
	public int fraseIndex = 0;
	public int time = 0;
	public int maxTime = 5;
	
	public NPC(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		// TODO Auto-generated constructor stub
		frases[0] = "Olá! Seja muito bem-vindo ao jogo :)";
		frases[1] = "Destrua os inimigos :)";
	}
	
	public void tick() {
		
		double distance = World.calculateDistance(Game.player.getX(), Game.player.getY(), getX(), getY());
		
		if (Math.abs(distance) < 20) {
			if (show == false) {
				show = true;
				showMessage = true;
			}
		} else {
			// showMessage = false;
		}
		
		this.time++;
		if (showMessage) {
			if (this.time >= this.maxTime) {
				this.time = 0;
				if (curIndexMsg < frases[fraseIndex].length() -1) {
					curIndexMsg++;
				}
				else {			
					if (fraseIndex < frases.length - 1) {
						fraseIndex++;
						curIndexMsg = 0;
					}
				}
			}
		}
	}
	
	public void render(Graphics g)
	{
		super.render(g);
		
		if (showMessage) {
			g.setColor(Color.white);
			g.fillRect(9, 9, Game.WIDTH-18, Game.HEIGHT-18);
			g.setColor(Color.blue);
			g.fillRect(10, 10, Game.WIDTH-20, Game.HEIGHT-20);
			
			g.setColor(Color.white);
			g.setFont(new Font("Arial", Font.BOLD, 9));
			g.drawString(frases[fraseIndex].substring(0, curIndexMsg), (int) x, (int) y);
			
			g.drawString("> Pressione enter para fechar", (int) x+10, (int) y+13);
		}
	}

}
