package link;

import java.util.ArrayList;

/**
 * 動畫物件
 * 
 * @author Ray
 *
 */
public class LinkMovieObj extends Thread {
	private OnEventListener mListener;
	private Role mSource;
	private Role mTarget;
	private ArrayList<Role> mRoles;
	private float mPlaySecond;

	public LinkMovieObj(Role s, Role t) {
		mSource = s;
		mTarget = t;
	}

	public void setDelegate(OnEventListener obj) {
		mListener = obj;
	}

	public void setData(String path, int startX, int startY, int w, int h) {
		mRoles = new ArrayList<Role>();
		String[] list = path.split("[,]");

		for (int i = 0; i < list.length; i++) {
			String[] xy = list[i].split("[:]");
			int x = Integer.parseInt(xy[0]) - 2;
			int y = Integer.parseInt(xy[1]) - 2;

			Role r = new Role();
			r.setBounds(w * x + startX, h * y + startY, w, h);
			r.setName("linkMovie");
			r.setLayerIndex(999);
			r.setKind(4);
			mRoles.add(r);
		}
	}

	public void play(float sec) {
		mPlaySecond = sec;
		start();
	}

	@Override
	public void run() {
		mListener.onEvent(Event.MOVIE_BEGIN, mRoles);

		try {
			Thread.sleep((int) (1000 * mPlaySecond));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		mListener.onEvent(Event.MOVIE_END, null);
		close();
	}

	public void close() {
		mListener = null;
		if (mSource != null) {
			mSource.removeFromSubView();
		}
		if (mTarget != null) {
			mTarget.removeFromSubView();
		}
		mSource = null;
		mTarget = null;

		for (int i = mRoles.size() - 1; i >= 0; i--) {
			Role r = mRoles.remove(i);
			r.removeFromSubView();
		}
		mRoles = null;
	}

}
