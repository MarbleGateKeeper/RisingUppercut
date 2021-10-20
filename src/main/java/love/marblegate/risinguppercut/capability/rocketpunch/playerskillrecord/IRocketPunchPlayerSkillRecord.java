package love.marblegate.risinguppercut.capability.rocketpunch.playerskillrecord;

public interface IRocketPunchPlayerSkillRecord {
    int getEffectiveChargeTime();

    void setEffectiveChargeTime(int effectiveChargeTime);

    void setTimer(int timer);

    int getTimer();

    void setDamagePerEffectiveCharge(float damage);

    float getDamagePerEffectiveCharge();

    void setSpeedIndex(double index);

    double getSpeedIndex();

    void setIgnoreArmor(boolean ignoreArmor);

    boolean ignoreArmor();

    void setHealing(boolean healing);

    boolean healing();

    void setIsFireDamage(boolean isFireDamage);

    boolean isFireDamage();

    void setKnockbackIndex(double index);

    double getKnockbackSpeedIndex();

    void setShouldLoot(int lootLevel);

    int shouldLoot();

    void setDirection(double dX, double dZ);

    double getDirectionX();

    double getDirectionZ();

    void decrease();

    void clear();
}
