package com.softaai.mpga.retrofit.api;

import com.softaai.mpga.model.ContactList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * @author Amol Pawar.
 */
public interface ApiService {

    /*
    Retrofit get annotation with our URL
    And our method that will return us the List of ContactList
    */
    @GET("/json_data.json")
    Call<ContactList> getMyJSON();
}
