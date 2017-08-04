package com.js.produce.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by 余青松 on 2017/8/3.
 */
public class FocusTransferFunctionServlet extends JSFunctionServlet {
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
        jsFunction += "// 设置输入框字符个数\n" +
                        "jQuery(item0).attr(\"maxlength\",\"2\")\n";
        jsFunction += "\n";
        jsFunction += "\n";
        jsFunction += "\n";
        jsFunction += "\n";
    }
}
