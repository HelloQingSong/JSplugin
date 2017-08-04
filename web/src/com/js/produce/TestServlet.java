package com.js.produce;

import com.js.produce.core.CreateHorTableJsController;
import com.js.produce.core.CreateVerTableJsController;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 余青松 on 2017/8/4.
 * 实时核查 -- http://localhost:8081/test?jsId=1&functionsNeeded=on&upperBound=1&lowerBound=10&locateFunction=default&locateExternalControl=%20&locateInternalControl=&functionsNeeded=FocusTransfer&nextControl=default&activation=yes&maxLength=4
 */
public class TestServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response){
        try{
            Map<String,String[]> userNeeds = request.getParameterMap();
            CreateHorTableJsController createVerTableJsController = new CreateHorTableJsController();

            System.out.println(createVerTableJsController.getJsResult(userNeeds));

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response){
        this.doGet(request,response);
    }
}
