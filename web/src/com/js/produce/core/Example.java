package com.js.produce.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by 余青松 on 2017/8/3.
 */
public class Example extends JSFunctionServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response){

    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response){
        this.doGet(request,response);
    }

    @Override
    public void setJsFunction(){

    }
}
