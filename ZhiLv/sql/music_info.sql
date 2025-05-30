-- 创建音乐信息表（存储音乐基本信息和文件路径）
CREATE TABLE `music_info` (
  `music_id`        BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '音乐ID',
  `title`           VARCHAR(100) NOT NULL COMMENT '音乐标题',
  `artist`          VARCHAR(50)  NOT NULL DEFAULT '未知' COMMENT '艺术家',
  `album`           VARCHAR(100)          DEFAULT '' COMMENT '专辑名称',
  `duration`        INT                   DEFAULT 0 COMMENT '时长（秒）',
  `file_url`        VARCHAR(500) NOT NULL COMMENT '文件存储路径（OSS或本地）',
  `cover_url`       VARCHAR(500)          DEFAULT '' COMMENT '封面图片路径',
  `upload_user_id`  BIGINT       NOT NULL COMMENT '上传用户ID（关联sys_user.user_id）',
  `upload_time`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
  `play_count`      INT          NOT NULL DEFAULT 0 COMMENT '播放次数',
  `status`          CHAR(1)      NOT NULL DEFAULT '0' COMMENT '状态（0正常 1删除 2审核）',
  `tags`            VARCHAR(200)          DEFAULT '' COMMENT '标签（逗号分隔，如流行,电子）'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='音乐信息表';

-- 索引优化
ALTER TABLE `music_info` ADD INDEX `idx_title` (`title`);
ALTER TABLE `music_info` ADD INDEX `idx_artist` (`artist`);
ALTER TABLE `music_info` ADD INDEX `idx_upload_time` (`upload_time`);
ALTER TABLE `music_info` ADD INDEX `idx_upload_user` (`upload_user_id`);

-- 插入静态测试数据（假设上传用户ID=100、101存在）
INSERT INTO `music_info` 
  (`title`, `artist`, `album`, `duration`, `file_url`, `cover_url`, `upload_user_id`, `status`, `tags`)
VALUES
  -- 用户100上传的正常状态音乐
  ('夏日海滩', '海浪乐队', '阳光季节', 180, '/music/summer_beach.mp3', '/images/music-default-cover.png', 100, '0', '流行,轻快'),
  ('月光小夜曲', '钢琴诗人', '夜色集', 240, '/music/moonlight.mp3', '/images/music-default-cover.png', 100, '0', '古典,舒缓'),
  
  -- 用户101上传的已删除音乐
  ('摇滚力量', '风暴乐队', '狂野现场', 200, '/music/rock_power.mp3', '/images/music-default-cover.png', 101, '1', '摇滚,激情'),
  
  -- 用户100上传的审核中音乐
  ('电子未来', '太空漫步', '科技之声', 210, '/music/electronic_future.mp3', '/images/music-default-cover.png', 100, '2', '电子,科技'),
  
  -- 用户101上传的无专辑音乐（部分字段省略）
  ('爵士随想', '蓝调猫', NULL, 185, '/music/jazz_impromptu.mp3','/images/music-default-cover.png', 101, '0', '爵士,即兴');

-- 验证数据
SELECT * FROM `music_info` WHERE status != '1'; -- 排除已删除的音乐