package Engine.Particles;

import Engine.Blocks.Block;
import Engine.Blocks.BlockInfo;
import Engine.Constants;
import Engine.World.World;
import Engine.javax.vecmath.Vector2d;

import java.awt.*;
import java.util.ArrayList;

import Engine.Particles.Particle;

public class ParticleEmitter {


    private ArrayList<Particle> particles;

    private Vector2d position;

    private boolean gravity;

    private Color color;

    private ParticleType type;

    private ParticleInfo pInfo;

    private boolean randomX;
    private boolean randomY;

    private boolean collision;



    public ParticleEmitter(int x, int y, ParticleType type){

        this.particles = new ArrayList<>();
        this.position = new Vector2d(x,y);
        this.type = type;
        this.gravity = true;

        this.color = new Color(0,0,0);

        this.collision = false;

        this.randomX = false;
        this.randomY = false;

        this.pInfo = null;

        switch (this.type){
            case BlockParticle:
                this.color = Color.LIGHT_GRAY;
                this.gravity = true;
                break;
            case Walking:
                this.color = Color.LIGHT_GRAY;
                this.gravity = true;
                break;
            case TorchParticle:
                this.color = new Color(255, 186, 26);
                this.gravity = false;
                this.addParticles(3, -5, 5, -20, -5, 2);
                this.collision = true;
                this.randomX = true;
                break;
            case Explosion:
                this.pInfo = Constants.particleHandler.getParticleInfo("Explosion");
                this.gravity = false;
                int speed = 70;
                this.addParticles(40, -speed, speed, -speed, speed/2, 0.7);
                this.randomX = true;
                break;
            case Explosion2:
                this.pInfo = Constants.particleHandler.getParticleInfo("Explosion2");
                this.gravity = false;
                speed = 0;
                this.addParticles(1, -speed, speed, -speed, speed, 1.3);
                break;
            case Leaf:
                this.color = new Color(3, 255, 22);
                this.gravity = true;

                this.addParticles(1,-10,10,0,0, 3);

                break;

        }





    }


    public void update(double dt, World world){

        double gravMod = 0.3;
        if(this.type == ParticleType.Leaf) gravMod = 0.01;

        for (int i = particles.size()-1; i >= 0; i--) {

            Particle p = this.particles.get(i);

            Vector2d vec = new Vector2d();
            if(this.gravity) vec.set(0, world.getGravity().getY() * gravMod);


            if(randomX){
                if(Constants.randomNumber(0,100) < 70){

                    int rX = Constants.randomNumber(-20,20);
                    vec.setX(vec.x + rX);
                }
            }


            p.update(dt, vec, world, this.collision);

            if(p.isRemoveThis()){
                this.particles.remove(i);
            }

        }



    }


    public void render(Graphics2D g2d){
        for (int i = 0; i < particles.size(); i++) {

            Particle p = particles.get(i);

            double alpha = 255;

            if(this.type == ParticleType.TorchParticle){
                alpha = 1 - (p.getTick() / p.getMaxTick());
                alpha *= 255;
            }






            if(this.pInfo == null){
                Color c = new Color(this.color.getRed(), this.color.getGreen(), this.color.getBlue(), (int)alpha);
                if(this.type == ParticleType.TorchParticle){
                    if(Constants.randomNumber(0,100) < 10){
                        c = new Color(255, 0, 18);
                    }
                }
                p.render(g2d, c);
            } else {

                double id = (1 - p.getTick() / p.getMaxTick());
                id = id * (this.pInfo.size() - 1);

                if(this.type == ParticleType.Explosion)
                    p.render(g2d, this.pInfo.getImage((int)id), true, true);
                else if(this.type == ParticleType.Explosion2)
                    p.render(g2d, this.pInfo.getImage((int)id), false, true);
                else
                    p.render(g2d, this.pInfo.getImage((int)id), false, false);
            }

        }
    }

    public int numParticles(){
        return this.particles.size();
    }

    public void addParticles(int num, int lowXVel, int highXVel, int lowYVel, int highYVel){
        addParticles(num, lowXVel, highXVel, lowYVel, highYVel, 1);
    }

    public void addParticles(int num, int lowXVel, int highXVel, int lowYVel, int highYVel, double tick){
        for (int i = 0; i < num; i++) {

            double xVel = Constants.randomNumber(lowXVel, highXVel);
            double yVel = Constants.randomNumber(lowYVel, highYVel);
            particles.add(new Particle(this.position.x, this.position.y, xVel, yVel, tick));
        }
    }

    public void setColorByBlock(int blockId){
        BlockInfo bInfo = Constants.blockHandler.getBlockInfo(blockId);
        int c = bInfo.img.getRGB(8,8);
        this.color = new Color(c);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isGravity() {
        return gravity;
    }

    public void setGravity(boolean gravity) {
        this.gravity = gravity;
    }
}
