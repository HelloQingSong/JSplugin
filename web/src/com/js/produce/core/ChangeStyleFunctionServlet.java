package com.js.produce.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 *
 * 生成改变文本框样式的 js 脚本
 * 对于 该 js 脚本， 需要获取:以下几个参数
 * 1. 添加的额外控件 --> 属性由配置文件设定，页面需显示其id号
 * 2. 目标控件的位置（该js脚本只与文本框相关，因此不需要获取其他控件的位置）--> 显示默认值，但可修改
 * 3. 目标控件的样式 --> "style-name" : "style"
 * 测试用url
 * http://localhost:8081/changeStyleJS?addControls=1&locateExternalControl=default&locateInternalControl=default&controlStyles="width":"100"&controlStyles="color":"red"
 */
public class ChangeStyleFunctionServlet  extends JSFunctionServlet{
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response){

        try{
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset='uft-8'");

            // 清空缓存
            this.clear();

            // 获取添加的控件
            String[] addControls_str = request.getParameterValues("addControls");
            this.setAddControls(addControls_str);

            // 获取外部控件定位
            String[] locate_external_str = request.getParameterValues("locateExternalControl");
            this.setLocateExternalControl(locate_external_str);

            // 获取内部控件定位
            String[] locate_internal_str = request.getParameterValues("locateInternalControl");
            this.setLocateInternalControl(locate_internal_str);

            // 获取控件样式
            controlStyles = new ArrayList<String>(Arrays.asList(request.getParameterValues("controlStyles")));

            // 生成核心功能 js
            this.setJsFunction();
            // 生成 最终 js 脚本
            System.out.println(this.getJsResult());
        }catch (Exception e ){
            e.printStackTrace();
        }

    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response){
        this.doGet(request,response);
    }

    /*
     * 生成核心功能 js
     */
    @Override
    public void setJsFunction(){
        if(controlStyles != null && controlStyles.size()>0) {
            // 设置样式
            jsFunction+="\t\tjQuery(item0).css({";
            jsFunction += controlStyles.get(0);
            for( int i=1 ; i<controlStyles.size(); ++i) {
                jsFunction += ","+controlStyles.get(i);
            }
            jsFunction+="});\n";
        }
    }
}
