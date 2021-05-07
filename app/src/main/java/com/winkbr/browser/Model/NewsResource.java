package com.winkbr.browser.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by pBrowser on 05-03-2018.
 */

public class NewsResource {
    @SerializedName("articles")
    @Expose
    private ArrayList<News> articles;

    public ArrayList<News> getArticles() {
        return articles;
    }

    public void setArticles(ArrayList<News> news) {
        this.articles = news;
    }
}
