package ca.jonnybauer.watched.Helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * This class is used to assist with creating and maintaining
 * a volley RequestQueue for API requests
 *
 * @author Jonathon Bauer
 * @version 1.0
 */
public class RequestHelper {
    private static RequestHelper instance;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private Context context;

    // Constructors
    private RequestHelper(Context context) {
        requestQueue = getRequestQueue(context);
        this.context = context;

        imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> cache = new LruCache<>(20);

            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });
    }

    // Get Instance Method
    public static synchronized RequestHelper getInstance(Context context) {
        if(instance == null) {
            instance = new RequestHelper(context);
        }
        return instance;
    }

    // Get Request Queue method
    public RequestQueue getRequestQueue(Context context) {
        if(requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        return requestQueue;
    }

    // Add to request Queue method
    public <T> void addToRequestQueue(Request<T> req, Context context) {
        getRequestQueue(context).add(req);
    }

    // Get Image Loader method
    public ImageLoader getImageLoader() {
        return imageLoader;
    }

}
