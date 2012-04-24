package fr.Entrivax.AlienAttack;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Random;

import fr.Entrivax.AlienAttack.Framework.GameState;

public class Player implements MouseMotionListener, MouseListener{
	int positionX, positionY;
	int mouseY;
	int mousepositionY;
	int motionY;
	Framework framework;
	Rectangle collisionBox;
	boolean destroyed;
	int score;
	int timeToDespawn;
	int cooldownAfterShot;
	boolean isClicked;
	Random rand = new Random();
	
	public Player(Framework framework){
		this.framework = framework;
		positionY = 15;
		positionX = 30;
		score = 0;
		destroyed = false;
		timeToDespawn = 10;
		cooldownAfterShot = 0;
		//----Collision
		collisionBox = new Rectangle();
		collisionBox.x = positionX;
		collisionBox.y = positionY+4;
		collisionBox.height = framework.playerImage.getHeight();
		collisionBox.width = framework.playerImage.getWidth();
		//---------------
	}
	
	public void Update()
	{
		if(!destroyed)
		{
			positionY += motionY;
			if(positionY > framework.CANVAS_HEIGHT-16 && positionY < 0)
				positionY = framework.CANVAS_HEIGHT-16;
			collisionBox.y = positionY;
			collisionTest();
			if(isClicked)
				Fire();
		}
		else if(destroyed && timeToDespawn > 0)
			timeToDespawn--;
		else if(destroyed && timeToDespawn <= 0)
		{
			framework.gameState = GameState.GAMEOVER;
			motionY = 0;
		}
		if(cooldownAfterShot > 0)
			cooldownAfterShot--;
	}
	
	public void MovePlayer(String direction)
	{
		if(!destroyed && !framework.gamePaused)
		{
			if(direction.equals("UP"))
			{
				motionY -= 2;
			}else if(direction.equals("DOWN"))
			{
				motionY += 2;
			}else if (motionY > 0)
				motionY--;
			else if(motionY < 0)
				motionY++;
				
			if(motionY > 4)
				motionY = 4;
			if(motionY < -4)
				motionY = -4;
			if(positionY <= 0 && motionY < 0)
			{
				motionY = 0;
				positionY = 0;
			}
			if(positionY >= framework.CANVAS_HEIGHT-16 && motionY > 0)
			{
				motionY = 0;
				positionY = framework.CANVAS_HEIGHT-16;
			}
		}
	}
	
	public void Fire()
	{
		if(cooldownAfterShot > 0)
			return;
		else
			cooldownAfterShot = 20;
		if(!framework.gamePaused && framework.gameState == GameState.PLAYING)
			framework.bullets.add(new Bullet(positionY-1, framework));
		else
			return;
		int num = rand.nextInt(3) + 1;
		framework.playSound("fire"+num+".wav");
	}
	
	private void collisionTest() {
		for(int i = 0; i < framework.aliens.size(); i++)
			if(framework.aliens.get(i).collisionBox.intersects(collisionBox))
			{
				destroyed = true;
				framework.playSound("exp"+ (rand.nextInt(4)+1)+".wav");
				framework.aliens.remove(i);
			}
		
	}
	
	public void draw(Graphics g)
	{
		if(!destroyed)
			g.drawImage(framework.playerImage, positionX, positionY, null);
		else if(destroyed && timeToDespawn > 0)
			g.drawImage(framework.explosionimage, positionX, positionY, null);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		isClicked = true;
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		isClicked = false;
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		if(!framework.gamePaused && framework.gameState == GameState.PLAYING)
		{
			mousepositionY = arg0.getY()-5;
			positionY = mousepositionY;
		}
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		if(!framework.gamePaused && framework.gameState == GameState.PLAYING)
		{
			mousepositionY = arg0.getY()-5;
			positionY = mousepositionY;
		}
	}
	
	
	
	
	
}
