package love.marblegate.risinguppercut.capability.rocketpunch;

public class RocketPunchIndicatorStandardImpl implements IRocketPunchIndicator {
    private int timer;

    public RocketPunchIndicatorStandardImpl(){
        this.timer = 0;
    }

    @Override
    public void set(int i){
        this.timer = i;
    }

    @Override
    public int get(){
        return this.timer;
    }

    @Override
    public void decrease(){
        this.timer -= 1;
        if(timer<0) timer = 0;
    }

    @Override
    public void increase(){
        this.timer += 1;
        if(timer>200) timer = 40;
    }

    @Override
    public void clear() {
        this.timer = 0;
    }
}
