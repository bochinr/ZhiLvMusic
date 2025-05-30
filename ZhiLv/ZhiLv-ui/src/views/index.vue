<template>
  <div class="home-container">
    <!-- 导航栏 -->
    <div class="navbar">
      <div class="logo">
        <div class="logo-text">音乐数据管理平台</div>
      </div>
    </div>

    <!-- 内容区域 -->
    <div class="content">
      <!-- 欢迎区域 -->
      <div class="welcome-section">
        <h1 class="welcome-title">音乐数据洞察中心</h1>
        <p class="welcome-subtitle">全面分析音乐平台数据，洞察用户行为，优化音乐服务体验</p>
      </div>

      <!-- 核心数据卡片 -->
      <div class="dashboard">
        <div class="stat-card">
          <div class="stat-icon">👥</div>
          <div class="stat-title">用户数量</div>
          <div class="stat-value">67</div>
        </div>

        <div class="stat-card">
          <div class="stat-icon">🎤</div>
          <div class="stat-title">歌手数量</div>
          <div class="stat-value">122</div>
        </div>

        <div class="stat-card">
          <div class="stat-icon">🎵</div>
          <div class="stat-title">歌曲数量</div>
          <div class="stat-value">498</div>
        </div>
      </div>

      <!-- 图表区域 -->
      <div class="charts-container">
        <div class="chart-card">
          <div class="chart-title">
            <span>📊</span> 歌曲类型比例
          </div>
          <div ref="genreChart" class="chart"></div>
        </div>

        <div class="chart-card">
          <div class="chart-title">
            <span>👫</span> 歌手性别比例
          </div>
          <div ref="genderChart" class="chart"></div>
        </div>
      </div>

      <!-- 歌手国籍分布 -->
      <div class="full-width-chart">
        <div class="chart-title">
          <span>🌍</span> 歌手国籍分布
        </div>
        <div ref="nationalityChart" class="chart"></div>
      </div>

      <!-- 页脚 -->
      <div class="footer">
        <p>音乐数据管理平台 v{{ version }} © 2025 版权所有</p>
        <div class="footer-links">
          <a href="#">关于我们</a>
          <a href="#">帮助文档</a>
          <a href="#">服务条款</a>
          <a href="#">隐私政策</a>
          <a href="#">联系我们</a>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Home',
  data() {
    return {
      version: "3.8.9",
      genreChart: null,
      genderChart: null,
      nationalityChart: null
    };
  },
  mounted() {
    // 动态加载本地资源
    this.loadLocalResources().then(() => {
      this.initCharts();
    });

    // 响应式调整
    window.addEventListener('resize', this.handleResize);
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize);

    // 销毁图表实例
    if (this.genreChart) this.genreChart.dispose();
    if (this.genderChart) this.genderChart.dispose();
    if (this.nationalityChart) this.nationalityChart.dispose();
  },
  methods: {
    loadLocalResources() {
      return new Promise((resolve) => {
        // 检查资源是否已加载
        if (window.echarts && window.Vue) {
          resolve();
          return;
        }

        // 加载Vue.js
        const vueScript = document.createElement('script');
        vueScript.src = '/local/vue.js';
        vueScript.onload = () => {
          // 加载ECharts
          const echartsScript = document.createElement('script');
          echartsScript.src = '/local/echarts.min.js';
          echartsScript.onload = () => {
            // 加载Element UI
            const elementStyle = document.createElement('link');
            elementStyle.rel = 'stylesheet';
            // elementStyle.href = '/local/element-ui.css';
            document.head.appendChild(elementStyle);

            const elementScript = document.createElement('script');
            // elementScript.src = '/local/element-ui.js';
            elementScript.onload = resolve;
            document.body.appendChild(elementScript);
          };
          document.body.appendChild(echartsScript);
        };
        document.body.appendChild(vueScript);
      });
    },

    initCharts() {
      // 歌曲类型饼图
      this.genreChart = echarts.init(this.$refs.genreChart);
      this.genreChart.setOption({
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          right: 10,
          top: 'center',
          textStyle: {
            color: '#333'
          }
        },
        series: [{
          name: '歌曲类型',
          type: 'pie',
          radius: ['40%', '70%'],
          avoidLabelOverlap: false,
          itemStyle: {
            borderRadius: 10,
            borderColor: '#fff',
            borderWidth: 2
          },
          label: {
            show: false,
            position: 'center',
            color: '#333'
          },
          emphasis: {
            label: {
              show: true,
              fontSize: '18',
              fontWeight: 'bold',
              color: '#333'
            }
          },
          labelLine: {
            show: false
          },
          data: [
            { value: 25, name: '歌家流行' },
            { value: 18, name: '节奏有曲面' },
            { value: 15, name: '华谊流行' },
            { value: 12, name: '轻音乐' },
            { value: 10, name: '粤语流行' },
            { value: 8, name: '韩国流行' },
            { value: 5, name: '古典' },
            { value: 4, name: '雅俗领唱' },
            { value: 2, name: '班酷' },
            { value: 1, name: '电子' }
          ],
          color: [
            '#4fc3f7', '#29b6f6', '#039be5', '#0288d1',
            '#0277bd', '#01579b', '#80deea', '#4dd0e1',
            '#26c6da', '#00bcd4'
          ]
        }]
      });

      // 歌手性别饼图
      this.genderChart = echarts.init(this.$refs.genderChart);
      this.genderChart.setOption({
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          right: 10,
          top: 'center',
          textStyle: {
            color: '#333'
          }
        },
        series: [{
          name: '歌手性别',
          type: 'pie',
          radius: '60%',
          center: ['40%', '50%'],
          itemStyle: {
            borderRadius: 10,
            borderColor: '#fff',
            borderWidth: 2
          },
          label: {
            formatter: '{b}: {c} ({d}%)',
            color: '#333'
          },
          data: [
            { value: 78, name: '男' },
            { value: 44, name: '女' }
          ],
          color: ['#4fc3f7', '#f48fb1']
        }]
      });

      // 歌手国籍柱状图
      this.nationalityChart = echarts.init(this.$refs.nationalityChart);
      this.nationalityChart.setOption({
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'value',
          axisLine: {
            lineStyle: {
              color: '#4a4a4a'
            }
          },
          axisLabel: {
            color: '#333'
          }
        },
        yAxis: {
          type: 'category',
          data: ['英国', '德国', '日本', '韩国', '中国', '美国'],
          axisLine: {
            lineStyle: {
              color: '#4a4a4a'
            }
          },
          axisLabel: {
            color: '#333'
          }
        },
        series: [{
          name: '歌手数量',
          type: 'bar',
          data: [5, 8, 10, 15, 17, 27],
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
              { offset: 0, color: '#4fc3f7' },
              { offset: 1, color: '#29b6f6' }
            ])
          },
          label: {
            show: true,
            position: 'right',
            color: '#333'
          }
        }]
      });
    },

    handleResize() {
      if (this.genreChart) this.genreChart.resize();
      if (this.genderChart) this.genderChart.resize();
      if (this.nationalityChart) this.nationalityChart.resize();
    }
  }
};
</script>

<style scoped>
.home-container {
  background: #f5f7fa;
  min-height: 100vh;
  padding: 20px 0;
}

.navbar {
  background: white;
  padding: 15px 30px;
  display: flex;
  justify-content: flex-start; /* 修改为靠左显示 */
  align-items: center;
  position: sticky;
  top: 0;
  z-index: 100;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  max-width: 1400px;
  margin: 0 auto 20px;
  border-radius: 10px;
}

.logo {
  display: flex;
  align-items: center;
  gap: 15px;
}

.logo-text {
  font-size: 22px;
  font-weight: 700;
  color: #333;
}

.content {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 20px;
}

.welcome-section {
  text-align: center;
  padding: 40px 0 30px;
}

.welcome-title {
  font-size: 36px;
  font-weight: 700;
  margin-bottom: 15px;
  color: #333;
}

.welcome-subtitle {
  font-size: 18px;
  color: #666;
  max-width: 700px;
  margin: 0 auto;
  line-height: 1.6;
}

.dashboard {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 25px;
  margin: 30px 0;
}

.stat-card {
  background: white;
  border-radius: 12px;
  padding: 25px;
  text-align: center;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  transition: all 0.3s;
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
}

.stat-icon {
  font-size: 42px;
  margin-bottom: 15px;
  color: #666;
}

.stat-title {
  font-size: 18px;
  color: #666;
  margin-bottom: 10px;
}

.stat-value {
  font-size: 42px;
  font-weight: 700;
  color: #333;
}

.charts-container {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 25px;
  margin: 30px 0;
}

.chart-card, .full-width-chart {
  background: white;
  border-radius: 12px;
  padding: 25px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.full-width-chart {
  grid-column: span 2;
  margin: 30px 0;
}

.chart-title {
  font-size: 20px;
  margin-bottom: 20px;
  color: #333;
  display: flex;
  align-items: center;
  gap: 10px;
  font-weight: 600;
}

.chart-title span {
  font-size: 24px;
}

.chart {
  height: 350px;
  width: 100%;
}

.footer {
  background: white;
  text-align: center;
  padding: 30px 0 20px;
  color: #666;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  margin-top: 20px;
}

.footer-links {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-top: 15px;
}

.footer-links a {
  color: #666;
  text-decoration: none;
  transition: all 0.3s;
}

.footer-links a:hover {
  color: #409EFF; /* 鼠标悬停时变为 #409EFF */
  text-decoration: underline;
}

@media (max-width: 992px) {
  .dashboard {
    grid-template-columns: 1fr;
  }

  .charts-container {
    grid-template-columns: 1fr;
  }

  .full-width-chart {
    grid-column: span 1;
  }

  .navbar {
    flex-direction: column;
    gap: 15px;
    padding: 15px;
  }
}

@media (max-width: 768px) {
  .welcome-title {
    font-size: 28px;
  }

  .welcome-subtitle {
    font-size: 16px;
  }

  .stat-value {
    font-size: 36px;
  }

  .footer-links {
    flex-wrap: wrap;
  }

  .chart {
    height: 300px;
  }
}
</style>
