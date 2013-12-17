/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scurvymcpirate;

import battleships.BattleshipAI;
import battleships.Board;
import battleships.Fleet;
import battleships.Position;
import battleships.Ship;

import java.util.Random;
import java.util.Stack;

/**
 *
 * @author Simon
 */
public class SMCP implements BattleshipAI {

    private final String aiName = "Scurvy MC Pirate";
    private int shotIncrement;
    private int shotSpray;
    private int shotX;
    private int shotY;
    private Position lastHit;
    private Stack shotStack;
    
    private Map map;
    
    private Random rnd;
    private int sizeX = 10;
    private int sizeY = 10;
    private int c;

    public SMCP() {
        this.map = new Map();
        this.shotStack = new Stack();
        this.lastHit = new Position(0, 0);
        this.rnd = new Random();
        this.shotSpray = 2;
        this.shotIncrement = 0;
        c = 0;
    }

    @Override
    public String getName() {
        return aiName;
    }

    @Override
    public void newMatch(int i) {
        map.trendSetter(true);
        System.out.println("new match begun");
    }

    @Override
    public void placeShips(Fleet fleet, Board board) {
        //System.out.println(c);
        
        map.trendSetter(false);
        shotX = 42;
        shotY = 0;
        sizeX = board.sizeX();
        sizeY = board.sizeY();

        for (int i = 0; i < fleet.getNumberOfShips(); ++i) {

            Ship s = fleet.getShip(i);
            boolean vertical = rnd.nextBoolean();
            boolean finished = false;
            Position pos = new Position(1, 1);
            if (vertical) {
                while (!finished) {
                    
                    int x = rnd.nextInt(sizeX);
                    int y = rnd.nextInt(sizeY - (s.size() - 1));
                    
                    if (!map.checkField(x, y, s, vertical)) {
                        pos = new Position(x, y);
                        map.setUsShip(x, y, s, vertical);
                        finished = true;
                    }
                }
            } else {
                while (!finished) {
                    int x = rnd.nextInt(sizeX - (s.size() - 1));
                    int y = rnd.nextInt(sizeY);
                    if (!map.checkField(x, y, s, vertical)) {
                        pos = new Position(x, y);
                        
                        map.setUsShip(x, y, s, vertical);
                        
                        finished = true;
                    }
                }
            }
            board.placeShip(pos, s, vertical);
            System.out.println(s.size());
        }
        //showShip();
        
    }

    @Override
    public void incoming(Position pstn) {
        this.map.incOppShotTrend(pstn.x, pstn.y);
    }

    @Override
    public Position getFireCoordinates(Fleet fleet) {
//        if (shotSpray < fleet.getShip(0).size()) {
//            shotSpray = fleet.getShip(0).size();
//        }
        c++;
        

        boolean stop = false;

        if (this.shotX == 42) {
            this.shotX = 0;
            this.map.setShot(shotX,shotY, true);
            return new Position(this.shotX, this.shotY);
        }

        if (!this.shotStack.empty()) {
            Position tmp = (Position) this.shotStack.pop();
            this.map.setShot(tmp.x,tmp.y, true);
            this.lastHit = new Position(tmp.x, tmp.y);
            return tmp;
        } else {
            while (!stop) {
                if (this.shotX < this.sizeX - this.shotSpray) {
                    this.shotX = this.shotX + this.shotSpray;
                    if (!map.getShot(shotX, shotY)){
                        stop = true;
                    }
                } else {
                    if (this.shotY < this.sizeY - 1) {
                        this.shotY++;
                    }
                    incCalc();
                    this.shotX = this.shotIncrement;
                    if (!!map.getShot(shotX, shotY)){
                        stop = true;
                    }
                }
                if ((shotX == 9 && shotY == 9) || (shotX == 8 && shotY == 9)){
                    stop = true;
                }
            }
        }
        
        this.map.setShot(shotX,shotY, true);
        this.lastHit = new Position(this.shotX, this.shotY);
        return new Position(this.shotX, this.shotY);
    }

    @Override
    public void hitFeedBack(boolean bln, Fleet fleet) {
        int x = this.lastHit.x;
        int y = this.lastHit.y;
        if (bln) {
            this.map.setHit(x,y, true);
            if (x - 1 >= 0) {
                if (!this.map.getShot(x - 1, y)) {
                    this.shotStack.push(new Position(x - 1, y));
                }
            }
            if (y - 1 >= 0) {
                if (!this.map.getShot(x, y-1)) {
                    this.shotStack.push(new Position(x, y - 1));
                }
            }
            if (y + 1 < this.sizeY) {
                if (!this.map.getShot(x, y +1)) {
                    this.shotStack.push(new Position(x, y + 1));
                }
            }
            if (x + 1 < this.sizeX) {
                if (!this.map.getShot(x + 1, y)) {
                    this.shotStack.push(new Position(x + 1, y));
                }
            }
        }
    }

    
    
    
    // Private methods here
    private void incCalc() {
        if (!(shotIncrement < shotSpray - 1)) {
            shotIncrement = 0;
        } else {
            shotIncrement++;
        }
    }
}