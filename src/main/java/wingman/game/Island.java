/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wingman.game;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.util.Random;

public class Island extends BackgroundObject {
    Random gen;

    public Island(Point location, Point speed, Image img, Random gen) {
        super(location, speed, img);
        this.gen = gen;
    }

    public void update(int w, int h) {
        location.translate(speed.x, speed.y);
        ;
        if (location.y >= h) {
            location.y = -100;
            location.x = Math.abs(gen.nextInt() % (w - 30));
        }
    }

    public void draw(Graphics g, ImageObserver obs) {
        g.drawImage(img, location.x, location.y, obs);
    }
}