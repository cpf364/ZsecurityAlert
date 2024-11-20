package com.zebra.zebradna;

interface IZDNARequest{
    /**
    * Call this method to request ZDNA to execute on Server.
    * @Params String json info to identify the request and inputData and more
    */
    String processRequest(in String json);

    /**
    * Call this method to fetch the response received from ZDNA server.
    * @Params String json info to identify the requestId and more
    * @Params String output will be json string
    */
    String getResponse(in String json);
}