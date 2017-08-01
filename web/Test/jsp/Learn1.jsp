<%--
  Created by IntelliJ IDEA.
  User: 余青松
  Date: 2017/8/1
  Time: 14:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
	<title>Hello</title>
	<%-- Microsoft 的 CDN http://ajax.microsoft.com/ajax/jquery/jquery-1.4.min.js--%>
	<script type="text/javascript" src="../js/jquery.min.js">
	</script>

	<script type="text/javascript">
	</script>

	<script type="text/javascript">
        $(document).ready(function () {
            $("button").click(function () {
                $("p").hide();
            });
        });
	</script>
</head>
<body>


<h2>This is a heading</h2>
<p>This is a paragraph.</p>
<p>This is another paragraph.</p>
<button type="button">Click me</button>
</body>
</html>
