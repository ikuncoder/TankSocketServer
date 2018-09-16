package wingman.modifiers.motions;

import wingman.WingmanWorld;
import wingman.game.PlayerShip;
import wingman.ui.GameMenu;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Observable;
import java.util.Observer;

public class MenuController extends MotionController implements KeyListener {
    KeyListener listener;
    Field field;
    Method action;
    int moveState;
    int[] keys;
    boolean player;

    public MenuController(GameMenu menu) {
        this(menu, new int[]{KeyEvent.VK_LEFT, KeyEvent.VK_UP, KeyEvent.VK_RIGHT, KeyEvent.VK_DOWN, KeyEvent.VK_SPACE});
        this.player = false;
    }

    public MenuController(PlayerShip player) {
        this(player, new int[]{KeyEvent.VK_LEFT, KeyEvent.VK_UP, KeyEvent.VK_RIGHT, KeyEvent.VK_DOWN, KeyEvent.VK_SPACE});
        moveState = 0;
        this.player = true;
    }

    public MenuController(Observer theObject, int[] keys) {
        super();
        this.addObserver(theObject);
        this.action = null;
        this.field = null;
        this.keys = keys;
        WingmanWorld world = WingmanWorld.getInstance();
        world.addKeyListener(this);
    }

    public void signalKeyPress(KeyEvent e) {

    }

    private void setMove(String direction) {
        try {
            action = GameMenu.class.getMethod(direction);
            this.setChanged();
        } catch (Exception e) {
        }
    }

    private void setFire() {
        try {
            action = GameMenu.class.getMethod("applySelection");
        } catch (NoSuchMethodException | SecurityException e1) {
        }
        notifyObservers();
    }

    public void read(Object theObject) {
        try {
            action.invoke(theObject);
            action = null;
        } catch (Exception e) {
        }
    }

    public void clearChanged() {
        super.clearChanged();
    }

    @Override
    public void update(Observable o, Object arg) {

    }

    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        // left
        if (code == keys[0]) {
            this.setMove("left");
        }
        // up
        else if (code == keys[1]) {
            this.setMove("up");
        }
        // right
        else if (code == keys[2]) {
            this.setMove("right");
        }
        // down
        else if (code == keys[3]) {
            this.setMove("down");
        }
        // fire
        else if (code == keys[4]) {
            this.setFire();
        }
        // also map to enter key!
        else if (code == KeyEvent.VK_ENTER) {
            this.setFire();
        }
        setChanged();
        this.notifyObservers();
    }

    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
}