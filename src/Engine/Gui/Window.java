package Engine.Gui;

import Engine.Blocks.BlockInfo;
import Engine.Blocks.ItemInfo;
import Engine.Constants;
import Engine.Entities.DropEntity;
import Engine.Inventory.Inventory;
import Engine.Inventory.InventoryItem;
import Engine.World.World;
import Engine.javax.vecmath.Vector2d;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.util.ArrayList;

public class Window {

    private ArrayList<Button> buttons;
    private ArrayList<InventorySlot> invSlots;

    private int x;
    private int y;

    private int width;
    private int height;

    private String name;

    private boolean active;

    private BufferedImage img;

    private double scale;

    private Inventory inventory;

    private Vector2d mousePos;

    private Vector2d slotPos;

    private boolean mouseLeftWasDown;
    private boolean mouseRightWasDown;

    public Window(String name, int x, int y, BufferedImage img) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = img.getWidth();
        this.height = img.getHeight();
        this.active = false;
        this.buttons = new ArrayList<>();
        this.invSlots = new ArrayList<>();
        this.img = img;
        this.scale = 2;

        this.mouseLeftWasDown = false;
        this.mouseRightWasDown = false;

        this.mousePos = new Vector2d();
        this.slotPos = new Vector2d();

    }

    public void render(Graphics2D g2d){

        AffineTransform at = g2d.getTransform();
        AffineTransform at2 = new AffineTransform();
        at2.translate(this.x, this.y);
        at2.scale(this.scale, this.scale);
        g2d.setTransform(at2);

        g2d.drawImage(this.img, 0, 0, null);
        g2d.setFont(Constants.InventoryItemNumFont);
        for (int i = 0; i < this.buttons.size(); i++) {
            Button b = this.buttons.get(i);
            b.render(g2d);

        }




        if(this.inventory != null){
            for (int i = 0; i < invSlots.size(); i++) {

                //g2d.setColor(Color.red);
                InventorySlot iSlot = invSlots.get(i);
                //g2d.fillRect(iSlot.getX(),  iSlot.getY(), 16, 16);

                InventoryItem item = inventory.getInventoryItem(iSlot.getxId(), iSlot.getyId());
                if (item != null) {
                    if (!(item.getId() == 0 && !item.isItem())) {
                        BufferedImage img = item.getImage();
                        g2d.drawImage(img, iSlot.getX(), iSlot.getY(), null);

                        int num = item.getNum();
                        g2d.setColor(Color.white);
                        if(!item.isStackable())
                            g2d.setColor(Color.red);
                        g2d.drawString("" + num, iSlot.getX(), iSlot.getY() + 5);
                    }
                }

                if(iSlot.pointInButton((int)mousePos.x, (int)mousePos.y, this.x, this.y, this.scale)){
                    g2d.setColor(new Color(0,0,0,100));
                    g2d.fillRect(iSlot.getX(), iSlot.getY(), iSlot.getWidth(), iSlot.getHeight());
                }


            }


        }
        g2d.setTransform(at);
        if(this.inventory != null) {
            InventoryItem mItem = this.inventory.getMouseItem();
            if (mItem != null) {

                BufferedImage img = mItem.getImage();

                g2d.drawImage(img, (int) this.mousePos.x - 16, (int) this.mousePos.y - 16, 32, 32, null);
                g2d.setFont(Constants.InventoryItemNumFont2);
                g2d.setColor(Color.white);
                if (!mItem.isStackable())
                    g2d.setColor(Color.red);
                g2d.drawString("" + mItem.getNum(), (int) this.mousePos.x - 16, (int) this.mousePos.y - 16 + 10);

            }


            g2d.setFont(Constants.InventoryItemNumFont2);
            for (int i = 0; i < this.buttons.size(); i++) {
                Button b = this.buttons.get(i);

                if (this.inventory != null) {
                    if (b.pointInButton((int) mousePos.getX(), (int) mousePos.getY(), this.x, this.y, this.scale)) {
                        BlockInfo blockInfo = Constants.blockHandler.getBlockInfo(b.getButtonText());
                        ItemInfo itemInfo = Constants.itemHandler.getItemInfo(b.getButtonText());
                        if (blockInfo != null) {
                            g2d.setColor(new Color(0, 0, 0, 100));
                            g2d.fillRect((int) mousePos.getX(), (int) mousePos.getY() - 10 - 12, blockInfo.name.length() * 7, 14);
                            g2d.setColor(Color.WHITE);
                            g2d.drawString(blockInfo.name, (int) mousePos.getX(), (int) mousePos.getY() - 10);
                        }

                        if (itemInfo != null) {
                            g2d.setColor(new Color(0, 0, 0, 100));
                            g2d.fillRect((int) mousePos.getX(), (int) mousePos.getY() - 10 - 12, itemInfo.name.length() * 7, 14);
                            g2d.setColor(Color.WHITE);
                            g2d.drawString(itemInfo.name, (int) mousePos.getX(), (int) mousePos.getY() - 10);
                        }
                        break;
                    }
                }
            }
        }

    }

    public void update(int mouseX, int mouseY, boolean mouseLeftDown, boolean mouseRightDown, World world){
        this.mousePos.set(mouseX, mouseY);
        boolean mRightClicked = mouseRightDown && !this.mouseRightWasDown;
        boolean mLeftClicked = mouseLeftDown && !this.mouseLeftWasDown;


        if(this.active){
            if(this.inventory != null){
                boolean found = false;
                for (int i = 0; i < invSlots.size(); i++) {

                    InventorySlot iSlot = invSlots.get(i);
                    if(iSlot.pointInButton((int)mousePos.x, (int)mousePos.y, this.x, this.y, this.scale)){
                        if(mLeftClicked){
                            this.inventory.clickSlot(iSlot.getxId(), iSlot.getyId());
                            found = true;
                            break;
                        }

                        if(mRightClicked){
                            this.inventory.rightClickSlot(iSlot.getxId(), iSlot.getyId());
                            found = true;
                            break;
                        }

                    }
                }
                if(mLeftClicked && !found && !mouseInWindow(mouseX, mouseY)){
                    if(this.inventory.getMouseItem() != null){

                        int offX = 0;
                        //Vector2d s2w = world.getmComp().screenToWorld(x, y, world.getCamera());

                        if(mouseX < world.getFrame().getWidth()/2.0){
                            offX = -Constants.randomNumber(32,64);;
                        } else {
                            offX = Constants.randomNumber(32,64);
                        }
                        DropEntity e = new DropEntity(world.getPlayer().getPosition().getX(), world.getPlayer().getPosition().getY()-20, world, this.inventory.getMouseItem());
                        e.getVelocity().set(offX, -100);
                        world.addEntity(0, e);
                        this.inventory.clearMouseItem();
                    }
                }

            }




            for (int i = 0; i < this.buttons.size(); i++) {
                Button b = this.buttons.get(i);

                if(b.pointInButton(mouseX, mouseY, this.x, this.y, this.scale)){
                    if(mLeftClicked || mRightClicked){

                        int amount = 64;
                        if(mRightClicked) amount = 1;

                        BlockInfo blockInfo = Constants.blockHandler.getBlockInfo(b.getButtonText());
                        if(blockInfo != null){
                            if(this.inventory != null){
                                this.inventory.setNewMouseItem(blockInfo.id, amount, false);
                            }
                        }
                        ItemInfo itemInfo = Constants.itemHandler.getItemInfo(b.getButtonText());
                        if(itemInfo != null){
                            if(this.inventory != null){
                                this.inventory.setNewMouseItem(itemInfo.id, amount, true);
                            }
                        }


                    }
                }
            }

        }
        this.mouseLeftWasDown = mouseLeftDown;
        this.mouseRightWasDown = mouseRightDown;

    }


    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public void addButton(Button b){
        this.buttons.add(b);
    }


    public void addSlot(InventorySlot iSlot){
        this.invSlots.add(iSlot);
    }


    public Button getButtons(int i){
        if(i >= 0 && i < this.buttons.size()) return this.buttons.get(i);
        return null;
    }


    public void setPosition(double x, double y){
        this.x = (int)x;
        this.y = (int)y;
    }

    public double getScale() {
        return scale;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setActive(boolean active, World world) {
        this.active = active;
        if(!this.active){
            if(this.inventory != null){
                //this.inventory.clearMouseItem();
                this.inventory.closeInventory(world);
            }
        }
    }

    public boolean mouseInWindow(int mouseX, int mouseY){
        return  mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width*this.scale && mouseY < this.y + this.height*this.scale;
    }


    public boolean isActive() {
        return active;
    }

    public String getName() {
        return name;
    }
}
