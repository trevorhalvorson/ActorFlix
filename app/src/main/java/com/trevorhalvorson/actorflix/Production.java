package com.trevorhalvorson.actorflix;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Trevor on 8/20/2015.
 */
public class Production {

    @Expose
    private Integer unit;
    @SerializedName("show_id")
    @Expose
    private Integer showId;
    @SerializedName("show_title")
    @Expose
    private String showTitle;
    @SerializedName("release_year")
    @Expose
    private String releaseYear;
    @Expose
    private String rating;
    @Expose
    private String category;
    @SerializedName("show_cast")
    @Expose
    private String showCast;
    @Expose
    private String director;
    @Expose
    private String summary;
    @Expose
    private String poster;
    @Expose
    private Integer mediatype;
    @Expose
    private String runtime;

    /**
     * @return The unit
     */
    public Integer getUnit() {
        return unit;
    }

    /**
     * @param unit The unit
     */
    public void setUnit(Integer unit) {
        this.unit = unit;
    }

    /**
     * @return The showId
     */
    public Integer getShowId() {
        return showId;
    }

    /**
     * @param showId The show_id
     */
    public void setShowId(Integer showId) {
        this.showId = showId;
    }

    /**
     * @return The showTitle
     */
    public String getShowTitle() {
        return showTitle;
    }

    /**
     * @param showTitle The show_title
     */
    public void setShowTitle(String showTitle) {
        this.showTitle = showTitle;
    }

    /**
     * @return The releaseYear
     */
    public String getReleaseYear() {
        return releaseYear;
    }

    /**
     * @param releaseYear The release_year
     */
    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }

    /**
     * @return The rating
     */
    public String getRating() {
        return rating;
    }

    /**
     * @param rating The rating
     */
    public void setRating(String rating) {
        this.rating = rating;
    }

    /**
     * @return The category
     */
    public String getCategory() {
        return category;
    }

    /**
     * @param category The category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * @return The showCast
     */
    public String getShowCast() {
        return showCast;
    }

    /**
     * @param showCast The show_cast
     */
    public void setShowCast(String showCast) {
        this.showCast = showCast;
    }

    /**
     * @return The director
     */
    public String getDirector() {
        return director;
    }

    /**
     * @param director The director
     */
    public void setDirector(String director) {
        this.director = director;
    }

    /**
     * @return The summary
     */
    public String getSummary() {
        return summary;
    }

    /**
     * @param summary The summary
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * @return The poster
     */
    public String getPoster() {
        return poster;
    }

    /**
     * @param poster The poster
     */
    public void setPoster(String poster) {
        this.poster = poster;
    }

    /**
     * @return The mediatype
     */
    public Integer getMediatype() {
        return mediatype;
    }

    /**
     * @param mediatype The mediatype
     */
    public void setMediatype(Integer mediatype) {
        this.mediatype = mediatype;
    }

    /**
     * @return The runtime
     */
    public String getRuntime() {
        return runtime;
    }

    /**
     * @param runtime The runtime
     */
    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

}
