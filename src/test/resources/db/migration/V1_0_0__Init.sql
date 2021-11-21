DROP TABLE IF EXISTS `region`;
CREATE TABLE `region`
(
    id            BIGINT PRIMARY KEY AUTO_INCREMENT,
    code          VARCHAR(32)  NOT NULL COMMENT '编码',
    name          VARCHAR(32)  NOT NULL COMMENT '名称',
    remark        VARCHAR(255) NOT NULL DEFAULT '' COMMENT '备注',
    parent_id     BIGINT       NOT NULL COMMENT '父节点',
    level         TINYINT      NOT NULL COMMENT '层级',
    leaf          BIT          NOT NULL COMMENT '叶子节点',
    serial_number SMALLINT     NOT NULL COMMENT '序号',
    creator_id    BIGINT       NOT NULL COMMENT '创建者主键',
    created_time  DATETIME     NOT NULL COMMENT '创建时间',
    modifier_id   BIGINT       NOT NULL COMMENT '修改者主键',
    modified_time DATETIME     NOT NULL COMMENT '最近修改时间'
);


insert into region (id, code, name, remark, parent_id, level, leaf, serial_number, creator_id, created_time,modifier_id,modified_time)values (1, 'world', '世界', '', 0, 1, 1, 1, 1, current_timestamp, 1, current_timestamp);
