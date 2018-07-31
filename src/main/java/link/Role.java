package link;

import link.base.RoleBase;

public class Role extends RoleBase {
	private int mMazeX;
	private int mMazeY;
	private int mKind; // 角色類別,1:背景,2:按鈕,3:牌
	private boolean mIsFoucs;

	public Role() {

	}

	public void setKind(int k) {
		mKind = k;
	}

	public int getKind() {
		return mKind;
	}

	public void setFocus(boolean b) {
		mIsFoucs = b;
	}

	public boolean isFocus() {
		return mIsFoucs;
	}

	public int getMazeX() {
		return mMazeX;
	}

	public void setMazeX(int mMazeX) {
		this.mMazeX = mMazeX;
	}

	public int getMazeY() {
		return mMazeY;
	}

	public void setMazeY(int mMazeY) {
		this.mMazeY = mMazeY;
	}
}
