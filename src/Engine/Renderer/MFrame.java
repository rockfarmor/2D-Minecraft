package Engine.Renderer;

import Engine.Blocks.Block;
import Engine.Constants;
import Engine.World.World;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MFrame extends JFrame implements KeyListener {


    public boolean keyW = false;
    public boolean keyA = false;
    public boolean keyS = false;
    public boolean keyD = false;
    public boolean keyE = false;
    public boolean keyEWasDown = false;
    public boolean keySpace = false;
    public boolean keyShift = false;

    public double val = 1;

    public JSlider slider;
    public JSlider slider2;

    private World state;



    public MFrame(String title, World state){
        super(title);
        this.pack();
        this.setVisible(true);
        this.setSize(800,600);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.state = state;
        addKeyListener(this);

    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        switch (e.getKeyCode()){
            case 65:
                keyA = true;
                break;
            case 87:
                keyW = true;
                break;
            case 68:
                keyD = true;
                break;
            case 83:
                keyS = true;
                break;
            case 69:
                keyE = true;
                break;
            case 37:
                //left
                state.setDayLight(state.getDayLight() - 1);
                break;
            case 16:
                keyShift = true;
                break;
            case 39:
                //right
                state.setDayLight(state.getDayLight() + 1);
                break;
            case 38:
                //up
                int id = state.getHandBlock() + 1;
                if(id > Constants.blockHandler.maxId)
                    id = 0;
                state.setHandBlock(id);
                break;
            case 40:
                //down
                id = state.getHandBlock() - 1;
                if(id < 0)
                    id = Constants.blockHandler.maxId;
                state.setHandBlock(id);
                break;

            case 49:
                //1
                //state.setHandBlock(Constants.blockHandler.getBlockId("Oak Wood Plank"));
                state.getPlayer().getInventory().setHandItem(0);
                break;
            case 50:
                //2
                state.getPlayer().getInventory().setHandItem(1);
                break;
            case 51:
                //3
                state.getPlayer().getInventory().setHandItem(2);
                break;
            case 52:
                //4
                state.getPlayer().getInventory().setHandItem(3);
                break;
            case 53:
                //5
                state.getPlayer().getInventory().setHandItem(4);
                break;
            case 54:
                //6
                state.getPlayer().getInventory().setHandItem(5);
                break;
            case 55:
                //7
                state.getPlayer().getInventory().setHandItem(6);
                break;
            case 56:
                //8
                state.getPlayer().getInventory().setHandItem(7);
                break;
            case 57:
                //9
                state.getPlayer().getInventory().setHandItem(8);
                break;
            case 79:
                state.setLayer(0);
                //6
                break;
            case 80:
                state.setLayer(1);
                //6
                break;
            case 32:
                this.keySpace = true;
                break;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()){
            case 65:
                keyA = false;
                break;
            case 87:
                keyW = false;
                break;
            case 68:
                keyD = false;
                break;
            case 83:
                keyS = false;
                break;
            case 69:
                keyE = false;
                break;
            case 32:
                this.keySpace = false;
                break;
            case 16:
                keyShift = false;
                break;
        }
    }


    public double getVal() {
        return val;
    }

    public void setVal(double val) {
        System.out.println(val);
        this.val = val;
    }
}
