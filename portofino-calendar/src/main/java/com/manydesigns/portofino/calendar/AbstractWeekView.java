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

package com.manydesigns.portofino.calendar;

import org.jetbrains.annotations.NotNull;
import org.joda.time.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Paolo Predonzani     - paolo.predonzani@manydesigns.com
 * @author Angelo Lupo          - angelo.lupo@manydesigns.com
 * @author Giampiero Granatella - giampiero.granatella@manydesigns.com
 * @author Alessio Stalla       - alessio.stalla@manydesigns.com
 * @author Sylvie Denarie       - sylvie.denarie@denarie.de
 */
public abstract class AbstractWeekView<T extends AbstractDay> {
    public static final String copyright =
            "Copyright (C) 2005-2017 ManyDesigns srl";

    //--------------------------------------------------------------------------
    // Fields
    //--------------------------------------------------------------------------

    final DateTime  referenceDateTime;
    final int       firstDayOfWeek;
    final LocalDate referenceDateMidnight;
    final int       referenceYear;
    final int       referenceMonth;

    final LocalDate weekStart;
    final LocalDate weekEnd;
    protected final Interval  weekInterval;

    protected final T[] days;

    //--------------------------------------------------------------------------
    // Logging
    //--------------------------------------------------------------------------

    public static final Logger logger =
            LoggerFactory.getLogger(AbstractWeekView.class);

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    public AbstractWeekView(DateTime referenceDateTime) {
        this(referenceDateTime, DateTimeConstants.MONDAY);
    }

    public AbstractWeekView(DateTime referenceDateTime, int firstDayOfWeek) {
        logger.debug("Initializing week");
        this.referenceDateTime = referenceDateTime;
        logger.debug("Reference date time: {}", referenceDateTime);
        this.firstDayOfWeek = firstDayOfWeek;
        logger.debug("First day of week: {}", firstDayOfWeek);

        referenceDateMidnight = new LocalDate(referenceDateTime);
        referenceYear = referenceDateTime.getYear();
        referenceMonth = referenceDateTime.getMonthOfYear();

        weekStart = referenceDateMidnight.withDayOfWeek(DateTimeConstants.MONDAY); // Week beginning with Monday
        weekEnd = weekStart.plusWeeks(1);
        weekInterval = new Interval(weekStart.toDateTimeAtStartOfDay(), weekEnd.toDateTimeAtStartOfDay());

        logger.debug("Initializing days");
        days = createDaysArray(7);
        LocalDate dayStart = weekStart;
        for (int i = 0; i < days.length; i++) {
            LocalDate dayEnd = dayStart.plusDays(1);
            days[i] = createDay(dayStart, dayEnd);

            dayStart = dayEnd;
        }
    }

    protected abstract T[] createDaysArray(int size);

    protected abstract T createDay(LocalDate dayStart, LocalDate dayEnd);

    public T findDayByDateTime(@NotNull DateTime dateTime) {
        for (T current : days) {
            if (current.getDayInterval().contains(dateTime)) {
                return current;
            }
        }
        throw new InternalError("DateTime in month but not in week's days: " + dateTime);
    }

    //--------------------------------------------------------------------------
    // Getters/setters
    //--------------------------------------------------------------------------


    public LocalDate getReferenceDateMidnight() {
        return referenceDateMidnight;
    }

    public LocalDate getWeekStart() {
        return weekStart;
    }

    public LocalDate getWeekEnd() {
        return weekEnd;
    }

    public Interval getWeekInterval() {
        return weekInterval;
    }

    public T getDay(int index) {
        return days[index];
    }

    public DateTime getReferenceDateTime() {
        return referenceDateTime;
    }

    public int getFirstDayOfWeek() {
        return firstDayOfWeek;
    }

}
