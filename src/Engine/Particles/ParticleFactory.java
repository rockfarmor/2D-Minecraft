package Engine.Particles;

import Engine.Entities.Entity;

public class ParticleFactory {


public static ParticleEmitter BlockParticle(final int x, final int y, final int blockId){

    ParticleEmitter emitter = new ParticleEmitter(x, y, ParticleType.BlockParticle);
    emitter.setColorByBlock(blockId);
    emitter.addParticles(20, -45, 45, -120, 0,0.3);
    return emitter;

}

    public static ParticleEmitter Walking(final int x, final int y, final int blockId, Entity e){

        ParticleEmitter emitter = new ParticleEmitter(x, y, ParticleType.BlockParticle);
        emitter.setColorByBlock(blockId);

        int val = -(int)e.getVelocity().getX()/3;

        emitter.addParticles(30, val - 40, val+40, -140, 0,0.6);

        return emitter;

    }
    public static ParticleEmitter Falling(final int x, final int y, final int blockId){

        ParticleEmitter emitter = new ParticleEmitter(x, y, ParticleType.BlockParticle);
        emitter.setColorByBlock(blockId);
        emitter.addParticles(5, -45, 45, -140, 0,0.3);

        return emitter;

    }

public static ParticleEmitter LeafeParticle(final int x, final int y){
    ParticleEmitter emitter = new ParticleEmitter(x, y, ParticleType.Leaf);
    return emitter;
}



}
