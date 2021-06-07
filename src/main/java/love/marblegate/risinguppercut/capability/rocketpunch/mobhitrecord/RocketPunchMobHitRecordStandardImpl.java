package love.marblegate.risinguppercut.capability.rocketpunch.mobhitrecord;

public class RocketPunchMobHitRecordStandardImpl implements IRocketPunchMobHitRecord {
    private int strength;
    private String attackerName;

    public RocketPunchMobHitRecordStandardImpl(){
        this.strength = 0;
        this.attackerName = "";
    }

    @Override
    public String getAttackerName() {
        return attackerName;
    }

    @Override
    public void setAttackerName(String name) {
        this.attackerName = name;
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
    public void clear() {
        this.strength = 0;
        this.attackerName = "";
    }
}
