package com.jark.template.common.mybatis.mapper;


import org.apache.ibatis.session.SqlSessionFactory;

public class LimitAndOffsetTest {

    private static final String JDBC_URL = "jdbc:hsqldb:mem:aname";

    private static final String JDBC_DRIVER = "org.hsqldb.jdbcDriver";

    private SqlSessionFactory sqlSessionFactory;

    //    @BeforeEach
    //    void setup() throws Exception {
    //        Class.forName(JDBC_DRIVER);
    //        InputStream is = getClass().getResourceAsStream("/examples/animal/data/CreateAnimalData.sql");
    //        try (Connection connection = DriverManager.getConnection(JDBC_URL, "sa", "")) {
    //            ScriptRunner sr = new ScriptRunner(connection);
    //            sr.setLogWriter(null);
    //            sr.runScript(new InputStreamReader(is));
    //        }
    //
    //        UnpooledDataSource ds = new UnpooledDataSource(JDBC_DRIVER, JDBC_URL, "sa", "");
    //        Environment environment = new Environment("test", new JdbcTransactionFactory(), ds);
    //        Configuration config = new Configuration(environment);
    //        config.addMapper(LimitAndOffsetMapper.class);
    //        sqlSessionFactory = new SqlSessionFactoryBuilder().build(config);
    //    }
    //
    //    @Test
    //    void testLimitAndOffset() {
    //        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
    //            final BaseMapper mapper = sqlSession.getMapper(BaseMapper.class);
    //
    //            List<Object> rows = mapper.selectWithLimitAndOffset(5, 3)
    //                .orderBy(id)
    //                .build()
    //                .execute();
    //
    //            assertThat(rows).hasSize(5);
    //            assertThat(rows.get(0).getId()).isEqualTo(4);
    //        }
    //    }
}