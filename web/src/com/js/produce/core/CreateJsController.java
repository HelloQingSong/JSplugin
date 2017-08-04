package com.js.produce.core;

import javax.servlet.http.HttpServlet;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 *
 */
public abstract class CreateJsController {

    // 获取jsResult.properties
    protected ResourceBundle jsResultResourceBundle;
    // 获取controls.properties
    protected ResourceBundle controlsResourceBundle = ResourceBundle.getBundle("properties.general");
    // jQuery 库文件路径
    protected String jQueryPath = "" ;
    // 最终生成的js脚本
    protected String jsResult = "";
    // 最终的核心功能
    protected String jsFunction = "";
    public CreateJsController(){
        try{
            jQueryPath = controlsResourceBundle.getString("jQueryPath");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /*
    * 获取最终的js脚本
    */
    protected abstract String getJsResult(Map<String, String[]> userNeeds);



}
