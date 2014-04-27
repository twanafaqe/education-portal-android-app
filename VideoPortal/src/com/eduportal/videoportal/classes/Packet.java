package com.eduportal.videoportal.classes;

public class Packet {

	private String id;
	private String title;
	private String category;
	private String username;
	private String image;

	public Packet(String p) {
		this.id = "";
		this.title = "";
		this.category = "";
		this.username = "";
		this.image = "";
	}

	public Packet() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

}
