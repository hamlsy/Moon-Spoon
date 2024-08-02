<template>
  <div class="main-page">

    <main class="content">
      <div class="notice-container">
        <h3 class="page-title">공지사항 작성</h3>

        <form @submit.prevent="submitNotice" class="notice-form">
          <div class="form-group">
            <label for="title">제목</label>
            <input type="text" id="title" v-model="notice.title" required>
          </div>
          <div class="form-group">
            <label for="content">내용</label>
            <textarea id="content" v-model="notice.content" rows="15" required></textarea>
          </div>
          <div class="form-actions">
            <button type="button" @click="cancel" class="cancel-button">취소</button>
            <button type="submit" class="submit-button">저장</button>
          </div>
        </form>
      </div>
    </main>

    <footer class="footer">
      <p>&copy; 2024 Moon-Spoon. <a href="https://github.com/hamlsy" target="_blank" rel="noopener noreferrer">GitHub</a></p>
    </footer>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: 'NoticeWrite',
  data() {
    return {
      notice: {
        title: '',
        content: ''
      },
      token: localStorage.getItem('token'),
      isLogin: false
    }
  },
  methods: {
    submitNotice() {
      const headers = {
        'Authorization': this.token
      };
      axios.post("/api/notice/create",{
        title: this.notice.title,
        content: this.notice.content
      } ,{headers})
          .then((res) => {
            alert("등록되었습니다.")
            console.log(res, "등록");
            this.$router.push("/noticeList");
          })
          .catch((err) => {
            console.log(err, "ERROR");
          })
    },
    cancel() {
      this.$router.go(-1);
    },
    checkLogin() {
      this.isLogin = !!localStorage.getItem('token');
    },
    logout(){
      alert("로그아웃 되었습니다.");
      localStorage.removeItem("token");
      this.$router.go(0);
    },
    notValid(){
      alert("아직 구현되지 않은 기능입니다.");
    },
  },
  created() {
    // 관리자 권한 확인 (필요시)
  },

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
}

.page-title {
  text-align: center;
  margin-bottom: 2rem;
}

.notice-form {
  display: flex;
  flex-direction: column;
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

.form-actions {
  display: flex;
  justify-content: space-between;
}

.cancel-button,
.submit-button {
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
}

.cancel-button {
  background-color: #f44336;
  color: white;
}

.submit-button {
  background-color: #FFD700;
}

</style>