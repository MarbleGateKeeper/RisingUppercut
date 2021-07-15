package love.marblegate.risinguppercut.entity.rocketpunchhit;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class RocketPunchEffectModel extends EntityModel<RocketPunchHitEntity> {
    final ModelRenderer body;

    public RocketPunchEffectModel() {
        body = new ModelRenderer(this);
    }

    @Override
    public void setRotationAngles(RocketPunchHitEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {

    }
}
