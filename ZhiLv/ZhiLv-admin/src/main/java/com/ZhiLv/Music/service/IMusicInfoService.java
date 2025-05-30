package com.ZhiLv.Music.service;

import java.util.List;
import com.ZhiLv.Music.domain.MusicInfo;

/**
 * 音乐信息Service接口
 * 
 * @author lishengzhi
 * @date 2025-05-30
 */
public interface IMusicInfoService 
{
    /**
     * 查询音乐信息
     * 
     * @param musicId 音乐信息主键
     * @return 音乐信息
     */
    public MusicInfo selectMusicInfoByMusicId(Long musicId);

    /**
     * 查询音乐信息列表
     * 
     * @param musicInfo 音乐信息
     * @return 音乐信息集合
     */
    public List<MusicInfo> selectMusicInfoList(MusicInfo musicInfo);

    /**
     * 新增音乐信息
     * 
     * @param musicInfo 音乐信息
     * @return 结果
     */
    public int insertMusicInfo(MusicInfo musicInfo);

    /**
     * 修改音乐信息
     * 
     * @param musicInfo 音乐信息
     * @return 结果
     */
    public int updateMusicInfo(MusicInfo musicInfo);

    /**
     * 批量删除音乐信息
     * 
     * @param musicIds 需要删除的音乐信息主键集合
     * @return 结果
     */
    public int deleteMusicInfoByMusicIds(Long[] musicIds);

    /**
     * 删除音乐信息信息
     * 
     * @param musicId 音乐信息主键
     * @return 结果
     */
    public int deleteMusicInfoByMusicId(Long musicId);
}
