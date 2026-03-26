package me.drex.instantfeedback.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.LightLayer;


@Environment(EnvType.CLIENT)
public class CreakingEyesParticle extends TextureSheetParticle {
    private final SpriteSet sprites;

    CreakingEyesParticle(ClientLevel clientLevel, double x, double y, double z, SpriteSet spriteSet) {
        super(clientLevel, x, y, z);
        this.sprites = spriteSet;
        this.quadSize *= 2.0f + (this.random.nextFloat() * 0.5f);

        this.z += (this.random.nextFloat() - 0.5) * 0.01;
        this.x += (this.random.nextFloat() - 0.5) * 0.01;

        this.hasPhysics = false;

        this.alpha = 0.0f;

        this.setSpriteFromAge(spriteSet);

        this.lifetime = 200 + this.random.nextInt(40);

    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public int getLightColor(float partialTick) {
        return net.minecraft.client.renderer.LightTexture.pack(15, 15);
    }

    private boolean isFadingOut = false;

    @Override
    public void tick() {
        super.tick();
        this.setSpriteFromAge(this.sprites);

        if (this.age % 5 == 0 && !this.isFadingOut) {
            BlockPos pos = BlockPos.containing(this.x, this.y, this.z);

            long time = this.level.getDayTime() % 24000;
            boolean isNight = time >= 13000 && time <= 23000;
            int blockLight = this.level.getBrightness(LightLayer.BLOCK, pos);

            boolean playerTooClose = false;
            double maxHorizontalDistSq = 36.0;
            for (net.minecraft.world.entity.player.Player player : this.level.players()) {
                double dx = this.x - player.getX();
                double dz = this.z - player.getZ();
                double dy = Math.abs(this.y - player.getY());

                if ((dx * dx + dz * dz) <= maxHorizontalDistSq && dy < 15.0) {
                    playerTooClose = true;
                    break;
                }
            }

            if (playerTooClose) {
                this.isFadingOut = true;
                this.lifetime = this.age + 5;
            } else if (!isNight || blockLight >= 4) {
                this.isFadingOut = true;
                this.lifetime = this.age + 10;
            }
        }

        int ticksUntilDeath = this.lifetime - this.age;

        if (this.isFadingOut) {
            this.alpha = Math.max(0.0f, (float) ticksUntilDeath / 10.0f);
        } else if (ticksUntilDeath < 20) {
            this.alpha = (float) ticksUntilDeath / 20.0f;
        } else if (this.age < 20) {
            this.alpha = (float) this.age / 20.0f;
        } else {
            this.alpha = 1.0f;
        }

        if (this.age >= this.lifetime) {
            this.remove();
        }
    }

    @Environment(EnvType.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType particleOptions, ClientLevel clientLevel, double d, double e, double f, double g, double h, double i) {
            return new CreakingEyesParticle(clientLevel, d, e, f, this.sprites);
        }
    }
}