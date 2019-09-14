package Engine.Renderer;

import Engine.World.World;
import Engine.javax.vecmath.Vector2d;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class MComponent extends JComponent implements MouseListener{



    private World gameState;

    private BufferedImage terrain;

    public boolean mouseLeftDown = false;
    public boolean mouseLeftWasDown = false;
    public boolean mouseLeftClicked = false;

    public boolean mouseRightDown = false;
    public boolean mouseRightWasDown = false;
    public boolean mouseRightClicked = false;


    public int mouseX = 0;
    public int mouseY = 0;

    private long now;
    private int framesCount = 0;
    private int framesCountAvg = 1;
    private long framesTimer = 0;

    public MComponent(World gs){

        this.gameState = gs;
        addMouseListener(this);

    }



    @Override protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2d = (Graphics2D) g;

        long beforeTime = System.nanoTime();

        gameState.render(g2d);

        mouseLeftClicked = !mouseLeftWasDown && mouseLeftDown;
        mouseRightClicked = !mouseRightWasDown && mouseRightDown;

        mouseRightWasDown = mouseRightDown;
        mouseLeftWasDown = mouseLeftDown;

        Point p = this.getMousePosition();
        if (p != null){
            mouseX = (int)p.getX();
            mouseY = (int)p.getY();
        }


        this.now = System.currentTimeMillis();
        framesCount++;
        if(this.now - this.framesTimer > 1000)
        {
            this.framesTimer = now;
            this.framesCountAvg = this.framesCount;
            this.framesCount = 0;
        }

    }

    public Vector2d screenToWorld(int x, int y, Camera camera){

        Vector2d res = new Vector2d();

        double _x = (x - camera.getOrigin().getX()) / camera.getScale().getX() + camera.getFollow().getX();
        double _y = (y - camera.getOrigin().getY()) / camera.getScale().getY() + camera.getFollow().getY();

        res.setX(_x);
        res.setY(_y);

        return res;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {


        if (e.getButton() == 1){
            //left
            mouseLeftDown = true;
        } else if (e.getButton() == 3){
            //Right
            mouseRightDown = true;
        }




    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //mouseDown = false;
        if (e.getButton() == 1){
            //left
            mouseLeftDown = false;
        } else if (e.getButton() == 3){
            //Right
            mouseRightDown = false;
        }

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public int getFps() {
        return this.framesCountAvg;
    }
}
