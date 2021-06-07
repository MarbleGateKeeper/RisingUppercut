package love.marblegate.risinguppercut.capability.rocketpunch.playerskillrecord;

public interface IRocketPunchPlayerSkillRecord {
    void setTimer(int timer);

    int getTimer();

    int getStrength();

    void setStrength(int strength);

    void setDirection(double dX, double dZ);

    double getDirectionX();

    double getDirectionZ();

    void decrease();

    void clear();
}
