package hr.fer.zemris.dao;

import java.util.List;

import hr.fer.zemris.model.BlogComment;
import hr.fer.zemris.model.BlogEntry;
import hr.fer.zemris.model.BlogUser;

public interface DAO {

	/**
	 * Dohvaća entry sa zadanim <code>id</code>-em. Ako takav entry ne postoji,
	 * vraća <code>null</code>.
	 * 
	 * @param id ključ zapisa
	 * @return entry ili <code>null</code> ako entry ne postoji
	 * @throws DAOException ako dođe do pogreške pri dohvatu podataka
	 */
	public BlogEntry getBlogEntry(Long id) throws DAOException;

	public List<BlogUser> getUsers() throws DAOException;

	public BlogUser getUser(String nick) throws DAOException;

	public List<BlogEntry> getUserContent(BlogUser creator) throws DAOException;

	
	
	
	
	
	public void commitEntry(BlogEntry entry) throws DAOException;

	public void commitBlogUser(BlogUser user) throws DAOException;

	public void commitComment(BlogComment comment) throws DAOException;

}