package Engine.Inventory;

import Engine.Constants;
import Engine.Entities.DropEntity;
import Engine.World.World;

public class Inventory {

    private InventoryItem[][] inventory;

    //private InventoryItem handItem;

    private int handItem;

    private InventoryItem mouseItem;
    private int mouseX;
    private int mouseY;


    public Inventory() {

        this.inventory = new InventoryItem[6][9];

        this.mouseItem = null;
        mouseX = -1;
        mouseY = -1;


        for (int y = 0; y < 6; y++) {
            for (int x = 0; x < 9; x++) {
                this.inventory[y][x] = new InventoryItem(0,0);
            }
        }

        inventory[3][0].set(Constants.blockHandler.getBlockId("Stone"), 60, false);
        inventory[3][1].set(Constants.blockHandler.getBlockId("Dirt"), 64, false);

        this.handItem = 0;

        setHandItem(0);
    }

    public int placeHanditem(){
        if(getHandItem()!= null && !getHandItem().isItem()){
            int id = getHandItem().getId();
            getHandItem().addNum(-1);
            return id;

        }
        return 0;
    }


    public void setMouseItem(int x, int y){
        if(x >= 0 && x < 9 && y >= 0 && y < 6){
            this.mouseItem = getInventoryItem(x,y);
            this.setNewInventoryItem(x,y);
            this.mouseX = x;
            this.mouseY = y;
        }
    }

    public void moveItem(int thisX, int thisY, int destX, int destY){

        InventoryItem thisItem = getInventoryItem(thisX, thisY);
        InventoryItem destItem = getInventoryItem(destX, destY);

        //setInventoryItem();

    }


    public void clearMouseItem(){
        this.mouseItem = null;
        this.mouseX = -1;
        this.mouseY = -1;
    }

    public void swap(int x, int y){
        if(x >= 0 && x < 9 && y >= 0 && y < 6) {
            InventoryItem item = getInventoryItem(x, y);
            if (item.getId() == 0) {
                setInventoryItem(mouseItem, x, y);
            } else {
                setInventoryItem(item, mouseX, mouseY);
                setInventoryItem(mouseItem, x, y);
            }



        }
    }

    public void clickSlot(int x, int y){
        if(x >= 0 && x < 9 && y >= 0 && y < 6){
            if(this.mouseItem == null){




                if(!(this.getInventoryItem(x, y).getId() == 0 && !this.getInventoryItem(x,y).isItem())) {
                    this.setMouseItem(x, y);
                }

                if(x == 4 && y == 4 && mouseItem != null){
                    takeCraftedItem();
                }


            } else {

                if(isValidSlot(mouseItem, x, y)){
                    if(this.getInventoryItem(x,y).getId() == 0 && !this.getInventoryItem(x,y).isItem() ){
                        this.setInventoryItem(mouseItem, x, y);
                        this.clearMouseItem();
                    } else {
                        InventoryItem mItem = this.mouseItem;
                        InventoryItem item = getInventoryItem(x,y);

                        if(item.getId() == mItem.getId() && item.isItem() == mItem.isItem() && item.isStackable()){
                            // can we add item?
                            if(item.getNum() == 64){
                                setMouseItem(x, y);
                                this.setInventoryItem(mItem, x, y);
                            } else {

                                int n = item.getNum() + mItem.getNum();
                                int newNum = n;
                                if(newNum > 64) newNum = 64;
                                item.setNum(newNum);
                                if(n <= 64){
                                    clearMouseItem();
                                } else {
                                    mItem.setNum(n - newNum);
                                }


                            }
                        } else {
                            setMouseItem(x, y);
                            this.setInventoryItem(mItem, x, y);
                        }

                    }
                } else {
                    InventoryItem mItem = this.mouseItem;
                    InventoryItem item = getInventoryItem(x,y);
                    if(x == 4 && y == 4){
                        if(item.getId() == mItem.getId() && item.isItem() == mItem.isItem() && item.isStackable()){
                            if(mItem.getNum() + item.getNum() <= 64){
                                mItem.addNum(item.getNum());
                                takeCraftedItem();

                            }

                        }
                    }
                }

            }
        }
        updateCraft();
    }

    public void rightClickSlot(int x, int y) {
        if (x >= 0 && x < 9 && y >= 0 && y < 6) {
            if(x == 4 && y == 4){
                clickSlot(x, y);
            } else {

                if (this.mouseItem == null) {
                    if (!(this.getInventoryItem(x, y).getId() == 0 && !this.getInventoryItem(x, y).isItem())) {
                        InventoryItem item = this.getInventoryItem(x,y);
                        if(item.getNum() == 1){
                            this.setMouseItem(x, y);
                        } else {

                            int newN = (int)(item.getNum() / 2.0);
                            item.setNum(item.getNum() - newN);

                            mouseItem = new InventoryItem(item.getId(), newN, item.isItem());

                        }

                    }
                } else {


                    InventoryItem item = this.getInventoryItem(x,y);
                    if((item.getId() == 0 && !item.isItem())){
                        if(isValidSlot(mouseItem, x, y)) {
                            this.setInventoryItem(new InventoryItem(mouseItem.getId(), 1, mouseItem.isItem()), x, y);
                            if (mouseItem.isStackable()) {
                                mouseItem.addNum(-1);
                                if(mouseItem.getNum() <= 0) clearMouseItem();
                            } else {
                                clearMouseItem();
                            }
                        }

                    } else {

                        if(item.getId() == mouseItem.getId() && item.isItem() == mouseItem.isItem() && item.isStackable()){

                            if(item.getNum() < 64){
                                item.addNum(1);
                                mouseItem.addNum(-1);
                            }
                        }
                    }

                }
            }
        }
        updateCraft();
    }



    public void addItem(int id, int num, boolean isItem){
        boolean found = false;

        if (num <= 0) return;

        for (int y_ = 0; y_ < 4; y_++) {
            for (int x = 0; x < 9; x++) {
                int y = y_ - 1;
                if (y_ == 0) y = 3;

                InventoryItem item = getInventoryItem(x, y);
                if (item.getId() == id && item.isItem() == isItem && num > 0 && item.isStackable()) {

                    if (item.getNum() < 64){

                        int n = item.getNum() + num;
                        int newn = n;
                        if(newn > 64) newn = 64;
                        num = n - newn;
                        getInventoryItem(x,y).setNum(newn);

                        if(num <= 0){
                            found = true;
                            break;
                        }

                    }

                    //setInventoryItem(new InventoryItem(id, num, isItem), x, y);

                }
            }
            if (found) break;
        }


        if(!found) {
            for (int y_ = 0; y_ < 4; y_++) {
                for (int x = 0; x < 9; x++) {
                    int y = y_ - 1;
                    if (y_ == 0) y = 3;

                    InventoryItem item = getInventoryItem(x, y);
                    if (item.getId() == 0 && !item.isItem()) {
                        found = true;
                        setInventoryItem(new InventoryItem(id, num, isItem), x, y);
                        break;
                    }
                }
                if (found) break;
            }
        }


        if(!found) Constants.debugger.addLog("Inventory full", 2);

    }


    public void setNewInventoryItem(int x, int y){
        if(x >= 0 && x < 9 && y >= 0 && y < 6){
            inventory[y][x] = new InventoryItem(0,0);
        }
    }

    public void setNewMouseItem(int id, int num, boolean isItem){
        if(id >= 0 && num > 0 && num <= 64){
            this.mouseItem = new InventoryItem(id, num, isItem);
        }
    }

    public void setHandItem(int x){
        if(x >= 0 && x < 9){
            this.handItem = x;
        }
    }

    public void setInventoryItem(InventoryItem item, int x, int y){
        if(x >= 0 && x < 9 && y >= 0 && y < 6){
            this.inventory[y][x] = item;
        }
    }

    public boolean isValidSlot(InventoryItem item, int x, int y){
        if(x >= 0 && x < 9 && y >= 0 && (y < 4) || y == 5){
            return true;
        }
        if(y == 4 && x >= 0 && x < 4){

            switch(x){
                case 0:
                    //head
                    if(item.isHeadArmor())
                        return true;
                    break;
                case 1:
                    if(item.isPlateArmor())
                        return true;
                    //body
                    break;
                case 2:
                    if(item.isLegArmor())
                        return true;
                    //leg
                    break;
                case 3:
                    if(item.isFootArmor())
                        return true;
                    //foot
                    break;
            }


        }
        return false;
    }

    public void updateCraft(){
        InventoryItem i1 = getInventoryItem(0, 5);
        InventoryItem i2 = getInventoryItem(1, 5);
        InventoryItem i3 = getInventoryItem(3, 5);
        InventoryItem i4 = getInventoryItem(4, 5);

        setNewInventoryItem(4,4);

        /*if(i2.isNull() && i4.isNull() && !i1.isItem() && !i3.isItem() && i1.getId() == 14 && i3.getId() == 14){
            System.out.println("craft stick");
            setInventoryItem(new InventoryItem(0,1, true), 4,4);
        }*/

        InventoryItem result = Constants.craftingHandler.checkRecipi(this.inventory[5]);

        if(!result.isNull()){
            setInventoryItem(result, 4, 4);
        }





    }

    public void takeCraftedItem(){

        for (int i = 0; i < 9; i++) {
            InventoryItem item = getInventoryItem(i, 5);
            item.addNum(-1);
        }

        updateCraft();


    }



    public InventoryItem getHandItem() {
        return getInventoryItem(this.handItem, 3);
    }

    public InventoryItem getInventoryItem(int x, int y){
        if(x >= 0 && x < 9 && y >= 0 && y < 6){
            return inventory[y][x];
        }
        return null;
    }

    public InventoryItem getMouseItem() {
        return mouseItem;
    }

    public void closeInventory(World world){

        updateCraft();

        if (mouseItem != null) {
            DropEntity e = new DropEntity(world.getPlayer().getPosition().getX(), world.getPlayer().getPosition().getY()-20, world, mouseItem);
            e.getVelocity().set(30, -100);
            world.addEntity(0, e);

            clearMouseItem();
        }

        for (int i = 0; i < 9; i++) {
            if(!getInventoryItem(i,5).isNull()){
                DropEntity e = new DropEntity(world.getPlayer().getPosition().getX(), world.getPlayer().getPosition().getY()-20, world, getInventoryItem(i,5));
                e.getVelocity().set(30, -100);
                world.addEntity(0, e);
                setNewInventoryItem(i,5);

            }
        }








    }


}
