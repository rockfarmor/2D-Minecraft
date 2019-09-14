package Engine.Model;

import Engine.Blocks.Block;
import Engine.FileHandler.FileInput;
import Engine.Inventory.Inventory;
import Engine.Inventory.InventoryItem;
import Engine.World.World;
import Engine.javax.vecmath.Vector2d;

import java.awt.image.BufferedImage;

public class ModelFactory {



    public static Model modelSteveCreator(String skin, World world, Inventory inventory){

        BufferedImage img = null;

        img = FileInput.LoadImage("Assets/Images/steve.png");


        BufferedImage diamond = FileInput.LoadImage("Assets/textures/models/armor/diamond_layer_1.png");

        BufferedImage bodyImg = img.getSubimage(20,20, 4, 12);

        BufferedImage dBody = diamond.getSubimage(20,20, 4, 12);
        //bodyImg.getGraphics().drawImage(dBody, 0, 0, null);


        BufferedImage legImg = img.getSubimage(8,20,4,12);
        //legImg.getGraphics().drawImage(diamond.getSubimage(8,20,4,12), 0, 0, null);

        BufferedImage legImg2 = img.getSubimage(4,20,4,12);
        //legImg2.getGraphics().drawImage(diamond.getSubimage(4,20,4,12), 0, 0, null);

        BufferedImage headImg = img.getSubimage(0,8,8,8);

        //headImg.getGraphics().drawImage(diamond.getSubimage(0,8,8,8), 0, 0, null);

        BufferedImage fArmImg = img.getSubimage(44,20,4,12);

        //fArmImg.getGraphics().drawImage(diamond.getSubimage(44,20,4,12), 0, 0, null);


        BufferedImage bArmImg = img.getSubimage(48,20,4,12);

        BufferedImage test = FileInput.LoadImage("Assets/textures/items/diamond_pickaxe.png");
        //BufferedImage test = world.terrainSheet.getSprite(Block.Tnt);

        Bone body = new ArmorBone(bodyImg, new Vector2d(-2,-12), new Vector2d(0,-3), 0, 0, "charBody", "body", inventory);
        Bone frontLeg = new ArmorBone(legImg2, new Vector2d(-2,-2), new Vector2d(0,0), 0, 1, "charFrontLeg", "leg", inventory);
        Bone backLeg = new ArmorBone(legImg, new Vector2d(-2,-2), new Vector2d(0,0), 0, -1, "charBackLeg", "leg", inventory);
        Bone head = new ArmorBone(headImg, new Vector2d(-4, -8), new Vector2d(0,-12), 0, 1, "charHead", "head", inventory);
        Bone frontArm = new ArmorBone(fArmImg, new Vector2d(-2, 0), new Vector2d(0,-12), 0, 2, "charFrontArm", "shoulder", inventory);
        Bone backArm = new ArmorBone(bArmImg, new Vector2d(-2, 0), new Vector2d(0,-12), 0, -1, "charBackArm", "shoulder", inventory);

        Bone testt = new InventoryBone(inventory, new Vector2d(-1, -15), new Vector2d(-2,10), Math.PI/4, 1, "item");

        body.addBone(frontLeg);
        body.addBone(backLeg);
        body.addBone(head);
        body.addBone(frontArm);
        body.addBone(backArm);

        frontArm.addBone(testt);

        Keyframe idleFrame = new Keyframe("idleFrame");
        idleFrame.addBoneAndTransformObject(body, new TransformationObject(new Vector2d(0,-3), 0));
        idleFrame.addBoneAndTransformObject(frontLeg, new TransformationObject(new Vector2d(), 0));
        idleFrame.addBoneAndTransformObject(backLeg, new TransformationObject(new Vector2d(), 0));
        idleFrame.addBoneAndTransformObject(head, new TransformationObject(new Vector2d(0,-12), 0));
        idleFrame.addBoneAndTransformObject(frontArm, new TransformationObject(new Vector2d(0,-12), -0.06));
        idleFrame.addBoneAndTransformObject(backArm, new TransformationObject(new Vector2d(0,-12), 0.07));

        Keyframe idleFrame1 = new Keyframe("idleFrame1");
        idleFrame1.addBoneAndTransformObject(body, new TransformationObject(new Vector2d(0,-3), 0));
        idleFrame1.addBoneAndTransformObject(frontLeg, new TransformationObject(new Vector2d(), 0));
        idleFrame1.addBoneAndTransformObject(backLeg, new TransformationObject(new Vector2d(), 0));
        idleFrame1.addBoneAndTransformObject(head, new TransformationObject(new Vector2d(0,-12), 0));
        idleFrame1.addBoneAndTransformObject(frontArm, new TransformationObject(new Vector2d(0,-12), 0.03));
        idleFrame1.addBoneAndTransformObject(backArm, new TransformationObject(new Vector2d(0,-12), -0.02));



        Keyframe walkFrame1 = new Keyframe("walkFrame1");
        walkFrame1.addBoneAndTransformObject(body, new TransformationObject(new Vector2d(0,-3), 0));
        walkFrame1.addBoneAndTransformObject(frontLeg, new TransformationObject(new Vector2d(), -Math.PI / 4));
        walkFrame1.addBoneAndTransformObject(backLeg, new TransformationObject(new Vector2d(), Math.PI / 4));
        walkFrame1.addBoneAndTransformObject(head, new TransformationObject(new Vector2d(0,-12), -0.1));
        walkFrame1.addBoneAndTransformObject(frontArm, new TransformationObject(new Vector2d(0,-12), Math.PI / 4));
        walkFrame1.addBoneAndTransformObject(backArm, new TransformationObject(new Vector2d(0,-12), -Math.PI / 4));

        Keyframe walkFrame2 = new Keyframe("walkFrame2");
        walkFrame2.addBoneAndTransformObject(body, new TransformationObject(new Vector2d(0,-3), 0));
        walkFrame2.addBoneAndTransformObject(frontLeg, new TransformationObject(new Vector2d(), Math.PI / 4));
        walkFrame2.addBoneAndTransformObject(backLeg, new TransformationObject(new Vector2d(), -Math.PI / 4));
        walkFrame2.addBoneAndTransformObject(head, new TransformationObject(new Vector2d(0,-12), 0.1));
        walkFrame2.addBoneAndTransformObject(frontArm, new TransformationObject(new Vector2d(0,-12), -Math.PI / 4));
        walkFrame2.addBoneAndTransformObject(backArm, new TransformationObject(new Vector2d(0,-12), Math.PI / 4));


        Keyframe frontFlip = new Keyframe("frontFlip1");
        frontFlip.addBoneAndTransformObject(body, new TransformationObject(new Vector2d(0,-3), 0));
        frontFlip.addBoneAndTransformObject(frontLeg, new TransformationObject(new Vector2d(), -Math.PI / 2));
        frontFlip.addBoneAndTransformObject(backLeg, new TransformationObject(new Vector2d(), -Math.PI / 4));
        frontFlip.addBoneAndTransformObject(head, new TransformationObject(new Vector2d(0,-12), -Math.PI / 2));
        frontFlip.addBoneAndTransformObject(frontArm, new TransformationObject(new Vector2d(0,-12), -Math.PI / 4));
        frontFlip.addBoneAndTransformObject(backArm, new TransformationObject(new Vector2d(0,-12), -Math.PI / 2));

        Keyframe frontFlip2 = new Keyframe("frontFlip2");
        frontFlip2.addBoneAndTransformObject(body, new TransformationObject(new Vector2d(0,-12), Math.PI));
        frontFlip2.addBoneAndTransformObject(frontLeg, new TransformationObject(new Vector2d(), -Math.PI / 2));
        frontFlip2.addBoneAndTransformObject(backLeg, new TransformationObject(new Vector2d(), -Math.PI / 4));
        frontFlip2.addBoneAndTransformObject(head, new TransformationObject(new Vector2d(0,-12), Math.PI / 2));
        frontFlip2.addBoneAndTransformObject(frontArm, new TransformationObject(new Vector2d(0,-12), Math.PI / 4));
        frontFlip2.addBoneAndTransformObject(backArm, new TransformationObject(new Vector2d(0,-12), Math.PI / 2));

        Keyframe frontFlip3 = new Keyframe("frontFlip3");
        frontFlip3.addBoneAndTransformObject(body, new TransformationObject(new Vector2d(0,-3), Math.PI * 2));
        frontFlip3.addBoneAndTransformObject(frontLeg, new TransformationObject(new Vector2d(), -Math.PI / 2));
        frontFlip3.addBoneAndTransformObject(backLeg, new TransformationObject(new Vector2d(), -Math.PI / 4));
        frontFlip3.addBoneAndTransformObject(head, new TransformationObject(new Vector2d(0,-12), Math.PI / 2));
        frontFlip3.addBoneAndTransformObject(frontArm, new TransformationObject(new Vector2d(0,-12), Math.PI / 4));
        frontFlip3.addBoneAndTransformObject(backArm, new TransformationObject(new Vector2d(0,-12), Math.PI / 2));


        Keyframe prone = new Keyframe("Prone");
        prone.addBoneAndTransformObject(body, new TransformationObject(new Vector2d(), Math.PI/4));


        Keyframe mine1 = new Keyframe("mineFrame1");
        mine1.addBoneAndTransformObject(frontArm, new TransformationObject(new Vector2d(0,-12), -4* Math.PI / 4));

        Keyframe mine2 = new Keyframe("mineFrame2");
        mine2.addBoneAndTransformObject(frontArm, new TransformationObject(new Vector2d(0,-12), -Math.PI / 4));

        Keyframe[] idleFrames = {idleFrame, idleFrame1};
        Keyframe[] walkingFrames = {walkFrame1, idleFrame, walkFrame2};
        Keyframe[] miningFrames = {mine1, mine2};
        Keyframe[] frontFlipFrames = {frontFlip, frontFlip2, frontFlip3};
        Keyframe[] proneFrames = {prone};


        Animation animIdle =  new Animation("Idle", idleFrames, AnimationTypes.Cycle, 80);
        Animation animWalking = new Animation("Walking", walkingFrames, AnimationTypes.Cycle, 15);
        Animation animMining = new Animation("Mining", miningFrames, AnimationTypes.Cycle, 10);
        Animation animFrontFlip = new Animation("FrontFlip", frontFlipFrames, AnimationTypes.Once, 20);
        Animation animProne = new Animation("Prone", frontFlipFrames, AnimationTypes.Cycle, 30);


        AnimationHandler a = new AnimationHandler();

        a.addAnimation(animIdle);
        a.addAnimation(animWalking);
        a.addAnimation(animMining);
        a.addAnimation(animFrontFlip);
        a.addAnimation(animProne);

        /*a.addKeyFrame(idleFrame);
        a.addKeyFrame(walkFrame1);
        a.addKeyFrame(walkFrame2);

        a.addKeyFrame(mine1);
        a.addKeyFrame(mine2);*/

        Model m = new Model(body, a, world);

        return m;
    }




    public static Model modelPigCreator(World world) {
        BufferedImage img = FileInput.LoadImage("Assets/textures/entity/pig/pig.png");

        BufferedImage headImg = img.getSubimage(0, 8, 8, 8);
        BufferedImage bodyImg = img.getSubimage(28, 16, 8, 16);
        BufferedImage snotImg = img.getSubimage(16, 17, 2, 3);
        BufferedImage legImg = img.getSubimage(13, 20, 3, 6);
        BufferedImage legImg2 = img.getSubimage(13, 20, 3, 6);
        BufferedImage legBehind = img.getSubimage(16, 20, 3, 6);
        BufferedImage legBehind2 = img.getSubimage(16, 20, 3, 6);

        Bone body = new Bone(bodyImg, new Vector2d(0,0), new Vector2d(), Math.PI/2, 0, "pigBody");
        Bone head = new Bone(headImg, new Vector2d(-4,-4), new Vector2d(), -Math.PI/2, 0, "pigHead");
        Bone snot = new Bone(snotImg, new Vector2d(), new Vector2d(4,0), 0, 0, "pigSnot");

        Bone legFrontB = new Bone(legImg, new Vector2d(-1,0), new Vector2d(7,12), -Math.PI/2, 0, "pigLegFrontB");
        Bone legBackB = new Bone(legBehind, new Vector2d(-1,0), new Vector2d(6,13), -Math.PI/2, -1, "pigLegBackB");

        Bone legFrontF = new Bone(legImg2, new Vector2d(-1,0), new Vector2d(7,3), -Math.PI/2, 0, "pigLegFrontF");
        Bone legBackF = new Bone(legBehind2, new Vector2d(-1,0), new Vector2d(6,4), -Math.PI/2, -1, "pigLegBackF");

        body.addBone(head);
        body.addBone(legFrontB);
        body.addBone(legBackB);
        body.addBone(legFrontF);
        body.addBone(legBackF);

        head.addBone(snot);


        Keyframe idleFrame = new Keyframe("idleFrame");
        idleFrame.addBoneAndTransformObject(body, new TransformationObject(new Vector2d(), Math.PI/2));
        idleFrame.addBoneAndTransformObject(head, new TransformationObject(new Vector2d(), -Math.PI/2 -0.1));

        Keyframe idleFrame2 = new Keyframe("idleFrame2");
        idleFrame2.addBoneAndTransformObject(body, new TransformationObject(new Vector2d(), Math.PI/2));
        idleFrame2.addBoneAndTransformObject(head, new TransformationObject(new Vector2d(), -Math.PI/2+0.1));

        Keyframe walkFrame1 = new Keyframe("walkFrame1");
        walkFrame1.addBoneAndTransformObject(body, new TransformationObject(new Vector2d(), Math.PI/2));
        walkFrame1.addBoneAndTransformObject(head, new TransformationObject(new Vector2d(), -Math.PI/2));

        walkFrame1.addBoneAndTransformObject(legFrontB, new TransformationObject(new Vector2d(7,12), -Math.PI/2 - 0.2));
        walkFrame1.addBoneAndTransformObject(legFrontF, new TransformationObject(new Vector2d(7,3), -Math.PI/2 - 0.2));

        walkFrame1.addBoneAndTransformObject(legBackB, new TransformationObject(new Vector2d(6,13), -Math.PI/2 + 0.2));
        walkFrame1.addBoneAndTransformObject(legBackF, new TransformationObject(new Vector2d(6,4), -Math.PI/2 + 0.2));

        Keyframe walkFrame2 = new Keyframe("walkFrame2");
        walkFrame2.addBoneAndTransformObject(body, new TransformationObject(new Vector2d(), Math.PI/2));
        walkFrame2.addBoneAndTransformObject(head, new TransformationObject(new Vector2d(), -Math.PI/2));

        walkFrame2.addBoneAndTransformObject(legFrontB, new TransformationObject(new Vector2d(7,12), -Math.PI/2 + 0.2));
        walkFrame2.addBoneAndTransformObject(legFrontF, new TransformationObject(new Vector2d(7,3), -Math.PI/2 + 0.2));

        walkFrame2.addBoneAndTransformObject(legBackB, new TransformationObject(new Vector2d(6,13), -Math.PI/2 - 0.2));
        walkFrame2.addBoneAndTransformObject(legBackF, new TransformationObject(new Vector2d(6,4), -Math.PI/2 - 0.2));



        Keyframe[] idleFrames = {idleFrame, idleFrame2};
        Keyframe[] walkFrames = {walkFrame1, walkFrame2};


        Animation animIdle =  new Animation("Idle", idleFrames, AnimationTypes.Cycle, 120);
        Animation animWalk =  new Animation("Walk", walkFrames, AnimationTypes.Cycle, 40);

        AnimationHandler a = new AnimationHandler();

        a.addAnimation(animIdle);
        a.addAnimation(animWalk);


        Model m = new Model(body, a, world);
        return m;
    }





}
