<%--
  Created by IntelliJ IDEA.
  User: 余青松
  Date: 2017/8/4
  Time: 13:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
	<title>Test3</title>
	<link rel="stylesheet" href="../css/bootstrap/3.3.7/css/bootstrap.min.css">
	<script src="../css/bootstrap/3.3.7/js/jquery.min.js"></script>
	<script src="../css/bootstrap/3.3.7/js/bootstrap.min.js"></script>

	<%--设置选择功能的参数设置区的显示与隐藏--%>
	<script type="text/javascript">
		jQuery(document).ready(function () {
			var checkBox = jQuery("input:checkbox[name=functionsNeeded]");
			for(var i=0;i<checkBox.length ; ++i){
			    jQuery(checkBox[i]).change(function () {
                    parameterID = jQuery(this).val()+ "Parameters";
                    parameter = jQuery("#"+parameterID);
			        if(jQuery(this).is(":checked")){
                        parameter.show();
			        }else{
			            parameter.hide("slow");
			        }

                });
			}
        });
	</script>

	<%--设置光标跳转中关于下一控件定位方式的显示与隐藏--%>
	<script type="text/javascript">
        jQuery(document).ready(function () {
            var radioActivation = jQuery("input:radio[name=activation][value=no]");
            var div = jQuery("#setNextControl");
            jQuery(radioActivation).change(function () {
                    if(jQuery(this).is(":checked")){
                        div.hide();
                    }
            });
            radioActivation = jQuery("input:radio[name=activation][value=yes]");
            jQuery(radioActivation).change(function () {
                if(jQuery(this).is(":checked")){
                    div.show();
                }
            });
	        var radioSet = jQuery("input:radio[name=nextControlWay][value=default]");
	        jQuery(radioSet).change(function () {
	            var parameter = jQuery("#nextControlCustomize");
	            if(jQuery(this).is(":checked")){
	                parameter.hide();
	            }
	        });
            var radioSet = jQuery("input:radio[name=nextControlWay][value=customize]");
            jQuery(radioSet).change(function () {
                var parameter = jQuery("#nextControlCustomize");
                if(jQuery(this).is(":checked")){
                    parameter.show();
                }
            });
        });
	</script>

	<%--设置 高级设置中  显示与隐藏--%>
	<script type="text/javascript">
        jQuery(document).ready(function () {
            var button = jQuery("#hignLevelSet");
            var buttonState = true;     // true 表示隐藏 false 表示展开
	        var span = jQuery(button).find("span").eq(1);
	        jQuery(button).click(function () {
	            if(buttonState){
	                buttonState = false;
	                jQuery("#locateFunctionParameters").show();
	               jQuery(span).attr("class","glyphicon glyphicon-chevron-up");
	            }else{
	                buttonState = true;
	                jQuery("#locateFunctionParameters").hide();
                    jQuery(span).attr("class","glyphicon glyphicon-chevron-down");
	            }
            });

            var div = jQuery("#editLocateJs");
            var radioSet = jQuery("input:radio[name=locateFunction][value=default]");
            jQuery(radioSet).change(function () {
                if(jQuery(this).is(":checked")){
                    div.hide();
                }
            });
            radioSet = jQuery("input:radio[name=locateFunction][value=customize]");
            jQuery(radioSet).change(function () {
                if(jQuery(this).is(":checked")){
                    div.show();
                }
            });

        });
	</script>
	
	<script type="text/javascript" language="JavaScript">
		<!--
        function resetPage() {
             if(window.confirm("确认重置？")){
                 history.go(0);
             }
        }
		//-->

	</script>
</head>

<body style="background-color: transparent" >

<%--设置背景--%>
<div style="position:absolute;z-index:-1;width:100%;height:100%;">
	<img src="../resources/images/background.jpg" width="100%" height="100%" />
</div>

<div class="row clearfix" style="width: 100%;padding-top: 5%" >
	<div class="col-lg-offset-2 col-lg-8" style="background-color:white ">
			<%--标题--%>
			<div class="col-lg-12">
				<h1>
					<a class="navbar-brand" style="">JS脚本生成器</a>
				</h1>
			</div>

			<div class="col-lg-12 form-group">
				<%--导航栏--%>
				<ul class="nav nav-tabs">
					<li class="active"><a class="navbar-header">横向表格</a></li>
					<li><a href="./CreateVerTableJs.jsp">纵向表格</a></li>
				</ul>

				<br>

				<form method="get" action="/test">
					<%--输入jsId控件--%>
					<div class="col-lg-12" style="padding: 0%">
						<div class="col-lg-3" style="padding: 0%">
							<input id="jsId" name="jsId" type="text" class="form-control"  placeholder="请输入脚本 Id"/>
						</div>
						<div class="col-lg-2">
							<h4 style="color: red"> * </h4>
						</div>
					</div>

					<br>

					<%--选择功能区--%>
					<div class="col-lg-12" style="padding: 0%">
						<div class="col-lg-3" style="padding: 0%;">
							<span class="text-primary">请选择你需要的功能：</span>
						</div>

						<div class="col-lg-9" style="padding: 0%">
							<%--实时核查-数值检测--%>
							<div class="col-lg-12" style="padding: 0%">
								<div class="col-lg-12" style="padding: 0%">
									<input  name="functionsNeeded" type="checkbox" class="checkbox-inline" value="RealTimeVer" style="margin: 0%"/>
									<span class="text-muted"> 实时核查-数值范围检测 </span>
								</div>
								<%--参数获取--%>
								<div id="RealTimeVerParameters" class="col-lg-12" style="padding: 0%;display: none" >
									<%--取值范围预览--%>
									<div class="col-lg-12" id="displayRealTimeVer">
										<span class="text-muted">取值范围 : </span>
										<span class="text-muted">(</span>
										<span class="text-primary" id="displayLower">-∞</span>
										<span class="text-muted">,</span>
										<span class="text-primary" id="displayUpper">+∞</span>
										<span class="text-muted">)</span>
										<span class="text-danger" id="reminderRealTimeVer">取值范围异常</span>
										<script type="text/javascript">
                                            jQuery(document).ready(function () {


                                                jQuery("#reminderRealTimeVer").hide();
                                                jQuery("#lowerBound").keyup(function () {
                                                    var lower = jQuery("#lowerBound").val();
                                                    var upper =  jQuery("#upperBound").val();

                                                    // 设置文本内容
                                                    if(lower == ""){
                                                        jQuery("#displayLower").text("-∞");
                                                        jQuery("#reminderRealTimeVer").hide();
                                                    }else{
                                                        jQuery("#displayLower").text(jQuery("#lowerBound").val());
                                                    }

                                                    // 检测取值范围
                                                    if(lower!= "" && upper!= ""){
                                                        lower = parseFloat(lower);
                                                        upper = parseFloat(upper);
                                                        if(lower > upper){
                                                            jQuery("#reminderRealTimeVer").show();
                                                        }else {
                                                            jQuery("#reminderRealTimeVer").hide();
                                                        }
                                                    }
                                                });

                                                jQuery("#upperBound").keyup(function () {
                                                    var lower = jQuery("#lowerBound").val();
                                                    var upper =  jQuery("#upperBound").val();

                                                    // 设置文本内容
                                                    if(upper == ""){
                                                        jQuery("#displayUpper").text("+∞");
                                                        jQuery("#reminderRealTimeVer").hide();
                                                    }else{
                                                        jQuery("#displayUpper").text(jQuery("#upperBound").val());
                                                    }

                                                    // 检测取值范围
                                                    if(lower!= "" && upper!= ""){
                                                        lower = parseFloat(lower);
                                                        upper = parseFloat(upper);
                                                        if(lower > upper){
                                                            jQuery("#reminderRealTimeVer").show();
                                                        }else {
                                                            jQuery("#reminderRealTimeVer").hide();
                                                        }
                                                    }
                                                });


                                            });
										</script>
									</div>
									<%--下界参数--%>
									<div class="col-lg-4">
										<input id="lowerBound" name="lowerBound" type="text" class="form-control"  placeholder="输入上界"/>
									</div>
									<h5 class="col-lg-1 text-center"  style="padding: 0%;width: auto">
										<a><span class="glyphicon glyphicon-resize-horizontal"></span></a>
									</h5>
									<%--上界参数--%>
									<div class="col-lg-4">
										<input id="upperBound" name="upperBound" type="text" class="form-control"  placeholder="输入下界"/>
									</div>
									<br>
									<br>
									<br>

								</div>
							</div>
							<br>

							<%--光标跳转--%>
							<div class="col-lg-12" style="padding: 0%">
								<div class="col-lg-12" style="padding: 0%">
									<input name="functionsNeeded" type="checkbox" class="checkbox-inline" value="FocusTransfer" style="margin: 0%"/>
									<span class="text-muted"> 光标跳转 </span>
								</div>
								<%--参数获取--%>
								<div id="FocusTransferParameters" class="col-lg-12" style="padding: 0%;display: none">
									<%--设置字符输入长度--%>
									<div class="col-lg-12"  >
										<div class="col-lg-4" style="padding: 0%">
											<input type="text" name="maxLength" id="maxLength" class="form-control" placeholder="请设置最大输入字符个数"/>
										</div>
									</div>
									<%--设置是否激活光标跳转--%>
									<div class="col-lg-12">
										<div class="col-lg-4" style="padding: 0%">
											<span type="text" class="text-muted">激活光标跳转：</span>
										</div>
										<div class="col-lg-2" style="padding: 0%">
											<input type="radio" name="activation"  checked="checked" value="yes"/>
											<span type="text" class="text-primary">yes</span>
										</div>
										<div class="col-lg-2" style="padding: 0%">
											<input type="radio" name="activation" value="no" />
											<span type="text" class="text-primary">no</span>
										</div>
									</div>
									<%--设置下一控件定位--%>
									<div class="col-lg-12" id="setNextControl">
										<div class="col-lg-12"  style="padding: 0%">
											<div class="col-lg-4" style="padding: 0%">
												<span type="text" class="text-muted">设置下一控件定位方式：</span>
											</div>
											<div class="col-lg-2" style="padding: 0%">
												<input type="radio" name="nextControlWay"  checked="checked" value="default"/>
												<span type="text" class="text-primary">默认</span>
											</div>
											<div class="col-lg-2" style="padding: 0%">
												<input type="radio" name="nextControlWay" value="customize" />
												<span type="text" class="text-primary">自定义</span>
											</div>
										</div>

										<div class="col-lg-12" style="padding: 0%;display: none" id="nextControlCustomize">
											<div class="col-lg-8" style="padding: 0%">
												<span type="text" class="text-success">示例: jQuery(location).parents('tr').eq(1).next('tr').find('input') </span>
											</div>
											<div class="col-lg-8" style="padding: 0%">
												<textarea class="text-muted form-control" name="nextControl" rows="2"></textarea>
											</div>
										</div>

									</div>

									<br>
									<br>
									<br>
								</div>
							</div>
							<br>

							<%--radio撤销--%>
							<div class="col-lg-12" style="padding: 0%">
								<div class="col-lg-12" style="padding: 0%">
									<input name="functionsNeeded" type="checkbox" class="checkbox-inline" value="RadioRevoked" style="margin: 0%"/>
									<span class="text-muted"> radio撤销 </span>
								</div>
							</div>
							<br>


							<%--设置文本框样式--%>
							<div class="col-lg-12" style="padding: 0%">
								<div class="col-lg-12" style="padding: 0%">
									<input name="functionsNeeded" type="checkbox" class="checkbox-inline" value="ChangeStyle" style="margin: 0%"/>
									<span class="text-muted"> 设置文本框样式 </span>
								</div>

								<%--参数获取--%>
								<div id="ChangeStyleParameters" class="col-lg-12" style="padding: 0%;display: none">
									<%--输入框的样式--%>
									<div class="col-lg-12"  >
										<div class="col-lg-3" style="padding: 0%;">
											<h5 class="text-muted">选择输入框样式：</h5>
										</div>
										<div id="styleNeedModel">
											<%--选择框 设置样式名--%>
											<div class="col-lg-4"  style="padding-left: 0%">
												<select class="form-control" name="stylesNeedName" >
													<option>width</option>
													<option>font-size</option>
													<option>color</option>
													<option>background-color</option>
												</select>
											</div>
											<h5 class="col-lg-1 text-center"  style="padding: 0%;width: auto">
												<a><span class="glyphicon glyphicon-resize-horizontal"></span></a>
											</h5>
											<%--设置样式具体值--%>
											<div class="col-lg-4"   id="stylesNeedValue" >
												<input type="text" name="stylesNeedValue" class="form-control"/>
											</div>
										</div>
									</div>
									<%--添加按钮--%>
									<div class="col-lg-12" >
										<div class="col-lg-offset-3" style="padding: 0%" >
											<input  type="button" value="增加" class="btn btn-primary" id="addNewStyle" />
										</div>

										<%--动态添加样式输入框--%>
										<script type="text/javascript">
                                            jQuery(document).ready(function () {
                                                jQuery("#addNewStyle").click(function () {
                                                    var newStyle = jQuery("#styleNeedModel").html();
                                                    newStyle =  "<br><br><div class=\"col-lg-12\" style=\"padding: 0%;\">"+
                                                        "<div class=\"col-lg-3\" style=\"padding: 0%;\"></div>" +
                                                        newStyle +
                                                        "</div>";
                                                    var before = jQuery("#styleNeedModel").parent();
                                                    jQuery(before).append(newStyle);
                                                });
                                            });
										</script>
									</div>

									<br>
									<br>
									<br>
								</div>
							</div>
							<br>

							<%--大小写转换--%>
							<div class="col-lg-12" style="padding: 0%">
								<div class="col-lg-12" style="padding: 0%">
									<input name="functionsNeeded" type="checkbox" class="checkbox-inline" value="ToUpperCase" style="margin: 0%"/>
									<span class="text-muted"> 大小写转换 </span>
								</div>
								<%--参数获取--%>
								<div id="ToUpperCaseParameters" class="col-lg-12" style="padding: 0%;display: none">
									<div class="col-lg-12">
										<div class="col-lg-3" style="padding: 0%">
											<span class="text-muted">选择转换模式：</span>
										</div>
										<div class="col-lg-3" style="padding: 0%">
											<input type="radio" name="toUpper" value="true" checked="checked"/>
											<span class="text-primary">小写转大写</span>
										</div>
										<div class="col-lg-3" style="padding: 0%">
											<input type="radio" name="toUpper" value="false"/>
											<span class="text-primary">大写转小写</span>
										</div>

										<br>

									</div>
								</div>
							</div>
						</div>

					</div>

					<%--控件定位--%>
					<div class="col-lg-12" style="padding: 0%">
						<div class="col-lg-12 " style="padding: 0%;">
							<button type="button" class="btn" style="outline: none" id="hignLevelSet">
								<span>高&nbsp;级</span>
								<span class="glyphicon glyphicon-chevron-down"></span>
							</button>
						</div>
						<div id="locateFunctionParameters" class="col-lg-12 " style="padding: 0%;display: none">
							<%--选择定位目标控件的脚本--%>
							<div class="col-lg-12" style="padding: 0%">
								<div class="col-lg-12"  style="padding: 0%">
									<div class="col-lg-2" style="padding: 0%">
										<span type="text" class="text-muted">控件定位脚本选择：</span>
									</div>
									<div class="col-lg-1" style="padding: 0%">
										<input type="radio" name="locateFunction"  checked="checked" value="default"/>
										<span type="text" class="text-primary">默认</span>
									</div>
									<div class="col-lg-1" style="padding: 0%">
										<input type="radio" name="locateFunction" value="customize" />
										<span type="text" class="text-primary">自定义</span>
									</div>
								</div>
								<br>
								<%--控件定位脚本编辑区--%>
								<div class="col-lg-12" style="padding: 0%;display: none" id="editLocateJs">
									<%--外部控件定位--%>
									<div class="col-lg-12" style="padding: 0%">
										<div class="col-lg-2"style="padding: 0%">
										<span class="text-muted">
											外部控件定位脚本：
										</span>
										</div>
										<div class="col-lg-8" style="padding: 0%">
											<span type="text" class="text-success">示例 : jQuery("#locateControl").parents("tr").eq(0); </span>
											<%--示例--%>
											<textarea class="text-muted form-control" name="locateExternalControl" rows="2"></textarea>
										</div>
									</div>

									<%--目标控件定位--%>
									<div class="col-lg-12" style="padding: 0%">
										<div class="col-lg-2"style="padding: 0%">
										<span class="text-muted">
											目标控件定位脚本：
										</span>
										</div>
										<div class="col-lg-8" style="padding: 0%">
											<span type="text" class="text-success">示例 : jQuery(location).find("input");</span>
											<%--示例--%>
											<textarea class="text-muted form-control" name="locateInternalControl" rows="2"></textarea>
										</div>
									</div>
								</div>


							</div>
						</div>
					</div>

					<%--提交与重置按钮 --%>
					<div class="col-lg-offset-3 col-lg-9" style="padding: 0% ">
						<input type="submit" class="btn btn-primary" value="确&nbsp;&nbsp;认"/>
						<input type="button" class="btn btn-default" value="重&nbsp;&nbsp;置" onClick="resetPage()"/>
					</div>
				</form>

			</div>
		</div>

</div>

</body>
</html>