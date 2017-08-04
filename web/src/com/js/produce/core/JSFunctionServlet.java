package com.js.produce.core;

import javax.servlet.http.HttpServlet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 *  js 脚本 模板
 *  js 脚本 与 所绑定的控件(即：目标控件)通常处在同一行
 *  因此 一般可采用 jQuery(this).parent("tr").find("input") 获取目标控件的位置
 *  但是 有的情况下，一行可能会有多个 "input" 控件，因此，默认为该方式，但是提供由用户输入框
 */
public abstract class JSFunctionServlet extends HttpServlet{

    // 获取jsResult.properties
    ResourceBundle jsResultResourceBundle = ResourceBundle.getBundle("properties.jsResult");

    // 获取controls.properties
    ResourceBundle controlsResourceBundle = ResourceBundle.getBundle("properties.controls");

    // jQuery 库文件路径
    private String jQueryPath = "" ;

    // 添加控件
    // 以下均默认第一个为定位控件
    protected ArrayList<String> addControls = new ArrayList<String>();
    // 添加控件id
    protected ArrayList<String> addControlsId = new ArrayList<String>();

    // 外部控件定位
    protected ArrayList<String> locateExternalControl = new ArrayList<String>()  ;
    // 内部控件定位
    protected  ArrayList<String> locateInternalControl = new ArrayList<String>();

    // 目标控件样式-->组织形式 "style-name" : "style" 5
    protected ArrayList<String> controlStyles;

    // 生成的js脚本核心功能
    protected String jsFunction = "";

    // 最终生成的js脚本
    protected String jsResult = "";



    /*
    * 初始化时，获取jQuery库文件的路径 --> 在配置文件 jsResult.properties 中
    */
    public void init(){
        try {
            System.out.println("init");
            jQueryPath = jsResultResourceBundle.getString("jQueryPath");
            addControlsId.add(jsResultResourceBundle.getString("controlForLocateId"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // 清空缓存
    protected void clear(){
        jsResult = "";
        jsFunction = "";
        if(addControlsId != null){
            addControlsId.clear();
            addControlsId.add(jsResultResourceBundle.getString("controlForLocateId"));
        }

        if(addControls!=null) {
            addControls.clear();
        }
        if(locateExternalControl != null) {
            locateExternalControl.clear();
        }
        if(locateInternalControl != null){
            locateInternalControl.clear();
        }
        if(controlStyles != null) {
            controlStyles.clear();
        }
    }

    /*
     * 根据 String[] 添加控件
     * 其中 String[0] 表示定位控件id后缀
     * 其它根据controls.properties中定义控件获取
     */
    public void setAddControls(String[] controls){
        if(controls !=null){
            for(int i=0; i<controls.length; ++i){
                if(i==0){
                    String controlForLocateId = addControlsId.get(0) + controls[i] ;
                    addControls.add("<div id='"+controlForLocateId+"'></div>");
                    addControlsId.set(0,controlForLocateId);
                }else {
                    // 获得控件的基本模板
                    String control= controlsResourceBundle.getString(controls[i]);
                    // 生成控件的id
                    String controlID = controlsResourceBundle.getString(controls[i]+"BaseID");
                    controlID += controls[0] + "" + i;

                    addControlsId.add(controlID);
                    addControls.add(control.replaceAll("#",controlID));
                }
            }
        }
    }

    /*
    * 设置外部控件定位
    * 当用户自定义外部控件定位方式时，应当提醒用户定位控件id为多少
    */
    public void setLocateExternalControl(String[] functions){
        for(int i=0; i<functions.length; ++i){
            if(i==0&&functions[i].equals("default")){
                locateExternalControl.add(jsResultResourceBundle.getString("locateExternalControl").replaceAll("controlForLocate",addControlsId.get(0)));
            }else{
                locateExternalControl.add(functions[i]);
            }
        }
    }

    /*
    * 设置内部控件定位
    */
    public void setLocateInternalControl(String[] functions){
        for(int i=0; i<functions.length ; ++i){
            if(i==0&&functions[i].equals("default")){
                locateInternalControl.add(jsResultResourceBundle.getString("locateInternalControl"));
            }else{
                locateInternalControl.add(functions[i]);
            }
        }
    }


    /*
    * 设置核心功能 js
    * */
    public abstract void setJsFunction();
    /*
    * 获取最终的js脚本
    */
    protected String getJsResult(){

        /***********************
         添加控件: 定位控件 + 额外控件
         ************************/
        if(addControls != null) {
            for (int i = 0; i < addControls.size(); ++i) {
                jsResult += addControls.get(i) + "\n";
            }
        }

        /***********************
        引入jQuery的库文件
        ************************/
        jsResult += "<script src=" + jQueryPath + "></script> \n\n";

        /***********************
         功能区构造
         ************************/
        jsResult += "<script type=text/javascript>\n";
        jsResult += "\tjQuery(document).ready(function(){\n";

        /***********************
         获取控件
         其中 item0 为 目标控件
         由于核心功能中，需要使用此处定义获得变量因此需要固定此处的变量名 为 item + j
         ****************/
        if(locateExternalControl!=null && locateInternalControl !=null) {
            for (int j = 0; j < locateExternalControl.size(); ++j) {
                // 获取根据定位控件获取外部控件"<tr>"
                jsResult += locateExternalControl.get(j);
                // 再根据外部控件获取内部控件
                jsResult += "\t\tvar item" + j + "=" + locateInternalControl.get(j) + "\n";
            }
        }

        /***********************
         核心功能
         ****************/
        jsResult += jsFunction;
        jsResult+="\t});\n</script>";

        return jsResult;
    }





}
