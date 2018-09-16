package tank;

import wingman.game.BackgroundObject;

import java.awt.*;

public class Wall extends BackgroundObject {
    public Wall(int x, int y) {
        super(new Point(x * 32, y * 32), new Point(0, 0), TankWorld.sprites.get("wall"));
    }

    public void damage(int damage) {
        return;
    }
}
