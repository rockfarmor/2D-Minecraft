package Engine.Gui;

import Engine.Blocks.BlockInfo;
import Engine.Blocks.ItemInfo;
import Engine.Constants;
import Engine.FileHandler.FileInput;
import Engine.Inventory.Inventory;

import java.awt.image.BufferedImage;

public class WindowFactory {




    public static Window InventoryWindow(){

        BufferedImage img = FileInput.LoadImage("Assets/textures/gui/container/inventory.png");
        img = img.getSubimage(0,0,176,166);

        Window w = new Window("Inventory", 0, 0, img);
        //Add slot for inventory
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 9; x++) {
                int y_ = 84 + y * (16 + 2);
                if (y == 3) y_ = 142;
                int x_ = 8 + x * (16 + 2);

                InventorySlot iSlot = new InventorySlot(x_,y_,16,16, x, y);
                w.addSlot(iSlot);

            }
        }
        //Add slot for armor
        for (int i = 0; i < 4; i++) {
            int y_ = 8 + i*(16 + 2);
            int x_ = 8;
            InventorySlot iSlot = new InventorySlot(x_, y_, 16, 16, i, 4);
            w.addSlot(iSlot);
        }


        /*
        0,1,2
        3,4,5
        6,7,8

         */
        //196, 35

        InventorySlot slot0 = new InventorySlot(98,36-18, 16, 16, 0, 5);
        w.addSlot(slot0);

        InventorySlot slot1 = new InventorySlot(98+18,36-18, 16, 16, 1, 5);
        w.addSlot(slot1);


        InventorySlot slot2 = new InventorySlot(98,36, 16, 16, 3, 5);
        w.addSlot(slot2);

        InventorySlot slot3 = new InventorySlot(98+18,36, 16, 16, 4, 5);
        w.addSlot(slot3);


        InventorySlot slotCraft = new InventorySlot(154,36-8, 16, 16, 4, 4);
        w.addSlot(slotCraft);


        int i = 1;
        for (int y = 0; y < 100; y++) {
            for (int x = 0; x < 8; x++) {
                BlockInfo blockInfo = Constants.blockHandler.getBlockInfo(i);
                if(i > 0 && blockInfo != null){
                    Button b = new Button(blockInfo.name, -100 + 8 * x,8 * y, 8 ,8, Constants.blockHandler.getBlockInfo(i).getImg());
                    w.addButton(b);
                }
                i++;
            }
        }

        i = 0;
        for (int y = 0; y < 100; y++) {
            for (int x = 0; x < 8; x++) {
                ItemInfo itemInfo = Constants.itemHandler.getItemInfo(i);
                if(itemInfo != null){
                    Button b = new Button(itemInfo.name, img.getWidth() + 10 + 8 * x,8 * y, 8 ,8, Constants.itemHandler.getItemInfo(i).img);
                    w.addButton(b);
                }
                i++;
            }
        }

        return w;

    }

    public static Window CraftingTableWindow(){

        BufferedImage img = FileInput.LoadImage("Assets/textures/gui/container/crafting_table.png");
        img = img.getSubimage(0,0,176,166);

        Window w = new Window("Craftingtable", 0, 0, img);
        //Add slot for inventory
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 9; x++) {
                int y_ = 84 + y * (16 + 2);
                if (y == 3) y_ = 142;
                int x_ = 8 + x * (16 + 2);

                InventorySlot iSlot = new InventorySlot(x_,y_,16,16, x, y);
                w.addSlot(iSlot);

            }
        }

        /*
        0,1,2
        3,4,5
        6,7,8

         */
        //196, 35

        InventorySlot slot0 = new InventorySlot(30,35-18, 16, 16, 0, 5);
        w.addSlot(slot0);

        InventorySlot slot1 = new InventorySlot(30+18,35-18, 16, 16, 1, 5);
        w.addSlot(slot1);

        InventorySlot slot11 = new InventorySlot(30+18+18,35-18, 16, 16, 2, 5);
        w.addSlot(slot11);


        InventorySlot slot2 = new InventorySlot(30,35, 16, 16, 3, 5);
        w.addSlot(slot2);

        InventorySlot slot3 = new InventorySlot(30+18,35, 16, 16, 4, 5);
        w.addSlot(slot3);

        InventorySlot slot33 = new InventorySlot(30+18+18,35, 16, 16, 5, 5);
        w.addSlot(slot33);

        InventorySlot slot22 = new InventorySlot(30,35+18, 16, 16, 6, 5);
        w.addSlot(slot22);

        InventorySlot slot32 = new InventorySlot(30+18,35+18, 16, 16, 7, 5);
        w.addSlot(slot32);

        InventorySlot slot332 = new InventorySlot(30+18+18,35+18, 16, 16, 8, 5);
        w.addSlot(slot332);

        InventorySlot slotCraft = new InventorySlot(124,35, 16, 16, 4, 4);
        w.addSlot(slotCraft);


        int i = 1;
        for (int y = 0; y < 100; y++) {
            for (int x = 0; x < 8; x++) {
                BlockInfo blockInfo = Constants.blockHandler.getBlockInfo(i);
                if(i > 0 && blockInfo != null){
                    Button b = new Button(blockInfo.name, -100 + 8 * x,8 * y, 8 ,8, Constants.blockHandler.getBlockInfo(i).getImg());
                    w.addButton(b);
                }
                i++;
            }
        }

        i = 0;
        for (int y = 0; y < 100; y++) {
            for (int x = 0; x < 8; x++) {
                ItemInfo itemInfo = Constants.itemHandler.getItemInfo(i);
                if(itemInfo != null){
                    Button b = new Button(itemInfo.name, img.getWidth() + 10 + 8 * x,8 * y, 8 ,8, Constants.itemHandler.getItemInfo(i).img);
                    w.addButton(b);
                }
                i++;
            }
        }

        return w;

    }







}
