//Ashleigh Chung, Jason Cheng
package main.controller;

public class Song implements Comparable<Song>{
    private String name = "";
    private String artist = "";
    private String album = "";
    private int year = -1;
    public Song(){ }
    public Song(String name, String artist){
        this.name = name;
        this.artist = artist;
    }
    @Override
    public int compareTo(Song other){
        String this_name_lower = this.getName().toLowerCase();
        String this_artist_lower = this.getArtist().toLowerCase();
        String other_name_lower= other.getName().toLowerCase();
        String other_artist_lower = other.getArtist().toLowerCase();

        int compare_name = this_name_lower.compareTo(other_name_lower);
        int compare_artist = this_artist_lower.compareTo(other_artist_lower);

        if(compare_name < 0){
            return -1;
        }else if(compare_name > 0){
            return 1;
        }else{
            if (compare_artist < 0){
                return -1;
            }else if (compare_artist > 0){
                return 1;
            }else{
                return 0;
            }
        }
    }
    @Override
    public String toString(){
        String album = "";
        if (this.getAlbum().length() > 0){
            album = this.getAlbum();
        }

        String year = "";
        if(this.getYear() >= 0){
            year = Integer.toString(this.getYear());
        }

        return "Name: " + this.getName()
                + "\nArtist: " + this.getArtist()
                + "\nYear: " + year
                + "\nAlbum: " + album;
    }

    //Used to save into textfile for data persistence
    public String toString2(){
        String album = "";
        if (this.getAlbum().length() > 0){
            album = this.getAlbum();
        }

        String year = "";
        if(this.getYear() >= 0){
            year = Integer.toString(this.getYear());
        }

        return this.getName()
                + "\n" + this.getArtist()
                + "\n" + year
                + "\n" + album + "\n";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
