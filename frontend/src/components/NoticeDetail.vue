<template>
  <div class="main-page">
    <nav class="navbar">
      <div class="navbar-container">
        <div class="navbar-brand">
          <router-link to="/mainPage" class="logo"><a style="color: #FFD700">Moon</a>-Spoon🥄</router-link>
        </div>
        <ul class="navbar-menu">
          <li><router-link to="/mainPage" class="nav-link">홈</router-link></li>
          <li v-if="!isLogin"><router-link to="/user/login" class="nav-link">로그인</router-link></li>
          <li v-if="isLogin"><a @click="logout" class="nav-link">로그아웃</a></li>
          <li><router-link to="/user/signup" class="nav-link">회원가입</router-link></li>
          <li><a @click="notValid" class="nav-link">프로필</a></li>
        </ul>
      </div>
    </nav>
    <main class="content">
      <div class="notice-container">
        <button @click="goBack" class="back-button">← 뒤로가기</button>

        <div class="notice-detail">
          <h1 class="notice-title">
            <span class="notice-tag">[공지]</span>
            {{ notice.title }}
          </h1>
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
        title: 'asd',
        author: 'asd',
        createdAt: 'asd',
        updatedAt: 'asd',
        content: 'asdasd'
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
.content {
  display: flex;
  justify-content: center;
  padding: 2rem;
}
.notice-container {
  width: 100%;
  max-width: 800px;
  background-color: white;
  border-radius: 12px;
  padding: 2rem;
  position: relative;
}
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
  display: flex;
  align-items: center;
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
.notice-tag {
  color: red;
  font-weight: bold;
  margin-right: 0.5rem;
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