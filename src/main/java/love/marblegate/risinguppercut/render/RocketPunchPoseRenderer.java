package love.marblegate.risinguppercut.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import love.marblegate.risinguppercut.capability.rocketpunch.IRocketPunchIndicator;
import love.marblegate.risinguppercut.capability.rocketpunch.RocketPunchIndicator;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class RocketPunchPoseRenderer {
    @SubscribeEvent
    public static void holdGunPost(RenderPlayerEvent.Post event) {
        LazyOptional<IRocketPunchIndicator> rkp_cap = event.getPlayer().getCapability(RocketPunchIndicator.ROCKET_PUNCH_INDICATOR);
        rkp_cap.ifPresent(
                cap -> {
                    if (cap.getTimer() > 0) {
                        PlayerEntity player = event.getPlayer();
                        PlayerModel<AbstractClientPlayerEntity> model = event.getRenderer().getEntityModel();
                        renderArmModel(model, player, event);
                    }
                });
    }

    private static void renderArmModel(PlayerModel<AbstractClientPlayerEntity> model, PlayerEntity player, RenderPlayerEvent event)
    {
        MatrixStack matrix = event.getMatrixStack();
        IVertexBuilder buffer = event.getBuffers().getBuffer(model.getRenderType(((AbstractClientPlayerEntity) player).getLocationSkin()));
        int light = event.getLight();
        int texture = OverlayTexture.NO_OVERLAY;

        model.bipedRightArm.rotationPointX = -MathHelper.cos((float) Math.toRadians(player.renderYawOffset)) * 5.5F;
        model.bipedRightArm.rotationPointY = player.isCrouching() ? 17.5F : 20.5F;
        model.bipedRightArm.rotationPointZ = -MathHelper.sin((float) Math.toRadians(player.renderYawOffset)) * 5.5F;
        model.bipedRightArm.rotateAngleX = 0.0F;
        model.bipedRightArm.rotateAngleY = 0.0F;
        model.bipedRightArm.rotateAngleZ = 0.0F;

        /*
        model.bipedRightArm.rotationPointX = -MathHelper.cos((float) Math.toRadians(player.renderYawOffset)) * 5.5F;
        model.bipedRightArm.rotationPointY = player.isCrouching() ? 17.5F : 20.5F;
        model.bipedRightArm.rotationPointZ = -MathHelper.sin((float) Math.toRadians(player.renderYawOffset)) * 5.5F;
        model.bipedRightArm.rotateAngleX = -1.6F - (player.rotationPitch/90)*1.2F; //-3.0F > -1.65F > -0.0F;
        model.bipedRightArm.rotateAngleY =  (float) -Math.toRadians(player.renderYawOffset) + 3.2F + -Math.abs((player.rotationPitch/90))*-0.05F;
        model.bipedRightArm.rotateAngleZ = 0.0F;
         */


        model.bipedRightArm.showModel=true;
        model.bipedRightArm.render(matrix, buffer, light, texture);
    }
}
