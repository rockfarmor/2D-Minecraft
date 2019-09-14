package Engine.Inventory;

import Engine.Blocks.BlockType;
import Engine.Constants;

public class RecipeItem {

    private int id;
    private boolean isItem;
    private boolean isblockType;

    private BlockType type;


    public RecipeItem(int id, boolean isItem) {
        this.id = id;
        this.isItem = isItem;
        this.isblockType = false;
        this.type = BlockType.Other;
    }

    public RecipeItem(BlockType type) {
        this.id = -1;
        this.isItem = false;
        this.isblockType = true;
        this.type = type;
    }


    public boolean equals(int id, boolean isItem){

        if(this.isblockType && !isItem){

            return Constants.blockHandler.getBlockType(id) == this.type;


        } else {
            return this.id == id && this.isItem == isItem;
        }



    }





}
