package com.doxa360.yg.android.darling.model;
import android.content.SearchRecentSuggestionsProvider;


public class RecentSuggestionProvider extends SearchRecentSuggestionsProvider {

    public final static String AUTHORITY = "com.doxa360.yg.android.savingsdemoapp.model.RecentSuggestionProvider";
    public final static int MODE = DATABASE_MODE_QUERIES | DATABASE_MODE_2LINES;

    public RecentSuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }


}