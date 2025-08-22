<template>
  <div class="todo-list">
    <div class="todo-header">
      <h4>
        <i class="el-icon-bell"></i>
        待办事项
      </h4>
      <el-button size="mini" type="text" @click="refreshTodo">
        <i class="el-icon-refresh"></i>
        刷新
      </el-button>
    </div>

    <div v-if="todoList.length === 0" class="empty-todo">
      <i class="el-icon-circle-check"></i>
      <p>暂无待办事项</p>
    </div>

    <div v-else class="todo-items">
      <div
        v-for="item in todoList"
        :key="item.id"
        :class="['todo-item', `priority-${item.priority}`]"
        @click="handleTodoClick(item)"
      >
        <div class="todo-icon">
          <i :class="item.icon"></i>
        </div>
        <div class="todo-content">
          <div class="todo-title">{{ item.title }}</div>
          <div class="todo-count">{{ item.count }} 件</div>
        </div>
        <div class="todo-arrow">
          <i class="el-icon-arrow-right"></i>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'TodoList',
  props: {
    todoList: {
      type: Array,
      default: () => []
    }
  },
  methods: {
    handleTodoClick(item) {
      this.$emit('todoClick', item)
    },
    refreshTodo() {
      this.$emit('refresh')
    }
  }
}
</script>

<style lang="scss" scoped>
.todo-list {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);

  .todo-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;

    h4 {
      margin: 0;
      color: #262626;
      font-size: 16px;
      font-weight: 600;

      i {
        margin-right: 8px;
        color: #1890ff;
      }
    }
  }

  .empty-todo {
    text-align: center;
    padding: 40px 0;
    color: #999;

    i {
      font-size: 48px;
      color: #d9d9d9;
      margin-bottom: 16px;
      display: block;
    }

    p {
      margin: 0;
      font-size: 14px;
    }
  }

  .todo-items {
    .todo-item {
      display: flex;
      align-items: center;
      padding: 16px 0;
      border-bottom: 1px solid #f0f0f0;
      cursor: pointer;
      transition: all 0.3s ease;

      &:last-child {
        border-bottom: none;
      }

      &:hover {
        background: #f5f5f5;
        border-radius: 6px;
        padding-left: 8px;
        padding-right: 8px;
      }

      .todo-icon {
        width: 40px;
        height: 40px;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 16px;

        i {
          font-size: 18px;
          color: #fff;
        }
      }

      .todo-content {
        flex: 1;

        .todo-title {
          font-size: 14px;
          color: #262626;
          font-weight: 500;
          margin-bottom: 4px;
        }

        .todo-count {
          font-size: 12px;
          color: #8c8c8c;
        }
      }

      .todo-arrow {
        color: #bfbfbf;
        font-size: 14px;
      }

      &.priority-high {
        .todo-icon {
          background: #ff4d4f;
        }
      }

      &.priority-medium {
        .todo-icon {
          background: #faad14;
        }
      }

      &.priority-normal {
        .todo-icon {
          background: #52c41a;
        }
      }
    }
  }
}
</style>
