package Engine.Inventory;

import Engine.Blocks.BlockType;

import java.util.ArrayList;

public class CraftingHandler {


    private ArrayList<CraftingRecipe> recipes;


    public CraftingHandler() {

        recipes = new ArrayList<>();
        this.init();
    }

    public InventoryItem checkRecipi(InventoryItem[] items){

        InventoryItem result = new InventoryItem(0, 0, false);


        for (int i = 0; i < recipes.size(); i++) {
            CraftingRecipe recipe = this.recipes.get(i);

            if(recipe.equals(items)){
                result = recipe.getResult();
                break;
            }


        }



        return result;
    }





    private void addRecipe(CraftingRecipe recipe){
        this.recipes.add(recipe);
    }


    private void init(){


        //Oak planks
        CraftingRecipe planksOak = new CraftingRecipe(1,1, new InventoryItem(14,4, false));
        planksOak.addRecipe(new RecipeItem(36, false));
        addRecipe(planksOak);

        //Spruce planks
        CraftingRecipe planksSpruce = new CraftingRecipe(1,1, new InventoryItem(15,4, false));
        planksSpruce.addRecipe(new RecipeItem(37, false));
        addRecipe(planksSpruce);

        //Birch planks
        CraftingRecipe planksBirch = new CraftingRecipe(1,1, new InventoryItem(16,4, false));
        planksBirch.addRecipe(new RecipeItem(38, false));
        addRecipe(planksBirch);

        //Jungle planks
        CraftingRecipe planksJungle = new CraftingRecipe(1,1, new InventoryItem(17,4, false));
        planksJungle.addRecipe(new RecipeItem(39, false));
        addRecipe(planksJungle);

        //Acacia planks
        CraftingRecipe planksAcacia = new CraftingRecipe(1,1, new InventoryItem(18,4, false));
        planksAcacia.addRecipe(new RecipeItem(40, false));
        addRecipe(planksAcacia);

        //DarkOak planks
        CraftingRecipe planksDarkOak = new CraftingRecipe(1,1, new InventoryItem(19,4, false));
        planksDarkOak.addRecipe(new RecipeItem(41, false));
        addRecipe(planksDarkOak);

        //Sticks
        CraftingRecipe sticks = new CraftingRecipe(1,2, new InventoryItem(51,4,  true));
        sticks.addHorizontalRecipe(new RecipeItem(BlockType.Planks), new RecipeItem(BlockType.Planks));
        addRecipe(sticks);

        //Torch
        CraftingRecipe torch = new CraftingRecipe(1, 2, new InventoryItem(115, 4, false));
        torch.addHorizontalRecipe(new RecipeItem(45, true), new RecipeItem(51, true));
        addRecipe(torch);

        //Crafting table
        CraftingRecipe craftingTable = new CraftingRecipe(2, 2, new InventoryItem(120, 1, false));
        craftingTable.addRecipe(new RecipeItem(BlockType.Planks), new RecipeItem(BlockType.Planks), new RecipeItem(BlockType.Planks), new RecipeItem(BlockType.Planks));
        addRecipe(craftingTable);

        //Crafting table
        CraftingRecipe furnance = new CraftingRecipe(3, 3, new InventoryItem(131, 1, false));
        furnance.addRecipe(new RecipeItem(12, false), new RecipeItem(12, false), new RecipeItem(12, false), new RecipeItem(12, false), null, new RecipeItem(12, false), new RecipeItem(12, false), new RecipeItem(12, false), new RecipeItem(12, false));
        addRecipe(furnance);

        //Ladder
        CraftingRecipe ladder = new CraftingRecipe(3, 3, new InventoryItem(133, 3, false));
        ladder.addRecipe(new RecipeItem(51, true), null, new RecipeItem(51, true), new RecipeItem(51, true),new RecipeItem(51, true),new RecipeItem(51, true), new RecipeItem(51, true), null,new RecipeItem(51, true));
        addRecipe(ladder);

        int slabId = 105;
        int blockId = 1;
        //Stone slab
        CraftingRecipe slab = new CraftingRecipe(3,1, new InventoryItem(105,6, false));
        slab.addVerticalRecipe(new RecipeItem(1,false), new RecipeItem(1,false), new RecipeItem(1,false));
        addRecipe(slab);

        //sandstone
        slabId = 106;
        blockId = 53;
        slab = new CraftingRecipe(3,1, new InventoryItem(slabId,6, false));
        slab.addVerticalRecipe(new RecipeItem(blockId,false), new RecipeItem(blockId,false), new RecipeItem(blockId,false));
        addRecipe(slab);

        //Cobble
        slabId = 107;
        blockId = 12;
        slab = new CraftingRecipe(3,1, new InventoryItem(slabId,6, false));
        slab.addVerticalRecipe(new RecipeItem(blockId,false), new RecipeItem(blockId,false), new RecipeItem(blockId,false));
        addRecipe(slab);

        //Brick
        //slabId = 108;
        //blockId = 1;
        //Stone brick, nether brick, quartz 109,110,111






        //Test diamond pickaxe
        CraftingRecipe diamondPickaxe = new CraftingRecipe(3,3, new InventoryItem(22,1, true));
        diamondPickaxe.addRecipe(new RecipeItem(49, true), new RecipeItem(49, true), new RecipeItem(49, true), null, new RecipeItem(51, true),null,null,new RecipeItem(51, true),null);
        addRecipe(diamondPickaxe);



        /*CraftingRecipe planks0 = new CraftingRecipe(1, 1, new InventoryItem(14, 4, false));
        planks0.addRecipe(new RecipeItem(36, false));
        addRecipe(planks0);

        CraftingRecipe sticks = new CraftingRecipe(1,2, new InventoryItem(51,4,  true));
        sticks.addHorizontalRecipe(new RecipeItem(BlockType.Planks), new RecipeItem(BlockType.Planks));

        addRecipe(sticks);

        CraftingRecipe torch = new CraftingRecipe(1, 2, new InventoryItem(115, 16, false));
        torch.addHorizontalRecipe(new RecipeItem(45, true), new RecipeItem(51, true));
        addRecipe(torch);

        CraftingRecipe craftingTable = new CraftingRecipe(2, 2, new InventoryItem(120, 1, false));
        craftingTable.addRecipe(new RecipeItem(BlockType.Planks), new RecipeItem(BlockType.Planks), new RecipeItem(BlockType.Planks), new RecipeItem(BlockType.Planks));

        addRecipe(craftingTable);*/




    }



}
