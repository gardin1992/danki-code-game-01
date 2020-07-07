package com.devdragons.entities;

import java.awt.image.BufferedImage;

public class WeaponGun extends Weapon {

	public WeaponGun(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		this.damage = 3;
		this.weaponType = WeaponTypes.GUN;
	}
}
