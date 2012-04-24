package fr.Entrivax.AlienAttack;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class VitesseFX {
	int x, y;
	Framework framework;
	BufferedImage image; 
	boolean foreground;
	
	public VitesseFX(int height, boolean foreground, Framework framework)
	{
		this.foreground = foreground;
		this.framework = framework;
		this.y = height;
		this.x = framework.CANVAS_WIDTH;
		try {
        	URL rocketImgUrl = this.getClass().getResource("VitesseFX.png");
			image = ImageIO.read(rocketImgUrl);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void Update()
	{
		if(x < -16)
			framework.vitesseFX.remove(this);
		if(foreground)
			this.x -= 15*2;
		else
			this.x -= 9*2;
	}
	
	public void draw(Graphics g)
	{
		if(foreground)
			g.drawImage(image, x, y, 20, 10, null);
		else
			g.drawImage(image, x, y, 17, 6, null);
	}

}