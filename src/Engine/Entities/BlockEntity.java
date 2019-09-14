package Engine.Entities;

import Engine.Blocks.Block;
import Engine.Constants;
import Engine.Enum.BlockSolidType;
import Engine.Particles.ParticleFactory;
import Engine.World.World;
import Engine.javax.vecmath.Vector2d;

import java.awt.*;

public class BlockEntity extends Entity{

    private Block block;
    private int layer;



    public BlockEntity(double x, double y, Block block, World state) {
        super(x, y, state);
        this.block = block;
        this.layer = 1;
    }

    public BlockEntity(Vector2d pos, Block block, World state) {
        super(pos, state);
        this.block = block;
    }

    @Override
    public void update(double dt) {

        if (!this.getRemove()) {



            this.getVelocity().setY(this.getVelocity().getY() + this.getState().getGravity().getY() * dt * 0.6);
            this.getPosition().set(this.getPosition().getX() + this.getVelocity().getX() * dt, this.getPosition().getY() + this.getVelocity().getY() * dt);


            Vector2d posVec = Constants.PosToTilePos(this.getPosition());
            Block b = getState().getBlock((int)posVec.getX(), (int)posVec.getY() +1, this.layer);

            if (b.getBlockId() == Block.Air || b.getSolidType() == BlockSolidType.Fluid) {
            } else {
                if (b.getBlockId() != Constants.blockHandler.getBlockId("Torch")) {
                    if(b.getBlockId() != 0)
                        getState().setBlockNoUpdate(0, (int) posVec.getX(), (int) posVec.getY(), this.layer);
                    getState().setBlock(this.block.getBlockId(), (int) posVec.getX(), (int) posVec.getY(), this.layer, 0, true, false);
                    getState().addEmitter(ParticleFactory.BlockParticle((int)this.getPosition().getX(), (int)this.getPosition().getY(), this.block.getBlockId()));
                }
                this.setRemove(true);
            }



        }
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    @Override
    public void render(Graphics2D g2d) {
        if (!this.getRemove()) {

            g2d.drawImage(Constants.blockHandler.getBlockInfo(this.block.getBlockId()).img, (int) this.getPosition().getX() - 8, (int) this.getPosition().getY() - 8, null);
            if(this.getLayer() == 0){
                g2d.setColor(new Color(0,0,0, 100));
                g2d.fillRect((int) this.getPosition().getX() - 8, (int) this.getPosition().getY() - 8, 16, 16);
            }


        }
    }





}
