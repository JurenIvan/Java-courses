package hr.fer.zemris.java.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

/**
 * Class that models data structure used to store data in database
 * 
 * @author juren
 *
 */
@Entity
@Table(name = "blogUsers")
@NamedQuery(name = "getAllUsers", query = "SELECT b FROM BlogUser b")
public class BlogUser {
	/**
	 * Key of structure
	 */
	private Long id;
	/**
	 * variable that stores first name
	 */
	private String firstName;
	/**
	 * variable that stores last name
	 */
	private String lastName;
	/**
	 * variable that stores nickname
	 */
	private String nick;
	/**
	 * Email of user who created this comment
	 */
	private String email;
	/**
	 * variable that sha1 version of password
	 */
	private String passwordHash;
	/**
	 * Set of {@link BlogEntry}s that user created
	 */
	private Set<BlogEntry> blogEntries;

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
	 * Standard getter
	 * 
	 * @return
	 */
	@Column(length = 256, nullable = false)
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Standard getter
	 * 
	 * @return
	 */
	@Column(length = 256, nullable = false)
	public String getLastName() {
		return lastName;
	}

	/**
	 * Standard getter
	 * 
	 * @return
	 */
	@Column(length = 64, nullable = false, unique = true)
	public String getNick() {
		return nick;
	}

	/**
	 * Standard getter
	 * 
	 * @return
	 */
	@Column(length = 320, nullable = false)
	public String getEmail() {
		return email;
	}

	/**
	 * Standard getter
	 * 
	 * @return
	 */
	@Column(length = 40, nullable = false)
	public String getPasswordHash() {
		return passwordHash;
	}

	/**
	 * Standard getter
	 * 
	 * @return
	 */
	@OneToMany(mappedBy = "creator", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
	@OrderBy("postedOn")
	public Set<BlogEntry> getBlogEntries() {
		return blogEntries;
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
		BlogUser other = (BlogUser) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @param nick the nick to set
	 */
	public void setNick(String nick) {
		this.nick = nick;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @param passwordHash the passwordHash to set
	 */
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	/**
	 * @param blogEntries the blogEntries to set
	 */
	public void setBlogEntries(Set<BlogEntry> blogEntries) {
		this.blogEntries = blogEntries;
	}

}
