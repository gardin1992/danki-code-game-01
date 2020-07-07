package com.devdragons.main;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Timer;

public class Score {
	
	public double points = 0;
	public int enemiesKill = 0;
	// pegar o tempo do level e usar para adicionar o score
	public Timer timer;
	
	public double score = 0;
	// na fazer ver o numeros de lifepacks e ammo disponiveis
	// ver quantos deles foram pegos
	// ver quantos foram usados
	// criar um calculo com isso também
	// sempre resetando no fim dos leveis
	// porém não altera o score
	public int qtdLifepacks = 0;
	public int qtdLifepacksUsed = 0;
	// lembrar q cada pack tem uma quantidade
	public int qtdAmmo = 0;
	public int qtdAmmoUsed = 0;
	
	public void tick() {
		
	}
	
	public int getScore() {
		
		score = (enemiesKill * 10);
		return (int) (score);
	}
	
	public void render(Graphics g) {
		g.setFont(Game.instance.fontText);
		g.setColor(Color.white);
		g.drawString("SCORE " + getScore(), 360, 50);
	}
}
