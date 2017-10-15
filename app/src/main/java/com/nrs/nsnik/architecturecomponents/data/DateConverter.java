package com.nrs.nsnik.architecturecomponents.data;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

class DateConverter {

    @TypeConverter
    public static Date longToDate(Long date){
        return date == null ? null : new Date(date);
    }

    @TypeConverter
    public static Long dateToLong(Date date){
        return date == null ? null : date.getTime();
    }

}
