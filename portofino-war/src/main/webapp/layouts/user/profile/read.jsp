<%@ page contentType="text/html;charset=ISO-8859-1" language="java"
         pageEncoding="ISO-8859-1"
%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"
%><%@taglib prefix="mde" uri="/manydesigns-elements"
%><stripes:layout-render name="/skins/${skin}/modal-page.jsp">
    <jsp:useBean id="actionBean" scope="request" type="com.manydesigns.portofino.actions.user.ProfileAction"/>
    <stripes:layout-component name="contentHeader">
        <stripes:submit name="updateConfiguration" value="Update configuration" class="contentButton"/>
        <stripes:submit name="cancel" value="Cancel" class="contentButton"/>
        <div class="breadcrumbs">
            <div class="inner">
                <mde:write name="breadcrumbs"/>
            </div>
        </div>
    </stripes:layout-component>
    <stripes:layout-component name="portletBody">
        <jsp:include page="readButtonsBar.jsp"/>
        <h1>Profile</h1>

        <mde:write name="actionBean" property="form"/>
        <h1>My groups</h1>
        <table>
            <thead>
                <tr>
                    <th>Name</th> <th>Description</th>
                </tr>
            </thead>
            <tbody>
                <s:iterator value="groups" var="group">
                <tr>
                    <td><s:property value="#group.name"/></td>
                    <td><s:property value="#group.description"/></td>
                </tr>
            </s:iterator>
            </tbody>
        </table>
        <jsp:include page="readButtonsBar.jsp"/>
        <input type="hidden" name="cancelReturnUrl" value="<c:out value="${actionBean.cancelReturnUrl}"/>"/>
    </stripes:layout-component>
    <stripes:layout-component name="contentFooter">
        <stripes:submit name="updateConfiguration" value="Update configuration" class="contentButton"/>
        <stripes:submit name="cancel" value="Cancel" class="contentButton"/>
    </stripes:layout-component>
</stripes:layout-render>