package Engine.Renderer;

import Engine.javax.vecmath.Vector2d;

public class Camera {


    private Vector2d origin;
    private Vector2d follow;
    private Vector2d scale;
    private Double rotation;

    public Camera(){
        origin = new Vector2d(0,0);
        follow = new Vector2d(0,0);
        scale = new Vector2d(1,1);
        rotation = 0d;
    }

    public Vector2d getOrigin() {
        return origin;
    }

    public void setOrigin(Vector2d origin) {
        this.origin = origin;
    }

    public void setOrigin(double x, double y) {
        this.origin.set(x, y);
    }

    public void setOrigin(double x, double y, double val) {

        this.origin.set(this.origin.getX() + (x - this.origin.getX()) * val, this.origin.getY() + (y - this.origin.getY()) * val);
    }


    public Vector2d getFollow() {
        return follow;
    }

    public void setFollow(Vector2d follow) {
        this.follow.set(follow);
    }

    public void setFollow(Vector2d follow, double val) {
        //this.follow.set(follow);
        this.follow.set(this.follow.getX() + (follow.getX() - this.follow.getX()) * val, this.follow.getY() + (follow.getY() - this.follow.getY()) * val);
    }

    public void setFollow(Vector2d follow, double valX, double valY) {
        //this.follow.set(follow);
        this.follow.set(this.follow.getX() + (follow.getX() - this.follow.getX()) * valX, this.follow.getY() + (follow.getY() - this.follow.getY()) * valY);
    }

    public void setFollow(double x, double y) {
        this.follow.set(x, y);
    }


    public Vector2d getScale() {
        return scale;
    }

    public void setScale(Vector2d scale) {
        this.scale = scale;
    }

    public void setScale(Double scale){
        this.scale.set(scale, scale);
    }

    public Double getRotation() {
        return rotation;
    }

    public void setRotation(Double rotation) {
        this.rotation = rotation;
    }




}
