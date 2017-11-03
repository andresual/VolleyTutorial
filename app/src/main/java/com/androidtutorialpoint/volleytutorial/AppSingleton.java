    package com.androidtutorialpoint.volleytutorial;

    import android.content.Context;
    import android.graphics.Bitmap;
    import android.util.LruCache;

    import com.android.volley.Request;
    import com.android.volley.RequestQueue;
    import com.android.volley.toolbox.ImageLoader;
    import com.android.volley.toolbox.Volley;

    /**
     * Created by andresual on 10/12/2017.
     */
    public class AppSingleton {
        private static AppSingleton mAppSingletonInstance; //mrmbuat instance
        private RequestQueue mRequestQueue; //membuat request
        private ImageLoader mImageLoader; //membuat image loader (jika ada gambar)
        private static Context mContext;

        private AppSingleton(Context context) {
            mContext = context;
            mRequestQueue = getRequestQueue();

            mImageLoader = new ImageLoader(mRequestQueue,
                    new ImageLoader.ImageCache() {
                        private final LruCache<String, Bitmap>
                                cache = new LruCache<String, Bitmap>(20);

                        @Override
                        public Bitmap getBitmap(String url) {
                            return cache.get(url);
                        }

                        @Override
                        public void putBitmap(String url, Bitmap bitmap) {
                            cache.put(url, bitmap);
                        }
                    });
        }

        //control multiple thread
        //supaya thread ditangani secara bergantian
        public static synchronized AppSingleton getInstance(Context context) {
            if (mAppSingletonInstance == null) {
                mAppSingletonInstance = new AppSingleton(context);
            }
            return mAppSingletonInstance;
        }

        //getter requestQueue
        public RequestQueue getRequestQueue() {
            if (mRequestQueue == null) {
                // getApplicationContext() adalah kunci, mnecegah dari leaking the
                // activity atau broadcastreceiver jika seseorang melewatinya.
                mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
            }
            return mRequestQueue;
        }

        //menampung request
        public <T> void addToRequestQueue(Request<T> req,String tag) {
            req.setTag(tag);
            getRequestQueue().add(req);
        }

        //getter imageLoader
        public ImageLoader getImageLoader() {
            return mImageLoader;
        }


        public void cancelPendingRequests(Object tag) {
            if (mRequestQueue != null) {
                mRequestQueue.cancelAll(tag);
            }
        }

    }