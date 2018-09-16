package wingman.modifiers.weapons;

import wingman.game.Bullet;
import wingman.game.PlayerShip;
import wingman.game.Ship;
import wingman.modifiers.motions.SimpleMotion;

import java.awt.*;

public class SpreadWeapon extends AbstractWeapon {

    @Override
    public void fireWeapon(Ship theShip) {
        if (theShip instanceof PlayerShip) {
            direction = 1;
        } else {
            direction = -1;
        }
        Point location = theShip.getLocationPoint();
        Point offset = theShip.getGunLocation();
        location.x += offset.x;
        location.y += offset.y;
        Point vertical = new Point(0, -10 * direction);
        Point left = new Point(1, -9 * direction);
        Point right = new Point(-1, -9 * direction);

        Bullet bullet0 = new Bullet(location, left, 4, new SimpleMotion(), theShip);
        Bullet bullet1 = new Bullet(location, right, 4, new SimpleMotion(), theShip);
        Bullet bullet2 = new Bullet(location, vertical, 4, new SimpleMotion(), theShip);

        bullets = new Bullet[3];
        bullets[0] = bullet0;
        bullets[1] = bullet1;
        bullets[2] = bullet2;

        this.setChanged();
        this.notifyObservers();

    }

}
