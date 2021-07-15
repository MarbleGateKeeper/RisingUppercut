package love.marblegate.risinguppercut.entity.rocketpunchhit;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;

public class RocketPunchEffectRender extends EntityRenderer<RocketPunchHitEntity> {
    EntityModel<RocketPunchHitEntity> entityModel;

    protected RocketPunchEffectRender(EntityRendererManager renderManager) {
        super(renderManager);
        entityModel = new RocketPunchEffectModel();
    }

    @Override
    public void render(RocketPunchHitEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }


    @Override
    public ResourceLocation getEntityTexture(RocketPunchHitEntity entity) {
        return null;
    }
}
