/**
* 显示消息提示框
* @param content 提示的标题
*/
export function toast(content) {
  uni.showToast({
    icon: 'none',
    title: content
  })
}

/**
* 显示模态弹窗
* @param content 提示的标题
*/
export function showConfirm(content) {
  return new Promise((resolve, reject) => {
    uni.showModal({
      title: '提示',
      content: content,
      cancelText: '取消',
      confirmText: '确定',
      success: function(res) {
        resolve(res)
      }
    })
  })
}

/**
* 参数处理
* @param params 参数
*/
export function tansParams(params) {
  let result = ''
  for (const propName of Object.keys(params)) {
    const value = params[propName]
    var part = encodeURIComponent(propName) + "="
    if (value !== null && value !== "" && typeof (value) !== "undefined") {
      if (typeof value === 'object') {
        for (const key of Object.keys(value)) {
          if (value[key] !== null && value[key] !== "" && typeof (value[key]) !== 'undefined') {
            let params = propName + '[' + key + ']'
            var subPart = encodeURIComponent(params) + "="
            result += subPart + encodeURIComponent(value[key]) + "&"
          }
        }
      } else {
        result += part + encodeURIComponent(value) + "&"
      }
    }
  }
  return result
}

// --- 以下为新增的日期格式化函数 ---

function pad(str, length = 2) {
	str += ''
	while (str.length < length) {
		str = '0' + str
	}
	return str.slice(-length)
}

const parser = {
	yyyy: (dateObj) => {
		return pad(dateObj.year, 4)
	},
	yy: (dateObj) => {
		return pad(dateObj.year)
	},
	MM: (dateObj) => {
		return pad(dateObj.month)
	},
	M: (dateObj) => {
		return dateObj.month
	},
	dd: (dateObj) => {
		return pad(dateObj.day)
	},
	d: (dateObj) => {
		return dateObj.day
	},
	hh: (dateObj) => {
		return pad(dateObj.hour)
	},
	h: (dateObj) => {
		return dateObj.hour
	},
	mm: (dateObj) => {
		return pad(dateObj.minute)
	},
	m: (dateObj) => {
		return dateObj.minute
	},
	ss: (dateObj) => {
		return pad(dateObj.second)
	},
	s: (dateObj) => {
		return dateObj.second
	},
	SSS: (dateObj) => {
		return pad(dateObj.millisecond, 3)
	},
	S: (dateObj) => {
		return dateObj.millisecond
	},
}

function getDate(time) {
	if (time instanceof Date) {
		return time
	}
	switch (typeof time) {
		case 'string':
			{
				if (time.indexOf('T') > -1) {
					return new Date(time)
				}
				return new Date(time.replace(/-/g, '/'))
			}
		default:
			return new Date(time)
	}
}

export function formatDate(date, format = 'yyyy-MM-dd hh:mm:ss') {
	if (!date && date !== 0) {
		return ''
	}
	date = getDate(date)
	const dateObj = {
		year: date.getFullYear(),
		month: date.getMonth() + 1,
		day: date.getDate(),
		hour: date.getHours(),
		minute: date.getMinutes(),
		second: date.getSeconds(),
		millisecond: date.getMilliseconds()
	}
	const tokenRegExp = /yyyy|yy|MM|M|dd|d|hh|h|mm|m|ss|s|SSS|SS|S/
	let flag = true
	let result = format
	while (flag) {
		flag = false
		result = result.replace(tokenRegExp, function(matched) {
			flag = true
			return parser[matched](dateObj)
		})
	}
	return result
}
