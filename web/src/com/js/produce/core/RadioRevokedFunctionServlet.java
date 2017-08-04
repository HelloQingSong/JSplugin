package com.js.produce.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * radio撤销功能
 * 测试url
 * http://localhost:8081/radioRevokedJS?addControls=3&addControls=img&locateExternalControl=default&locateInternalControl=default
 */
public class RadioRevokedFunctionServlet extends JSFunctionServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response){
        try {
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset='uft-8'");
            HashMap<String,String[]> e = (HashMap<String,String[]>)request.getParameterMap();
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
        jsFunction += "\n\t\tjQuery("+addControlsId.get(1)+").click(function(){\n";
        jsFunction += "\t\t\tfor(i=0; i<item0.length;++i){\n";
        jsFunction += "\t\t\titem0[i].checked = false\n";
        jsFunction += "\t\t}\n";
        jsFunction += "\t\titem0.change();\n";
        jsFunction += "\t\t});\n";
    }
}
