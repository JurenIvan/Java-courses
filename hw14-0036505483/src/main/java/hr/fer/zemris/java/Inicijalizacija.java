package hr.fer.zemris.java;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

import hr.fer.zemris.java.model.PollModel;
import hr.fer.zemris.java.model.PollOptionModel;

/**
 * Class that is a implementation of {@link ServletContextListener}. Its
 * function is to initialize databases tables to default values defined in
 * project.
 * 
 * @author juren
 *
 */
@WebListener
public class Inicijalizacija implements ServletContextListener {

	/** String containing sql request for inserting into polloptions table */
	private static final String POLLOPTIONS_INSERT_SQL = "INSERT INTO PollOptions (optionTitle,optionLink,pollID,votesCount) values(?,?,?,?)";
	/** String containing sql request for inserting into polls table */
	private static final String POLLS_INSERTION_SQL = "INSERT INTO Polls (title,message) values(?,?)";
	/** constant used for storing sql command that creates table "pooloptions" */
	private static final String CREATE_TABLE_POOL_OPTIONS = "CREATE TABLE PollOptions (id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, optionTitle VARCHAR(100) NOT NULL, optionLink VARCHAR(150) NOT NULL, pollID BIGINT, votesCount BIGINT, FOREIGN KEY (pollID) REFERENCES Polls(id))";
	/** constant used for storing sql command that creates table "Polls" */
	private static final String CREATE_TABLE_POOLS = "CREATE TABLE Polls (id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, title VARCHAR(150) NOT NULL, message CLOB(2048) NOT NULL)";
	/**
	 * variable that stores default path to txt file used for initialization of
	 * table
	 */
	private String DEFAULT_POOLOPTIONS_DEFINITION_PATH;
	/**
	 * variable that stores default path to txt file used for initialization of
	 * table
	 */
	private String DEFAULT_POOLS_DEFINITION_PATH;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		DEFAULT_POOLOPTIONS_DEFINITION_PATH = sce.getServletContext().getRealPath("/WEB-INF/pollOptionsDefinition.txt");
		DEFAULT_POOLS_DEFINITION_PATH = sce.getServletContext().getRealPath("/WEB-INF/polls.txt");

		String connectionURL;
		ComboPooledDataSource cpds = new ComboPooledDataSource();
		try {
			cpds.setDriverClass("org.apache.derby.jdbc.ClientDriver");
			connectionURL = buildConnectionURL(sce);
			cpds.setJdbcUrl(connectionURL);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (PropertyVetoException e1) {
			throw new RuntimeException("Error while pool initialization.", e1);
		}
		initializeTables(cpds);
		sce.getServletContext().setAttribute("hr.fer.zemris.dbpool", cpds);
		
	}

	/**
	 * Method used to check and initialize tables of database if no tables are found
	 * or found tables are empty
	 * 
	 * @param cpds {@link ComboPooledDataSource} used for data transfer
	 */
	private void initializeTables(ComboPooledDataSource cpds) {
		try (Connection con = cpds.getConnection()) {
			if (!tableExists(con, "Polls")) {
				con.prepareStatement(CREATE_TABLE_POOLS).execute();
			}

			if (!tableExists(con, "PollOptions")) {
				con.prepareStatement(CREATE_TABLE_POOL_OPTIONS).execute();
			}

			if (isTableEmpty(con, "Polls") || isTableEmpty(con, "PollOptions")) {
				fillDefaultValues(con);
			}

		} catch (SQLException e) {
		}
	}

	/**
	 * Method that loads data from files, and initializes tables with that data.
	 * 
	 * @param con used for communication with database
	 * @throws SQLException if database can not be initialized
	 */
	private void fillDefaultValues(Connection con) throws SQLException {
		List<PollModel> polls;
		List<PollOptionModel> pollOptions;
		try {
			polls = loadPolls(DEFAULT_POOLS_DEFINITION_PATH);
			pollOptions = loadPollOptionModels(DEFAULT_POOLOPTIONS_DEFINITION_PATH);
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
			throw new SQLException("Cannot load default values.");
		}

		PreparedStatement psPoll = con.prepareStatement(POLLS_INSERTION_SQL, Statement.RETURN_GENERATED_KEYS);
		PreparedStatement psPollOption = con.prepareStatement(POLLOPTIONS_INSERT_SQL);

		for (var poll : polls) {
			psPoll.setString(1, poll.getTitle());
			psPoll.setString(2, poll.getMessage());
			psPoll.executeUpdate();
			ResultSet rs = psPoll.getGeneratedKeys();
			rs.next();
			long generatedKey = rs.getLong(1);

			for (var pollOption : pollOptions.stream().filter(t -> t.getPollID() == poll.getId())
					.collect(Collectors.toList())) {
				psPollOption.setString(1, pollOption.getOptionTitle());
				psPollOption.setString(2, pollOption.getOptionLink());
				psPollOption.setLong(3, generatedKey);
				psPollOption.setLong(4, pollOption.getVoteCount());
				psPollOption.executeUpdate();
			}
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ComboPooledDataSource cpds = (ComboPooledDataSource) sce.getServletContext()
				.getAttribute("hr.fer.zemris.dbpool");
		if (cpds != null) {
			try {
				DataSources.destroy(cpds);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Method that parses dbsettings.property file and creates a string needed for
	 * conencting to database defined in dbsettings.property file.
	 * 
	 * @param sce used to get path do data
	 * @return string needed for connecting to database defined in
	 *         dbsettings.property file
	 */
	private String buildConnectionURL(ServletContextEvent sce) {
		String results = sce.getServletContext().getRealPath("/WEB-INF/dbsettings.properties");
		Properties dataBaseProperties = new Properties();
		try {
			dataBaseProperties.load(Files.newInputStream(Paths.get(results)));
		} catch (IOException e) {
			throw new IllegalArgumentException("dbsettings.properties file not found.");
		}
		StringBuilder sb = new StringBuilder(256);
		sb.append("jdbc:derby://");
		sb.append(getDataAndCheck(dataBaseProperties, "host"));
		sb.append(":");
		sb.append(getDataAndCheck(dataBaseProperties, "port"));
		sb.append("/");
		sb.append(getDataAndCheck(dataBaseProperties, "name"));
		sb.append(";user=");
		sb.append(getDataAndCheck(dataBaseProperties, "user"));
		sb.append(";password=");
		sb.append(getDataAndCheck(dataBaseProperties, "password"));
		return sb.toString();
	}

	/**
	 * Method that parses line from property in such a way that it is compared to
	 * other parameter and decided upon that if the line in dbsettings.property file
	 * is valid or no.
	 * 
	 * @param dataBaseProperties used for data retrieval
	 * @param atribute           name that is a key to mappings in provided
	 *                           Properties
	 * @return value of mapped object
	 * @throws IllegalArgumentException if no such string is found in
	 *                                  pdbsettings.property.
	 */
	private static String getDataAndCheck(Properties dataBaseProperties, String atribute) {
		if (dataBaseProperties.getProperty(atribute) == null)
			throw new IllegalArgumentException(atribute + " is missing in dbsettings.properties  .");
		return dataBaseProperties.getProperty(atribute);
	}

	/**
	 * Method that checks whether table with name is equal to one provided as
	 * parameter is existent
	 * 
	 * @param con  used for data communication to base
	 * @param name of table
	 * @return flag describing statement above
	 */
	private static boolean tableExists(Connection con, String name) {
		try {
			DatabaseMetaData meta = con.getMetaData();
			return meta.getTables(null, null, name.toUpperCase(), null).next();
		} catch (SQLException e) {
		//	e.printStackTrace();
			return false;
		}
	}

	/**
	 * Method that checks whether table with name is equal to one provided as
	 * parameter is not empty
	 * 
	 * @param con  used for data comunication to base
	 * @param name of table
	 * @return flag describing statement above
	 */
	private static boolean isTableEmpty(Connection con, String tableName) {
		try {
			return !con.prepareStatement("SELECT * FROM " + tableName).executeQuery().next();
		} catch (SQLException e) {
			return false;
		}
	}

	/**
	 * Method that reads all lines from file found on given path and parses data
	 * into {@link List} of {@link PollOptionModel}. method expects that columns are
	 * divided by tabs and each row represents a new entry
	 * 
	 * @param pathToFile path where file with data is looked for
	 * @return list of {@link PollOptionModel}
	 * @throws NumberFormatException if numbers from data can not be read
	 * @throws IOException           if file can not be read
	 */
	public static List<PollOptionModel> loadPollOptionModels(String pathToFile)
			throws NumberFormatException, IOException {
		Path path = Paths.get(pathToFile);
		List<String> lines = Files.readAllLines(path);

		List<PollOptionModel> results = new ArrayList<>(lines.size() + 1);
		for (var line : Files.readAllLines(path)) {
			String[] splitted = line.split("\\t");
			results.add(new PollOptionModel(Long.parseLong(splitted[0]), splitted[1], splitted[2],
					Long.parseLong(splitted[3]), 0));
		}

		return results;
	}

	/**
	 * Method that reads all lines from file found on given path and parses data
	 * into {@link List} of {@link PollModel}. Method expects that columns are
	 * divided by tabs and each row represents a new entry
	 * 
	 * @param pathToFile path where file with data is looked for
	 * @return list of {@link PollModel}
	 * @throws NumberFormatException if numbers from data can not be read
	 * @throws IOException           if file can not be read
	 */
	public static List<PollModel> loadPolls(String pathToFile) throws NumberFormatException, IOException {

		Path path = Paths.get(pathToFile);
		List<String> lines = Files.readAllLines(path);

		List<PollModel> results = new ArrayList<>(lines.size() + 1);
		for (var line : Files.readAllLines(path)) {
			String[] splitted = line.split("\\t");
			results.add(new PollModel(Long.parseLong(splitted[0]), splitted[1], splitted[2]));
		}
		return results;
	}

}