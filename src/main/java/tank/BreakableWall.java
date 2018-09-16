package tank;

import wingman.game.BackgroundObject;
import wingman.game.GameObject;

import java.awt.*;
import java.awt.image.ImageObserver;

import lskServer.SocketServer;

public class BreakableWall extends BackgroundObject {
    int timer = 400;
    public SocketServer socketServer=new SocketServer();

    public BreakableWall(int x, int y) {
        super(new Point(x * 32, y * 32), new Point(0, 0), TankWorld.sprites.get("wall2"));
    }

    //You need to fill in here
    public boolean collision(GameObject otherObject) {//������ӵ��Ļ���Ҫ������ʧ����
        if (location.intersects(otherObject.getLocation())) {
            if (otherObject instanceof TankBullet||otherObject instanceof AiTankBullet) {
            	
            	//BreakableWall��ʧָ�����λ����Ϣ�Ϳ�����
            	socketServer.sendHander("RemoveBreakableWall"+"+" + 0 +"+"+ this.getLocationPoint().x + "+" +this.getLocationPoint().y + "+" + 0+"+"+0+"+"+0+"+"+0+"+"+0);
            	this.show = false;
            }
                
            return true;
        }
        return false;
    }

    //You need to fill in here
    public void draw(Graphics g, ImageObserver obs) {
        if (show)
            super.draw(g, obs);
        else {
            this.timer--;
            if (this.timer < 0) {
                this.timer = 400;
                this.show = true;
                
              //BreakableWall����ָ�����λ����Ϣ�Ϳ�����
              socketServer.sendHander("BreakableWall"+"+" + 0 +"+"+ this.getLocationPoint().x + "+" +this.getLocationPoint().y + "+" + 0+"+"+0+"+"+0+"+"+0+"+"+0);
            }
        }
    }
}