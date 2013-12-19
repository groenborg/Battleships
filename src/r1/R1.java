/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package r1;

import battleships.BattleshipAI;

/**
 *
 * @author Simon
 */
public class R1 {

    public static BattleshipAI getAI() {
        return new NLBB();
    }
}
