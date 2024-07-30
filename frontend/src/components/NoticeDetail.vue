<template>
  <div class="main-page">
    <nav class="navbar">
      <!-- 네비게이션 바 내용은 동일하므로 생략 -->
    </nav>

    <main class="content">
      <button @click="goBack" class="back-button">← 뒤로가기</button>

      <div class="notice-detail">
        <h1 class="notice-title">{{ notice.title }}</h1>
        <div class="notice-info">
          <span>작성자: {{ notice.author }}</span>
          <span>작성시간: {{ notice.createdAt }}</span>
          <span v-if="notice.updatedAt">수정시간: {{ notice.updatedAt }}</span>
        </div>
        <div class="notice-content" v-html="notice.content"></div>
      </div>

      <div v-if="isAdmin" class="admin-actions">
        <button @click="editNotice" class="edit-button">수정</button>
        <button @click="deleteNotice" class="delete-button">삭제</button>
      </div>
    </main>

    <footer class="footer">
      <p>&copy; 2024 Moon-Spoon. <a href="https://github.com/hamlsy" target="_blank" rel="noopener noreferrer">GitHub</a></p>
    </footer>
  </div>
</template>

<script>
export default {
  name: 'NoticeDetail',
  data() {
    return {
      isAdmin: false,
      notice: {
        title: '',
        author: '',
        createdAt: '',
        updatedAt: '',
        content: ''
      }
    }
  },
  methods: {
    goBack() {
      this.$router.go(-1);
    },
    editNotice() {
      // 수정 페이지로 이동
    },
    deleteNotice() {
      // 삭제 확인 후 삭제 요청
    },
    fetchNoticeDetail() {
      // 서버에서 공지사항 상세 정보 가져오기
    },
    checkAdmin() {
      // 관리자 권한 확인
    }
  },
  created() {
    this.fetchNoticeDetail();
    this.checkAdmin();
  }
}
</script>

<style scoped>
/* 기존 스타일 유지 */

.back-button {
  position: absolute;
  top: 1rem;
  left: 1rem;
  background: none;
  border: none;
  cursor: pointer;
  font-size: 1rem;
}

.notice-detail {
  background-color: white;
  border-radius: 12px;
  padding: 2rem;
  margin-top: 3rem;
}

.notice-title {
  font-size: 1.5rem;
  margin-bottom: 1rem;
}

.notice-info {
  color: #666;
  margin-bottom: 2rem;
}

.notice-info span {
  margin-right: 1rem;
}

.notice-content {
  line-height: 1.6;
}

.admin-actions {
  margin-top: 2rem;
  text-align: right;
}

.admin-actions button {
  margin-left: 1rem;
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.edit-button {
  background-color: #4CAF50;
  color: white;
}

.delete-button {
  background-color: #f44336;
  color: white;
}
</style>