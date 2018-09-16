package wingman.game.enemy;

import wingman.WingmanWorld;
import wingman.game.Ship;
import wingman.modifiers.motions.SimpleFiringMotion;
import wingman.modifiers.weapons.PulseWeapon;

import java.awt.*;

public class PulseEnemy extends Ship {
    public PulseEnemy(int location) {
        this(location, new Point(0, 3), 10, 30);

        motion = new SimpleFiringMotion(30);
    }

    public PulseEnemy(int location, Point speed, int strength, int fireInterval) {
        super(location, speed, strength, WingmanWorld.sprites.get("enemy3"));
        this.weapon = new PulseWeapon();

        motion = new SimpleFiringMotion(fireInterval);
    }
}
