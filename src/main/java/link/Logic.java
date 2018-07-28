package link;

import java.util.ArrayList;

import link.util.MouseVisit;

/**
 * 遊戲邏輯
 * 
 * @author Ray
 *
 */
public class Logic implements OnEventListener {
	public int ROW = 12;
	public int COL = 7;
	private int[][] mPeiArray;
	private MouseVisit mMazeVisit = new MouseVisit();
	private Role mFocuskRole = null;
	private OnEventListener mListener;

	private int mStartX = 75;
	private int mStartY = 75;
	private int mWidth = 50;
	private int mHeight = 65;

	public Logic() {
		LogicInit();
	}

	public void LogicInit() {

	}

	public void setDelegate(OnEventListener obj) {
		mListener = obj;
	}

	/**
	 * 建立角色，並載入角色使用的圖片
	 */
	public ArrayList<Role> createBbRoles(int screenWidth, int screenHeight) {
		ArrayList<Role> bgRoles = new ArrayList<Role>();
		// 加入背景
		Role bg = new Role();
		bg.setBounds(0, 0, screenWidth, screenHeight);
		bg.setImage("background.jpg");
		bg.setLayerIndex(1);
		bg.setKind(1);
		bgRoles.add(bg);

		// 開始鈕
		Role StartBtn = new Role();
		StartBtn.setBounds(0, 0, 50, 50);
		StartBtn.setImage("restart.png");
		StartBtn.setLayerIndex(10);
		StartBtn.setName("start");
		StartBtn.setKind(2);
		bgRoles.add(StartBtn);

		return bgRoles;
	}

	/**
	 * 檢查是否兩張牌可連線，並將牌移除
	 * 
	 * @param clickRole
	 * @return
	 */
	public boolean checkLink(Role clickRole) {
		if (mFocuskRole != null) {
			if (mFocuskRole.mMazeX != clickRole.mMazeX || mFocuskRole.mMazeY != clickRole.mMazeY) {
				if (mFocuskRole.getName().equals(clickRole.getName())) {
					int[][] ary = arrayAddWall(mPeiArray);
					ary[clickRole.mMazeX][clickRole.mMazeY] = 0;
					ary[mFocuskRole.mMazeX][mFocuskRole.mMazeY] = 0;
					String path = mMazeVisit.findLink(ary, clickRole.mMazeX, clickRole.mMazeY, mFocuskRole.mMazeX,
							mFocuskRole.mMazeY);

					if (path.length() > 0) {// 兩張牌有相連的路徑，將兩張牌移除
						mPeiArray[clickRole.mMazeX - 2][clickRole.mMazeY - 2] = 0;
						mPeiArray[mFocuskRole.mMazeX - 2][mFocuskRole.mMazeY - 2] = 0;
						clickRole.setFocus(true);// 被點到的牌發亮
						LinkMovieObj movieObj = new LinkMovieObj(mFocuskRole, clickRole);
						movieObj.setDelegate(this);
						movieObj.setData(path, mStartX - 1, mStartY, mWidth, mHeight);
						movieObj.play(0.3f);
						mFocuskRole = null;

						// 所有牌已清除，重新開局
						if (isClean(mPeiArray)) {
							mListener.onEvent(Event.START, null);
							return true;
						}
						// "尋路---------begin---"
						String nextLivePath = checkLive(mPeiArray);
						// "尋路---------end---"
						System.out.println("下個連線路徑=" + nextLivePath);
						if (nextLivePath.length() == 0) {// 無解，把牌打亂
							mListener.onEvent(Event.RESTART, null);
						}
						return true;
					}
				}
			}
			// 移除被點到的牌
			mFocuskRole.setFocus(false);
			mFocuskRole = null;
		} else {

			mFocuskRole = clickRole;
			mFocuskRole.setFocus(true);// 被點到的牌發亮

		}
		return false;
	}

	@Override
	public void onEvent(Event eventname, Object obj) {
		// 連線動畫開始播
		if (eventname == Event.MOVIE_BEGIN) {
			mListener.onEvent(eventname, obj);
			return;
		}
		// 連線動畫播完
		if (eventname == Event.MOVIE_END) {
			return;
		}

	}

	/**
	 * 產生亂數陣列1~42
	 * 
	 * @return
	 */
	public ArrayList<Role> rest() {
		ArrayList<Role> aryList = null;
		mFocuskRole = null;

		int[] ary = new int[ROW * COL];

		for (int i = 1; i <= ary.length; i++) {
			ary[i - 1] = (i % (ary.length / 2)) + 1;
		}
		aryList = randAllPei(ary);
		return aryList;
	}

	/**
	 * 將剩餘的牌打亂
	 * 
	 * @return
	 */
	public ArrayList<Role> washPei() {
		ArrayList<Role> aryList = randAllPei(getAry());
		mFocuskRole = null;

		return aryList;
	}

	/**
	 * 檢查是否所有的牌都清除
	 * 
	 * @param ary
	 * @return
	 */
	public boolean isClean(int[][] ary) {
		for (int i = 0; i < ary.length; i++) {
			for (int j = 0; j < ary[i].length; j++) {
				if (ary[i][j] > 0) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 將牌打亂，並且最少需有一路徑可解
	 * 
	 * @param ary
	 *            剩餘的牌
	 * @return
	 */
	public ArrayList<Role> randAllPei(int[] ary) {
		ArrayList<Role> roles = new ArrayList<Role>();
		do {
			mPeiArray = new int[ROW][COL];
			roles.clear();
			for (int i = 0; i < ary.length; i++) {
				int rand = (int) (Math.random() * 42);
				int tmp = ary[rand];
				ary[rand] = ary[i];
				ary[i] = tmp;
			}

			for (int j = 0; j < ary.length; j++) {
				int x = j % ROW;
				int y = j % COL;

				if (ary[j] > 0) {
					Role r = new Role();
					r.setBounds(mWidth * x + mStartX, mHeight * y + mStartY, mWidth, mHeight);
					r.setImage("poker_" + ary[j] + ".png");
					r.setName(String.valueOf(ary[j]));
					r.setLayerIndex(j + 2);
					r.setKind(3);
					r.mMazeX = x + 2;
					r.mMazeY = y + 2;
					mPeiArray[x][y] = ary[j];
					roles.add(r);
				}
			}

		} while (checkLive(mPeiArray).length() == 0);// 找不到可連線的路徑，重新洗牌
		return roles;
	}

	/**
	 * 將剩餘的牌2維陣列轉成1維陣列
	 * 
	 * @return
	 */
	public int[] getAry() {
		int[] ary = new int[ROW * COL];

		int index = 0;
		for (int i = 0; i < mPeiArray.length; i++) {
			for (int j = 0; j < mPeiArray[i].length; j++) {
				ary[index] = mPeiArray[i][j];
				index++;
				// System.out.println((index) + "===" + peiAry[i][j]);
			}
		}
		return ary;
	}

	/**
	 * 將已被消除的牌指定空
	 * 
	 * @param x
	 * @param y
	 */
	public void removeRole(int x, int y) {
		mPeiArray[x][y] = 0;
	}

	/**
	 * 取得牌迷宮
	 * 
	 * @return
	 */
	public int[][] getMaze() {
		return mPeiArray;
	}

	/**
	 * 將迷宮加一層外牆,否則尋路時會穿牆
	 * 
	 * @param maze
	 * @return
	 */
	public int[][] arrayAddWall(int[][] maze) {
		int[][] copyMaze = new int[maze.length + 4][maze[0].length + 4];

		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[i].length; j++) {
				if (maze[i][j] > 0) {
					copyMaze[i + 2][j + 2] = 2;
				}
			}
		}

		for (int i = 0; i < copyMaze.length; i++) {
			if (i == 0 || i == copyMaze.length - 1) {
				for (int j = 0; j < copyMaze[i].length; j++) {
					copyMaze[i][j] = 2;
				}
			} else {
				copyMaze[i][0] = 2;
				copyMaze[i][copyMaze[i].length - 1] = 2;
			}
		}

		return copyMaze;
	}

	/**
	 * 檢查目前的牌型是如還有解,true:有解,false:無解
	 * 
	 * @param pAry
	 */
	public String checkLive(int[][] pAry) {
		int[][] wallAry = arrayAddWall(pAry);
		// mMazeVisit.print(wallAry);
		for (int i = 0; i < pAry.length; i++) {
			for (int j = 0; j < pAry[i].length; j++) {
				if (pAry[i][j] != 0) {
					for (int k = 0; k < pAry.length; k++) {
						for (int l = 0; l < pAry[k].length; l++) {

							if (i != k || j != l) {
								if (pAry[i][j] == pAry[k][l]) {
									int beginX = i + 2;
									int beginY = j + 2;
									int endX = k + 2;
									int endY = l + 2;
									wallAry[beginX][beginY] = 0;
									wallAry[endX][endY] = 0;
									String path = mMazeVisit.findLink(wallAry, beginX, beginY, endX, endY);
									if (path.length() > 0) {
										System.out.println(
												"[" + beginX + "][" + beginY + "][" + endX + "][" + endY + "]");
										System.out.println(path);
										return path;
										// mazeVisit.print(wallAry);
									}
									wallAry[beginX][beginY] = 1;
									wallAry[endX][endY] = 1;
								}
							}
						}
					}
				}
			}
		}
		return "";
	}
}
