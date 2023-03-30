import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

public class DefaultTestWatcher implements TestWatcher {

    private ExtentReports extent;

    @Override
    public void testSuccessful(ExtensionContext context) {
        ExtentReportsUtil.methodTest.pass("测试成功");
        ExtentReportsUtil.extent.flush();
        ExtentReportsUtil.methodTest = null;
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable e) {
        ExtentReportsUtil.methodTest.fail(e);
        ExtentReportsUtil.extent.flush();
        ExtentReportsUtil.methodTest = null;
    }

}



