<template>
  <div class="main-page">
    <nav class="navbar">
      <!-- 네비게이션 바 내용은 동일하므로 생략 -->
    </nav>

    <main class="content">
      <h1 class="page-title">공지사항 작성</h1>

      <form @submit.prevent="submitNotice" class="notice-form">
        <div class="form-group">
          <label for="title">제목</label>
          <input type="text" id="title" v-model="notice.title" required>
        </div>
        <div class="form-group">
          <label for="content">내용</label>
          <textarea id="content" v-model="notice.content" rows="10" required></textarea>
        </div>
        <button type="submit" class="submit-button">작성 완료</button>
      </form>
    </main>

    <footer class="footer">
      <p>&copy; 2024 Moon-Spoon. <a href="https://github.com/hamlsy" target="_blank" rel="noopener noreferrer">GitHub</a></p>
    </footer>
  </div>
</template>

<script>
export default {
  name: 'NoticeWrite',
  data() {
    return {
      notice: {
        title: '',
        content: ''
      }
    }
  },
  methods: {
    submitNotice() {
      // 서버에 공지사항 작성 요청
    }
  },
  created() {
    // 관리자 권한 확인 (필요시)
  }
}
</script>

<style scoped>
/* 기존 스타일 유지 */

.page-title {
  text-align: center;
  margin-bottom: 2rem;
}

.notice-form {
  background-color: white;
  border-radius: 12px;
  padding: 2rem;
}

.form-group {
  margin-bottom: 1.5rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
}

.form-group input,
.form-group textarea {
  width: 100%;
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.submit-button {
  display: block;
  width: 100%;
  padding: 0.75rem;
  background-color: #FFD700;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
}
</style>