package link.util;

import java.util.ArrayList;

/**
 * 老鼠走迷宮<br>
 * 
 * 功能如下:<br>
 * findLink():連連看專案，有判斷條件，路徑需兩個轉角以內才回傳。<br>
 * find():取得可從起點至終點一個路徑，不是最短路徑。<br>
 * findMore():取得多組可從起點至終點個路徑。<br>
 * 
 * 以上method的參數裡maze需設定指定的內容值,0:可走的點,1:已走過的點(不可設定此數值),2:牆壁
 * 
 * @author Ray Lee
 *
 */
public class MouseVisit {

	private class Point {
		public int mX;
		public int mY;

		Point(int x, int y) {
			this.mX = x;
			this.mY = y;

		}
	}

	public MouseVisit() {
	}

	public Point createPoint(int x, int y) {
		return new Point(x, y);
	}

	// 連連看專用尋路-------------begin----------------------

	private String mLinkPath = "";

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
	public String findLink(int[][] maze, int startX, int startY, int endX, int endY) {
		return findLink(maze, new Point(startX, startY), new Point(endX, endY));
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
	public String findLink(int[][] maze, Point start, Point end) {
		mLinkPath = "";
		ArrayList<Point> ary = new ArrayList<>();// 存多組路徑
		int[][] copyMaze = arraycopy(maze);// 將要被尋路的迷宮copy一份
		// System.out.println("迷宮長["+copyMaze.length+"]寬["+copyMaze[0].length+"]");
		visitLink(copyMaze, start, end, ary);// 開始走訪迷宮
		return mLinkPath;
	}

	/**
	 * 求單一解,不為最短路徑<br>
	 * 走訪迷宮，已走過的點標記為1，若走到底之後，不是終點，再將走過的點標記還原為0<br>
	 * 
	 * @param maze
	 *            迷宮本身(maze需設定指定的內容值,0:可走的點,1:已走過的點(不可設定此數值),2:牆壁)
	 * @param pt
	 * @param end
	 * @param ary
	 *            單一解路徑
	 * @param pathAry
	 *            存多組解路徑
	 * @return
	 */
	private void visitLink(int[][] maze, Point pt, Point end, ArrayList<Point> ary) {
		if (mLinkPath.length() > 0)
			return;

		maze[pt.mX][pt.mY] = 1;
		ary.add(pt);

		// 走到終點，檢查路徑ary是否轉角符合2次以內，就回傳true
		if (!isNoOver2Corner(ary)) {

			if (pt.mX == end.mX && pt.mY == end.mY) {
				mLinkPath = getMaxePath(ary);
				return;
			}

			if (maze[pt.mX][pt.mY + 1] == 0)
				visitLink(maze, new Point(pt.mX, pt.mY + 1), end, ary);
			if (maze[pt.mX + 1][pt.mY] == 0)
				visitLink(maze, new Point(pt.mX + 1, pt.mY), end, ary);
			if (maze[pt.mX][pt.mY - 1] == 0)
				visitLink(maze, new Point(pt.mX, pt.mY - 1), end, ary);
			if (maze[pt.mX - 1][pt.mY] == 0)
				visitLink(maze, new Point(pt.mX - 1, pt.mY), end, ary);
		}
		ary.remove(ary.size() - 1);
		maze[pt.mX][pt.mY] = 0;

	}

	/**
	 * 判斷是否路徑有2個轉角以上,true:超過兩個轉角,false:兩個轉角以內
	 * 
	 * @param ary
	 * @return
	 */
	private boolean isNoOver2Corner(ArrayList<Point> ary) {
		if (ary.size() > 3) {// 只有3個節點，是不可能轉兩個角
			int corner = 0;// 轉角次數(扣除第一次起步時，會判斷為轉角一次)
			int recordX = 0;
			int recordY = 0;
			Point p = ary.get(0);
			Point p2 = ary.get(1);
			recordX = (p2.mX - p.mX);
			recordY = (p2.mY - p.mY);

			for (int i = 1; i < ary.size() - 1; i++) {
				p = ary.get(i);
				p2 = ary.get(i + 1);
				int nextX = (p2.mX - p.mX);
				int nextY = (p2.mY - p.mY);

				if (recordX != nextX || recordY != nextY) {
					recordX = nextX;
					recordY = nextY;
					corner++;
				}
			}
			if (corner > 2) {
				return true;
			}
		}
		return false;
	}

	// 連連看專用尋路-------------end----------------------

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
		int[][] copyMaze = arraycopy(maze);
		ArrayList<Point> ary = new ArrayList<>();// 解題路徑
		visit(copyMaze, start, end, ary);
		return getMaxePath(ary);
	}

	/**
	 * 求單一解,不為最短路徑<br>
	 * 走訪迷宮，已走過的點標記為1，若走到底之後，不是終點，再將走過的點標記還原為0<br>
	 * 
	 * @param maze
	 *            迷宮本身(maze需設定指定的內容值,0:可走的點,1:已走過的點(不可設定此數值),2:牆壁)
	 * @param pt
	 * @param end
	 * @return
	 */
	private boolean visit(int[][] maze, Point pt, Point end, ArrayList<Point> ary) {
		if (maze[pt.mX][pt.mY] == 0) {
			maze[pt.mX][pt.mY] = 1;
			ary.add(pt);
			if (maze[end.mX][end.mY] != 1) {
				if (!(visit(maze, new Point(pt.mX, pt.mY + 1), end, ary)
						|| visit(maze, new Point(pt.mX + 1, pt.mY), end, ary)
						|| visit(maze, new Point(pt.mX, pt.mY - 1), end, ary)
						|| visit(maze, new Point(pt.mX - 1, pt.mY), end, ary))) {
					ary.remove(ary.size() - 1);
					maze[pt.mX][pt.mY] = 0;
				}
			}
		}
		return maze[end.mX][end.mY] == 1;
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
	private String[] findMore(int[][] maze, int startX, int startY, int endX, int endY) {
		return findMore(maze, new Point(startX, startY), new Point(endX, endY));
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
	public String[] findMore(int[][] maze, Point start, Point end) {
		ArrayList<String> pathAry = new ArrayList<>();// 存多組路徑
		int[][] copyMaze = arraycopy(maze);// 將要被尋路的迷宮copy一份
		visitMore(copyMaze, start, end, new ArrayList<>(), pathAry);// 開始走訪迷宮

		// 將多組解copy轉成String 陣列
		String[] pathStrAry = new String[pathAry.size()];
		System.arraycopy(pathAry.toArray(), 0, pathStrAry, 0, pathAry.size());
		return pathStrAry;
	}

	/**
	 * 求單一解,不為最短路徑<br>
	 * 走訪迷宮，已走過的點標記為1，若走到底之後，不是終點，再將走過的點標記還原為0<br>
	 * 
	 * @param maze
	 *            迷宮本身(maze需設定指定的內容值,0:可走的點,1:已走過的點(不可設定此數值),2:牆壁)
	 * @param pt
	 * @param end
	 * @param ary
	 *            單一解路徑
	 * @param pathAry
	 *            存多組解路徑
	 * @return
	 */
	private void visitMore(int[][] maze, Point pt, Point end, ArrayList<Point> ary, ArrayList<String> paths) {
		maze[pt.mX][pt.mY] = 1;
		ary.add(pt);

		if (pt.mX == end.mX && pt.mY == end.mY) {
			// 走到終點，將路徑轉成字串，存入pathAry
			String path = getMaxePath(ary);
			paths.add(path);
		}

		if (maze[pt.mX][pt.mY + 1] == 0) {
			visitMore(maze, new Point(pt.mX, pt.mY + 1), end, ary, paths);
		}
		if (maze[pt.mX + 1][pt.mY] == 0) {
			visitMore(maze, new Point(pt.mX + 1, pt.mY), end, ary, paths);
		}
		if (maze[pt.mX][pt.mY - 1] == 0) {
			visitMore(maze, new Point(pt.mX, pt.mY - 1), end, ary, paths);
		}
		if (maze[pt.mX - 1][pt.mY] == 0) {
			visitMore(maze, new Point(pt.mX - 1, pt.mY), end, ary, paths);
		}

		ary.remove(ary.size() - 1);
		maze[pt.mX][pt.mY] = 0;
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

	/**
	 * 取得迷宮的路徑字串，例如:
	 * 
	 * @param maze
	 * @return
	 */
	public String getMaxePath(ArrayList<Point> ary) {
		StringBuffer path = new StringBuffer();
		for (int i = 0; i < ary.size(); i++) {
			Point p = ary.get(i);

			if (path.length() > 0) {
				path.append(",");
			}
			path.append(p.mX);
			path.append(":");
			path.append(p.mY);
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
		print(answerMaze);
	}

	/**
	 * 印出迷宮
	 * 
	 * @param maze
	 */
	public void print(int[][] maze) {
		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[i].length; j++) {
				int block = maze[i][j];
				switch (block) {
				case 0:
					System.out.print(" ");
					break;
				case 1:
					System.out.print("o");
					break;
				case 2:
					System.out.print("█");
				}
			}
			System.out.println();
		}
	}

	/**
	 * 是否超出邊界, true:超出邊界,false:未超出邊界
	 * 
	 * @param maze
	 * @param p
	 * @return
	 */
	/*
	 * public static boolean isOverEdge(int[][] maze, Point p){ try{ int a =
	 * maze[p.x][p.y]; }catch(ArrayIndexOutOfBoundsException e){
	 * //System.out.println("超出邊界x["+p.x+"],y["+p.y+"]"); return true; } return
	 * false; }
	 */

	public static void main(String[] args) {
		System.out.println("老鼠走迷宮尋徑");
		demo1();// 求單一解
		// demo2();//求多解
		// demo3();//連連看，判斷2個轉角以內的碉徑才回傳

	}

	// -------以下為測試的method-----------

	// 求單一解
	private static void demo1() {
		MouseVisit MouseVisit = new MouseVisit();
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
		MouseVisit.print(maze);// 原本的迷宮
		System.out.println("原本的迷宮-------end-----");

		System.out.println("找到路徑的迷宮-------beign-----");
		String path = MouseVisit.find(maze, startX, startY, endX, endY);
		System.out.println("路徑=" + path);
		MouseVisit.print(maze, path);// 加上走過路徑的迷宮
		System.out.println("找到路徑的迷宮-------end-----");

	}

	// 求多解
	private static void demo2() {
		MouseVisit MouseVisit = new MouseVisit();
		System.out.println("求多解-----");
		// 起點
		int startX = 1;
		int startY = 1;

		// 終點
		int endX = 5;
		int endY = 5;
		// 迷宮
		int[][] maze = new int[][] { { 2, 2, 2, 2, 2, 2, 2 }, { 2, 0, 0, 0, 0, 0, 2 }, { 2, 0, 2, 0, 0, 0, 2 },
				{ 2, 0, 0, 2, 0, 2, 2 }, { 2, 2, 0, 2, 0, 2, 2 }, { 2, 0, 0, 0, 0, 0, 2 }, { 2, 2, 2, 2, 2, 2, 2 } };
		/*
		 * int[][] maze = new int[][]{ {2, 2, 2, 2, 2, 2, 2}, {2, 0, 0, 0, 0, 0, 2}, {2,
		 * 0, 0, 0, 0, 0, 2}, {2, 0, 0, 0, 0, 0, 2}, {2, 0, 0, 0, 0, 0, 2}, {2, 0, 0, 0,
		 * 0, 0, 2}, {2, 2, 2, 2, 2, 2, 2}};
		 */
		String[] pathAry = MouseVisit.findMore(maze, startX, startY, endX, endY);

		for (int i = 0; i < pathAry.length; i++) {
			String path = pathAry[i];
			System.out.println("路徑[" + i + "]==" + path);
			MouseVisit.print(maze, path);
		}

	}

	// 求多解
	private static void demo3() {
		MouseVisit MouseVisit = new MouseVisit();
		System.out.println("求多解-----");
		// 起點
		int startX = 2;
		int startY = 1;

		// 終點
		int endX = 2;
		int endY = 3;
		// 迷宮
		int[][] maze = new int[][] { { 2, 2, 2, 2, 2, 2, 2 }, { 2, 0, 0, 0, 0, 0, 2 }, { 2, 0, 2, 0, 0, 0, 2 },
				{ 2, 0, 0, 2, 0, 2, 2 }, { 2, 2, 0, 2, 0, 2, 2 }, { 2, 0, 0, 0, 0, 0, 2 }, { 2, 2, 2, 2, 2, 2, 2 } };

		/*
		 * int[][] maze = new int[][]{ {2, 2, 2, 2, 2, 2, 2}, {2, 0, 0, 0, 0, 0, 2}, {2,
		 * 0, 0, 0, 0, 0, 2}, {2, 0, 0, 0, 0, 0, 2}, {2, 0, 0, 0, 0, 0, 2}, {2, 0, 0, 0,
		 * 0, 0, 2}, {2, 2, 2, 2, 2, 2, 2}};
		 */
		String path = MouseVisit.findLink(maze, startX, startY, endX, endY);

		System.out.println("路徑==" + path);

		if (path.length() > 0) {
			MouseVisit.print(maze, path);
		} else {
			maze[startX][startY] = 1;
			maze[endX][endY] = 1;
			MouseVisit.print(maze);
			System.out.println("不能連");
		}

	}

}
