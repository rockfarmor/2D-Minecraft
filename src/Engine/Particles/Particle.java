package Engine.Particles;

import Engine.Blocks.Block;
import Engine.Constants;
import Engine.World.World;
import Engine.javax.vecmath.Vector2d;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Particle {

    private Vector2d position;
    private Vector2d velocity;
    private boolean removeThis;
    private double tick;
    private double maxTick;


    public Particle(double x, double y, double velx, double vely, double tick) {
        this.position = new Vector2d(x, y);
        this.velocity = new Vector2d(velx, vely);
        this.removeThis = false;
        this.tick = tick;
        this.maxTick = tick;
    }

    public void update(double dt, Vector2d acceleration, World world, boolean collision){
        if(!this.removeThis) {

            this.getVelocity().setX(this.getVelocity().getX() + acceleration.getX() * dt);
            this.getVelocity().setY(this.getVelocity().getY() + acceleration.getY() * dt);
            this.getPosition().set(this.getPosition().getX() + this.getVelocity().getX() * dt, this.getPosition().getY() + this.getVelocity().getY() * dt);

            //this.position.set(this.position.x + this.velocity.x * dt, this.position.y + this.velocity.y * dt);
            //this.position.set(this.position.x + acceleration.x * dt, this.position.y + acceleration.y * dt);

            if(collision){
                Vector2d posVec = Constants.PosToTilePos(this.getPosition());
                Block b = world.getBlock((int)posVec.getX(), (int)posVec.getY());
                if (!b.transparant()){
                   this.removeThis = true;
                }
            }





            this.tick -= dt;

            if (this.tick < 0) {
                this.removeThis = true;
            }
        }

    }

    public void render(Graphics2D g2d, BufferedImage img, boolean scales, boolean alphas){
        if(!this.removeThis){
            double alpha =(this.tick / this.maxTick);
            //alpha *= 255;
            Composite pre = g2d.getComposite();
            if(alphas)
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)alpha));

            double scale = 1;

            if(scales){
                scale =  1 - alpha;
                scale = 1 + scale * 4;
            }


            g2d.drawImage(img, (int) this.position.x - img.getWidth()/2, (int) this.position.y - img.getHeight()/2, (int)(scale * img.getWidth()), (int)(scale * img.getHeight()), null);
            g2d.setComposite(pre);



        }
    }

    public void render(Graphics2D g2d, Color c){
        if(!this.removeThis) {

            g2d.setColor(c);

            g2d.fillOval((int) this.position.x, (int) this.position.y, 1, 1);


        }
    }

    public Vector2d getPosition() {
        return position;
    }

    public Vector2d getVelocity() {
        return velocity;
    }

    public boolean isRemoveThis() {
        return removeThis;
    }

    public double getTick() {
        return tick;
    }

    public double getMaxTick() {
        return maxTick;
    }
}
