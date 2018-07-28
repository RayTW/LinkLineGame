package link;

import java.util.ArrayList;

import javax.swing.JFrame;

/**
 * 
 * @author Ray Lee
 *
 */
public class LineGame extends JFrame implements OnEventListener {
	private static final String VERSION = "1.0.0";
	private static final int SCREEN_WIDTH = 800;
	private static final int SCREEN_HEIGHT = 600;
	private LinkCanvas mCanvas;// 畫布
	private Logic mLogic;// 遊戲邏輯

	public LineGame() {
		init();
	}

	public void init() {
		mLogic = new Logic();
		mLogic.setDelegate(this);
		mCanvas = new LinkCanvas();
		mCanvas.setDelegate(this);
		getContentPane().add(mCanvas);
		mCanvas.addRoles(mLogic.createBbRoles(SCREEN_WIDTH, SCREEN_HEIGHT));// 建背景、按鈕
		mCanvas.addRoles(mLogic.rest());// 建立牌
	}

	public void startCanvasRepaint() {
		mCanvas.start();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onEvent(Event eventname, Object obj) {
		// 加入連線動畫物件
		if (eventname == Event.MOVIE_BEGIN) {
			mCanvas.addRoles((ArrayList<Role>) obj);
			return;
		}

		// 點擊重新開始
		if (eventname == Event.START) {
			mCanvas.removeRoleKind(3);
			mCanvas.addRoles(mLogic.rest());// 建立牌
			return;
		}
		// 換牌
		if (eventname == Event.RESTART) {
			mCanvas.removeRoleKind(3);
			mCanvas.addRoles(mLogic.washPei());// 建立牌
			return;
		}
		// 點擊牌
		if (eventname == Event.CLICK_PEI) {
			mLogic.checkLink((Role) obj);
			return;
		}
	}

	public static void main(String[] args) {
		Utility.get().loadImages();

		LineGame game = new LineGame();
		game.setTitle("version " + VERSION);
		game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.setBounds(50, 50, SCREEN_WIDTH, SCREEN_HEIGHT);
		game.setVisible(true);
		game.startCanvasRepaint();
	}

}
