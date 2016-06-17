package net.tinybrick.websocket.services;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.websocket.Session;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WebsocketSessionManager implements ISessionManager<Object> {
	final String cacheName = "presenceCache";
	@Value("${presence.list.mem.size}") Integer presenceCacheSize;
	@Value("${presence.list.mem.timeToLiveSeconds}") Integer presenceTimeToLiveSeconds = 0;
	@Value("${presence.list.mem.timeToIdleSeconds}") Integer presenceTimeToIdleSeconds = 0;

	Cache presenceCache;
	static CacheManager cacheManager;

	private static synchronized CacheManager getCacheManager() {
		if (null == cacheManager) {
			cacheManager = CacheManager.getInstance();
		}
		return cacheManager;
	}

	@PostConstruct
	public void init() {
		cacheManager = getCacheManager();

		presenceCache = cacheManager.getCache(cacheName);
		if (null == presenceCache) {
			presenceCache = new Cache(cacheName, presenceCacheSize, false, false, presenceTimeToLiveSeconds,
					presenceTimeToIdleSeconds);
			cacheManager.addCache(presenceCache);
		}
	}

	public Cache getPresenceCache() {
		return presenceCache;
	}

	public void setPresenceCache(Cache presenceCache) {
		this.presenceCache = presenceCache;
	}

	public List<String> getUsers() {
		return this.presenceCache.getKeys();
	}

	public Element addSession(Object name, Session session) {
		List<Session> sessions = getSession(name);
		if (null != sessions) {
			sessions.add(session);
		}
		else {
			sessions = new ArrayList<Session>();
			sessions.add(session);
		}

		Element element = new Element(name, sessions);
		presenceCache.put(element);

		return element;
	}

	public List<Session> getSession(Object name) {
		Element sessionElement = presenceCache.get(name);
		return (List<Session>) (null == sessionElement ? null : sessionElement.getObjectValue());
	}

	public boolean removeSession(Object name) {
		return presenceCache.remove(name);
	}
}
