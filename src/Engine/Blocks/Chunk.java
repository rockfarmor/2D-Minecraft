package Engine.Blocks;

import Engine.Constants;
import Engine.Particles.ParticleFactory;
import Engine.Particles.ParticleType;
import Engine.Renderer.Camera;
import Engine.World.World;
import Engine.Renderer.SpriteSheet;
import Engine.javax.vecmath.Vector2d;
import com.sun.org.apache.bcel.internal.classfile.ConstantString;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

public class Chunk {

    private Block[][][] chunkArray;
    private int chunkX;

    public static final int ChunkHeight = 512;
    public static final int ChunkWidth = 16;

    public static final int BaseHeight = ChunkHeight / 2;

    private BiomeType bType;

    private World state;

    private int[] topHeight;




    public Chunk(float[][] heightMap, float[][] heightMap2, float[][] treeNoise, int chunkX, HashMap<Integer, HashMap<Integer, HashMap<Integer, HashMap<Integer, Block>>>> placedblocks, World state){
        this.chunkX = chunkX;
        this.chunkArray = new Block[ChunkHeight][ChunkWidth][2];

        this.state = state;
        this.topHeight = new int[ChunkWidth];
        Arrays.fill(this.topHeight, ChunkHeight);

        generateChunk(heightMap, heightMap2, treeNoise, chunkX, placedblocks);

    }

    public void generateChunk(float[][] heightMap, float[][] heightMap2, float[][] treeNoise, int chunkX, HashMap<Integer, HashMap<Integer, HashMap<Integer, HashMap<Integer, Block>>>> placedBlocks){

        int midPoint = Constants.HeightNoiseNum / 2;

        int chX = chunkX / 4;


        double biomeVal = Constants.getNoise(heightMap, (int)(Constants.HeightNoiseNum / 2) + chunkX, 0);

        this.bType = Constants.getBiomeTypeFromVal(biomeVal);



        for (int x = 0; x < ChunkWidth; x++) {

            int x_ = Constants.ChunkPosToGlobal(x, chunkX);
            int midX =  midPoint + x_;



            /*double e = Constants.getSmoothenedNoise(heightMap, midPoint + chunkX * ChunkWidth + x, 0, 3);
            double e2 = Constants.getSmoothenedNoise(heightMap, (int)(midPoint + (chunkX * ChunkWidth)*3 + x), 2, 3);

            double elev = Math.pow(e, 2.1);
            double rough = Math.pow(e2, 0.4);
            double detail = Constants.getNoise(heightMap, midPoint - chunkX * ChunkWidth - x,1);



            //int height = (int)(Math.pow(e, 2.1) * 100);
            int dirtHeight = (int)(20 * Constants.getNoise(heightMap, midPoint + chunkX * ChunkWidth + x, 1));

            //int dirtLevel = (int)((elev + (rough * detail)) * 100);*/
            int dirtLevel = 0;



            double val = preCalcedVal(heightMap, midX, 6, 0.25);
            if (chunkX < 0) {
                //System.out.println(chunkX + " x : " + x + " val " + val);
            }



            int maxHeight = 100;
            int baseMod = 70;


            dirtLevel = (int)(val * maxHeight);




            dirtLevel = BaseHeight + baseMod - dirtLevel;

            if (dirtLevel > BaseHeight){
                bType = BiomeType.Ocean;
            }



            int dirtHeight = (int)(10 * Constants.getNoise(heightMap, midX, 1));










            int xHeight = dirtLevel;

            int bedRock = (int)(Constants.getNoise(treeNoise,chunkX*x + x,0)*4);
            for (int y = 0; y < ChunkHeight; y++) {


                int id = Block.Air;

                if(bType == BiomeType.Desert || bType == BiomeType.Mesa){
                    if (y >= dirtLevel) id = Constants.blockHandler.getBlockId("Sand");
                    if (y >= dirtLevel + dirtHeight) id = Constants.blockHandler.getBlockId("Sandstone");
                    if (y >= dirtLevel + 2 + dirtHeight) id = Constants.blockHandler.getBlockId("Stone");

                } /*else if (bType == BiomeType.Mesa){
                    if (y >= dirtLevel && y < dirtLevel + 2 + dirtHeight) {

                        id = Block.Terracotta;

                        int yV = y % 4;
                        switch (yV){
                            case 0:
                                id = Block.Terracotta;
                                break;
                            case 1:
                                id = Block.Terracotta;
                                break;
                            case 2:
                                id = Block.Terracotta_white;
                                break;
                            case 3:
                                id = Block.Terracotta_white;
                                break;
                        }


                    }

                    if (y >= dirtLevel + 2 + dirtHeight) id = Block.Stone;
                } */else {
                    if (y >= dirtLevel) id = Constants.blockHandler.getBlockId("Dirt");
                    if (y == dirtLevel && chunkArray[y-1][x][1].getBlockId() == Block.Air) {
                        id = Constants.blockHandler.getBlockId("Grass");
                    }
                    if (y >= dirtLevel + 2 + dirtHeight) id = Constants.blockHandler.getBlockId("Stone");
                }








                if (y >= ChunkHeight-1-bedRock) id = Constants.blockHandler.getBlockId("Bedrock");
                if (id == Block.Air){
                    if (y >= BaseHeight) id = Constants.blockHandler.getBlockId("Water");
                }

                if (id == Constants.blockHandler.getBlockId("Grass") || id == Constants.blockHandler.getBlockId("Dirt")) {
                    if (y >= dirtLevel && y < dirtLevel + 2 && xHeight >= BaseHeight - 1) id = Constants.blockHandler.getBlockId("Sand");
                    if (y >= dirtLevel && y < dirtLevel + 2 && xHeight >= BaseHeight + 3) id = Constants.blockHandler.getBlockId("Gravel");
                }


                int behindId = Constants.blockHandler.getBlockId("Stone");
                if (id == Block.Air){
                    behindId = Block.Air;
                } else if (id == Constants.blockHandler.getBlockId("Dirt")){
                    behindId = Constants.blockHandler.getBlockId("Dirt");
                } else if (id == Constants.blockHandler.getBlockId("Grass")){
                    behindId = Constants.blockHandler.getBlockId("Grass");
                } else if (id == Constants.blockHandler.getBlockId("Sand") || id == Constants.blockHandler.getBlockId("Sandstone")){
                    behindId = Constants.blockHandler.getBlockId("Sandstone");
                }



                this.setBlock(behindId, x, y,0);
                this.setBlock(id, x, y,1);
                if (id == Constants.blockHandler.getBlockId("Water")){
                    this.getBlock(x, y, 1).setSpecialValue(15);
                }

            }

            double flowerVal = Constants.getNoise(treeNoise,ChunkWidth * this.chunkX + x, 0);


            if (bType == BiomeType.Normal ||bType == BiomeType.Birch_Forest) {


                if (flowerVal < 0.1) {

                    int fId = (int)(flowerVal * 100);
                    if(fId > 9) fId = 9;

                    this.setBlock(84 + fId, x, xHeight - 1, 1);

                }

                if (flowerVal >= 0.3 && flowerVal < 0.8) {
                    if (this.getBlock(x, xHeight).getBlockId() == Constants.blockHandler.getBlockId("Grass")) {
                        this.setBlock(Constants.blockHandler.getBlockId("Tall Grass"), x, xHeight - 1, 1);
                    }
                }
                double tree = Constants.getNoise(treeNoise, chunkX * 5 + x, 0);

                if (placedBlocks.get(this.chunkX) == null) {
                    int realX = x_;
                    if (this.getBlock(x, xHeight).getBlockId() == Constants.blockHandler.getBlockId("Grass")) {
                        if (tree > 0.8) {

                            int treeType = 0;
                            if (bType == BiomeType.Birch_Forest) treeType = 1;

                            state.generateTree(realX, xHeight - 1, treeType);
                        }
                    }

                }
            } else if (bType == BiomeType.Desert || bType == BiomeType.Mesa){

                if (flowerVal < 0.1) {
                    this.setBlock(Constants.blockHandler.getBlockId("Dead Bush"), x, xHeight - 1, 1);
                }

                double tree = Constants.getNoise(treeNoise, chunkX * 5 + x, 0);

                if (placedBlocks.get(this.chunkX) == null && bType == BiomeType.Desert) {
                    int realX = x_;
                    if (this.getBlock(x, xHeight).getBlockId() == Constants.blockHandler.getBlockId("Sand")) {
                        if (tree > 0.8) {
                            state.generateCactus(realX, xHeight - 1);
                        }
                    }

                }




            }

            topHeight[x] = xHeight;


        }


        double ds = Constants.getNoise(treeNoise, chunkX*3,0);


        int numVeins = (int)(ds * 26) + 6;

        for (int i = 0; i < numVeins; i++) {


            double e = Constants.getNoise(treeNoise, i*chunkX * i*3,0);
            double e2 = Constants.getNoise(treeNoise,(int)(midPoint - (chunkX*3 + e*1000)) * i, 0);


            int x = (int)(e * 16);
            int y = (int)(e2 * 200);


            Block b = getBlock(x, ChunkHeight - y);
            if (b.getBlockId() == 1) {
                int num = Constants.blockHandler.getBlockId("Coal Ore");

                int y_ = ChunkHeight - y;

                int seaLevel = ChunkHeight / 2;

                double ee = Constants.getNoise(treeNoise, (int)((x + ChunkHeight * 16 + y) * e), 0);
                //double eee = Constants.getNoise(treeNoise, x * ChunkWidth * 16, 0);
                if (y_ >= seaLevel && ee >= 0.4 && ee <= 0.8){
                    //Iron
                    num = Constants.blockHandler.getBlockId("Iron Ore");
                }

                if (y_  >= ChunkHeight - 32 && ee >= 0.2 && ee <= 0.4){
                    //Gold
                    num = Constants.blockHandler.getBlockId("Gold Ore");
                }

                if (y_ >= ChunkHeight - 16 && ee < 0.2){
                    //Diamond
                    num = Constants.blockHandler.getBlockId("Diamond Ore");
                }



                int deltay = (int)(Constants.getNoise(treeNoise, x*y + x*num, 0)*4);
                int deltax = (int)(Constants.getNoise(treeNoise, x*y, 0)*4);

                int thickness = (int)(Constants.getNoise(treeNoise, x*y - x*num, 0)*3) + 1;

                if (Constants.getNoise(treeNoise, x,0) >= 0.5) deltay *= -1;
                if (Constants.getNoise(treeNoise, 10-x,0) >= 0.5) deltax *= -1;

                drawOres(0,0,deltax, deltay,thickness,x, ChunkHeight - y,num);



            }




        }


        // Cave generation
        for (int chuckOff = -5; chuckOff < 6; chuckOff++) {
            int cX = this.chunkX+chuckOff;
            ArrayList<Point[]> cave = caveGeneration(heightMap, treeNoise, cX);
            for (int i = 0; i < cave.size(); i++) {

                int thick = (int)cave.get(i)[0].getX();
                for (int j = 1; j < cave.get(i).length-2; j++) {

                    int offsetX = (cX - this.chunkX) * 16;


                    Point p1 = cave.get(i)[j];
                    Point p2 = cave.get(i)[j+1];
                    carveCave((int)p1.getX() + offsetX, (int)p1.getY(), (int)p2.getX() + offsetX, (int)p2.getY(),thick, 0);

                }

            }
        }

        if (placedBlocks.get(this.chunkX) != null){
            Iterator it = placedBlocks.get(this.chunkX).entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                HashMap<Integer, HashMap<Integer, Block>> map1 = placedBlocks.get(this.chunkX).get(pair.getKey());
                Iterator it2 = map1.entrySet().iterator();
                while (it2.hasNext()) {
                    Map.Entry pair2 = (Map.Entry)it2.next();

                    HashMap<Integer, Block> blockMap = placedBlocks.get(this.chunkX).get(pair.getKey()).get(pair2.getKey());



                    if (blockMap.get(1) != null){
                        Block b = blockMap.get(1);

                        int xx = (int)pair.getKey();
                        int yy = (int)pair2.getKey();

                        setBlock(b.getBlockId(), xx, yy, 1);
                    }

                    if (blockMap.get(0) != null){
                        Block b = blockMap.get(0);
                        int xx = (int)pair.getKey();
                        int yy = (int)pair2.getKey();
                        setBlock(b.getBlockId(), xx, yy, 0);
                    }


                }


                /*HashMap<Integer, Block> map1 = placedBlocks.get(this.chunkX).get(pair.getKey());
                Iterator it2 = map1.entrySet().iterator();
                while (it2.hasNext()) {
                    Map.Entry pair2 = (Map.Entry)it2.next();

                    Block b = (Block)pair2.getValue();
                    int xx = (int)pair.getKey();
                    int yy = (int)pair2.getKey();
                    setBlock(b.getBlockId(), xx, yy);

                }*/

            }

        } else {

        }

    }


    public ArrayList<Point[]> caveGeneration(float[][] heightMap, float[][] treeNoise, int cX){
        ArrayList<Point[]> points = new ArrayList<>();

        int numCaves = (int)(Math.round(Constants.getNoise(treeNoise, 2000 + cX*20, 0) * 2));


        for (int i = 0; i < numCaves; i++) {



            int x = (int)(Constants.getNoise(treeNoise, 132 + cX*i + i ,0) * 16);
            int y = ChunkHeight - (int)(Constants.getNoise(treeNoise, 513 + cX + i*i,0) * (ChunkHeight/2 + 100));


            int numWorms = (int)(Constants.getNoise(treeNoise, x*16 + y + 321,0) * 100)+1;
            int thickness = 6;

            Point[] point = new Point[numWorms+1+1];

            int currentX = x;
            int currentY = y;

            point[0] = new Point(thickness,0);
            point[1] = new Point(currentX, currentY);


            for (int j = 0; j < numWorms; j++) {

                int xj = (int)(Constants.getNoise(treeNoise, x*cX + j,0) * 10);
                int yj = (int)(Constants.getNoise(treeNoise, y + xj +j,0) * 5);

                if ((Constants.getNoise(treeNoise, xj + j,0)) > 0.5){
                    xj *= -1;
                }

                if ((Constants.getNoise(treeNoise, yj + j,0)) > 0.7){
                    yj *= -1;
                    xj = (int)(xj / 4);
                }

                currentX += xj;
                currentY += yj;

                point[j+1] = new Point(currentX, currentY);

            }
            points.add(point);


        }

        return points;

    }


    public void renderChunk(Graphics2D g2d, Vector2d renderPoint, World world){

        int playerY = (int)(renderPoint.getY() / 16);

        //int renderXChunk = Constants.GetChunkFromPos(renderPoint);

        int playerX = ((int)renderPoint.getX() / 16);

        int screenWidth = world.getFrame().getWidth();
        int screenHeight = world.getFrame().getHeight();

        Camera camera = world.getCamera();

        double xScale = camera.getScale().getX();
        double yScale = camera.getScale().getY();

        int boxWidth = (int)(16 * xScale);
        int boxHeight = (int)(16 * yScale);

        int numWidth = (int)((screenWidth / boxWidth) / 2)+4;
        int numHeight = (int)((screenHeight / boxHeight) / 2)+7;

        int w = 16;
        int h = 16;

        Composite originalComposite = g2d.getComposite();

        for (int y = playerY + numHeight-1; y >= playerY - numHeight; y--) {
            for (int x = 0; x < ChunkWidth; x++) {

                if (y >= 0 && y < ChunkHeight) {

                    int globalPoint = Constants.ChunkPosToGlobal(x, this.chunkX);
                    if (Math.abs(globalPoint - playerX) < numWidth){


                        Block b = getBlock(x, y);


                        if(b.getBlockId() == Constants.blockHandler.getBlockId("Torch")){
                            if (Constants.randomNumber(0, 1000) < 50){
                                world.addEmitter((int)globalPoint * 16 + 8, y * 16 + 5, ParticleType.TorchParticle);
                            }
                        }

                        if(Constants.blockHandler.getBlockType(b.getBlockId()) == BlockType.Leaf){
                            if (Constants.randomNumber(0, 1000) < 1){
                                world.addEmitter(ParticleFactory.LeafeParticle((int)globalPoint * 16 + 8, y * 16 + 5));
                            }
                        }


                        int x_ = x * w + this.chunkX * w * ChunkWidth;
                        int y_ = y * h;

                        if (getBlock(x, y - 1).transparant()) {
                            int alpha = 255 - (int) (255 * b.getMaxLightLevel(state.getDayLight()) / 16);
                            alpha += 120;
                            if (alpha > 255) alpha = 255;

                            Block bBehind = getBlock(x, y - 1, 0);
                            if (bBehind.getBlockId() > 0) {

                                BlockInfo bH =  Constants.blockHandler.getBlockInfo(bBehind.getBlockId());

                                BufferedImage bImg = bH.img;
                                BufferedImage bImgShadow = bH.shadowImage;

                                g2d.drawImage(bImg, x_, y_ - 16, null);

                                int rule = AlphaComposite.SRC_OVER;
                                Composite comp = AlphaComposite.getInstance(rule , (float)(alpha / 255.0));
                                g2d.setComposite(comp );
                                g2d.drawImage(bImgShadow, x_, y_-16, 16, 16, null);
                                g2d.setComposite(originalComposite);

                            }
                        }

                        if (b.getBlockId() > 0) {
                            BlockInfo binfo =  Constants.blockHandler.getBlockInfo(b.getBlockId());

                            int off = 0;
                            int heightOff = 0;
                            BufferedImage img = binfo.getImg();
                            BufferedImage shadowImg = binfo.shadowImage;

                            if(binfo.isSlab){
                                off = 8;
                                img = binfo.img.getSubimage(0,8,16,8);
                                shadowImg = binfo.shadowImage.getSubimage(0,8,16,8);
                                heightOff = 8;
                            }

                            if(b.getBlockId() == Constants.blockHandler.getBlockId("Flowing Water") && b.getSpecialValue() > 0){
                                int specialValue = b.getSpecialValue();

                                double heightVal = (specialValue / (double)Constants.WaterTravel) * 16;

                                int hei = (int)heightVal;

                                img = img.getSubimage(0,0,16, hei);
                                shadowImg = binfo.shadowImage.getSubimage(0,0,16,hei);
                                off = 16 - hei;
                                heightOff = 16 - hei;

                            } else if(b.getBlockId() == Constants.blockHandler.getBlockId("Flowing Lava") && b.getSpecialValue() > 0){
                                int specialValue = b.getSpecialValue();

                                double heightVal = (specialValue / (double)Constants.LavaTravel) * 16;

                                int hei = (int)heightVal;

                                img = img.getSubimage(0,0,16, hei);
                                shadowImg = binfo.shadowImage.getSubimage(0,0,16,hei);
                                off = 16 - hei;
                                heightOff = 16 - hei;

                            }





                            int alpha = 255 - (int) (255 * b.getMaxLightLevel(state.getDayLight()) / 16);
                            boolean highBlock = getBlock(x, y - 1).transparant() && b.getTopImage() != null;
                            highBlock = highBlock || (!getBlock(x, - 1).transparant() && binfo.isSlab);
                            if(alpha < 255) {
                                g2d.drawImage(img, x_, y_ + off, null);
                            }


                            int nextAlpha = alpha + 70;
                            if (nextAlpha > 255) nextAlpha = 255;


                            g2d.setColor(new Color(0, 0, 0, nextAlpha));

                            if (highBlock && binfo.topImage != null) {
                                BufferedImage img2 = binfo.topImage;

                                g2d.drawImage(img2, x_, y_ - 4 + off, 16, 4, null);
                                int rule = AlphaComposite.SRC_OVER;
                                Composite comp = AlphaComposite.getInstance(rule , (float)(nextAlpha / 255.0));
                                g2d.setComposite(comp );
                                g2d.drawImage(binfo.shadowImageTop, x_, y_ - 4 + off, 16, 4, null);
                                g2d.setComposite(originalComposite);

                            }


                            Color c = new Color(0, 0, 0, alpha);

                            g2d.setColor(c);

                            int rule = AlphaComposite.SRC_OVER;
                            Composite comp = AlphaComposite.getInstance(rule , (float)(alpha / 255.0));
                            g2d.setComposite(comp );
                            g2d.drawImage(shadowImg, x_, y_ + off, 16, 16 - heightOff, null);
                            g2d.setComposite(originalComposite);

                            //Flowing water
                            //if(b.getBlockId() == 26){
                            //    g2d.setColor(Color.RED);
                            //   g2d.drawString("" + b.getSpecialValue(), x_, y_+5);
                            //}


                        }
                    }
                }

            }
        }

    }




    public Block getBlock(int x, int y){
        return getBlock(x, y,1);
    }

    public Block getBlock(int x, int y, int z){
        if (z < 0 || z >= 2) return new Block(0);

        if (x >= 0 && y >= 0 && x < ChunkWidth && y < ChunkHeight){
            return this.chunkArray[y][x][z];
        }

        if (x < 0 && y >= 0 && x >= -ChunkWidth && y < ChunkHeight){
            int leftChunk = this.chunkX -1;
            Chunk c = state.getChunk(leftChunk);

            if (c != null){
                return c.getBlock(x + ChunkWidth, y, z);
            }
        }
        if (x >= ChunkWidth && y >= 0 && x < ChunkWidth*2 && y < ChunkHeight){
            int rightChunk = this.chunkX+1;
            Chunk c = state.getChunk(rightChunk);
            if (c != null){
                return c.getBlock(x - ChunkWidth, y, z);
            }

        }
        return new Block(0);
    }

    public void setBlock(int id, int x, int y){
        this.setBlock(id, x, y, 1);
    }

    public void setBlock(int id, int x, int y, int z){
        if (x >= 0 && y >= 0 && x < ChunkWidth && y < ChunkHeight && z >= 0 && z < 2){
            this.chunkArray[y][x][z] = new Block(id);
        }
    }



    private void carveCave(int x, int y, int x2, int y2, int thickness, int block){
        int w = x2 - x ;
        int h = y2 - y ;
        int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0 ;
        if (w<0) dx1 = -1 ; else if (w>0) dx1 = 1 ;
        if (h<0) dy1 = -1 ; else if (h>0) dy1 = 1 ;
        if (w<0) dx2 = -1 ; else if (w>0) dx2 = 1 ;
        int longest = Math.abs(w) ;
        int shortest = Math.abs(h) ;
        if (!(longest>shortest)) {
            longest = Math.abs(h) ;
            shortest = Math.abs(w) ;
            if (h<0) dy2 = -1 ; else if (h>0) dy2 = 1 ;
            dx2 = 0 ;
        }
        int numerator = longest >> 1 ;
        for (int i=0;i<=longest;i++) {

            for (int j = 0; j < thickness; j++) {
                for (int k = 0; k < thickness; k++) {


                    if (getDistance(x,y,x+j, y+k) <= 3) {

                        Block b = getBlock(+x + j, y + k);
                        if (b.getBlockId() <= 3) {
                            int blockId = block;
                            b.setBlockId(blockId);
                        }

                    }
                }

            }

            numerator += shortest ;
            if (!(numerator<longest)) {
                numerator -= longest ;
                x += dx1 ;
                y += dy1 ;
            } else {
                x += dx2 ;
                y += dy2 ;
            }
        }

    }


    private void drawOres(int x, int y, int x2, int y2, int thickness, int realx, int realy, int ore){

        int w = x2 - x ;
        int h = y2 - y ;
        int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0 ;
        if (w<0) dx1 = -1 ; else if (w>0) dx1 = 1 ;
        if (h<0) dy1 = -1 ; else if (h>0) dy1 = 1 ;
        if (w<0) dx2 = -1 ; else if (w>0) dx2 = 1 ;
        int longest = Math.abs(w) ;
        int shortest = Math.abs(h) ;
        if (!(longest>shortest)) {
            longest = Math.abs(h) ;
            shortest = Math.abs(w) ;
            if (h<0) dy2 = -1 ; else if (h>0) dy2 = 1 ;
            dx2 = 0 ;
        }
        int numerator = longest >> 1 ;
        for (int i=0;i<=longest;i++) {

            for (int j = 0; j < thickness; j++) {
                for (int k = 0; k < thickness; k++) {
                    Block b = getBlock(realx + x +j, realy + y + k);
                    if (b.getBlockId() == Block.Stone)
                        b.setBlockId(ore);
                }

            }

            numerator += shortest ;
            if (!(numerator<longest)) {
                numerator -= longest ;
                x += dx1 ;
                y += dy1 ;
            } else {
                x += dx2 ;
                y += dy2 ;
            }
        }

    }

    public double getDistance(int x1, int y1, int x2, int y2){
        return Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
    }


    private double preCalcedVal(float[][] map, int x, int num, double freq){

        double value = 0;


        for (int i = -num; i < num+1; i++) {

            double heightLow = Constants.getNoise(map, x + i, 0);

            value += Math.pow(heightLow, freq);
        }

        return value / (1 + num*2);
    }


    public BiomeType getbType() {
        return bType;
    }
}
