package Engine.Model;

import Engine.Blocks.BlockType;
import Engine.Constants;
import Engine.Inventory.Inventory;
import Engine.Inventory.InventoryItem;
import Engine.javax.vecmath.Vector2d;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class InventoryBone extends Bone {

    private Inventory inventory;

    private int offX;
    private int offY;
    private int width;
    private int height;
    private double initRot;

    public InventoryBone(Inventory inventory, Vector2d origo, Vector2d offset, double rotation, int zIndex, String id) {
        super(null, origo, offset, rotation, zIndex, id);
        this.inventory = inventory;
        setOffs();
        this.initRot = rotation;
    }

    public void setOffs(){
        this.offX = 2;
        this.offY = 6;
        this.width = 8;
        this.height = 8;
        this.setRotation(this.initRot);
    }

    public void setOffsTorch(){
        this.offX = -2;
        this.offY = 4;
        this.width = 8;
        this.height = 8;
        this.setRotation(this.initRot + Math.PI/4);
    }

    public void setOffItem(){
        this.offX = 0;
        this.offY = 0;
        this.width = 16;
        this.height = 16;
        this.setRotation(this.initRot);
    }

    @Override
    public void renderImage(Graphics2D g2d) {
        if(this.inventory.getHandItem() != null && !(this.inventory.getHandItem().getId() == 0 && !this.inventory.getHandItem().isItem())){

            BlockType bType = Constants.blockHandler.getBlockType(inventory.getHandItem().getId());

            if(this.inventory.getHandItem().isItem()) {

                InventoryItem item = this.inventory.getHandItem();
                if (Constants.itemHandler.getItemInfo(item.getId()).isWeapon || Constants.itemHandler.getItemInfo(item.getId()).isTool){
                    setOffItem();
                } else {
                    setOffs();
                }
            } else if(bType == BlockType.Torch || bType == BlockType.Flower){
                setOffsTorch();
            }  else {
                setOffs();
            }

            AffineTransform at = g2d.getTransform();

            if(this.inventory.getHandItem().isTool()){
                AffineTransform at2 = g2d.getTransform();
                at2.scale(1,-1);
                g2d.setTransform(at2);
                this.setRotation(this.initRot - Math.PI/2);
            }




            g2d.drawImage(inventory.getHandItem().getImage(), (int)this.getOrigo().getX()+this.offX, (int)this.getOrigo().getY()+this.offY, this.width, this.height, null);
            g2d.setTransform(at);
        }
    }

    @Override
    public void renderShadow(Graphics2D g2d, Color c) {

        BufferedImage img;
        if(!inventory.getHandItem().isItem())
            img = Constants.blockHandler.getBlockInfo(inventory.getHandItem().getId()).getImg();
        else
            img = Constants.itemHandler.getItemInfo(inventory.getHandItem().getId()).img;

        AffineTransform at = g2d.getTransform();

        if(inventory.getHandItem().isTool()) {
            AffineTransform at2 = g2d.getTransform();
            at2.scale(1,-1);
            g2d.setTransform(at2);
        }

        this.renderShadowImg(g2d, c, img, this.offX, this.offY, this.width, this.height);

        g2d.setTransform(at);

    }
}
