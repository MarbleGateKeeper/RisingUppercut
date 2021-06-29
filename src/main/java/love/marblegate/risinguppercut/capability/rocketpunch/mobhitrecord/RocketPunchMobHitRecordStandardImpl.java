package love.marblegate.risinguppercut.capability.rocketpunch.mobhitrecord;

public class RocketPunchMobHitRecordStandardImpl implements IRocketPunchMobHitRecord {
    private int strength;
    private double dX;
    private double dZ;
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
    public void clear() {
        this.strength = 0;
        this.attackerName = "";
        this.dZ = 0;
        this.dX = 0;
    }
}
