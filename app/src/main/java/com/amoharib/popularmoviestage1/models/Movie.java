package com.amoharib.popularmoviestage1.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.amoharib.popularmoviestage1.utils.NetworkUtils;

public class Movie implements Parcelable {
    int id;
    String originalTitle;
    String poster;
    String overview;
    Double voteAverage;
    String releaseDate;

    public Movie(int id, String originalTitle, String poster, String overview, Double voteAverage, String releaseDate) {
        this.id = id;
        this.originalTitle = originalTitle;
        this.poster = poster;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
    }

    public Movie(int id, String originalTitle, String overview, Double voteAverage, String releaseDate) {
        this.id = id;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getPoster() {
        return NetworkUtils.IMAGE_BASE_URL + poster;
    }

    public String getBasePoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.originalTitle);
        dest.writeString(this.poster);
        dest.writeString(this.overview);
        dest.writeValue(this.voteAverage);
        dest.writeString(this.releaseDate);
    }

    protected Movie(Parcel in) {
        this.id = in.readInt();
        this.originalTitle = in.readString();
        this.poster = in.readString();
        this.overview = in.readString();
        this.voteAverage = (Double) in.readValue(Double.class.getClassLoader());
        this.releaseDate = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
