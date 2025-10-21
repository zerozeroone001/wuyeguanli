<template>
  <div class="navbar">
    <hamburger
      id="hamburger-container"
      :is-active="sidebar.opened"
      class="hamburger-container"
      @toggleClick="toggleSideBar"
    />

    <breadcrumb v-if="!topNav" id="breadcrumb-container" class="breadcrumb-container" />
    <top-nav v-if="topNav" id="topmenu-container" class="topmenu-container" />

    <div class="right-menu">
      <template v-if="device !== 'mobile'">
        <div class="right-menu-item community-wrapper">
          <template v-if="isSuperAdmin">
            <el-select
              v-model="selectedCommunity"
              placeholder="请选择小区"
              size="small"
              @change="handleCommunityChange"
            >
              <el-option :value="0" label="全部小区" />
              <el-option
                v-for="item in communityOptions"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </template>
          <template v-else>
            <span class="community-name">{{ displayCommunityName }}</span>
          </template>
        </div>

        <search id="header-search" class="right-menu-item" />

        <el-tooltip content="源码地址" effect="dark" placement="bottom">
          <ruo-yi-git id="ruoyi-git" class="right-menu-item hover-effect" />
        </el-tooltip>

        <el-tooltip content="文档地址" effect="dark" placement="bottom">
          <ruo-yi-doc id="ruoyi-doc" class="right-menu-item hover-effect" />
        </el-tooltip>

        <screenfull id="screenfull" class="right-menu-item hover-effect" />

        <el-tooltip content="布局大小" effect="dark" placement="bottom">
          <size-select id="size-select" class="right-menu-item hover-effect" />
        </el-tooltip>
      </template>

      <el-dropdown class="avatar-container right-menu-item hover-effect" trigger="hover">
        <div class="avatar-wrapper">
          <img :src="avatar" class="user-avatar" />
          <span class="user-nickname">{{ nickName }}</span>
        </div>
        <el-dropdown-menu slot="dropdown">
          <router-link to="/user/profile">
            <el-dropdown-item>个人中心</el-dropdown-item>
          </router-link>
          <el-dropdown-item divided @click.native="logout">
            <span>退出登录</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>

      <div
        v-if="setting"
        class="right-menu-item hover-effect setting"
        @click="setLayout"
      >
        <svg-icon icon-class="more-up" />
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex"
import Breadcrumb from "@/components/Breadcrumb"
import TopNav from "@/components/TopNav"
import Hamburger from "@/components/Hamburger"
import Screenfull from "@/components/Screenfull"
import SizeSelect from "@/components/SizeSelect"
import Search from "@/components/HeaderSearch"
import RuoYiGit from "@/components/RuoYi/Git"
import RuoYiDoc from "@/components/RuoYi/Doc"

export default {
  emits: ["setLayout"],
  components: {
    Breadcrumb,
    TopNav,
    Hamburger,
    Screenfull,
    SizeSelect,
    Search,
    RuoYiGit,
    RuoYiDoc
  },
  data() {
    return {
      selectedCommunity: ""
    }
  },
  computed: {
    ...mapGetters([
      "sidebar",
      "avatar",
      "device",
      "nickName",
      "isSuperAdmin",
      "currentCommunityName",
      "currentCommunityId",
      "communityOptions"
    ]),
    setting() {
      return this.$store.state.settings.showSettings
    },
    topNav() {
      return this.$store.state.settings.topNav
    },
    displayCommunityName() {
      return this.currentCommunityName || '未绑定小区'
    }
  },
  watch: {
    currentCommunityId: {
      immediate: true,
      handler(val) {
        if (val === null || val === undefined) {
          this.selectedCommunity = ""
        } else {
          this.selectedCommunity = val
        }
      }
    }
  },
  methods: {
    toggleSideBar() {
      this.$store.dispatch("app/toggleSideBar")
    },
    setLayout() {
      this.$emit("setLayout")
    },
    handleCommunityChange(value) {
      const targetValue = value === "" ? null : value
      this.$store
        .dispatch("community/changeCommunity", targetValue)
        .then(() => {
          this.$message.success('小区切换成功，页面即将刷新')
          this.$nextTick(() => {
            this.$router.go(0)
          })
        })
        .catch(() => {})
    },
    logout() {
      this.$confirm('确定退出登录吗?', '提示', {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          this.$store.dispatch("LogOut").then(() => {
            location.href = "/index"
          })
        })
        .catch(() => {})
    }
  }
}
</script>

<style lang="scss" scoped>
.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);

  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background 0.3s;
    -webkit-tap-highlight-color: transparent;

    &:hover {
      background: rgba(0, 0, 0, 0.025);
    }
  }

  .breadcrumb-container {
    float: left;
  }

  .topmenu-container {
    position: absolute;
    left: 50px;
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 50px;
    width: 19%;
    display: flex;
    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: middle;

      &.hover-effect {
        cursor: pointer;
        transition: background 0.3s;

        &:hover {
          background: rgba(0, 0, 0, 0.025);
        }
      }
    }

    .community-wrapper {
      display: flex;
      align-items: center;
      padding-right: 16px;

      .el-select {
        width: 180px;
      }

      .community-name {
        font-size: 14px;
        color: #606266;
      }
    }

    .avatar-container {
      margin-right: 0;
      padding-right: 0;

      .avatar-wrapper {
        margin-top: 10px;
        position: relative;

        .user-avatar {
          cursor: pointer;
          width: 30px;
          height: 30px;
          border-radius: 50%;
        }

        .user-nickname {
          position: relative;
          bottom: 10px;
          font-size: 14px;
          font-weight: bold;
        }
      }
    }
  }
}
</style>
