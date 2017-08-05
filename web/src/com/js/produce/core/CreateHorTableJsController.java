package com.js.produce.core;

import java.util.ArrayList;
import java.util.HashMap;
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
public class CreateHorTableJsController extends CreateJsController {
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

    // 内部控件定位
    private  String locateInternalControl = "";

    // 目标控件样式-->组织形式 "style-name" : "style" 5
    private ArrayList<String> controlStyles = new ArrayList<String>();

    // 初始化
    public CreateHorTableJsController(){
        super();
        try{
            jsResultResourceBundle = ResourceBundle.getBundle("properties.horizontal");
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

    //  设置额外控件
//    private void setAddControls(String[] controlsNeed,String[] descriptions){
//        if(controlsNeed != null){
//            for (int i=0; i<controlsNeed.length ; ++i){
//                // 类型
//                String type = controlsNeed[i];
//                // controlBaseID
//                String baseId = jsResultResourceBundle.getString(type+"BaseID");
//                // id 组成 -- controlBaseID + jsId + i
//                String id = baseId +jsId+i;
//                String control = jsResultResourceBundle.getString(type).replaceAll("#" + baseId ,id);
//                if(control.contains("=description=")){
//                    control.replaceAll("=description=",descriptions[i]);
//                }
//                // 添加控件ID
//                addControlsId.add(id);
//                // 添加控件
//                addControls.add(control);
//            }
//        }
//
//    }
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
    // 2. 定位目标控件
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
        if( functionNeeds !=null && userNeeds != null){
            for(int i=0 ; i<functionNeeds.length ; ++i){
                switch (functionNeeds[i]){
                    case "RealTimeVer":
                        // 正无穷Number.POSITIVE_INFINITY
                        // 负无穷Number.NEGATIVE_INFINITY
                        // 最大值Number.MAX_VALUE
                        // 上界
                        String upper = userNeeds.get("upperBound")[0];
                        if(upper.equals("")) {
                            upper = "Number.POSITIVE_INFINITY";
                        }
                        // 下界
                        String lower = userNeeds.get("lowerBound")[0];
                        if(lower.equals("")){
                            lower = "Number.NEGATIVE_INFINITY";
                        }
                        // 添加扩展控件
                        String type = "span";
                        // controlBaseID
                        String baseId = jsResultResourceBundle.getString(type+"BaseID");
                        // id 组成 -- controlBaseID + jsId + i
                        String id = baseId +jsId+i;
                        String control = jsResultResourceBundle.getString(type).replaceAll("#" + baseId ,id);


                        String content = "请输入一个";
                        int temp = 0;
                        // 设置内容
                        if(!lower.equals("Number.NEGATIVE_INFINITY")){
                            content += "大于"+ lower;
                            ++temp;
                        }
                        if(!lower.equals("Number.NEGATIVE_INFINITY") && !upper.equals("Number.POSITIVE_INFINITY")){
                            content += "且";
                        }
                        if(!upper.equals("Number.POSITIVE_INFINITY")){
                            content += "小于" + upper;
                            ++temp;
                        }
                        if(temp>0){
                            content += "的数";
                        }else{
                            content += "数" ; // 当区间为  ( -∞ , +∞ )时，请输入任意一个数
                        }



                        control = control.replaceAll("=content=",content);

                        // 添加控件ID
                        addControlsId.add(id);
                        // 添加控件
                        addControls.add(control);

                        jsFunction += "\t\t" + "jQuery(targetItem).keyup(function(){\n";
                        jsFunction += "\t\t\t" + "var number = jQuery(targetItem).val(); \n";
                        jsFunction += "\t\t\t" + "number = Math.round(number); \n";
                        jsFunction += "\t\t\t" + "if("+lower+"<number & number <"+upper+"){\n";
                        jsFunction += "\t\t\t\t" + "jQuery('#"+id+"').hide();\n";
                        jsFunction += "\t\t\t" + "}else{\n";
                        jsFunction += "\t\t\t\t" + "jQuery('#"+id+"').show();\n";
                        jsFunction += "\t\t\t" + "}\n";
                        jsFunction += "\t\t" + "});\n";
                        break;

                    case "FocusTransfer":
                        String nextControl = userNeeds.get("nextControlWay")[0];
                        if(nextControl.equals("default")){
                            nextControl = "jQuery(location).parents('tr').eq(1).next('tr').find('input')";
                        }else if(nextControl.equals("customize")){
                            nextControl = userNeeds.get("nextControl")[0];
                        }
                        // 设置最大输入字符个数
                        jsFunction += "\t\t" + "jQuery(targetItem).attr('maxlength',"+userNeeds.get("maxLength")[0] +");\n";
                        // 是否激活光标转移
                        if(userNeeds.get("activation")[0].equals("yes")) {
                            // 定位下一个控件
                            jsFunction += "\t\t" + "var nextControl = "+nextControl+ ";\n";
                            jsFunction += "\t\t" + "jQuery(targetItem).keyup(function(){\n";
                            jsFunction += "\t\t\t" + "if(jQuery(targetItem).val().length == " + userNeeds.get("maxLength")[0] + "){\n";
                            jsFunction += "\t\t\t\t" + "nextControl.focus();\n";
                            jsFunction += "\t\t\t" + "}\n";
                            jsFunction += "\t\t" + "});\n";
                        }
                        break;
                    case "RadioRevoked":
                        jsFunction = "";
                        // 添加扩展控件
                        type = "img";
                        // controlBaseID
                        baseId = jsResultResourceBundle.getString(type+"BaseID");
                        // id 组成 -- controlBaseID + jsId + i
                        id = baseId +jsId+i;
                        control = jsResultResourceBundle.getString(type).replaceAll("#" + baseId ,id);

                        // 添加控件ID
                        addControlsId.add(id);
                        // 添加控件
                        addControls.add(control);

                        jsFunction += "\t\tjQuery('#"+id+"').click(function(){\n";
                        jsFunction += "\t\t\tfor(i=0; i<targetItem.length;++i){\n";
                        jsFunction += "\t\t\t\ttargetItem[i].checked = false;\n";
                        jsFunction += "\t\t\t}\n";
                        jsFunction += "\t\t\ttargetItem.change();\n";
                        jsFunction += "\t\t});\n";
                        return;
                    case "ChangeStyle":
                            String[] styleNames = userNeeds.get("stylesNeedName");
                            String[] styleValues = userNeeds.get("stylesNeedValue");
                            ArrayList<String> styles = new ArrayList<String>();
                            for( int p = 0; p<styleNames.length; ++p){
                                styles.add("'"+styleNames[p]+"':'"+styleValues[p]+"'");
                            }
                            // 设置样式
                            jsFunction+="\t\tjQuery(targetItem).css({";
                            jsFunction += styles.get(0);
                            for( int j=1 ; j<styles.size(); ++j) {
                                jsFunction += ","+styles.get(j);
                            }
                            jsFunction+="});\n";

                        break;
                    case "ToUpperCase":
                        jsFunction += "\n\t\tjQuery(targetItem).keyup(function(){ \n";
                        jsFunction += "\t\t\tjQuery(targetItem).val(jQuery(targetItem).val().toUpperCase());\n";
                        jsFunction += "\t\t});\n";
                        break;
                }
            }
        }
    }

    // 组装最终的js脚本
    private void setJsResult(){
        // 定位控件
        jsResult += locateControl + "\n";
        // 扩展控件
        if(addControls != null){
            for(int i = 0; i<addControls.size(); ++i){
                jsResult += addControls.get(i)+"\n";
            }
        }
        // 引入jQuery库文件
        jsResult += "\n<script src='" + jQueryPath + "'></script>\n\n";
        // 功能区构造
        jsResult += "<script type=text/javascript>\n";
        jsResult += "\tjQuery(document).ready(function(){\n";

        //定位目标控件
        // 1. 获取根据定位控件获取外部控件"<tr>"
        jsResult += "\t\tvar location = "+ locateExternalControl;
        // 2. 再根据外部控件获取内部控件
        jsResult += "\t\tvar targetItem = "+ locateInternalControl + "\n";

        // 核心功能
        jsResult += jsFunction;

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
        System.out.println("hello");
        try{
            // 设置定位控件 --> locateControl
            this.setLocateControl(userNeeds.get("jsId")[0]);
            // 设置扩展控件 --> addControls
            // this.setAddControls(userNeeds.get("additionalControls"),userNeeds.get("additionalDescriptions"));
            // 设置 locateExternalControl
            this.setLocateExternalControl(userNeeds.get("locateFunction")[0],userNeeds.get("locateExternalControl")[0]);
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

}
