package org.blazer.userservice.action;

import org.roaringbitmap.buffer.MutableRoaringBitmap;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class CacheTest2 {

	public static void main(String[] args) throws InterruptedException {

		CacheManager manager = CacheManager.newInstance("src/main/resources/ehcache2.xml");
		Cache cache = manager.getCache("sampleCache2");

		// Element e = new Element("key1", "value1");
		// cache.put(e);

		Thread.sleep(1000);

		System.out.println(((MutableRoaringBitmap)cache.get("key1").getObjectValue()));
//		for (int i = 0; i < 100; i++) {
//			Thread.sleep(10000);
//			Element e = new Element("new" + i, "value" + i);
//			cache.put(e);
//			try {
//				System.out.println(cache.get("key1").getObjectValue());
//			} catch (Exception ee) {
//				ee.printStackTrace();
//			}
//		}
	}

}
