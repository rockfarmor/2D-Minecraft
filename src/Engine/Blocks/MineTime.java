package Engine.Blocks;

import Engine.Constants;

public class MineTime {



    public static double hardness(int blockId){
        double hardness = 100000000;
        MineTools tool = MineTools.Any;
        ToolType toolType = ToolType.Any;

        switch (Constants.blockHandler.getMineType(blockId)){
            case Plants:
                hardness = 0.2;
                tool = MineTools.Axe;
                break;
            case Plants2:
                tool = MineTools.Any;
                hardness = 0;
                break;
            case Wood:
                tool = MineTools.Axe;
                hardness = 2;
                break;
            case Metal1:
                tool = MineTools.Pickaxe;
                toolType = ToolType.Wood;
                hardness = 2;
                break;
            case Metal2:
                tool = MineTools.Pickaxe;
                hardness = 5;
                toolType = ToolType.Stone;
                break;
            case Metal3:
                tool = MineTools.Pickaxe;
                toolType = ToolType.Iron;
                hardness = 2;
                break;
            case Rail:
                tool = MineTools.Pickaxe;
                hardness = 0.7;
                break;
            case Rock1:
                tool = MineTools.Pickaxe;
                hardness = 1.5;
                break;
            case Rock2:
                hardness = 3;
                toolType = ToolType.Stone;
                tool = MineTools.Pickaxe;
                break;
            case Rock3:
                hardness = 3;
                toolType = ToolType.Iron;
                tool = MineTools.Pickaxe;
                break;
            case Rock4:
                hardness = 50;
                toolType = ToolType.Diamond;
                tool = MineTools.Pickaxe;
                break;
            case Leaves:
                hardness = 0.2;
                tool = MineTools.Shear;
                break;
            case Web:
                hardness = 0.2;
                tool = MineTools.Shear;
                break;
            case Wool:
                hardness = 0.8;
                tool = MineTools.Shear;
                break;
            case Ground:
                hardness = 0.6;
                tool = MineTools.Shovel;
                break;
            case Snow:
                hardness = 0.2;
                tool = MineTools.Shovel;
                break;
            case Circuits:
                hardness = 0;
                break;
            case Glass:
                hardness = 0.3;

                break;
            case Other:
                hardness = 0.2;
                break;
            case Other2:
                hardness = 0;
                break;
            case Piston:
                hardness = 0.5;
                break;
            case Cactus:
                hardness = 0.4;
                break;
            case Liquid:
                hardness = 100000000;
                break;
            case Unbreakable:
                hardness = 100000000;
                break;
        }

        return hardness;
    }

    public static MineTools effectiveTool(int blockId){
        double hardness = 1000000000;
        MineTools tool = MineTools.Any;
        ToolType toolType = ToolType.Any;

        switch (Constants.blockHandler.getMineType(blockId)){
            case Plants:
                hardness = 0.2;
                tool = MineTools.Axe;
                break;
            case Plants2:
                tool = MineTools.Any;
                hardness = 0;
                break;
            case Wood:
                tool = MineTools.Axe;
                hardness = 2;
                break;
            case Metal1:
                tool = MineTools.Pickaxe;
                toolType = ToolType.Wood;
                hardness = 2;
                break;
            case Metal2:
                tool = MineTools.Pickaxe;
                hardness = 5;
                toolType = ToolType.Stone;
                break;
            case Metal3:
                tool = MineTools.Pickaxe;
                toolType = ToolType.Iron;
                hardness = 2;
                break;
            case Rail:
                tool = MineTools.Pickaxe;
                hardness = 0.7;
                break;
            case Rock1:
                tool = MineTools.Pickaxe;
                hardness = 1.5;
                break;
            case Rock2:
                hardness = 3;
                toolType = ToolType.Stone;
                tool = MineTools.Pickaxe;
                break;
            case Rock3:
                hardness = 3;
                toolType = ToolType.Iron;
                tool = MineTools.Pickaxe;
                break;
            case Rock4:
                hardness = 50;
                toolType = ToolType.Diamond;
                tool = MineTools.Pickaxe;
                break;
            case Leaves:
                hardness = 0.2;
                tool = MineTools.Shear;
                break;
            case Web:
                hardness = 0.2;
                tool = MineTools.Shear;
                break;
            case Wool:
                hardness = 0.8;
                tool = MineTools.Shear;
                break;
            case Ground:
                hardness = 0.6;
                tool = MineTools.Shovel;
                break;
            case Snow:
                hardness = 0.2;
                tool = MineTools.Shovel;
                break;
            case Circuits:
                hardness = 0;
                break;
            case Glass:
                hardness = 0.3;

                break;
            case Other:
                hardness = 0.2;
                break;
            case Other2:
                hardness = 0;
                break;
            case Piston:
                hardness = 0.5;
                break;
            case Cactus:
                hardness = 0.4;
                break;
            case Liquid:
                hardness = 1000000;
                break;
            case Unbreakable:
                hardness = 1000000;
                break;
        }

        return tool;
    }

    public static ToolType getToolType(int blockId){
        double hardness = 1000000000;
        MineTools tool = MineTools.Any;
        ToolType toolType = ToolType.Any;
        switch (Constants.blockHandler.getMineType(blockId)){
            case Plants:
                hardness = 0.2;
                tool = MineTools.Axe;
                break;
            case Plants2:
                tool = MineTools.Any;
                hardness = 0;
                break;
            case Wood:
                tool = MineTools.Axe;
                hardness = 2;
                break;
            case Metal1:
                tool = MineTools.Pickaxe;
                toolType = ToolType.Wood;
                hardness = 2;
                break;
            case Metal2:
                tool = MineTools.Pickaxe;
                hardness = 5;
                toolType = ToolType.Stone;
                break;
            case Metal3:
                tool = MineTools.Pickaxe;
                toolType = ToolType.Iron;
                hardness = 2;
                break;
            case Rail:
                tool = MineTools.Pickaxe;
                hardness = 0.7;
                break;
            case Rock1:
                tool = MineTools.Pickaxe;
                hardness = 1.5;
                break;
            case Rock2:
                hardness = 3;
                toolType = ToolType.Stone;
                tool = MineTools.Pickaxe;
                break;
            case Rock3:
                hardness = 3;
                toolType = ToolType.Iron;
                tool = MineTools.Pickaxe;
                break;
            case Rock4:
                hardness = 50;
                toolType = ToolType.Diamond;
                tool = MineTools.Pickaxe;
                break;
            case Leaves:
                hardness = 0.2;
                tool = MineTools.Shear;
                break;
            case Web:
                hardness = 0.2;
                tool = MineTools.Shear;
                break;
            case Wool:
                hardness = 0.8;
                tool = MineTools.Shear;
                break;
            case Ground:
                hardness = 0.6;
                tool = MineTools.Shovel;
                break;
            case Snow:
                hardness = 0.2;
                tool = MineTools.Shovel;
                break;
            case Circuits:
                hardness = 0;
                break;
            case Glass:
                hardness = 0.3;

                break;
            case Other:
                hardness = 0.2;
                break;
            case Other2:
                hardness = 0;
                break;
            case Piston:
                hardness = 0.5;
                break;
            case Cactus:
                hardness = 0.4;
                break;
            case Liquid:
                hardness = 1000000;
                break;
            case Unbreakable:
                hardness = 1000000;
                break;
        }
        return toolType;
    }


    public static int getToolTypeNum(ToolType tool){

        switch (tool){
            case Wood:
                return 1;
            case Stone:
                return 2;
            case Iron:
                return 3;
            case Diamond:
                return 4;
            case Gold:
                return 4;
        }
        return 0;
    }




}
