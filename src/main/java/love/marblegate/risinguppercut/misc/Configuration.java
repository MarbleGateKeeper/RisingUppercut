package love.marblegate.risinguppercut.misc;

import net.minecraftforge.common.ForgeConfigSpec;

public class Configuration {
    public static final ForgeConfigSpec MOD_CONFIG;

    public static class RisingUppercutConfig{
        public static ForgeConfigSpec.IntValue UPRISING_TIME;
        public static ForgeConfigSpec.IntValue FLOATING_TIME;
        public static ForgeConfigSpec.DoubleValue DAMAGE;
        public static ForgeConfigSpec.IntValue COOLDOWN;
        public static ForgeConfigSpec.DoubleValue RISING_SPEED_INDEX;
    }

    public static class RocketPunchConfig{
        public static ForgeConfigSpec.IntValue MAX_CHARGE_TIME;
        public static ForgeConfigSpec.DoubleValue DAMAGE;
        public static ForgeConfigSpec.IntValue COOLDOWN;
        public static ForgeConfigSpec.DoubleValue MOVEMENT_SPEED_INDEX;
        public static ForgeConfigSpec.DoubleValue KNOCKBACK_SPEED_INDEX;

    }


    static{
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("rising_uppercut");
        RisingUppercutConfig.DAMAGE = builder.comment("Attack damage of rising uppercut.").defineInRange("DAMAGE",8,0.1,1000);
        RisingUppercutConfig.COOLDOWN = builder.comment("Cooldown Time of rising uppercut. Its unit is tick.","Cooldown can not be cancelled due to preventing abuse.").defineInRange("COOLDOWN",60,10,10000);
        RisingUppercutConfig.UPRISING_TIME = builder.comment("Duration of uprising effect of rising uppercut. Its unit is tick.").defineInRange("UPRISING_TIME",8,1,10000);
        RisingUppercutConfig.FLOATING_TIME = builder.comment("Duration of floating effect of rising uppercut. Its unit is tick.").defineInRange("FLOATING_TIME",4,1,10000);
        RisingUppercutConfig.RISING_SPEED_INDEX = builder.comment("Index of rising speed when rising uppercut is activated.","It's not recommended to alter this value greatly.").defineInRange("RISING_SPEED_INDEX",0.1,0.01,0.5);
        builder.pop();

        builder.push("rocket_punch");
        RocketPunchConfig.DAMAGE = builder.comment("Attack damage of rocket punch per tick of charging.").defineInRange("DAMAGE",0.5,0.1,1000);
        RocketPunchConfig.COOLDOWN = builder.comment("Cooldown Time of rocket punch. Its unit is tick.","Cooldown can not be cancelled due to preventing abuse.").defineInRange("COOLDOWN",120,10,10000);
        RocketPunchConfig.MAX_CHARGE_TIME = builder.comment("Max Charge Time of rocket punch. Its unit is tick.").defineInRange("MAX_CHARGE_TIME",20,1,10000);
        RocketPunchConfig.MOVEMENT_SPEED_INDEX = builder.comment("Index of player's movement speed when rocket punch is activated.","It's not recommended to alter this value greatly.").defineInRange("MOVEMENT_SPEED_INDEX",2,0.1,10);
        RocketPunchConfig.KNOCKBACK_SPEED_INDEX = builder.comment("Index of victim's movement speed when one is being knockbacked by rocket punch.","It's not recommended to alter this value greatly.").defineInRange("KNOCKBACK_SPEED_INDEX",2,0.1,10);
        builder.pop();

        MOD_CONFIG = builder.build();
    }
}
