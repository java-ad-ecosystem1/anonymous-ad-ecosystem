
CREATE TABLE `product`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `product_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '产品类型',
  `product_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '产品名称',
  `describe` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '产品介绍',
  `img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '图片路径',
  `product_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '产品价格',
  `product_count` int NULL DEFAULT NULL COMMENT '产品数量',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

INSERT INTO `product` VALUES (1, 'mobile', '无线蓝牙耳机', '高品质无线蓝牙耳机,采用最新蓝牙5.3技术,提供卓越的音质体验。主动降噪功能让您专注于音乐,无论是通勤还是运动都是完美选择。', '/uploads/images/img.png', 299.00, 50, '2025-11-22 20:22:53');
INSERT INTO `product` VALUES (2, 'computer', '超薄笔记本电脑', '超薄设计，高性能处理器，适合办公和娱乐使用。', '/uploads/images/img_1.png', 5999.00, 30, '2025-11-22 20:28:39');
INSERT INTO `product` VALUES (3, 'mobile', '智能手表', '智能健康监测，运动追踪，支持多种运动模式。', '/uploads/images/img_2.png', 1299.00, 80, '2025-11-22 20:29:48');
INSERT INTO `product` VALUES (4, 'photography', '专业相机镜头', '专业级相机镜头，高清晰度，适合专业摄影师使用。', '/uploads/images/img_3.png', 3899.00, 15, '2025-11-22 20:30:58');
INSERT INTO `product` VALUES (5, 'mobile', '5G智能手机', 'G网络支持，高性能处理器，拍照功能强大。', '/uploads/images/img_5.png', 4299.00, 40, '2025-11-22 20:32:14');
INSERT INTO `product` VALUES (6, 'clothes', '全棉短袖', '吸湿排汗 穿着舒适 穿着挺阔 抗皱耐磨', '/uploads/images/img_4.png', 299.00, 20, '2025-11-22 20:37:05');