package tank;

import wingman.GameClock;
import wingman.GameSounds;
import wingman.GameWorld;
import wingman.game.*;
import wingman.modifiers.AbstractGameModifier;
import wingman.modifiers.motions.MotionController;
import wingman.modifiers.weapons.AbstractWeapon;
import wingman.modifiers.weapons.PulseWeapon;
import wingman.modifiers.weapons.SimpleWeapon;
import wingman.ui.GameMenu;
import wingman.ui.InfoBar;
import wingman.ui.InterfaceObject;

import javax.swing.*;

import lskServer.SocketServer;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.net.URL;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class TankWorld extends GameWorld implements Runnable{

	public static final GameSounds sound = new GameSounds();
	public static final GameClock clock = new GameClock();
	// GameWorld is a singleton class!
	private static final TankWorld game = new TankWorld();
	public static HashMap<String, Image> sprites = GameWorld.sprites;
	public static HashMap<String, MotionController> motions = new HashMap<String, MotionController>();
	public TankLevel level;
	GameMenu menu;
	int score = 0, life = 4;
	Random generator = new Random();
	int sizeX, sizeY;
	Point mapSize;
	// is player still playing, did they win, and should we exit
	boolean gameOver, gameWon, gameFinished;
	ImageObserver observer;
	private Thread thread;
	private BufferedImage bimg, player1view, player2view;
	/* Some ArrayLists to keep track of game things */
	//private ArrayList<Bullet> bullets;
	private CopyOnWriteArrayList<Bullet> bullets;
	private ArrayList<PlayerShip> players;
	private ArrayList<InterfaceObject> ui;
	private ArrayList<Ship> powerups;
	private ArrayList<PlayerShip> aiplayers;
	public SocketServer socketServer;
	public int mapNum;

	// constructors makes sure the game is focusable, then
	// initializes a bunch of ArrayLists
	private TankWorld() {
		this.setFocusable(true);
		background = new ArrayList<BackgroundObject>();
		bullets=new CopyOnWriteArrayList<>();
		players = new ArrayList<PlayerShip>();
		ui = new ArrayList<InterfaceObject>();
		powerups = new ArrayList<Ship>();
		aiplayers=new ArrayList<>();
		socketServer = new SocketServer();
	}

	/* This returns a reference to the currently running game */
	public static TankWorld getInstance() {
		return game;
	}

	public void tankWorkStart() {
		final TankWorld game = TankWorld.getInstance();
		JFrame f = new JFrame("TankWars Game");
		f.addWindowListener(new WindowAdapter() {
			public void windowGainedFocus(WindowEvent e) {
				game.requestFocusInWindow();
			}
		});
		f.getContentPane().add("Center", game);
		f.pack();
		// 图形界面的总体的大小
		f.setSize(new Dimension(900, 600));
		game.setDimensions(800, 600);
		//随机地图的选择
		Random random=new Random();
		mapNum=random.nextInt(3);
		//分配客户端playerId
		//第三个字符表示tank的name
		socketServer.sendHander("playerId" + "+" + 0 + "+" +0+ "+" + 0 + "+"
				+ 0 + "+" + 0 + "+" + 0 + "+" + 0
				+ "+" + "0");
		socketServer.sendHander("playerId" + "+" + 1+ "+" +1+ "+" + 0 + "+"
				+ 0 + "+" + 0 + "+" + 0 + "+" + 0
				+ "+" + "0");
		socketServer.sendHander("randomMap" + "+" + 0 + "+" + mapNum + "+" + 0 + "+"
				+ 0 + "+" + 0 + "+" + 0 + "+" + 0
				+ "+" + "0");
		game.init();
		f.setVisible(true);
		//设置该框架是否可以由用户调整大小。
		f.setResizable(false);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GameWorld.sound.playmp3("Resources/AlliedForces.mp3");
		//新建一条线程，处理aiplayer
		new Thread(new AiTankHander()).start();
		//此线程是处理用户的tank
		game.start();
	}

	/* Game Initialization */
	public void init() {
		setBackground(Color.white);
		loadSprites();
		gameOver = false;
		observer = this;
		String filename="Resources/level"+mapNum+".txt";
		level = new TankLevel(filename);
		level.addObserver(this);
		clock.addObserver(level);
		// 小地图的大小
		mapSize = new Point(level.w * 32, level.h * 32);
		GameWorld.setSpeed(new Point(0, 0));
		addBackground(new Background(mapSize.x, mapSize.y, GameWorld.getSpeed(), sprites.get("background")));

		level.load();
	}
	
	
	/* start the game thread */
	public void start() {
		//该线程是处理用户tank的线程
		thread = new Thread(this);
		//thread.setPriority(Thread.MIN_PRIORITY);
		thread.setPriority(Thread.MAX_PRIORITY);
		thread.start();
	}

	/* run the game */
	public void run() {
		Thread me = Thread.currentThread();
		while (thread == me) {
			this.requestFocusInWindow();
			repaint();
			try {
				// 每隔23ms刷新一次
				thread.sleep(23); // pause a little to slow things down
			} catch (InterruptedException e) {
				break;
			}
		}
	}
	
	

	// this is the main function where game stuff happens!
	// each frame is also drawn here
	public void drawFrame(int w, int h, Graphics2D g2) {
		ListIterator<?> iterator = getBackgroundObjects();
		// iterate through all blocks
		while (iterator.hasNext()) {
			BackgroundObject obj = (BackgroundObject) iterator.next();
			obj.update(w, h);			
			//这里是画地图背景的
			obj.draw(g2, this);
			if (obj instanceof BigExplosion || obj instanceof SmallExplosion) {// BigExplosion 大爆炸
				if (!obj.show)
					iterator.remove();
				continue;
			}
			// check player-block collisions
			// 处理物体与player相交的关系
			ListIterator<PlayerShip> players = getPlayers();
			while (players.hasNext() && obj.show) {
				Tank player = (Tank) players.next();
				if (obj.collision(player)) {
					Rectangle location = obj.getLocation();
					Rectangle playerLocation = player.getLocation();
					if (playerLocation.y < location.y)
						player.move(0, -2);
					if (playerLocation.y > location.y)
						player.move(0, 2);
					if (playerLocation.x < location.x)
						player.move(-2, 0);
					if (playerLocation.x > location.x)
						player.move(2, 0);				
					// 这里进行不断的发送消息
					socketServer.sendHander("1" + "+" + player.getName() + "+" + player.getLocation().x + "+" + player.getLocation().y + "+"
							+ ((Tank) player).getDirection() + "+" + player.getLives() + "+" + player.getHealth() + "+" + player.getScore()
							+ "+" + player.getStrength());
				}
			}
			// 这里也要不断刷新tank的位置
		}

		if (!gameFinished) {
			//ListIterator<Bullet> bullets = this.getBullets();
			//ArrayList<Bullet> bullets2=this.getBullet();
			 bullets=this.getBullet();
			for(int i=0;i<bullets.size();i++) {
				Bullet bullet = bullets.get(i);											
				if (bullet.getY() > h + 10 || bullet.getY() < -10 || bullet.getX() > w + 10 || bullet.getX() < -10) {
					// 发消息移除子弹
					socketServer.sendHander("#" + "+" + bullet.getBulletID() + "+" + bullet.getLocationPoint().x + "+"
							+ bullet.getLocationPoint().y + "+" + 0 + "+" + 0 + "+" + 0 + "+" + 0 + "+" + 0);
					bullets.remove(i);
				} else {
					iterator = this.getBackgroundObjects();
					while (iterator.hasNext()) {
						GameObject other = (GameObject) iterator.next();
						if (other.show && other.collision(bullet)) {
							addBackground(new SmallExplosion(bullet.getLocationPoint()));
							// 小爆炸显示指令
							socketServer.sendHander("SmallExplosion" + "+" + bullet.getBulletID() + "+"
									+ bullet.getLocationPoint().x + "+" + bullet.getLocationPoint().y + "+" + 0 + "+"
									+ 0 + "+" + 0 + "+" + 0 + "+" + 0);
							// 子弹移除指令
							socketServer.sendHander("#" + "+" + bullet.getBulletID() + "+" + bullet.getLocationPoint().x
									+ "+" + bullet.getLocationPoint().y + "+" + 0 + "+" + 0 + "+" + 0 + "+" + 0 + "+"
									+ 0);
							bullets.remove(i);
							break;
						}

					}
				}
				// 发送子弹位置指令
				socketServer.sendHander("*" + "+" + bullet.BulletID + "+" + bullet.getLocationPoint().x + "+"
						+ bullet.getLocationPoint().y + "+" + 0 + "+" + 0 + "+" + 0 + "+" + 0 + "+" + 0);
				bullet.draw(g2, this);
			}
			
			
			//这里处理的是AiTank放出来的子弹碰到tank的情景
			iterator = this.getAiPlayers();
			while (iterator.hasNext()) {
				PlayerShip aiplayer = (PlayerShip) iterator.next();
				//bullets = this.getBullets();
				bullets=this.getBullet();
				for(int i=0;i<bullets.size();i++) {
					Bullet bullet =bullets.get(i);
					if (bullet.collision(aiplayer) && aiplayer.respawnCounter <= 0 && bullet.getOwner() != aiplayer&&!(bullet.getOwner() instanceof AiTank)) {
						aiplayer.damage(bullet.getStrength());
						bullet.getOwner().incrementScore(bullet.getStrength());
						this.addBackground(new SmallExplosion(bullet.getLocationPoint()));
						// 小爆炸显示指令
						socketServer.sendHander("SmallExplosion" + "+" + bullet.getBulletID() + "+"
								+ bullet.getLocationPoint().x + "+" + bullet.getLocationPoint().y + "+" + 0 + "+"
								+ 0 + "+" + 0 + "+" + 0 + "+" + 0);
						// 发送子弹位置指令      新增
						socketServer.sendHander("*" + "+" + bullet.BulletID + "+" + bullet.getLocationPoint().x + "+"
								+ bullet.getLocationPoint().y + "+" + 0 + "+" + 0 + "+" + 0 + "+" + 0 + "+" + 0);
						// 子弹移除指令
						socketServer.sendHander("#" + "+" + bullet.getBulletID() + "+" + bullet.getLocationPoint().x
								+ "+" + bullet.getLocationPoint().y + "+" + 0 + "+" + 0 + "+" + 0 + "+" + 0 + "+"
								+ 0);
						bullets.remove(i);
					}
				}
			}
			

			// update players and draw
			iterator = getPlayers();
			while (iterator.hasNext()) {
				PlayerShip player = (PlayerShip) iterator.next();

				if (player.isDead()) {
					gameOver = true;
					continue;
				}

				//bullets = this.getBullets();
				bullets=this.getBullet();
				for(int i=0;i<bullets.size();i++) {
					//Bullet bullet = bullets.next();
					Bullet bullet=bullets.get(i);
					if (bullet.collision(player) && player.respawnCounter <= 0 && bullet.getOwner() != player) {
						player.damage(bullet.getStrength());
						bullet.getOwner().incrementScore(bullet.getStrength());
						addBackground(new SmallExplosion(bullet.getLocationPoint()));
						// 小爆炸显示指令
						socketServer.sendHander("SmallExplosion" + "+" + bullet.getBulletID() + "+"
								+ bullet.getLocationPoint().x + "+" + bullet.getLocationPoint().y + "+" + 0 + "+"
								+ 0 + "+" + 0 + "+" + 0 + "+" + 0);
						
						// 发送子弹位置指令
						socketServer.sendHander("*" + "+" + bullet.BulletID + "+" + bullet.getLocationPoint().x + "+"
								+ bullet.getLocationPoint().y + "+" + 0 + "+" + 0 + "+" + 0 + "+" + 0 + "+" + 0);
						
						// 子弹移除指令
						socketServer.sendHander("#" + "+" + bullet.getBulletID() + "+" + bullet.getLocationPoint().x
								+ "+" + bullet.getLocationPoint().y + "+" + 0 + "+" + 0 + "+" + 0 + "+" + 0 + "+"
								+ 0);
						bullets.remove(i);
					}
				}

				ListIterator<Ship> powerups = this.powerups.listIterator();
				while (powerups.hasNext()) {
					Ship powerup = powerups.next();
					powerup.draw(g2, this);
					if (powerup.collision(player) && player.respawnCounter <= 0) {
						AbstractWeapon weapon = powerup.getWeapon();						
						//tank武器升级
						socketServer.sendHander("powerup" + "+" + player.getName() + "+" + player.getLocationPoint().x
								+ "+" + player.getLocationPoint().y + "+" + 0 + "+" + 0 + "+" + 0 + "+" + 0 + "+"
								+ 0);
						player.setWeapon(weapon);
						//能量块消失
						powerup.die();
						player.setWeapon(weapon);
					}
				}
			}
			
			
			PlayerShip p1 = players.get(0);
			PlayerShip p2 = players.get(1);

			p1.update(w, h);

			if (p1.collision(p2)) {
				Rectangle playerLocation = p1.getLocation();
				Rectangle location = p2.getLocation();
				if (playerLocation.y < location.y)
					p1.move(0, -2);
				if (playerLocation.y > location.y)
					p1.move(0, 2);
				if (playerLocation.x < location.x)
					p1.move(-2, 0);
				if (playerLocation.x > location.x)
					p1.move(2, 0);
			}
			//处理ai和tank的关系
			if (p1.collision(getAiPlayer().get(0))) {
				Rectangle playerLocation = p1.getLocation();
				Rectangle location = getAiPlayer().get(0).getLocation();
				if (playerLocation.y < location.y)
					p1.move(0, -2);
				if (playerLocation.y > location.y)
					p1.move(0, 2);
				if (playerLocation.x < location.x)
					p1.move(-2, 0);
				if (playerLocation.x > location.x)
					p1.move(2, 0);
			}

			p2.update(w, h);
			if (p2.collision(p1)) {
				Rectangle playerLocation = p2.getLocation();
				Rectangle location = p1.getLocation();
				if (playerLocation.y < location.y)
					p2.move(0, -1);
				if (playerLocation.y > location.y)
					p2.move(0, 1);
				if (playerLocation.x < location.x)
					p2.move(-1, 0);
				if (playerLocation.x > location.x)
					p2.move(1, 0);
			}

			//处理ai和tank的关系
			if (p2.collision(getAiPlayer().get(1))) {
				Rectangle playerLocation = p2.getLocation();
				Rectangle location = getAiPlayer().get(1).getLocation();
				if (playerLocation.y < location.y)
					p2.move(0, -1);
				if (playerLocation.y > location.y)
					p2.move(0, 1);
				if (playerLocation.x < location.x)
					p2.move(-1, 0);
				if (playerLocation.x > location.x)
					p2.move(1, 0);
			}
			p1.draw(g2, this);
			p2.draw(g2, this);
			// 这里进行不断的发送消息
			socketServer.sendHander("1" + "+" + p1.getName() + "+" + p1.getLocation().x + "+" + p1.getLocation().y + "+"
					+ ((Tank) p1).getDirection() + "+" + p1.getLives() + "+" + p1.getHealth() + "+" + p1.getScore()
					+ "+" + p1.getStrength()+"+"+p2.respawnCounter);
			socketServer.sendHander("1" + "+" + p2.getName() + "+" + p2.getLocation().x + "+" + p2.getLocation().y + "+"
					+ ((Tank) p2).getDirection() + "+" + p2.getLives() + "+" + p2.getHealth() + "+" + p2.getScore()
					+ "+" + p2.getStrength()+"+"+p2.respawnCounter);
			//aiplayer
			PlayerShip p3 = aiplayers.get(0);
			p3.draw(g2, this);
			PlayerShip p4=aiplayers.get(1);
			p4.draw(g2, this);
			socketServer.sendHander("11" + "+" + p3.getName() + "+" + p3.getLocation().x + "+" + p3.getLocation().y + "+"
					+ ((AiTank) p3).getDirection() + "+" + p3.getLives() + "+" + p3.getHealth() + "+" + p3.getScore()
					+ "+" + p1.getStrength()+"+"+p2.respawnCounter);
			socketServer.sendHander("11" + "+" + p4.getName() + "+" + p4.getLocation().x + "+" + p4.getLocation().y + "+"
					+ ((AiTank) p4).getDirection() + "+" + p4.getLives() + "+" + p4.getHealth() + "+" + p4.getScore()
					+ "+" + p4.getStrength()+"+"+p4.respawnCounter);
		}
		// end game stuff
		else {
			g2.setColor(Color.WHITE);
			g2.setFont(new Font("Calibri", Font.PLAIN, 24));
			if (!gameWon) {
				g2.drawImage(sprites.get("gameover"), w / 3 - 50, h / 2, null);
				
				socketServer.sendHander("gameFinishedAndNotgameWon" + "+" + 1 + "+" + 0 + "+" + 0 + "+"
						+ 0 + "+" + 0 + "+" + 0 + "+" + 0
						+ "+" + 0);
			} else {
				//发送消息通知客户端win
				socketServer.sendHander("gameFinishedAndGameWon" + "+" + 1 + "+" + 0 + "+" + 0 + "+"
						+ 0 + "+" + 0 + "+" + 0 + "+" + 0
						+ "+" + 0);
				g2.drawImage(sprites.get("youwon"), sizeX / 3, 100, null);
			}
			g2.drawString("Score", sizeX / 3, 400);
			int i = 1;
			for (PlayerShip player : players) {
				g2.drawString(player.getName() + ": " + Integer.toString(player.getScore()), sizeX / 3, 375 + 50 * i);
				i++;
			}

		}

	}

	
	/* paint each frame */
	public void paint(Graphics g) {
		if (players.size() != 0)
			clock.tick();
		Dimension windowSize = getSize();
		
		Graphics2D g2 = createGraphics2D(mapSize.x, mapSize.y);
		drawFrame(mapSize.x, mapSize.y, g2);
		g2.dispose();
		int p1x = this.players.get(0).getX() - windowSize.width / 4 > 0
				? this.players.get(0).getX() - windowSize.width / 4
				: 0;
		int p1y = this.players.get(0).getY() - windowSize.height / 2 > 0
				? this.players.get(0).getY() - windowSize.height / 2
				: 0;

		if (p1x > mapSize.x - windowSize.width / 2) {
			p1x = mapSize.x - windowSize.width / 2;
		}
		if (p1y > mapSize.y - windowSize.height) {
			p1y = mapSize.y - windowSize.height;
		}

		int p2x = this.players.get(1).getX() - windowSize.width / 4 > 0
				? this.players.get(1).getX() - windowSize.width / 4
				: 0;
		int p2y = this.players.get(1).getY() - windowSize.height / 2 > 0
				? this.players.get(1).getY() - windowSize.height / 2
				: 0;

		if (p2x > mapSize.x - windowSize.width / 2) {
			p2x = mapSize.x - windowSize.width / 2;
		}
		if (p2y > mapSize.y - windowSize.height) {
			p2y = mapSize.y - windowSize.height;
		}

		player1view = bimg.getSubimage(p1x, p1y, windowSize.width / 2, windowSize.height);
		player2view = bimg.getSubimage(p2x, p2y, windowSize.width / 2, windowSize.height);
		g.drawImage(player1view, 0, 0, this);
		g.drawImage(player2view, windowSize.width / 2, 0, this);
		g.drawRect(windowSize.width / 2 - 1, 0, 1, windowSize.height);
		g.drawRect(windowSize.width / 2 - 76, 399, 151, 151);
		g.drawImage(bimg, windowSize.width / 2 - 75, 400, 150, 150, observer);

		// interface stuff
		ListIterator<InterfaceObject> objects = ui.listIterator();
		int offset = 0;
		while (objects.hasNext()) {
			InterfaceObject object = objects.next();
			object.draw(g, offset, windowSize.height);
			offset += 500;
		}
	}

	/* Functions for loading image resources */
	protected void loadSprites() {
		sprites.put("background", getSprite("Resources/Background.png"));

		sprites.put("wall", getSprite("Resources/Blue_wall1.png"));
		sprites.put("wall2", getSprite("Resources/Blue_wall2.png"));

		sprites.put("bullet", getSprite("Resources/bullet.png"));
		sprites.put("powerup", getSprite("Resources/powerup.png"));

		sprites.put("explosion1_1", getSprite("Resources/explosion1_1.png"));
		sprites.put("explosion1_2", getSprite("Resources/explosion1_2.png"));
		sprites.put("explosion1_3", getSprite("Resources/explosion1_3.png"));
		sprites.put("explosion1_4", getSprite("Resources/explosion1_4.png"));
		sprites.put("explosion1_5", getSprite("Resources/explosion1_5.png"));
		sprites.put("explosion1_6", getSprite("Resources/explosion1_6.png"));
		sprites.put("explosion2_1", getSprite("Resources/explosion2_1.png"));
		sprites.put("explosion2_2", getSprite("Resources/explosion2_2.png"));
		sprites.put("explosion2_3", getSprite("Resources/explosion2_3.png"));
		sprites.put("explosion2_4", getSprite("Resources/explosion2_4.png"));
		sprites.put("explosion2_5", getSprite("Resources/explosion2_5.png"));
		sprites.put("explosion2_6", getSprite("Resources/explosion2_6.png"));
		sprites.put("explosion2_7", getSprite("Resources/explosion2_7.png"));

		sprites.put("player1", getSprite("Resources/Tank_blue_basic_strip60.png"));
		sprites.put("player2", getSprite("Resources/Tank_blue_basic_strip60.png"));
		
		sprites.put("aiplayer1", getSprite("Resources/Tank_blue_basic_strip60.png"));
		sprites.put("aiplayer2", getSprite("Resources/Tank_blue_basic_strip60.png"));
	}

	public Image getSprite(String name) {
		URL url = TankWorld.class.getResource(name);
		Image img = java.awt.Toolkit.getDefaultToolkit().getImage(url);
		try {
			MediaTracker tracker = new MediaTracker(this);
			tracker.addImage(img, 0);
			tracker.waitForID(0);
		} catch (Exception e) {
		}
		return img;
	}

	/********************************
	 * These functions GET things * from the game world *
	 ********************************/

	public int getFrameNumber() {
		return clock.getFrame();
	}

	public int getTime() {
		return clock.getTime();
	}

	public void removeClockObserver(Observer theObject) {
		clock.deleteObserver(theObject);
	}

	public ListIterator<BackgroundObject> getBackgroundObjects() {
		return background.listIterator();
	}

	public ListIterator<PlayerShip> getPlayers() {
		return players.listIterator();
	}
	
	public ArrayList<PlayerShip> getPlayer(){
		return players;
	}
	
	public ListIterator<PlayerShip> getAiPlayers() {
		return aiplayers.listIterator();
	} 
	
	public ArrayList<PlayerShip> getAiPlayer(){
		return aiplayers;
	}

	public ListIterator<Bullet> getBullets() {
		return bullets.listIterator();
	}

	public CopyOnWriteArrayList<Bullet> getBullet(){
		return bullets;
	}
	
	
	
	public int countPlayers() {
		return players.size();
	}

	public void setDimensions(int w, int h) {
		this.sizeX = w;
		this.sizeY = h;
	}

	/********************************
	 * These functions ADD things * to the game world *
	 ********************************/

	public void addBullet(Bullet... newObjects) {
		for (Bullet bullet : newObjects) {
			bullets.add(bullet);
		}
	}

	public void addPlayer(PlayerShip... newObjects) {
		for (PlayerShip player : newObjects) {
			players.add(player);
			ui.add(new InfoBar(player, Integer.toString(players.size())));
		}
	}

	public void addAiPlayer(PlayerShip... newObjects) {
		for (PlayerShip player : newObjects) {
			aiplayers.add(player);
		}
	}
	
	
	// add background items (islands)
	public void addBackground(BackgroundObject... newObjects) {
		for (BackgroundObject object : newObjects) {
			background.add(object);
		}
	}

	// add power ups to the game world
	public void addPowerUp(Ship powerup) {
		powerups.add(powerup);
	}

	public void addRandomPowerUp() {
		// rapid fire weapon or pulse weapon
		if (generator.nextInt(10) % 2 == 0)
			powerups.add(new PowerUp(generator.nextInt(sizeX), 1, new SimpleWeapon(5)));
		else {
			powerups.add(new PowerUp(generator.nextInt(sizeX), 1, new PulseWeapon()));
		}
	}

	public void addClockObserver(Observer theObject) {
		clock.addObserver(theObject);
	}

	public Graphics2D createGraphics2D(int w, int h) {
		Graphics2D g2 = null;
		if (bimg == null || bimg.getWidth() != w || bimg.getHeight() != h) {
			bimg = (BufferedImage) createImage(w, h);
		}
		g2 = bimg.createGraphics();
		g2.setBackground(getBackground());
		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2.clearRect(0, 0, w, h);
		return g2;
	}



	/* End the game, and signal either a win or loss */
	public void endGame(boolean win) {
		this.gameOver = true;
		this.gameWon = win;
	}

	public boolean isGameOver() {
		return gameOver;
	}

	// signal that we can stop entering the game loop
	public void finishGame() {
		gameFinished = true;
	}

	/*
	 * I use the 'read' function to have observables act on their observers.
	 */
	@Override
	public void update(Observable o, Object arg) {
		AbstractGameModifier modifier = (AbstractGameModifier) o;
		modifier.read(this);
	}
}