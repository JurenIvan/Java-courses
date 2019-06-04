package hr.fer.zemris.java.servlets;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

class Record {

	private String id;
	private String bandName;
	private String sampleUrl;
	private int votes;

	/**
	 * @param id
	 * @param bandName
	 * @param sampleUrl
	 * @param votes
	 */
	public Record(String splitted, String bandName, String sampleUrl, int votes) {
		super();
		this.id = splitted;
		this.bandName = bandName;
		this.sampleUrl = sampleUrl;
		this.votes = votes;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the bandName
	 */
	public String getBandName() {
		return bandName;
	}

	/**
	 * @param bandName the bandName to set
	 */
	public void setBandName(String bandName) {
		this.bandName = bandName;
	}

	/**
	 * @return the sampleUrl
	 */
	public String getSampleUrl() {
		return sampleUrl;
	}

	/**
	 * @param sampleUrl the sampleUrl to set
	 */
	public void setSampleUrl(String sampleUrl) {
		this.sampleUrl = sampleUrl;
	}

	/**
	 * @return the votes
	 */
	public int getVotes() {
		return votes;
	}

	/**
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

	public static Record makeRecord(String t) {
		String[] splitted = t.split("\\t");
		return new Record(splitted[0], splitted[1], splitted[2], 0);
	}

	protected static List<Record> readDefinition(HttpServletRequest req) throws IOException {
		String fileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt");

		if (!Files.exists(Paths.get(fileName))) {
			Files.createFile(Paths.get(fileName));
		}
		return Files.readAllLines(Paths.get(fileName)).stream().map((t) -> Record.makeRecord(t))
				.collect(Collectors.toList());
	}

	protected static void updateRecords(List<Record> records, String fileName) throws IOException {

		List<String[]> iDVotes = Files.readAllLines(Paths.get(fileName)).stream().map(t -> t.split("\\t"))
				.collect(Collectors.toList());

		for (String[] e : iDVotes) {
			int index;
			if ((index = findIndexOf(e[0], records)) != -1) {
				records.get(index).setVotes(Integer.parseInt(e[1]));
			}
		}
	}

	protected static int findIndexOf(String string, List<Record> records) {
		for (int i = 0; i < records.size(); i++) {
			if (string.equals(records.get(i).getId()))
				return i;
		}
		return -1;
	}
	
	@Override
	public String toString() {
		return id+" "+bandName+" "+sampleUrl+" "+votes;
	}

}
