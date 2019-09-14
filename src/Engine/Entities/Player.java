package Engine.Entities;

import Engine.Blocks.*;
import Engine.Collision.BoxCollision;
import Engine.Constants;
import Engine.Enum.BlockSolidType;
import Engine.Inventory.Inventory;
import Engine.Model.Model;
import Engine.Model.ModelFactory;
import Engine.Particles.ParticleFactory;
import Engine.World.World;
import Engine.javax.vecmath.Vector2d;
import Engine.javax.vecmath.Vector3d;

import java.awt.*;

public class Player extends Entity {


    private BoxCollision boundingBox;

    private boolean upBumping;
    private boolean downBumping;
    private boolean leftBumping;
    private boolean rightBumping;

    private boolean ladder;

    private boolean jumpReady;

    private int ready;
    private double duration;
    private double engageTime;
    private double requestTime;

    private double gracePeriod;
    private double speedBoost;
    private double jumpVelocity;

    private Model playerModel;

    private Inventory inventory;

    private Vector3d blockBreakPos;
    private boolean breakingBlock;
    private double blockBreakingState;
    private int breakingState;


    public Player(double x, double y, World world) {
        super(x, y, world);
        this.boundingBox = new BoxCollision(16,28,-8, -28);
        this.upBumping = false;
        this.downBumping = false;
        this.leftBumping = false;
        this.rightBumping = false;
        this.jumpReady = false;
        this.ladder = false;

        this.blockBreakPos = new Vector3d();
        this.breakingBlock = false;
        this.blockBreakingState = 0;
        this.breakingState = 0;


        //Jumping
        this.ready = 0;
        this.engageTime = 0;
        this.duration = 0.05;
        this.requestTime = 0;
        this.gracePeriod = 0.1;
        this.speedBoost = 0.3;
        this.jumpVelocity = 55;
        this.inventory = new Inventory();
        this.playerModel = ModelFactory.modelSteveCreator("", world, this.inventory);

    }


    @Override
    public void update(double dt) {

        this.playerModel.getAnimationHandler().update(dt);


        double absX = Math.abs(this.getVelocity().getX());

        Vector2d posVec = Constants.PosToTilePos(this.getPosition());
        Block b = this.getState().getBlock((int)posVec.getX(), (int)posVec.getY());

        if(b.getBlockId() == Constants.blockHandler.getBlockId("Ladder")){
            this.getVelocity().setY(this.getVelocity().getY() + this.getState().getGravity().getY() * 0.3 * dt);
        } else {
            this.getVelocity().setY(this.getVelocity().getY() + this.getState().getGravity().getY() * dt);
        }

        this.getPosition().set(this.getPosition().getX() + this.getVelocity().getX() * dt, this.getPosition().getY() + this.getVelocity().getY() * dt);

        if(this.getDir() != 0){

            this.getVelocity().setX(this.getVelocity().getX() + this.getAcceleration() * dt * this.getDir());

        } else if(this.getVelocity().getX() != 0){
            double decel = Math.min(absX, this.getDeceleration() * dt);
            if(this.getVelocity().getX() > 0){
                this.getVelocity().setX(this.getVelocity().getX() - decel);
            } else {
                this.getVelocity().setX(this.getVelocity().getX() + decel);
            }
        }


        double drag = this.getDragFactor() * this.getVelocity().getX() * absX;
        this.getVelocity().setX(this.getVelocity().getX() - drag);

        this.jumpUpdate(dt);

        if(breakingBlock){

            Block br= getState().getBlock((int)blockBreakPos.getX(), (int)blockBreakPos.getY(), (int)blockBreakPos.getZ());
            double hardness = MineTime.hardness(br.getBlockId());
            if(hardness <  1000000) {
                MineTools effectiveTool = MineTime.effectiveTool(br.getBlockId());

                ItemInfo info = null;
                double multiplier = 3;
                ItemInfo inf = Constants.itemHandler.getItemInfo(inventory.getHandItem().getId());
                if (inventory.getHandItem().isItem()) {
                    if (inf.getMineTool() == effectiveTool) {
                        ToolType toolType = inf.getToolType();
                        switch (toolType) {
                            case Wood:
                                multiplier *= 2;
                                break;
                            case Stone:
                                multiplier *= 4;
                                break;
                            case Iron:
                                multiplier *= 6;
                                break;
                            case Diamond:
                                multiplier *= 8;
                                break;
                            case Gold:
                                multiplier *= 12;
                                break;
                        }


                    }
                }

                this.blockBreakingState += dt * multiplier;

                this.breakingState = (int) (blockBreakingState / hardness);

                if (blockBreakingState > hardness * 9) {

                    ToolType reqToolType = MineTime.getToolType(br.getBlockId());
                    int reqToolNum = MineTime.getToolTypeNum(reqToolType);
                    int thisToolNum = 0;

                    if (inventory.getHandItem().isItem()) {
                        thisToolNum = MineTime.getToolTypeNum(inf.getToolType());
                    }

                    boolean dropItem = true;
                    if(reqToolNum == 0){
                        dropItem = true;
                    } else {
                        dropItem = false;
                        if (inventory.getHandItem().isItem()) {
                            if (inf.getMineTool() == effectiveTool) {
                                if(thisToolNum >= reqToolNum){
                                    dropItem = true;
                                }
                            }
                        }
                    }

                    this.breakingBlock = false;
                    getState().breakBlock((int) blockBreakPos.getX(), (int) blockBreakPos.getY(), (int) blockBreakPos.getZ(), true, dropItem);
                    this.blockBreakingState = 0;

                }
            } else{
                breakingBlock = false;
            }
        }



        collisionHandler();


    }

    public void jumpUpdate(double dt) {
        if(this.requestTime > 0){
            if(this.ready > 0){
                this.engageTime = this.duration;
                this.requestTime = 0;
            }
            this.requestTime -= dt;
        }

        if(this.engageTime > 0){
            //this.getVelocity().setY(this.getVelocity().getY() - (this.jumpVelocity + Math.abs(this.getVelocity().getX()) * this.speedBoost));
            this.getVelocity().setY(this.getVelocity().getY() - (this.jumpVelocity));
            this.engageTime -= dt;
        }

        this.ready -= 1;

    }







    public void collisionHandler(){


        Vector2d posVec = Constants.PosToTilePos(this.getPosition());
        this.upBumping = false;
        this.downBumping = false;
        this.leftBumping = false;
        this.rightBumping = false;
        this.jumpReady = false;
        this.ladder = false;

        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 3; x++) {

                int offsetX = 0;
                if (x == 1){
                    offsetX = 1;
                } else if (x == 2){
                    offsetX = -1;
                }

                Vector2d tileVec = new Vector2d(posVec.getX() + offsetX, posVec.getY()-y);
                Block b = getState().getBlock(posVec.getX() + offsetX, posVec.getY() - y);

                Rectangle tileRec;
                if(b.isSlab()){
                    tileRec = Constants.TileCollisionSlab.getRect(new Vector2d(tileVec.getX()*16,tileVec.getY()*16));
                } else {
                    tileRec = Constants.TileCollision.getRect(new Vector2d(tileVec.getX()*16,tileVec.getY()*16));
                }



                if ((b.getSolidType() == BlockSolidType.Slab || b.getSolidType() == BlockSolidType.Solid) && this.getBoundingBox().AABBCollision(Constants.TileCollision, this.getPosition(), new Vector2d(tileVec.getX()*16,tileVec.getY()*16))) {

                    Rectangle topRec = boundingBox.topRect(this.getPosition());
                    Rectangle botRec = boundingBox.botRect(this.getPosition());
                    Rectangle leftRec = boundingBox.leftRect(this.getPosition());
                    Rectangle rightRec = boundingBox.rightRect(this.getPosition());


                    boolean topInter = topRec.intersects(tileRec);
                    boolean botInter = botRec.intersects(tileRec);
                    boolean leftInter = leftRec.intersects(tileRec);
                    boolean rightInter = rightRec.intersects(tileRec);


                    if (topInter && !botInter && this.getVelocity().getY() < 0) {
                        //System.out.println("top");
                        double overlap = topRec.y - (tileRec.y + tileRec.height);
                        this.getPosition().setY(this.getPosition().getY() - overlap);
                        this.getVelocity().setY(0);
                        this.cancelJump();
                    }
                    if (botInter && !topInter && this.getVelocity().getY() > 0) {
                        //System.out.println("bot");
                        double overlap = botRec.y + botRec.height - tileRec.y;

                        boolean part = false;
                        if(this.getVelocity().getY() > 300){
                            part = true;
                        }

                        this.getPosition().setY(this.getPosition().getY() - overlap);
                        this.getVelocity().setY(0);
                        this.downBumping = true;
                        this.jumpReady = true;
                        this.ready = 1;

                        if(part){
                            getState().addEmitter(ParticleFactory.Falling((int)this.getPosition().getX(), (int)this.getPosition().getY(), b.getBlockId()));
                        }

                    }
                    if (leftInter && !rightInter){
                        double overlap = leftRec.x - (tileRec.x + tileRec.width);
                        this.getPosition().setX(this.getPosition().getX() - overlap);
                        this.getVelocity().setX(0);
                    }



                    if (rightInter && !leftInter){
                        double overlap = rightRec.x + rightRec.width - (tileRec.x);
                        this.getPosition().setX(this.getPosition().getX() - overlap);
                        this.getVelocity().setX(0);

                    }
                } else if (b.getSolidType() == BlockSolidType.Ladder && boundingBox.botRect(this.getPosition()).intersects(tileRec)) {
                    this.ladder = true;
                }
            }

        }

    }





    @Override
    public void render(Graphics2D g2d) {
        super.render(g2d);



        Vector2d posVec = Constants.PosToTilePos(this.getPosition());

        Block bb = this.getState().getBlock(posVec.getX(), posVec.getY());

        this.playerModel.render(g2d, new Vector2d(this.getPosition().getX(), this.getPosition().getY()-8), new Color(0,0,0, 255 - (int)(255 * bb.getMaxLightLevel(this.getState().getDayLight())/16)));


        g2d.setColor(new Color(255,255,255, 100));
        //g2d.fillRect((int)this.getPosition().getX() + boundingBox.getOffsetX(), (int)this.getPosition().getY() + boundingBox.getOffsetY(), boundingBox.getWidth(), boundingBox.getHeight());

        /*
        g2d.setColor(Color.red);
        Rectangle l = this.boundingBox.leftRect(this.getPosition());
        g2d.fillRect(l.x, l.y, l.width, l.height);

        l = this.boundingBox.rightRect(this.getPosition());
        g2d.fillRect(l.x, l.y, l.width, l.height);
        g2d.setColor(Color.yellow);

        l = this.boundingBox.topRect(this.getPosition());
        g2d.fillRect(l.x, l.y, l.width, l.height);

        g2d.setColor(Color.WHITE);
        l = this.boundingBox.botRect(this.getPosition());
        g2d.fillRect(l.x, l.y, l.width, l.height);
        */


    }

    public void jump(){
        if(this.ladder){
            this.getVelocity().setY(this.getVelocity().getY() - (15));
        } else {
            this.requestTime = this.gracePeriod;
        }


    }

    public void cancelJump(){
        this.engageTime = 0;
        this.requestTime = 0;
    }

    public void setBreakingBlock(int x, int y, int z){

        if(breakingBlock){
            if((int)blockBreakPos.getX() == x && (int)blockBreakPos.getY() == y && (int)blockBreakPos.getZ() == z){
            } else {
                this.blockBreakPos.set(x,y,z);
                this.blockBreakingState = 0;
            }

        } else {
            this.breakingBlock = true;
            this.blockBreakPos.set(x,y,z);
            this.blockBreakingState = 0;
        }

    }




    public Inventory getInventory() {
        return inventory;
    }

    public BoxCollision getBoundingBox() {
        return boundingBox;
    }

    public boolean isJumpReady() {
        return jumpReady;
    }

    public boolean isLadder() {
        return ladder;
    }

    public Model getPlayerModel() {
        return playerModel;
    }

    public void setBreakingBlock(boolean breakingBlock) {
        this.breakingBlock = breakingBlock;
    }

    public Vector3d getBlockBreakPos() {
        return blockBreakPos;
    }

    public boolean isBreakingBlock() {
        return breakingBlock;
    }

    public double getBlockBreakingState() {
        return blockBreakingState;
    }

    public int getBreakingState() {
        return breakingState;
    }
}
