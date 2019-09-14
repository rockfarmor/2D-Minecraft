package Engine.Model;

import Engine.javax.vecmath.Vector2d;

public class TransformationObject {

    private Vector2d offset;
    private double rotation;


    public TransformationObject(Vector2d offset, double rotation){
        this.offset = new Vector2d(offset);
        this.rotation = rotation;
    }

    public Vector2d getOffset() {
        return offset;
    }

    public double getRotation() {
        return rotation;
    }
}
