package Engine.Model;

import Engine.Constants;
import Engine.Inventory.Inventory;
import Engine.javax.vecmath.Vector2d;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ArmorBone extends Bone {

    private String armorId;
    private Inventory inventory;


    public ArmorBone(BufferedImage img, Vector2d origo, Vector2d offset, double rotation, int zIndex, String id, String armorId, Inventory inventory) {
        super(img, origo, offset, rotation, zIndex, id);
        this.armorId = armorId;
        this.inventory = inventory;
    }

    public void renderShadow(Graphics2D g2d, Color c){

        BufferedImage img = null;
        BufferedImage img2 = null;

        /*
            addItemInfo(25, "Leather Helmet", "leather_cap.png", false, false, false, true, true,false, false, false);
        addItemInfo(26, "Leather Tunic", "leather_tunic.png", false, false, false, true, false,true, false, false);
        addItemInfo(27, "Leather Pants", "leather_pants.png", false, false, false, true, false,false, true, false);
        addItemInfo(28, "Leather Boots", "leather_boots1.png", false, false, false, true, false,false, false, true);

        addItemInfo(29, "Iron Helmet", "iron_helmet.png", false, false, false, true, true,false, false, false);
        addItemInfo(30, "Iron Tunic", "iron_chestplate.png", false, false, false, true, false,true, false, false);
        addItemInfo(31, "Iron Pants", "iron_leggings.png", false, false, false, true, false,false, true, false);
        addItemInfo(32, "Iron Boots", "iron_boots.png", false, false, false, true, false,false, false, true);

        addItemInfo(33, "Gold Helmet", "gold_helmet.png", false, false, false, true, true,false, false, false);
        addItemInfo(34, "Gold Tunic", "gold_chestplate.png", false, false, false, true, false,true, false, false);
        addItemInfo(35, "Gold Pants", "gold_leggings.png", false, false, false, true, false,false, true, false);
        addItemInfo(36, "Gold Boots", "gold_boots.png", false, false, false, true, false,false, false, true);

        addItemInfo(37, "Diamond Helmet", "diamond_helmet.png", false, false, false, true, true,false, false, false);
        addItemInfo(38, "Diamond Tunic", "diamond_chestplate.png", false, false, false, true, false,true, false, false);
        addItemInfo(39, "Diamond Pants", "diamond_leggings.png", false, false, false, true, false,false, true, false);
        addItemInfo(40, "Diamond Boots", "diamond_boots.png", false, false, false, true, false,false, false, true);

        addItemInfo(41, "Chainmail Helmet", "chainmail_helmet.png", false, false, false, true, true,false, false, false);
        addItemInfo(42, "Chainmail Tunic", "chainmail_chestplate.png", false, false, false, true, false,true, false, false);
        addItemInfo(43, "Chainmail Pants", "chainmail_leggings.png", false, false, false, true, false,false, true, false);
        addItemInfo(44, "Chainmail Boots", "chainmail_boots.png", false, false, false, true, false,false, false, true);
        26,30,34,38,42


         */
        int x = 0;
        int x2 = 0;
        int y = 0;
        int y2 = 0;
        int w = 0;
        int w2 = 0;
        int h = 0;
        int h2 = 0;

        if(armorId.equals("head")){
            //25, 29, 33, 37, 41 chain
            switch (inventory.getInventoryItem(0,4).getId()){
                case 25:
                    //leather
                    img = Constants.LeatherArmor;
                    break;
                case 29:
                    //iron
                    img = Constants.IronArmor;
                    break;
                case 33:
                    //gold
                    img = Constants.GoldArmor;
                    break;
                case 37:
                    //diamond
                    img = Constants.DiamondArmor;
                    break;
                case 41:
                    //chain
                    img = Constants.ChainArmor;
                    break;
            }
            x = 0;
            y = 8;
            w = 8;
            h = 8;

        } else if(armorId.equals("body")){

            //26,30,34,38,42

            switch (inventory.getInventoryItem(1,4).getId()){
                case 26:
                    //leather
                    img = Constants.LeatherArmor;
                    break;
                case 30:
                    //iron
                    img = Constants.IronArmor;
                    break;
                case 34:
                    //gold
                    img = Constants.GoldArmor;
                    break;
                case 38:
                    //diamond
                    img = Constants.DiamondArmor;
                    break;
                case 42:
                    //chain
                    img = Constants.ChainArmor;
                    break;
            }
            x = 20;
            y = 20;
            w = 4;
            h = 12;

        } else if(armorId.equals("shoulder")){

            //26,30,34,38,42

            switch (inventory.getInventoryItem(1,4).getId()){
                case 26:
                    //leather
                    img = Constants.LeatherArmor;
                    break;
                case 30:
                    //iron
                    img = Constants.IronArmor;
                    break;
                case 34:
                    //gold
                    img = Constants.GoldArmor;
                    break;
                case 38:
                    //diamond
                    img = Constants.DiamondArmor;
                    break;
                case 42:
                    //chain
                    img = Constants.ChainArmor;
                    break;
            }
            x = 44;
            y = 20;
            w = 4;
            h = 12;

        } else if(armorId.equals("leg")){
            switch (inventory.getInventoryItem(2,4).getId()){
                case 27:
                    //leather
                    img = Constants.LeatherArmor;
                    break;
                case 31:
                    //iron
                    img = Constants.IronArmor;
                    break;
                case 35:
                    //gold
                    img = Constants.GoldArmor;
                    break;
                case 39:
                    //diamond
                    img = Constants.DiamondArmor;
                    break;
                case 43:
                    //chain
                    img = Constants.ChainArmor;
                    break;
            }

            x = 16;
            y = 20;
            w = 4;
            h = 12;

            switch (inventory.getInventoryItem(3,4).getId()){
                case 28:
                    //leather
                    img2 = Constants.LeatherArmor;
                    break;
                case 32:
                    //iron
                    img2 = Constants.IronArmor;
                    break;
                case 36:
                    //gold
                    img2 = Constants.GoldArmor;
                    break;
                case 40:
                    //diamond
                    img2 = Constants.DiamondArmor;
                    break;
                case 44:
                    //chain
                    img2 = Constants.ChainArmor;
                    break;
            }

            x2 = 4;
            y2 = 20;
            w2 = 4;
            h2 = 12;

        }


        if(img != null){
            renderImage(g2d, img.getSubimage(x,y,w,h));
        }

        if(img2 != null){
            renderImage(g2d, img2.getSubimage(x2,y2,w2,h2));
        }


        renderShadowImg(g2d, c, this.getImg(), 0 , 0, this.getImg().getWidth(), this.getImg().getHeight());
    }



}
