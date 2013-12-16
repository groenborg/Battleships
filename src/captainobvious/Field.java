package captainobvious;

public class Field {

    private boolean shot;
    private boolean hit;
    private boolean resolved;
    private boolean usShip;
    private int oppShipTrend;
    private int oppShotTrend;

    public Field() {
        this.shot = false;
        this.hit = false;
        this.resolved = false;
        this.usShip = false;
        this.oppShipTrend = 0;
        this.oppShotTrend = 0;
    }

    public void reset() {
        this.shot = false;
        this.hit = false;
        this.resolved = false;
        this.usShip = false;
    }

    public void setShot(boolean yes) {
        this.shot = yes;
    }

    public void setHit(boolean yes) {
        this.hit = yes;
    }

    public void setResolved(boolean yes) {
        this.resolved = yes;
    }

    public void setUsShip(boolean yes) {
        this.usShip = yes;
    }
    
    public void incOppShipTrend(int inc){
        this.oppShipTrend = this.oppShipTrend + inc;
    }
    
    public void incOppShotTrend(int inc){
        this.oppShotTrend = this.oppShotTrend + inc;
    }

    public boolean getShot() {
        return this.shot;
    }

    public boolean getHit() {
        return this.shot;
    }

    public boolean getResolved() {
        return this.shot;
    }

    public boolean getUsShip() {
        return this.shot;
    }

    public int getOppShipTrend() {
        return this.oppShipTrend;
    }

    public int getOppShotTrend() {
        return this.oppShotTrend;
    }
}
