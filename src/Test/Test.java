/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import battleships.BattleshipAI;
import battleships.exampleplayers.RandomPlayer;
import battleships.exampleplayers.SystematicShooter;
import battleships.game.Game;
import battleships.game.GameResult;
import captainobvious.CO;
import nobellaureateblackbart.NLBB;
import phdblackbeard.PHDBB;
import scurvymcpirate.SMCP;

/**
 *
 * @author Simon
 */
public class Test {
    public static void main(String[] args) {
        int pointsA = 0;
        int pointsB = 0;
        int pointsC = 0;
        int[] ships = {2,3,3,4,5};
        Game game = new Game(10,10, ships);
        
        BattleshipAI pa = new CO();
        BattleshipAI pb = new NLBB();
        for(int i = 0; i < 10; ++i)
        {
            GameResult res = game.playRound(pa, pb);
            if(res == GameResult.AWINS) pointsA++;
            else if(res == GameResult.BWINS) pointsB++;
            else if(res == GameResult.DRAW) pointsC++;
        }
        System.out.println(pa.getName() + ": " + pointsA);
        System.out.println(pb.getName() + ": " + pointsB);
        System.out.println("Draw: " + ": " + pointsC);
        
        
    }
}
