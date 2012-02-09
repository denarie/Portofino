<%@ page import="com.manydesigns.portofino.model.pages.AccessLevel" %>
<%@ page import="com.manydesigns.portofino.model.pages.RootPage" %>
<%@ page import="com.manydesigns.portofino.system.model.users.Group" %>
<%@ page contentType="text/html;charset=UTF-8" language="java"
         pageEncoding="UTF-8"
%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes-dynattr.tld"
%><%@ taglib prefix="mde" uri="/manydesigns-elements"
%><%@ taglib tagdir="/WEB-INF/tags" prefix="portofino"
%><%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<stripes:layout-render name="/skins/default/admin-page.jsp">
    <jsp:useBean id="actionBean" scope="request" type="com.manydesigns.portofino.actions.admin.RootPermissionsAction"/>
    <stripes:layout-component name="pageTitle">
        Root permission
    </stripes:layout-component>
    <stripes:layout-component name="contentHeader">
        <portofino:buttons list="root-permissions" cssClass="contentButton" />
    </stripes:layout-component>
    <stripes:layout-component name="portletTitle">
        Root permission
    </stripes:layout-component>
    <stripes:layout-component name="portletBody">
        <%
            RootPage rootPage = actionBean.getApplication().getModel().getRootPage();
        %>
        <table>
            <tr>
                <th>Group</th>
                <th>Access Level</th>
            </tr>
            <c:forEach var="group" items="${actionBean.groups}">
                <tr>
                    <%
                        Group group = (Group) pageContext.getAttribute("group");
                        String groupId = group.getGroupId();
                        AccessLevel localAccessLevel = actionBean.getLocalAccessLevel(rootPage, groupId);
                    %>
                    <td>
                        <c:out value="${group.name}"/>
                    </td>
                    <td>
                        <select name="accessLevels[${group.groupId}]">
                            <option value="<%= AccessLevel.NONE.name() %>"
                                    <%
                                        if (AccessLevel.NONE.equals(localAccessLevel)) {
                                            out.print("selected='selected'");
                                        }
                                    %>>
                                <fmt:message key="permissions.level.none" />
                            </option>
                            <option value="<%= AccessLevel.VIEW.name() %>"
                                    <%
                                        if (AccessLevel.VIEW.equals(localAccessLevel)) {
                                            out.print("selected='selected'");
                                        }
                                    %>>
                                <fmt:message key="permissions.level.view" />
                            </option>
                            <option value="<%= AccessLevel.EDIT.name() %>"
                                    <%
                                        if (AccessLevel.EDIT.equals(localAccessLevel)) {
                                            out.print("selected='selected'");
                                        }
                                    %>>
                                <fmt:message key="permissions.level.edit" />
                            </option>
                            <option value="<%= AccessLevel.DENY.name() %>"
                                    <%
                                        if (AccessLevel.DENY.equals(localAccessLevel)) {
                                            out.print("selected='selected'");
                                        }
                                    %>>
                                <fmt:message key="permissions.level.deny" />
                            </option>
                        </select>
                    </td>
                </tr>
            </c:forEach>
        </table>

    </stripes:layout-component>
    <stripes:layout-component name="contentFooter">
        <portofino:buttons list="root-permissions" cssClass="contentButton" />
    </stripes:layout-component>
</stripes:layout-render>