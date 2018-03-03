package mseffner.twitchnotifier.networking;

import com.android.volley.Response;

import java.util.HashMap;
import java.util.Map;


public class RequestFactory {

    // Note that this is a test client id and is not used for release versions
    private static final String CLIENT_ID = "6mmva5zc6ubb4j8zswa0fg6dv3y4xw";
    private static final String CLIENT_ID_HEADER = "Client-ID";
    private static final Map<String, String> headers = new HashMap<>();
    static { headers.put(CLIENT_ID_HEADER, CLIENT_ID); }

    @SuppressWarnings("unchecked")
    public static GsonRequest getRequest(int requestType, String url, Response.Listener listener,
                                         Response.ErrorListener errorListener) {
        GsonRequest request = null;
        switch (requestType) {
            case Requests.REQUEST_TYPE_USERS:
                request = getUsersRequest(url, listener, errorListener);
                break;
            case Requests.REQUEST_TYPE_STREAMS:
                request = getStreamsRequest(url, listener, errorListener);
                break;
            case Requests.REQUEST_TYPE_FOLLOWS:
                request = getFollowsRequest(url, listener, errorListener);
                break;
            case Requests.REQUEST_TYPE_GAMES:
                request = getGamesRequest(url, listener, errorListener);
                break;
        }
        return request;
    }

    private static GsonRequest getUsersRequest(String url, Response.Listener<Containers.Users> listener,
                                               Response.ErrorListener errorListener) {
        return new GsonRequest<>(url, Containers.Users.class, headers, listener, errorListener);
    }

    private static GsonRequest getFollowsRequest(String url, Response.Listener<Containers.Follows> listener,
                                                 Response.ErrorListener errorListener) {
        return new GsonRequest<>(url, Containers.Follows.class, headers, listener, errorListener);
    }

    private static GsonRequest getStreamsRequest(String url, Response.Listener<Containers.Streams> listener,
                                                 Response.ErrorListener errorListener) {
        return new GsonRequest<>(url, Containers.Streams.class, headers, listener, errorListener);
    }

    private static GsonRequest getGamesRequest(String url, Response.Listener<Containers.Games> listener,
                                               Response.ErrorListener errorListener) {
        return new GsonRequest<>(url, Containers.Games.class, headers, listener, errorListener);
    }
}
