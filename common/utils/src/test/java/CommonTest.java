import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.RegisterExtension;

@Slf4j

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CommonTest {
    @RegisterExtension
    public DefaultTestWatcher testWatcher = new DefaultTestWatcher();

    @BeforeAll
    public void init(TestInfo testInfo) {
//        System.out.println(testInfo.getTestClass().get().getResource("").getPath());
        ExtentReportsUtil.init(
            testInfo.getTags().stream().findFirst().orElseGet(() -> System.currentTimeMillis() + ""));
        ExtentReportsUtil.classTest = ExtentReportsUtil.extent.createTest(testInfo.getDisplayName());
        ExtentReportsUtil.initTest = ExtentReportsUtil.classTest.createNode("测试初始化");
        log.info("[{}]类开始测试", testInfo.getDisplayName());
    }

    @BeforeEach
    public void setUp(TestInfo testInfo) {
        ExtentReportsUtil.methodTest =
            ExtentReportsUtil.classTest.createNode(testInfo.getDisplayName());
        ExtentReportsUtil.initTest = null;
        log.info("[{}]方法开始测试", testInfo.getDisplayName());
    }

    @AfterEach
    public void tearDown(TestInfo testInfo) {
        log.info("[{}]方法结束测试", testInfo.getDisplayName());
    }

    @AfterAll
    public void done(TestInfo testInfo) {
        log.info("[{}]类结束测试", testInfo.getDisplayName());
        ExtentReportsUtil.extent.flush();
        ExtentReportsUtil.initTest = ExtentReportsUtil.classTest.createNode("测试结束");
    }
}


