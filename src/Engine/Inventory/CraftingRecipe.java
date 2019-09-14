package Engine.Inventory;

public class CraftingRecipe {

    private RecipeItem[] recipeItems;
    private int dimensionX;
    private int dimensionY;

    private InventoryItem result;

    public CraftingRecipe(int dimensionX, int dimensionY, InventoryItem item) {
        this.dimensionX = dimensionX;
        this.dimensionY = dimensionY;
        this.recipeItems = new RecipeItem[9];
        for (int i = 0; i < 9; i++) {
            this.recipeItems[i] = null;
        }
        this.result = item;
    }

    public void addRecipe(RecipeItem item){
        this.recipeItems[0] = item;
    }

    public void addVerticalRecipe(RecipeItem item, RecipeItem item2){
        this.recipeItems[0] = item;
        this.recipeItems[1] = item2;
    }

    public void addVerticalRecipe(RecipeItem item, RecipeItem item2, RecipeItem item3){
        this.recipeItems[0] = item;
        this.recipeItems[1] = item2;
        this.recipeItems[2] = item3;
    }

    public void addHorizontalRecipe(RecipeItem item, RecipeItem item2){
        this.recipeItems[0] = item;
        this.recipeItems[3] = item2;
    }

    public void addHorizontalRecipe(RecipeItem item, RecipeItem item2, RecipeItem item3){
        this.recipeItems[0] = item;
        this.recipeItems[3] = item2;
        this.recipeItems[6] = item3;
    }

    public void addRecipe(RecipeItem item0, RecipeItem item1, RecipeItem item2, RecipeItem item3){
        this.recipeItems[0] = item0;
        this.recipeItems[1] = item1;
        this.recipeItems[3] = item2;
        this.recipeItems[4] = item3;
    }

    public void addRecipe(RecipeItem item0, RecipeItem item1, RecipeItem item2, RecipeItem item3, RecipeItem item4, RecipeItem item5, RecipeItem item6, RecipeItem item7, RecipeItem item8){

        this.recipeItems[0] = item0;
        this.recipeItems[1] = item1;
        this.recipeItems[2] = item2;
        this.recipeItems[3] = item3;
        this.recipeItems[4] = item4;
        this.recipeItems[5] = item5;
        this.recipeItems[6] = item6;
        this.recipeItems[7] = item7;
        this.recipeItems[8] = item8;


    }





    public boolean equals(InventoryItem[] items){
        int numItems = 0;
        int minX = 100;
        int minY = 100;
        int maxX = -100;
        int maxY = -100;

        int width = 0;
        int height = 0;

        boolean eq = true;
        for (int i = 0; i < items.length; i++) {
            int x = i % 3;
            int y = i / 3;
            if(items[i] != null){
                if(!items[i].isNull()){
                    numItems++;
                    if(x <= minX) minX = x;
                    if(x >= maxX) maxX = x;
                    if(y <= minY) minY = y;
                    if(y >= maxY) maxY = y;
                }
            }
        }

        width = maxX - minX + 1;
        height = maxY - minY + 1;


        if(numItems > 0){
            //System.out.println("num items" + numItems);

            if(this.dimensionX == width && this.dimensionY == height){
                for (int i = 0; i < items.length; i++) {
                    int x = i % 3;
                    int y = i / 3;

                    if (x >= minX && x <= maxX && y >= minY && y <= maxY){

                        int x_ = x - minX;
                        int y_ = y - minY;
                        InventoryItem item = items[i];
                        int ii = y_ * 3 + x_;


                        if(this.recipeItems[ii] == null){
                            if(!item.isNull()){
                                eq = false;
                                break;
                            }
                        } else {
                            if(this.recipeItems[ii].equals(item.getId(), item.isItem())){

                            } else {
                                eq = false;
                                break;
                            }

                        }


                    }



                }


            } else {
                eq = false;
            }



        } else {
            eq = false;
        }


        return eq;


    }




    public InventoryItem getResult() {
        return new InventoryItem(result.getId(), result.getNum(), result.isItem());
    }
}
