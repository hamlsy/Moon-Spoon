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
      <h1 class="page-title">공지사항</h1>

      <div class="notice-list">
        <div v-for="notice in notices" :key="notice.id" class="notice-item" @click="goToDetail(notice.id)">
          <span class="notice-title">
            {{ notice.title }}
            <span class="notice-tag">[공지]</span>
          </span>
          <span class="notice-info">
            <span>{{ notice.author }}</span>
            <span>{{ notice.createdAt }}</span>
            <span v-if="notice.updatedAt">수정: {{ notice.updatedAt }}</span>
          </span>
        </div>
      </div>


      <div class="pagination">
        <button @click="changePage(currentPage - 1)" :disabled="currentPage === 1">이전</button>
        <span>{{ currentPage }} / {{ totalPages }}</span>
        <button @click="changePage(currentPage + 1)" :disabled="currentPage === totalPages">다음</button>
      </div>

      <button v-if="isAdmin" @click="goToWrite" class="write-button">공지사항 작성</button>
    </main>

    <footer class="footer">
      <p>&copy; 2024 Moon-Spoon. <a href="https://github.com/hamlsy" target="_blank" rel="noopener noreferrer">GitHub</a></p>
    </footer>
  </div>
</template>

<script>
export default {
  name: 'NoticePage',
  data() {
    return {
      isLogin: false,
      isAdmin: false,
      notices: [{'title':"aaa", "content":"adsadsaas", "author":"asdasdasd"}], // 서버에서 받아올 공지사항 목록
      currentPage: 1,
      totalPages: 1,
    }
  },
  methods: {
    notValid() {
      alert("아직 구현되지 않은 기능입니다.");
    },
    logout() {
      alert("로그아웃 되었습니다.");
      localStorage.removeItem("token");
      this.$router.go(0);
    },
    goToDetail(id) {
      this.$router.push(`/notice/${id}`);
    },
    goToWrite() {
      this.$router.push('/notice/write');
    },
    changePage() {
      // 페이지 변경 로직 (서버에 요청)
    },
    checkLogin() {
      this.isLogin = !!localStorage.getItem('token');
    },
    checkAdmin() {
      // 서버에 관리자 권한 확인 요청
    },
    fetchNotices() {
      // 서버에서 공지사항 목록 가져오기
    }
  },
  created() {
    this.checkLogin();
    this.checkAdmin();
    this.fetchNotices();
  }
}
</script>

<style scoped>
/* 기존 스타일 유지 */

.page-title {
  text-align: center;
  margin-bottom: 2rem;
}

.notice-list {
  background-color: white;
  border-radius: 12px;
  padding: 1rem;
}

.notice-item {
  display: flex;
  justify-content: space-between;
  padding: 1rem;
  border-bottom: 1px solid #e0e0e0;
  cursor: pointer;
}

.notice-item:last-child {
  border-bottom: none;
}

.notice-title {
  font-weight: bold;
}

.notice-tag {
  color: red;
  font-weight: bold;
  margin-left: 0.5rem;
}

.notice-info {
  color: #666;
  font-size: 0.9rem;
}

.notice-info span {
  margin-left: 1rem;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 2rem;
}

.pagination button {
  margin: 0 0.5rem;
}

.write-button {
  display: block;
  margin: 2rem auto 0;
  padding: 0.5rem 1rem;
  background-color: #FFD700;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
</style>