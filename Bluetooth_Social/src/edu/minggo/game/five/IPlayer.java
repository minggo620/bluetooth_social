package edu.minggo.game.five;

import java.util.List;

/**
 * 玩家接口
 * @author minggo
 * @created 2013-2-27下午07:05:12
 */
public interface IPlayer {
	//下一步棋子，传入对手已经下的棋子集合
	public void run(List<Point> enemyPoints, Point point);

	public boolean hasWin();
	
	public void setChessboard(IChessboard chessboard);
	
	public List<Point> getMyPoints();
}
