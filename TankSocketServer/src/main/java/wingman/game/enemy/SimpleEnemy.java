package wingman.game.enemy;

import wingman.WingmanWorld;
import wingman.game.Ship;
import wingman.modifiers.motions.SimpleFiringMotion;
import wingman.modifiers.motions.SimpleMotion;
import wingman.modifiers.weapons.NullWeapon;
import wingman.modifiers.weapons.SimpleWeapon;

import java.awt.*;

/*A simple enemy that just moves vertically and possibly fires at fixed intervals*/
public class SimpleEnemy extends Ship {
    public SimpleEnemy(int location) {
        this(location, new Point(0, 3), 5, 0);
    }

    public SimpleEnemy(int location, Point speed, int strength, int fireInterval) {
        super(location, speed, strength, WingmanWorld.sprites.get("enemy1"));

        if (fireInterval == 0) {
            this.weapon = new NullWeapon();
            motion = new SimpleMotion();
        } else {
            this.weapon = new SimpleWeapon();
            motion = new SimpleFiringMotion(fireInterval);
        }
    }

}
