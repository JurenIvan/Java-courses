package hr.fer.zemris.java.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.json.JSONArray;

import hr.fer.zemris.java.helper.Picture;
import hr.fer.zemris.java.helper.TagLoader;


/**
 * Razred koristi biblioteku org.json za rad s JSON formatom (da vidite kako se
 * s time radi; mogli smo koristiti i gson).
 * 
 * @author juren
 */
@Path("/gallery")
public class PictureJSON {

	@GET
	@Produces("application/json")		
	public Response getTagList() throws IOException {
		Set<String> tagSet = TagLoader.getTagLoader(null).getAllTags();
		JSONArray json=new JSONArray(new ArrayList<>(tagSet));
		return Response.status(Status.OK).entity(json.toString()).build();	
	}

	
	@Path("{tagName}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)	
	public Response getPictures(@PathParam("tagName") String tagName) throws IOException {
		List<Picture> listOfPictures=TagLoader.getTagLoader(null).getPicturesWithTag(tagName);
		
		if (listOfPictures.size()<1) {
			return Response.status(Status.NOT_FOUND).build();
		}

		JSONArray rArray = new JSONArray(listOfPictures);

		return Response.status(Status.OK).entity(rArray.toString()).build();
	}

	@Path("/getPicture/{filename}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPicture(@PathParam("filename") String filename) throws IOException {
		Picture picture=TagLoader.getTagLoader(null).getPic(filename);
		
		if (picture==null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		List<Picture> list=new ArrayList<>();
		list.add(picture);
		
		JSONArray rArray = new JSONArray(list);

		return Response.status(Status.OK).entity(rArray.toString()).build();
	}

}
