package Engine.Blocks;

import Engine.FileHandler.FileInput;

import java.awt.image.BufferedImage;

public class ItemInfo {

    public int id;
    public String name;
    public boolean isArmor;
    public boolean isTool;
    public boolean isWeapon;
    public boolean isHeadArmor;
    public boolean isPlateArmor;
    public boolean isLegArmor;
    public boolean isFootArmor;
    public boolean isConsumable;

    public boolean isStackable;

    public BufferedImage img;

    public ItemInfo(int id, String name, String imgSource){

        this.id = id;
        this.name = name;

        this.isArmor = false;
        this.isWeapon = false;
        this.isHeadArmor = false;
        this.isPlateArmor = false;
        this.isLegArmor = false;
        this.isFootArmor = false;
        this.isConsumable = false;

        this.isStackable = true;


        if(imgSource == null || imgSource.equals("")) {
            this.img = null;
            this.img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        } else {
            this.img = FileInput.LoadImage("Assets/textures/items/" + imgSource);
            if(this.img == null){
                System.out.println("Error loading + " + name);
                this.img = new BufferedImage(16,16, BufferedImage.TYPE_INT_ARGB);
            }

            if(this.img.getWidth() == 32){
                BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
                img.getGraphics().drawImage(this.img, 0, 0, 16, 16, null);
                this.img = img;
            }

        }

    }

    public MineTools getMineTool(){

        if(id == 0 || id == 5 || id == 10 || id == 15 || id == 20)
            return MineTools.Sword;

        if(id == 1 || id == 6 || id == 11 || id == 16 || id == 21)
            return MineTools.Shovel;

        if(id == 2 || id == 7 || id == 12 || id == 17 || id == 22)
            return MineTools.Pickaxe;

        if(id == 3 || id == 8 || id == 13 || id == 18 || id == 23)
            return MineTools.Axe;

        return MineTools.Any;
    }

    public ToolType getToolType(){

        if (id <= 24){
            if (id < 5)
                return ToolType.Wood;
            if (id < 10)
                return ToolType.Stone;
            if(id < 15) return ToolType.Iron;
            return ToolType.Diamond;
        }

        return ToolType.Any;
    }




}
