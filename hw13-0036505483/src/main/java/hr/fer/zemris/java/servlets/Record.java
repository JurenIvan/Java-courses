package hr.fer.zemris.java.servlets;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

/**
 * Class that models Record that people can vote for. Each {@link Record} has
 * its identifier (id), band name, String representing url to one of their songs
 * and number of votes it got in pool.
 * 
 * Class also has bundle of methods used for creation or loading records from
 * files.
 * 
 * @author juren
 *
 */
public class Record {

	/**
	 * Unique identifier of {@link Record}
	 */
	private String id;
	/**
	 * String storing name of band
	 */
	private String bandName;
	/**
	 * String representing url to one of their songs
	 */
	private String sampleUrl;
	/**
	 * Number of votes that {@link Record} got so far.s
	 */
	private int votes;

	/**
	 * Standard constructor.
	 * 
	 * @param id
	 * @param bandName
	 * @param sampleUrl
	 * @param votes
	 * @throws NullPointerException     if id,bandName or url provided are null
	 *                                  references
	 * @throws IllegalArgumentException if number of votes is less than zero
	 */
	public Record(String splitted, String bandName, String sampleUrl, int votes) {
		super();
		this.id = Objects.requireNonNull(splitted, "Can not make band with no id");
		this.bandName = Objects.requireNonNull(bandName, "Can not make band with no name");
		this.sampleUrl = Objects.requireNonNull(sampleUrl, "Cannot make band with no represetative songs");
		if (votes < 0) {
			throw new IllegalArgumentException("Negative number of votes is not allowed");
		}
		this.votes = votes;
	}

	/**
	 * Standard getter.
	 * 
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Standard getter.
	 * 
	 * @return the bandName
	 */
	public String getBandName() {
		return bandName;
	}

	/**
	 * Standard getter.
	 * 
	 * @return the sampleUrl
	 */
	public String getSampleUrl() {
		return sampleUrl;
	}

	/**
	 * Standard getter.
	 * 
	 * @return the votes
	 */
	public int getVotes() {
		return votes;
	}

	/**
	 * Standard setter
	 * 
	 * @param votes the votes to set
	 */
	public void setVotes(int votes) {
		this.votes = votes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Record))
			return false;
		Record other = (Record) obj;
		return Objects.equals(id, other.id);
	}

	/**
	 * Method that creates new Record by splitting provided string by tags. Sets
	 * votes to 0
	 * 
	 * @param t input string such as /t is tag (12/tNAME/tSAMPLE/t)
	 * @return new record
	 */
	public static Record makeRecord(String t) {
		String[] splitted = t.split("\\t");
		return new Record(splitted[0], splitted[1], splitted[2], 0);
	}

	/**
	 * Method that searches for Record that has id equal to string provided, returns
	 * it's index
	 * 
	 * @param string  id that is searched for
	 * @param records list of records
	 * @return index of record
	 */
	protected static int findIndexOf(String string, List<Record> records) {
		for (int i = 0; i < records.size(); i++) {
			if (string.equals(records.get(i).getId()))
				return i;
		}
		return -1;
	}

	@Override
	public String toString() {
		return id + " " + bandName + " " + sampleUrl + " " + votes;
	}

	/**
	 * Method that takes {@link HttpServletRequest} , extracts real path to
	 * resources, reads files that are responsible for defining and holding results
	 * of poll and creates list of records that has all data a {@link Record} can
	 * hold
	 * 
	 * @param req used to get real path to resources
	 * @return list of records
	 * @throws IOException if files can not be found
	 */
	protected static List<Record> loader(HttpServletRequest req) throws IOException {
		String fileNameDefinition = req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt");
		String fileNameResults = req.getServletContext().getRealPath("\\WEB-INF\\glasanje-rezultati.txt");
		if (!Files.exists(Paths.get(fileNameDefinition))) {
			Files.createFile(Paths.get(fileNameDefinition));
		}
		if (!Files.exists(Paths.get(fileNameResults))) {
			Files.createFile(Paths.get(fileNameResults));
		}
		List<Record> definition = Files.readAllLines(Paths.get(fileNameDefinition)).stream()
				.map((t) -> Record.makeRecord(t)).collect(Collectors.toList());

		List<String[]> iDVotes = Files.readAllLines(Paths.get(fileNameResults)).stream().map(t -> t.split("\\t"))
				.collect(Collectors.toList());

		for (String[] e : iDVotes) {
			int index;
			if ((index = findIndexOf(e[0], definition)) != -1) {
				definition.get(index).setVotes(Integer.parseInt(e[1]));
			}
		}
		return definition;
	}

}
