package hr.fer.zemris.java.model;

/**
 * Class serving as a data structure that models option for poll.
 * 
 * @author juren
 *
 */
public class PollOptionModel {

	/**
	 * variable that stores unique ID number
	 */
	private long id;
	/**
	 * Variable that stores title of an option
	 */
	private String optionTitle;
	/**
	 * variable that stores URL to special data
	 */
	private String optionLink;
	/**
	 * variable that stores unique ID number of poll that defines this option
	 */
	private long pollID;
	/**
	 * variable that stores total count of votes for given option
	 */
	private long voteCount;

	/**
	 * Standard getter for voteCount
	 * 
	 * @return the voteCount
	 */
	public long getVoteCount() {
		return voteCount;
	}

	/**
	 * Standard setter for voteCount
	 * 
	 * @param voteCount the voteCount to set
	 */
	public void setVoteCount(long voteCount) {
		this.voteCount = voteCount;
	}

	/**
	 * Standard setter for id
	 * 
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Standard setter for optionTitle
	 * 
	 * @param optionTitle the optionTitle to set
	 */
	public void setOptionTitle(String optionTitle) {
		this.optionTitle = optionTitle;
	}

	/**
	 * Standard setter for optionLink
	 * 
	 * @param optionLink the optionLink to set
	 */
	public void setOptionLink(String optionLink) {
		this.optionLink = optionLink;
	}

	/**
	 * Standard setter for pollID
	 * 
	 * @param pollID the pollID to set
	 */
	public void setPollID(long pollID) {
		this.pollID = pollID;
	}

	/**
	 * Standard getter for id
	 * 
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Standard getter for optionTitle
	 * 
	 * @return the optionTitle
	 */
	public String getOptionTitle() {
		return optionTitle;
	}

	/**
	 * Standard getter for optionLink
	 * 
	 * @return the optionLink
	 */
	public String getOptionLink() {
		return optionLink;
	}

	/**
	 * Standard getter for pollID
	 * 
	 * @return the pollID
	 */
	public long getPollID() {
		return pollID;
	}

	/**
	 * 
	 * Standard constructor
	 * 
	 * @param id
	 * @param optionTitle
	 * @param optionLink
	 * @param pollID
	 * @param voteCount
	 */
	public PollOptionModel(long id, String optionTitle, String optionLink, long pollID, long voteCount) {
		super();
		this.id = id;
		this.optionTitle = optionTitle;
		this.optionLink = optionLink;
		this.pollID = pollID;
		this.voteCount = voteCount;
	}

}
