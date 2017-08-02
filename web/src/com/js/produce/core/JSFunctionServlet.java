package com.js.produce.core;

import javax.servlet.http.HttpServlet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * Created by 余青松 on 2017/8/2.
 * js脚本模板
 *
 */
public abstract class JSFunctionServlet extends HttpServlet{
    // 添加的额外控件
    protected ArrayList<String> addControls = new ArrayList<String>();
    // jQuery 库文件路径
    private String jQueryPath = "" ;
    // 目标控件的位置
    // 默认第一个为目标控件的位置 locations[0];
    // 当js脚本功能涉及多个控件时，除目标控件外的其他控件位置存储在 1、2...
    protected ArrayList<String> controlsLocations = new ArrayList<String>();
    // 目标控件样式
    protected HashMap<String,String> controlStyles = new HashMap<String,String>();
    // 生成的js脚本核心功能
    protected String jsFunction = "";
    // 最终生成的js脚本
    protected String jsResult = "";


    /*
    * 初始化时，获取jQuery库文件的目录
    */
    public JSFunctionServlet(){
        ResourceBundle resourceBundle = ResourceBundle.getBundle("./jsResult.properties");
        jQueryPath = resourceBundle.getString("jQueryPath");
    }

    public String getJsResult(){
        jsResult += "<script type='text/javascript'> \n\n";
        /***********************
         添加的额外控件位置
         ************************/
        for (int i = 0; i<addControls.size(); ++i){
            jsResult += addControls.get(i) + "\n";
        }

        /***********************
        引入jQuery的库文件
        ************************/
        jsResult += "<script src=" + jQueryPath + "></script> \n\n";

        /***********************
         功能区构造
         ************************/
        jsResult += "<script type=text/javascript>\n";
        jsResult += "\tjQuery(document).ready(function{\n";
        /***********************
         获取控件
         其中 item0 为 目标控件
         由于核心功能中，需要使用此处定义获得变量因此需要固定此处的变量名 为 item + j
         ****************/
        for( int j=0; j<controlsLocations.size(); ++j) {
            jsResult += "var item" + j + "=" + controlsLocations.get(j) + "\n";
        }

        /***********************
         核心功能
         ****************/
        jsResult += jsFunction;
        jsResult+="</script>";

        return jsResult;

    }

}
