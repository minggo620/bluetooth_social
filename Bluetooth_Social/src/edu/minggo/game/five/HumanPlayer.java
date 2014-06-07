package edu.minggo.game.five;

import java.util.List;
/**
 * 
 * @author minggo
 * @created 2013-2-27обнГ07:04:23
 */
public class HumanPlayer extends BasePlayer implements IPlayer{

	@Override
	public void run(List<Point> enemyPoints,Point p) {
		getMyPoints().add(p);
		allFreePoints.remove(p);
	}
}
