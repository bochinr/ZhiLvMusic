<template>
  <div class="app-container">
    <!-- 搜索区域 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="音乐标题" prop="title">
        <el-input v-model="queryParams.title" placeholder="请输入音乐标题" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="艺术家" prop="artist">
        <el-input v-model="queryParams.artist" placeholder="请输入艺术家" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="专辑名称" prop="album">
        <el-input v-model="queryParams.album" placeholder="请输入专辑名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option label="正常" value="0" />
          <el-option label="删除" value="1" />
          <el-option label="审核中" value="2" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮区域 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['music:music:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['music:music:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['music:music:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['music:music:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 音乐列表 -->
    <el-table v-loading="loading" :data="musicList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="音乐ID" align="center" prop="musicId" width="80" />
      <el-table-column label="封面" align="center" width="100">
        <template slot-scope="scope">
          <el-image
            :src="scope.row.coverUrl || require('@/assets/images/music-default-cover.png')"
            fit="cover"
            style="width: 60px; height: 60px; border-radius: 4px;"
            :preview-src-list="[scope.row.coverUrl]"
          >
            <div slot="error" class="image-slot">
              <i class="el-icon-picture-outline" style="font-size: 24px; color: #999;"></i>
            </div>
          </el-image>
        </template>
      </el-table-column>
      <el-table-column label="音乐信息" min-width="200">
        <template slot-scope="scope">
          <div class="music-info">
            <div class="music-title">{{ scope.row.title }}</div>
            <div class="music-artist">{{ scope.row.artist }}</div>
            <div class="music-album">{{ scope.row.album || '未命名专辑' }}</div>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="时长" align="center" width="80">
        <template slot-scope="scope">
          <span>{{ formatDuration(scope.row.duration) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="播放次数" align="center" prop="playCount" width="100" sortable>
        <template slot-scope="scope">
          <el-tag>{{ scope.row.playCount || 0 }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status === '0' ? 'success' : scope.row.status === '2' ? 'warning' : 'danger'">
            {{ statusMap[scope.row.status] || scope.row.status }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="上传时间" align="center" width="150">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.uploadTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="标签" align="center" min-width="150">
        <template slot-scope="scope">
          <el-tag
            v-for="(tag, index) in scope.row.tags ? scope.row.tags.split(',') : []"
            :key="index2"
            size="mini"
            style="margin-right: 5px; margin-bottom: 5px;"
          >
            {{ tag }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-video-play"
            @click="handlePlay(scope.row)"
          >播放</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['music:music:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['music:music:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加/修改音乐对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="音乐标题" prop="title">
              <el-input v-model="form.title" placeholder="请输入音乐标题" />
            </el-form-item>
            <el-form-item label="艺术家" prop="artist">
              <el-input v-model="form.artist" placeholder="请输入艺术家" />
            </el-form-item>
            <el-form-item label="专辑名称" prop="album">
              <el-input v-model="form.album" placeholder="请输入专辑名称" />
            </el-form-item>
            <el-form-item label="时长(秒)" prop="duration">
              <el-input-number v-model="form.duration" :min="0" :step="10" style="width: 100%;" />
            </el-form-item>
            <el-form-item label="上传用户ID" prop="uploadUserId">
              <el-input v-model="form.uploadUserId" placeholder="请输入上传用户ID" />
            </el-form-item>
            <el-form-item label="上传时间" prop="uploadTime">
              <el-date-picker
                v-model="form.uploadTime"
                type="datetime"
                value-format="yyyy-MM-dd HH:mm:ss"
                placeholder="选择上传时间"
                style="width: 100%;"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="封面图片">
              <el-upload
                class="avatar-uploader"
                action="/api/upload"
                :show-file-list="false"
                :on-success="handleCoverSuccess"
                :before-upload="beforeCoverUpload"
              >
                <img v-if="form.coverUrl" :src="form.coverUrl" class="avatar" />
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
              </el-upload>
            </el-form-item>
            <el-form-item label="音乐文件">
              <el-upload
                class="upload-demo"
                action="/api/upload"
                :on-success="handleMusicSuccess"
                :before-upload="beforeMusicUpload"
                :on-remove="handleMusicRemove"
                :limit="1"
              >
                <el-button size="small" type="primary">点击上传</el-button>
                <div slot="tip" class="el-upload__tip">只能上传mp3文件，且不超过50MB</div>
              </el-upload>
              <div v-if="form.fileUrl" class="music-preview">
                <audio controls :src="form.fileUrl" style="width: 100%; margin-top: 10px;"></audio>
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="标签" prop="tags">
          <el-tag
            v-for="tag in form.tagsArray"
            :key="tag"
            closable
            :disable-transitions="false"
            @close="handleTagClose(tag)"
            style="margin-right: 10px;"
          >
            {{ tag }}
          </el-tag>
          <el-input
            class="input-new-tag"
            v-if="inputTagVisible"
            v-model="inputTagValue"
            ref="saveTagInput"
            size="small"
            @keyup.enter="handleTagInputConfirm"
            @blur="handleTagInputConfirm"
          >
          </el-input>
          <el-button v-else class="button-new-tag" size="small" @click="showTagInput">+ 添加标签</el-button>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio label="0">正常</el-radio>
            <el-radio label="2">审核中</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 音乐播放器 -->
    <div class="music-player" v-if="currentMusic">
      <audio ref="audioPlayer" :src="currentMusic.fileUrl" controls autoplay style="width: 100%;"></audio>
      <div class="player-info">
        <div class="player-cover">
          <img :src="currentMusic.coverUrl || require('@/assets/images/music-default-cover.png')" />
        </div>
        <div class="player-details">
          <div class="player-title">{{ currentMusic.title }}</div>
          <div class="player-artist">{{ currentMusic.artist }}</div>
          <div class="player-album">{{ currentMusic.album || '未命名专辑' }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { listMusic, getMusic, delMusic, addMusic, updateMusic } from "@/api/music/music";

export default {
  name: "Music",
  data() {
    return {
      // 加载状态
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 音乐列表数据
      musicList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 状态映射
      statusMap: {
        '0': '正常',
        '1': '删除',
        '2': '审核中'
      },
      // 当前播放音乐
      currentMusic: null,
      // 标签输入相关
      inputTagVisible: false,
      inputTagValue: '',
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: null,
        artist: null,
        album: null,
        duration: null,
        uploadUserId: null,
        uploadTime: null,
        playCount: null,
        status: null,
        tags: null
      },
      // 表单参数
      form: {
        tagsArray: [] // 用于标签编辑的数组
      },
      // 表单校验规则
      rules: {
        title: [
          { required: true, message: "音乐标题不能为空", trigger: "blur" }
        ],
        artist: [
          { required: true, message: "艺术家不能为空", trigger: "blur" }
        ],
        fileUrl: [
          { required: true, message: "请上传音乐文件", trigger: "blur" }
        ],
        uploadUserId: [
          { required: true, message: "上传用户ID不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "请选择状态", trigger: "change" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询音乐列表 */
    getList() {
      this.loading = true;
      listMusic(this.queryParams).then(response => {
        this.musicList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 格式化时长 */
    formatDuration(seconds) {
      if (!seconds) return '00:00';
      const min = Math.floor(seconds / 60);
      const sec = seconds % 60;
      return `${min.toString().padStart(2, '0')}:${sec.toString().padStart(2, '0')}`;
    },
    /** 播放音乐 */
    handlePlay(row) {
      this.currentMusic = row;
      // 增加播放次数
      row.playCount = (row.playCount || 0) + 1;
      updateMusic(row);
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 多选框选中数据 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.musicId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加音乐";
      this.form.tagsArray = [];
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const musicId = row.musicId || this.ids[0];
      getMusic(musicId).then(response => {
        this.form = response.data;
        // 初始化标签数组
        this.form.tagsArray = this.form.tags ? this.form.tags.split(',') : [];
        this.open = true;
        this.title = "修改音乐信息";
      });
    },
    /** 提交表单 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          // 将标签数组转换为字符串
          this.form.tags = this.form.tagsArray.join(',');

          if (this.form.musicId != null) {
            updateMusic(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addMusic(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const musicIds = row.musicId || this.ids;
      this.$modal.confirm('是否确认删除选中的音乐？').then(() => {
        return delMusic(musicIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('music/music/export', {
        ...this.queryParams
      }, `music_${new Date().getTime()}.xlsx`)
    },
    /** 重置表单 */
    reset() {
      this.form = {
        musicId: null,
        title: null,
        artist: null,
        album: null,
        duration: null,
        fileUrl: null,
        coverUrl: null,
        uploadUserId: null,
        uploadTime: null,
        playCount: 0,
        status: "0",
        tags: null,
        tagsArray: []
      };
      this.resetForm("form");
    },
    /** 取消按钮 */
    cancel() {
      this.open = false;
      this.reset();
    },
    /** 封面图片上传成功 */
    handleCoverSuccess(res, file) {
      this.form.coverUrl = res.url;
    },
    /** 音乐文件上传成功 */
    handleMusicSuccess(res, file) {
      this.form.fileUrl = res.url;
    },
    /** 音乐文件移除 */
    handleMusicRemove() {
      this.form.fileUrl = null;
    },
    /** 封面图片上传前校验 */
    beforeCoverUpload(file) {
      const isImage = file.type.includes('image');
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isImage) {
        this.$message.error('只能上传图片文件!');
      }
      if (!isLt2M) {
        this.$message.error('图片大小不能超过 2MB!');
      }
      return isImage && isLt2M;
    },
    /** 音乐文件上传前校验 */
    beforeMusicUpload(file) {
      const isAudio = file.type.includes('audio');
      const isLt50M = file.size / 1024 / 1024 < 50;

      if (!isAudio) {
        this.$message.error('只能上传音频文件!');
      }
      if (!isLt50M) {
        this.$message.error('音频文件大小不能超过 50MB!');
      }
      return isAudio && isLt50M;
    },
    /** 标签操作 */
    handleTagClose(tag) {
      this.form.tagsArray.splice(this.form.tagsArray.indexOf(tag), 1);
    },
    showTagInput() {
      this.inputTagVisible = true;
      this.$nextTick(() => {
        this.$refs.saveTagInput.$refs.input.focus();
      });
    },
    handleTagInputConfirm() {
      const inputValue = this.inputTagValue.trim();
      if (inputValue && !this.form.tagsArray.includes(inputValue)) {
        this.form.tagsArray.push(inputValue);
      }
      this.inputTagVisible = false;
      this.inputTagValue = '';
    }
  }
};
</script>

<style scoped>
.app-container {
  padding-bottom: 80px; /* 为播放器留出空间 */
}

.music-info {
  line-height: 1.6;
}
.music-title {
  font-weight: bold;
  font-size: 16px;
}
.music-artist {
  color: #666;
  font-size: 14px;
}
.music-album {
  color: #999;
  font-size: 13px;
}

.music-player {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background: #fff;
  border-top: 1px solid #ebeef5;
  padding: 10px 20px;
  box-shadow: 0 -2px 12px rgba(0, 0, 0, 0.1);
  z-index: 1000;
  display: flex;
  flex-direction: column;
}

.player-info {
  display: flex;
  align-items: center;
  margin-top: 10px;
}

.player-cover {
  width: 40px;
  height: 40px;
  border-radius: 4px;
  overflow: hidden;
  margin-right: 10px;
}

.player-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.player-details {
  flex: 1;
}

.player-title {
  font-weight: bold;
}

.player-artist, .player-album {
  font-size: 12px;
  color: #666;
}

.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 100%;
  height: 200px;
}

.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 100%;
  height: 200px;
  line-height: 200px;
  text-align: center;
}

.avatar {
  width: 100%;
  height: 100%;
  display: block;
  object-fit: contain;
}
</style>
