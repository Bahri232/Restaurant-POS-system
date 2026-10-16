package com.floreantpos.model;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "server")
public class Server implements Comparable<Server>, Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String firstName;
	private String lastName;
	private String nickname;
	private String phone;
	private Boolean active = Boolean.TRUE;

	public Server() {
	}

	public Server(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Boolean getActive() {
		return active;
	}

	public Boolean isActive() {
		return active == null || active.booleanValue();
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getFullName() {
		String fn = firstName == null ? "" : firstName.trim();
		String ln = lastName == null ? "" : lastName.trim();
		String full = (fn + " " + ln).trim();
		return full.isEmpty() ? (nickname != null ? nickname : "Server #" + id) : full;
	}

	public String getDisplayName() {
		String full = getFullName();
		if (nickname != null && !nickname.trim().isEmpty()) {
			return full + " (" + nickname.trim() + ")";
		}
		return full;
	}

	@Override
	public String toString() {
		return getDisplayName();
	}

	@Override
	public int hashCode() {
		return id == null ? 0 : id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Server other = (Server) obj;
		if (id == null) {
			return other.id == null && super.equals(obj);
		}
		return id.equals(other.id);
	}

	@Override
	public int compareTo(Server o) {
		if (o == null)
			return 1;
		String thisName = getDisplayName();
		String otherName = o.getDisplayName();
		return thisName.compareToIgnoreCase(otherName);
	}
}
