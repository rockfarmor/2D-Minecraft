package Engine;

import Engine.World.World;
import Engine.Math.MathUtils;
import Engine.Math.RandomXS128;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class Main {


    public static void main(String[] args) {

        int seed = 613223;


        MathUtils.random = new RandomXS128();
        World world = new World();



        double dt = 1/60.0;
        long accumulatedTime = 0;
        long lastTime = 0;

        final Action doOneStep = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                world.updateAccumulator();
                //world.update(dt);
            }
        };

        final Timer clockTimer = new Timer(0, doOneStep);
        clockTimer.setCoalesce(true);
        clockTimer.start();


    }







}
