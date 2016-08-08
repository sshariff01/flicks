package com.shariffproductions.flicks;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

public class HttpClient extends AsyncHttpClient {
    public void getNowPlayingMovies(ResponseHandlerInterface responseHandler) {
        String url = "http://api.themoviedb.org/3/movie/now_playing";
        RequestParams requestParams = new RequestParams();
        requestParams.put("api_key", "7b41c04250dbd7586e158310eb2cb5a0");
        requestParams.put("page", "1");

        this.get(url, requestParams, responseHandler);
    }
}
