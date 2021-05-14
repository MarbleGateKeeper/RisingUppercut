package love.marblegate.risinguppercut.capability.rocketpunch;

public interface IRocketPunchIndicator {
    void set(int i);

    int get();

    void decrease();

    void increase();

    void clear();
}
