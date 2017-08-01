<%--
  Created by IntelliJ IDEA.
  User: 余青松
  Date: 2017/8/1
  Time: 14:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>

</head>
<body>

<%--改变文本框长度--%>
<div id="A" style="background-color: red">
	<input value="Hello"/>
</div>
<script type="text/javascript"  src="../js/jquery.min.js">
</script>
<script type="text/javascript">
    $.noConflict();
    jQuery(document).ready(function($){
            var T = $("#A").parent().parent().find("input");
            $(T).css({"background-color":"blue"});
            $(T).width(100);
        }
    );
</script>

<hr>

<%--radio 选择撤销--%>
<img id="undoradio1" src="images/bt_Restore.gif">
<script src="../js/jquery.min.js">// for OC versions before 3.1.4, usejquery-1.3.2.min.js !
</script>
<script lang="Javascript">
    $.noConflict();
    jQuery(document).ready(function($){
        $("#undoradio1").click(function (e) {
            for (i = 0; i < radioGroup1.length; i++) {
                radioGroup1[i].checked = false;
            }
            radioGroup1.change();
        });
        // identify the group radio buttons
        var radioGroup1 = $("#undoradio1").parent().parent().find("input");
    });
</script>

</body>
</html>
