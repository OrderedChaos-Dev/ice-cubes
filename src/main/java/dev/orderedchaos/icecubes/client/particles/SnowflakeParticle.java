package dev.orderedchaos.icecubes.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

public class SnowflakeParticle extends TextureSheetParticle  {

  private final SpriteSet spriteSet;

  public SnowflakeParticle(ClientLevel level, double x, double y, double z, SpriteSet spriteSet) {
    super(level, x, y, z);
    this.spriteSet = spriteSet;
    this.gravity = 0.05f;
    this.setSpriteFromAge(spriteSet);
  }

  @Override
  public void tick() {
    this.setSpriteFromAge(spriteSet);
    super.tick();
  }

  @Override
  public ParticleRenderType getRenderType() {
    return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
  }

  @OnlyIn(Dist.CLIENT)
  public static class Provider implements ParticleProvider<SimpleParticleType> {
    private final SpriteSet spriteSet;

    public Provider(SpriteSet spriteSet) {
      this.spriteSet = spriteSet;
    }

    @Nullable
    @Override
    public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
      return new SnowflakeParticle(level, x, y, z, spriteSet);
    }
  }
}
