package hr.fer.zemris.java.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;

import hr.fer.zemris.java.dao.DAO;
import hr.fer.zemris.java.dao.DAOException;
import hr.fer.zemris.java.model.BlogComment;
import hr.fer.zemris.java.model.BlogEntry;
import hr.fer.zemris.java.model.BlogUser;

/**
 * Class that resembles implementation of {@link DAO} interface
 * 
 * @author juren
 *
 */
public class JPADAOImpl implements DAO {

	@Override
	public BlogEntry getBlogEntry(Long id) throws DAOException {
		return JPAEMProvider.getEntityManager().find(BlogEntry.class, id);
	}

	@Override
	public List<BlogUser> getUsers() throws DAOException {
		return JPAEMProvider.getEntityManager().createNamedQuery("getAllUsers", BlogUser.class).getResultList();
	}

	@Override
	public BlogUser getUser(String nick) throws DAOException {
		List<BlogUser> blogUsers = JPAEMProvider.getEntityManager()
				.createQuery("select b from BlogUser as b where b.nick=:nick", BlogUser.class)
				.setParameter("nick", nick).getResultList();

		if (blogUsers != null && blogUsers.size() > 0) {
			return blogUsers.get(0);
		}
		return null;
	}

	@Override
	public List<BlogEntry> getUserContent(BlogUser creator) throws DAOException {
		return JPAEMProvider.getEntityManager()
				.createQuery("select b from BlogEntry as b where b.creator=:c", BlogEntry.class)
				.setParameter("c", creator).getResultList();
	}

	@Override
	public void commitEntry(BlogEntry entry) throws DAOException {
		EntityManager em = JPAEMProvider.getEntityManager();

		if (entry.getId() != null) {
			em.merge(entry);
		} else {
			em.persist(entry);
		}
	}

	@Override
	public void commitBlogUser(BlogUser user) throws DAOException {
		EntityManager em = JPAEMProvider.getEntityManager();

		if (user.getId() != null) {
			em.merge(user);
		} else {
			em.persist(user);
		}
	}

	@Override
	public void commitComment(BlogComment comment) throws DAOException {
		EntityManager em = JPAEMProvider.getEntityManager();

		if (comment.getId() != null) {
			em.merge(comment);
		} else {
			em.persist(comment);
		}
	}

}