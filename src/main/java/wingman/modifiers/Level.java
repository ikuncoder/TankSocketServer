package wingman.modifiers;

import wingman.WingmanWorld;
import wingman.game.Ship;
import wingman.game.enemy.*;
import wingman.modifiers.weapons.SpreadWeapon;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

/*This is where enemies are introduced into the game according to a timeline*/
public class Level extends AbstractGameModifier implements Observer {
    int start, lastSpawn, lastPowerUp;
    Integer position;
    Random generator = new Random();
    Ship[] enemyBuffer;
    LinkedHashMap<Integer, Ship[]> timeline;
    int w, h;
    int endgameDelay = 100;    // don't immediately end on game end

    /*Constructor sets up arrays of enemies in a LinkedHashMap*/
    public Level(int w, int h) {
        super();
        this.w = w;
        this.h = h;
        timeline = new LinkedHashMap<Integer, Ship[]>();
    }

    public void load() {
        start = WingmanWorld.getInstance().getTime();
        lastSpawn = WingmanWorld.getInstance().getTime();
        lastPowerUp = WingmanWorld.getInstance().getTime();
        position = new Integer(0);

        int playerMultiplier = WingmanWorld.getInstance().countPlayers();
        int enemyStrength = 5 * playerMultiplier;

        Ship[] wave = new Ship[]{
                new SimpleEnemy(100),
                new SimpleEnemy(200),
                new SimpleEnemy(300),
                new SimpleEnemy(400),
                new SimpleEnemy(500),
                new SimpleEnemy(600),
                new SimpleEnemy(700),
                new SimpleEnemy(-100, new Point(3, 5), enemyStrength, 30),
                new SimpleEnemy(h + 100, new Point(-3, 7), enemyStrength, 50),
                new SimpleEnemy(-200, new Point(-2, 9), enemyStrength, 40),
        };
        timeline.put(new Integer(0), wave);

        wave = new Ship[]{
                new SimpleEnemy(-10, new Point(2, 3), enemyStrength, 30),
                new SimpleEnemy(-120, new Point(3, 6), enemyStrength, 40),
                new SimpleEnemy(w + 40, new Point(-3, 7), enemyStrength, 50),
                new SimpleEnemy(w + 150, new Point(-5, 8), enemyStrength, 50),
                new SimpleEnemy(100, new Point(3, 9), enemyStrength, 40),
                new SimpleEnemy(300, new Point(0, 7), enemyStrength, 50),
                new SimpleEnemy(500, new Point(-1, 5), enemyStrength, 30),
                new SimpleEnemy(700, new Point(-4, 8), enemyStrength, 40),
        };
        timeline.put(new Integer(1), wave);

        wave = new Ship[]{
                new Bomber(200, 8, 50),
                new PulseEnemy(-10, new Point(6, 1), 20, 60),
                new Bomber(400, 4, 30),
                new Bomber(600, 6, 40)
        };
        timeline.put(new Integer(2), wave);

        for (int i = 3; i < 9; i += 3) {
            wave = new Ship[]{
                    new SimpleEnemy(100, new Point(0, 5), enemyStrength, 30),
                    new PulseEnemy(800, new Point(-2, 3), 30, 50),
                    new SimpleEnemy(300, new Point(1, 6), enemyStrength, 25),
                    new HoverEnemy(300),
                    new SimpleEnemy(600, new Point(-1, 4), enemyStrength, 20),
                    new PowerupEnemy(400, new Point(0, 3), new SpreadWeapon()),
                    new SimpleEnemy(500, new Point(0, 5), enemyStrength, 20)
            };

            timeline.put(new Integer(i), wave);

            wave = new Ship[]{
                    new SimpleEnemy(-100, new Point(0, 5), enemyStrength, 30),
                    new SimpleEnemy(0, new Point(1, 6), enemyStrength, 25),
                    new SimpleEnemy(100, new Point(0, 5), enemyStrength, 30),
                    new SimpleEnemy(300, new Point(-1, 10), enemyStrength, 35),
                    new SimpleEnemy(400, new Point(3, 8), enemyStrength, 25),
                    new HoverEnemy(100 + 100 * (i % 6), new Point(1 * (i % 2), 1 + i % 5)),
                    new SimpleEnemy(800, new Point(-2, 5), enemyStrength, 25),
                    new SimpleEnemy(900, new Point(-6, 3), enemyStrength, 30),
                    new SimpleEnemy(1000, new Point(-6, 4), enemyStrength, 25),
            };
            timeline.put(new Integer(i + 1), wave);

            wave = new Ship[]{
                    new Bomber(200, 8, 50),
                    new PulseEnemy(-10, new Point(4, 2), 20, 60),
                    new Bomber(400, 4, 30),
                    new Bomber(600, 6, 40),
                    new HoverEnemy(100 + 100 * (i % 6), new Point(-1 * (i % 2), 1 + i % 6))
            };
            timeline.put(new Integer(i + 2), wave);

        }

        wave = new Ship[]{
                new Boss(new Point(w / 2, -100), new Point(3, 3), enemyStrength * 80)
        };

        timeline.put(new Integer(timeline.size() + 1), wave);
    }

    public void read(Object theObject) {
    }

    /*Level observes GameClock and updates on every tick*/
    @Override
    public void update(Observable o, Object arg) {
        WingmanWorld world = WingmanWorld.getInstance();
        if (world.countEnemies() < 3) {
            Ship[] enemies = timeline.remove(position);
            if (enemies != null) {
                world.addEnemies(enemies);
                setChanged();
                notifyObservers();
            }
            position += 1;
        }

        if (world.getTime() - lastSpawn > 7000) {
            Ship[] wave = {
                    new SimpleEnemy(100, new Point(1, 4), 10, 45),
                    new SimpleEnemy(200, new Point(1, 5), 10, 30),
                    new SimpleEnemy(300, new Point(1, 4), 15, 60),
                    new SimpleEnemy(500, new Point(-2, 5), 10, 40),
                    new SimpleEnemy(600, new Point(2, 6), 10, 30),
                    new SimpleEnemy(700, new Point(-2, 5), 10, 70),
                    new PulseEnemy(500)
            };
            world.addEnemies(wave);
            lastSpawn = world.getTime();
        }

        if (world.getTime() - lastPowerUp > 30000) {
            world.addRandomPowerUp();
            lastPowerUp = world.getTime();
        }


        if (world.isGameOver()) {
            if (endgameDelay <= 0) {
                WingmanWorld.getInstance().removeClockObserver(this);
                WingmanWorld.getInstance().finishGame();
            } else endgameDelay--;
        }
    }
}
