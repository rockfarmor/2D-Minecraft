package Engine.Entities;

import Engine.World.World;
import Engine.javax.vecmath.Vector2d;

import java.awt.*;

public class Entity {



    private Vector2d position;
    private Vector2d velocity;
    //private Vector2d acceleration;

    private double dragFactor;
    private double deceleration;
    private double acceleration;




    private int dir;

    private World state;

    private Boolean remove;

    public Entity(double x, double y, World state){

        this.init(new Vector2d(x,y), state);
    }

    public Entity(Vector2d pos, World state){
        this.init(pos, state);

    }

    private void init(Vector2d pos, World state){
        this.position = new Vector2d(pos);
        this.velocity = new Vector2d();
        this.state = state;
        this.remove = false;


        //Moving x
        this.dragFactor = 1 / 5000.0;
        this.deceleration = 400;
        this.acceleration = 400;
        this.dir = 0;





    }


    public void update(double dt){

    }

    public void render(Graphics2D g2d){

    }


    public Vector2d getPosition() {
        return position;
    }

    public void setPosition(Vector2d position) {
        this.position = position;
    }

    public Vector2d getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2d velocity) {
        this.velocity = velocity;
    }

    /*public Vector2d getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Vector2d acceleration) {
        this.acceleration = acceleration;
    }
    */

    public World getState() {
        return state;
    }

    public void setRemove(Boolean remove) {
        this.remove = remove;
    }

    public Boolean getRemove() {
        return remove;
    }

    public double getDragFactor() {
        return dragFactor;
    }

    public void setDragFactor(double dragFactor) {
        this.dragFactor = dragFactor;
    }

    public double getDeceleration() {
        return deceleration;
    }

    public void setDeceleration(double deceleration) {
        this.deceleration = deceleration;
    }

    public double getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(double acceleration) {
        this.acceleration = acceleration;
    }

    public int getDir() {
        return dir;
    }

    public void setDir(int dir) {
        this.dir = dir;
    }


}
