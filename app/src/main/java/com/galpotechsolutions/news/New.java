package com.galpotechsolutions.news;

import android.media.Image;

public class New {

    /** Title of the New*/
    private String titlesText;
    /** Section name of the New*/
    private String sectionsText;
    /** Publication Date of the New*/
    private String publicationDatesText;
    /** Autor name of the New*/
    private String autorsText;
    /** Website URL of the New */
    private String urlsText;
    /** Body textof the New */
    private String bodiesText;
    /** Drawable resource ID */
    //private int imageResourceIds = NO_IMAGE_PROVIDED;
    private String imageResourceIds;
    /** Constant value that represents no image was provided for this word */
    private static final int NO_IMAGE_PROVIDED = -1;

    /**
     *
     * @param titleText
     * @param sectionText
     * @param publicationDateText
     * @param autorText
     * @param thumbnailImage
     * @param bodyText
     */
    public New(String titleText, String sectionText, String publicationDateText, String autorText, String thumbnailImage, String bodyText, String url) {
        titlesText = titleText;
        sectionsText = sectionText;
        publicationDatesText = publicationDateText;
        autorsText = autorText;
        bodiesText = bodyText;
        urlsText = url;
        imageResourceIds = thumbnailImage;

    }

    /**
     * Get the title of the new.
     */
    public String getTitle(){
        return titlesText;
    }

    /**
     * Get the section of the new.
     */
    public String getSection(){
        return sectionsText;
    }

    /**
     * Get the publication date of the new.
     */
    public String getPublicationDate(){
        return publicationDatesText;
    }

    /**
     * Get the writer of the new.
     */
    public String getAutor(){
        return autorsText;
    }

    /**
     * Get the body of the new.
     */
    public String getBody(){
        return bodiesText;
    }

    /**
     * Get the website URL to find more information about the new.
     */
    public String getUrl(){
        return urlsText;
    }

    /**
     * Get the image of the new
     */
    public String getImageResourceIds(){
        return imageResourceIds;
    }

    /**
     * Returns whether or not there is an image for this news story.
     */
    public boolean hasImage() {
        return !getImageResourceIds().equals("");
    }
}
