package Engine.Model;

import Engine.javax.vecmath.Vector2d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Keyframe {

    //private ArrayList<Bone> bones;
    private HashMap<String, Bone> bones;
    private HashMap<String, TransformationObject> transformObjects;
    private String id;
    //private ArrayList<TransformationObject> transformObjects;

    public Keyframe(String id) {

        this.bones = new HashMap<>();
        this.transformObjects = new HashMap<>();
        this.id = id;
        //this.transformObjects = new ArrayList<>();

    }

    public void addBoneAndTransformObject(Bone b, TransformationObject tObject){
        this.bones.put(b.getId(), b);
        this.transformObjects.put(b.getId(), tObject);
        //this.bones.add(b);
        //this.transformObjects.add(tObject);
    }


    public void applyTransform() {

        Iterator it = this.bones.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();

            Bone b = this.bones.get(pair.getKey());
            TransformationObject t = this.transformObjects.get(pair.getKey());
            b.setRotation(t.getRotation());
            b.setOffset(t.getOffset());


        }

    }


    public Keyframe interpolatedKeyframe(double val, Keyframe other){
        Keyframe interpolatedFrame = new Keyframe("interpolated");

        Iterator it = this.bones.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            Bone b = this.getBone(pair.getKey());
            Bone thatBone = other.getBone(pair.getKey());

            if (b != null && thatBone != null){

                if (b.getId() == thatBone.getId()){

                    TransformationObject thatTransform = other.getTransformationObject(pair.getKey());
                    TransformationObject thisTransform = this.getTransformationObject(pair.getKey());
                    double newRotation = 0;

                    double range = thisTransform.getRotation() - thatTransform.getRotation();
                    range *= val;
                    range = Math.abs(range);
                    if (thisTransform.getRotation() < thatTransform.getRotation()){

                        newRotation = thisTransform.getRotation() + range;
                    } else {
                        newRotation = thisTransform.getRotation() - range;


                    }
                    Vector2d newOffset = new Vector2d(thisTransform.getOffset());
                    Vector2d dirVector = new Vector2d(thatTransform.getOffset());
                    if (thatTransform.getOffset().getX() != thisTransform.getOffset().getX() || thatTransform.getOffset().getY() != thisTransform.getOffset().getY()) {
                        dirVector.sub(thisTransform.getOffset());
                        double len = dirVector.length() * val;
                        dirVector.normalize();
                        dirVector.scale(len);
                        newOffset = new Vector2d(thisTransform.getOffset());
                        newOffset.add(dirVector);
                    }



                    TransformationObject newTransform = new TransformationObject(newOffset, newRotation);
                    interpolatedFrame.addBoneAndTransformObject(b, newTransform);


                }


            }

        }



        return interpolatedFrame;
    }


    public Bone getBone(Object key){
        return this.bones.get(key);
    }

    public TransformationObject getTransformationObject(Object key){
        return this.transformObjects.get(key);
    }

    public String getId() {
        return id;
    }
}
