package com.js.produce.core;

import com.sun.deploy.util.StringUtils;

import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * 生成横向表的js脚本
 * 表单提交参数设定
 * jsId : 定位控件id后缀 -- js 脚本id
 *

 * // additionalControls : 添加的额外html控件 支持多个，由controls.properties决定，其id号结合相应的BaseID和jsId决定
 * // additionalDescriptions : 添加的额外控件中的文字描述，一般为 <spn></span> <p></p> 之间的描述文字
 * // additionalForFunction : 添加的额外控件会与一类功能绑定，且一类功能只能与一类控件绑定
 * functionsNeeded : 脚本所需的功能
 * 1. RealTimeVer -- 实时核查
 * 功能参数: upperBound : 上界 ==> 数字
 *          lowerBound : 下界 ==> 数字
 * 2. FocusTransfer -- 光标跳转
 * 功能参数: maxLength ：可以输入的最大字符个数 ==> 整数
 *          activation ： 是否激活光标跳转功能 ==> true or false
 *          nextControlWay : 选择以那种方式设置下一个控件定位脚本 default or customize
 *          nextControl ： 下一个控件定位脚本 ==> 自定义脚本
 *  3. RadioRevoked -- radio 选择撤销
 *  功能参数 : 无
 *  4. ChangeStyle -- 文本框样式
 *  功能参数 :
 *           stylesNeedName ： 'styleName'
 *           stylesNeedValue : '参数'
 *           stylesNeed ： 'styleName':'参数'
 *  5. ToUpperCase -- 强制转换大小写
 *  功能参数：
 *          toUpper: true==>默认，小写转大写；false ==> 大写转小写
 *  locateFunction : 定位目标控件方式 ==> default or customize(定制)
 * locateExternalControl : 定位外部控件 ==> 自定义脚本
 * locateInternalControl : 定位内部控件 ==> 自定义脚本
 *
 */
public class CreateVerTableJsController extends CreateJsController {
    // 注释
    private String jsAnnotations = "";
    // 定位控件
    private String locateControl = "";
    // 脚本id
    private String jsId = "";
    //  定位控件id
    private String locateControlID = "";

    // 目前认为添加扩展控件可作为一个单独的功能，由于这里只需要生成五类脚本，
    // 而脚本内部的实现细节应尽量少的由外界控制，用户的核心需求还是功能，而
    // 不是越来越多的细节设置，因此此处应弱化的添加扩展控件功能思考，核心还应放在关于核心功能算法的优化上
    // 因此思考两日后，于 2017/8/4 决定去除由用户选择扩展控件的功能，扩展控件当根据具体功能来设定
    // 因此，扩展控件的设置集成到 setJsFunction() 中
    // 扩展控件
    private ArrayList<String> addControls = new ArrayList<String>();
    // 扩展控件id
    private ArrayList<String> addControlsId = new ArrayList<String>();

    // 外部控件定位
    private String locateExternalControl ="";

    // 定位目标控件所在列数
    private String locateTargetColumn = "";

    // 内部控件定位
    private  String locateInternalControl = "";

    // hover-display-title.js 路径
    private String hoverDisplayTitleJsPath = "";

    // 初始化函数
    private String initFunctions = "";

    //  新添元素所需的初始化函数
    private String addNewInitFunctions = "";

    // 初始化
    public CreateVerTableJsController(){
        super();
        try{
            jsResultResourceBundle = ResourceBundle.getBundle("properties.vertical");
            hoverDisplayTitleJsPath = jsResultResourceBundle.getString("hoverDisplayTitleJsPath");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // 设置定位控件
    private void setLocateControl(String id){
        jsId = id;
        locateControl = jsResultResourceBundle.getString("locateControl");
        String locateControlBaseID = jsResultResourceBundle.getString("locateControlBaseID");
        locateControlID = locateControlBaseID + jsId;
        locateControl = locateControl.replaceAll("#"+locateControlBaseID,locateControlID);
    }

    // 目标控件定位
    // 1. 定位外部控件
    private void setLocateExternalControl(String choose,String newFunction ){
        switch (choose){
            case "default":
                locateExternalControl = jsResultResourceBundle.getString("locateExternalControl");
                locateExternalControl = locateExternalControl.replace(jsResultResourceBundle.getString("locateControlBaseID"),locateControlID);
                break;
            case "customize":
                locateExternalControl = newFunction;
                break;
            default:
                System.out.println("Some problems in locateExternalControl");
        }
    }

    // 2. 定位目标控件所处列数
    private void setLocateTargetColumn(String choose,String newFunction){

        switch (choose){
            case "default":
                locateTargetColumn = jsResultResourceBundle.getString("locateTargetColumn");
                locateTargetColumn = locateTargetColumn.replace("#"+jsResultResourceBundle.getString("locateTargetSpecialID"),"th_"+locateControlID);
                break;
            case "customize":
                locateTargetColumn = newFunction;
                break;
            default:
                System.out.println("Some problems in locateTargetColumn");
        }
    }
    // 3. 定位目标控件
    private void setLocateInternalControl(String choose,String newFunction ){
        switch (choose){
            case "default":
                locateInternalControl = jsResultResourceBundle.getString("locateInternalControl");
                break;
            case "customize":
                locateInternalControl = newFunction;
                break;
            default:
                System.out.println("Some problems in locateInternalControl");
        }
    }

    // 组装核心功能
    private void setJsFunction(String [] functionNeeds,Map<String,String[]> userNeeds){
        String locateFunctionTemp = "";
        //定位目标控件
        // 1. 获取根据定位控件获取外部控件"<tr>"
        locateFunctionTemp += "\t\t\tvar aim = "+ locateExternalControl;
        // 2. 获取目标控件所处列数
        locateFunctionTemp += locateTargetColumn;
        // 3. 再根据外部控件获取内部控件
        locateFunctionTemp += "\t\t\tvar targetItems = "+ locateInternalControl + "\n";

        if( functionNeeds !=null && userNeeds != null){
            // 添加扩展控件
            String type = "";
            // controlBaseID
            String baseId = "";
            // id 组成 -- controlBaseID + jsId + i
            String id = "";
            String control = "";

            for(int i=0 ; i<functionNeeds.length ; ++i){
                switch (functionNeeds[i]){
                    case "RealTimeVer":
                        initFunctions += "\n\t\t"+"RealTimeVer();\n";
                        addNewInitFunctions += "\n\t\t\t" + "RealTimeVer();"+ "\n";
                        // 注释
                        jsAnnotations += "<!--实时核查-数值范围检测-->\n";
                        jsFunction += "\t\tfunction RealTimeVer() {\n";
                        jsFunction += locateFunctionTemp;

                        // 正无穷Number.POSITIVE_INFINITY
                        // 负无穷Number.NEGATIVE_INFINITY
                        // 最大值Number.MAX_VALUE
                        // 上界
                        String upper = userNeeds.get("upperBound")[0];
                        upper = upper.replace(" ","");
                        if(upper.isEmpty()) {
                            upper = "Number.POSITIVE_INFINITY";
                        }
                        // 下界
                        String lower = userNeeds.get("lowerBound")[0];
                        lower = lower.replaceAll(" ","");
                        if(lower.isEmpty()){
                            lower = "Number.NEGATIVE_INFINITY";
                        }

                        // 左区间
                        String leftInterval = userNeeds.get("LeftInterval")[0];
                        String left = " < ";
                        if(leftInterval.equals("close")){
                            left = " <= ";
                        }
                        // 右区间
                        String rightInterval = userNeeds.get("RightInterval")[0];
                        String right = " < ";
                        if(rightInterval.equals("close")){
                            right = " <= ";
                        }

                        String content = "请输入一个";
                        int temp = 0;
                        // 设置内容
                        if(!lower.equals("Number.NEGATIVE_INFINITY")){
                            content += "大于";
                            if(leftInterval.equals("close")){
                                content += "等于";
                            }
                            content += lower;
                            ++temp;
                        }

                        if(!lower.equals("Number.NEGATIVE_INFINITY") && !upper.equals("Number.POSITIVE_INFINITY")){
                            content += "且";
                        }
                        if(!upper.equals("Number.POSITIVE_INFINITY")){
                            content += "小于";
                            if(rightInterval.equals("close")){
                                content += "等于";
                            }
                            content += upper;
                            ++temp;
                        }
                        if(temp>0){
                            content += "的数";
                        }else{
                            content += "数" ; // 当区间为  ( -∞ , +∞ )时，请输入任意一个数
                        }

                        // 添加扩展控件
                        type = "span";
                        // controlBaseID
                        baseId = jsResultResourceBundle.getString(type+"BaseID");
                        // id 组成 -- controlBaseID + jsId + i
                        id = baseId +jsId+i;
                        control = jsResultResourceBundle.getString(type).replaceAll("#" + baseId ,id);
                        control = control.replaceAll("=content=",content);
                        jsFunction += "\t\t\t" + "jQuery(targetItems).each(function(){\n";
                        jsFunction += "\t\t\t\t" + "jQuery(this).attr('title','"+content+"'); \n\n";

                        jsFunction += "\t\t\t\t"+"if (jQuery(this).parents('td').eq(0).find('#"+id+"').length == 0){ \n";
                        jsFunction += "\t\t\t\t\t" + "jQuery(this).parents('td').eq(0).find('input').last().after(\"<br>"+control+"\");\n";
                        jsFunction += "\t\t\t\t" + "}\n";

                        jsFunction += "\t\t\t\t" + "jQuery(this).keyup(function(){\n";
                        jsFunction += "\t\t\t\t\t" + "var number = jQuery(this).val(); \n";
                        jsFunction += "\t\t\t\t\t" + "number = Math.pow(number,1); \n";
                        jsFunction += "\t\t\t\t\t" + "if("+lower+left+"number & number "+right+upper+"){\n";
//                        jsFunction += "\t\t\t\t\t\t" + "jQuery(this).css({'color':'black','background-color':'#FFFF99'});\n";
                        jsFunction += "\t\t\t\t\t\t" + "jQuery(this).parents('td').eq(0).find('#"+id+"').hide();";
                        jsFunction += "\t\t\t\t\t" + "}else{\n";
                        jsFunction += "\t\t\t\t\t\t" + "jQuery(this).parents('td').eq(0).find('#"+id+"').show();\n";
                        jsFunction += "\t\t\t\t\t" + "}\n";
                        jsFunction += "\t\t\t\t" + "});\n";

                        jsFunction += "\t\t\t\t" + "jQuery(this).mouseout(function () {\n";
                        jsFunction += "\t\t\t\t\t" + "titleMouseOut(this);\n";
                        jsFunction += "\t\t\t\t" + "});\n";
                        jsFunction += "\t\t\t\t" + "jQuery(this).mouseover(function () {\n";
                        jsFunction += "\t\t\t\t\t" + "titleMouseOver(this,event,15);\n";
                        jsFunction += "\t\t\t\t" + "});\n";

                        jsFunction += "\t\t\t" + "});\n";
                        jsFunction += "\t\t}\n";
                        break;
                    case "FocusTransfer":
                        initFunctions += "\n\t\t"+"FocusTransfer();\n";
                        addNewInitFunctions += "\n\t\t\t" + "FocusTransfer();"+ "\n";
                        // 注释
                        jsAnnotations += "<!--光标跳转-->\n";
                        jsFunction += "\t\tfunction FocusTransfer() {\n";
                        jsFunction += locateFunctionTemp;

                        String nextControl = userNeeds.get("nextControlWay")[0];
                        if(nextControl.equals("default")){
                            nextControl = "\t\t\t\t\t\tvar allInputs = jQuery.find(\"input\");\n"
                                    + "\t\t\t\t\t\tvar target_ordinal = 0;\n"
                                    + "\t\t\t\t\t\tfor(var i = 0; i<allInputs.length; ++i){\n"
                                    + "\t\t\t\t\t\t\tif(allInputs[i].id ==this.id){\n"
                                    + "\t\t\t\t\t\t\t\ttarget_ordinal = i;\n"
                                    + "\t\t\t\t\t\t\t\tbreak;\n"
                                    + "\t\t\t\t\t\t\t}\n"
                                    + "\t\t\t\t\t\t}\n"
                                    + "\t\t\t\t\t\tvar nextControl = allInputs[target_ordinal+1];\n";
                        }else if(nextControl.equals("customize")){
                            nextControl = userNeeds.get("nextControl")[0];
                        }


                        jsFunction += "\t\t\t" + "jQuery(targetItems).each(function(){\n";
                        // 设置最大输入字符个数
                        jsFunction += "\t\t\t\t" + "jQuery(this).attr('maxlength','"+userNeeds.get("maxLength")[0]+"'); \n\n";
                        jsFunction += "\t\t\t\t" + "jQuery(this).attr('title','"+"请输入"+userNeeds.get("maxLength")[0]+"字符"+"'); \n\n";// 是否激活光标转移
                        if(userNeeds.get("activation")[0].equals("yes")) {
                            jsFunction += "\t\t\t\t" + "jQuery(this).keyup(function(){\n";
                            jsFunction += "\t\t\t\t\t" + "if(jQuery(this).val().length == " + userNeeds.get("maxLength")[0] + "){\n";
                            // 设置目标控件id
                            jsFunction += "\t\t\t\t\t\tif(jQuery(this).attr('id') == undefined || jQuery(this).attr('id') == \"\"){\n";
                            jsFunction += "\t\t\t\t\t\t\t" + "jQuery(this).attr('id',"+"'input_"+locateControlID+"');\n";
                            jsFunction += "\t\t\t\t\t\t}\n";
                            // 定位下一个控件
                            jsFunction +=  nextControl;
                            jsFunction += "\t\t\t\t\t\t" + "nextControl.focus();\n";
                            jsFunction += "\t\t\t\t\t" + "}\n";
                            jsFunction += "\t\t\t\t" + "});\n";
                        }
                        jsFunction += "\t\t\t" + "});\n";
                        jsFunction += "\t\t}\n";

                        break;
                    case "RadioRevoked":
                        initFunctions = "";
                        addNewInitFunctions = "";
                        initFunctions += "\n\t\t"+"RadioRevoked();\n";
                        addNewInitFunctions += "\n\t\t\t" + "RadioRevoked();"+ "\n";
                        // 注释
                        jsAnnotations += "<!--radio撤销-->\n";
                        jsFunction = "";
                        jsFunction += "\t\tfunction RadioRevoked() {\n";
                        jsFunction += locateFunctionTemp;
                        jsFunction += "\t\t\t"+"targetItems = jQuery(aim).parents('table').eq(0).children('tbody').children('tr').children('td:nth-child(' +(i+1) + ')');\n";
                        // 添加扩展控件
                        type = "img";
                        // controlBaseID
                        baseId = jsResultResourceBundle.getString(type+"BaseID");
                        // id 组成 -- controlBaseID + jsId + i
                        id = baseId +jsId+i;
                        control = jsResultResourceBundle.getString(type).replaceAll("#" + baseId ,id);

//                       // 添加控件ID
//                       addControlsId.add(id);
//                       // 添加控件
//                       addControls.add(control);

                        jsFunction += "\t\t\t" + "jQuery(targetItems).each(function(){\n";
                        // 设置最大输入字符个数
                        jsFunction += "\t\t\t\t"+"if (jQuery(this).find('#"+id+"').length == 0){ \n";
                        jsFunction += "\t\t\t\t\t" + "if (jQuery(this).children('br').length != 0){ \n";
                        jsFunction += "\t\t\t\t\t\t" + "jQuery(this).children('br').last().after(\""+control+"\");\n";
                        jsFunction += "\t\t\t\t\t" + "}else { \n";
                        jsFunction += "\t\t\t\t\t\t" + "jQuery(this).children('input[type=radio]').last().after(\""+control+"\"); \n ";
                        jsFunction += "\t\t\t\t\t" + "} \n";
                        jsFunction += "\t\t\t\t" + "} \n";
                        jsFunction += "\t\t\t\t" + "jQuery(this).find('#"+id+"').click(function(){\n";
                        jsFunction += "\t\t\t\t\t" + "var radioGroup = jQuery(this).parent().find('input');\n";
                        // 设置目标控件id
                        jsFunction += "\t\t\t\t\tfor (i = 0; i < radioGroup.length; i++){\n";
                        jsFunction += "\t\t\t\t\t\t" + "radioGroup[i].checked = false;\n";
                        jsFunction += "\t\t\t\t\t}\n";
                        // 定位下一个控件
                        jsFunction += "\t\t\t\t\t" + "radioGroup.change();\n";
                        jsFunction += "\t\t\t\t\t" + "\n";
                        jsFunction += "\t\t\t\t" + "});\n";

                        jsFunction += "\t\t\t" + "});\n";
                        jsFunction += "\t\t}\n";
                        return;
                    case "ChangeStyle":
                        initFunctions += "\n\t\t"+"ChangeStyle();\n";
                        addNewInitFunctions += "\n\t\t\t" + "ChangeStyle();"+ "\n";
                        // 注释
                        jsAnnotations += "<!--设置文本框样式-->\n";
                        jsFunction += "\t\tfunction ChangeStyle() {\n";
                        jsFunction += locateFunctionTemp;

                        String[] styleNames = userNeeds.get("stylesNeedName");
                        String[] styleValues = userNeeds.get("stylesNeedValue");
                        ArrayList<String> styles = new ArrayList<String>();
                        for( int p = 0; p<styleNames.length; ++p){
                            styles.add("'"+styleNames[p]+"':'"+styleValues[p]+"'");
                        }
                        // 设置样式
                        jsFunction+="\t\t\tjQuery(targetItems).each(function() {\n";
                        jsFunction += "\t\t\t\tjQuery(this).css({";
                        jsFunction += styles.get(0);
                        for( int j=1 ; j<styles.size(); ++j) {
                            jsFunction += ","+styles.get(j);
                        }
                        jsFunction+="});\n";
                        jsFunction+="\t\t\t});\n";
                        jsFunction+="\t\t}\n";

                        break;
                    case "ToUpperCase":
                        initFunctions += "\n\t\t"+"ToUpperCase();\n";
                        addNewInitFunctions += "\n\t\t\t" + "ToUpperCase();"+ "\n";
                        // 注释
                        jsAnnotations += "<!--大小写转换-->\n";

                        jsFunction += "\t\tfunction ToUpperCase() {\n";
                        jsFunction += locateFunctionTemp;

                        jsFunction+="\t\t\tjQuery(targetItems).each(function() {\n";
                        jsFunction += "\t\t\t\tjQuery(this).keyup(function(){ \n";
                        String[] choose = userNeeds.get("toUpper");
                        if(choose.length == 1) {
                            if(choose[0].equals("true")){
                                // 小写转大写
                                jsFunction += "\t\t\t\t\tjQuery(this).val(jQuery(this).val().toUpperCase());\n";
                            }else if(choose[0].equals("false")){
                                // 大写转小写
                                jsFunction += "\t\t\t\t\tjQuery(this).val(jQuery(this).val().toLowerCase());\n";
                            }
                        }
                        jsFunction += "\t\t\t\t});\n";
                        jsFunction += "\t\t\t});\n";
                        jsFunction += "\t\t}\n";
                        break;
                    case "SetInputContentAndReadOnly":
                        initFunctions += "\n\t\t"+"SetInputContentAndReadOnly();\n";
                        // 注释
                        jsAnnotations += "<!--设置输入框内容和只读属性-->\n";

                        jsFunction += "\t\tfunction SetInputContentAndReadOnly() {\n";
                        jsFunction += locateFunctionTemp;

                        String[] inputContents = userNeeds.get("inputContent");
                        String[] isReadonly = userNeeds.get("isReadOnly");
                        // 设置文本框内容
                        String inputContentArray = "var texts = new Array(";
                        inputContentArray += "\""+inputContents[0]+"\"";
                        for( int p = 1; p<inputContents.length; ++p){
                            inputContentArray += ",\""+inputContents[p]+"\"";
                        }
                        inputContentArray += ");";
                        // 设置文本框只读属性
                        String isReadOnlyArray = "var checks = new Array(";
                        isReadOnlyArray += ""+isReadonly[0]+"";
                        for( int p = 1; p<isReadonly.length; ++p){
                            isReadOnlyArray += ","+isReadonly[p]+"";
                        }
                        isReadOnlyArray += ");";
                        isReadOnlyArray = isReadOnlyArray.replaceAll("false,true","true");
                        System.out.println(isReadOnlyArray);
                        System.out.println();
                        jsFunction += "\t\t\t"+inputContentArray + "\n";
                        jsFunction += "\t\t\t"+isReadOnlyArray + "\n";
                        jsFunction += "\t\t\t" + "var j = 0;"+"\n";

                        jsFunction+="\t\t\tjQuery(targetItems).each(function() {\n";
                        jsFunction += "\t\t\t\tif(jQuery(this).attr(\"type\") == \"text\" && j <texts.length ){\n";
                        // 设置只读属性
                        jsFunction += "\t\t\t\t\t" + "if(checks[j]){\n";
                        jsFunction += "\t\t\t\t\t\tjQuery(this).attr(\"readonly\",\"readonly\");\n";
                        jsFunction += "\t\t\t\t\t"+"}"+"\n";
                        jsFunction += "\t\t\t\t\tjQuery(this).val(texts[j]);\n";
                        jsFunction += "\t\t\t\t\t"+"++j;"+"\n";
                        jsFunction += "\t\t\t\t"+"}"+"\n";
                        jsFunction += "\t\t\t});\n";
                        jsFunction += "\t\t}\n";
                        break;
                }
            }
        }
    }

    // 组装最终的js脚本
    private void setJsResult(){
        // 注释
        jsResult += jsAnnotations + "\n";
        // 定位控件
        jsResult += locateControl + "\n";
        // 扩展控件
        if(addControls != null){
            for(int i = 0; i<addControls.size(); ++i){
                jsResult += addControls.get(i)+"\n";
            }
        }
        // 引入jQuery库文件
        jsResult += "\n<script src='" + jQueryPath + "'></script>\n";
        // 引入hover-display-title.js
        jsResult += "\n<script src='" + hoverDisplayTitleJsPath + "'></script>\n\n";
        // 功能区构造
        jsResult += "<script type=text/javascript>\n";
        jsResult += "\tjQuery(document).ready(function(){\n";

        // 核心功能
        jsResult += jsFunction;

        // 使新添加的元素也具有功能
        jsResult += "\t\tjQuery('.btn.btn-primary').click(function() {";
        jsResult += addNewInitFunctions + "\n";
        jsResult += "\t\t});\n";

        // 初始化
        jsResult += initFunctions;

        jsResult+="\t});\n</script>";
    }


    // 设置核心功能
    // 1. 实时核查--数值范围检测
    // 2. 光标跳转
    // 3. radio选择撤销
    // 4. 改变文本框长度
    // 5. 大小写转换
    // 生成最终js脚本
    @Override
    public String getJsResult(Map<String,String[]> userNeeds){

        this.clear();
        try{
            // 设置定位控件 --> locateControl
            this.setLocateControl(userNeeds.get("jsId")[0]);
            // 设置扩展控件 --> addControls
            // this.setAddControls(userNeeds.get("additionalControls"),userNeeds.get("additionalDescriptions"));
            // 设置 locateExternalControl
            this.setLocateExternalControl(userNeeds.get("locateFunction")[0],userNeeds.get("locateExternalControl")[0]);
            // 设置 locateTargetColumn
            this.setLocateTargetColumn(userNeeds.get("locateFunction")[0],userNeeds.get("locateExternalControl")[0]);
            // 设置 locateInternalControl
            this.setLocateInternalControl(userNeeds.get("locateFunction")[0],userNeeds.get("locateInternalControl")[0]);
            // 设置 jsFunction
            this.setJsFunction(userNeeds.get("functionsNeeded"),userNeeds);
            // 设置 jsResult
            this.setJsResult();
        }catch (Exception e){
            e.printStackTrace();
        }

        return jsResult;
    }

    @Override
    protected void clear(){
        super.clear();
        // 外部控件定位
        locateExternalControl ="";
        // 内部控件定位
        locateInternalControl = "";
        // 定位控件
        locateControl = "";
        // 脚本id
        jsId = "";
        //  定位控件id
        locateControlID = "";
        addControlsId.clear();
        addControls.clear();
        // 注释
        jsAnnotations = "";
    }

}
