package wingman.modifiers.weapons;

import wingman.GameWorld;
import wingman.WingmanWorld;
import wingman.game.Bullet;
import wingman.game.PlayerShip;
import wingman.game.Ship;
import wingman.modifiers.AbstractGameModifier;

import java.util.ArrayList;
import java.util.Observer;

import lskServer.SocketServer;

/*Weapons are fired by motion controllers on behalf of players or ships
 * They observe motions and are observed by the Game World
 */
public abstract class AbstractWeapon extends AbstractGameModifier {
    public int reload = 5;
    protected Bullet[] bullets;
    protected int direction;
    boolean friendly;
    int lastFired = 0, reloadTime;

    public AbstractWeapon() {
        this(WingmanWorld.getInstance());
    }

    public AbstractWeapon(Observer world) {
        super();
        this.addObserver(world);
    }

    public void fireWeapon(Ship theShip) {
        if (theShip instanceof PlayerShip) {
            direction = 1;
        } else {
            direction = -1;
        }
    }

    /* read is called by Observers when they are notified of a change */
    public void read(Object theObject) {
        GameWorld world = (GameWorld) theObject;
        world.addBullet(bullets);
        
        //ArrayList<Bullet> bullets=world.getBullet();
		//�㲥һ�Σ�֪ͨ�ͻ�������һ���ӵ�
        
        	SocketServer server=new SocketServer();
        	if(bullets.length==1) {
    			server.sendHander("^"+"+" + bullets[bullets.length-1].getOwner().getName()+"+"+ bullets[bullets.length-1].getLocationPoint().x + "+" + bullets[bullets.length-1].getLocationPoint().y+ "+" + direction+"+"+bullets[bullets.length-1].BulletID+"+"+0+"+"+0+"+"+0);
        	}else if (bullets.length==2) {
        		/*for(int i=0;i<2;i++) {
        			server.sendHander("^"+"+" + bullets[bullets.length-1-i].getOwner().getName()+"+"+ bullets[bullets.length-1-i].getLocationPoint().x + "+" + bullets[bullets.length-1-i].getLocationPoint().y+ "+" + direction+"+"+bullets[bullets.length-1-i].BulletID+"+"+0+"+"+0+"+"+0);	
        			System.out.println("�����ӵ�"+bullets[bullets.length-1-i].BulletID+"��Ϣ���ͳɹ�");
        		}*/
        		for(int i=2;i>0;i--) {
        			server.sendHander("^"+"+" + bullets[bullets.length-i].getOwner().getName()+"+"+ bullets[bullets.length-i].getLocationPoint().x + "+" + bullets[bullets.length-i].getLocationPoint().y+ "+" + direction+"+"+bullets[bullets.length-i].BulletID+"+"+0+"+"+0+"+"+0);
        		}
        	}
		/*if(bullets.length!=0) {
			SocketServer server=new SocketServer();
			server.sendHander("^"+"+" + bullets[bullets.length-1].getOwner().getName()+"+"+ bullets[bullets.length-1].getLocationPoint().x + "+" + bullets[bullets.length-1].getLocationPoint().y+ "+" + direction+"+"+bullets[bullets.length-1].BulletID+"+"+0+"+"+0+"+"+0);
			System.out.println("�����ӵ���Ϣ���ͳɹ�");
		}*/
    }

    public void remove() {
        this.deleteObserver(WingmanWorld.getInstance());
    }
    
    public Bullet[] getBullets() {
		return bullets;
	}

	public void setBullets(Bullet[] bullets) {
		this.bullets = bullets;
	}
}
