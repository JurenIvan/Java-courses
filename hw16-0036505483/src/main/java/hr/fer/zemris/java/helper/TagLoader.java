package hr.fer.zemris.java.helper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class that does maintenance of data. Used as JDBC. Modeled as singleton. Has
 * method getTagLoader that sets up required data and returns all that is asked
 * for.
 * 
 * @author juren
 *
 */
public class TagLoader {

	/**
	 * Static variable that stores only instance of {@link TagLoader}
	 */
	private static TagLoader singleton;

	/**
	 * Collection storing all pictures that have been loaded from folder and whose
	 * description has been found.
	 */
	private Set<Picture> pictures;
	/**
	 * Map that stores tags as keys and list of pictures that has that tag. Not best
	 * solution memory wise but fastest for getting all pictures that have specific
	 * tag.
	 */
	private HashMap<String, List<Picture>> tagMap;

	/**
	 * private constructor for singleton design pattern used to create single
	 * instance of {@link TagLoader} and set up initial dataBase.
	 * 
	 * @param descriptorPath path to descriptor file
	 * @throws IOException if no such descriptor exist
	 */
	private TagLoader(Path descriptorPath) throws IOException {
		pictures = new HashSet<>();
		tagMap = new HashMap<>();
		loadPictures(descriptorPath);
		organizePicturesByTags();
		return;
	}

	/**
	 * Method that returns instance of {@link TagLoader} or instances new one at
	 * first. (first time use with apsolute path, after that null is acceptable)
	 * 
	 * @param descriptorPath used when calling method for first time.
	 * @return instance of {@link TagLoader} (always the same instance)
	 * @throws IOException
	 */
	public static TagLoader getTagLoader(Path descriptorPath) throws IOException {
		if (singleton != null)
			return singleton;
		return singleton = new TagLoader(descriptorPath);
	}

	/**
	 * Method that returns method with specific filename or null if no such exists
	 * 
	 * @param filename file name of picture required
	 * @return Picture
	 */
	public Picture getPic(String filename) {
		for (var elem : pictures) {
			if (elem.getFileName().equals(filename))
				return elem;
		}
		return null;
	}

	/**
	 * Method that returns list of all pictures with given path or null if no such
	 * exist
	 * 
	 * @param tag String represeting requested tag
	 * @return list of all pictures with given path or null if no such exist
	 */
	public List<Picture> getPicturesWithTag(String tag) {
		return tagMap.get(tag);
	}

	/**
	 * Method that returns all tags from descriptor
	 * 
	 * @return Set of tags
	 */
	public Set<String> getAllTags() {
		return tagMap.keySet();
	}

	/**
	 * private method used to organize pictures into proper structures.
	 */
	private void organizePicturesByTags() {
		for (var pic : pictures) {
			List<String> tags = pic.getTags();
			for (var tag : tags) {
				List<Picture> underThatTag = tagMap.get(tag);
				if (underThatTag == null) {
					underThatTag = new ArrayList<>();
					tagMap.put(tag, underThatTag);
					underThatTag.add(pic);
				} else {
					underThatTag.add(pic);
				}
			}
		}
	}

	/**
	 * Method that loads all pictures from provided path to set of pictures.
	 * 
	 * @param descriptorPath absolute path to file descriptor
	 * @throws IOException if no such descriptor file exists
	 */
	private void loadPictures(Path descriptorPath) throws IOException {
		List<String> lines = Files.readAllLines(descriptorPath);
		if (lines.size() % 3 != 0) {
			throw new IllegalArgumentException("Descriptor file is not properly formated.");
		}
		for (int i = 0; i < lines.size(); i = i + 3) {
			String fileName = lines.get(i);
			String pictureTitle = lines.get(i + 1);
			String[] tags = lines.get(i + 2).split(",");
			ArrayList<String> tagsParsed = new ArrayList<String>(tags.length);
			for (int j = 0; j < tags.length; j++) {
				tagsParsed.add(tags[j].trim().toLowerCase());
			}
			pictures.add(new Picture(fileName, pictureTitle, tagsParsed));
		}
	}

}
