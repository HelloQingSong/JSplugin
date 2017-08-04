package com.js.produce.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 实现大小写转换
 * 测试url
 * http://localhost:8081/toUpperCaseJS?addControls=1&locateExternalControl=default&locateInternalControl=default
 */
public class toUpperCaseFunctionServlet extends JSFunctionServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response){
        try {
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

            // 生成核心功能 js
            this.setJsFunction();
            // 生成 最终 js 脚本
            System.out.println(this.getJsResult());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response){
        this.doGet(request,response);
    }

    @Override
    public void setJsFunction(){
        jsFunction += "\n\t\tjQuery(item0).keyup(function(){ \n";
        jsFunction += "\t\t\tjQuery(item0).val(jQuery(item0).val().toUpperCase());\n";
        jsFunction += "\t\t});\n";
    }
}
