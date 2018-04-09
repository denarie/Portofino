<%@ page import="com.manydesigns.elements.ElementsThreadLocals" %>
<%@ page import="com.manydesigns.elements.xml.XhtmlBuffer" %>
<%@ page import="com.manydesigns.portofino.pageactions.calendar.Event" %>
<%@ page import="com.manydesigns.portofino.pageactions.calendar.EventDay" %>
<%@ page import="com.manydesigns.portofino.pageactions.calendar.EventWeek" %>
<%@ page import="com.manydesigns.portofino.pageactions.calendar.WeekView" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="org.joda.time.DateTime" %>
<%@ page import="org.joda.time.Interval" %>
<%@ page import="org.joda.time.Duration" %>
<%@ page import="org.joda.time.format.DateTimeFormatter" %>
<%@ page import="org.joda.time.format.DateTimeFormatterBuilder" %>
<%@ page import="java.text.MessageFormat" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Locale" %>
<%@ page import="com.manydesigns.portofino.calendar.PresentationHelper" %>
<%@ page import="org.joda.time.LocalDate" %>
<%@ page contentType="text/html;charset=UTF-8" language="java"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes-dynattr.tld"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="actionBean" scope="request" type="com.manydesigns.portofino.pageactions.calendar.CalendarAction"/>
<%
    int maxEventsPerCell = actionBean.getConfiguration().getMaxEventsPerCellInMonthView();
    WeekView weekView = actionBean.getWeekView();
    DateTimeFormatter dayOfWeekFormatter =
            new DateTimeFormatterBuilder()
                    .appendDayOfWeekShortText()
                    .toFormatter()
                    .withLocale(request.getLocale());

    DateTimeFormatter firstDayOfMonthFormatter =
            new DateTimeFormatterBuilder()
                    .appendMonthOfYearShortText()
                    .appendLiteral(" ")
                    .appendDayOfMonth(1)
                    .toFormatter()
                    .withLocale(request.getLocale());

    DateTimeFormatter monthFormatter =
            new DateTimeFormatterBuilder()
                    .appendMonthOfYearText()
                    .appendLiteral(" ")
                    .appendYear(4, 4)
                    .toFormatter()
                    .withLocale(request.getLocale());
    DateTime referenceDateTime = actionBean.getReferenceDateTime();
    DateTimeFormatter dateFormatter =
            new DateTimeFormatterBuilder()
                    .appendDayOfWeekText()
                    .appendLiteral(" ")
                    .appendDayOfMonth(1)
                    .appendLiteral(" ")
                    .appendMonthOfYearText()
                    .appendLiteral(" ")
                    .appendYear(4, 4)
                    .toFormatter()
                    .withLocale(request.getLocale());

    DateTimeFormatter hhmmFormatter =
            new DateTimeFormatterBuilder()
                    .appendHourOfDay(2)
                    .appendLiteral(":")
                    .appendMinuteOfHour(2)
                    .toFormatter()
                    .withLocale(request.getLocale());
    
    XhtmlBuffer xhtmlBuffer = new XhtmlBuffer(out);
%>
<style type="text/css">
    .calendar-container {
        position: relative; height: <%= ((maxEventsPerCell + 1) * 24) * 6 %>px;
    }
    .days-table td, .days-table th {
        margin: 0; padding: 0 0 0 10px; border: none; text-align: left;
    }
    .week-table, .week-table td, .week-table th {
    	border: medium none;
    	 margin: 0; padding: 0 0 0 10px;
    	 text-align: left;
    }
    .calendar-table {
        position: absolute; width: 100%; margin-top: 10px; height: 90%;
        border-right: 1px solid #DDDDDD;
        border-bottom: 1px solid #DDDDDD;
    }
    .calendar-table td, .calendar-table th {
        border-style: none;
        border-top: 1px solid #DDDDDD;
        border-left: 1px solid #DDDDDD;
    }
    .calendar-container table {
        width: 100%; padding: 0; margin: 0; table-layout: fixed;
    }
    .calendar-row {
        position: absolute; width: 100%; height: <%= 100.0 / 6.0 %>%;
    }
    .grid-table {
        width: 100%; height: 100%;
        position: absolute; left: 0px; top: 0px;
        border: none;
    }
    .grid-table td {
        border-left: 1px solid #DDDDDD;
    }
    .grid-table td.today {
        border: solid 1px black;
    }
    .events-table {
        position: relative; border: none;
    }
    .events-table td {
        padding: 1px 1px 0 2px; border: none;
    }
    .events-table th {
        padding: 0 0 3px 10px; border: none; text-align: left;
    }
    .event {
        border-radius: 3px;
        font-size: smaller;
        letter-spacing: 0.03em;
        text-align: center;
        padding: 0 0 0 4px; white-space: nowrap; overflow: hidden;
    }
    .event .more{
        color: #a9a9a9;
        text-shadow: 0px 0px 1px rgba(255,255,255,0.5);
    }
    .event a {
        color: white;
        text-shadow: 0px 0px 1px rgba(0,0,0,0.5);
    }
    .outOfMonth {
        color: #BBBBBB;
    }
    .event-dialog {
        display: none;
    }
</style>
<h1><%= StringUtils.capitalize(monthFormatter.print(weekView.getReferenceDateTime())) %></h1>
<div>
    <div class="pull-right" >
        <button type="submit" name="agendaView" class="btn btn-default btn-sm">
            <span class="glyphicon glyphicon-book"></span>
            <fmt:message key="agenda" />
        </button>
        <button type="submit" name="monthView" class="btn btn-default btn-sm">
            <span class="glyphicon glyphicon-calendar"></span>
            <fmt:message key="month" />
        </button>
    </div>
    <div>
        <%
            Interval weekInterval = weekView.getWeekInterval();
            boolean todayDisabled = weekInterval.contains(new DateTime());
        %>
        <button type="submit" name="today" class="btn btn-default btn-sm"<%= todayDisabled ? " disabled='true'" : "" %>>
            <span class="glyphicon glyphicon-calendar"></span>
            <fmt:message key="current.week" />
        </button>
        <button type="submit" name="prevWeek" class="btn btn-default btn-sm">
            <em class="glyphicon glyphicon-chevron-left"></em>
            <fmt:message key="previous" />
        </button>
        <button type="submit" name="nextWeek" class="btn btn-default btn-sm">
            <fmt:message key="next" />
            <em class="glyphicon glyphicon-chevron-right"></em>
        </button>
    </div>
</div>
<div class="horizontalSeparator"></div>
<div class="calendar-container">
    <table class="week-table">
        <tr>
         <% for(int index = 0; index < 7; index++) {
            WeekView.WeekViewDay day = weekView.getDay(index);
            
            LocalDate dayOfWeek = weekView.getDay(index).getDayStart();
            xhtmlBuffer.openElement("th");
            if(day.getDayInterval().contains(new DateTime())) {
                xhtmlBuffer.addAttribute("class", "today");
            }
            xhtmlBuffer.write(dayOfWeekFormatter.print(dayOfWeek));
            xhtmlBuffer.closeElement("th");
         }
         %>
         </tr>
         <tr>
         <td>
         <table class="events-table">
           <% for(int index = 0; index < 7; index++) {
        	   WeekView.WeekViewDay day = weekView.getDay(index);
            	 for(Event event : day.getEvents()) {
	                xhtmlBuffer.openElement("tr");
	                
	                DateTime start = event.getInterval().getStart();
	                DateTime end = event.getInterval().getEnd();
	
	                writeEventCell(hhmmFormatter, xhtmlBuffer, day, event, start, end);
	                
	                xhtmlBuffer.closeElement("tr");
            	 }
            }
            %>
         </table>
         </td>
         </tr>
     </table>         

</div><%!private void writeEventCell(DateTimeFormatter hhmmFormatter, XhtmlBuffer xhtmlBuffer, WeekView.WeekViewDay day, Event event,
 DateTime start, DateTime end) {
	xhtmlBuffer.openElement("td");
	xhtmlBuffer.openElement("span");
	xhtmlBuffer.addAttribute("class", "event");
	
	String dialogId =
	    writeEventDialog(hhmmFormatter, xhtmlBuffer, day, event, start, end);
	
	xhtmlBuffer.openElement("a");
	xhtmlBuffer.addAttribute("style", "color: " + event.getCalendar().getForegroundHtmlColor() + ";");
	xhtmlBuffer.addAttribute("href", "#");
	xhtmlBuffer.addAttribute("data-target", "#" + dialogId);
	xhtmlBuffer.addAttribute("data-toggle", "modal");
	xhtmlBuffer.write(event.getDescription());
	xhtmlBuffer.closeElement("a");
	xhtmlBuffer.closeElement("span");
	xhtmlBuffer.closeElement("td");
}

private String writeEventDialog(DateTimeFormatter hhmmFormatter, XhtmlBuffer xhtmlBuffer, WeekView.WeekViewDay day, Event event,
 DateTime start, DateTime end) {
		String dialogId = "event-dialog-" + event.getId() + "-" + day.getDayInterval().getStartMillis();

		PresentationHelper.openDialog(xhtmlBuffer, dialogId, null);

		// modal-header
		xhtmlBuffer.openElement("div");
		xhtmlBuffer.addAttribute("class", "modal-header");

		PresentationHelper.writeDialogCloseButtonInHeader(xhtmlBuffer);

		xhtmlBuffer.openElement("h1");
		xhtmlBuffer.addAttribute("class", "modal-title");
		if (event.getReadUrl() != null) {
			xhtmlBuffer.writeAnchor(event.getReadUrl(), event.getDescription());
		} else {
			xhtmlBuffer.write(event.getDescription());
		}
		xhtmlBuffer.closeElement("h1");
		xhtmlBuffer.closeElement("div"); // modal-header

		// modal-body
		xhtmlBuffer.openElement("div");
		xhtmlBuffer.addAttribute("class", "modal-body");

		xhtmlBuffer.openElement("p");
		String timeDescription;
		DateTimeFormatter startFormatter = makeEventDateTimeFormatter(start, hhmmFormatter.getLocale());
		timeDescription = startFormatter.print(start);
		if (end.minus(1).getDayOfYear() != start.getDayOfYear()) {
			DateTime formatEnd = end;
			if (formatEnd.getMillisOfDay() == 0) {
				formatEnd = formatEnd.minusDays(1);
			}
			DateTimeFormatter endFormatter = makeEventDateTimeFormatter(formatEnd, hhmmFormatter.getLocale());
			timeDescription += " - " + endFormatter.print(formatEnd);
		} else if (end.getMillisOfDay() != start.getMillisOfDay()) {
			timeDescription += " - " + hhmmFormatter.print(end);
		}
		xhtmlBuffer.write(timeDescription);
		xhtmlBuffer.closeElement("p");
		if (event.getEditUrl() != null) {
			xhtmlBuffer.openElement("p");
			String editText = ElementsThreadLocals.getText("edit");
			xhtmlBuffer.writeAnchor(event.getEditUrl(), editText);
			xhtmlBuffer.closeElement("p");
		}
		xhtmlBuffer.closeElement("div"); // modal-body

		// modal-footer
		xhtmlBuffer.openElement("div");
		xhtmlBuffer.addAttribute("class", "modal-footer");
		PresentationHelper.writeDialogCloseButtonInFooter(xhtmlBuffer);
		xhtmlBuffer.closeElement("div"); // modal-footer

		PresentationHelper.closeDialog(xhtmlBuffer);
		return dialogId;
	}

	private DateTimeFormatter makeEventDateTimeFormatter(DateTime start, Locale locale) {
		DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder().appendDayOfWeekShortText().appendLiteral(", ")
				.appendDayOfMonth(1).appendLiteral(" ").appendMonthOfYearText();
		if (start.getSecondOfDay() > 0) {
			builder.appendLiteral(", ").appendHourOfDay(2).appendLiteral(":").appendMinuteOfHour(2);
		}
		return builder.toFormatter().withLocale(locale);
	}%>
