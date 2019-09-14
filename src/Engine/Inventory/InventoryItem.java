package Engine.Inventory;

import Engine.Constants;

import java.awt.image.BufferedImage;

public class InventoryItem {

    private int id;
    private int num;
    private boolean isItem;
    private boolean stackable;

    public InventoryItem(int id, int num) {
        this.id = id;
        this.num = num;
        this.stackable = true;
        this.isItem = false;
    }

    public InventoryItem(int id, int num, boolean isItem) {
        this.id = id;
        this.num = num;
        this.stackable = true;
        this.isItem = isItem;

        if(isItem){
            this.stackable = Constants.itemHandler.isStackable(this.id);
        }

        this.set(id, num, isItem);

    }


    public BufferedImage getImage(){
        if(this.isItem){
            return Constants.itemHandler.getItemInfo(this.id).img;
        } else {
            return Constants.blockHandler.getBlockInfo(this.id).getImg();
        }
    }

    public int getId() {
        return id;
    }

    public int getNum() {
        return num;
    }

    public void set(int id, int num, boolean isItem){
        if(id == 0 && !isItem){
            this.id = 0;
            this.num = 0;
        } else {
            this.id = id;
            this.num = num;
            this.isItem = isItem;

            if(isItem){
                this.stackable = Constants.itemHandler.isStackable(this.id);
            }

            if(!this.stackable){
                if(this.num > 0) this.num = 1;
            }

            if(this.num <= 0){
                this.id = 0;
                this.num = 0;
            }
            if(this.num > 64) this.num = 64;

        }
    }

    public void addNum(int num){
        this.num += num;
        if(this.num > 0 && !this.stackable) this.num = 1;
        if(this.num <= 0){
            this.num = 0;
            this.id = 0;
            this.isItem = false;
        }
    }

    public boolean isNull(){
        return this.id == 0 && this.isItem == false;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public boolean isItem() {
        return isItem;
    }

    public boolean isHeadArmor(){
        if(this.isItem){
            return Constants.itemHandler.getItemInfo(this.id).isHeadArmor;
        }
        return false;
    }

    public boolean isPlateArmor(){
        if(this.isItem){
            return Constants.itemHandler.getItemInfo(this.id).isPlateArmor;
        }
        return false;
    }

    public boolean isLegArmor(){
        if(this.isItem){
            return Constants.itemHandler.getItemInfo(this.id).isLegArmor;
        }
        return false;
    }

    public boolean isFootArmor(){
        if(this.isItem){
            return Constants.itemHandler.getItemInfo(this.id).isFootArmor;
        }
        return false;
    }

    public boolean isStackable(){
        if(this.isItem)
            return Constants.itemHandler.isStackable(this.id);
        return true;
    }

    public boolean isTool(){
        if(this.isItem)
            return Constants.itemHandler.isTool(this.id);
        return false;
    }


}
