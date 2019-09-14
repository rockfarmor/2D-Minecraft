package Engine.Blocks;

import Engine.Constants;
import Engine.Enum.BlockSolidType;
import com.sun.org.apache.bcel.internal.generic.LADD;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Block {

    private int blockId;
    private int lightLevel;
    private int lightLevel2;

    private int specialValue;


    public Block(int blockId){
        this.blockId = blockId;
        this.lightLevel = 0;
        this.specialValue = 0;
    }

    public Block(int blockId, int specialValue){
        this.blockId = blockId;
        this.lightLevel = 0;
        this.specialValue = specialValue;
    }

    public int getLightLevel() {
        return lightLevel;
    }

    public void setLightLevel(int lightLevel) {
        this.lightLevel = lightLevel;
    }

    public void setMaxLightLevel(int lightLevel){
        if (this.lightLevel < lightLevel){
            this.lightLevel = lightLevel;
        }
    }

    public int getLightLevel2() {
        return lightLevel2;
    }

    public void setLightLevel2(int lightLevel) {
        this.lightLevel2 = lightLevel;
    }

    public void setMaxLightLevel2(int lightLevel){
        if (this.lightLevel2 < lightLevel){
            this.lightLevel2 = lightLevel;
        }
    }

    public int getMaxLightLevel(){
        int val = this.lightLevel + this.lightLevel2;
        if (val > 16) val = 16;

        return val;
    }

    public int getMaxLightLevel(int daylight){
        int val = (int)(this.lightLevel * daylight / 16) + this.lightLevel2;
        if (val > 16) val = 16;

        return val;
    }



    public int getBlockId() {
        return blockId;
    }

    public void setBlockId(int blockId) {
        this.blockId = blockId;
    }

    public int getSpecialValue() {
        return specialValue;
    }

    public void setSpecialValue(int specialValue) {
        this.specialValue = specialValue;
        if (this.specialValue > 15) this.specialValue = 15;
        if (this.specialValue < 0) this.specialValue = 0;
    }

    public void addSpecialValue(int specialValue) {
        this.specialValue += specialValue;
        if (this.specialValue > 15) this.specialValue = 15;
        if (this.specialValue < 0) this.specialValue = 0;
    }

    public boolean isSlab(){
        if(Constants.blockHandler.getBlockInfo(this.blockId) == null)
            return false;
        return Constants.blockHandler.getBlockInfo(this.blockId).isSlab;
    }


    public boolean isLightSource(){
        if(Constants.blockHandler.getBlockInfo(this.blockId) == null)
            return false;
        return Constants.blockHandler.getBlockInfo(this.blockId).isLightSource;
    }

    public int getLightSourceLevel(){
        if (this.isLightSource()) return 8;
        return 0;
    }

    public int getTopBlockId(){

        return this.getBlockId();

    }

    public BufferedImage getTopImage(){
        if(Constants.blockHandler.getBlockInfo(this.blockId) == null)
            return null;
        return Constants.blockHandler.getBlockInfo(this.blockId).topImage;
    }


    public BlockSolidType getSolidType(){

        if(Constants.blockHandler.getBlockInfo(this.blockId) == null)
            return BlockSolidType.Air;
        return Constants.blockHandler.getBlockInfo(this.blockId).solidType;

        /*
        switch (this.getBlockId()){
            case Air:
                return BlockSolidType.Air;
            case Torch:
                return BlockSolidType.Air;
            case Flower_Poppy:
                return BlockSolidType.Air;
            case Flower_Daisy:
                return BlockSolidType.Air;
            case High_Grass:
                return BlockSolidType.Air;
            case Dead_shrub:
                return BlockSolidType.Air;
            case Ladder:
                return BlockSolidType.Ladder;
            case Cactus:
                return BlockSolidType.Air;
            case Water:
                return BlockSolidType.Fluid;
        }
        return BlockSolidType.Solid;*/
    }

    public boolean isFallingBlock(){

        if(Constants.blockHandler.getBlockInfo(this.blockId) == null)
            return false;
        return Constants.blockHandler.getBlockInfo(this.blockId).gravityBlock;
        /*
        switch (this.getBlockId()){
            case Sand:
                return true;
            case Gravel:
                return true;
        }

        return false;
        */
    }


    public boolean transparant(){
        if(Constants.blockHandler.getBlockInfo(this.blockId) == null)
            return true;
        return Constants.blockHandler.getBlockInfo(this.blockId).transparant;


        //return this.blockId == Air || this.blockId == Ladder  || this.blockId == Cactus || this.blockId == Water || this.blockId == Glass || this.blockId == Torch || this.blockId == Dead_shrub || this.blockId == Flower_Daisy || this.blockId == Flower_Poppy || this.blockId == High_Grass || this.blockId == Oak_Leaf;
    }

    public boolean renderAll(){

        if(Constants.blockHandler.getBlockInfo(this.blockId) == null)
            return true;
        return Constants.blockHandler.getBlockInfo(this.blockId).renderAll;

        /*
        if (this.getBlockId() == Torch) {
            return false;
        } else if (this.getBlockId() == High_Grass){
            return false;
        } else if (this.getBlockId() == Flower_Daisy){
            return false;
        } else if (this.getBlockId() == Flower_Poppy){
            return false;
        } else if (this.getBlockId() == Dead_shrub){
            return false;
        } else if (this.getBlockId() == Ladder){
            return false;
        } else if (this.getBlockId() == Oak_Leaf){
            return false;
        } else if (this.getBlockId() == Oak_sapling){
            return false;
        } else if (this.getBlockId() == Cactus){
            return false;
        }



        return true;*/
    }


    public static final int Air = 0;
    public static final int Stone = 1;
    public static final int Dirt = 2;
    public static final int Grass = 3;
    public static final int Plank = 4;
    public static final int Stone_Slab = 5;
    public static final int Bricks = 7;
    public static final int Tnt = 8;
    public static final int Cobweb = 11;
    public static final int Flower_Poppy = 12;
    public static final int Flower_Daisy = 13;

    public static final int Dead_shrub = 55 + 24;

    public static final int Cactus = 54 + 24 + 24;

    public static final int Red_sand = 14;
    public static final int Oak_sapling = 15;
    public static final int Bricks_slab = 16;
    public static final int Sandstone_slab = 17;
    public static final int Cobblestone_slab = 18;
    public static final int Cobblestone = 24;
    public static final int Bedrock = 25;
    public static final int Sand = 26;
    public static final int Gravel = 27;
    public static final int Oak_log = 28;
    public static final int Iron_block = 30;
    public static final int Gold_block = 31;
    public static final int Diamond_block = 32;

    public static final int High_Grass = 56;

    public static final int Oak_Leaf = 76;

    public static final int Ladder = 75 + 24*2;

    public static final int Sand_stone = 12 * 24;


    public static final int Glass = 73;

    //Light
    public static final int Torch = 144-24;



    //Ores
    public static final int Gold_ore = 48;
    public static final int Iron_ore = 49;
    public static final int Coal_ore = 50;
    public static final int Diamond_ore = 74;
    public static final int Redstone_ore = 75;

    public static final int Water = 442;

    //Terracotta

    public static final int Terracotta = 330;
    public static final int Terracotta_black = 331;
    public static final int Terracotta_purple = 332;
    public static final int Terracotta_brown = 333;
    public static final int Terracotta_green = 334;
    public static final int Terracotta_red = 335;

    public static final int Terracotta_darkbrown = 352;
    public static final int Terracotta_white = 353;

    public static final int Birch_log = 18*24 + 3;

}
