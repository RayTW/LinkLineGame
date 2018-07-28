package link.util;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;

/**
 * 2012-09-05 暫存圖檔，將圖片預先載入存放，並可用此取用<br>
 * 
 * 
 * @author Ray
 *
 */
public class ImageCache {
	private ConcurrentHashMap<String, Image> mChacheMap;

	public ImageCache() {
		mChacheMap = new ConcurrentHashMap<>();
	}

	/**
	 * 讀取多個圖片並預載後，將圖檔檔名當做圖檔chache的key(含副檔名)
	 * 
	 * @param pathAryu
	 *            多張圖片的檔案路徑
	 */
	public void putImages(String[] pathAryu) {
		for (int i = 0; i < pathAryu.length; i++) {
			String fielPath = pathAryu[i];
			int beginIndex = fielPath.lastIndexOf("[\\/]");

			if (beginIndex == -1) {
				beginIndex = 0;
			}
			String fileName = fielPath.substring(beginIndex, fielPath.length());
			put(fileName, fielPath);
		}
	}

	/**
	 * 指定圖檔路徑預載
	 * 
	 * @param inameName
	 * @param filePath
	 */
	public void put(String imgName, String filePath) {
		File f = new File(filePath);
		try {
			Image imgBuf = ImageIO.read(f);
			mChacheMap.put(imgName, imgBuf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 指定圖檔路徑預載
	 * 
	 * @param imgName
	 * @param url
	 */
	public void put(String imgName, URL url) {
		try {
			Image imgBuf = ImageIO.read(url);
			mChacheMap.put(imgName, imgBuf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 放入image物件
	 * 
	 * @param imageName(含副檔名)
	 * @param imgObj
	 */
	public void put(String imageName, Image imgObj) {
		mChacheMap.put(imageName, imgObj);
	}

	/**
	 * 取得指定chache的圖檔
	 * 
	 * @param imageName
	 * @return
	 */
	public Image get(String imageName) {
		return mChacheMap.get(imageName);
	}

	/**
	 * 移除指定chache的圖檔
	 * 
	 * @param imageName
	 * @return
	 */
	public Image rmove(String imageName) {
		return mChacheMap.remove(imageName);
	}

	/**
	 * 取得目前chache的圖檔個數
	 * 
	 * @return
	 */
	public int size() {
		return mChacheMap.size();
	}

	/**
	 * 取得cache容器
	 * 
	 * @return
	 */
	public Map<String, Image> getChachMap() {
		return mChacheMap;
	}

	/**
	 * 清除所有chache的圖檔
	 */
	public void clear() {
		mChacheMap.clear();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

}
