package love.marblegate.risinguppercut.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

public class RotationUtil {
    public static double getHorizentalLookVecX(LivingEntity entity){
        float yaw = entity.rotationYaw;
        float f1 = -yaw * ((float)Math.PI / 180F);
        return MathHelper.sin(f1);
    }

    public static double getHorizentalLookVecZ(LivingEntity entity){
        float yaw = entity.rotationYaw;
        float f1 = -yaw * ((float)Math.PI / 180F);
        return MathHelper.cos(f1);
    }
}
