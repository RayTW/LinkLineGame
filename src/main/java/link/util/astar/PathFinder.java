package link.util.astar;

import java.util.LinkedList;
import java.util.List;

public class PathFinder {
	/**
	 * A*方式尋徑,注入開始坐標及目標坐標後運算,返回可行路徑的List
	 * 
	 * @param map
	 *            地圖
	 * @param limit
	 *            行走區域限制
	 * @param startPos
	 * @param objectPos
	 * @return
	 */
	public List<Node> searchPath(int[][] map, int[] limit, Point startPos, Point objectPos) {
		// 路徑優先等級list(此示例中為內部方法)
		LevelList<Node> levelList = new LevelList<>();

		// 已完成路徑的list
		LinkedList<Node> closedList = new LinkedList<>();

		// 初始化起始節點與目標節點
		Node startNode = new Node(startPos);
		Node objectNode = new Node(objectPos);
		// 設定起始節點參數
		startNode.mCostFromStart = 0;
		startNode.mCostToObject = startNode.getCost(objectNode);
		startNode.mParentNode = null;
		// 加入運算等級序列
		levelList.add(startNode);
		// 當運算等級序列中存在數據時，循環處理尋徑，直到levelList為空
		while (!levelList.isEmpty()) {
			// 取出並刪除最初的元素
			Node firstNode = levelList.removeFirst();
			// 判定是否和目標node坐標相等
			if (firstNode.equals(objectNode)) {
				// 是的話即可構建出整個行走路線圖，運算完畢
				return makePath(firstNode);
			} else {
				// 否則
				// 加入已驗證List
				closedList.add(firstNode);
				// 獲得firstNode的移動區域
				LinkedList<Node> nodeLimit = firstNode.getLimit();
				// 遍歷
				for (int i = 0; i < nodeLimit.size(); i++) {
					// 獲得相鄰節點
					Node neighborNode = nodeLimit.get(i);
					// 獲得是否滿足等級條件
					boolean isOpen = levelList.contains(neighborNode);
					// 獲得是否已行走
					boolean isClosed = closedList.contains(neighborNode);
					// 判斷是否無法通行
					boolean isHit = isHit(map, limit, neighborNode.mPos.mX, neighborNode.mPos.mY);
					// 當三則判定皆非時
					if (!isOpen && !isClosed && !isHit) {
						// 設定costFromStart
						neighborNode.mCostFromStart = firstNode.mCostFromStart + 1;
						// 設定costToObject
						neighborNode.mCostToObject = neighborNode.getCost(objectNode);
						// 改變neighborNode父節點
						neighborNode.mParentNode = firstNode;
						// 加入level
						levelList.add(neighborNode);
					}
				}
			}

		}
		// 清空數據
		levelList.clear();
		levelList = null;
		closedList.clear();
		closedList = null;
		// 當while無法運行時，將返回null
		return null;
	}

	/**
	 * 判定是否為可通行區域
	 *
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean isHit(int[][] map, int[] limit, int x, int y) {
		for (int i = 0; i < limit.length; i++) {
			if (map[x][y] == limit[i]) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 通過Node製造行走路徑
	 * 
	 * @param node
	 * @return
	 */
	private LinkedList<Node> makePath(Node node) {
		LinkedList<Node> path = new LinkedList<>();
		// 當上級節點存在時
		while (node.mParentNode != null) {
			// 在第一個元素處添加
			path.addFirst(node);
			// 將node賦值為parent node
			node = node.mParentNode;
		}
		// 在第一個元素處添加
		path.addFirst(node);
		return path;
	}

	private class LevelList<T extends Comparable<T>> extends LinkedList<T> {
		/**
		 * 增加一個node
		 *
		 * @param node
		 */
		@Override
		public boolean add(T node) {
			for (int i = 0; i < size(); i++) {
				if (node.compareTo(get(i)) <= 0) {
					add(i, node);
					return true;
				}
			}
			addLast(node);
			return true;
		}
	}
}
