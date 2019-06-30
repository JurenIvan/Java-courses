package hr.fer.zemris.java.servlets;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.dao.DAOProvider;
import hr.fer.zemris.java.forms.BlogCommentForm;
import hr.fer.zemris.java.forms.BlogEntryForm;
import hr.fer.zemris.java.model.BlogComment;
import hr.fer.zemris.java.model.BlogEntry;
import hr.fer.zemris.java.model.BlogUser;

/**
 * Servlet used by webapp to navigate around the blog. Has Multiple methods that
 * are products of refactoring of code. Servlet is mapped to
 * "/servleti/author/*".
 * 
 * @author juren
 *
 */
@WebServlet("/servleti/author/*")
public class AuthorsServlets extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		String path = req.getPathInfo().substring(1);
		String[] splitted = path.split("/");

		BlogUser bu = DAOProvider.getDAO().getUser(splitted[0]);
		if (bu == null)
			errorHandling(req, resp, "No such user registered!");

		req.setAttribute("creator", bu);
		if (splitted.length == 1) {
			showContext(req, resp);
			return;
		}

		if (splitted.length == 2 && splitted[1].equals("new")) {
			newBlogGet(req, resp);
			return;
		}

		if (splitted.length == 2 && splitted[1].equals("edit")) {
			editPage(req, resp, splitted[0], bu);
			return;
		}

		if (splitted.length == 2) {
			newCommentGet(req, resp, splitted[0], splitted[1]);
			return;
		}

		errorHandling(req, resp, "Wrong url pattern!");
	}

	/**
	 * Method used to edit {@link BlogEntry}
	 */
	private void editPage(HttpServletRequest req, HttpServletResponse resp, String nameOfUser, BlogUser bu)
			throws ServletException, IOException {

		if (req.getSession().getAttribute("current.user.nick") != null
				&& req.getSession().getAttribute("current.user.nick").equals(nameOfUser)) {

			String param = req.getParameter("id");
			try {
				if (DAOProvider.getDAO().getBlogEntry(Long.parseLong(param)).getCreator().getNick()
						.equals(nameOfUser)) {

					BlogEntryForm bef = new BlogEntryForm();
					BlogEntry be = DAOProvider.getDAO().getBlogEntry(Long.parseLong(param));
					bef.fillFromRecord(be);

					req.setAttribute("record", bef);
					req.getRequestDispatcher("/WEB-INF/pages/BlogEntryForm.jsp").forward(req, resp);

				} else {
					errorHandling(req, resp, "FORBIDEN!");
				}
			} catch (Exception e) {
				errorHandling(req, resp, "FORBIDEN!");
			}
		}
	}

	/**
	 * Method used to create new comments
	 */
	private void newCommentGet(HttpServletRequest req, HttpServletResponse resp, String nickame, String id)
			throws ServletException, IOException {

		BlogEntry be;
		try {
			be = DAOProvider.getDAO().getBlogEntry(Long.parseLong(id));
		} catch (Exception e) {
			errorHandling(req, resp, "upsie.. FORBIDEN!");
			return;
		}

		req.setAttribute("post", be);
		req.setAttribute("comments", be.getComments());
		req.setCharacterEncoding("UTF-8");
		req.setAttribute("id", id);
		req.setAttribute("user", be.getCreator().getNick());
		req.getRequestDispatcher("/WEB-INF/pages/EditCommentPost.jsp").forward(req, resp);

	}

	/**
	 * Method used to create new {@link BlogEntry}es
	 */
	private void newBlogGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		BlogUser tempUser = (BlogUser) req.getAttribute("creator");
		BlogUser logUser = (BlogUser) DAOProvider.getDAO()
				.getUser((String) req.getSession().getAttribute("current.user.nick"));
		if (logUser == null)
			errorHandling(req, resp, "Not allowed");

		if (tempUser.getNick() != logUser.getNick())
			errorHandling(req, resp, "Not allowed");

		req.getRequestDispatcher("/WEB-INF/pages/EditBlogPost.jsp").forward(req, resp);

	}

	/**
	 * Method used to show all users in database an to link to their works
	 * 
	 */
	private void showContext(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		BlogUser bu = (BlogUser) req.getAttribute("creator");
		List<BlogEntry> content = DAOProvider.getDAO().getUserContent(bu);
		req.setAttribute("posts", content);
		req.setAttribute("nick", bu.getNick());
		req.getRequestDispatcher("/WEB-INF/pages/ContentList.jsp").forward(req, resp);
	}

	// ---------------------------------------------------------

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		String path = req.getPathInfo().substring(1);
		String[] splitted = path.split("/");
		BlogUser bu = DAOProvider.getDAO().getUser(splitted[0]);

		if (bu == null)
			errorHandling(req, resp, "No such user registered!");

		req.setAttribute("creator", bu);

		if (splitted.length == 2 && splitted[1].equals("new")) {
			newBlogPost(req, resp, splitted[0]);
			return;
		}

		if (splitted.length == 2 && splitted[1].equals("edit")) {
			newBlogPostNew(req, resp, splitted[0]);
			return;
		}

		if (splitted.length == 2) {
			newCommentPost(req, resp, splitted[0], splitted[1]);
			return;
		}

		errorHandling(req, resp, "Wrong url pattern!");
	}

	/**
	 * Method used to add a new {@link BlogEntry} and to repeat if any mistake comes
	 */
	private void newBlogPostNew(HttpServletRequest req, HttpServletResponse resp, String name)
			throws ServletException, IOException {
		BlogEntryForm bef = new BlogEntryForm();
		bef.fillFromHttpsRequest(req);
		bef.validate();

		if (bef.hasErrors()) {
			req.setAttribute("form", bef);
			req.getRequestDispatcher("/WEB-INF/pages/EditBlogPost.jsp").forward(req, resp);
			return;
		}

		if (req.getSession().getAttribute("current.user.nick") == null) {
			errorHandling(req, resp, "Not allowed");
			return;
		}
		String loggedInUser = (String) req.getSession().getAttribute("current.user.nick");
		if (!loggedInUser.equals(name)) {
			errorHandling(req, resp, "Not allowed");
			return;
		}
		String param2 = req.getParameter("id");
		BlogEntry be;
		try {
		 be = DAOProvider.getDAO().getBlogEntry(Long.parseLong(param2));
		}catch (Exception e) {
			errorHandling(req, resp, "Forbiden!");
			return;
		}
		bef.fillToRecord(be);
		be.setLastModifiedAt(new Date());

		DAOProvider.getDAO().commitEntry(be);
		resp.sendRedirect(req.getServletContext().getContextPath() + "/servleti/author/" + name);
	}

	/**
	 * Method used to add a new {@link BlogComment} and to repeat if any mistake
	 * comes in input phase
	 */
	private void newCommentPost(HttpServletRequest req, HttpServletResponse resp, String nickame, String id)
			throws ServletException, IOException {

		BlogCommentForm bef = new BlogCommentForm();
		bef.fillFromHttpsRequest(req);
		bef.validate();

		if (bef.hasErrors()) {
			req.setAttribute("record", bef);
			req.getRequestDispatcher("/WEB-INF/pages/EditCommentPost.jsp").forward(req, resp);
			return;
		}

		BlogComment be = new BlogComment();
		bef.fillToRecord(be);

		if (be.getUsersEMail() == null || be.getUsersEMail().isBlank()) {
			be.setUsersEMail((String) req.getSession().getAttribute("current.user.email"));
			if (be.getUsersEMail() == null) {
				be.setUsersEMail("ANONYMOUS");
			}
		}

		try {
			be.setBlogEntry(DAOProvider.getDAO().getBlogEntry(Long.parseLong(id)));
		} catch (Exception e) {
			errorHandling(req, resp, "Forbiden!");
			return;
		}
		be.setPostedOn(new Date());

		DAOProvider.getDAO().commitComment(be);
		resp.sendRedirect(req.getServletContext().getContextPath() + "/servleti/author/" + nickame);
	}

	/**
	 * Method that prepares data for new {@link BlogEntry} and repeats process if
	 * somethinh goes wrong
	 */
	private void newBlogPost(HttpServletRequest req, HttpServletResponse resp, String name)
			throws ServletException, IOException {

		BlogEntryForm bef = new BlogEntryForm();
		bef.fillFromHttpsRequest(req);
		bef.validate();

		if (bef.hasErrors()) {
			req.setAttribute("form", bef);
			req.getRequestDispatcher("/WEB-INF/pages/EditBlogPost.jsp").forward(req, resp);
			return;
		}

		if (req.getSession().getAttribute("current.user.nick") == null) {
			errorHandling(req, resp, "Not allowed");
			return;
		}
		String loggedInUser = (String) req.getSession().getAttribute("current.user.nick");
		if (!loggedInUser.equals(name)) {
			errorHandling(req, resp, "Not allowed");
			return;
		}

		BlogEntry be = new BlogEntry();
		bef.fillToRecord(be);
		be.setCreatedAt(new Date());
		be.setLastModifiedAt(new Date());
		be.setCreator(DAOProvider.getDAO().getUser(name));

		DAOProvider.getDAO().commitEntry(be);
		resp.sendRedirect(req.getServletContext().getContextPath() + "/servleti/author/" + name);
	}

	/**
	 * Method that is used to redirect user to error page
	 * 
	 */
	private void errorHandling(HttpServletRequest req, HttpServletResponse resp, String message)
			throws ServletException, IOException {
		req.setAttribute("error", message);
		req.getRequestDispatcher("/WEB-INF/pages/Error.jsp").forward(req, resp);
	}

}
