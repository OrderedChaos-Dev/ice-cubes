package dev.orderedchaos.icecubes.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.orderedchaos.icecubes.IceCubes;
import dev.orderedchaos.icecubes.common.entities.IceCubeEntity;
import net.minecraft.client.model.SlimeModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.SlimeOuterLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class IceCubeRenderer extends MobRenderer<IceCubeEntity, SlimeModel<IceCubeEntity>> {
  private static final ResourceLocation ICE_CUBE_LOCATION = ResourceLocation.fromNamespaceAndPath(IceCubes.MOD_ID, "textures/entity/ice_cube.png");

  public IceCubeRenderer(EntityRendererProvider.Context context) {
    super(context, new SlimeModel<>(context.bakeLayer(ModelLayers.SLIME)), 0.25F);
    this.addLayer(new SlimeOuterLayer<>(this, context.getModelSet()));
  }

  @Override
  public void render(IceCubeEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
    this.shadowRadius = 0.25F * (float) pEntity.getSize();
    super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
  }

  @Override
  protected boolean isShaking(IceCubeEntity pEntity) {
    return super.isShaking(pEntity) || pEntity.isMelting();
  }

  @Override
  protected void scale(IceCubeEntity pLivingEntity, PoseStack pPoseStack, float pPartialTickTime) {
    float f = 0.999F;
    pPoseStack.scale(0.999F, 0.999F, 0.999F);
    pPoseStack.translate(0.0F, 0.001F, 0.0F);
    float f1 = (float) pLivingEntity.getSize();
    float f2 = Mth.lerp(pPartialTickTime, pLivingEntity.oSquish, pLivingEntity.squish) / (f1 * 0.5F + 1.0F);
    float f3 = 1.0F / (f2 + 1.0F);
    pPoseStack.scale(f3 * f1, 1.0F / f3 * f1, f3 * f1);
  }

  @Override
  public ResourceLocation getTextureLocation(IceCubeEntity pEntity) {
    return ICE_CUBE_LOCATION;
  }
}