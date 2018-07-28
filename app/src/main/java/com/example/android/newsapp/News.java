package com.example.android.newsapp;

public class News {

    /**
     * News Article Title
     */
    private String mArticleTitle;

    /**
     * Section of the News Article
     */
    private String mArticleSection;

    /**
     * Author of the News Article
     */
    private String mArticleAuthor;

    /**
     * Date of the Article
     */
    private String mArticleDate;

    /**
     * Website URL of the News Article
     */
    private String mUrl;

    /**
     * Constructs a new News Object
     */

    public News(String articleTitle, String articleSection, String articleAuthor, String articleDate, String url) {

        mArticleTitle = articleTitle;
        mArticleSection = articleSection;
        mArticleAuthor = articleAuthor;
        mArticleDate = articleDate;
        mUrl = url;
    }

    /**
     * Gets Article Title
     */

    public String getArticleTitle() {
        return mArticleTitle;
    }

    /**
     * Gets Article Section
     */

    public String getArticleSection() {
        return mArticleSection;
    }

    /**
     * Gets Article Author
     */

    public String getArticleAuthor() {
        return mArticleAuthor;
    }

    /**
     * Gets Article Date
     */

    public String getArticleDate() {
        return mArticleDate;
    }

    /**
     * Gets Url to News Article
     */

    public String getUrl() {
        return mUrl;
    }


}
