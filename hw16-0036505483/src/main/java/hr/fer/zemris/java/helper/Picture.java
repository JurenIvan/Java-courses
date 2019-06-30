package hr.fer.zemris.java.helper;

import java.util.List;

/**
 * Class used as a structure for data. Contains variables that describe picture;
 * name, file name and list of tags
 * 
 * @author juren
 *
 */
public class Picture {

	/**
	 * String representing exact filename
	 */
	private String fileName;
	/**
	 * String representing description or title of picture
	 */
	private String pictureTitle;
	/**
	 * List of string that represent tags for this picture
	 */
	private List<String> tags;

	/**
	 * Standard constructor for {@link Picture}
	 * 
	 * @param fileName     String representing exact filename
	 * @param pictureTitle String representing description or title of picture
	 * @param tags         List of string that represent tags for this picture
	 */
	public Picture(String fileName, String pictureTitle, List<String> tags) {
		super();
		this.fileName = fileName;
		this.pictureTitle = pictureTitle;
		this.tags = tags;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the pictureTitle
	 */
	public String getPictureTitle() {
		return pictureTitle;
	}

	/**
	 * @param pictureTitle the pictureTitle to set
	 */
	public void setPictureTitle(String pictureTitle) {
		this.pictureTitle = pictureTitle;
	}

	/**
	 * @return the tags
	 */
	public List<String> getTags() {
		return tags;
	}

	/**
	 * @param tags the tags to set
	 */
	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
		return result;
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
		if (getClass() != obj.getClass())
			return false;
		Picture other = (Picture) obj;
		if (fileName == null) {
			if (other.fileName != null)
				return false;
		} else if (!fileName.equals(other.fileName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return pictureTitle + "     " + fileName + "     " + tags.toString();
	}

}
