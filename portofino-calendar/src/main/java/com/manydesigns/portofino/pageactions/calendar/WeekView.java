/*
 * Copyright (C) 2005-2017 ManyDesigns srl.  All rights reserved.
 * http://www.manydesigns.com/
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package com.manydesigns.portofino.pageactions.calendar;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalDate;

import com.manydesigns.portofino.calendar.AbstractDay;
import com.manydesigns.portofino.calendar.AbstractWeekView;

/**
 * @author Paolo Predonzani - paolo.predonzani@manydesigns.com
 * @author Angelo Lupo - angelo.lupo@manydesigns.com
 * @author Giampiero Granatella - giampiero.granatella@manydesigns.com
 * @author Alessio Stalla - alessio.stalla@manydesigns.com
 * @author Sylvie Denarie - sylvie.denarie@denarie.de
 */
public class WeekView extends AbstractWeekView<WeekView.WeekViewDay> {
	public static final String copyright = "Copyright (C) 2005-2017 ManyDesigns srl";

	// --------------------------------------------------------------------------
	// Constructors and builder overrides
	// --------------------------------------------------------------------------

	public WeekView(DateTime referenceDateTime) {
		super(referenceDateTime);
	}

	public WeekView(DateTime referenceDateTime, int firstDayOfWeek) {
		super(referenceDateTime, firstDayOfWeek);
	}

	@Override
	protected WeekViewDay[] createDaysArray(int size) {
		return new WeekViewDay[size];
	}

	@Override
	protected WeekViewDay createDay(LocalDate dayStart, LocalDate dayEnd) {
		return new WeekViewDay(dayStart, dayEnd);
	}

	// --------------------------------------------------------------------------
	// Events
	// --------------------------------------------------------------------------

	public int addEvent(Event event) {
		Interval weekViewOverlap = weekInterval.overlap(event.getInterval());
		if (weekViewOverlap == null) {
			logger.debug("Event not overlapping with week view");
			return 0;
		} else {
			logger.debug("Event overlapping with week view");
			int added = 0;
			for (WeekViewDay day : days) {
				if (day.addEvent(event)) {
					added++;
				}
			}
			return added;
		}
	}

	public void clearEvents() {
		logger.debug("Clearing events");
		for (WeekViewDay day : days) {
			day.clearEvents();
		}
	}

	public void sortEvents() {
		logger.debug("Sorting events");
		for (WeekViewDay day : days) {
			day.sortEvents();
		}
	}

	// --------------------------------------------------------------------------
	// Accessory classes
	// --------------------------------------------------------------------------

	public class WeekViewDay extends AbstractDay {
		final List<Event> events = new LinkedList<Event>();

		public WeekViewDay(LocalDate dayStart, LocalDate dayEnd) {
			super(dayStart, dayEnd);
		}

		public void sortEvents() {
			Collections.sort(events, new Comparator<Event>() {
				public int compare(Event o1, Event o2) {
					return o1.getInterval().getStart().compareTo(o2.getInterval().getStart());
				}
			});
		}

		public void clearEvents() {
			events.clear();
		}

		public boolean addEvent(Event event) {
			Interval dayOverlap = dayInterval.overlap(event.getInterval());
			if (dayOverlap == null) {
				logger.debug("Event not overlapping with day");
				return false;
			} else {
				logger.debug("Event overlapping with day");
				events.add(event);
				return true;
			}
		}
	    public List<Event> getEvents() {
	        return events;
	    }

	}

}
