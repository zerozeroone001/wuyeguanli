import config from '@/config'
import storage from '@/utils/storage'
import constant from '@/utils/constant'
import { isHttp, isEmpty } from "@/utils/validate"
import { login, logout, getInfo, wechatLogin } from '@/api/login'
import { getMyProfile } from '@/api/profile' // 引入获取认证信息的API
import { getToken, setToken, removeToken } from '@/utils/auth'
import defAva from '@/static/avatar.png'

const baseUrl = config.baseUrl

const user = {
  state: {
    token: getToken(),
    id: storage.get(constant.id),
    name: storage.get(constant.name),
    avatar: storage.get(constant.avatar),
    roles: storage.get(constant.roles),
    permissions: storage.get(constant.permissions),
    // 物业系统扩展字段
    nickName: storage.get(constant.nickName) || '',
    authStatus: storage.get(constant.authStatus) || false,
    phone: storage.get(constant.phone),
    building: storage.get(constant.building),
    unit: storage.get(constant.unit),
    room: storage.get(constant.room),
    ownerType: storage.get(constant.ownerType) || '1', // 1-业主,2-租户,3-其他
    ownerProfile: storage.get(constant.ownerProfile) || {},
    isOwner: storage.get(constant.isOwner) || 0 // 0-未认证，1-业主，2-业委会
  },

  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_OWNER_PROFILE: (state, profile) => {
      state.ownerProfile = profile
      // 为了兼容旧代码，可以同时更新根状态上的字段
      state.authStatus = profile.authStatus
      state.nickName = profile.realName || state.nickName
      state.building = profile.buildingNo
      state.unit = profile.unitNo
      state.room = profile.roomNo
      storage.set(constant.ownerProfile, profile)
    },
    SET_ID: (state, id) => {
      state.id = id
      storage.set(constant.id, id)
    },
    SET_NAME: (state, name) => {
      state.name = name
      storage.set(constant.name, name)
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
      storage.set(constant.avatar, avatar)
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
      storage.set(constant.roles, roles)
    },
    SET_PERMISSIONS: (state, permissions) => {
      state.permissions = permissions
      storage.set(constant.permissions, permissions)
    },
    // 物业系统扩展mutations
    SET_NICKNAME: (state, nickName) => {
      state.nickName = nickName
      storage.set(constant.nickName, nickName)
    },
    SET_AUTH_STATUS: (state, authStatus) => {
      state.authStatus = authStatus
      storage.set(constant.authStatus, authStatus)
    },
    SET_PHONE: (state, phone) => {
      state.phone = phone
      storage.set(constant.phone, phone)
    },
    SET_PROPERTY_INFO: (state, propertyInfo) => {
      state.building = propertyInfo.building
      state.unit = propertyInfo.unit
      state.room = propertyInfo.room
      storage.set(constant.building, propertyInfo.building)
      storage.set(constant.unit, propertyInfo.unit)
      storage.set(constant.room, propertyInfo.room)
    },
    SET_OWNER_TYPE: (state, ownerType) => {
      state.ownerType = ownerType
      storage.set(constant.ownerType, ownerType)
    },
    SET_IS_OWNER: (state, isOwner) => {
      state.isOwner = isOwner
      storage.set(constant.isOwner, isOwner)
    }
  },

  actions: {
    // 登录
    Login({ commit }, userInfo) {
      const username = userInfo.username.trim()
      const password = userInfo.password
      const code = userInfo.code
      const uuid = userInfo.uuid
      return new Promise((resolve, reject) => {
        login(username, password, code, uuid).then(res => {
          setToken(res.token)
          commit('SET_TOKEN', res.token)
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 微信登录
    WechatLogin({ commit }, wechatLoginForm) {
      return new Promise((resolve, reject) => {
        wechatLogin(wechatLoginForm).then(res => {
          setToken(res.token)
          commit('SET_TOKEN', res.token)
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 获取用户信息
    GetInfo({ commit, state }) {
      return new Promise((resolve, reject) => {
        getInfo().then(res => {
          const user = res.user
		  let avatar = user.avatar || ""
		  if (!isHttp(avatar)) {
            avatar = (isEmpty(avatar)) ? defAva : baseUrl + avatar
          }
          const userid = (isEmpty(user) || isEmpty(user.userId)) ? "" : user.userId
		  const username = (isEmpty(user) || isEmpty(user.userName)) ? "" : user.userName
          const nickName = (isEmpty(user) || isEmpty(user.nickName)) ? username : user.nickName

		  if (res.roles && res.roles.length > 0) {
            commit('SET_ROLES', res.roles)
            commit('SET_PERMISSIONS', res.permissions)
          } else {
            commit('SET_ROLES', ['ROLE_DEFAULT'])
          }
          commit('SET_ID', userid)
          commit('SET_NAME', username)
          commit('SET_NICKNAME', nickName)
          commit('SET_AVATAR', avatar)
          // 更新认证状态和其他物业信息
          commit('SET_AUTH_STATUS', res.is_owner > 0) // 使用新的is_owner字段
          commit('SET_PHONE', user.phonenumber)
          commit('SET_PROPERTY_INFO', {
            building: user.building,
            unit: user.unit,
            room: user.room
          })
          commit('SET_OWNER_TYPE', user.ownerType)
          // 保存is_owner字段
          commit('SET_IS_OWNER', res.is_owner)
          resolve(res)
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 退出系统
    LogOut({ commit, state }) {
      return new Promise((resolve, reject) => {
        logout(state.token).then(() => {
          commit('SET_TOKEN', '')
          commit('SET_ROLES', [])
          commit('SET_PERMISSIONS', [])
          removeToken()
          storage.clean()
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 获取业主认证信息
    GetProfileInfo({ commit }) {
      return new Promise((resolve, reject) => {
        getMyProfile().then(res => {
          if (res.data) {
            commit('SET_OWNER_PROFILE', res.data)
            resolve(res.data)
          } else {
            reject('获取认证信息失败')
          }
        }).catch(error => {
          reject(error)
        })
      })
    }
  }
}

export default user
