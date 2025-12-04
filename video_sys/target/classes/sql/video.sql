
CREATE TABLE `video`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `video_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '视频类型',
  `video_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '视频名称',
  `video_publisher` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '视频发布者',
  `video_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '视频路径',
  `duration` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '视频时长',
  `video_describe` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '视频描述',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of video
-- ----------------------------
INSERT INTO `video` VALUES (1, 'computer', 'There are many variations of passages of Lorem Ipsum available', 'Education', '/uploads/videos/video1.mp4', '3:00', '这是一个精彩的视频内容。Lorem Ipsum是印刷和排版行业的虚拟文本。自16世纪以来,Lorem Ipsum一直是行业的标准虚拟文本,当时一位不知名的印刷商拿了一个类型的厨房并把它打乱以制作一本类型样本书。它不仅经历了五个世纪,而且还经历了电子排版的飞跃,基本上保持不变。它在20世纪60年代随着包含Lorem Ipsum段落的Letraset表的发布而流行,最近随着Aldus PageMaker等桌面出版软件(包括Lorem Ipsum的版本)而流行。', '2025-11-23 16:18:48');
INSERT INTO `video` VALUES (2, 'photography', 'Lorem Ipsum is simply dummy text', 'Education', '/uploads/videos/video1.mp4', '3:50', '这是一个精彩的视频内容。Lorem Ipsum是印刷和排版行业的虚拟文本。自16世纪以来,Lorem Ipsum一直是行业的标准虚拟文本,当时一位不知名的印刷商拿了一个类型的厨房并把它打乱以制作一本类型样本书。它不仅经历了五个世纪,而且还经历了电子排版的飞跃,基本上保持不变。它在20世纪60年代随着包含Lorem Ipsum段落的Letraset表的发布而流行,最近随着Aldus PageMaker等桌面出版软件(包括Lorem Ipsum的版本)而流行。', '2025-11-23 16:20:17');
INSERT INTO `video` VALUES (3, 'clothes', '10年收官之作，Demna在Balenciaga第一次谢幕了', 'Industry', '/uploads/videos/video1.mp4', '0:10', '结构感强烈的美第奇式与诺斯费拉图式领口也被“嫁接”在有史以来最轻盈的风衣与针织衫上。而所谓“轻盈”，即是Demna想要在高定世界中挑战的又一准则。通常来说，高定的繁复工艺和重工装饰，无疑会使演绎造型的过程成为一种负担。但这一次，他打造出了史上最轻的科技感丝质飞行员夹克，适合夏季时节的塔夫绸材质的“商务”机车夹克，以及无侧缝的羽绒服与羊绒-骆马毛混纺长大衣等非常具有日装实用性的高定作品。', '2025-11-23 16:38:00');
INSERT INTO `video` VALUES (4, 'mobile', '苹果 iPhone 17e 曝料：配 A19、N1 芯片、主打出色续航', '苹果', '/uploads/videos/video1.mp4', '0:50', '在外观设计和影像系统方面，预计 iPhone 17e 将延续 iPhone 16e 的设计语言，并升级配备 1800 万像素的前置单摄像头。至于售价，分析师认为苹果大概率会维持 599 美元的定价策略，确保其在入门市场的价格优势。', '2025-11-23 16:39:56');

