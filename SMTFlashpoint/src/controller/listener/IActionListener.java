/**
 * 
 */
package controller.listener;

import model.Action;
import model.Actiontype;
import model.Block;

/**
 * @author Relic
 *
 */
public interface IActionListener {
	void actionSelected(Action type,Block start, Block ziel);


}
