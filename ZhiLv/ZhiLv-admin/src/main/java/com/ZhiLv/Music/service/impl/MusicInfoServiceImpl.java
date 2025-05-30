package com.ZhiLv.Music.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ZhiLv.Music.mapper.MusicInfoMapper;
import com.ZhiLv.Music.domain.MusicInfo;
import com.ZhiLv.Music.service.IMusicInfoService;

/**
 * 音乐信息Service业务层处理
 * 
 * @author lishengzhi
 * @date 2025-05-30
 */
@Service
public class MusicInfoServiceImpl implements IMusicInfoService 
{
    @Autowired
    private MusicInfoMapper musicInfoMapper;

    /**
     * 查询音乐信息
     * 
     * @param musicId 音乐信息主键
     * @return 音乐信息
     */
    @Override
    public MusicInfo selectMusicInfoByMusicId(Long musicId)
    {
        return musicInfoMapper.selectMusicInfoByMusicId(musicId);
    }

    /**
     * 查询音乐信息列表
     * 
     * @param musicInfo 音乐信息
     * @return 音乐信息
     */
    @Override
    public List<MusicInfo> selectMusicInfoList(MusicInfo musicInfo)
    {
        return musicInfoMapper.selectMusicInfoList(musicInfo);
    }

    /**
     * 新增音乐信息
     * 
     * @param musicInfo 音乐信息
     * @return 结果
     */
    @Override
    public int insertMusicInfo(MusicInfo musicInfo)
    {
        return musicInfoMapper.insertMusicInfo(musicInfo);
    }

    /**
     * 修改音乐信息
     * 
     * @param musicInfo 音乐信息
     * @return 结果
     */
    @Override
    public int updateMusicInfo(MusicInfo musicInfo)
    {
        return musicInfoMapper.updateMusicInfo(musicInfo);
    }

    /**
     * 批量删除音乐信息
     * 
     * @param musicIds 需要删除的音乐信息主键
     * @return 结果
     */
    @Override
    public int deleteMusicInfoByMusicIds(Long[] musicIds)
    {
        return musicInfoMapper.deleteMusicInfoByMusicIds(musicIds);
    }

    /**
     * 删除音乐信息信息
     * 
     * @param musicId 音乐信息主键
     * @return 结果
     */
    @Override
    public int deleteMusicInfoByMusicId(Long musicId)
    {
        return musicInfoMapper.deleteMusicInfoByMusicId(musicId);
    }
}
