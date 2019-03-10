package com.doxa360.yg.android.darling;

import android.app.Application;

public class WallpaperApp extends Application {

    public static final String WALLPAPER = "WALLPAPER";
    public static final String CATEGORY = "CATEGORY";
    public static final String ALBUM = "ALBUM";

    public static final String SERVER_BASE_URL = "https://devstudios.ng/darling/api/v1/";
    public static final String PHOTO_URL = "https://devstudios.ng/darling/wp/";
    public static final String THUMBNAIL_URL = "https://devstudios.ng/darling/wp/thumb/";
    public static final String SMALLER_THUMBNAIL_URL = "https://devstudios.ng/darling/wp/thumb/small/";
    public static final String SYNC_URL = "http://devstudios.ng/darling/wp/sync/";

    public static final String RECENT_WALLPAPERS = "recent";
    public static final String CATEGORY_BY_ID = "category";
    public static final String ALL_CATEGORIES = "categories";
    public static final String TOP_WALLPAPERS = "top";
    public static final String SIGN_IN_API = "login";
    public static final String SIGN_UP_API = "create";
    public static final String SYNC_API = "sync";
    public static final String RETRIEVE_API = "retrieve";

    public static final String APP_NAME = "com.doxa360.yg.android.darling";
}
