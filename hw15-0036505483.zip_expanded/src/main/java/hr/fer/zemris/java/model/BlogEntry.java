package hr.fer.zemris.java.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
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
@Table(name = "blog_entries")
public class BlogEntry {

	/**
	 * Key of structure
	 */
	private Long id;
	/**
	 * List of comments left under this Entry
	 */
	private List<BlogComment> comments = new ArrayList<>();
	/**
	 * date when comment was created
	 */
	private Date createdAt;
	/**
	 * date when comment was last ModifiedAt
	 */
	private Date lastModifiedAt;
	/**
	 * title of post
	 */
	private String title;
	/**
	 * Email of user who created this comment
	 */
	private String usersEMail;
	/**
	 * Message stored in post
	 */
	private String text;

	/**
	 * varialbe that entity that created this post
	 */
	private BlogUser creator;

	/**
	 * Standard getter
	 * 
	 * @return
	 */
	@ManyToOne
	@JoinColumn(nullable = true)
	public BlogUser getCreator() {
		return creator;
	}

	/**
	 * Standard getter
	 * 
	 * @return
	 */
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	/**
	 * standard setter
	 * 
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Standard getter
	 * 
	 * @return
	 */
	@OneToMany(mappedBy = "blogEntry", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
	@OrderBy("postedOn")
	public List<BlogComment> getComments() {
		return comments;
	}

	/**
	 * standard setter
	 * 
	 * @param id
	 */
	public void setComments(List<BlogComment> comments) {
		this.comments = comments;
	}

	/**
	 * standard getter
	 * 
	 * @param id
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	public Date getCreatedAt() {
		return createdAt;
	}

	/**
	 * standard setter
	 * 
	 * @param id
	 */
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * standard getter
	 * 
	 * @param id
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	public Date getLastModifiedAt() {
		return lastModifiedAt;
	}

	/**
	 * standard setter
	 * 
	 * @param id
	 */
	public void setLastModifiedAt(Date lastModifiedAt) {
		this.lastModifiedAt = lastModifiedAt;
	}

	/**
	 * standard getter
	 * 
	 * @param id
	 */
	@Column(length = 200, nullable = false)
	public String getTitle() {
		return title;
	}

	/**
	 * standard setter
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * standard getter
	 */
	@Column(length = 4096, nullable = false)
	public String getText() {
		return text;
	}

	/**
	 * standard setter
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setCreator(BlogUser owner) {
		this.creator = owner;
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
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		BlogEntry other = (BlogEntry) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	/**
	 * @return the usersEMail
	 */
	public String getUsersEMail() {
		return usersEMail;
	}

	/**
	 * @param usersEMail the usersEMail to set
	 */
	public void setUsersEMail(String usersEMail) {
		this.usersEMail = usersEMail;
	}
	

}