package Engine.Collision;

import Engine.javax.vecmath.Vector2d;
import org.w3c.dom.css.Rect;

import java.awt.*;

public class BoxCollision {

    private int width;
    private int height;
    private int offsetX;
    private int offsetY;

    public BoxCollision(int width, int height, int offsetX, int offsetY) {
        this.width = width;
        this.height = height;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public boolean AABBCollisionVelocity(BoxCollision other, Vector2d thisPos, Vector2d thisVel,Vector2d thatPos){
        boolean colliding = false;

        int thisX = (int)thisPos.getX() + this.offsetX + (int)thisVel.getX();
        int thisY = (int)thisPos.getY() + this.offsetY + (int)thisVel.getY();
        int thatX = (int)thatPos.getX() + other.getOffsetX();
        int thatY = (int)thatPos.getY() + other.getOffsetY();

        int thatWidth = other.getWidth();
        int thatHeight = other.getHeight();

        colliding = thisX < thatX + thatWidth && thisX + this.width > thatX && thisY < thatY + thatHeight && thisY + this.height > thatY;

        return colliding;
    }

    public Vector2d getMinimumTranslationVector(BoxCollision other, Vector2d thisPos, Vector2d thisVel, Vector2d thatPos){

        Vector2d res = new Vector2d();
        double thisX = (int)thisPos.getX() + this.offsetX + thisVel.getX();
        double thisY = (int)thisPos.getY() + this.offsetY + thisVel.getY();
        int thatX = (int)thatPos.getX() + other.getOffsetX();
        int thatY = (int)thatPos.getY() + other.getOffsetY();

        int thatWidth = other.getWidth();
        int thatHeight = other.getHeight();

        int oneQuarters = this.height / 4;
        int oneQuarterWidth = this.width / 4;

        if (other.PointInBox(thisX + oneQuarterWidth, thisY, thatPos) || other.PointInBox(thisX + this.width - oneQuarterWidth, thisY, thatPos)){
            //top
            double yTranslation = thatY + thatHeight - thisY;
            res.setY(yTranslation);
        } else if (other.PointInBox(thisX + oneQuarterWidth, thisY + this.height, thatPos) || other.PointInBox(thisX + this.width - oneQuarterWidth, thisY + this.height, thatPos)){
            //bot
            double yTranslation = thatY  - (thisY + this.height);
            res.setY(yTranslation);
            System.out.println(yTranslation + ",->> ");
        } else if (other.PointInBox(thisX + this.width, thisY + oneQuarters, thatPos) || other.PointInBox(thisX + this.width, thisY + oneQuarters * 3, thatPos)){

            double xTranslation = thatX - (thisX + this.width);
            res.setX(xTranslation);

        }else if (other.PointInBox(thisX, thisY + oneQuarters, thatPos) || other.PointInBox(thisX, thisY + oneQuarters * 3, thatPos)){

            double xTranslation = thatX + thatWidth - (thisX);
            res.setX(xTranslation);


        }



       return res;
    }


    public double getTopY(){
        return this.offsetY;
    }

    public double getBotY(){
        return this.offsetY + this.height;
    }

    public double getLeftX(){
        return this.offsetX;
    }

    public double getRightX(){
        return this.offsetX + this.width;
    }


    public Rectangle leftRect(Vector2d pos){
        double xx = pos.getX() + getLeftX();
        double yy = pos.getY() + getTopY();

        double w = getWidth() / 4;
        double h = getHeight() / 4;

        Rectangle rec = new Rectangle((int)xx, (int)(yy + h), (int)w, (int)h * 2-1);
        return rec;
    }

    public Rectangle rightRect(Vector2d pos){
        double xx = pos.getX() + getRightX();
        double yy = pos.getY() + getTopY();

        double w = getWidth() / 4;
        double h = getHeight() / 4;

        Rectangle rec = new Rectangle((int)xx - (int)w, (int)(yy + h), (int)w, (int)h * 2-1);
        return rec;

    }

    public Rectangle topRect(Vector2d pos){
        double w = getWidth() / 4;
        double h = getHeight() / 4;
        double xx = pos.getX() + getLeftX();
        double yy = pos.getY() + getTopY();
        Rectangle rec = new Rectangle((int)xx + (int)w, (int)yy, (int)w * 2, (int)h*2);

        return rec;
    }

    public Rectangle botRect(Vector2d pos){
        double w = getWidth() / 4;
        double h = getHeight() / 4;
        double xx = pos.getX() + getLeftX();
        double yy = pos.getY() + getTopY();
        Rectangle rec = new Rectangle((int)xx + (int)w, (int)yy + (int)h*2, (int)w * 2, (int)h*2);
        return rec;
    }

    public Rectangle getRect(Vector2d pos){
        double xx = pos.getX() + getLeftX();
        double yy = pos.getY() + getTopY();

        double w = getWidth();
        double h = getHeight();

        return new Rectangle((int)xx, (int)yy, (int)w, (int)h);
    }




    public boolean PointInBox(double pointX, double pointY, Vector2d thisPos){
        int thisX = (int)thisPos.getX() + this.offsetX;
        int thisY = (int)thisPos.getY() + this.offsetY;

        return pointX >= thisX && pointX < thisX + this.width && pointY >= thisY && pointY < thisY + this.getWidth();

    }



    public boolean AABBCollision(BoxCollision other, Vector2d thisPos, Vector2d thatPos){
        return AABBCollisionVelocity(other, thisPos, new Vector2d(0,0), thatPos);
    }


    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }
}
