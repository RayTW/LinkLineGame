package link.base;

import java.awt.Image;

import link.Utility;
import link.OnRoleDeadListener;

/**
 * 
 * @author Ray Lee
 *
 */
public class RoleBase {
	private static volatile int sAutoID;
	private OnRoleDeadListener mListener;
	private String mName;
	private int mId; // 角色的id，用來移除角色的
	private int mX;
	private int mY;
	private int mW;
	private int mH;
	private int mIndex; // 角色的圖層
	private int mFlag; // 目前角色動畫圖播到第幾個位置
	private int mKind; // 角色類別,1:背景,2:按鈕,3:牌
	private boolean mIsFoucs;
	private Image[] mImages;

	public RoleBase() {
		++sAutoID;
		mId = sAutoID;
		RoleBaseInit();
		mName = "";
	}

	public void RoleBaseInit() {
		mFlag = 0;
	}

	public void setOnRoleDeadListener(OnRoleDeadListener listener) {
		mListener = listener;
	}

	public void setKind(int k) {
		mKind = k;
	}

	public int getKind() {
		return mKind;
	}

	public void setName(String n) {
		mName = n;
	}

	public String getName() {
		return mName;
	}

	public void setBounds(int rx, int ry, int rw, int rh) {
		mX = rx;
		mY = ry;
		mW = rw;
		mH = rh;
	}

	public void setId(int i) {
		mId = i;
	}

	public int getId() {
		return mId;
	}

	public void setLayerIndex(int i) {
		mIndex = i;
	}

	public int getLayerIndex() {
		return mIndex;
	}

	/**
	 * 設定角色移動後的位置
	 * 
	 * @param rx
	 * @param ry
	 */
	public void move(int rx, int ry) {
		mX = rx;
		mY = ry;
	}

	public void setX(int rx) {
		mX = rx;
	}

	public int getX() {
		return mX;
	}

	public void setY(int ry) {
		mY = ry;
	}

	public int getY() {
		return mY;
	}

	public void setW(int rw) {
		mW = rw;
	}

	public int getW() {
		return mW;
	}

	public void setH(int rh) {
		mH = rh;
	}

	public int getH() {
		return mH;
	}

	public void setFocus(boolean b) {
		mIsFoucs = b;
	}

	public boolean isFocus() {
		return mIsFoucs;
	}

	/**
	 * 設定角色使用的動畫連續圖
	 * 
	 * @param filename
	 */
	public void setImages(String[] filename) {
		mImages = new Image[filename.length];

		for (int i = 0; i < filename.length; i++) {
			String imageName = filename[i];
			mImages[i] = Utility.get().getImageCache().get(imageName);
		}
	}

	/**
	 * 設定角色的圖
	 * 
	 * @param imageName
	 */
	public void setImage(String imageName) {
		if (mImages == null || mImages.length < 1) {
			mImages = new Image[1];
		}
		Image img = Utility.get().getImageCache().get(imageName);
		mImages[0] = img;
	}

	/**
	 * 取得角色圖
	 * 
	 * @return
	 */
	public Image getImage() {
		if (mImages != null && mFlag < mImages.length) {
			return mImages[mFlag];
		}
		return null;
	}

	/**
	 * 判斷是否碰撞到目前物件
	 * 
	 * @param hx
	 * @param hy
	 * @param hw
	 * @param hh
	 * @return
	 */
	public boolean hitTest(int hx, int hy, int hw, int hh) {
		return ((hx + hw > mX) && (hx < mX + mW) && (hy + hh > mY) && (hy < mY + mH));
	}

	/**
	 * 將角色從畫布上移除
	 */
	public void removeFromSubView() {
		if (mListener != null) {
			mListener.onRoleDead(mId);
		}
	}

	public void close() {
		mListener = null;
		mImages = null;
	}
}
