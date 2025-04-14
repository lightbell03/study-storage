package org.example.chap11.foreignkey;

public class Artist {
	private Long id;
	private String name;

	public Artist(long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
