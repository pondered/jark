CREATE TABLE user_info (
    id          BIGINT       NOT NULL,
    username    VARCHAR(32)  NOT NULL DEFAULT '' COMMENT '用户名',
    password    VARCHAR(256) NOT NULL DEFAULT '' COMMENT '密码',
    phone       VARCHAR(16)  NOT NULL DEFAULT '' COMMENT '手机号码',
    deleted     TINYINT(2)   NOT NULL DEFAULT 0 COMMENT '删除1,未删除0',
    create_time DATETIME(3)  NOT NULL DEFAULT NOW() COMMENT '创建时间',
    update_time DATETIME(3)  NOT NULL DEFAULT NOW() ON UPDATE NOW() COMMENT '更新时间',
    PRIMARY KEY (id)
) COMMENT '用户信息表';