import { listCommunity, getCommunity } from "@/api/system/community"

const STORAGE_KEY = "CURRENT_COMMUNITY_ID"

const state = {
  isSuperAdmin: false,
  currentId: localStorage.getItem(STORAGE_KEY)
    ? Number(localStorage.getItem(STORAGE_KEY))
    : null,
  currentName: "",
  communityList: []
}

const mutations = {
  SET_IS_SUPER_ADMIN(state, flag) {
    state.isSuperAdmin = flag
  },
  SET_CURRENT_ID(state, id) {
    state.currentId = id
    if (id === null || id === undefined || id === "") {
      localStorage.removeItem(STORAGE_KEY)
    } else {
      localStorage.setItem(STORAGE_KEY, id)
    }
  },
  SET_CURRENT_NAME(state, name) {
    state.currentName = name || ""
  },
  SET_COMMUNITY_LIST(state, list) {
    state.communityList = Array.isArray(list) ? list : []
  }
}

const actions = {
  /**
   * 根据登录用户初始化小区信息。
   * @param {*} context Vuex 上下文
   * @param {*} payload { user, roles }
   */
  async initialize({ commit, dispatch, state }, { user, roles }) {
    const isAdmin = Array.isArray(roles) && roles.includes("admin")
    commit("SET_IS_SUPER_ADMIN", isAdmin)

    const userCommunityId =
      user && user.communityId != null ? Number(user.communityId) : null

    // 如果本地存储的 ID 对当前用户无效（例如普通账号切换），则重置
    if (!isAdmin) {
      commit("SET_CURRENT_ID", userCommunityId)
    } else if (state.currentId == null && userCommunityId != null) {
      commit("SET_CURRENT_ID", userCommunityId)
    }

    // 非超级管理员必须展示绑定小区
    if (!isAdmin) {
      await dispatch("resolveCommunityName", userCommunityId)
      commit("SET_COMMUNITY_LIST", [])
      return
    }

    // 超级管理员加载全部小区列表
    await dispatch("loadCommunityList")

    // 设置显示名称（优先本地选中的 ID）
    const targetId =
      state.currentId != null ? state.currentId : userCommunityId ?? null
    commit("SET_CURRENT_ID", targetId)
    if (targetId == null) {
      commit("SET_CURRENT_NAME", "全部小区")
    } else {
      await dispatch("resolveCommunityName", targetId)
    }
  },

  /**
   * 加载全部小区列表（超级管理员使用）。
   */
  async loadCommunityList({ commit }) {
    const params = {
      pageNum: 1,
      pageSize: 9999
    }
    const response = await listCommunity(params)
    const rows = response && response.rows ? response.rows : []
    const mapped = rows.map(item => ({
      id: item.communityId,
      name: item.communityName
    }))
    commit("SET_COMMUNITY_LIST", mapped)
  },

  /**
   * 切换当前小区。
   */
  async changeCommunity({ commit, dispatch, state }, communityId) {
    if (communityId === undefined || communityId === "") {
      commit("SET_CURRENT_ID", null)
      commit("SET_CURRENT_NAME", "全部小区")
      return
    }
    const numericId = Number(communityId)
    commit("SET_CURRENT_ID", numericId)

    const target =
      state.communityList.find(item => item.id === numericId) || null
    if (target) {
      commit("SET_CURRENT_NAME", target.name)
    } else {
      await dispatch("resolveCommunityName", numericId)
    }
  },

  /**
   * 根据 ID 解析小区名称。
   */
  async resolveCommunityName({ commit }, communityId) {
    if (communityId == null) {
      commit("SET_CURRENT_NAME", "全部小区")
      return
    }
    try {
      const res = await getCommunity(communityId)
      const name =
        res && res.data && res.data.communityName
          ? res.data.communityName
          : ""
      commit("SET_CURRENT_NAME", name)
    } catch (error) {
      commit("SET_CURRENT_NAME", "")
      throw error
    }
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
