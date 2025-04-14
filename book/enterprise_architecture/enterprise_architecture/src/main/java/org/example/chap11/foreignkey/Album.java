package org.example.chap11.foreignkey;

public class Album {
	private Long id;
	private String title;
	private Artist artist;

	public Album(long id, String title, Artist artist) {
		this.id = id;
		this.title = title;
		this.artist = artist;
	}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public Artist getArtist() {
		return artist;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setArtist(Artist artist) {
		this.artist = artist;
	}
}
