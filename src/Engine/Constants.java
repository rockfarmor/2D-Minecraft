package Engine;

import Engine.Blocks.*;
import Engine.Collision.BoxCollision;
import Engine.Debug.Debugger;
import Engine.FileHandler.FileInput;
import Engine.Gui.GuiHandler;
import Engine.Inventory.CraftingHandler;
import Engine.Math.BicubicInterpolation;
import Engine.Particles.ParticleHandler;
import Engine.javax.vecmath.Vector2d;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Constants {



    //Noise consts
    public final static int HeightNoiseNum = 200000;

    public final static int ChunksLoaded = 5;

    public final static int VerticalBlockRendeR = 20;
    public final static int HorizontalBlockRendeR = 5;

    public final static int BlockSize = 16;

    public final static int MaxCaveChunk = 3;

    public final static int WaterTravel = 7;
    public static int LavaTravel = 4;

    public final static double PlayerReach = 64;

    public final static BoxCollision TileCollision = new BoxCollision(16,16,0,0);
    public final static BoxCollision TileCollisionSlab = new BoxCollision(16,8,0,8);


    public final static boolean DebugDraw = false;

    public final static Debugger debugger = new Debugger();

    public final static BlockHandler blockHandler = new BlockHandler();
    public final static ItemHandler itemHandler = new ItemHandler();

    public final static ParticleHandler particleHandler = new ParticleHandler();

    public final static GuiHandler guiHandler = new GuiHandler();

    public final static CraftingHandler craftingHandler = new CraftingHandler();

    public final static BlockRuleHandler blockRuleHandler = new BlockRuleHandler();

    public final static BufferedImage LeatherArmor = FileInput.LoadImage("Assets/textures/models/armor/leather_layer_1.png");
    public final static BufferedImage ChainArmor = FileInput.LoadImage("Assets/textures/models/armor/chainmail_layer_1.png");
    public final static BufferedImage IronArmor = FileInput.LoadImage("Assets/textures/models/armor/iron_layer_1.png");
    public final static BufferedImage GoldArmor = FileInput.LoadImage("Assets/textures/models/armor/gold_layer_1.png");
    public final static BufferedImage DiamondArmor = FileInput.LoadImage("Assets/textures/models/armor/diamond_layer_1.png");


    //fonts
    public final static Font InventoryItemNumFont =new Font("Dialog", Font.PLAIN, 6);
    public final static Font InventoryItemNumFont2 =new Font("Dialog", Font.PLAIN, 12);


    public static BiomeType getBiomeTypeFromVal(double val){

        if (val >= 0.1 && val < 0.3){
            return BiomeType.Desert;
        } else if(val < 0.1){
            return  BiomeType.Mesa;
        } else {
            if (val >= 0.5 && val < 0.6){
                return BiomeType.Birch_Forest;
            }


            return BiomeType.Normal;
        }



    }



    public static double getNoise(float[][] noise, int x, int y){

        int width = noise.length;
        int height = noise[0].length;
        return noise[Math.abs(x % width)][Math.abs(y % height)];

    }

    public static double getNoise(float[][] noise, int x, int y, double freq){

        int width = noise.length;
        int height = noise[0].length;

        int x_ = (int) (x * freq);
        int y_ = (int) (y * freq);

        return noise[Math.abs(x_ % width)][Math.abs(y_ % height)];

    }


    public static double getSmoothenedNoise(float[][] noise, int x, int y, int num){
        int width = noise.length;
        int height = noise[0].length;

        double val = 0;

        for (int i = 1; i < num+1; i++) {
            val += 1/i * noise[(i * x) % width][(i * y) % height];
        }


        return val;
    }

    public static int GetChunkFromPos(double x){

        int chunkX = (int)(x / 16 / 16);

        if (x < 0){
            chunkX -= 1;
        }

        return chunkX;

    }

    public static int GetChunkFromPos(Vector2d pos){

        int chunkX = (int)(pos.getX() / 16 / 16);

        if (pos.getX() < 0){
            chunkX -= 1;
        }

        return chunkX;

    }

    public static Vector2d GetChunkPosFromGlobal(Vector2d pos){
        Vector2d p = new Vector2d();

        int x = (int)pos.getX() % 16;
        if (x < 0){
            x = 16 + x;
        }
        p.setX(x);

        p.setY(pos.getY());

        return p;

    }


    public static Vector2d PosToTilePos(Vector2d pos){
        int xx = (int)(pos.getX()/16);
        if (pos.getX() < 0){
            xx -= 1;
        }
        int yy = (int)(pos).getY()/16;
        return new Vector2d(xx, yy);

    }

    public static Vector2d PosToTilePos(double x, double y){
        int xx = (int)(x/16);
        if (x < 0){
            xx -= 1;
        }
        int yy = (int)y/16;
        return new Vector2d(xx, yy);

    }





    public static int ChunkPosToGlobal(int posX, int chunkX){

        Vector2d globalPos = new Vector2d();
        int xPos = 0;

        if (chunkX < 0){
            xPos = chunkX * Chunk.ChunkWidth + posX;
        } else {
            xPos = chunkX * Chunk.ChunkWidth + posX;
        }



        //int xPos = chunkX * Chunk.ChunkWidth;
        //xPos += pos.getX();





        return xPos;

    }

    public static Color blend(Color c0, Color c1) {
        double totalAlpha = c0.getAlpha() + c1.getAlpha();
        double weight0 = c0.getAlpha() / totalAlpha;
        double weight1 = c1.getAlpha() / totalAlpha;

        double r = weight0 * c0.getRed() + weight1 * c1.getRed();
        double g = weight0 * c0.getGreen() + weight1 * c1.getGreen();
        double b = weight0 * c0.getBlue() + weight1 * c1.getBlue();
        double a = Math.max(c0.getAlpha(), c1.getAlpha());

        return new Color((int) r, (int) g, (int) b, (int) a);
    }




    public static int randomNumber(int lowerBound, int upperBound){
        Random random = new Random();

        if(lowerBound == upperBound) return lowerBound;

        return random.nextInt(upperBound - lowerBound) + lowerBound;
    }


    public static double distance(double x1, double y1, double x2, double y2){
        return Math.sqrt((x2 - x1)*(x2 - x1) + (y2 - y1)*(y2 - y1));
    }

    public static double distance(Vector2d vec1, Vector2d vec2){
        return Math.sqrt((vec2.getX() - vec1.getX())*(vec2.getX() - vec1.getX()) + (vec2.getY() - vec1.getY())*(vec2.getY() - vec1.getY()));
    }

    public static BufferedImage shadowImage(int x0y0, int x1y0, int x2y0, int x0y1, int x1y1, int x2y1, int x0y2, int x1y2, int x2y2){
        BufferedImage img = new BufferedImage(16,16,BufferedImage.TYPE_INT_ARGB);
        int light = 16;
        int a = (255 - (255 * light ) / 16);


        double[] x1 = {0, 1, 2};
        double[] x2 = {0, 1, 2};
        double[][] ys = {
                {x0y0, x1y0, x2y0},
                {x0y1, x1y1, x2y1},
                {x0y2, x1y2, x2y2}
        };


        BicubicInterpolation bicubic = new BicubicInterpolation(x1, x2, ys);

        for (int y = 0; y < 16*3; y++) {
            for (int x = 0; x < 16*3; x++) {

                int realX = x - 16;
                int realY = y - 16;


                double xVal = x / (16.0*3.0) * 2;
                double yVal = y / (16.0*3.0) * 2;
                int alpha = (int)(255 - (255 * bicubic.interpolate(xVal, yVal) ) / 16);
                if(alpha < 0) alpha = 0;
                if(alpha > 255) alpha = 255;
                Color c = new Color(0,0,0,alpha);


                if(realX >= 0 && realX < 16 && realY >= 0 && realY < 16) {
                    img.setRGB(realX, realY,c.getRGB());
                }

            }
        }


        /*double[][] yy = new double[101][101];
        for (int i = 0; i <= 100; i++) {
            for (int j = 0; j <= 100; j++) {
                System.out.println(i * 0.03);
                yy[i][j] = bicubic.interpolate(i * 0.03, j * 0.03);
            }
        }

        for (int y = 0; y < img.getWidth(); y++) {
            for (int x = 0; x < img.getHeight(); x++) {
                int alpha = (int)((yy[y][x] / 6.0) * 255);
                if(alpha < 0) alpha = 0;
                if(alpha > 255) alpha = 255;

                Color c = new Color(0,0,0,alpha);
                img.setRGB(x,y,c.getRGB());



            }
        }*/






        return img;
    }


    public static double lerp(double v0, double v1, double t) {
        return v0 + t * (v1 - v0);
    }

    public static double interpolate(double x0, double y0, double x1, double y1, double x){
        //returns interpolated y for x;
        double y = 0;

        if(x0 == x1){
            return y0;
        }

        y = (y0 * (x1 - x) + y1*(x - x0)) / (x1 - x0);
        System.out.println(y + ", " + x0 + "," + y0 + ", " + x1 + "," + y1 + "," + x);
        return y;
    }

    public static double interpolate(double x0, double y0, double val0, double x1, double y1, double val1, double x, double y){
        //returns interpolated val for x;
        return (Constants.interpolate(x0, val0, x1, val1, x) + Constants.interpolate(y0, val0, y1, val1, y)) / 2.0;
    }



    public static double lerp(double x0, double y0, double x1, double y1, double t0, double t1){


        double valX = lerp(x0, x1, t0);
        double valY = lerp(y0, y1, t1);

        return (valX + valY) / 2;


    }








}
