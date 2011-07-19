<%@ page contentType="text/html;charset=ISO-8859-1" language="java"
         pageEncoding="ISO-8859-1"
%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ taglib prefix="mde" uri="/manydesigns-elements"
%><jsp:include page="/skins/default/header.jsp"/>
<s:form method="post">
    <jsp:include page="/skins/default/model/tableData/createButtonsBar.jsp"/>
    <div id="inner-content">
        <h1>Create: <s:property value="qualifiedTableName"/></h1>
        <s:if test="form.requiredFieldsPresent">
            Fields marked with a "*" are required.
        </s:if>
        <mdes:write value="form"/>
        <s:hidden name="cancelReturnUrl" value="%{cancelReturnUrl}"/>
    </div>
    <jsp:include page="/skins/default/model/tableData/createButtonsBar.jsp"/>
</s:form>
<jsp:include page="/skins/default/footer.jsp"/>