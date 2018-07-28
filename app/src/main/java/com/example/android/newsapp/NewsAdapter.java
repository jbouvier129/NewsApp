package com.example.android.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {

    /**
     * Creates a new NewsAdapter
     */
    public NewsAdapter(Context context, List<News> newsItems) {
        super(context, 0, newsItems);
    }

    /**
     * Returns a list item view of the news item at the given position
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list_item, parent, false);
        }

        News currentNewsItem = getItem(position);

        /*
         * Finds the article view then sets it equal to a shorter formatted version
         * of the JSON response value
         */
        TextView articleView = listItemView.findViewById(R.id.article_title);
        String formattedArticle = formatTitle(currentNewsItem.getArticleTitle());
        formattedArticle = formattedArticle + "...";
        articleView.setText(formattedArticle);

        /*
         * finds the section view and sets it equal to the JSON response value
         */
        TextView sectionView = listItemView.findViewById(R.id.article_section);
        sectionView.setText(currentNewsItem.getArticleSection());

        /*
         * Finds the author view and sets it equal to the JSON response value
         */
        TextView authorView = listItemView.findViewById(R.id.article_author);
        authorView.setText(currentNewsItem.getArticleAuthor());

        /*
         * Logic to set a default value in the event the JSON response value
         * is empty
         */
        String defaultAuthor = getContext().getString(R.string.author_default);
        String test = authorView.getText().toString();
        if (test.isEmpty()) {
            authorView.setText(defaultAuthor);
        }

        /*
         *Finds the date view then sets it equal to a shortened version of the
         * JSON response value
         */
        String formattedDate = formatDate(currentNewsItem.getArticleDate());
        TextView dateView = listItemView.findViewById(R.id.article_date);
        dateView.setText(formattedDate);

        return listItemView;
    }

    /*
     * Formats the article title so that it stays within the view boundaries
     */
    private String formatTitle(String articleTitle) {
        if (articleTitle.length() > 45) {
            String titleFormat = articleTitle.substring(0, 45);
            return formatTitle(titleFormat);
        } else {
            return articleTitle;
        }
    }

    /*
     * Shortens the date so that it stays within the view boundaries
     */
    private String formatDate(String articleDate) {
        articleDate = articleDate.substring(0, 10);
        return articleDate;
    }


}