package link;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import link.base.GameCanvas;

/**
 * 
 * @author Ray Lee
 *
 */
public class LinkCanvas extends GameCanvas implements MouseMotionListener, MouseListener, OnRoleDeadListener {
	private ArrayList<Role> mRoles;
	private OnEventListener mListener;

	public LinkCanvas() {
		init();
	}

	private void init() {
		mRoles = new ArrayList<Role>();
		addMouseMotionListener(this);
		addMouseListener(this);
	}

	public void setDelegate(OnEventListener obj) {
		mListener = obj;
	}

	// 重畫所有物件到畫布上
	@Override
	public void onDraw(Graphics g) {
		@SuppressWarnings("unchecked")
		ArrayList<Role> tmpAry = (ArrayList<Role>) mRoles.clone();

		for (int i = tmpAry.size() - 1; i >= 0; i--) {
			Role r = tmpAry.get(i);

			Image bgImg = r.getImage();
			if (bgImg != null) {
				g.drawImage(bgImg, r.getX(), r.getY(), r.getW(), r.getH(), this);
			}
			// 畫牌
			if (r.getKind() == 3) {
				if (r.isFocus()) {
					g.setColor(Color.YELLOW);
					g.drawRect(r.getX(), r.getY(), r.getW(), r.getH());
					g.setColor(Color.BLACK);
				}
			} else if (r.getKind() == 4) {// 畫連線圖
				g.setColor(Color.ORANGE);
				g.fillRect(r.getX(), r.getY(), r.getW(), r.getH());
				g.setColor(Color.BLACK);
			}
		}
	}

	// ---------MouseMotionListener----begin-------------
	// 按住滑鼠拖曳時，觸發的事件
	@Override
	public void mouseDragged(MouseEvent mouseDragEvt) {
	}

	// 移動滑鼠時，觸發的事件
	@Override
	public void mouseMoved(MouseEvent mouseMoveEvt) {
	}
	// ---------MouseMotionListener----end-------------

	// ---------MouseListener----begin-------------
	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {// 左鍵
			for (int i = 0; i < mRoles.size(); i++) {
				Role r = mRoles.get(i);
				if (r.hitTest(e.getX(), e.getY(), 1, 1)) {
					// 點到開始按鈕
					if ("start".equals(r.getName())) {
						// System.out.println("點到開始按鈕");
						mListener.onEvent(Event.START, null);
						return;
					}
					// 點擊到牌
					if (r.getName().matches("[0-9]{1,2}")) {
						mListener.onEvent(Event.CLICK_PEI, r);
						return;
					}

					return;
				}
			}
			return;
		}

		if (e.getButton() == MouseEvent.BUTTON3) {// 右鍵
			mListener.onEvent(Event.RESTART, null);

			return;
		}
	}
	// ---------MouseListener----end-------------

	/**
	 * 新增角色到場景上
	 * 
	 * @param r
	 */
	public void addRole(Role r) {
		r.setOnRoleDeadListener(this);
		mRoles.add(r);
		sortLayerIndex(mRoles);
	}

	/**
	 * 新增多個角色到場景上
	 * 
	 * @param ary
	 */
	public void addRoles(ArrayList<Role> ary) {
		for (int i = 0; i < ary.size(); i++) {
			Role r = ary.get(i);
			addRole(r);
		}
	}

	/**
	 * 從場景上把角色移除
	 * 
	 * @param id
	 * @return
	 */
	public void removeRole(int id) {
		for (int i = mRoles.size() - 1; i >= 0; i--) {
			Role r = mRoles.get(i);
			if (r.getId() == id) {
				mRoles.remove(i);
				r.close();
				break;
			}
		}
	}

	/**
	 * 從場景上把角色移除
	 * 
	 * @param id
	 * @return
	 */
	public void removeRoleKind(int k) {
		for (int i = mRoles.size() - 1; i >= 0; i--) {
			Role r = mRoles.get(i);
			if (r.getKind() == k) {
				mRoles.remove(i);
				r.close();
			}
		}
	}

	/**
	 * 從場景上把角色移除
	 * 
	 * @param id
	 * @return
	 */
	public void removeAllRole() {
		for (int i = mRoles.size() - 1; i >= 0; i--) {
			Role r = mRoles.remove(i);
			r.close();
		}
	}

	/**
	 * 以Role的層級進行排序，圖層高的在前，圖層低在後。
	 * 
	 * @param ary
	 */
	public void sortLayerIndex(ArrayList<Role> ary) {
		for (int i = 0; i < ary.size() - 1; i++) {
			Role r = ary.get(i);
			for (int j = i + 1; j < ary.size(); j++) {
				Role rr = ary.get(j);

				if (r.getLayerIndex() < rr.getLayerIndex()) {
					ary.set(i, rr);
					ary.set(j, r);
				}
			}
		}
	}

	@Override
	public void onRoleDead(int id) {
		removeRole(id);
	}

}
