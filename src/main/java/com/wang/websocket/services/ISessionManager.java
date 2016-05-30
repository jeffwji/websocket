package com.wang.websocket.services;

import java.util.List;

import javax.websocket.Session;

import net.sf.ehcache.Element;

public interface ISessionManager<T> {
	public Element addSession(T name, Session session);

	public List<Session> getSession(T name);

	public boolean removeSession(T name);

	public List<String> getUsers();
}
