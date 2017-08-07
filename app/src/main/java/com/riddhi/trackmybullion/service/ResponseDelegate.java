package com.riddhi.trackmybullion.service;

/**
 * Created by Rahul on 8/7/2017.
 */

public interface ResponseDelegate {
    void didReceiveResponse();
    void didReceiveError();
}