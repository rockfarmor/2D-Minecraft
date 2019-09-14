package Engine.Blocks;

import Engine.Constants;

public class BlockDrop {

    private int id;
    private int numMin;
    private int numMax;
    private boolean isItem;

    public BlockDrop(int id, int numMin, int numMax, boolean isItem){
        this.id = id;
        this.numMin = numMin;
        this.numMax = numMax;
        this.isItem = isItem;
    }

    public BlockDrop(int id, int num, boolean isItem){
        this.id = id;
        this.numMin = num;
        this.numMax = num;
        this.isItem = isItem;
    }


    public int getNum(){
        if(this.numMin == numMax) return numMin;
        int num = Constants.randomNumber(numMin, numMax);
        if(num < 0) num = 0;

        return num;
    }

    public int getId() {
        return id;
    }

    public boolean isItem() {
        return isItem;
    }
}
