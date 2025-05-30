package com.ZhiLv.Music.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ZhiLv.common.annotation.Excel;
import com.ZhiLv.common.core.domain.BaseEntity;

/**
 * 音乐信息对象 music_info
 * 
 * @author lishengzhi
 * @date 2025-05-30
 */
public class MusicInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 音乐ID */
    private Long musicId;

    /** 音乐标题 */
    @Excel(name = "音乐标题")
    private String title;

    /** 艺术家 */
    @Excel(name = "艺术家")
    private String artist;

    /** 专辑名称 */
    @Excel(name = "专辑名称")
    private String album;

    /** 时长（秒） */
    @Excel(name = "时长", readConverterExp = "秒=")
    private Long duration;

    /** 文件存储路径（OSS或本地） */
    @Excel(name = "文件存储路径", readConverterExp = "O=SS或本地")
    private String fileUrl;

    /** 封面图片路径 */
    @Excel(name = "封面图片路径")
    private String coverUrl;

    /** 上传用户ID（关联sys_user.user_id） */
    @Excel(name = "上传用户ID", readConverterExp = "关=联sys_user.user_id")
    private Long uploadUserId;

    /** 上传时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "上传时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date uploadTime;

    /** 播放次数 */
    @Excel(name = "播放次数")
    private Long playCount;

    /** 状态（0正常 1删除 2审核） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=删除,2=审核")
    private String status;

    /** 标签（逗号分隔，如流行,电子） */
    @Excel(name = "标签", readConverterExp = "逗=号分隔，如流行,电子")
    private String tags;

    public void setMusicId(Long musicId) 
    {
        this.musicId = musicId;
    }

    public Long getMusicId() 
    {
        return musicId;
    }

    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }

    public void setArtist(String artist) 
    {
        this.artist = artist;
    }

    public String getArtist() 
    {
        return artist;
    }

    public void setAlbum(String album) 
    {
        this.album = album;
    }

    public String getAlbum() 
    {
        return album;
    }

    public void setDuration(Long duration) 
    {
        this.duration = duration;
    }

    public Long getDuration() 
    {
        return duration;
    }

    public void setFileUrl(String fileUrl) 
    {
        this.fileUrl = fileUrl;
    }

    public String getFileUrl() 
    {
        return fileUrl;
    }

    public void setCoverUrl(String coverUrl) 
    {
        this.coverUrl = coverUrl;
    }

    public String getCoverUrl() 
    {
        return coverUrl;
    }

    public void setUploadUserId(Long uploadUserId) 
    {
        this.uploadUserId = uploadUserId;
    }

    public Long getUploadUserId() 
    {
        return uploadUserId;
    }

    public void setUploadTime(Date uploadTime) 
    {
        this.uploadTime = uploadTime;
    }

    public Date getUploadTime() 
    {
        return uploadTime;
    }

    public void setPlayCount(Long playCount) 
    {
        this.playCount = playCount;
    }

    public Long getPlayCount() 
    {
        return playCount;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setTags(String tags) 
    {
        this.tags = tags;
    }

    public String getTags() 
    {
        return tags;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("musicId", getMusicId())
            .append("title", getTitle())
            .append("artist", getArtist())
            .append("album", getAlbum())
            .append("duration", getDuration())
            .append("fileUrl", getFileUrl())
            .append("coverUrl", getCoverUrl())
            .append("uploadUserId", getUploadUserId())
            .append("uploadTime", getUploadTime())
            .append("playCount", getPlayCount())
            .append("status", getStatus())
            .append("tags", getTags())
            .toString();
    }
}
