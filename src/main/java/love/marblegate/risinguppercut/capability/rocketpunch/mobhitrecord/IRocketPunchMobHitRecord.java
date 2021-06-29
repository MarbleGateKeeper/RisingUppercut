package love.marblegate.risinguppercut.capability.rocketpunch.mobhitrecord;

public interface IRocketPunchMobHitRecord {
    String getAttackerName();

    void setAttackerName(String name);

    int getStrength();

    void setStrength(int strength);

    void setDirection(double dX, double dZ);

    double getDirectionX();

    double getDirectionZ();

    void clear();
}
