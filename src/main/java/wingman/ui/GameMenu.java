package wingman.ui;

import wingman.WingmanWorld;
import wingman.game.PlayerShip;
import wingman.modifiers.AbstractGameModifier;
import wingman.modifiers.motions.MenuController;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Observable;

public class GameMenu extends InterfaceObject {
    int selection;
    MenuController controller;
    boolean waiting;

    public GameMenu() {
        selection = 0;
        controller = new MenuController(this);
        waiting = true;
    }

    public void draw(Graphics g2, int x, int y) {
        g2.setFont(new Font("Calibri", Font.PLAIN, 24));
        if (selection == 0)
            g2.setColor(Color.RED);
        else
            g2.setColor(Color.WHITE);
        g2.drawString("1 Player", 200, 150);
        if (selection == 1)
            g2.setColor(Color.RED);
        else
            g2.setColor(Color.WHITE);
        g2.drawString("2 Player", 200, 250);
        if (selection == 2)
            g2.setColor(Color.RED);
        else
            g2.setColor(Color.WHITE);
        g2.drawString("Quit", 200, 350);
    }

    public void down() {
        if (selection < 2)
            selection++;
    }

    public void up() {
        if (selection > 0)
            selection--;
    }

    public void applySelection() {
        WingmanWorld world = WingmanWorld.getInstance();
        Dimension size = world.getSize();
        if (selection == 0) {
            int[] controls = {KeyEvent.VK_LEFT, KeyEvent.VK_UP, KeyEvent.VK_RIGHT, KeyEvent.VK_DOWN, KeyEvent.VK_SPACE};
            PlayerShip player = new PlayerShip(new Point(240, size.height - 50), new Point(6, 6), WingmanWorld.sprites.get("player1"), controls, "Player 1");
            world.addPlayer(player);
        } else if (selection == 1) {
            int[] controls = {KeyEvent.VK_A, KeyEvent.VK_W, KeyEvent.VK_D, KeyEvent.VK_S, KeyEvent.VK_SPACE};
            PlayerShip[] players = new PlayerShip[2];
            players[0] = new PlayerShip(new Point(160, size.height - 50), new Point(6, 6), WingmanWorld.sprites.get("player1"), controls, "Player 1");
            controls = new int[]{KeyEvent.VK_LEFT, KeyEvent.VK_UP, KeyEvent.VK_RIGHT, KeyEvent.VK_DOWN, KeyEvent.VK_ENTER};
            players[1] = new PlayerShip(new Point(400, size.height - 50), new Point(6, 6), WingmanWorld.sprites.get("player2"), controls, "Player 2");
            world.addPlayer(players);
        } else {
            System.exit(0);
        }

        WingmanWorld.sound.play("Resources/strobe.mp3");

        controller.deleteObservers();
        world.removeKeyListener(controller);
        world.level.load();
        waiting = false;
    }

    public void update(Observable o, Object arg) {
        AbstractGameModifier modifier = (AbstractGameModifier) o;
        modifier.read(this);
    }

    public boolean isWaiting() {
        return this.waiting;
    }
}
