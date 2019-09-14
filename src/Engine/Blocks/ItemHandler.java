package Engine.Blocks;

import java.util.HashMap;

public class ItemHandler {

    private HashMap<Integer, ItemInfo> itemInfo;
    private HashMap<String, Integer> nameInt;

    public int maxId;

    public ItemHandler() {
        this.itemInfo = new HashMap<>();
        this.nameInt = new HashMap<>();
        this.maxId = 0;
        init();
    }

    public ItemInfo getItemInfo(int id){
        if(itemInfo.get(id) == null){
            //System.out.println("GAME BROKEN" + id);
            return null;
        }
        return itemInfo.get(id);
    }

    public ItemInfo getItemInfo(String name){
        if(nameInt.get(name) != null){
            return this.itemInfo.get(nameInt.get(name));
        }
        return null;
    }


    public void addItemInfo(int id, String name, String imgSource, boolean isConsumable, boolean isTool, boolean isWeapon){
        addItemInfo(id, name, imgSource, isConsumable, isTool, isWeapon, false, false, false, false,false);
    }

    public void addItemInfo(int id, String name, String imgSource, boolean isConsumable, boolean isTool, boolean isWeapon, boolean isArmor, boolean isHeadArmor, boolean isPlateArmor, boolean isLegArmor, boolean isFootArmor){


        ItemInfo iInfo = new ItemInfo(id, name, imgSource);
        iInfo.isConsumable = isConsumable;
        iInfo.isTool = isTool;
        iInfo.isWeapon = isWeapon;
        iInfo.isArmor = isArmor;
        iInfo.isHeadArmor = isHeadArmor;
        iInfo.isPlateArmor = isPlateArmor;
        iInfo.isLegArmor = isLegArmor;
        iInfo.isFootArmor = isFootArmor;

        iInfo.isStackable = true;
        if(isTool || isWeapon || isArmor) iInfo.isStackable = false;

        this.itemInfo.put(iInfo.id, iInfo);
        this.nameInt.put(iInfo.name, iInfo.id);
        if(id > this.maxId){
            this.maxId = id;
        }
    }

    public boolean isStackable(int id){
        if(itemInfo.get(id) != null){
            return itemInfo.get(id).isStackable;
        }
        return true;
    }

    public boolean isTool(int id){
        if(itemInfo.get(id) != null){
            return itemInfo.get(id).isTool;
        }
        return true;
    }




    public void init(){


        addItemInfo(0, "Wooden Sword", "wood_sword.png", false, false, true);
        addItemInfo(1, "Wooden Shovel", "wood_shovel.png", false, true, false);
        addItemInfo(2, "Wooden Pickaxe", "wood_pickaxe.png", false, true, false);
        addItemInfo(3, "Wooden Axe", "wood_axe.png", false, true, false);
        addItemInfo(4, "Wooden Hoe", "wood_hoe.png", false, true, false);

        addItemInfo(5, "Stone Sword", "stone_sword.png", false, false, true);
        addItemInfo(6, "Stone Shovel", "stone_shovel.png", false, true, false);
        addItemInfo(7, "Stone Pickaxe", "stone_pickaxe.png", false, true, false);
        addItemInfo(8, "Stone Axe", "stone_axe.png", false, true, false);
        addItemInfo(9, "Stone Hoe", "stone_hoe.png", false, true, false);

        addItemInfo(10, "Iron Sword", "iron_sword.png", false, false, true);
        addItemInfo(11, "Iron Shovel", "iron_shovel.png", false, true, false);
        addItemInfo(12, "Iron Pickaxe", "iron_pickaxe.png", false, true, false);
        addItemInfo( 13, "Iron Axe", "iron_axe.png", false, true, false);
        addItemInfo(14, "Iron Hoe", "iron_hoe.png", false, true, false);

        addItemInfo(15, "Gold Sword", "gold_sword.png", false, false, true);
        addItemInfo(16, "Gold Shovel", "gold_shovel.png", false, true, false);
        addItemInfo(17, "Gold Pickaxe", "gold_pickaxe.png", false, true, false);
        addItemInfo( 18, "Gold Axe", "gold_axe.png", false, true, false);
        addItemInfo(19, "Gold Hoe", "gold_hoe.png", false, true, false);

        addItemInfo(20, "Diamond Sword", "diamond_sword.png", false, false, true);
        addItemInfo(21, "Diamond Shovel", "diamond_shovel.png", false, true, false);
        addItemInfo(22, "Diamond Pickaxe", "diamond_pickaxe.png", false, true, false);
        addItemInfo( 23, "Diamond Axe", "diamond_axe.png", false, true, false);
        addItemInfo(24, "Diamond Hoe", "diamond_hoe.png", false, true, false);

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


        addItemInfo(45, "Coal", "coal.png", false, false, false);
        addItemInfo(46, "Charcoal", "charcoal.png", false, false, false);
        addItemInfo(47, "Iron Ingot", "iron_ingot.png", false, false, false);
        addItemInfo(48, "Gold Ingot", "gold_ingot.png", false, false, false);
        addItemInfo(49, "Diamond", "diamond.png", false, false, false);
        addItemInfo(50, "Seed", "seeds_wheat.png", false, false, false);
        addItemInfo(51, "Stick", "stick.png", false, false, false);



    }


}
