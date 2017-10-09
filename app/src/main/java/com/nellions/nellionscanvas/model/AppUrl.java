package com.nellions.nellionscanvas.model;

/**
 * Created by SCHOOL on 11/9/2015.
 */
public class AppUrl {

    /*
    production
    public String domainUrl = "http://nellions.co.ke/";
    public String restApiUrl = "http://nellions.co.ke/canvas/nellions_canvas_backend/rest/";
    */

    public String domainUrl = "https://nellions.co.ke/canvas/";
    //public String domainUrl = "http://192.168.43.94/canvas/";
    public String restApiUrl = "https://nellions.co.ke/canvas/nellions_canvas_backend/rest/";
    //public String restApiUrl = "http://192.168.43.94/canvas/nellions_canvas_backend/rest/";

    public String restApiMethod;

    /* initialize the rest method using the constructor */
    public AppUrl(String restApiMethod) {
        super();
        this.restApiMethod = restApiMethod;
    }

    public String getDomainUrl() {
        return domainUrl;
    }

    public void setDomainUrl(String domainUrl) {
        this.domainUrl = domainUrl;
    }

    public String getRestApiUrl() {
        return restApiUrl;
    }

    public void setRestApiUrl(String restApiUrl) {
        this.restApiUrl = restApiUrl;
    }

    public String getRestApiMethod() {
        return restApiMethod;
    }

    public void setRestApiMethod(String restApiMethod) {
        this.restApiMethod = restApiMethod;
    }

    /* returns the complete rest url */
    public String getCompleteUrl() {
        return getRestApiUrl() + getRestApiMethod();
    }
}
