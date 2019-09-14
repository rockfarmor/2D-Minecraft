package Engine.Blocks;

import Engine.Enum.BlockSolidType;
import Engine.FileHandler.FileInput;

import java.awt.image.BufferedImage;
import java.util.HashMap;

public class BlockHandler {



    private HashMap<Integer, BlockInfo> blockInfo;
    private HashMap<String, Integer> nameInt;

    private HashMap<Integer, BlockDrop> dropInfo;

    private HashMap<Integer, BlockType> blockType;
    private HashMap<Integer, MineTypes> mineType;

    public int maxId;

    private int internalClock;

    private BufferedImage[] destroy;


    public BlockHandler() {
        this.blockInfo = new HashMap<>();
        this.nameInt = new HashMap<>();
        this.blockType = new HashMap<>();
        this.dropInfo = new HashMap<>();
        this.mineType = new HashMap<>();
        this.maxId = 0;
        init();

        this.destroy = new BufferedImage[10];

        for (int i = 0; i < 10; i++) {
            this.destroy[i] = FileInput.LoadImage("Assets/textures/blocks/destroy_stage_" + i + ".png");
        }


        this.internalClock = 2000;


    }

    public BufferedImage getDestroy(int i){
        if(i >= 0 && i < this.destroy.length){
            return this.destroy[i];
        }
        return this.destroy[9];
    }


    public void update(){

    }


    public void addBlockInfo(int id, String name, String imgSource, boolean transparent, boolean renderAll, BlockSolidType bSolidType, boolean isLightSource){
        addBlockInfo(id, name, imgSource, transparent, renderAll, bSolidType, isLightSource, imgSource, false);
    }

    public void addBlockInfo(int id, String name, String imgSource, boolean transparent, boolean renderAll, BlockSolidType bSolidType, boolean isLightSource, boolean isSlab){
        addBlockInfo(id, name, imgSource, transparent, renderAll, bSolidType, isLightSource, imgSource, isSlab);
    }

    public void addBlockInfo(int id, String name, String imgSource, boolean transparent, boolean renderAll, BlockSolidType bSolidType, boolean isLightSource, String top){
        addBlockInfo(id, name, imgSource, transparent, renderAll, bSolidType, isLightSource, top, false);
    }

    public void addBlockType(int id, BlockType type){
        this.blockType.put(id, type);
    }

    public void addMineType(int id, MineTypes type) {
        this.mineType.put(id, type);
    }

    public void addBlockDrop(int id, int dropId, int numMin, int numMax, boolean isItem){
        this.dropInfo.put(id, new BlockDrop(dropId, numMin, numMax, isItem));
    }

    public void addBlockDrop(int id, int dropId, int num, boolean isItem){
        this.dropInfo.put(id, new BlockDrop(dropId, num, isItem));
    }

    public BlockType getBlockType(int id){
        if(this.blockType.get(id) != null) return this.blockType.get(id);
        return BlockType.Other;
    }

    public BlockDrop getBlockDrop(int id){
        if(this.dropInfo.get(id) != null) return this.dropInfo.get(id);
        return new BlockDrop(id, 1, false);
    }


    public void addBlockInfo(int id, String name, String imgSource, boolean transparent, boolean renderAll, BlockSolidType bSolidType, boolean isLightSource, String top, boolean isSlab){
        BlockInfo bInfo = new BlockInfo(id, name, imgSource, top, isSlab);
        bInfo.transparant = transparent;
        bInfo.renderAll = renderAll;
        bInfo.solidType = bSolidType;
        bInfo.isLightSource = isLightSource;
        this.blockInfo.put(id, bInfo);
        this.nameInt.put(name, id);

        if(id > this.maxId){
            this.maxId = id;
        }

    }

    public MineTypes getMineType(int id){
        if(this.mineType.get(id) != null) return this.mineType.get(id);
        return MineTypes.Wood;
    }




    public BlockInfo getBlockInfo(int id){
        if(blockInfo.get(id) == null){
            //System.out.println("GAME BROKEN" + id);
            return null;
        }
        return blockInfo.get(id);
    }

    public BlockInfo getBlockInfo(String name){
        if(nameInt.get(name) != null){
            return this.blockInfo.get(nameInt.get(name));
        }
        return null;
    }

    public int getBlockId(String name){
        if(this.nameInt.get(name) == null){
            System.out.println(name);
        }
        return this.nameInt.get(name);
    }



    public void init(){
        this.addBlockInfo(0, "Air", null, true, true, BlockSolidType.Air, false);
        this.addBlockInfo(1, "Stone", "stone.png", false, true, BlockSolidType.Solid, false);
        this.addBlockDrop(1, 12, 1, false);
        this.addBlockInfo(2, "Granite", "stone_granite.png", false, true, BlockSolidType.Solid, false);
        this.addBlockInfo(3, "Polished Granite", "stone_granite_smooth.png", false, true, BlockSolidType.Solid, false);
        this.addBlockInfo(4, "Diorite", "stone_diorite.png", false, true, BlockSolidType.Solid, false);
        this.addBlockInfo(5, "Polished Diorite", "stone_diorite_smooth.png" , false, true, BlockSolidType.Solid, false);
        this.addBlockInfo(6, "Andesite", "stone_andesite.png", false, true, BlockSolidType.Solid, false);
        this.addBlockInfo(7, "Polished Andesite", "stone_andesite_smooth.png", false, true, BlockSolidType.Solid, false);

        this.addMineType(1, MineTypes.Rock1);
        this.addMineType(2, MineTypes.Rock1);
        this.addMineType(3, MineTypes.Rock1);
        this.addMineType(4, MineTypes.Rock1);
        this.addMineType(5, MineTypes.Rock1);
        this.addMineType(6, MineTypes.Rock1);
        this.addMineType(7, MineTypes.Rock1);


        this.addBlockInfo(8, "Grass", "grass_side.png", false, true, BlockSolidType.Solid, false, "grass_top.png");
        this.addBlockDrop(8, 9, 1, false);
        this.addBlockInfo(9, "Dirt", "dirt.png",  false, true, BlockSolidType.Solid, false);
        this.addBlockInfo(10, "Coarse Dirt", "coarse_dirt.png", false, true, BlockSolidType.Solid, false);
        this.addBlockInfo(11, "Podzol", "dirt_podzol_side.png", false, true, BlockSolidType.Solid, false, "dirt_podzol_top.png");

        this.addMineType(8, MineTypes.Ground);
        this.addMineType(9, MineTypes.Ground);
        this.addMineType(10, MineTypes.Ground);
        this.addMineType(11, MineTypes.Ground);

        //this.addBlockInfo(12, "Cobblestone", "stone.png", false, true, BlockSolidType.Solid, false);
        this.addBlockInfo(12, "Cobblestone", "cobblestone.png", false, true, BlockSolidType.Solid, false, "cobblestone.png");
        this.addBlockInfo(13, "Mossy Cobblestone", "cobblestone_mossy.png", false, true, BlockSolidType.Solid, false,"cobblestone_mossy.png");

        this.addMineType(12, MineTypes.Rock1);
        this.addMineType(13, MineTypes.Rock1);

        this.addBlockInfo(14, "Oak Wood Plank", "planks_oak.png", false, true, BlockSolidType.Solid, false);
        this.addBlockInfo(15, "Spruce Wood Plank", "planks_spruce.png" , false, true, BlockSolidType.Solid, false);
        this.addBlockInfo(16, "Birch Wood Plank", "planks_birch.png", false, true, BlockSolidType.Solid, false);
        this.addBlockInfo(17, "Jungle Wood Plank", "planks_jungle.png", false, true, BlockSolidType.Solid, false);
        this.addBlockInfo(18, "Acacia Wood Plank", "planks_acacia.png", false, true, BlockSolidType.Solid, false);
        this.addBlockInfo(19, "Dark Oak Planks", "planks_big_oak.png",  false, true, BlockSolidType.Solid, false);

        addBlockType(14, BlockType.Planks);
        addBlockType(15, BlockType.Planks);
        addBlockType(16, BlockType.Planks);
        addBlockType(17, BlockType.Planks);
        addBlockType(18, BlockType.Planks);
        addBlockType(19, BlockType.Planks);

        this.addMineType(14, MineTypes.Wood);
        this.addMineType(15, MineTypes.Wood);
        this.addMineType(16, MineTypes.Wood);
        this.addMineType(17, MineTypes.Wood);
        this.addMineType(18, MineTypes.Wood);
        this.addMineType(19, MineTypes.Wood);



        this.addBlockInfo(20, "Oak Sapling", "sapling_oak.png", true, false, BlockSolidType.Air, false, null);
        this.addBlockInfo(21, "Spruce Sapling", "sapling_spruce.png", true, false, BlockSolidType.Air, false, null);
        this.addBlockInfo(22, "Birch Sapling", "sapling_birch.png", true, false, BlockSolidType.Air, false, null);
        this.addBlockInfo(23, "Jungle Sapling", "sapling_jungle.png", true, false, BlockSolidType.Air, false, null);
        this.addBlockInfo(24, "Acacia Sapling", "sapling_acacia.png", true, false, BlockSolidType.Air, false, null);
        this.addBlockInfo(25, "Dark Oak Sapling", "sapling_roofed_oak.png" , true, false, BlockSolidType.Air, false, null);

        this.addMineType(20, MineTypes.Plants2);
        this.addMineType(21, MineTypes.Plants2);
        this.addMineType(22, MineTypes.Plants2);
        this.addMineType(23, MineTypes.Plants2);
        this.addMineType(24, MineTypes.Plants2);
        this.addMineType(25, MineTypes.Plants2);


        this.addBlockInfo(26, "Flowing Water", "water_flow.png", true, true, BlockSolidType.Fluid, false);
        this.addBlockDrop(26, 0, 0, false);
        this.addBlockInfo(27, "Water", "water_still.png", true, true, BlockSolidType.Fluid, false);
        this.addBlockDrop(27, 0, 0, false);
        this.addBlockInfo(28, "Flowing Lava", "lava_flow.png", true, true, BlockSolidType.Fluid, true);
        this.addBlockDrop(28, 0, 0, false);
        this.addBlockInfo(29, "Lava", "lava_still.png",  true, true, BlockSolidType.Fluid, true);
        this.addBlockDrop(29, 0, 0, false);

        this.addMineType(26, MineTypes.Liquid);
        this.addMineType(27, MineTypes.Liquid);
        this.addMineType(28, MineTypes.Liquid);
        this.addMineType(29, MineTypes.Liquid);


        this.addBlockInfo(30, "Sand", "sand.png", false, true, BlockSolidType.Solid, false);
        this.addBlockInfo(31, "Red Sand", "red_sand.png", false, true, BlockSolidType.Solid, false);
        this.addBlockInfo(32, "Gravel", "gravel.png", false, true, BlockSolidType.Solid, false);

        this.addMineType(30, MineTypes.Ground);
        this.addMineType(31, MineTypes.Ground);
        this.addMineType(32, MineTypes.Ground);

        this.addBlockInfo(33, "Gold Ore", "gold_ore.png", false, true, BlockSolidType.Solid, false);
        this.addBlockInfo(34, "Iron Ore", "iron_ore.png", false, true, BlockSolidType.Solid, false);
        this.addBlockInfo(35, "Coal Ore", "coal_ore.png" , false, true, BlockSolidType.Solid, false);

        this.addMineType(33, MineTypes.Rock3);
        this.addMineType(34, MineTypes.Rock2);
        this.addMineType(35, MineTypes.Rock1);
        this.addBlockDrop(35, 45, 1,4, true);

        this.addBlockInfo(36, "Oak Wood", "log_oak.png", false, true, BlockSolidType.Solid, false, "log_oak_top.png");
        this.addBlockInfo(37, "Spruce Wood", "log_spruce.png", false, true, BlockSolidType.Solid, false, "log_spruce_top.png");
        this.addBlockInfo(38, "Birch Wood", "log_birch.png", false, true, BlockSolidType.Solid, false, "log_birch_top.png");
        this.addBlockInfo(39, "Jungle Wood", "log_jungle.png",  false, true, BlockSolidType.Solid, false, "log_jungle_top.png");

        this.addBlockInfo(40, "Acacia wood", "log_acacia.png", false, true, BlockSolidType.Solid, false, "log_acacia_top.png");
        this.addBlockInfo(41, "Dark Oak Wood", "log_big_oak.png", false, true, BlockSolidType.Solid, false, "log_big_oak_top.png");

        this.addMineType(36, MineTypes.Wood);
        this.addMineType(37, MineTypes.Wood);
        this.addMineType(38, MineTypes.Wood);
        this.addMineType(39, MineTypes.Wood);
        this.addMineType(40, MineTypes.Wood);
        this.addMineType(41, MineTypes.Wood);

        this.addBlockInfo(42, "Oak Leaves", "leaves_oak.png", true, false, BlockSolidType.Solid, false);
        this.addBlockInfo(43, "Spruce Leaves", "leaves_spruce.png", true, false, BlockSolidType.Solid, false);
        this.addBlockInfo(44, "Birch Leaves", "leaves_birch.png", true, false, BlockSolidType.Solid, false);
        this.addBlockInfo(45, "Jungle Leaves", "leaves_jungle.png", true, false, BlockSolidType.Solid, false);
        this.addBlockInfo(46, "Acacia Leaves", "leaves_acacia.png", true, false, BlockSolidType.Solid, false);
        this.addBlockInfo(47, "Dark Oak Leaves", "leaves_big_oak.png", true, false, BlockSolidType.Solid, false);

        this.addBlockType(42, BlockType.Leaf);
        this.addBlockType(43, BlockType.Leaf);
        this.addBlockType(44, BlockType.Leaf);
        this.addBlockType(45, BlockType.Leaf);
        this.addBlockType(46, BlockType.Leaf);
        this.addBlockType(47, BlockType.Leaf);

        this.addMineType(42, MineTypes.Leaves);
        this.addMineType(43, MineTypes.Leaves);
        this.addMineType(44, MineTypes.Leaves);
        this.addMineType(45, MineTypes.Leaves);
        this.addMineType(46, MineTypes.Leaves);
        this.addMineType(47, MineTypes.Leaves);

        this.addBlockInfo(48, "Sponge", "sponge.png", false, true, BlockSolidType.Solid, false);
        this.addBlockInfo(49, "Wet Sponge", "sponge_wet.png", false, true, BlockSolidType.Solid, false);

        this.addMineType(48, MineTypes.Other);
        this.addMineType(49, MineTypes.Other);

        this.addBlockInfo(50, "Glass", "glass.png", true, false, BlockSolidType.Solid, false);
        this.addMineType(50, MineTypes.Glass);

        this.addBlockInfo(51, "Lapis Lazuli Ore", "lapis_ore.png", false, true, BlockSolidType.Solid, false);
        this.addBlockInfo(52, "Lapis Lazuli Block", "lapis_block.png", false, true, BlockSolidType.Solid, false);

        this.addMineType(51, MineTypes.Rock2);
        this.addMineType(52, MineTypes.Metal2);

        this.addBlockInfo(53, "Sandstone", "sandstone_normal.png", false, true, BlockSolidType.Solid, false);
        this.addBlockInfo(54, "Chiseled Sandstone", "sandstone_carved.png", false, true, BlockSolidType.Solid, false, "sandstone_top.png");
        this.addBlockInfo(55, "Smooth Sandstone", "sandstone_smooth.png" , false, true, BlockSolidType.Solid, false, "sandstone_top.png");
        this.addBlockInfo(56, "Red Sandstone", "red_sandstone_normal.png", true, false, BlockSolidType.Solid, false);
        this.addBlockInfo(57, "Chiseled Red Sandstone", "red_sandstone_carved.png", true, false, BlockSolidType.Solid, false, "red_sandstone_top.png");
        this.addBlockInfo(58, "Smooth Red Sandstone", "red_sandstone_smooth.png", false, true, BlockSolidType.Solid, false, "red_sandstone_top.png");

        this.addMineType(53, MineTypes.Rock1);
        this.addMineType(54, MineTypes.Rock1);
        this.addMineType(55, MineTypes.Rock1);
        this.addMineType(56, MineTypes.Rock1);
        this.addMineType(57, MineTypes.Rock1);
        this.addMineType(58, MineTypes.Rock1);

        this.addBlockInfo(59, "Note Block", "noteblock.png", false, true, BlockSolidType.Solid, false);
        this.addMineType(59, MineTypes.Wood);

        this.addBlockInfo(60, "Bed", "", false, true, BlockSolidType.Air, false);
        this.addMineType(60, MineTypes.Other);
        this.addBlockInfo(61, "Sticky Piston", "piston_side.png", false, true, BlockSolidType.Solid, false, "piston_top_sticky.png");
        this.addBlockInfo(62, "Piston", "piston_side.png", false, true, BlockSolidType.Solid, false, "piston_top_normal.png");
        this.addMineType(61, MineTypes.Piston);
        this.addMineType(62, MineTypes.Piston);

        this.addBlockInfo(63, "Cobweb", "web.png", true, false, BlockSolidType.Air, false, null);
        this.addMineType(63, MineTypes.Web);
        this.addBlockInfo(64, "Dead Bush", "deadbush.png", true, false, BlockSolidType.Air, false, null);
        this.addBlockDrop(64, 0, 0, false);
        this.addBlockType(64, BlockType.Flower);
        this.addBlockInfo(65, "Tall Grass", "tallgrass.png", true, false, BlockSolidType.Air, false, null);
        this.addBlockDrop(65, 50, -2,3, true);
        this.addBlockType(65, BlockType.Flower);

        this.addBlockInfo(66, "Fern", "fern.png" , true, false, BlockSolidType.Air, false, null);
        this.addBlockDrop(66, 0, 0, false);
        this.addBlockType(66, BlockType.Flower);

        this.addMineType(63, MineTypes.Plants2);
        this.addMineType(64, MineTypes.Plants2);
        this.addMineType(65, MineTypes.Plants2);
        this.addMineType(66, MineTypes.Plants2);

        this.addBlockInfo(67, "White Wool", "wool_colored_white.png", false, true, BlockSolidType.Solid, false);
        this.addBlockInfo(68, "Orange Wool", "wool_colored_orange.png", false, true, BlockSolidType.Solid, false);
        this.addBlockInfo(69, "Magenta Wool", "wool_colored_magenta.png", false, true, BlockSolidType.Solid, false);

        this.addBlockInfo(70, "Light Blue Wool", "wool_colored_light_blue.png", false, true, BlockSolidType.Solid, false);
        this.addBlockInfo(71, "Yellow Wool", "wool_colored_yellow.png", false, true, BlockSolidType.Solid, false);
        this.addBlockInfo(72, "Lime Wool", "wool_colored_lime.png", false, true, BlockSolidType.Solid, false);
        this.addBlockInfo(73, "Pink Wool", "wool_colored_pink.png", false, true, BlockSolidType.Solid, false);
        this.addBlockInfo(74, "Gray Wool", "wool_colored_gray.png", false, true, BlockSolidType.Solid, false);
        this.addBlockInfo(75, "Light Gray Wool", "wool_colored_silver.png", false, true, BlockSolidType.Solid, false);
        this.addBlockInfo(76, "Cyan Wool", "wool_colored_cyan.png", false, true, BlockSolidType.Solid, false);
        this.addBlockInfo(77, "Purple Wool", "wool_colored_purple.png", false, true, BlockSolidType.Solid, false);
        this.addBlockInfo(78, "Blue Wool", "wool_colored_blue.png", false, true, BlockSolidType.Solid, false);
        this.addBlockInfo(79, "Brown Wool", "wool_colored_brown.png", false, true, BlockSolidType.Solid, false);

        this.addBlockInfo(80, "Green Wool", "wool_colored_green.png", false, true, BlockSolidType.Solid, false);
        this.addBlockInfo(81, "Red Wool", "wool_colored_red.png", false, true, BlockSolidType.Solid, false);
        this.addBlockInfo(82, "Black Wool", "wool_colored_black.png", false, true, BlockSolidType.Solid, false);
        this.addBlockInfo(83, "Magenta Wool", "wool_colored_magenta.png", false, true, BlockSolidType.Solid, false);

        this.addMineType(67, MineTypes.Wool);
        this.addMineType(68, MineTypes.Wool);
        this.addMineType(69, MineTypes.Wool);
        this.addMineType(70, MineTypes.Wool);
        this.addMineType(71, MineTypes.Wool);
        this.addMineType(72, MineTypes.Wool);
        this.addMineType(73, MineTypes.Wool);
        this.addMineType(74, MineTypes.Wool);
        this.addMineType(75, MineTypes.Wool);
        this.addMineType(76, MineTypes.Wool);
        this.addMineType(77, MineTypes.Wool);
        this.addMineType(78, MineTypes.Wool);
        this.addMineType(79, MineTypes.Wool);
        this.addMineType(80, MineTypes.Wool);
        this.addMineType(81, MineTypes.Wool);
        this.addMineType(82, MineTypes.Wool);
        this.addMineType(83, MineTypes.Wool);

        this.addBlockInfo(84, "Dandelion", "flower_dandelion.png", true, false, BlockSolidType.Air, false, null);
        this.addBlockInfo(85, "Poppy", "flower_rose.png", true, false, BlockSolidType.Air, false, null);
        this.addBlockInfo(86, "Blue Orchid", "flower_blue_orchid.png", true, false, BlockSolidType.Air, false, null);
        this.addBlockInfo(87, "Allium", "flower_allium.png", true, false, BlockSolidType.Air, false, null);
        this.addBlockInfo(88, "Azure Bluet", "flower_houstonia.png", true, false, BlockSolidType.Air, false, null);
        this.addBlockInfo(89, "Red Tulip", "flower_tulip_red.png", true, false, BlockSolidType.Air, false, null);

        this.addBlockInfo(90, "Orange Tulip", "flower_tulip_orange.png", true, false, BlockSolidType.Air, false, null);
        this.addBlockInfo(91, "White Tulip", "flower_tulip_white.png", true, false, BlockSolidType.Air, false, null);
        this.addBlockInfo(92, "Pink Tulip", "flower_tulip_pink.png", true, false, BlockSolidType.Air, false, null);
        this.addBlockInfo(93, "Oxeye Daisy", "flower_oxeye_daisy.png", true, false, BlockSolidType.Air, false, null);
        this.addBlockInfo(94, "Brown Mushroom", "mushroom_brown.png", true, false, BlockSolidType.Air, false, null);
        this.addBlockInfo(95, "Red Mushroom", "mushroom_red.png", true, false, BlockSolidType.Air, false, null);

        this.addBlockType(84, BlockType.Flower);
        this.addBlockType(85, BlockType.Flower);
        this.addBlockType(86, BlockType.Flower);
        this.addBlockType(87, BlockType.Flower);
        this.addBlockType(88, BlockType.Flower);
        this.addBlockType(89, BlockType.Flower);
        this.addBlockType(90, BlockType.Flower);
        this.addBlockType(91, BlockType.Flower);
        this.addBlockType(92, BlockType.Flower);
        this.addBlockType(93, BlockType.Flower);
        this.addBlockType(94, BlockType.Flower);
        this.addBlockType(95, BlockType.Flower);

        this.addMineType(84, MineTypes.Plants2);
        this.addMineType(85, MineTypes.Plants2);
        this.addMineType(86, MineTypes.Plants2);
        this.addMineType(87, MineTypes.Plants2);
        this.addMineType(88, MineTypes.Plants2);
        this.addMineType(89, MineTypes.Plants2);
        this.addMineType(90, MineTypes.Plants2);
        this.addMineType(91, MineTypes.Plants2);
        this.addMineType(92, MineTypes.Plants2);
        this.addMineType(93, MineTypes.Plants2);
        this.addMineType(94, MineTypes.Plants2);
        this.addMineType(95, MineTypes.Plants2);



        this.addBlockInfo(96, "Gold Block", "gold_block.png", false, true, BlockSolidType.Solid, false);
        this.addBlockInfo(97, "Iron Block", "iron_block.png", false, true, BlockSolidType.Solid, false);

        this.addMineType(96, MineTypes.Metal2);
        this.addMineType(95, MineTypes.Metal1);

        this.addBlockInfo(98, "Double Stone Slab", "stone_slab_side.png", false, true, BlockSolidType.Solid, false, "stone_slab_top.png");
        this.addBlockInfo(99, "Double Sandstone Slab", "sandstone_normal.png", false, true, BlockSolidType.Solid, false, "sandstone_top.png");

        this.addBlockInfo(100, "Double Cobblestone Slab", "cobblestone.png", false, true, BlockSolidType.Solid, false);
        this.addBlockInfo(101, "Double Brick Slab", "brick.png", false, true, BlockSolidType.Solid, false);
        this.addBlockInfo(102, "Double Stone Brick Slab", "stonebrick.png", false, true, BlockSolidType.Solid, false);
        this.addBlockInfo(103, "Double Nether Brick Slab", "nether_brick.png", false, true, BlockSolidType.Solid, false);
        this.addBlockInfo(104, "Double Quartz Slab", "quartz_block_side.png", false, true, BlockSolidType.Solid, false, "quartz_block_top.png");
        this.addBlockInfo(105, "Stone Slab", "stone_slab_side.png", true, true, BlockSolidType.Slab, false, "stone_slab_top.png", true);
        this.addBlockInfo(106, "Sandstone Slab", "sandstone_normal.png", true, true, BlockSolidType.Slab, false, "sandstone_top.png", true);
        this.addBlockInfo(107, "Cobblestone Slab", "cobblestone.png", true, true, BlockSolidType.Slab, false, true);
        this.addBlockInfo(108, "Brick Slab", "brick.png", true, true, BlockSolidType.Slab, false, true);
        this.addBlockInfo(109, "Stone Brick Slab", "stonebrick.png", true, true, BlockSolidType.Slab, false, true);

        this.addBlockInfo(110, "Nether Brick Slab", "nether_brick.png", true, true, BlockSolidType.Slab, false, true);
        this.addBlockInfo(111, "Quartz Slab", "quartz_block_side.png", true, true, BlockSolidType.Slab, false,"quartz_block_top.png", true);

        this.addMineType(98, MineTypes.Rock1);
        this.addMineType(99, MineTypes.Rock1);
        this.addMineType(100, MineTypes.Rock1);
        this.addMineType(101, MineTypes.Rock1);
        this.addMineType(102, MineTypes.Rock1);
        this.addMineType(103, MineTypes.Rock1);
        this.addMineType(104, MineTypes.Rock1);
        this.addMineType(105, MineTypes.Rock1);
        this.addMineType(106, MineTypes.Rock1);
        this.addMineType(107, MineTypes.Rock1);
        this.addMineType(108, MineTypes.Rock1);
        this.addMineType(109, MineTypes.Rock1);
        this.addMineType(110, MineTypes.Rock1);
        this.addMineType(111, MineTypes.Rock1);


        this.addBlockInfo(112, "Bookshelf", "bookshelf.png", false, true, BlockSolidType.Solid, false, "planks_oak.png");
        this.addMineType(112, MineTypes.Wood);
        this.addBlockInfo(113, "Obsidian", "obsidian.png", false, true, BlockSolidType.Solid, false);
        this.addMineType(113, MineTypes.Rock4);
        this.addBlockInfo(114, "Bedrock", "bedrock.png", false, false, BlockSolidType.Solid, false);
        this.addMineType(114, MineTypes.Unbreakable);
        this.addBlockInfo(115, "Torch", "torch_on.png", true, false, BlockSolidType.Air, true, null);
        this.addBlockType(115, BlockType.Torch);
        this.addMineType(115, MineTypes.Other2);

        this.addBlockInfo(116, "Fire", "fire_layer_0.png", true, false, BlockSolidType.Air, true, null);
        this.addMineType(116, MineTypes.Other2);
        this.addBlockDrop(116, 0, 0, false);
        this.addBlockInfo(117, "Monster Spawner", "mob_spawner.png", true, false, BlockSolidType.Solid, false);
        this.addMineType(117, MineTypes.Rock1);
        this.addBlockInfo(118, "Diamond Ore", "diamond_ore.png", false, true, BlockSolidType.Solid, false);
        this.addMineType(118, MineTypes.Rock3);
        this.addBlockDrop(118, 49, 1,4, true);
        this.addBlockInfo(119, "Diamond Block", "diamond_block.png", false, true, BlockSolidType.Solid, false);
        this.addMineType(119, MineTypes.Metal3);
        this.addBlockInfo(120, "Crafting Table", "crafting_table_front.png", false, true, BlockSolidType.Solid, false, "crafting_table_top.png");
        this.addMineType(120, MineTypes.Wood);
        this.addBlockInfo(121, "Wheat Crops 0", "wheat_stage_0.png", true, false, BlockSolidType.Air, false, null);
        this.addBlockInfo(122, "Wheat Crops 1", "wheat_stage_1.png", true, false, BlockSolidType.Air, false, null);
        this.addBlockInfo(123, "Wheat Crops 2", "wheat_stage_2.png", true, false, BlockSolidType.Air, false, null);
        this.addBlockInfo(124, "Wheat Crops 3", "wheat_stage_3.png", true, false, BlockSolidType.Air, false, null);
        this.addBlockInfo(125, "Wheat Crops 4", "wheat_stage_4.png", true, false, BlockSolidType.Air, false, null);
        this.addBlockInfo(126, "Wheat Crops 5", "wheat_stage_5.png", true, false, BlockSolidType.Air, false, null);
        this.addBlockInfo(127, "Wheat Crops 6", "wheat_stage_6.png", true, false, BlockSolidType.Air, false, null);
        this.addBlockInfo(128, "Wheat Crops 7", "wheat_stage_7.png", true, false, BlockSolidType.Air, false, null);

        this.addMineType(121, MineTypes.Plants2);
        this.addMineType(122, MineTypes.Plants2);
        this.addMineType(123, MineTypes.Plants2);
        this.addMineType(124, MineTypes.Plants2);
        this.addMineType(125, MineTypes.Plants2);
        this.addMineType(126, MineTypes.Plants2);
        this.addMineType(127, MineTypes.Plants2);
        this.addMineType(128, MineTypes.Plants2);

        this.addBlockInfo(129, "Farmland Wry", "dirt.png", false, true, BlockSolidType.Solid, false, "farmland_dry.png");
        this.addBlockInfo(130, "Farmland Wet", "dirt.png", false, true, BlockSolidType.Solid, false, "farmland_wet.png");
        this.addMineType(129, MineTypes.Ground);
        this.addMineType(130, MineTypes.Ground);

        this.addBlockInfo(131, "Furnace", "furnace_front_off.png", false, true, BlockSolidType.Solid, false, "furnace_top.png");
        this.addBlockInfo(132, "Burning Furnace", "furnace_front_on.png", false, true, BlockSolidType.Solid, false, "furnace_top.png");
        this.addMineType(131, MineTypes.Rock1);
        this.addMineType(132, MineTypes.Rock1);

        this.addBlockInfo(133, "Ladder", "ladder.png", true, false, BlockSolidType.Ladder, false, null);
        this.addMineType(133, MineTypes.Wood);
        this.addBlockInfo(134, "Snow Block", "snow.png", false, true, BlockSolidType.Solid, false);
        this.addMineType(134, MineTypes.Snow);
        this.addBlockInfo(135, "Ice", "ice.png", true, true, BlockSolidType.Solid, false);
        this.addBlockInfo(136, "Packed Ice", "ice_packed.png", true, true, BlockSolidType.Solid, false);
        this.addMineType(135, MineTypes.Snow);
        this.addMineType(136, MineTypes.Snow);

        this.addBlockInfo(137, "Cactus", "cactus_side.png", true, false, BlockSolidType.Air, false, "cactus_top.png");
        this.addMineType(137, MineTypes.Cactus);
        this.addBlockInfo(138, "Clay", "clay.png", false, true, BlockSolidType.Solid, false);
        this.addMineType(138, MineTypes.Ground);
        this.addBlockInfo(139, "Sugar Canes", "reeds.png", true, false, BlockSolidType.Air, false, null);
        this.addMineType(139, MineTypes.Plants2);

        this.addBlockInfo(140, "Jukebox", "jukebox_side.png", false, true, BlockSolidType.Solid, false, "jukebox_top.png");
        this.addMineType(140, MineTypes.Ground);
        this.addBlockInfo(141, "Pumpkin", "pumpkin_face_off.png", false, true, BlockSolidType.Solid, false, "pumpkin_top.png");
        this.addBlockInfo(142, "Jack o'Lantern", "pumpkin_face_on.png", false, true, BlockSolidType.Solid, true, "pumpkin_top.png");
        this.addMineType(141, MineTypes.Plants);
        this.addMineType(142, MineTypes.Plants);
        this.addBlockInfo(143, "Netherrack", "netherrack.png", false, true, BlockSolidType.Solid, false);
        this.addMineType(143, MineTypes.Rock1);
        this.addBlockInfo(144, "Soul Sand", "soul_sand.png", false, true, BlockSolidType.Solid, false);
        this.addMineType(144, MineTypes.Ground);
        this.addBlockInfo(145, "Glowstone", "glowstone.png", false, true, BlockSolidType.Solid, true);
        this.addMineType(145, MineTypes.Glass);

        this.addBlockInfo(146, "Cake Block", "cake_side.png", true, true, BlockSolidType.Slab, false, "cake_top.png", true);
        this.addMineType(146, MineTypes.Other);

        this.addBlockInfo(147, "Orange Stained Glass", "glass_orange.png", true, false, BlockSolidType.Solid, false);
        this.addBlockInfo(148, "Magenta Stained Glass", "glass_magenta.png", true, false, BlockSolidType.Solid, false);
        this.addBlockInfo(149, "Light Blue Stained Glass", "glass_blue.png", true, false, BlockSolidType.Solid, false);

        this.addBlockInfo(150, "Yellow Stained Glass", "glass_yellow.png", true, false, BlockSolidType.Solid, false);
        this.addBlockInfo(151, "Lime Stained Glass", "glass_lime.png", true, false, BlockSolidType.Solid, false);
        this.addBlockInfo(152, "Pink Stained Glass", "glass_pink.png", true, false, BlockSolidType.Solid, false);
        this.addBlockInfo(153, "Gray Stained Glass", "glass_gray.png", true, false, BlockSolidType.Solid, false);
        this.addBlockInfo(154, "Light Stained Glass", "glass_silver.png", true, false, BlockSolidType.Solid, false);
        this.addBlockInfo(155, "Cyan Stained Glass", "glass_cyan.png", true, false, BlockSolidType.Solid, false);
        this.addBlockInfo(156, "Purple Stained Glass", "glass_purple.png", true, false, BlockSolidType.Solid, false);
        this.addBlockInfo(157, "Blue Stained Glass", "glass_blue.png", true, false, BlockSolidType.Solid, false);
        this.addBlockInfo(158, "Brown Stained Glass", "glass_brown.png", true, false, BlockSolidType.Solid, false);
        this.addBlockInfo(159, "Green Stained Glass", "glass_green.png", true, false, BlockSolidType.Solid, false);

        this.addBlockInfo(160, "Red Stained Glass", "glass_red.png", true, false, BlockSolidType.Solid, false);
        this.addBlockInfo(161, "Black Stained Glass", "glass_black.png", true, false, BlockSolidType.Solid, false);
        this.addBlockInfo(162, "Magenta Stained Glass", "glass_magenta.png", true, false, BlockSolidType.Solid, false);

        this.addMineType(147, MineTypes.Glass);
        this.addMineType(148, MineTypes.Glass);
        this.addMineType(149, MineTypes.Glass);
        this.addMineType(150, MineTypes.Glass);
        this.addMineType(151, MineTypes.Glass);
        this.addMineType(152, MineTypes.Glass);
        this.addMineType(153, MineTypes.Glass);
        this.addMineType(154, MineTypes.Glass);
        this.addMineType(155, MineTypes.Glass);
        this.addMineType(156, MineTypes.Glass);
        this.addMineType(157, MineTypes.Glass);
        this.addMineType(158, MineTypes.Glass);
        this.addMineType(159, MineTypes.Glass);
        this.addMineType(160, MineTypes.Glass);
        this.addMineType(161, MineTypes.Glass);
        this.addMineType(162, MineTypes.Glass);


        this.addBlockInfo(163, "Brown Mushroom Block", "mushroom_block_skin_brown.png", false, true, BlockSolidType.Solid, false);
        this.addBlockInfo(164, "Red Mushroom Block", "mushroom_block_skin_red.png", false, true, BlockSolidType.Solid, false);
        this.addBlockInfo(165, "Mushroom stem", "mushroom_block_skin_stem.png", false, true, BlockSolidType.Solid, false);

        this.addMineType(163, MineTypes.Wood);
        this.addMineType(164, MineTypes.Wood);
        this.addMineType(165, MineTypes.Wood);

        this.addBlockInfo(166, "Iron Bars", "iron_bars.png", true, false, BlockSolidType.Solid, false, null);
        this.addMineType(166, MineTypes.Metal1);
        this.addBlockInfo(167, "Melon Block", "melon_side.png", false, true, BlockSolidType.Solid, false, "melon_top.png");
        this.addMineType(166, MineTypes.Plants);
        this.addBlockInfo(168, "Tnt", "tnt_side.png", true, false, BlockSolidType.Solid, false, "tnt_top.png");
        this.addMineType(168, MineTypes.Other2);

    }

    public boolean breakableByWater(int blockId){
        BlockType type = getBlockType(blockId);

        return type == BlockType.Flower || type == BlockType.Torch || blockId == 116;

    }


}
