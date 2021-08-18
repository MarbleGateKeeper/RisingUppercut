package love.marblegate.risinguppercut.misc;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;

public class RotationUtil {
    public static double getHorizentalLookVecX(LivingEntity entity) {
        float yaw = entity.rotationYaw;
        float f1 = -yaw * ((float) Math.PI / 180F);
        return MathHelper.sin(f1);
    }

    public static double getHorizentalLookVecZ(LivingEntity entity) {
        float yaw = entity.rotationYaw;
        float f1 = -yaw * ((float) Math.PI / 180F);
        return MathHelper.cos(f1);
    }
}
