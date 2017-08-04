package com.js.produce.core;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * 生成纵向表的js脚本
 */
public class CreateVerTableJsController extends CreateJsController {
    public CreateVerTableJsController(){
        super();
        try{
            jsResultResourceBundle = ResourceBundle.getBundle("properties.vertical");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public String getJsResult(Map<String,String[]> userNeeds){
        return jsResult;
    }
}
