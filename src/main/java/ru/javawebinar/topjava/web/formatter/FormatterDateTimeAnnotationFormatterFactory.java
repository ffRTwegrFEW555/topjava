package ru.javawebinar.topjava.web.formatter;

import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;
import ru.javawebinar.topjava.util.DateTimeUtil;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.Temporal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Created by USER on 08.02.2017, 0:54.
 *
 * @author Vadim Gamaliev <a href="mailto:gamaliev-vadim@yandex.com">gamaliev-vadim@yandex.com</a>
 * @version 1.0
 */
public class FormatterDateTimeAnnotationFormatterFactory implements AnnotationFormatterFactory<FormatterDateTime> {

    private static final Set<Class<?>> FIELD_TYPES;

    static {
        Set<Class<?>> fieldTypes = new HashSet<>();
        fieldTypes.add(LocalTime.class);
        fieldTypes.add(LocalDate.class);
        FIELD_TYPES = Collections.unmodifiableSet(fieldTypes);
    }

    @Override
    public Set<Class<?>> getFieldTypes() {
        return FIELD_TYPES;
    }

    @Override
    public Printer<?> getPrinter(FormatterDateTime annotation, Class<?> fieldType) {
        return null;
    }

    @Override
    public Parser<?> getParser(FormatterDateTime annotation, Class<?> fieldType) {
        return new FormatterDateTimeClass();
    }

    private static class FormatterDateTimeClass implements Formatter<Temporal> {

        @Override
        public Temporal parse(String text, Locale locale) throws ParseException {
            if (text.matches("[\\d]{4}-[\\d]{2}-[\\d]{2}")) {
                return DateTimeUtil.parseLocalDate(text);
            } else if (text.matches("[\\d]{2}:[\\d]{2}")) {
                return DateTimeUtil.parseLocalTime(text);
            } else {
                throw new ParseException("Expected pattern for LocalDate: yyyy-MM-dd, and for LocalTime: HH:mm", 0);
            }
        }

        @Override
        public String print(Temporal object, Locale locale) {
            return null;
        }
    }
}
