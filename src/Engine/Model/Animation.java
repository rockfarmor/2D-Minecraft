package Engine.Model;

public class Animation {


    private Keyframe[] keyframes;
    private int tick;

    private int maxTicks;

    private AnimationTypes animationType;
    private String animationName;

    private boolean active;

    private int currAnimation;

    private boolean up;

    private boolean betweenInterpolate;
    private int betweenInterpolateNum;

    private Keyframe prevFrame;

    private Keyframe prevFrame2;

    public Animation(String animationName, Keyframe[] keyframes, AnimationTypes animationType, int maxTicks) {

        this.tick = 0;
        this.animationName = animationName;
        this.keyframes = keyframes;
        this.animationType = animationType;
        this.maxTicks = maxTicks;
        this.active = false;
        this.currAnimation = 0;

        this.prevFrame = null;
        this.prevFrame2 = null;

        this.up = true;

        this.betweenInterpolate = false;
        this.betweenInterpolateNum = 0;


    }


    public void update(double dt){

        int numAnimations = keyframes.length;
        int realMaxTicks = (int)(maxTicks);

        if(this.betweenInterpolate){

            //k1.interpolatedKeyframe(val, k2).applyTransform();
            if(this.prevFrame2 != null){
                Keyframe nextFrame = keyframes[0];

                this.prevFrame2.interpolatedKeyframe(this.betweenInterpolateNum / 10.0, nextFrame);
                this.betweenInterpolateNum += 1;
                if(this.betweenInterpolateNum >= 10){
                    this.prevFrame2 = null;
                    this.betweenInterpolate = false;
                }
            } else {
                this.betweenInterpolate = false;
            }


        } else {
            if (numAnimations == 1) {
                keyframes[0].applyTransform();
            } else {

                int nextAnimation = this.currAnimation;
                if (this.animationType == AnimationTypes.Once) {
                    if (this.currAnimation + 1 <= keyframes.length - 1) {
                        nextAnimation = this.currAnimation + 1;
                    } else {
                        nextAnimation = keyframes.length - 1;
                    }

                } else if (this.animationType == AnimationTypes.Repeat) {
                    if (this.currAnimation + 1 <= keyframes.length - 1) {
                        nextAnimation = this.currAnimation + 1;
                    } else {
                        nextAnimation = 0;
                    }
                } else if (this.animationType == AnimationTypes.Cycle) {
                    if (this.up) {
                        if (this.currAnimation + 1 <= keyframes.length - 1) {
                            nextAnimation = this.currAnimation + 1;
                        } else {
                            nextAnimation = keyframes.length - 1;
                        }
                    } else {
                        if (this.currAnimation - 1 >= 0) {
                            nextAnimation = this.currAnimation - 1;
                        } else {
                            nextAnimation = 1;
                        }
                    }

                }

                double val = (double) tick / (double) realMaxTicks;

                Keyframe k1 = keyframes[this.currAnimation];
                Keyframe k2 = keyframes[nextAnimation];

                Keyframe interpolated = k1.interpolatedKeyframe(val, k2);
                interpolated.applyTransform();
                this.prevFrame = interpolated;
            }
            this.tick++;
            if (this.tick >= realMaxTicks) {
                this.tick = 0;
                if (this.animationType == AnimationTypes.Once) {
                    this.currAnimation++;
                    if (this.currAnimation >= numAnimations - 1) {
                        this.currAnimation = 0;
                        this.active = false;
                    }
                } else if (this.animationType == AnimationTypes.Repeat) {
                    this.currAnimation++;
                    if (this.currAnimation >= numAnimations) {
                        this.currAnimation = 0;
                    }
                } else if (this.animationType == AnimationTypes.Cycle) {
                    if (this.up) {
                        this.currAnimation++;
                        if (this.currAnimation >= numAnimations - 1) {
                            this.up = false;
                        }
                    } else {
                        this.currAnimation--;
                        if (this.currAnimation <= 0) {
                            this.up = true;
                        }
                    }
                }
            }
        }
    }


    public void interpolateState(double val, int id1, int id2){
        Keyframe k1 = this.keyframes[id1];
        Keyframe k2 = this.keyframes[id2];

        Keyframe interK = k1.interpolatedKeyframe(val, k2);
        interK.applyTransform();
    }



    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active, Keyframe frame){
        if (this.active == active){

        } else {
            this.active = active;
            this.tick = 0;
            //Maby interpolate here?

            if(frame == null){
                this.betweenInterpolate = false;
            } else {
                this.betweenInterpolate = true;
                this.betweenInterpolateNum = 0;
                this.prevFrame2 = frame;
            }

        }
    }

    public void setActive(boolean active) {
        setActive(active, null);
    }

    public String getAnimationName() {
        return animationName;
    }

    public Keyframe getPrevFrame() {
        return prevFrame;
    }
}
