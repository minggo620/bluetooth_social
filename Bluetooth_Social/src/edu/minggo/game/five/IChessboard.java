package edu.minggo.game.five;

import java.util.List;
/**
 * 棋盘接口
 * @author minggo
 * @created 2013-2-27下午07:04:36
 */
public interface IChessboard {
	//取得棋盘最大横坐标
	public int getMaxX();
	//最大纵坐标
	public int getMaxY();
	//取得当前所有空白点，这些点才可以下棋
	public List<Point> getFreePoints();
}
