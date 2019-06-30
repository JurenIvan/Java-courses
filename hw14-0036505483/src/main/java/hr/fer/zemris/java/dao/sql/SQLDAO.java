package hr.fer.zemris.java.dao.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.dao.DAO;
import hr.fer.zemris.java.dao.DAOException;
import hr.fer.zemris.java.model.PollModel;
import hr.fer.zemris.java.model.PollOptionModel;

/**
 * Ovo je implementacija podsustava DAO uporabom tehnologije SQL. Ova konkretna
 * implementacija očekuje da joj veza stoji na raspolaganju preko
 * {@link SQLConnectionProvider} razreda, što znači da bi netko prije no što
 * izvođenje dođe do ove točke to trebao tamo postaviti. U web-aplikacijama
 * tipično rješenje je konfigurirati jedan filter koji će presresti pozive
 * servleta i prije toga ovdje ubaciti jednu vezu iz connection-poola, a po
 * zavrsetku obrade je maknuti.
 * 
 * @author marcupic
 * 
 * @version 1.1
 */
public class SQLDAO implements DAO {

	@Override
	public List<PollModel> getPools() throws DAOException {
		List<PollModel> unosi = new ArrayList<>();
		Connection con = SQLConnectionProvider.getConnection();
		try (PreparedStatement pst = con.prepareStatement("select id, title, message from Polls order by id");
				ResultSet rs = pst.executeQuery();) {
			while (rs != null && rs.next()) {
				unosi.add(new PollModel(rs.getLong(1), rs.getString(2), rs.getString(3)));
			}
		} catch (Exception ex) {
			throw new DAOException("Error while fetching data.", ex);
		}
		return unosi;
	}

	@Override
	public List<PollOptionModel> getResultsData(long pollID) throws DAOException {
		List<PollOptionModel> unosi = new ArrayList<>();
		Connection con = SQLConnectionProvider.getConnection();
		try (PreparedStatement pst = con.prepareStatement(
				"select id, optionTitle,optionLink,pollID,votesCount from polloptions where pollID=?")) {
			pst.setLong(1, pollID);
			try (ResultSet rs = pst.executeQuery();) {
				while (rs != null && rs.next()) {
					unosi.add(new PollOptionModel(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getLong(4),
							rs.getLong(5)));
				}
			}
		} catch (Exception ex) {
			throw new DAOException("Error while fetching data.", ex);
		}

		return unosi;
	}

	@Override
	public void voteFor(long pollID, long id) {
		Connection con = SQLConnectionProvider.getConnection();
		try (PreparedStatement pst = con
				.prepareStatement("update PollOptions set votesCount=votesCount+1 where id=? and pollID=? ");) {
			pst.setLong(1, id);
			pst.setLong(2, pollID);
			pst.executeUpdate();
		} catch (Exception ex) {
			throw new DAOException("Error while fetching data.", ex);
		}
	}

	@Override
	public PollModel getDescription(long pollID) throws DAOException {
		Connection con = SQLConnectionProvider.getConnection();
		PollModel returning;
		try (PreparedStatement pst = con.prepareStatement("select id, title,message from Polls where id=?");) {
			System.out.println("Test");
			pst.setLong(1, pollID);
			try (ResultSet rs = pst.executeQuery()) {
				rs.next();
				returning= new PollModel(rs.getLong(1), rs.getString(2), rs.getString(3));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new DAOException("Error while fetching data.", ex);
		}
		return returning;
	}
}