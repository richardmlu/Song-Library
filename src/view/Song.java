//Brian Abarquez and Richard Lu
package view;

import java.util.Comparator;

public class Song implements Comparable<Song>{
	
	@Override
    public String toString() {
        return this.title.toString();
    }
	
	public String title;
	public String artist;
	public String album;
	public String year;
	
	public Song() {
		this.title = this.artist = this.album = this.year = "";
	}
	
	public Song(String title, String artist,  String album, String year) {
		this.title = title;
		this.artist = artist;
		this.album = album;
		this.year = year;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}
	
	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}	

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	@Override
	public int compareTo(Song arg0) {
		return 0;
	}	
	
	public static Comparator<Song> TitleSort = new Comparator<Song>() {

		public int compare(Song song1, Song song2) {
		   String SongTitle1 = song1.getTitle().toUpperCase();
		   String SongTitle2 = song2.getTitle().toUpperCase();
		   return SongTitle1.compareTo(SongTitle2);
	}};
	
	public static Comparator<Song> ArtistSort = new Comparator<Song>() {

		public int compare(Song song1, Song song2) {
		   String SongArtist1 = song1.getArtist().toUpperCase();
		   String SongArtist2 = song2.getArtist().toUpperCase();
		   return SongArtist1.compareTo(SongArtist2);
	}};
	
	public static Comparator<Song> AlbumSort = new Comparator<Song>() {

		public int compare(Song song1, Song song2) {
		   String SongArtist1 = song1.getAlbum().toUpperCase();
		   String SongArtist2 = song2.getAlbum().toUpperCase();
		   return SongArtist1.compareTo(SongArtist2);
	}};
	
	public static Comparator<Song> YearSort = new Comparator<Song>() {

		public int compare(Song song1, Song song2) {
		   String SongYear1 = song1.getYear().toUpperCase();
		   String SongYear2 = song2.getYear().toUpperCase();
		   return SongYear1.compareTo(SongYear2);
	}};


}