package tank;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.ListIterator;

import com.kesar.a.KeSarStart;

import lskServer.SocketServer;
import wingman.GameClock;
import wingman.game.BackgroundObject;
import wingman.game.Bullet;
import wingman.game.PlayerShip;
import wingman.game.Ship;
import wingman.game.SmallExplosion;
import wingman.modifiers.weapons.AbstractWeapon;

public class AiTankHander implements Runnable {

	private Point mapsize ;
	//public TankLevel level = new TankLevel("Resources/level.txt");
	TankLevel level = new TankLevel("Resources/level"+TankWorld.getInstance().mapNum+".txt");
	public TankWorld tankWorld = TankWorld.getInstance();;
	public ArrayList<PlayerShip> aiplayer;
	public Graphics2D g2;
	public SocketServer socketServer;
	public static final GameClock clock = new GameClock();
	
	public AiTankHander(){
		mapsize = new Point(level.w * 32, level.h * 32);
		aiplayer = tankWorld.getAiPlayer();
		g2 = tankWorld.createGraphics2D(mapsize.x, mapsize.y);
		socketServer=new SocketServer();
	}
	
	{
		level.addObserver(tankWorld);
		clock.addObserver(level);
	}

	@Override
	public void run() {
		Thread thread = Thread.currentThread();
		//new KeSarStart().Start();
		while (true) {
			try {
				// ÿ��23msˢ��һ��
				thread.sleep(3000); // pause a little to slow things down
			} catch (InterruptedException e) {
				break;
			}
			// ˢ�µ���Ai
			PlayerShip p3 = aiplayer.get(0);
			PlayerShip p4=aiplayer.get(1);
			// p3.draw(g2, tankWorld);
			p3.update(mapsize.x, mapsize.y);
			p4.update(mapsize.x, mapsize.y);
		}

	}

}
