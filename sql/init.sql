-- ============================================================
-- AI 智能食材管理系统 - 数据库初始化脚本
-- 适用数据库：MySQL 8.0+
-- ============================================================
CREATE DATABASE IF NOT EXISTS food_control DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE food_control;

-- -----------------------------------------------------------
-- 1. 用户表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS user;
CREATE TABLE user (
    id              BIGINT       AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    username        VARCHAR(50)  NOT NULL UNIQUE              COMMENT '用户名',
    password        VARCHAR(255) NOT NULL                      COMMENT '密码',
    real_name       VARCHAR(50)  DEFAULT NULL                  COMMENT '真实姓名',
    phone           VARCHAR(20)  DEFAULT NULL                  COMMENT '手机号',
    email           VARCHAR(100) DEFAULT NULL                  COMMENT '邮箱',
    role            TINYINT      NOT NULL DEFAULT 0            COMMENT '角色 0-普通商户 1-管理员',
    avatar          VARCHAR(500) DEFAULT NULL                  COMMENT '头像URL',
    status          TINYINT      NOT NULL DEFAULT 1            COMMENT '状态 0-禁用 1-启用',
    create_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_role (role),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- -----------------------------------------------------------
-- 2. 食材库存表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS food_stock;
CREATE TABLE food_stock (
    id              BIGINT       AUTO_INCREMENT PRIMARY KEY COMMENT '食材ID',
    user_id         BIGINT       NOT NULL                      COMMENT '所属用户',
    name            VARCHAR(100) NOT NULL                      COMMENT '食材名称',
    category        VARCHAR(50)  NOT NULL                      COMMENT '分类(蔬菜/肉类/水产/调味品/粮油/其他)',
    stock_quantity  DECIMAL(10,2) NOT NULL DEFAULT 0.00        COMMENT '库存数量',
    unit            VARCHAR(20)  NOT NULL DEFAULT '斤'          COMMENT '单位',
    purchase_price  DECIMAL(10,2) DEFAULT NULL                 COMMENT '采购单价(元)',
    supplier_id     BIGINT       DEFAULT NULL                  COMMENT '供应商ID',
    expiry_date     DATE         DEFAULT NULL                  COMMENT '保质期截止日',
    warning_threshold DECIMAL(10,2) NOT NULL DEFAULT 10.00     COMMENT '预警阈值',
    storage_location VARCHAR(100) DEFAULT NULL                 COMMENT '存放位置',
    remark          VARCHAR(500) DEFAULT NULL                  COMMENT '备注',
    status          TINYINT      NOT NULL DEFAULT 1            COMMENT '状态 0-已下架 1-在库',
    create_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user (user_id),
    INDEX idx_category (category),
    INDEX idx_expiry (expiry_date),
    INDEX idx_warning (warning_threshold)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='食材库存表';

-- -----------------------------------------------------------
-- 3. 采购订单表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS purchase_order;
CREATE TABLE purchase_order (
    id              BIGINT       AUTO_INCREMENT PRIMARY KEY COMMENT '订单ID',
    user_id         BIGINT       NOT NULL                      COMMENT '采购人',
    order_no        VARCHAR(50)  NOT NULL UNIQUE               COMMENT '订单编号',
    supplier_id     BIGINT       DEFAULT NULL                  COMMENT '供应商ID',
    total_amount    DECIMAL(12,2) DEFAULT 0.00                 COMMENT '总金额(元)',
    status          TINYINT      NOT NULL DEFAULT 0            COMMENT '状态 0-待审核 1-已通过 2-已到货 3-已取消',
    order_date      DATE         NOT NULL                      COMMENT '下单日期',
    delivery_date   DATE         DEFAULT NULL                  COMMENT '预计到货日',
    remark          VARCHAR(500) DEFAULT NULL                  COMMENT '备注',
    create_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user (user_id),
    INDEX idx_status (status),
    INDEX idx_order_date (order_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购订单表';

-- -----------------------------------------------------------
-- 4. 采购订单明细表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS purchase_order_item;
CREATE TABLE purchase_order_item (
    id              BIGINT       AUTO_INCREMENT PRIMARY KEY,
    order_id        BIGINT       NOT NULL                      COMMENT '订单ID',
    food_name       VARCHAR(100) NOT NULL                      COMMENT '食材名称',
    quantity        DECIMAL(10,2) NOT NULL                     COMMENT '采购数量',
    unit            VARCHAR(20)  NOT NULL DEFAULT '斤'          COMMENT '单位',
    unit_price      DECIMAL(10,2) NOT NULL                     COMMENT '单价',
    subtotal        DECIMAL(12,2) NOT NULL                     COMMENT '小计',
    INDEX idx_order (order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购订单明细表';

-- -----------------------------------------------------------
-- 5. 供应商表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS supplier;
CREATE TABLE supplier (
    id              BIGINT       AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(100) NOT NULL                      COMMENT '供应商名称',
    contact_person  VARCHAR(50)  DEFAULT NULL                  COMMENT '联系人',
    phone           VARCHAR(20)  DEFAULT NULL                  COMMENT '联系电话',
    address         VARCHAR(300) DEFAULT NULL                  COMMENT '地址',
    remark          VARCHAR(500) DEFAULT NULL,
    status          TINYINT      NOT NULL DEFAULT 1            COMMENT '状态 0-禁用 1-启用',
    create_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='供应商表';

-- -----------------------------------------------------------
-- 6. 食材损耗表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS food_loss_data;
CREATE TABLE food_loss_data (
    id              BIGINT       AUTO_INCREMENT PRIMARY KEY,
    user_id         BIGINT       NOT NULL                      COMMENT '所属用户',
    food_id         BIGINT       DEFAULT NULL                  COMMENT '关联食材ID',
    food_name       VARCHAR(100) NOT NULL                      COMMENT '食材名称',
    loss_quantity   DECIMAL(10,2) NOT NULL                     COMMENT '损耗数量',
    unit            VARCHAR(20)  NOT NULL DEFAULT '斤'          COMMENT '单位',
    loss_reason     VARCHAR(200) NOT NULL                      COMMENT '损耗原因(过期/破损/变质/其他)',
    loss_amount     DECIMAL(10,2) DEFAULT 0.00                 COMMENT '损耗金额(元)',
    record_date     DATE         NOT NULL                      COMMENT '记录日期',
    operator        VARCHAR(50)  DEFAULT NULL                  COMMENT '操作人',
    remark          VARCHAR(500) DEFAULT NULL,
    create_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user (user_id),
    INDEX idx_food (food_id),
    INDEX idx_date (record_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='食材损耗表';

-- -----------------------------------------------------------
-- 7. AI 对话记录表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS ai_chat_record;
CREATE TABLE ai_chat_record (
    id              BIGINT       AUTO_INCREMENT PRIMARY KEY,
    user_id         BIGINT       NOT NULL                      COMMENT '用户ID',
    session_id      VARCHAR(64)  DEFAULT NULL                  COMMENT '会话ID(支持多轮)',
    role            VARCHAR(20)  NOT NULL DEFAULT 'user'       COMMENT '角色 user/assistant',
    content         TEXT         NOT NULL                      COMMENT '对话内容',
    model_type      VARCHAR(50)  DEFAULT 'coze'                COMMENT '模型类型 coze/volc/deepseek',
    create_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user (user_id),
    INDEX idx_session (session_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI对话记录表';

-- -----------------------------------------------------------
-- 8. AI 生成资源表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS ai_generate_resource;
CREATE TABLE ai_generate_resource (
    id              BIGINT       AUTO_INCREMENT PRIMARY KEY,
    user_id         BIGINT       NOT NULL                      COMMENT '用户ID',
    title           VARCHAR(200) DEFAULT NULL                  COMMENT '标题',
    content         TEXT         DEFAULT NULL                  COMMENT '生成文章内容',
    image_url       VARCHAR(500) DEFAULT NULL                  COMMENT '图片URL',
    generate_type   VARCHAR(50)  NOT NULL                      COMMENT '生成类型 recipe/poster/report/analysis',
    status          TINYINT      DEFAULT 0                     COMMENT '状态 0-生成中 1-已完成 2-失败',
    model_info      VARCHAR(200) DEFAULT NULL                  COMMENT '模型信息',
    create_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user (user_id),
    INDEX idx_type (generate_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI生成资源表';

-- -----------------------------------------------------------
-- 9. AI 食材分析报表表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS ai_analysis_report;
CREATE TABLE ai_analysis_report (
    id              BIGINT       AUTO_INCREMENT PRIMARY KEY,
    user_id         BIGINT       NOT NULL                      COMMENT '用户ID',
    report_title    VARCHAR(200) NOT NULL                      COMMENT '报表标题',
    report_type     VARCHAR(50)  NOT NULL                      COMMENT '报表类型 loss_trend/cost_analysis/warning/stock_forecast',
    report_data     JSON         DEFAULT NULL                  COMMENT '报表数据(JSON格式)',
    summary         TEXT         DEFAULT NULL                  COMMENT 'AI总结建议',
    chart_config    JSON         DEFAULT NULL                  COMMENT '图表配置(ECharts)',
    period_start    DATE         DEFAULT NULL                  COMMENT '分析起始日期',
    period_end      DATE         DEFAULT NULL                  COMMENT '分析截止日期',
    create_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user (user_id),
    INDEX idx_type (report_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI食材分析报表表';

-- -----------------------------------------------------------
-- 插入默认管理员账号 (密码: admin123)
-- -----------------------------------------------------------
INSERT INTO user (username, password, real_name, role, status)
VALUES ('admin', 'admin123', '系统管理员', 1, 1);