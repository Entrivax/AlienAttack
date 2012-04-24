package fr.Entrivax.AlienAttack;

import java.awt.Graphics2D;
import java.awt.Image;

public class FontRenderer{
	Framework framework;
	public FontRenderer(Framework framework){
		this.framework = framework;
	}
	
	public void drawString(Graphics2D g2d, String string, int x, int y, float sizemultiplier){
		String lowercase = string.toLowerCase();
		for(int i = 0; i < string.length(); i++){
			int position = Character.getNumericValue(lowercase.charAt(i))*6;
			Image character;
			if(string.charAt(i) == ' ')
				character = framework.font.getSubimage(5, 0, 1, 7);
			else if(string.charAt(i) == '?')
				character = framework.font.getSubimage(214, 0, 6, 7);
			else if(string.charAt(i) == '!')
				character = framework.font.getSubimage(221, 0, 6, 7);
			else if(string.charAt(i) == '.')
				character = framework.font.getSubimage(228, 0, 6, 7);
			else if(string.charAt(i) == ':')
				character = framework.font.getSubimage(234, 0, 6, 7);
			else
				character = framework.font.getSubimage(position, 0, 6, 7);
			g2d.drawImage(character, (int)(x + i*6*sizemultiplier), y, (int)(6*sizemultiplier), (int)(7*sizemultiplier), null);
		}
	}
	
	public int stringWidth(String string, float sizemultiplier){
		return (int)(string.length()*6*sizemultiplier);
	}
	
}
