package hr.fer.zemris.java.dao;

import java.util.List;

import hr.fer.zemris.java.model.PollModel;
import hr.fer.zemris.java.model.PollOptionModel;

/**
 * Interface towards database. Models methods used to communication with
 * database.
 * 
 * @author juren
 * 
 * @version 1.1
 *
 */
public interface DAO {
	/**
	 * Method that returns sorted list of polls from underlying database.
	 * 
	 * @return {@link List} of {@link PollModel} stored in database.
	 * @throws DAOException if any error with communication occurs.
	 */
	public List<PollModel> getPools() throws DAOException;

	/**
	 * Method that returns list of {@link PollOptionModel} that represent results of
	 * poll from underlying database.
	 * 
	 * @return {@link List} of {@link PollOptionModel} stored in database
	 *         representing data.
	 * @throws DAOException if any error with communication occurs.
	 */
	public List<PollOptionModel> getResultsData(long pollID) throws DAOException;

	/**
	 * Method that returns one {@link PollModel} from underlying database based on
	 * pollID
	 * 
	 * @param pollID poll identifier
	 * @return {@link PollModel} representing poll descriptor
	 * @throws DAOException if any error with communication occurs.
	 */
	public PollModel getDescription(long pollID) throws DAOException;

	/**
	 * method that increases vote count by one to {@link PollOptionModel} that has
	 * provided id in poll that has the same id as provided
	 * 
	 * @param ID     id of {@link PollOptionModel}
	 * @param pollID id of {@link PollModel}
	 * @throws DAOException if any error with communication occurs.
	 */
	void voteFor(long pollID, long ID);
}