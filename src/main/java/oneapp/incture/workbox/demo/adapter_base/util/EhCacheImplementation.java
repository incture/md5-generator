package oneapp.incture.workbox.demo.adapter_base.util;

import java.util.List;

//import sap.core.connectivity.api.authentication.AuthenticationHeader;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class EhCacheImplementation {

	private CacheManager cacheManager;
	private Cache cache;

	public EhCacheImplementation() {
	}

	public EhCacheImplementation(String cacheName) {
		cacheManager = CacheManager.create();
		cache = cacheManager.getCache(cacheName);
	}

	@SuppressWarnings("unchecked")
	public List<String> getKeys() {
		return cache.getKeys();
	}
	
	/* SAML Cache Impl. */
//	public void putSAMLHeaderIntoCache(String key, AuthenticationHeader appToAppSSOHeader) {
//		try {
//			cache.put(new Element(key, appToAppSSOHeader));
//		} catch (CacheException e) {
//			System.err.println(String.format("Problem occurred while putting AuthenticationHeader into cache: %s", e));
//		}
//	}
//	
//	public AuthenticationHeader retriveSAMLHeaderFromCache(String key) {
//		try {
//			Element element = cache.get(key);
//			if (element != null)
//				return (AuthenticationHeader) element.getObjectValue();
//		} catch (CacheException ce) {
//			System.err.println(
//					String.format("Problem occurred while trying to retrieve AuthenticationHeader from cache: %s", ce));
//		}
//		return null;
//	}

	public void putInCache(String key, TaskCacheInstance value) {
		System.err.println("[WBP-Dev]Insert into cache : Key : " + key + " Value : " + value);
		try {
			cache.put(new Element(key, value));
		} catch (CacheException e) {
			System.err.println(String.format("Problem occurred while putting TaskCacheInstance into cache: %s", e.getMessage()));
		}
	}

	public TaskCacheInstance retrieveFromCache(String key) {
		try {
			Element element = cache.get(key);
			if (element != null)
				return (TaskCacheInstance) element.getObjectValue();
		} catch (CacheException ce) {
			System.err.println(
					String.format("Problem occurred while trying to retrieve TaskCacheInstance from cache: %s", ce));
		}
		return null;
	}

	public void removeFromCache(String key) {
		System.err.println("[WBP-Dev]Removed from cache : " + key);
		try {
			if (key != null)
				cache.remove(key);
		} catch (CacheException ce) {
			System.err.println(
					String.format("Problem occurred while trying to retrieve TaskCacheInstance from cache: %s", ce));
		}
	}

	public void removeAllFromCache() {
		List<String> keys = this.getKeys();
		for (String key : keys) {
			try {
				if (key != null)
					cache.remove(key);
			} catch (CacheException ce) {
				System.err.println(String.format("Problem occurred while trying to retrieve TaskCacheInstance from cache: %s",
						ce));
			}
		}
	}
	
	public void putOwnersInCache(String key, OwnerCacheInstance ownerCache) {
		System.err.println("[WBP-Dev]Insert into cache : Key : " + key + " Value : " + ownerCache);
		try {
			cache.put(new Element(key, ownerCache));
		} catch (CacheException e) {
			System.err.println(String.format("Problem occurred while putting OwnerCacheInstance into cache: %s", e));
		}
	}
	
//	public OwnerCacheInstance retriveOwner

}
