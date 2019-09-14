package Engine.Model;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AnimationHandler {


    private HashMap<String, Keyframe> keyframes;
    private int currKeyframe;

    private HashMap<String, Animation> animations;

    private String prevAnimation;

    public AnimationHandler(){
        keyframes = new HashMap<>();
        this.animations = new HashMap<>();
        this.currKeyframe = 0;
        this.prevAnimation = "";
    }

    public void addAnimation(Animation a){
        this.animations.put(a.getAnimationName(), a);
    }

    public void update(double dt){
        Iterator it = this.animations.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            Animation a = this.animations.get(pair.getKey());
            if (a.isActive() && a.getAnimationName() != "Mining"){
                a.update(dt);
            }
        }
        Animation mining = this.animations.get("Mining");
        if (mining != null){
            if (mining.isActive()){
                mining.update(dt);
            }
        }
    }
    public void toggleAnimation(String animationName, boolean active){
        toggleAnimation(animationName, active, false);

    }


    public void toggleAnimation(String animationName, boolean active, boolean isPrev){
        if (this.animations.get(animationName) != null){

            Keyframe prev = null;
            if(this.animations.get(prevAnimation) != null && !prevAnimation.equals(animationName) && isPrev){
                prev = this.animations.get(prevAnimation).getPrevFrame();
            }


            this.animations.get(animationName).setActive(active, prev);
            this.prevAnimation = animationName;
        }
    }



    public void addKeyFrame(Keyframe k){
        this.keyframes.put(k.getId(), k);
    }

    public Animation getAnimation(String animationName){
        return this.animations.get(animationName);
    }




    public void setModelState(String keyframeId){

        if (this.keyframes.get(keyframeId) != null){
            this.keyframes.get(keyframeId).applyTransform();
            //  this.currKeyframe = keyframeId;
        }
    }



    public int getCurrKeyframe() {
        return currKeyframe;
    }

    public void setCurrKeyframe(int currKeyframe) {
        this.currKeyframe = currKeyframe;
    }
}
