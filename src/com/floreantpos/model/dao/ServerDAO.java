package com.floreantpos.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.PosException;
import com.floreantpos.PosLog;
import com.floreantpos.model.Server;
import com.floreantpos.model.Ticket;

public class ServerDAO extends _RootDAO {
	private static ServerDAO instance = new ServerDAO();

	public static ServerDAO getInstance() {
		return instance;
	}

	public ServerDAO() {
	}

	@Override
	protected Class getReferenceClass() {
		return Server.class;
	}

	public Server get(Integer id) {
		if (id == null)
			return null;
		Session session = null;
		try {
			session = getSession();
			return (Server) session.get(Server.class, id);
		} finally {
			closeSession(session);
		}
	}

	public List<Server> findAll() {
		Session session = null;
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(Server.class);
			criteria.addOrder(Order.asc("firstName"));
			criteria.addOrder(Order.asc("lastName"));
			return criteria.list();
		} finally {
			closeSession(session);
		}
	}

	public List<Server> findAllActive() {
		Session session = null;
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(Server.class);
			criteria.add(Restrictions.or(Restrictions.isNull("active"), Restrictions.eq("active", Boolean.TRUE)));
			criteria.addOrder(Order.asc("firstName"));
			criteria.addOrder(Order.asc("lastName"));
			return criteria.list();
		} finally {
			closeSession(session);
		}
	}

	public boolean hasOrders(Server server) {
		if (server == null || server.getId() == null)
			return false;
		Session session = null;
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(Ticket.class);
			criteria.add(Restrictions.eq("assignedServer", server));
			criteria.setProjection(Projections.rowCount());
			Number count = (Number) criteria.uniqueResult();
			return count != null && count.intValue() > 0;
		} catch (Exception e) {
			PosLog.error(getClass(), e);
			return false;
		} finally {
			closeSession(session);
		}
	}

	public void saveOrUpdate(Server server) {
		Session session = null;
		Transaction tx = null;
		try {
			session = createNewSession();
			tx = session.beginTransaction();
			session.saveOrUpdate(server);
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			throw e;
		} finally {
			closeSession(session);
		}
	}

	public void delete(Server server) throws PosException {
		if (server == null || server.getId() == null)
			return;

		if (hasOrders(server)) {
			throw new PosException("Server '" + server.getDisplayName()
					+ "' has past orders in the system and cannot be deleted. Please set their status to Inactive instead.");
		}

		Session session = null;
		Transaction tx = null;
		try {
			session = createNewSession();
			tx = session.beginTransaction();
			session.delete(server);
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			throw e;
		} finally {
			closeSession(session);
		}
	}
}
