package hr.fer.zemris.java.dao;

import java.util.List;

import hr.fer.zemris.java.model.BlogComment;
import hr.fer.zemris.java.model.BlogEntry;
import hr.fer.zemris.java.model.BlogUser;

/**
 * Ugovor koji prezentira popis metoda kojima mozemo razgovarati s bazom
 * podataka u pozadini
 * 
 * @author juren
 *
 */
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

	/**
	 * Dohvaća sve BlogUser entriye
	 * 
	 * @return
	 * @throws DAOException
	 */
	public List<BlogUser> getUsers() throws DAOException;

	/**
	 * Dohvaća entry sa zadanim <code>id</code>-em.Ako takav entry ne postoji, vraća
	 * <code>null</code>.
	 * 
	 * @param nick
	 * @return
	 * @throws DAOException
	 */
	public BlogUser getUser(String nick) throws DAOException;

	/**
	 * Dohvaća entry sa zadanim <code>id</code>-em.Ako takav entry ne postoji, vraća
	 * <code>null</code>.
	 * 
	 * @param creator
	 * @return
	 * @throws DAOException
	 */
	public List<BlogEntry> getUserContent(BlogUser creator) throws DAOException;

	/**
	 * Sprema dovedeni {@link BlogEntry} u bazu.
	 * 
	 * @param entry
	 * @throws DAOException
	 */
	public void commitEntry(BlogEntry entry) throws DAOException;

	/**
	 * Sprema dovedeni {@link BlogUser} u bazu
	 * 
	 * @param user
	 * @throws DAOException
	 */
	public void commitBlogUser(BlogUser user) throws DAOException;

	/**
	 * Sprema dovedeni {@link BlogComment} u bazu
	 * 
	 * @param comment
	 * @throws DAOException
	 */
	public void commitComment(BlogComment comment) throws DAOException;

}