package fr.Entrivax.AlienAttack;

import java.awt.Graphics;
import java.awt.Rectangle;

public class Bullet {
	int x, y;
	Framework framework;
	Rectangle collisionBox;
	
	public Bullet(int height, Framework framework)
	{
		this.framework = framework;
		this.y = height;
		this.x = 32;
		collisionBox = new Rectangle();
		collisionBox.height = 11;
		collisionBox.width = framework.bulletImage.getWidth();
		collisionBox.x = x+10;
		collisionBox.y = y+4;
	}
	
	public void Update()
	{
		this.x += 6;
		collisionBox.x = x+10;
		if(this.x > framework.CANVAS_WIDTH)
			framework.bullets.remove(this);
	}
	
	public void draw(Graphics g)
	{
		g.drawImage(framework.bulletImage, x, y, null);
	}

}
