package hr.fer.zemris.java.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Class that models datastructure used to store data in database
 * 
 * @author juren
 *
 */
@Entity
@Table(name = "blog_comments")
public class BlogComment {
	/**
	 * Key of structure
	 */
	private Long id;
	/**
	 * varialbe that stores {@link BlogEntry} that is parent for this comment
	 */
	private BlogEntry blogEntry;
	/**
	 * Email of user who created this comment
	 */
	private String usersEMail;
	/**
	 * Message stored in comment
	 */
	private String message;
	/**
	 * date when comment was created
	 */
	private Date postedOn;

	/**
	 * Standard getter
	 * @return
	 */
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	/**
	 * Standard getter
	 * @return
	 */
	@ManyToOne
	@JoinColumn(nullable = false)
	public BlogEntry getBlogEntry() {
		return blogEntry;
	}

	/**
	 * Standard getter
	 * @return
	 */
	@Column(length = 320, nullable = false)
	public String getUsersEMail() {
		return usersEMail;
	}

	/**
	 * Standard getter
	 * @return
	 */
	@Column(length = 4096, nullable = false)
	public String getMessage() {
		return message;
	}

	/**
	 * Standard getter
	 * @return
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	public Date getPostedOn() {
		return postedOn;
	}

	/**
	 * Standard setter
	 */
	public void setBlogEntry(BlogEntry blogEntry) {
		this.blogEntry = blogEntry;
	}

	/**
	 * Standard setter
	 */
	public void setUsersEMail(String usersEMail) {
		this.usersEMail = usersEMail;
	}

	/**
	 * Standard setter
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Standard setter
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Standard setter
	 */
	public void setPostedOn(Date postedOn) {
		this.postedOn = postedOn;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BlogComment other = (BlogComment) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}