import request from '@/utils/request'

// 查询音乐信息列表
export function listMusic(query) {
  return request({
    url: '/music/music/list',
    method: 'get',
    params: query
  })
}

// 查询音乐信息详细
export function getMusic(musicId) {
  return request({
    url: '/music/music/' + musicId,
    method: 'get'
  })
}

// 新增音乐信息
export function addMusic(data) {
  return request({
    url: '/music/music',
    method: 'post',
    data: data
  })
}

// 修改音乐信息
export function updateMusic(data) {
  return request({
    url: '/music/music',
    method: 'put',
    data: data
  })
}

// 删除音乐信息
export function delMusic(musicId) {
  return request({
    url: '/music/music/' + musicId,
    method: 'delete'
  })
}
