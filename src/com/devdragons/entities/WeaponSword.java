package com.devdragons.entities;

import java.awt.image.BufferedImage;


public class WeaponSword extends Weapon {
	
	public WeaponSword(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		this.damage = 1; 
		this.weaponType = WeaponTypes.SWORD;
	}
}
