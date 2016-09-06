package org.blazer.user.action;

import org.roaringbitmap.buffer.MutableRoaringBitmap;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class CacheTest {

	public static void main(String[] args) throws InterruptedException {
		CacheManager manager = CacheManager.newInstance("src/main/resources/ehcache.xml");
		Cache cache = manager.getCache("sampleCache2");

		MutableRoaringBitmap rr1 = MutableRoaringBitmap.bitmapOf(1,2,3,4,11,15);
		Element e = new Element("key1", rr1);
		cache.put(e);


		System.out.println(cache.get("key1").getObjectValue());
		Thread.sleep(5000);
		System.out.println(cache.get("key1").getObjectValue());
//		for (int i = 0; i < 100; i++) {
//			Thread.sleep(10000);
//			try {
//				System.out.println(cache.get("new" + i).getObjectValue());
//			} catch (Exception ee) {
//				ee.printStackTrace();
//			}
//		}
	}

}
