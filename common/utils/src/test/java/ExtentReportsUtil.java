import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.ExtentSparkReporterConfig;
import com.aventstack.extentreports.reporter.configuration.ViewName;

import cn.hutool.core.date.DatePattern;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExtentReportsUtil {
    public static Map<String, ExtentReports> extentMap = new HashMap<>();

    public static ExtentTest classTest;

    public static ExtentTest methodTest;

    public static ExtentReports extent;

    public static ExtentTest initTest;

    public static void init(String name) {
        if (!extentMap.containsKey(name)) {
            final String path = "build/test-output/" + name + ".html";
            final File file = new File(path);
            extent = new ExtentReports();
            ExtentSparkReporter spark =
                new ExtentSparkReporter(path)
                    .config(
                        ExtentSparkReporterConfig.builder()
                            .offlineMode(true)
                            .documentTitle(name)
                            .timeStampFormat(DatePattern.NORM_DATETIME_MS_PATTERN)
                            .build())
                    .viewConfigurer()
                    .viewOrder()
                    .as(
                        new ViewName[] {
                            ViewName.AUTHOR,
                            ViewName.CATEGORY,
                            ViewName.DASHBOARD,
                            ViewName.DEVICE,
                            ViewName.EXCEPTION,
                            ViewName.LOG,
                            ViewName.TEST
                        })
                    .apply();
            extent.attachReporter(spark);
            extentMap.put(name, extent);
            log.info("日志路径:{}", file.getAbsolutePath());
        } else {
            extent = extentMap.get(name);
        }
    }
}


