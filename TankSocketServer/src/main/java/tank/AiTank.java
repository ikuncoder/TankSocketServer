package tank;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.HashMap;

import com.kesar.a.KeSarStart;

import lskServer.SocketServer;
import wingman.GameWorld;
import wingman.game.BigExplosion;
import wingman.game.PlayerShip;
import wingman.modifiers.motions.InputController;

public class AiTank extends PlayerShip {
	int direction;
	public SocketServer socketServer = new SocketServer();
	KeSarStart keSarStart = new KeSarStart();
	TankWorld tankWorld = TankWorld.getInstance();

	public AiTank(Point location, Image img, int[] controls, String name) {
		super(location, new Point(0, 0), img, controls, name);
		resetPoint = new Point(location);
		this.gunLocation = new Point(32, 32);

		this.name = name;
		weapon = new AiTankWeapon();
		motion = new InputController(this, controls, TankWorld.getInstance());
		lives = 100;
		health = 100;
		strength = 100;
		score = 0;
		respawnCounter = 0;
		height = 64;
		width = 64;
		direction = 180;
		this.location = new Rectangle(location.x, location.y, width, height);
	}

	// You need to fill in here
	public void turn(int angle) {
		this.direction += angle;
		if (this.direction >= 360) {
			this.direction = 0;
		} else if (this.direction < 0) {
			this.direction = 359;
		}
	}

	// You need to fill in here
	public void update(int w, int h) {
		int player1X = 0;
		int player1Y = 0;
		ArrayList<PlayerShip> players = tankWorld.getPlayer();
		if (this.getName().equals("3")) {
			player1X = players.get(0).getLocation().x;
			player1Y = players.get(0).getLocation().y;
		} else if (this.getName().equals("4")) {
			player1X = players.get(1).getLocation().x;
			player1Y = players.get(1).getLocation().y;
		}

		/* ˼·����Ҫת����Ȼ����ǰ�� */
		// ̹�˵�λ��
		int[][] map = keSarStart.GetMap(player1X / 32, player1Y / 32, this.getLocation().x / 32,
				this.getLocation().y / 32, 8);
		int playerX = this.location.x / 32;
		int playerY = this.location.y / 32;
		//keSarStart.printMap(map);//打印地图
		// 8������
		if (map[playerY - 1][playerX] == 8) {// ��
			this.direction = 90;
			this.up = 1;
			while ((this.getX() / 32 != playerX) || (this.getX() % 32 != 0) || (this.getY() / 32 != (playerY - 1))
					|| (this.getY() % 32 != 0)) {
				int dy = (int) (1 * (double) Math.cos(Math.toRadians(direction + 90)));
				int dx = (int) (1 * (double) Math.sin(Math.toRadians(this.direction + 90)));
				location.x += dx * (up - down);
				location.y += dy * (up - down);
			}
			map[playerY][playerX] = 0;// ��ԭ���ĵ���������Ϊ0
			this.up = 0;
		} else if (map[playerY + 1][playerX] == 8) {// ����
			this.direction = 270;
			this.up = 1;
			while ((this.getX() / 32 != playerX) || (this.getX() % 32 != 0) || (this.getY() / 32 != (playerY + 1))
					|| (this.getY() % 32 != 0)) {
				int dy = (int) (1 * (double) Math.cos(Math.toRadians(direction + 90)));
				int dx = (int) (1 * (double) Math.sin(Math.toRadians(this.direction + 90)));
				location.x += dx * (up - down);
				location.y += dy * (up - down);
			}
			map[playerY][playerX] = 0;// ��ԭ���ĵ���������Ϊ0
			this.up = 0;
		} else if (map[playerY][playerX - 1] == 8) {// ���
			this.direction = 180;
			this.up = 1;
			while ((this.getX() / 32) != (playerX - 1) || (this.getX() % 32 != 0)) {
				int dy = (int) (1 * (double) Math.cos(Math.toRadians(direction + 90)));
				int dx = (int) (1 * (double) Math.sin(Math.toRadians(this.direction + 90)));
				location.x += dx * (up - down);
				location.y += dy * (up - down);
			}
			map[playerY][playerX] = 0;// ��ԭ���ĵ���������Ϊ0
			this.up = 0;
		} else if (map[playerY][playerX + 1] == 8) {// �ұ�
			this.direction = 0;
			this.up = 1;
			while ((this.getX() / 32 != (playerX + 1)) || (this.getX() % 32 != 0) || (this.getY() / 32 != playerY)
					|| (this.getY() % 32 != 0)) {
				int dy = (int) (1 * (double) Math.cos(Math.toRadians(direction + 90)));
				int dx = (int) (1 * (double) Math.sin(Math.toRadians(this.direction + 90)));
				location.x += dx * (up - down);
				location.y += dy * (up - down);
			}
			map[playerY][playerX] = 0;// ��ԭ���ĵ���������Ϊ0
			this.up = 0;
		} else if (map[playerY - 1][playerX - 1] == 8) {// ����
			this.direction = 135;
			this.up = 1;
			while ((this.getX() / 32 != playerX - 1) || (this.getX() % 32 != 0)
					|| (this.location.y / 32 != (playerY - 1)) || (this.getY() % 32 != 0)) {
				int dy = ((int) (5 * (double) Math.cos(Math.toRadians(direction + 90)))) / 3;
				// (int) (5 * (double) Math.sin(Math.toRadians(this.direction + 90)))=-3
				int dx = ((int) (5 * (double) Math.sin(Math.toRadians(this.direction + 90)))) / 3;
				location.x += dx * (up - down);
				location.y += dy * (up - down);
			}
			map[playerY][playerX] = 0;
			this.up = 0;
		} else if (map[playerY + 1][playerX - 1] == 8) {// ����
			this.direction = 225;
			this.up = 1;
			while ((this.getX() / 32 != (playerX - 1)) || (this.getX() % 32 != 0)
					|| (this.getY() / 32 != (playerY + 1)) || (this.getY() % 32 != 0)) {
				int dy = ((int) (5 * (double) Math.cos(Math.toRadians(direction + 90)))) / 3;
				int dx = ((int) (5 * (double) Math.sin(Math.toRadians(this.direction + 90)))) / 3;
				location.x += dx * (up - down);
				location.y += dy * (up - down);
			}
			map[playerY][playerX] = 0;
			this.up = 0;
		} else if (map[playerY - 1][playerX + 1] == 8) {// ����
			this.direction = 45;
			this.up = 1;
			while ((this.getX() / 32 != playerX + 1) || (this.getX() % 32 != 0)
					|| (this.getY() / 32 != (playerY - 1)) || (this.getY() % 32 != 0)) {
				int dy = ((int) (5 * (double) Math.cos(Math.toRadians(direction + 90)))) / 3;
				int dx = (int) (5 * (double) Math.sin(Math.toRadians(this.direction + 90))) / 3;
				location.x += dx * (up - down);
				location.y += dy * (up - down);
			}
			map[playerY][playerX] = 0;
			this.up = 0;
		} else if (map[playerY + 1][playerX + 1] == 8) {// ����
			this.direction = 315;
			this.up = 1;
			while ((this.getX() / 32 != (playerX + 1)) || (this.getX() % 32 != 0)
					|| (this.getY() / 32 != (playerY + 1)) || (this.getY() % 32 != 0)) {
				int dy = ((int) (5 * (double) Math.cos(Math.toRadians(direction + 90)))) / 3;
				int dx = ((int) (5 * (double) Math.sin(Math.toRadians(this.direction + 90)))) / 3;
				location.x += dx * (up - down);
				location.y += dy * (up - down);
			}
			map[playerY][playerX] = 0;
			this.up = 0;
		} else {//����������Զ���������һ�㲻�ᷢ��
			String[] strings = {"up", "up", "up", "up", "up", "up", "firing"};
			for (int i = 0; i < strings.length; i++) {
				if (strings[i].equals("turn")) {
					right = 1;
					this.turn(180 * (left - right));
				} else if (strings[i].equals("up")) {
					up = 1;
					int dy = (int) (1 * (double) Math.cos(Math.toRadians(direction + 90)));
					int dx = (int) (1 * (double) Math.sin(Math.toRadians(this.direction + 90)));
					location.x += dx * (up - down);
					location.y += dy * (up - down);
				} else if (strings[i].equals("firing")) {
					isFiring = true;
					if (isFiring) {
						int frame = TankWorld.getInstance().getFrameNumber();
						if (frame >= lastFired + weapon.reload) {
							fire();
							lastFired = frame;
						}
					}

				}
			}
		}
		//����
		this.isFiring = true;
		if (this.isFiring) {
			int frame = TankWorld.getInstance().getFrameNumber();
			if (frame >= lastFired + weapon.reload) {
				fire();
				lastFired = frame;
			}
		}
		this.isFiring = false;
		if (location.y < 0)
			location.y = 0;
		if (location.y > h - this.height)
			location.y = h - this.height;
		if (location.x < 0)
			location.x = 0;
		if (location.x > w - this.width)
			location.x = w - this.width;
	}

	public void draw(Graphics g, ImageObserver obs) {
		if (respawnCounter <= 0) {
			g.drawImage(img, // the image
					location.x, location.y, // destination top left
					location.x + this.getSizeX(), location.y + this.getSizeY(), // destination lower right
					(direction / 6) * this.getSizeX(), 0, // source top left
					((direction / 6) * this.getSizeX()) + this.getSizeX(), this.getSizeY(), // source lower right
					obs);
		} else if (respawnCounter == 80) {
			TankWorld.getInstance().addClockObserver(this.motion);
			respawnCounter -= 1;
		} else if (respawnCounter < 80) {
			if (respawnCounter % 2 == 0) {
				g.drawImage(img, // the image
						location.x, location.y, // destination top left
						location.x + this.getSizeX(), location.y + this.getSizeY(), // destination lower right
						(direction / 6) * this.getSizeX(), 0, // source top left
						((direction / 6) * this.getSizeX()) + this.getSizeX(), this.getSizeY(), // source lower right
						obs);
				// ����Ϣ֪ͨ�ͻ���ˢ�¸�������tank
				socketServer.sendHander("AirespawnCounter" + "+" + this.getName() + "+" + location.x + "+" + location.y
						+ "+" + this.direction + "+" + this.getLives() + "+" + this.getHealth() + "+" + this.getScore()
						+ "+" + this.getHealth() + "+" + this.respawnCounter);
			}
			respawnCounter -= 1;
		} else {
			// ������>80��<160
			respawnCounter -= 1;

			socketServer.sendHander("AirespawnCounter" + "+" + this.getName() + "+" + location.x + "+" + location.y
					+ "+" + this.direction + "+" + this.getLives() + "+" + this.getHealth() + "+" + this.getScore()
					+ "+" + this.getHealth() + "+" + this.respawnCounter);
		}

	}

	public void die() {
		this.show = false;
		// ֪ͨ��������tank��ʧ
		socketServer.sendHander("tankDisappear" + "+" + this.getName() + "+" + location.x + "+" + location.y + "+" + 0
				+ "+" + 0 + "+" + 0 + "+" + 0 + "+" + 0);
		GameWorld.setSpeed(new Point(0, 0));
		BigExplosion explosion = new BigExplosion(new Point(location.x, location.y));
		TankWorld.getInstance().addBackground(explosion);
		// ��ը��ʾָ��
		socketServer.sendHander("BigExplosion" + "+" + 0 + "+" + location.x + "+" + location.y + "+" + 0 + "+" + 0 + "+"
				+ 0 + "+" + 0 + "+" + 0);
		lives -= 1;
		if (lives >= 0) {
			TankWorld.getInstance().removeClockObserver(this.motion);
			reset();
		} else {
			this.motion.delete(this);
		}
	}

	public void reset() {
		this.setLocation(resetPoint);
		health = strength;
		respawnCounter = 160;
		this.weapon = new AiTankWeapon();
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

}
