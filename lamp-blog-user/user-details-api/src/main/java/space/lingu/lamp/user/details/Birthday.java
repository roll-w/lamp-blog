/*
 * Copyright (C) 2023 RollW
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package space.lingu.lamp.user.details;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * @author RollW
 */
public class Birthday implements Serializable, Comparable<Birthday> {
    public static final Birthday UNKNOWN = new Birthday(0, 0, 0);

    private final int year;
    private final int month;
    private final int day;

    public Birthday(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public LocalDate toDate() {
        return LocalDate.of(year, month, day);
    }

    public String getBirthday() {
        // pattern: yyyyMMdd
        return String.format("%04d%02d%02d", year, month, day);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Birthday birthday = (Birthday) o;
        return year == birthday.year && month == birthday.month && day == birthday.day;
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, month, day);
    }

    @Override
    public String toString() {
        return getBirthday();
    }

    public boolean isTheDay(LocalDate date) {
        return month == date.getMonthValue() && day == date.getDayOfMonth();
    }

    public boolean isTheDay(int month, int day) {
        return this.month == month && this.day == day;
    }

    public static Birthday fromString(String birthday) {
        if (birthday == null || birthday.isEmpty()) {
            return null;
        }
        // pattern: yyyyMMdd
        int year = Integer.parseInt(birthday.substring(0, 4));
        int month = Integer.parseInt(birthday.substring(4, 6));
        int day = Integer.parseInt(birthday.substring(6, 8));
        return new Birthday(year, month, day);
    }

    public static Birthday fromLocalDate(LocalDate date) {
        if (date == null) {
            return null;
        }

        return new Birthday(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
    }

    @Override
    public int compareTo(Birthday o) {
        if (year != o.year) {
            return year - o.year;
        }
        if (month != o.month) {
            return month - o.month;
        }
        return day - o.day;
    }
}
