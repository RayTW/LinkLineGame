package link.util;

import java.util.List;

import link.util.astar.Node;
import link.util.astar.PathFinder;
import link.util.astar.Point;

public class AStarVisit {
	public final static int[] WALL = { 2 };
	private PathFinder mFinder;

	public AStarVisit() {
		AStarVisitInit();
	}

	public void AStarVisitInit() {
		mFinder = new PathFinder();
	}

	/**
	 * 走訪迷官，回傳已取得路徑的陣列(求單一解,不為最短路徑)
	 * 
	 * @param maze
	 *            迷宮本身(maze需設定指定的內容值,0:可走的點,1:已走過的點(不可設定此數值),2:牆壁)
	 * @param startX
	 *            起點x
	 * @param startY
	 *            起點y
	 * @param endX
	 *            終點x
	 * @param endY
	 *            終點y
	 * @return
	 */
	public String find(int[][] maze, int startX, int startY, int endX, int endY) {
		return find(maze, new Point(startX, startY), new Point(endX, endY));
	}

	/**
	 * 走訪迷官，回傳已取得路徑的陣列(求單一解,不為最短路徑)
	 * 
	 * @param maze
	 *            迷宮本身(maze需設定指定的內容值,0:可走的點,1:已走過的點(不可設定此數值),2:牆壁)
	 * @param start
	 * @param end
	 * @return
	 */
	public String find(int[][] maze, Point start, Point end) {
		String path = "";
		List list = mFinder.searchPath(maze, WALL, start, end);

		path = getMaxePath(list);
		list.clear();
		return path;
	}

	/**
	 * 取得迷宮的路徑字串，例如:
	 * 
	 * @param maze
	 * @return
	 */
	public String getMaxePath(List list) {
		// System.out.println(list);
		StringBuffer path = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			Node node = (Node) list.get(i);

			if (path.length() > 0) {
				path.append(",");
			}
			path.append(node.mPos.mX);
			path.append(":");
			path.append(node.mPos.mY);
		}
		// System.out.println("總步數==" + ary.size());
		return path.toString();
	}

	/**
	 * 印出迷宮
	 * 
	 * @param srcMaze
	 *            原本的迷宮
	 * @param path
	 *            路徑
	 */
	public void print(int[][] srcMaze, String path) {
		int[][] answerMaze = arraycopy(srcMaze);

		// 將解題路徑放到迷宮上
		String[] pathAry = path.split("[,]");
		for (int i = 0; i < pathAry.length; i++) {
			String[] xy = pathAry[i].split("[:]");
			int x = Integer.parseInt(xy[0]);
			int y = Integer.parseInt(xy[1]);
			answerMaze[x][y] = 1;
		}
	}

	/**
	 * 複制一份迷宮
	 * 
	 * @param maze
	 * @return
	 */
	public int[][] arraycopy(int[][] maze) {
		int[][] copyMaze = new int[maze.length][];

		for (int i = 0; i < maze.length; i++) {
			copyMaze[i] = new int[maze[i].length];
			System.arraycopy(maze[i], 0, copyMaze[i], 0, maze[i].length);
		}
		return copyMaze;
	}

	public static void main(String[] args) {
		System.out.println("A-Star尋徑");
		demo1();

	}

	// 求單一解
	private static void demo1() {
		AStarVisit AStarVisit = new AStarVisit();
		System.out.println("求單一解-----");
		// 起點
		int startX = 1;
		int startY = 1;

		// 終點
		int endX = 5;
		int endY = 5;
		// 迷宮
		int[][] maze = new int[][] { { 2, 2, 2, 2, 2, 2, 2 }, { 2, 0, 0, 0, 0, 0, 2 }, { 2, 0, 2, 0, 2, 0, 2 },
				{ 2, 0, 0, 2, 0, 2, 2 }, { 2, 2, 0, 0, 0, 0, 2 }, { 2, 0, 0, 0, 0, 0, 2 }, { 2, 2, 2, 2, 2, 2, 2 } };

		System.out.println("原本的迷宮-------beign-----");
		// AStarVisit.print(maze);// 原本的迷宮
		System.out.println("原本的迷宮-------end-----");

		System.out.println("找到路徑的迷宮-------beign-----");
		String path = AStarVisit.find(maze, startX, startY, endX, endY);
		System.out.println("路徑=" + path);
		AStarVisit.print(maze, path);// 加上走過路徑的迷宮
		System.out.println("找到路徑的迷宮-------end-----");

	}

}
