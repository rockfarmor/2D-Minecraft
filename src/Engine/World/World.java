package Engine.World;

import Engine.Blocks.*;
import Engine.Debug.Log;
import Engine.Entities.*;
import Engine.Enum.BlockSolidType;
import Engine.FileHandler.FileInput;
import Engine.Gui.GuiHandler;
import Engine.Inventory.InventoryItem;
import Engine.Math.ImprovedNoise;
import Engine.Model.AnimationHandler;
import Engine.Model.Model;
import Engine.Model.ModelFactory;
import Engine.Particles.ParticleEmitter;
import Engine.Particles.ParticleFactory;
import Engine.Particles.ParticleType;
import Engine.Renderer.Camera;
import Engine.Renderer.MComponent;
import Engine.Renderer.MFrame;
import Engine.javax.vecmath.Vector2d;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.*;

import Engine.Constants;
import Engine.javax.vecmath.Vector3d;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

public class World {


    private MFrame frame;
    private MComponent mComp;

    private int dayLight;

    private HashMap<Integer, Chunk> chunkMap;
    private HashMap<Integer, HashMap<Integer, HashMap<Integer, HashMap<Integer, Block>>>> mapBlocks;

    private ArrayList<Entity> entities;


    private ArrayList<int[]> blockUpdates;
    private ArrayList<int[]> waterUpdates;

    private Player player;

    private Camera camera;

    private float [][] heightNoise;
    private float [][] heightNoise2;
    private float [][] treeNoise;

    private int updateVal = 5;


    private int layer;

    private long lastTime;
    private double accumulatedTime;

    private double _accumulator;
    private double step;

    private double deltaTime;

    private ArrayList<ParticleEmitter> particleEmitters;
    private Vector2d gravity;

    private BufferedImage bg1;
    private BufferedImage bg2;
    private BufferedImage planet1;
    private BufferedImage planet2;
    private BufferedImage nebula;

    private BufferedImage inv;
    private BufferedImage invHand;

    private double planetRotation = 0;

    private int animationTimer;
    private boolean half;

    private Vector2d rayCast;
    private Vector2d rayCastPos;

    private boolean rightWasDown;



    public World(){
        init();

    }

    /*
     * Initializes variables and states
     */

    public void init(){

        //Sets variables connected to the update-loop
        this.lastTime = 0;
        this.deltaTime = 1 / 256.0;
        this.accumulatedTime = 0;
        this._accumulator = 0;
        this.step = 1/(120.0);

        this.rightWasDown = false;

        this.animationTimer = 10;
        this.half = true;

        bg1 = FileInput.LoadImage("Assets/Images/spr_stars01.png");
        bg2 = FileInput.LoadImage("Assets/Images/spr_stars02.png");
        planet1 = FileInput.LoadImage("Assets/Images/spr_planet02.png");
        planet2 = FileInput.LoadImage("Assets/Images/spr_planet04.png");
        nebula = FileInput.LoadImage("Assets/Images/nebla.jpg");

        this.inv = FileInput.LoadImage("Assets/Textures/gui/widgets.png");
        this.invHand = inv.getSubimage(0,22,24,24);
        this.inv = inv.getSubimage(0,0, 182,22);


        //Entities
        this.player = new Player(0,-500, this);
        this.rayCast = new Vector2d();
        this.rayCastPos = new Vector2d();

        Constants.guiHandler.getWindow("Inventory").setInventory(player.getInventory());
        Constants.guiHandler.getWindow("Craftingtable").setInventory(player.getInventory());

        //Constants
        this.gravity = new Vector2d(0, 1500);
        this.dayLight = 16;
        this.layer = 1;
        //this.handBlock = Constants.blockHandler.getBlockId("Torch");

        //Noise-maps
        this.heightNoise = ImprovedNoise.generatePerlinNoise(Constants.HeightNoiseNum,3, 8);
        this.heightNoise2 = ImprovedNoise.generatePerlinNoise(Constants.HeightNoiseNum,3, 8);
        this.treeNoise = ImprovedNoise.generateBlueNoise(10000,1);

        //Hashmaps and Arraylists
        this.chunkMap = new HashMap<>();
        this.mapBlocks = new HashMap<>();
        this.entities = new ArrayList<>();
        this.blockUpdates = new ArrayList<>();
        this.waterUpdates = new ArrayList<>();
        this.particleEmitters = new ArrayList();


        //Initializes the JFrame and JComponent
        this.frame = new MFrame("Minecraft", this);
        this.mComp = new MComponent(this);
        this.frame.add(this.mComp);

        //Init Camera
        this.camera = new Camera();
        this.camera.setOrigin(this.frame.getWidth()/2,this.frame.getHeight()/2);
        this.camera.setScale(4.0);
        this.camera.setFollow(player.getPosition());

    }

    /*
     * Removes all the current Chunks
     */
    public void reloadChunks(){
        Set set = chunkMap.entrySet();
        Iterator iterator = set.iterator();
        while(iterator.hasNext()) {
            Map.Entry mentry = (Map.Entry) iterator.next();
            iterator.remove();
        }
    }

    /*
     * Loads and unloads chunks based on the player position.
     */
    public void loadAndUnloadChunks(){

        int playerChunk = Constants.GetChunkFromPos(player.getPosition());

        boolean added = false;

        Set set = chunkMap.entrySet();
        Iterator iterator = set.iterator();
        while(iterator.hasNext()) {
            Map.Entry mentry = (Map.Entry)iterator.next();
            if (!(((int)mentry.getKey() >= playerChunk - Constants.ChunksLoaded) && ((int)mentry.getKey() <= playerChunk + Constants.ChunksLoaded))){
                iterator.remove();
            }
        }


        for (int i = playerChunk - Constants.ChunksLoaded; i < playerChunk + Constants.ChunksLoaded+1; i++) {

            if (!chunkMap.containsKey(i)){
                chunkMap.put(i, new Chunk(heightNoise, heightNoise2, treeNoise, i, mapBlocks, this));
                added = true;
            }

        }

        if (added){
            updateLightLevel();
        }
    }

    /*
     * Main render-method
     */

    public void render(Graphics2D g2d){
        if(this.animationTimer <= 0) {

            Constants.blockHandler.getBlockInfo("Fire").update();
            Constants.blockHandler.getBlockInfo("Flowing Water").update();


            if(this.half){
                Constants.blockHandler.getBlockInfo("Water").update();
                Constants.blockHandler.getBlockInfo("Lava").update();
                Constants.blockHandler.getBlockInfo("Flowing Lava").update();
            }

            this.animationTimer = 5;
            this.half = !this.half;
        }
        this.animationTimer--;


        g2d.setColor(new Color(0, 0, 63));
        g2d.fillRect(0,0, mComp.getWidth(), mComp.getHeight());

        double offset = -player.getPosition().getX() / 100;

        double offset2 = -player.getPosition().getY() / 1000;

        double scale = 1;

        for (int i = -2; i < 3; i++) {
            for (int j = -2; j < 3; j++) {
                g2d.drawImage(this.bg1, (int)(offset + i * this.bg1.getWidth() * scale), (int)(offset2 + j * this.bg1.getHeight() * scale),(int)(this.bg1.getWidth()*scale), (int)(this.bg1.getHeight()*scale),null);
            }
        }
        offset = -player.getPosition().getX() / 200;
        offset2 = -player.getPosition().getY() / 1500;
        Composite com = g2d.getComposite();
        int rule = AlphaComposite.SRC_OVER;
        Composite comp = AlphaComposite.getInstance(rule , 0.4f );
        g2d.setComposite(comp);
        g2d.drawImage(this.nebula, 0 , -300, this.nebula.getWidth(), this.nebula.getHeight(), null);
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, -300+this.nebula.getHeight(), this.nebula.getWidth(), 400);
        g2d.setComposite(com);

        for (int i = -2; i < 3; i++) {
            for (int j = -2; j < 3; j++) {
                g2d.drawImage(this.bg2, (int)(offset + i * this.bg2.getWidth() * scale), (int)(offset2 + j * this.bg2.getHeight() * scale),(int)(this.bg2.getWidth()*scale), (int)(this.bg2.getHeight()*scale),null);
            }
        }

        double x = this.frame.getWidth()/2 + this.frame.getWidth()/2*Math.cos(this.planetRotation - Math.PI);
        double y = this.frame.getHeight()/2 + this.frame.getHeight()/2*Math.sin(this.planetRotation - Math.PI);
        g2d.drawImage(this.planet1, (int)x-45,(int)y-45, 150-75,150-75,null);
        x = this.frame.getWidth()/2 + this.frame.getWidth()/2*Math.cos(this.planetRotation - Math.PI+ Math.PI / 6);
        y = this.frame.getHeight()/2 + this.frame.getHeight()/2*Math.sin(this.planetRotation - Math.PI + Math.PI / 6);
        g2d.drawImage(this.planet2, (int)x-75,(int)y-75, 150,150,null);


        this.planetRotation += Math.PI/10000;

        AffineTransform at = new AffineTransform();
        at.translate((int)camera.getOrigin().getX(), (int)camera.getOrigin().getY());
        at.scale(camera.getScale().getX(), camera.getScale().getY());
        at.rotate(camera.getRotation());
        at.translate((int)-camera.getFollow().getX(), (int)-camera.getFollow().getY());
        g2d.setTransform(at);

        Set set = chunkMap.entrySet();
        Iterator iterator = set.iterator();
        while(iterator.hasNext()) {
            Map.Entry mentry = (Map.Entry) iterator.next();

            Chunk c = chunkMap.get(mentry.getKey());
            c.renderChunk(g2d, player.getPosition(), this);

        }

        if(player.isBreakingBlock()){

            Vector3d pos = player.getBlockBreakPos();
            double stage = player.getBreakingState();

            int destroyStage = (int)stage;

            BufferedImage destroy = Constants.blockHandler.getDestroy(destroyStage);

            g2d.drawImage(destroy, (int)(pos.getX()) * 16, (int)(pos.getY()) * 16, null);


        }


        //render mouse
        Vector2d s2w = this.mComp.screenToWorld(this.mComp.mouseX, this.mComp.mouseY, camera);

        double localX = player.getPosition().getX() + rayCast.getX();
        double localY = player.getPosition().getY() - 16 + rayCast.getY();

        int xx2 = (int)this.rayCastPos.getX();
        int yy2 = (int)this.rayCastPos.getY();


        //rayCast.set(player.getPosition().getX(), rayCast.getY()-16);

        //System.out.println(localX);



        int xx = (int) (localX / 16);
        if (localX < 0) {
            xx -= 1;
        }
        int yy = (int) (localY / 16);


        g2d.setColor(Color.RED);
        g2d.drawRect(xx2 * 16, yy2 * 16, 16, 16);



        g2d.setColor(new Color(255,255,255,100));
        g2d.drawRect(xx * 16, yy * 16, 16, 16);

        Block mblock = getBlock(xx, yy);
        Block mblocko = getBlock(xx, yy-1);
        if (mblocko.getSolidType() == BlockSolidType.Air){
            if (mblock.getTopBlockId() != -1){
                g2d.drawRect(xx * 16, yy * 16-4, 16, 4);
                if(mblock.getSolidType() == BlockSolidType.Air)
                    g2d.drawRect(xx * 16, yy * 16 + 16-4, 16, 4);
            }
        }

        player.render(g2d);

        //g2d.setColor(Color.red);
        //g2d.drawLine((int)player.getPosition().getX(), (int)player.getPosition().getY()-16, (int)player.getPosition().getX() + (int)rayCast.getX(), (int)player.getPosition().getY()-16 + (int)rayCast.getY());

        int playerChunk = Constants.GetChunkFromPos(player.getPosition());

        for (int i = 0; i < entities.size(); i++) {
            entities.get(i).render(g2d);
        }

        g2d.setColor(Color.red);

        for (int i = 0; i < this.particleEmitters.size(); i++) {
            this.particleEmitters.get(i).render(g2d);
        }

        at = new AffineTransform();
        g2d.setTransform(at);

        g2d.setColor(new Color(0, 0, 0, 100));
        g2d.fillRect(0,0, 200,frame.getHeight() / 2);
        g2d.setColor(Color.WHITE);
        g2d.drawString("FPS: " + mComp.getFps(), 10,15);
        g2d.drawString("x: " + (int)player.getPosition().getX()/16 + ", y: " + (int)player.getPosition().getY()/16, 10,30);
        g2d.drawString("PlayerChunk: " + playerChunk, 10,45);
        if (this.getChunk(playerChunk) != null)
            g2d.drawString("Biome: " + getChunk(playerChunk).getbType().toString(), 10,60);

        for (int i = 0; i < Constants.debugger.logSize(); i++) {
            Log l = Constants.debugger.getLog(i);
            g2d.drawString(i + "->" + l.getText(), 10, 60+15 + i*15);
        }


        int x_ = (int)(frame.getWidth() / 2 - this.inv.getWidth());
        int y_ = (int)(frame.getHeight() - 100);

        g2d.drawImage(this.inv, x_, y_, this.inv.getWidth() * 2, this.inv.getHeight() * 2, null);
        g2d.setFont(Constants.InventoryItemNumFont2);
        for (int i = 0; i < 9; i++) {
            InventoryItem item = player.getInventory().getInventoryItem(i, 3);
            if (item != null && !(item.getId() == 0 && !item.isItem())) {
                g2d.drawImage(item.getImage(), x_ + 3 * 2 + 40 * i, y_ + 3 * 2, 32, 32, null);
                g2d.setColor(Color.white);
                g2d.drawString("" + item.getNum(), x_ + 3 * 2 + 40 * i, y_ + 3 * 2 + 10);
            }
        }

        for (int i = 0; i < 9; i++) {
            InventoryItem item = player.getInventory().getInventoryItem(i, 3);
            if (item == player.getInventory().getHandItem()){
                g2d.drawImage(this.invHand, x_-2 + 40*i, y_-2,this.invHand.getWidth() * 2, this.invHand.getHeight() * 2, null);
                break;
            }
        }





        Constants.guiHandler.render(g2d);





        g2d.dispose();

    }

    /*
     * Update-accumulator. Makes sure that the game is updated at least every 1/60 of a second.
     */
    public void updateAccumulator(){
        updateNoDt();
        long time = System.currentTimeMillis();


        double acc = (time - this.lastTime) / 1000.0;

        boolean updated = false;


        accumulatedTime += (time - this.lastTime) / 1000.0;
        if (accumulatedTime > 1)
            accumulatedTime = 1;

        while(accumulatedTime > this.deltaTime){
            this.update(this.deltaTime);
            updated = true;

            accumulatedTime -= this.deltaTime;
        }

        this.lastTime = time;
        if(updated) {
            this.mComp.repaint();
        }
    }
    /*
     * Updates that can't wait. Usually connected to user input.
     */
    public void updateNoDt(){
        guiHandler();
        if(!Constants.guiHandler.isActiveWindow())
            mouseHandler();
        frame.keyEWasDown = frame.keyE;
    }

    /*
     * Update Method, used for smaller timesteps.
     */
    public void update(double dt){
        this._accumulator += dt;

        while(this._accumulator > this.step){
            this.simulate(this.step);
            this._accumulator -= this.step;
        }
    }

    /*
     * Main update method
     */
    public void simulate(double dt){

        Constants.debugger.update();

        //Updates every particleEmitter and removes them if they are done.
        for (int i = particleEmitters.size()-1; i >= 0; i--) {
            ParticleEmitter emitter = this.particleEmitters.get(i);
            emitter.update(dt, this);

            if(emitter.numParticles() <= 0){
                this.particleEmitters.remove(i);
            }
        }


        player.update(dt);

        if(player.isJumpReady()){
            if(Math.abs(player.getVelocity().getX()) > 10){
                if(Constants.randomNumber(0,1000) < 100){
                    Block b = getBlock((int)(player.getPosition().getX() / 16), ((player.getPosition().getY() + 7)/16));
                    addEmitter(ParticleFactory.Walking((int)player.getPosition().getX(), (int)player.getPosition().getY(), b.getBlockId(), player));
                }


            }
        }
        player.setDir(0);


        if (frame.keyD) {
            player.setDir(1);
            //player.getVelocity().setX(player.getVelocity().getX() + 400);
        }

        if(frame.keyA){
            player.setDir(-1);
        }

        if (frame.keyW) {
            player.jump();
        } else {
            player.cancelJump();
        }

        if(frame.keySpace)
            player.getVelocity().setY(player.getVelocity().getY() - (15));


        loadAndUnloadChunks();
        camera.setFollow(player.getPosition(), 0.4);
        camera.setOrigin(frame.getWidth() / 2, frame.getHeight() / 2, 0.1);


        if (camera.getFollow().getY() > (Chunk.ChunkHeight) * 16 - 150+16) camera.getFollow().setY((Chunk.ChunkHeight) * 16 - 150+16);

        int playerChunk = Constants.GetChunkFromPos(player.getPosition());

        for (int i = entities.size()-1; i >= 0; i--) {
            if (entities.get(i).getRemove()) {
                entities.remove(i);
            } else {
                entities.get(i).update(dt);
            }
        }


        AnimationHandler a = player.getPlayerModel().getAnimationHandler();

        if (frame.keyA){
            player.getPlayerModel().setScaleX(-1);
        } else if (frame.keyD){
            player.getPlayerModel().setScaleX(1);
        }




        if(frame.keyShift){
            if ((frame.keyA || frame.keyD)){
                a.toggleAnimation("FrontFlip", true);
                a.toggleAnimation("Idle", false);
                a.toggleAnimation("Walking", false);
            } else {
                a.toggleAnimation("Idle", true);
                a.toggleAnimation("Walking", false);
            }
        } else {
            if ((frame.keyA || frame.keyD)){
                a.toggleAnimation("Walking", true);
                a.toggleAnimation("Idle", false);
                a.toggleAnimation("FrontFlip", false);

            } else {
                a.toggleAnimation("Idle", true);
                a.toggleAnimation("Walking", false);
                a.toggleAnimation("FrontFlip", false);

            }

        }

        if (mComp.mouseLeftDown){
            a.toggleAnimation("Mining", true);
        } else {
            a.toggleAnimation("Mining", false);
        }




        if (blockUpdates.size() > 0){
            ArrayList<int[]> bUpdates = new ArrayList<>();
            for (int i = 0; i < blockUpdates.size(); i++) {
                bUpdates.add(blockUpdates.get(i));
            }
            blockUpdates = new ArrayList<>();

            for (int i = bUpdates.size() -1; i >= 0; i--) {
                int[] blocks = bUpdates.get(i);
                updateBlock(blocks[0], blocks[1], blocks[2]);
            }
        }

        if(updateVal <= 0) {
            int num = 0;
            for (int i = 0; i < 1; i++) {
                if(waterUpdates.size() > 0){
                    int[] blocks = waterUpdates.get(waterUpdates.size()-1);
                    this.updateBlock(blocks[0], blocks[1], blocks[2]);
                    this.waterUpdates.remove(waterUpdates.size()-1);
                }
            }

            updateVal = 10;
        }
        updateVal--;


    }

    public void guiHandler(){

        int x = this.mComp.mouseX;
        int y = this.mComp.mouseY;

        if(frame.keyE && !frame.keyEWasDown){

            if(Constants.guiHandler.isWindowActive("Craftingtable")){
                Constants.guiHandler.deActiveWindow("Craftingtable", this);
            } else {
                Constants.guiHandler.toggleWindow("Inventory", this);
            }

        }

        Constants.guiHandler.update(x, y, this.mComp.mouseLeftDown, this.mComp.mouseRightDown, frame, this);

    }

    public void mouseHandler(){

        int x = this.mComp.mouseX;
        int y = this.mComp.mouseY;

        boolean rightClicked = this.mComp.mouseRightDown && !this.rightWasDown;

        Vector2d s2w = this.mComp.screenToWorld(x, y, camera);
        rayCast.set(s2w);

        double localX = s2w.getX();
        double localY = s2w.getY();


        rayCast.set(localX - player.getPosition().getX(), localY - (player.getPosition().getY() - 16));
        double realLength = rayCast.length();
        rayCast.normalize();

        double length = realLength;
        boolean collide = false;
        if(length > Constants.PlayerReach) length = Constants.PlayerReach;

        Block found = null;

        for (int i = 0; i < Constants.PlayerReach; i++) {

            Vector2d vec = Constants.PosToTilePos(player.getPosition().getX() + rayCast.getX() * i, player.getPosition().getY()-16 + rayCast.getY() * i);
            Block bl = getBlock(vec.getX(), vec.getY());
            BlockSolidType solidType = bl.getSolidType();

            if(!(bl.getBlockId() == 0 || solidType == BlockSolidType.Fluid)){
                if(i < realLength) {
                    length = i;
                    collide = true;
                    break;
                }

            }
        }
        this.rayCastPos.set(rayCast);
        if(collide)
            this.rayCastPos.scale(length-5);
        else
            this.rayCastPos.scale(length);


        this.rayCastPos.set(rayCastPos.getX() + player.getPosition().getX(), rayCastPos.getY() + player.getPosition().getY() - 16);
        rayCast.scale(length);



        localX = player.getPosition().getX() + rayCast.getX();
        localY = player.getPosition().getY() - 16 + rayCast.getY();

        int xx2 = (int) (rayCastPos.getX() / 16);
        if (localX < 0) {
            xx2 -= 1;
        }
        int yy2 = (int) (rayCastPos.getY() / 16);

        rayCastPos.set(xx2, yy2);

        //if(this.layer == 0) collide = true;

        if(!collide){

            Block up = getBlock(xx2, yy2-1);
            Block down = getBlock(xx2, yy2+1);
            Block left = getBlock(xx2-1, yy2);
            Block right = getBlock(xx2+1, yy2);
            Block back = getBlock(xx2, yy2, 0);
            if(!(up.getBlockId() == 0 || up.getSolidType() == BlockSolidType.Fluid) || !(down.getBlockId() == 0 || down.getSolidType() == BlockSolidType.Fluid) || !(right.getBlockId() == 0 || right.getSolidType() == BlockSolidType.Fluid) || !(left.getBlockId() == 0 || left.getSolidType() == BlockSolidType.Fluid) || !(back.getBlockId() == 0 || back.getSolidType() == BlockSolidType.Fluid)){
                collide = true;
            }

        }



        if (this.mComp.mouseLeftDown){


            int xx = (int) (localX / 16);
            if (localX < 0) {
                xx -= 1;
            }
            int yy = (int) (localY / 16);


            int layer = 1;

            if(getBlock(xx,yy,1).getBlockId() == 0)
                layer = 0;

            Block b = getBlock(xx, yy, layer);

            MineTypes mineTypes = Constants.blockHandler.getMineType(b.getBlockId());

            if(b.getBlockId() != Block.Air && mineTypes != MineTypes.Liquid && mineTypes != MineTypes.Unbreakable){
                //player.breakBlock(xx,yy,this.layer, b);
                player.setBreakingBlock(xx, yy, layer);
            }

        } else {
            player.setBreakingBlock(false);
        }

        if (rightClicked){


            int xx = (int)(localX/16);
            if (localX < 0){
                xx -= 1;
            }
            int yy = (int)(localY/16);

            int xx3 = (int) (s2w.getX() / 16);
            if (localX < 0) {
                xx3 -= 1;
            }
            int yy3 = (int) (s2w.getY() / 16);


            if (getBlock(xx3,yy3, this.layer).getBlockId() == Constants.blockHandler.getBlockId("Crafting Table")) {
                Constants.guiHandler.activeWindow("Craftingtable", this);

            } else if (getBlock(xx3,yy3, this.layer).getBlockId() == Constants.blockHandler.getBlockId("Tnt")) {
                this.setBlock(Block.Air, xx3, yy3, this.layer);
                addEntity(Constants.GetChunkFromPos(xx3*16+8), new BlockExplosive(xx3*16+8, yy3*16+8, this));
            }  else if (getBlock(xx2,yy2, this.layer).getBlockId() == Block.Air && collide) {
                this.setBlock(player.getInventory().placeHanditem(), xx2, yy2, this.layer,0,true, false);

            } else if(getBlock(xx2, yy2, this.layer).getSolidType() == BlockSolidType.Fluid && collide){

                this.setBlockNoUpdate(Block.Air, xx2, yy2, this.layer);
                this.setBlock(player.getInventory().placeHanditem(), xx2, yy2, this.layer,0,true, false);

            } else if (getBlock(xx,yy, this.layer).getBlockId() == Constants.blockHandler.getBlockId("Stone Slab") && player.getInventory().getHandItem().getId() == Constants.blockHandler.getBlockId("Stone Slab")){
                player.getInventory().placeHanditem();
                this.setBlock(Block.Air,xx, yy, this.layer,0, false, true);
                this.setBlock(Constants.blockHandler.getBlockId("Double Stone Slab"), xx, yy, this.layer);
            }
        }

        this.rightWasDown = mComp.mouseRightDown;

    }


    public void setMap(int id, int x, int y){
        setBlock(id, x, y, 1);
    }


    public void breakBlock(int x, int y, int layer){
        breakBlock(x, y, layer, true, true);
    }

    public void breakBlock(int x, int y, int layer, boolean update){
        breakBlock(x,y,layer,update,true);
    }

    public void breakBlock(int x, int y, int layer, boolean update, boolean dropItem){
        Block b = getBlock(x,y,layer);

        if(dropItem) {
            BlockDrop drop = Constants.blockHandler.getBlockDrop(b.getBlockId());
            if (!(drop.getId() == 0 && !drop.isItem())) {
                int num = drop.getNum();
                if (num > 0) {
                    InventoryItem item = new InventoryItem(drop.getId(), num, drop.isItem());

                    int offX = 0;
                    int rand = Constants.randomNumber(0, 5);
                    if (Constants.randomNumber(0, 100) < 50) {
                        offX = -rand;
                    } else {
                        offX = rand;
                    }
                    DropEntity e = new DropEntity(x * 16 + 8 + offX, y * 16, this, item);
                    addEntity(0, e);
                    e.getVelocity().set(0, -20);
                }
            }
        }
        addEmitter(ParticleFactory.BlockParticle(x * 16+8, y * 16 + 8, b.getBlockId()));


        setBlock(Block.Air, x, y, layer, 0, update, !update);





    }


    public void setBlockNoUpdate(int id, int x, int y, int layer){
        setBlock(id, x, y, layer, 0, false, true);
    }


    public void setBlock(int id,int x, int y, int layer){
        setBlock(id, x, y, layer,0, true, false);
    }

    public void setBlock(int id,int x, int y, int layer, boolean updateThis){
        setBlock(id, x, y, layer, 0, updateThis, false);
    }

    public void setBlock(int id,int x, int y, int layer, int specialValue, boolean updateThis){
        setBlock(id, x, y, layer, specialValue, updateThis, false);
    }

    public void setBlock(int id,int x, int y, int layer, int specialValue, boolean updateThis, boolean neverUpdate){
        int chunkX = (x / (16));
        int xx = x % 16;
        if (x < 0){
            chunkX-=1;
            xx += 16;
        }



        int idd = id;



        if (chunkMap.get(chunkX) != null){
            Block b = chunkMap.get(chunkX).getBlock(xx, y, layer);
            if (b.getBlockId() != 0){
                idd = 0;
            }
            b.setBlockId(idd);
            b.setSpecialValue(specialValue);
        }
        if(chunkX < 0){
            if (xx == 16){ xx = 0;
                chunkX++;
            }
        }

        if(mapBlocks.get(chunkX) != null){
            if (mapBlocks.get(chunkX).get(xx) != null){
                if (mapBlocks.get(chunkX).get(xx).get(y) != null){
                    if (mapBlocks.get(chunkX).get(xx).get(y).get(layer) != null){
                        Block b = mapBlocks.get(chunkX).get(xx).get(y).get(layer);
                        b.setBlockId(idd);
                    } else {
                        mapBlocks.get(chunkX).get(xx).get(y).put(layer, new Block(idd));
                    }
                } else {
                    mapBlocks.get(chunkX).get(xx).put(y, new HashMap<>());
                    mapBlocks.get(chunkX).get(xx).get(y).put(layer, new Block(idd));
                }
            } else {
                mapBlocks.get(chunkX).put(xx, new HashMap<>());
                mapBlocks.get(chunkX).get(xx).put(y, new HashMap<>());
                mapBlocks.get(chunkX).get(xx).get(y).put(layer, new Block(idd));
            }
        } else {

            mapBlocks.put(chunkX, new HashMap<>());
            mapBlocks.get(chunkX).put(xx, new HashMap<>());
            mapBlocks.get(chunkX).get(xx).put(y, new HashMap<>());
            mapBlocks.get(chunkX).get(xx).get(y).put(layer, new Block(idd));
        }

        mapBlocks.get(chunkX).get(xx).get(y).get(layer).setSpecialValue(specialValue);

        if (updateThis){
            sheduleBlockUpdate(x, y, layer);
        }

        if (!neverUpdate) {





            //updateBlock(x, y-1, layer);
            //updateBlock(x, y+1, layer);


            sheduleBlockUpdate(x - 1, y, layer);
            sheduleBlockUpdate(x + 1, y, layer);
            sheduleBlockUpdate(x, y - 1, layer);
            sheduleBlockUpdate(x, y + 1, layer);
        }

        if(updateThis ||!neverUpdate){
            updateLightLevel();
        }





    }


    public void updateLightLevel(){


        ArrayList<Point> lightSources = new ArrayList<>();

        int lightTick = 2;

        int pChunk = Constants.GetChunkFromPos(player.getPosition());
        int startX = (pChunk - Constants.ChunksLoaded-1) * Chunk.ChunkWidth;
        int stopX = (pChunk + Constants.ChunksLoaded+1) * Chunk.ChunkWidth;

        for (int x = startX; x < stopX; x++) {

            for (int y = 0; y < Chunk.ChunkHeight; y++) {
                Block b = getBlock(x,y);

                b.setLightLevel(0);

            }
        }



        for (int x = startX; x < stopX; x++) {
            int val = 16;
            for (int y = 0; y < Chunk.ChunkHeight; y++) {
                Block b = getBlock(x,y);

                b.setMaxLightLevel(val);

                if (!b.transparant()){
                    val -= lightTick;
                }
                if (val <= 0)
                    break;
            }
        }

        for (int x = startX; x < stopX; x++) {
            for (int y = 0; y < Chunk.ChunkHeight; y++) {
                Block b = getBlock(x,y);
                int val = b.getLightLevel()-lightTick;
                if (b.transparant()) {
                    Block r = getBlock(x + 1, y);
                    r.setMaxLightLevel(val);
                }

                if (b.isLightSource()){
                    lightSources.add(new Point(x,y));
                }
                b.setLightLevel2(0);

            }

        }

        for (int x = stopX -1; x >=startX; x--) {
            for (int y = 0; y < Chunk.ChunkHeight; y++) {
                Block b = getBlock(x,y);
                int val = b.getLightLevel()-lightTick;
                if (b.transparant()) {
                    Block l = getBlock(x - 1, y);
                    l.setMaxLightLevel(val);
                }
            }

        }

        for (int x = startX; x < stopX; x++) {
            for (int y = 0; y < Chunk.ChunkHeight; y++) {
                Block b = getBlock(x,y);
                int val = b.getLightLevel()-lightTick;
                Block r = getBlock(x, y+1);
                Block r2 = getBlock(x, y-1);
                r2.setMaxLightLevel(val);
                r.setMaxLightLevel(val);

            }

        }




        int lightTravel = 16;
        for (int i = 0; i < lightSources.size(); i++) {
            for (int y = -lightTravel; y < lightTravel+1; y++) {
                for (int x = -lightTravel; x < lightTravel+1; x++) {
                    Point p = lightSources.get(i);
                    Block b = getBlock((int)p.getX() + x, (int)p.getY() + y);

                    int val = 0;

                    double distance = Math.sqrt(x*x + y*y);
                    if (distance <= lightTravel){
                        val = (int)(16 * (1 - distance / lightTravel));
                    }

                    b.setMaxLightLevel2(val);
                }
            }
        }
    }

    public void setHandBlock(int handBlock) {

        this.player.getInventory().getHandItem().set(handBlock, 64, false);


        //String bName = Constants.blockHandler.getBlockInfo(this.handBlock).name;
        //Constants.debugger.addLog("Block " + bName + " Choosen", 1);
    }

    public int getDayLight() {
        return dayLight;
    }

    public Vector2d getGravity() {
        return gravity;
    }

    public void setDayLight(int dayLight) {
        this.dayLight = dayLight;
        if (this.dayLight < 0) this.dayLight = 0;
        if (this.dayLight > 16) this.dayLight = 16;
    }

    public Block getBlock(double x, double y){
        return getBlock((int)x, (int)y);
    }

    public Block getBlock(int x, int y){
        return getBlock(x, y, 1);
    }


    public Block getBlock(int x, int y, int layer){
        //int chunkX = (int)(x / (16));
        //int xx = (int)(Constants.GetChunkPosFromGlobal(new Vector2d(x,y)).getX());


        int chunkX = (x / (16));
        int xx = x % 16;
        if (x < 0){
            chunkX-=1;
            xx += 16;
        }


        if (chunkMap.get(chunkX) != null){
            return chunkMap.get(chunkX).getBlock(xx, y, layer);
        }


        return new Block(0);
    }


    public void addEntity(int chunkX, Entity e){
        /*if (entities.get(chunkX) == null){
            ArrayList<Entity> list = new ArrayList<>();
            entities.put(chunkX, list);
        }
        entities.get(chunkX).add(0, e);*/
        entities.add(0,e);


    }

    public Entity getEntity(int chunkX, int i){
        /*
        if (entities.get(chunkX) != null) {
            ArrayList<Entity> list = entities.get(chunkX);
            if (i >= 0 && i < list.size()){
                return list.get(i);
            }
        }
        return null;
        */
        return null;
    }

    public int getNumEntities(int chunkX){

        //if (entities.get(chunkX) != null){
        //    return entities.get(chunkX).size();
        //}


        return 0;
    }



    public void updateBlock(int x, int y){
        updateBlock(x, y, 1);
    }

    public void sheduleBlockUpdate(int x, int y, int layer){
        int id = getBlock(x,y,layer).getBlockId();
        int[] pos = {x, y, layer};
        if(id == Constants.blockHandler.getBlockId("Water") || id == Constants.blockHandler.getBlockId("Flowing Water") || id == Constants.blockHandler.getBlockId("Lava") || id == Constants.blockHandler.getBlockId("Flowing Lava")){
            this.waterUpdates.add(0, pos);
        } else {
            this.blockUpdates.add(0, pos);
        }
    }



    public void updateBlock(int x, int y, int layer){

        int waterTravel = Constants.WaterTravel;
        int lavaTravel = Constants.LavaTravel;

        Block b = getBlock(x, y, layer);

        if (b.isFallingBlock() && layer == 1) {
            if (getBlock(x,y+1).getBlockId() == Block.Air){
                BlockEntity bl = new BlockEntity(x * 16 + 8, y * 16 + 8, new Block(b.getBlockId()), this);
                bl.setLayer(layer);
                int chunkX = Constants.GetChunkFromPos(x * 16 + 8);
                this.addEntity(chunkX, bl);
                this.setBlock(Block.Air, x, y, layer,0, true, false);
            }
        } else if(b.getBlockId() == Constants.blockHandler.getBlockId("Water")  && layer == 1) {
            //Flowing Water
            Block left = getBlock(x-1,y);
            Block right = getBlock(x+1,y);
            Block down = getBlock(x,y+1);


            if(down.getSolidType() == BlockSolidType.Solid) {
                if (left.getBlockId() == 0) {
                    this.setBlock(Constants.blockHandler.getBlockId("Flowing Water"), x - 1, y, 1, waterTravel-1, true);
                } else if (left.getBlockId() == Constants.blockHandler.getBlockId("Flowing Water")) {
                    if (left.getSpecialValue() < waterTravel-1) {
                        setBlockNoUpdate(Block.Air, x-1, y, 1);

                        this.setBlock(Constants.blockHandler.getBlockId("Flowing Water"), x - 1, y, 1, waterTravel-1, false, false);
                    }

                    if (left.getSpecialValue() >= waterTravel-1) {
                        sheduleBlockUpdate(x - 1, y, 1);
                    }

                } else if(Constants.blockHandler.breakableByWater(left.getBlockId())){
                    this.breakBlock(x-1,y,1, false);
                    this.setBlock(Constants.blockHandler.getBlockId("Flowing Water"), x - 1, y, 1, waterTravel-1, true);
                } else if(left.getBlockId() == Constants.blockHandler.getBlockId("Lava")){
                    this.setBlockNoUpdate(Block.Air, x-1, y, 1);
                    this.setBlock(Constants.blockHandler.getBlockId("Obsidian"), x-1, y, 1, 0, true, false);
                } else if(left.getBlockId() == Constants.blockHandler.getBlockId("Flowing Lava")){
                    this.setBlockNoUpdate(Block.Air, x-1, y, 1);
                    this.setBlock(Constants.blockHandler.getBlockId("Cobblestone"), x-1, y, 1, 0, true, false);
                }

                if (right.getBlockId() == 0) {
                    this.setBlock(Constants.blockHandler.getBlockId("Flowing Water"), x + 1, y, 1, waterTravel-1, false, false);
                } else if (right.getBlockId() == Constants.blockHandler.getBlockId("Flowing Water")) {

                    if (right.getSpecialValue() < 4) {
                        setBlockNoUpdate(Block.Air, x + 1, y, 1);
                        this.setBlock(Constants.blockHandler.getBlockId("Flowing Water"), x + 1, y, 1, waterTravel-1, false, false);
                    }
                    if (right.getSpecialValue() >= 4) {
                        sheduleBlockUpdate(x + 1, y, 1);
                    }

                } else if(Constants.blockHandler.breakableByWater(right.getBlockId())){
                    this.breakBlock(x+1,y,1, false);
                    this.setBlock(Constants.blockHandler.getBlockId("Flowing Water"), x + 1, y, 1, waterTravel-1, false, false);
                } else if(right.getBlockId() == Constants.blockHandler.getBlockId("Lava")){
                    this.setBlockNoUpdate(Block.Air, x+1, y, 1);
                    this.setBlock(Constants.blockHandler.getBlockId("Obsidian"), x+1, y, 1, 0, true, false);
                } else if(right.getBlockId() == Constants.blockHandler.getBlockId("Flowing Lava")){
                    this.setBlockNoUpdate(Block.Air, x+1, y, 1);
                    this.setBlock(Constants.blockHandler.getBlockId("Cobblestone"), x+1, y, 1, 0, true, false);
                }



            } else {
                if(down.getBlockId() == 0){
                    this.setBlock(Constants.blockHandler.getBlockId("Flowing Water"), x, y+1, 1, waterTravel, true, false);
                } else if(Constants.blockHandler.breakableByWater(down.getBlockId())){
                    this.breakBlock(x,y+1,1, false);
                    this.setBlock(Constants.blockHandler.getBlockId("Flowing Water"), x, y+1, 1, waterTravel, true, false);
                } else if(down.getBlockId() == Constants.blockHandler.getBlockId("Lava")){
                    this.setBlockNoUpdate(Block.Air, x, y+1, 1);
                    this.setBlock(Constants.blockHandler.getBlockId("Obsidian"), x, y+1, 1, 0, true, false);
                } else if(down.getBlockId() == Constants.blockHandler.getBlockId("Flowing Lava")){
                    this.setBlockNoUpdate(Block.Air, x, y+1, 1);
                    this.setBlock(Constants.blockHandler.getBlockId("Cobblestone"), x, y+1, 1, 0, true, false);
                }

            }

        } else if(b.getBlockId() == Constants.blockHandler.getBlockId("Flowing Water") && layer == 1){
            Block left = getBlock(x-1,y);
            Block right = getBlock(x+1,y);
            Block down = getBlock(x,y+1);
            Block up = getBlock(x,y-1);


            int flowing = Constants.blockHandler.getBlockId("Flowing Water");
            int water = Constants.blockHandler.getBlockId("Water");

            if(left.getBlockId() == Constants.blockHandler.getBlockId("Water") && right.getBlockId() == Constants.blockHandler.getBlockId("Water")){
                setBlockNoUpdate(Block.Air, x, y,1);
                setBlockNoUpdate(Constants.blockHandler.getBlockId("Water"), x, y,1);
            }

            int nextSpecial = b.getSpecialValue() - 1;

            b = getBlock(x, y);


            if (b.getBlockId() == flowing) {
                int nearVal = 0;

                if (up.getBlockId() == water) nearVal = waterTravel + 1;
                if (left.getBlockId() == water) nearVal = waterTravel + 1;
                if (right.getBlockId() == water) nearVal = waterTravel + 1;
                if (up.getBlockId() == flowing) nearVal = waterTravel + 1;

                if (left.getBlockId() == flowing && left.getSpecialValue() > nearVal)
                    nearVal = left.getSpecialValue();
                if (right.getBlockId() == flowing && right.getSpecialValue() > nearVal)
                    nearVal = right.getSpecialValue();

                if (b.getSpecialValue() >= nearVal) {

                    int sVal = b.getSpecialValue();
                    if (sVal - 4 <= 0) {
                        this.setBlock(Block.Air, x, y, 1, 0, true, false);
                    } else {
                        setBlockNoUpdate(0, x, y, 1);

                        this.setBlock(Constants.blockHandler.getBlockId("Flowing Water"), x, y, 1, sVal - 4, true, false);
                    }
                }


            }

            b = getBlock(x, y);

            if (b.getBlockId() == flowing) {


                boolean val = true;


                if (down.getSolidType() == BlockSolidType.Solid) {
                    if (left.getBlockId() == Block.Air && nextSpecial > 0) {
                        setBlock(Constants.blockHandler.getBlockId("Flowing Water"), x - 1, y, 1, nextSpecial, true, false);
                        val = false;
                    } else if(Constants.blockHandler.breakableByWater(left.getBlockId()) && nextSpecial > 0){
                        this.breakBlock(x-1,y,1, false);
                        setBlock(Constants.blockHandler.getBlockId("Flowing Water"), x - 1, y, 1, nextSpecial, true, false);
                        val = false;
                    } else if(left.getBlockId() == Constants.blockHandler.getBlockId("Lava")){
                        this.setBlockNoUpdate(Block.Air, x-1, y, 1);
                        this.setBlock(Constants.blockHandler.getBlockId("Obsidian"), x-1, y, 1, 0, true, false);
                        val = false;
                    } else if(left.getBlockId() == Constants.blockHandler.getBlockId("Flowing Lava")){
                        this.setBlockNoUpdate(Block.Air, x-1, y, 1);
                        this.setBlock(Constants.blockHandler.getBlockId("Cobblestone"), x-1, y, 1, 0, true, false);
                        val = false;
                    }

                    if (right.getBlockId() == Block.Air && nextSpecial > 0) {
                        setBlock(Constants.blockHandler.getBlockId("Flowing Water"), x + 1, y, 1, nextSpecial, true, false);
                        val = false;
                    } else if(Constants.blockHandler.breakableByWater(right.getBlockId()) && nextSpecial > 0){
                        this.breakBlock(x+1,y,1, false);
                        setBlock(Constants.blockHandler.getBlockId("Flowing Water"), x + 1, y, 1, nextSpecial, true, false);
                        val = false;
                    } else if(right.getBlockId() == Constants.blockHandler.getBlockId("Lava")){
                        this.setBlockNoUpdate(Block.Air, x+1, y, 1);
                        this.setBlock(Constants.blockHandler.getBlockId("Obsidian"), x+1, y, 1, 0, true, false);
                        val = false;
                    } else if(right.getBlockId() == Constants.blockHandler.getBlockId("Flowing Lava")){
                        this.setBlockNoUpdate(Block.Air, x+1, y, 1);
                        this.setBlock(Constants.blockHandler.getBlockId("Cobblestone"), x+1, y, 1, 0, true, false);
                        val = false;
                    }


                    if (val) {

                        String dir = "";

                        if (left.getBlockId() == water || (left.getBlockId() == flowing && left.getSpecialValue() > b.getSpecialValue())) {
                            if (right.getBlockId() == Block.Air || (right.getBlockId() == flowing && right.getSpecialValue() < b.getSpecialValue())) {
                                dir = "right";
                            }
                        } else if (right.getBlockId() == water || (right.getBlockId() == flowing && right.getSpecialValue() > b.getSpecialValue())) {
                            if (left.getBlockId() == Block.Air || (left.getBlockId() == flowing && left.getSpecialValue() < b.getSpecialValue())) {
                                dir = "left";
                            }
                        }

                        if (dir.equals("right")) {

                            if (right.getBlockId() == flowing && right.getSpecialValue() < b.getSpecialValue() - 1) {
                                setBlock(Constants.blockHandler.getBlockId("Flowing Water"), x + 1, y, 1, nextSpecial, false, false);
                            }

                        } else if (dir.equals("left")) {
                            if (left.getBlockId() == flowing && left.getSpecialValue() < b.getSpecialValue() - 1) {
                                setBlock(Constants.blockHandler.getBlockId("Flowing Water"), x - 1, y, 1, nextSpecial, false, false);
                            }
                        }


                    }
                } else {

                    if (down.getBlockId() == 0) {
                        this.setBlock(Constants.blockHandler.getBlockId("Flowing Water"), x, y + 1, 1, waterTravel, true, false);
                    } else if(Constants.blockHandler.breakableByWater(down.getBlockId())){
                        this.breakBlock(x,y+1,1, false);
                        this.setBlock(Constants.blockHandler.getBlockId("Flowing Water"), x, y + 1, 1, waterTravel, true, false);

                    } else if(down.getBlockId() == Constants.blockHandler.getBlockId("Lava")){
                        this.setBlockNoUpdate(Block.Air, x, y+1, 1);
                        this.setBlock(Constants.blockHandler.getBlockId("Obsidian"), x, y + 1, 1, 0, true, false);
                    } else if(down.getBlockId() == Constants.blockHandler.getBlockId("Flowing Lava")){
                        this.setBlockNoUpdate(Block.Air, x, y+1, 1);
                        this.setBlock(Constants.blockHandler.getBlockId("Cobblestone"), x, y + 1, 1, 0, true, false);
                    } else {
                        if (down.getBlockId() == flowing && down.getSpecialValue() < waterTravel) {
                            setBlockNoUpdate(0, x, y + 1, 1);
                            this.setBlock(Constants.blockHandler.getBlockId("Flowing Water"), x, y + 1, 1, waterTravel, true, false);
                        }
                    }
                }

                if (b.getBlockId() == flowing && b.getSpecialValue() <= 0) {
                    setBlock(Block.Air, x, y, 1, 0, false, true);
                }
            }

        } else if(b.getBlockId() == Constants.blockHandler.getBlockId("Lava")  && layer == 1) {
            //Flowing Water
            Block left = getBlock(x-1,y);
            Block right = getBlock(x+1,y);
            Block down = getBlock(x,y+1);


            if(down.getSolidType() == BlockSolidType.Solid) {
                if (left.getBlockId() == 0) {
                    this.setBlock(Constants.blockHandler.getBlockId("Flowing Lava"), x - 1, y, 1, lavaTravel-1, true);
                } else if (left.getBlockId() == Constants.blockHandler.getBlockId("Flowing Lava")) {
                    if (left.getSpecialValue() < 4) {
                        setBlockNoUpdate(Block.Air, x-1, y, 1);
                        this.setBlock(Constants.blockHandler.getBlockId("Flowing Lava"), x - 1, y, 1, lavaTravel-1, false, false);
                    }

                    if (left.getSpecialValue() >= 4) {
                        sheduleBlockUpdate(x - 1, y, 1);
                    }

                } else if(Constants.blockHandler.breakableByWater(left.getBlockId())){

                    this.breakBlock(x-1,y,1, false);
                    this.setBlock(Constants.blockHandler.getBlockId("Flowing Lava"), x - 1, y, 1, lavaTravel-1, true);
                }

                if (right.getBlockId() == 0) {
                    this.setBlock(Constants.blockHandler.getBlockId("Flowing Lava"), x + 1, y, 1, lavaTravel-1, false, false);
                } else if (right.getBlockId() == Constants.blockHandler.getBlockId("Flowing Lava")) {

                    if (right.getSpecialValue() < lavaTravel-1) {
                        setBlockNoUpdate(Block.Air, x + 1, y, 1);
                        this.setBlock(Constants.blockHandler.getBlockId("Flowing Lava"), x + 1, y, 1, lavaTravel-1, false, false);
                    }
                    if (right.getSpecialValue() >= lavaTravel-1) {
                        sheduleBlockUpdate(x + 1, y, 1);
                    }

                } else if(Constants.blockHandler.breakableByWater(right.getBlockId())){
                    this.breakBlock(x+1,y,1, false);
                    this.setBlock(Constants.blockHandler.getBlockId("Flowing Lava"), x + 1, y, 1, lavaTravel-1, false, false);
                }
            } else {
                if(down.getBlockId() == 0){
                    this.setBlock(Constants.blockHandler.getBlockId("Flowing Lava"), x, y+1, 1, lavaTravel, true, false);
                } else if(Constants.blockHandler.breakableByWater(down.getBlockId())){
                    this.breakBlock(x,y+1,1, false);
                    this.setBlock(Constants.blockHandler.getBlockId("Flowing Lava"), x, y+1, 1, lavaTravel, true, false);
                }

            }

        } else if(b.getBlockId() == Constants.blockHandler.getBlockId("Flowing Lava") && layer == 1){
            Block left = getBlock(x-1,y);
            Block right = getBlock(x+1,y);
            Block down = getBlock(x,y+1);
            Block up = getBlock(x,y-1);


            int flowing = Constants.blockHandler.getBlockId("Flowing Lava");
            int water = Constants.blockHandler.getBlockId("Lava");

            int nextSpecial = b.getSpecialValue() - 1;

            b = getBlock(x, y);


            if (b.getBlockId() == flowing) {
                int nearVal = 0;

                if (up.getBlockId() == water) nearVal = lavaTravel + 1;
                if (left.getBlockId() == water) nearVal = lavaTravel + 1;
                if (right.getBlockId() == water) nearVal = lavaTravel + 1;
                if (up.getBlockId() == flowing) nearVal = lavaTravel + 1;

                if (left.getBlockId() == flowing && left.getSpecialValue() > nearVal)
                    nearVal = left.getSpecialValue();
                if (right.getBlockId() == flowing && right.getSpecialValue() > nearVal)
                    nearVal = right.getSpecialValue();

                if (b.getSpecialValue() >= nearVal) {

                    int sVal = b.getSpecialValue();
                    if (sVal - 4 <= 0) {
                        this.setBlock(Block.Air, x, y, 1, 0, true, false);
                    } else {
                        setBlockNoUpdate(0, x, y, 1);

                        this.setBlock(Constants.blockHandler.getBlockId("Flowing Lava"), x, y, 1, sVal - 4, true, false);
                    }
                }


            }

            b = getBlock(x, y);

            if (b.getBlockId() == flowing) {


                boolean val = true;


                if (down.getSolidType() == BlockSolidType.Solid) {
                    if (left.getBlockId() == Block.Air && nextSpecial > 0) {
                        setBlock(Constants.blockHandler.getBlockId("Flowing Lava"), x - 1, y, 1, nextSpecial, true, false);
                        val = false;
                    } else if(Constants.blockHandler.breakableByWater(left.getBlockId()) && nextSpecial > 0){
                        this.breakBlock(x-1,y,1, false);
                        setBlock(Constants.blockHandler.getBlockId("Flowing Lava"), x - 1, y, 1, nextSpecial, true, false);
                        val = false;
                    }

                    if (right.getBlockId() == Block.Air && nextSpecial > 0) {
                        setBlock(Constants.blockHandler.getBlockId("Flowing Lava"), x + 1, y, 1, nextSpecial, true, false);
                        val = false;
                    } else if(Constants.blockHandler.breakableByWater(right.getBlockId()) && nextSpecial > 0){
                        this.breakBlock(x+1,y,1, false);
                        setBlock(Constants.blockHandler.getBlockId("Flowing Lava"), x + 1, y, 1, nextSpecial, true, false);
                        val = false;
                    }


                    if (val) {

                        String dir = "";

                        if (left.getBlockId() == water || (left.getBlockId() == flowing && left.getSpecialValue() > b.getSpecialValue())) {
                            if (right.getBlockId() == Block.Air || (right.getBlockId() == flowing && right.getSpecialValue() < b.getSpecialValue())) {
                                dir = "right";
                            }
                        } else if (right.getBlockId() == water || (right.getBlockId() == flowing && right.getSpecialValue() > b.getSpecialValue())) {
                            if (left.getBlockId() == Block.Air || (left.getBlockId() == flowing && left.getSpecialValue() < b.getSpecialValue())) {
                                dir = "left";
                            }
                        }

                        if (dir.equals("right")) {

                            if (right.getBlockId() == flowing && right.getSpecialValue() < b.getSpecialValue() - 1) {
                                setBlock(Constants.blockHandler.getBlockId("Flowing Lava"), x + 1, y, 1, nextSpecial, false, false);
                            }

                        } else if (dir.equals("left")) {
                            if (left.getBlockId() == flowing && left.getSpecialValue() < b.getSpecialValue() - 1) {
                                setBlock(Constants.blockHandler.getBlockId("Flowing Lava"), x - 1, y, 1, nextSpecial, false, false);
                            }
                        }


                    }
                } else {

                    if (down.getBlockId() == 0) {
                        this.setBlock(Constants.blockHandler.getBlockId("Flowing Lava"), x, y + 1, 1, lavaTravel, true, false);
                    } else if(Constants.blockHandler.breakableByWater(down.getBlockId())){
                        this.breakBlock(x,y+1,1, false);
                        this.setBlock(Constants.blockHandler.getBlockId("Flowing Lava"), x, y + 1, 1, lavaTravel, true, false);

                    } else {
                        if (down.getBlockId() == flowing && down.getSpecialValue() < lavaTravel) {
                            setBlockNoUpdate(0, x, y + 1, 1);
                            this.setBlock(Constants.blockHandler.getBlockId("Flowing Lava"), x, y + 1, 1, lavaTravel, true, false);
                        }
                    }
                }

                if (b.getBlockId() == flowing && b.getSpecialValue() <= 0) {
                    setBlock(Block.Air, x, y, 1, 0, false, true);
                }
            }

        }






    }

    public void generateCactus(int x, int y){
        for (int i = 0; i < 3; i++) {
            this.setBlockNoUpdate(Constants.blockHandler.getBlockId("Cactus"), x,y-i, 1);
        }
    }

    public void generateTree(int x, int y, int type){

        int baseId = Constants.blockHandler.getBlockId("Oak Wood");
        int baseLeave = Constants.blockHandler.getBlockId("Oak Leaves");
        if (type == 1) baseId = Constants.blockHandler.getBlockId("Birch Wood");
        if (type == 1) baseLeave = Constants.blockHandler.getBlockId("Birch Leaves");

        int height = Constants.randomNumber(5,10);
        for (int i = 0; i < height; i++) {
            this.setBlockNoUpdate(baseId, x,y-i, 0);
        }

        for (int i = -3; i < 2; i++) {
            for (int j = -3; j < 2; j++) {
                int x_ = x + i+1;
                int y_ = y - height+1 + j;

                if ((i == -3 && j == -3) || (i == 1 && j == -3) || (i == -3 && j == 1) || (i == 1 && j == 1)){

                }else {


                    if (this.getBlock(x_, y_, 1).getBlockId() == Block.Air) {
                        this.setBlockNoUpdate(baseLeave, x_, y_, 1);
                    }

                    if ((x_ == x) && y_ >= y - height) {
                        this.setBlockNoUpdate(baseId, x_, y_, 0);
                    } else {
                        this.setBlockNoUpdate(baseLeave, x_, y_, 0);
                    }
                }

            }
        }

        /*for (int y_ = -3; y_ < 1; y_++) {
            for (int x_ = -2; x_ < 3; x_++) {

                int


                Block u = this.getBlock(x + x_, y + y_ - height+1, 0);
                Block b = this.getBlock(x + x_, y + y_ - height+1, 1);

                if (u.getBlockId() == Block.Air) {
                    //this.setBlockNoUpdate(Block.Oak_Leaf, x + x_, y + y_ - height+1, 0);
                    if(x_ == 0 && y < 0){
                        this.setBlockNoUpdate(baseId, x + x_, y + y_ - height+1, 0);
                    }
                }

                if (b.getBlockId() == Block.Air) {
                    this.setBlockNoUpdate(Block.Oak_Leaf, x + x_, y + y_ - height+1, 1);

                }



            }
        }*/



    }

    public void explode(int x, int y, int radius){

        for (int i = 0; i < radius*2; i++) {
            for (int j = 0; j < radius*2; j++) {
                int x_ = x - radius + i;
                int y_ = y - radius + j;

                //
                double distance = Constants.distance(x,y,x_,y_);
                if (distance <= radius){
                    Block b = getBlock(x_, y_);
                    if (b.getBlockId() == Constants.blockHandler.getBlockId("Tnt")){
                        this.addEntity(Constants.GetChunkFromPos(x_*16 + 8), new BlockExplosive(x_*16 + 8, y_ * 16 + 8, this));
                    }

                    boolean up = distance == radius;
                    if(b.getBlockId() != Block.Air) {
                        this.addEmitter(ParticleFactory.BlockParticle(x_ * 16 + 8, y_ * 16 + 8, b.getBlockId()));

                        BlockDrop drop = Constants.blockHandler.getBlockDrop(b.getBlockId());
                        if(!(drop.getId() == 0 && !drop.isItem())){
                            int num = drop.getNum();
                            if(num > 0) {
                                InventoryItem item = new InventoryItem(drop.getId(), num, drop.isItem());

                                int offX = 0;
                                int rand = Constants.randomNumber(0,5);
                                if(Constants.randomNumber(0,100) < 50){
                                    offX = -rand;
                                } else {
                                    offX = rand;
                                }
                                DropEntity e = new DropEntity(x_ * 16 + 8 + offX, y_ * 16, this, item);
                                addEntity(0, e);
                                e.getVelocity().set(0, -20);

                                //player.getInventory().addItem(drop.getId(), num, drop.isItem());
                            }
                        }
                    }


                    this.setBlock(Block.Air, x_, y_, 1, 0, false, !up);


                }
            }

        }
        this.addEmitter(x*16 + 8, y * 16+8, ParticleType.Explosion2);
        this.addEmitter(x*16 + 8, y * 16+8, ParticleType.Explosion);

        for (int i = 0; i < entities.size()+1; i++) {
            Entity e;
            if(i == entities.size()) {
                e = player;
            } else {
                e = entities.get(i);
            }


            double xx = e.getPosition().getX();
            double yy = e.getPosition().getY();

            Vector2d thatVec = new Vector2d(e.getPosition());
            Vector2d thisVec = new Vector2d(x * 16 + 8, y * 16 + 8);

            thatVec.sub(thisVec);

            if(thatVec.length() < radius * 16){

                double val = (radius * 16 - thatVec.length()) / (radius * 16) * 400;


                thatVec.normalize();
                thatVec.scale(val);
                e.getVelocity().add(thatVec);

            }

        }




        updateLightLevel();

    }

    public void addEmitter(int x, int y, ParticleType type){
        this.particleEmitters.add(new ParticleEmitter(x, y, type));
    }

    public void addEmitter(ParticleEmitter emitter){
        this.particleEmitters.add(emitter);
    }

    public Chunk getChunk(int chunkX){
        return chunkMap.get(chunkX);
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public Camera getCamera() {
        return camera;
    }

    public MFrame getFrame() {
        return frame;
    }

    public MComponent getmComp() {
        return mComp;
    }

    public int getHandBlock() {
        return this.player.getInventory().getHandItem().getId();
    }

    public Player getPlayer() {
        return player;
    }
}
