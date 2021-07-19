package love.marblegate.risinguppercut.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class BlankRender<T extends Entity> extends EntityRenderer<T> {
    //Do not need Renderer;
    public BlankRender(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public void render(T entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }


    @Override
    public ResourceLocation getEntityTexture(T entity) {
        return null;
    }
}
