package love.marblegate.risinguppercut.capability.rocketpunch.playerskillrecord;

public class RocketPunchPlayerSkillRecordStandardImpl implements IRocketPunchPlayerSkillRecord {
    int timer;
    double dX;
    double dZ;
    float damage;
    double speedIndex;
    double knockbackSpeedIndex;
    boolean ignoreArmor;
    boolean healing;
    boolean isFireDamage;
    int shouldLoot;


    public RocketPunchPlayerSkillRecordStandardImpl() {
        timer = 0;
        dX = 0;
        dZ = 0;
        damage = 0;
        speedIndex = 0;
        knockbackSpeedIndex = 0;
        ignoreArmor = false;
        healing = false;
        isFireDamage = false;
        shouldLoot = 0;
    }


    @Override
    public void setTimer(int timer) {
        this.timer = timer;
    }

    @Override
    public int getTimer() {
        return timer;
    }

    @Override
    public void setDamage(float damage) {
        this.damage = damage;
    }

    @Override
    public float getDamage() {
        return damage;
    }

    @Override
    public void setSpeedIndex(double index) {
        speedIndex = index;
    }

    @Override
    public double getSpeedIndex() {
        return speedIndex;
    }

    @Override
    public void setIgnoreArmor(boolean ignoreArmor) {
        this.ignoreArmor = ignoreArmor;
    }

    @Override
    public boolean ignoreArmor() {
        return ignoreArmor;
    }

    @Override
    public void setHealing(boolean healing) {
        this.healing = healing;
    }

    @Override
    public boolean healing() {
        return healing;
    }

    @Override
    public void setIsFireDamage(boolean isFireDamage) {
        this.isFireDamage = isFireDamage;
    }

    @Override
    public boolean isFireDamage() {
        return isFireDamage;
    }

    @Override
    public void setKnockbackIndex(double index) {
        knockbackSpeedIndex = index;
    }

    @Override
    public double getKnockbackSpeedIndex() {
        return knockbackSpeedIndex;
    }

    @Override
    public void setShouldLoot(int shouldLoot) {
        this.shouldLoot = shouldLoot;
    }

    @Override
    public int shouldLoot() {
        return shouldLoot;
    }


    @Override
    public void setDirection(double dX, double dZ) {
        this.dX = dX;
        this.dZ = dZ;
    }

    @Override
    public double getDirectionX() {
        return dX;
    }

    @Override
    public double getDirectionZ() {
        return dZ;
    }

    @Override
    public void decrease() {
        timer -= 1;
        if (timer < 0) timer = 0;
    }


    @Override
    public void clear() {
        timer = 0;
        dX = 0;
        dZ = 0;
        damage = 0;
        speedIndex = 0;
        knockbackSpeedIndex = 0;
        ignoreArmor = false;
        healing = false;
        isFireDamage = false;
        shouldLoot = 0;
    }
}
