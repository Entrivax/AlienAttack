package fr.Entrivax.AlienAttack;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

public class Alien {
	int positionX, positionY;
	int speed = 2;
	Framework framework;
	Rectangle collisionBox;
	boolean destroyed;
	int timeToDespawn;
	Random rand = new Random();
	
	public Alien(Framework framework, int posY){
		this.framework = framework;
		destroyed = false;
		positionX = framework.CANVAS_WIDTH;
		positionY = posY;
		timeToDespawn = 10;
		collisionBox = new Rectangle();
		collisionBox.height = framework.alienImage.getHeight();
		collisionBox.width = framework.alienImage.getWidth();
		collisionBox.x = positionX;
		collisionBox.y = positionY+4;
	}
	
	public void Update(){
		if(!destroyed)
		{
			positionX -= speed;
			collisionBox.x = positionX;
			collisionTest();
		}
		if(destroyed)
			timeToDespawn--;
		
		if(timeToDespawn <= 0)
			framework.aliens.remove(this);
		
		if(positionX+16 < 0)
			framework.aliens.remove(this);
	}
	
	private void collisionTest() {
		for(int i = 0; i < framework.bullets.size(); i++)
			if(collisionBox.intersects(framework.bullets.get(i).collisionBox))
			{
				destroyed = true;
				framework.playSound("exp"+ (rand.nextInt(4)+1)+".wav");
				if(!framework.player.destroyed)
					framework.player.score += 100;
				framework.bullets.remove(i);
			}
		
	}

	public void draw(Graphics g)
	{
		if(!destroyed)
			g.drawImage(framework.alienImage, positionX, positionY, null);
		else
			g.drawImage(framework.explosionimage, positionX, positionY, null);
	}

}
