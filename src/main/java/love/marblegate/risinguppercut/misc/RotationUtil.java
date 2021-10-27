package love.marblegate.risinguppercut.misc;

import net.minecraft.world.entity.LivingEntity;

public class RotationUtil {
    // TODO Need Test it was yRot
    public static double getHorizentalLookVecX(LivingEntity entity) {
        float yaw = entity.yRotO;
        float f1 = -yaw * ((float) Math.PI / 180F);
        return Math.sin(f1);
    }

    public static double getHorizentalLookVecZ(LivingEntity entity) {
        float yaw = entity.yRotO;
        float f1 = -yaw * ((float) Math.PI / 180F);
        return Math.cos(f1);
    }
}
