package fr.Entrivax.AlienAttack;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
   
public class Framework extends JFrame {
	private static final long serialVersionUID = 1L;
   int CANVAS_WIDTH = 800;
   int CANVAS_HEIGHT = 600;
   static final int UPDATE_RATE = 120;
   static final long UPDATE_PERIOD = 1000000000L / UPDATE_RATE;
   Player player;
   BufferedImage pauseImage;
   BufferedImage GOImage;
   BufferedImage B4Image;
   BufferedImage B1Image;
   BufferedImage B2Image;
   BufferedImage logoImage;
   BufferedImage alienImage;
   BufferedImage explosionimage;
   BufferedImage playerImage;
   BufferedImage bulletImage;
   BufferedImage font;
   ArrayList<Bullet> bullets = new ArrayList<Bullet>();
   ArrayList<Alien> aliens = new ArrayList<Alien>();
   ArrayList<VitesseFX> vitesseFX = new ArrayList<VitesseFX>();
   public static enum GameState{MAIN_MENU, OPTIONS, PLAYING, GAMEOVER, CREDITS}
   public GameState gameState;
   boolean gamePaused = false;
   Random rand = new Random();
   public int currentFPS = 0;
   public int FPS = 0;
   public long start = 0;
   private boolean upPressed;
   private boolean downPressed;
   public FontRenderer fontrenderer;
   
   private GameCanvas canvas;
   
   public Framework() {
	  setUndecorated(true);
      gameInit();
   
      canvas = new GameCanvas();
      canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
      this.setContentPane(canvas);
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      this.pack();
      this.setVisible(true);
      this.setLocation((int) (this.getToolkit().getScreenSize().getWidth()-CANVAS_WIDTH)/2, (int) (this.getToolkit().getScreenSize().getHeight()-CANVAS_HEIGHT)/2);
      gameStart();
   }
   public void gameInit() { 
	   gameState = GameState.MAIN_MENU;
	   fontrenderer = new FontRenderer(this);
       try {
			pauseImage = ImageIO.read(this.getClass().getResource("Pause.png"));
			font = ImageIO.read(this.getClass().getResource("font.png"));
			GOImage = ImageIO.read(this.getClass().getResource("GameOverScreen.png"));
			B4Image = ImageIO.read(this.getClass().getResource("Button4.png"));
			B1Image = ImageIO.read(this.getClass().getResource("Button1.png"));
			B2Image = ImageIO.read(this.getClass().getResource("Button2.png"));
			logoImage = ImageIO.read(this.getClass().getResource("Logo.png"));
			alienImage = ImageIO.read(this.getClass().getResource("Alien.png"));
			explosionimage = ImageIO.read(this.getClass().getResource("Explosion.png"));
			playerImage = ImageIO.read(this.getClass().getResource("Player.png"));
			bulletImage = ImageIO.read(this.getClass().getResource("Bullet.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	   player = new Player(this);
   }
   
   public synchronized void playSound(final String url) { // MUST IN .WAV !!!!
	    new Thread(new Runnable() {
	      public void run() {
	        try {
	          Clip clip = AudioSystem.getClip();;
	          AudioInputStream inputStream = AudioSystem.getAudioInputStream(this.getClass().getResource(url));
	          clip.open(inputStream);
	          clip.start();
	        } catch (Exception e) {
	          System.err.println();
	        }
	      }
	    }).start();
	  }

   
   public void gameStart() {
	   Thread gameThread =  new Thread() {
	         @Override
	         public void run() {
	            gameLoop();
	         }
	      };
	      gameThread.start();
   }
   
   public void gameLoop()
   {
	   long beginTime, timeTaken, timeLeft; 
	   while (true) {
		  beginTime = System.nanoTime();
	      if (!gamePaused) {
	         gameUpdate();
	      }
	      repaint();
	      //**FPS counter**
	      currentFPS++;
	      if(System.currentTimeMillis() - start >= 1000) {
	    	  FPS = currentFPS;
	    	  currentFPS = 0;
	    	  start = System.currentTimeMillis();
	      }
	      //***************
	      timeTaken = System.nanoTime() - beginTime;
	      timeLeft = (UPDATE_PERIOD - timeTaken) / 1000000;
	      if (timeLeft < 10) timeLeft = 10;
	      try {
	         Thread.sleep(timeLeft);
	      } catch (InterruptedException ex) { }
	   }
   }
   
   public void gameShutdown() {  }
   
   public void gameUpdate() { 
	   CANVAS_WIDTH = getWidth();
	   CANVAS_HEIGHT = getHeight();
	   switch(gameState)
	   {
	   case MAIN_MENU:
		 //--------------------------------FX
		   if(rand.nextInt(20) == 1)
			   vitesseFX.add(new VitesseFX(rand.nextInt(CANVAS_HEIGHT-16), false, this));

		   if(rand.nextInt(30) == 1)
			   vitesseFX.add(new VitesseFX(rand.nextInt(CANVAS_HEIGHT-16), true, this));
		   
		   for(int i=0; i<vitesseFX.size(); i++)
			   vitesseFX.get(i).Update();
		   
		   //--------------------------------Entities
		   //----Bullets
		   for(int i=0; i<bullets.size(); i++)
			   bullets.get(i).Update();
		   //----Enemies
		   if(rand.nextInt(20) == 1)
			   aliens.add(new Alien(this, rand.nextInt(CANVAS_HEIGHT-16)));
		   for(int i=0; i<aliens.size(); i++)
			   aliens.get(i).Update();
		   break;
		   
	   case OPTIONS:
		 //--------------------------------FX
		   if(rand.nextInt(20) == 1)
			   vitesseFX.add(new VitesseFX(rand.nextInt(CANVAS_HEIGHT-15), false, this));

		   if(rand.nextInt(30) == 1)
			   vitesseFX.add(new VitesseFX(rand.nextInt(CANVAS_HEIGHT-15), true, this));
		   
		   for(int i=0; i<vitesseFX.size(); i++)
			   vitesseFX.get(i).Update();
		   
		   //--------------------------------Entities
		   //----Bullets
		   for(int i=0; i<bullets.size(); i++)
			   bullets.get(i).Update();
		   //----Enemies
		   if(rand.nextInt(20) == 1)
			   aliens.add(new Alien(this, rand.nextInt(CANVAS_HEIGHT-16)));
		   for(int i=0; i<aliens.size(); i++)
			   aliens.get(i).Update();
		   break;
		   
	   case CREDITS:
			 //--------------------------------FX
			   if(rand.nextInt(20) == 1)
				   vitesseFX.add(new VitesseFX(rand.nextInt(CANVAS_HEIGHT-15), false, this));

			   if(rand.nextInt(30) == 1)
				   vitesseFX.add(new VitesseFX(rand.nextInt(CANVAS_HEIGHT-15), true, this));
			   
			   for(int i=0; i<vitesseFX.size(); i++)
				   vitesseFX.get(i).Update();
			   
			   //--------------------------------Entities
			   //----Bullets
			   for(int i=0; i<bullets.size(); i++)
				   bullets.get(i).Update();
			   //----Enemies
			   if(rand.nextInt(20) == 1)
				   aliens.add(new Alien(this, rand.nextInt(CANVAS_HEIGHT-16)));
			   for(int i=0; i<aliens.size(); i++)
				   aliens.get(i).Update();
			   break;
		   
	   case PLAYING:
		   //--------------------------------FX
		   if(rand.nextInt(20) == 1)
			   vitesseFX.add(new VitesseFX(rand.nextInt(CANVAS_HEIGHT-15), false, this));

		   if(rand.nextInt(30) == 1)
			   vitesseFX.add(new VitesseFX(rand.nextInt(CANVAS_HEIGHT-15), true, this));
		   
		   for(int i=0; i<vitesseFX.size(); i++)
			   vitesseFX.get(i).Update();
		   
		   //--------------------------------Entities
		   //----Bullets
		   for(int i=0; i<bullets.size(); i++)
			   bullets.get(i).Update();
		   //----Player
		   if(downPressed)
			   player.MovePlayer("DOWN");
		   else if(upPressed)
			   player.MovePlayer("UP");
		   else
			   player.MovePlayer("");
		   player.Update();
		   //----Enemies
		   int chance = rand.nextInt(20);
		   if(chance == 1)
			   aliens.add(new Alien(this, rand.nextInt(CANVAS_HEIGHT-16)));
		   for(int i=0; i<aliens.size(); i++)
			   aliens.get(i).Update();
		   break;
		   
	   case GAMEOVER:
		   if(rand.nextInt(20) == 1)
			   vitesseFX.add(new VitesseFX(rand.nextInt(CANVAS_HEIGHT-15), false, this));

		   if(rand.nextInt(30) == 1)
			   vitesseFX.add(new VitesseFX(rand.nextInt(CANVAS_HEIGHT-15), true, this));
		   
		   for(int i=0; i<vitesseFX.size(); i++)
			   vitesseFX.get(i).Update();
		   
		   for(int i=0; i<bullets.size(); i++)
			   bullets.get(i).Update();
		   chance = rand.nextInt(20);
		   if(chance == 1)
			   aliens.add(new Alien(this, rand.nextInt(CANVAS_HEIGHT-16)));
		   for(int i=0; i<aliens.size(); i++)
			   aliens.get(i).Update();
		   break;
	   }
   }
   
   // Refresh the display after each step.
   // Use (Graphics g) as argument if you are not using Java 2D.
   public void gameDraw(Graphics2D g2d) { 
	   switch(gameState)
	   {
	   case MAIN_MENU:
		   for(int i=0; i<vitesseFX.size(); i++)
			   if(!vitesseFX.get(i).foreground)
				   vitesseFX.get(i).draw(g2d);
		   
		   for(int i=0; i<bullets.size(); i++)
			   bullets.get(i).draw(g2d);
		   for(int i=0; i<aliens.size(); i++)
			   aliens.get(i).draw(g2d);

		   for(int i=0; i<vitesseFX.size(); i++)
			   if(vitesseFX.get(i).foreground)
				   vitesseFX.get(i).draw(g2d);
		   
		   fontrenderer.drawString(g2d, "Alpha 1.02", 3, CANVAS_HEIGHT - 13, 1.5f);
		   fontrenderer.drawString(g2d, "by Entrivax", CANVAS_WIDTH-fontrenderer.stringWidth("by entrivax", 1.5f)-3, CANVAS_HEIGHT - 13, 1.5f);
		   g2d.drawImage(B1Image, 250, 250, null);
		   fontrenderer.drawString(g2d, "Start game", CANVAS_WIDTH/2-fontrenderer.stringWidth("Start game", 2)/2, 260, 2);
		   g2d.drawImage(B2Image, 250, 285, null);
		   fontrenderer.drawString(g2d, "Option", CANVAS_WIDTH/2-fontrenderer.stringWidth("Option", 2)/2, 295, 2);
		   g2d.drawImage(B4Image, 250, 385, null);
		   fontrenderer.drawString(g2d, "Quit game", CANVAS_WIDTH/2-fontrenderer.stringWidth("Quit game", 2)/2, 395, 2);
		   g2d.drawImage(B4Image, 250, 455, null);
		   fontrenderer.drawString(g2d, "Credits", CANVAS_WIDTH/2-fontrenderer.stringWidth("Credits", 2)/2, 465, 2);
		   g2d.drawImage(logoImage, 50, 0, null);
		   break;
		   
	   case OPTIONS:
		   for(int i=0; i<vitesseFX.size(); i++)
			   if(!vitesseFX.get(i).foreground)
				   vitesseFX.get(i).draw(g2d);
		   
		   for(int i=0; i<bullets.size(); i++)
			   bullets.get(i).draw(g2d);
		   for(int i=0; i<aliens.size(); i++)
			   aliens.get(i).draw(g2d);

		   for(int i=0; i<vitesseFX.size(); i++)
			   if(vitesseFX.get(i).foreground)
				   vitesseFX.get(i).draw(g2d);
		   
		   fontrenderer.drawString(g2d, "Mouvement vers le haut : UP", (int)(CANVAS_WIDTH/2-fontrenderer.stringWidth("Mouvement vers le haut : UP", 2)/2), 150, 2);
		   fontrenderer.drawString(g2d, "Mouvement vers le bas : DOWN", (int)(CANVAS_WIDTH/2-fontrenderer.stringWidth("Mouvement vers le bas : DOWN", 2)/2), 170, 2);
		   fontrenderer.drawString(g2d, "Tirer : Space", (int)(CANVAS_WIDTH/2-fontrenderer.stringWidth("Tirer : Space", 2)/2), 190, 2);
		   fontrenderer.drawString(g2d, "return to main menu", (int)(CANVAS_WIDTH/2-fontrenderer.stringWidth("return to main menu", 2)/2), 345, 2);
		   g2d.drawImage(B4Image, 250, 335, null);
		   break;
		   
	   //TODO
	   case CREDITS:
		   for(int i=0; i<vitesseFX.size(); i++)
			   if(!vitesseFX.get(i).foreground)
				   vitesseFX.get(i).draw(g2d);
		   
		   for(int i=0; i<bullets.size(); i++)
			   bullets.get(i).draw(g2d);
		   for(int i=0; i<aliens.size(); i++)
			   aliens.get(i).draw(g2d);

		   for(int i=0; i<vitesseFX.size(); i++)
			   if(vitesseFX.get(i).foreground)
				   vitesseFX.get(i).draw(g2d);

		   fontrenderer.drawString(g2d, "Musique : Waiting", (int)(CANVAS_WIDTH/2-fontrenderer.stringWidth("Musique : Waiting", 2)/2), 180, 2);
		   fontrenderer.drawString(g2d, "Musique par Nikolai Azas", (int)(CANVAS_WIDTH/2-fontrenderer.stringWidth("Musique de Nikolai Azas", 2)/2), 200, 2);
		   fontrenderer.drawString(g2d, "Bruitages par Lorenzo Pilotto", (int)(CANVAS_WIDTH/2-fontrenderer.stringWidth("Bruitages par Lorenzo Pilotto", 2)/2), 220, 2);
		   fontrenderer.drawString(g2d, "Codeur : Lorenzo Pilotto", (int)(CANVAS_WIDTH/2-fontrenderer.stringWidth("Codeur : Lorenzo Pilotto", 2)/2), 240, 2);
		   fontrenderer.drawString(g2d, "Idee originale : Lorenzo Pilotto", (int)(CANVAS_WIDTH/2-fontrenderer.stringWidth("Idee originale : Lorenzo Pilotto", 2)/2), 260, 2);
		   fontrenderer.drawString(g2d, "Testeur : godfire", (int)(CANVAS_WIDTH/2-fontrenderer.stringWidth("Testeur : godfire", 2)/2), 280, 2);
		   fontrenderer.drawString(g2d, "return to main menu", (int)(CANVAS_WIDTH/2-fontrenderer.stringWidth("return to main menu", 2)/2), 345, 2);
		   g2d.drawImage(B4Image, 250, 335, null);
		   break;
		   
	   case PLAYING:
		   for(int i=0; i<vitesseFX.size(); i++)
			   if(!vitesseFX.get(i).foreground)
				   vitesseFX.get(i).draw(g2d);
		   
		   player.draw(g2d);
		   for(int i=0; i<bullets.size(); i++)
			   bullets.get(i).draw(g2d);
		   for(int i=0; i<aliens.size(); i++)
			   aliens.get(i).draw(g2d);

		   for(int i=0; i<vitesseFX.size(); i++)
			   if(vitesseFX.get(i).foreground)
				   vitesseFX.get(i).draw(g2d);
		   
		   fontrenderer.drawString(g2d, "Score : "+player.score, 5, 5, 2);
		   if(gamePaused)
		   {
			   g2d.drawImage(pauseImage, CANVAS_WIDTH/2-pauseImage.getWidth()/2, 16, null);
			   g2d.drawImage(B4Image, CANVAS_WIDTH/2-B4Image.getWidth()/2, 335, null);
			   fontrenderer.drawString(g2d, "return to main menu", (int)(CANVAS_WIDTH/2-fontrenderer.stringWidth("return to main menu", 2)/2), 345, 2);
		   }
		   break;
		   
	   case GAMEOVER:

		   for(int i=0; i<vitesseFX.size(); i++)
			   if(!vitesseFX.get(i).foreground)
				   vitesseFX.get(i).draw(g2d);
		   
		   player.draw(g2d);
		   for(int i=0; i<bullets.size(); i++)
			   bullets.get(i).draw(g2d);
		   for(int i=0; i<aliens.size(); i++)
			   aliens.get(i).draw(g2d);

		   for(int i=0; i<vitesseFX.size(); i++)
			   if(vitesseFX.get(i).foreground)
				   vitesseFX.get(i).draw(g2d);
		   
		   fontrenderer.drawString(g2d, "Score : "+player.score, 5, 5, 2);
		   g2d.drawImage(GOImage, 0, 0, CANVAS_WIDTH, CANVAS_HEIGHT, null);
		   fontrenderer.drawString(g2d, "return to main menu", (int)(CANVAS_WIDTH/2-fontrenderer.stringWidth("return to main menu", 2)/2), 345, 2);
		   g2d.drawImage(B4Image, 250, 335, null);
		   break;
	   }
	   fontrenderer.drawString(g2d, "FPS : " + FPS, 5, 30, 1);
	}
   
   // Process a key-pressed event.
   public void gameKeyPressed(int keyCode) {
      if(keyCode == KeyEvent.VK_UP)
    	  upPressed = true;
      else if(keyCode == KeyEvent.VK_DOWN)
    	  downPressed = true;
      if(keyCode == KeyEvent.VK_SPACE)
         player.Fire();
      if(keyCode == KeyEvent.VK_ESCAPE)
         gamePaused = !gamePaused;
   }
   
   public void gameMouseClicked(MouseEvent arg0) {
	}
   // Process a key-released event.
   public void gameKeyReleased(int keyCode) { 
	   if(keyCode == KeyEvent.VK_UP)
		   upPressed = false;
	   else if(keyCode == KeyEvent.VK_DOWN)
		   downPressed = false;
   }
   
   // Process a key-typed event.
   public void gameKeyTyped(char keyChar) {  }
   
   // Other methods
   // ......
   
   // Custom drawing panel, written as an inner class.
   class GameCanvas extends JPanel implements KeyListener, MouseListener {
	private static final long serialVersionUID = 1L;

	// Constructor
      public GameCanvas() {
    	 setDoubleBuffered(true);
         setFocusable(true);  // so that can receive key-events
         requestFocus();
         addKeyListener(this);
         addMouseListener(player);
         addMouseListener(this);
         addMouseMotionListener(player);
      }
   
      // Override paintComponent to do custom drawing.
      // Called back by repaint().
      @Override
      public void paintComponent(Graphics g) {
         Graphics2D g2d = (Graphics2D)g;  // if using Java 2D
         super.paintComponent(g2d);       // paint background
         setBackground(Color.black);      // may use an image for background
         
         // Draw the game objects
         gameDraw(g2d);
      }
      
      // KeyEvent handlers
      @Override
      public void keyPressed(KeyEvent e) {
         gameKeyPressed(e.getKeyCode());
      }
      
      @Override
      public void keyReleased(KeyEvent e) {
         gameKeyReleased(e.getKeyCode());
      }
   
      @Override
      public void keyTyped(KeyEvent e) {
         gameKeyTyped(e.getKeyChar());
      }

	@Override
	public void mouseClicked(MouseEvent arg0) {
		gameMouseClicked(arg0);
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		if(gameState == GameState.MAIN_MENU)
		{
			if(ButtonTest(250, 250, arg0.getX(), arg0.getY())){
				gameState = GameState.PLAYING;
				aliens.clear();
				player.score = 0;
				player.destroyed = false;
				player.timeToDespawn = 10;
			} else if(ButtonTest(250, 285, arg0.getX(), arg0.getY())){
				gameState = GameState.OPTIONS;
			} else if(ButtonTest(250, 385, arg0.getX(), arg0.getY())){
				System.exit(0);
			} else if(ButtonTest(250, 455, arg0.getX(), arg0.getY())){
				gameState = GameState.CREDITS;
			}
		} else if(gameState == GameState.GAMEOVER) {
			if(ButtonTest(250, 335, arg0.getX(), arg0.getY())){
				gameState = GameState.MAIN_MENU;
			}	
		} else if(gameState == GameState.OPTIONS){
			if(ButtonTest(250, 335, arg0.getX(), arg0.getY())){
				gameState = GameState.MAIN_MENU;
			}
		} else if(gameState == GameState.CREDITS){
			if(ButtonTest(250, 335, arg0.getX(), arg0.getY())){
				gameState = GameState.MAIN_MENU;
			}
		} else if(gameState == GameState.PLAYING && gamePaused){
			if(ButtonTest(250, 335, arg0.getX(), arg0.getY())){
				gamePaused = false;
				player.destroyed = true;
				player.timeToDespawn = 0;
				gameState = GameState.MAIN_MENU;
			}
		}
	}
	
	private boolean ButtonTest(int i, int j, int mx, int my){
		if(mx > i && mx < i+300 && my > j && my < j+35)
			return true;
		return false;
	}
	
	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
   }
   
   public static void main(String[] args) {
      SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
            new Framework();
         }
      });
   }

}