package link.util.astar;

import java.util.LinkedList;

public class Node implements Comparable<Node> {
	public Point mPos;

	// 開始點數值
	public int mCostFromStart;

	// 目標地點數值
	public int mCostToObject;

	// 父節點
	public Node mParentNode;

	/**
	 * 座標點初始化
	 *
	 * @param p
	 */
	public Node(Point p) {
		mPos = p;
	}

	/**
	 * 返迴路徑成植
	 *
	 * @param node
	 * @return
	 */
	public int getCost(Node node) {
		// 獲得坐標點間差值(x1, y1)-(x2, y2)
		int m = node.mPos.mX - mPos.mX;
		int n = node.mPos.mY - mPos.mY;
		// 取兩節點間歐幾理德距離(直線嘴離)做為估計值，用以獲得成本
		return (int) Math.sqrt(m * m + n * n);
	}

	/**
	 * 檢查node對像是否和驗證對像一致
	 */
	@Override
	public boolean equals(Object node) {
		// 驗證坐標為判斷依據
		if (mPos.mX == ((Node) node).mPos.mX && mPos.mY == ((Node) node).mPos.mY) {
			return true;
		}
		return false;
	}

	/**
	 * 比較兩點以獲得最小成本對象
	 */
	@Override
	public int compareTo(Node node) {
		int a1 = mCostFromStart + mCostToObject;
		int a2 = node.mCostFromStart + node.mCostToObject;
		if (a1 < a2) {
			return -1;
		} else if (a1 == a2) {
			return 0;
		} else {
			return 1;
		}
	}

	/**
	 * 獲得上下左右各點間移動限制區域
	 *
	 * @return
	 */
	public LinkedList<Node> getLimit() {
		LinkedList<Node> limit = new LinkedList<>();
		int x = mPos.mX;
		int y = mPos.mY;
		// 上下左右各點間移動區域(對於斜視地圖，可以開啟註釋的偏移部分，此時將評估8個方位)
		// 上
		limit.add(new Node(new Point(x, y - 1)));
		// 右上
		// limit.add(new Node(new Point(x+1, y-1)));
		// 右
		limit.add(new Node(new Point(x + 1, y)));
		// 右下
		// limit.add(new Node(new Point(x+1, y+1)));
		// 下
		limit.add(new Node(new Point(x, y + 1)));
		// 左下
		// limit.add(new Node(new Point(x-1, y+1)));
		// 左
		limit.add(new Node(new Point(x - 1, y)));
		// 左上
		// limit.add(new Node(new Point(x-1, y-1)));

		return limit;
	}

}