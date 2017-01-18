package ru.javawebinar.topjava.service;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by USER on 18.01.2017, 1:51.
 *
 * @author Vadim Gamaliev <a href="mailto:gamaliev-vadim@yandex.com">gamaliev-vadim@yandex.com</a>
 * @version 1.0
 */
public class TimerRule implements TestRule {
    private static Map<String, Long> result = new HashMap<>();
    private int ruleType;

    public TimerRule(int ruleType) {
        this.ruleType = ruleType;
    }

    @Override
    public Statement apply(Statement base, Description description) {
        if (ruleType == 0) {
            return new Statement() {
                @Override
                public void evaluate() throws Throwable {
                    long start = System.nanoTime();
                    base.evaluate();
                    long end = System.nanoTime();
                    long finish = end - start;
                    System.out.println("Time: " + finish / 1_000_000 + "ms.");
                    TimerRule.result.put(description.getMethodName(), finish);
                }
            };
        } else if (ruleType == 1) {
            return new Statement() {
                @Override
                public void evaluate() throws Throwable {
                    base.evaluate();
                    final long[] count = {0};
                    System.out.println("\n==========================================\n");
                    TimerRule.result.forEach((s, aLong) -> {
                        System.out.println(String.format("%-30s : %-5d %s", s, aLong / 1_000_000, "ms."));
                        count[0] += aLong;
                    });
                    System.out.println(String.format("\n%-30s : %-5d %s", "In total", count[0] / 1_000_000, "ms."));
                    System.out.println("\n==========================================\n");
                }
            };
        }

        return null;
    }
}
