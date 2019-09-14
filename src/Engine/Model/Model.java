package Engine.Model;


import Engine.Blocks.Block;
import Engine.Constants;
import Engine.World.World;
import Engine.javax.vecmath.Vector2d;
import com.sun.javafx.iio.ImageStorage;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Model {

    private Bone parentBone;

    private AnimationHandler animationHandler;

    private BufferedImage img;
    private BufferedImage clear;

    private int scaleX;
    private int scaleY;

    private World world;

    public Model(Bone parentBone, AnimationHandler animationHandler, World world) {
        this.parentBone = parentBone;
        this.animationHandler = animationHandler;
        this.scaleX = 1;
        this.scaleY = 1;
        this.img = new BufferedImage(400,400, BufferedImage.TYPE_INT_ARGB);
        this.clear = new BufferedImage(400,400, BufferedImage.TYPE_INT_ARGB);
        this.world = world;
    }




    public void render(Graphics2D g2d, Vector2d pos, Color c){
        parentBone.render(g2d, pos, this.scaleX, this.scaleY, c);
    }

    public AnimationHandler getAnimationHandler(){
        return this.animationHandler;
    }

    public void toggleAnimation(String animationName, boolean active){

        this.getAnimationHandler().toggleAnimation(animationName, active);

    }



    public int getScaleX() {
        return scaleX;
    }

    public void setScaleX(int scaleX) {

      this.scaleX = scaleX;

    }
}
