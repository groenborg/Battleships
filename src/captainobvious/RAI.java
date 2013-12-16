package captainobvious;

import battleships.BattleshipAI;
import battleships.Board;
import battleships.Fleet;
import battleships.Position;

/**
 *
 * @author Simon og Kasper
 */
public class RAI implements BattleshipAI {

    private final String aiName = "Captain Obvious";
    private Position current;
    private Field[][] map;
    private int matchNumber;
    private int sizeX = 10;
    private int sizeY = 10;

    public RAI() {
        this.map = constructMap();
    }

    @Override
    public String getName() {
        return aiName;
    }

    @Override
    public void newMatch(int i) {
        
    }

    @Override
    public void placeShips(Fleet fleet, Board board) {
        resetMapFields(false);




    }

    @Override
    public void incoming(Position pstn) {
        
        
    }

    @Override
    public Position getFireCoordinates(Fleet fleet) {
        throw new UnsupportedOperationException("Not supported yet.");

    }

    @Override
    public void hitFeedBack(boolean bln, Fleet fleet) {
    }

    // Private methods here
    private Field[][] constructMap() {
        Field[][] mapField = new Field[sizeX][sizeY];
        for (int x = 0; x < this.sizeX; ++x) {
            for (int y = 0; y < this.sizeY; ++y) {
                mapField[x][y] = new Field();
            }
        }
        return mapField;
    }

    private void resetMapFields(boolean all) {
        for (int x = 0; x < this.sizeX;) {
            for (int y = 0; y < this.sizeY; ++y) {
                if(all){
                    
                }else{
                    this.map[x][y].reset();
                }
                
            }
        }
    }
}
