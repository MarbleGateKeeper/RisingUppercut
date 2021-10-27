package love.marblegate.risinguppercut.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class BlankRender<T extends Entity> extends EntityRenderer<T> {
    //Do not need Renderer;
    public BlankRender(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(T entityIn, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
    }


    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return null;
    }
}
