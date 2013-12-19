/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Test;

import Y2.ProAI;
import battleships.BattleshipAI;
import battleships.exampleplayers.RandomPlayer;
import battleships.exampleplayers.SystematicShooter;
import battleships.game.Game;
import battleships.game.Tournament;
import battleships.game.Tournament.Participant;
import battleships.game.Tournament.TournamentGame;
import captainobvious.CO;
import r1.NLBB;
import phdblackbeard.PHDBB;
import scurvymcpirate.SMCP;


/**
 *
 * @author Tobias
 */
public class TournamentTest
{
    public static void main(String[] args)
    {
        int[] ships = {2,3,3,4,5};
        Game game = new Game(10,10, ships);
        TournamentGame[] games = {new TournamentGame(game, 100)};
        BattleshipAI[] ais = {new RandomPlayer(), new SystematicShooter(), new CO(), new SMCP(), new PHDBB(), new ProAI(), new NLBB()};
        Tournament t = new Tournament(games, ais);
        Participant[] result = t.runTournament(true);
        for(Participant p : result)
        {
            System.out.println(p);
        }
    }
}
