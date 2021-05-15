package love.marblegate.risinguppercut.capability.rocketpunch;

public class RocketPunchIndicatorStandardImpl implements IRocketPunchIndicator {
    private int timer;
    private double dX;
    private double dZ;
    private int strength;

    public RocketPunchIndicatorStandardImpl(){
        this.timer = 0;
        this.dX = 0;
        this.dZ = 0;
        this.strength = 0;
    }


    @Override
    public void setTimer(int timer) {
        this.timer = timer;
        this.strength = strength;
    }

    @Override
    public int getTimer() {
        return this.timer;
    }

    @Override
    public int getStrength() {
        return this.strength;
    }

    @Override
    public void setStrength(int strength) {
        this.strength = strength;
    }

    @Override
    public void setDirection(double dX, double dZ) {
        this.dX = dX;
        this.dZ = dZ;
    }

    @Override
    public double getDirectionX() {
        return this.dX;
    }

    @Override
    public double getDirectionZ() {
        return this.dZ;
    }

    @Override
    public void decrease(){
        this.timer -= 1;
        if(timer<0) timer = 0;
    }


    @Override
    public void clear() {
        this.timer = 0;
        this.dX = 0;
        this.dZ = 0;
        this.strength = 0;
    }
}
