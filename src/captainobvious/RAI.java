package captainobvious;
import battleships.BattleshipAI;
import battleships.Board;
import battleships.Fleet;
import battleships.Position;
/**
 *
 * @author Simon og Kasper
 */



public class RAI implements BattleshipAI{

    
    private final String aiName = "Captain Obvious";
    private Position current; 
    
    private int matchNumber; 
    
    
    public RAI(){
    
    
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
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void incoming(Position pstn) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Position getFireCoordinates(Fleet fleet) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void hitFeedBack(boolean bln, Fleet fleet) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
}
