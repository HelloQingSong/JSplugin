package com.js.produce.servlets;

import com.js.produce.core.CreateHorTableJsController;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by 余青松 on 2017/8/5.
 */
public class CreateHorTableJsServlet  extends HttpServlet{
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
