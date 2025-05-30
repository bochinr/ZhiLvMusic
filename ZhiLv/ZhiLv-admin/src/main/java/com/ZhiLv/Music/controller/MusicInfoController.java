package com.ZhiLv.Music.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ZhiLv.common.annotation.Log;
import com.ZhiLv.common.core.controller.BaseController;
import com.ZhiLv.common.core.domain.AjaxResult;
import com.ZhiLv.common.enums.BusinessType;
import com.ZhiLv.Music.domain.MusicInfo;
import com.ZhiLv.Music.service.IMusicInfoService;
import com.ZhiLv.common.utils.poi.ExcelUtil;
import com.ZhiLv.common.core.page.TableDataInfo;

/**
 * 音乐信息Controller
 * 
 * @author lishengzhi
 * @date 2025-05-30
 */
@RestController
@RequestMapping("/music/music")
public class MusicInfoController extends BaseController
{
    @Autowired
    private IMusicInfoService musicInfoService;

    /**
     * 查询音乐信息列表
     */
    @PreAuthorize("@ss.hasPermi('music:music:list')")
    @GetMapping("/list")
    public TableDataInfo list(MusicInfo musicInfo)
    {
        startPage();
        List<MusicInfo> list = musicInfoService.selectMusicInfoList(musicInfo);
        return getDataTable(list);
    }

    /**
     * 导出音乐信息列表
     */
    @PreAuthorize("@ss.hasPermi('music:music:export')")
    @Log(title = "音乐信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MusicInfo musicInfo)
    {
        List<MusicInfo> list = musicInfoService.selectMusicInfoList(musicInfo);
        ExcelUtil<MusicInfo> util = new ExcelUtil<MusicInfo>(MusicInfo.class);
        util.exportExcel(response, list, "音乐信息数据");
    }

    /**
     * 获取音乐信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('music:music:query')")
    @GetMapping(value = "/{musicId}")
    public AjaxResult getInfo(@PathVariable("musicId") Long musicId)
    {
        return success(musicInfoService.selectMusicInfoByMusicId(musicId));
    }

    /**
     * 新增音乐信息
     */
    @PreAuthorize("@ss.hasPermi('music:music:add')")
    @Log(title = "音乐信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MusicInfo musicInfo)
    {
        return toAjax(musicInfoService.insertMusicInfo(musicInfo));
    }

    /**
     * 修改音乐信息
     */
    @PreAuthorize("@ss.hasPermi('music:music:edit')")
    @Log(title = "音乐信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MusicInfo musicInfo)
    {
        return toAjax(musicInfoService.updateMusicInfo(musicInfo));
    }

    /**
     * 删除音乐信息
     */
    @PreAuthorize("@ss.hasPermi('music:music:remove')")
    @Log(title = "音乐信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{musicIds}")
    public AjaxResult remove(@PathVariable Long[] musicIds)
    {
        return toAjax(musicInfoService.deleteMusicInfoByMusicIds(musicIds));
    }
}
