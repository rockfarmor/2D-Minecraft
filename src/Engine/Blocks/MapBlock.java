package Engine.Blocks;

public class MapBlock {

    public int x;
    public int y;
    public int z;
    public int id;

    public MapBlock(int id, int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;
        this.id = id;
    }

    public MapBlock(int id, int x, int y){
        this.x = x;
        this.y = y;
        this.z = 1;
        this.id = id;
    }




}
