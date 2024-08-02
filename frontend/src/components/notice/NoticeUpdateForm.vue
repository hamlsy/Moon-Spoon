<template>
  <div class="main-page">
    <main class="content">
      <div class="notice-container">
        <h3 class="page-title">공지사항 수정</h3>

        <form @submit.prevent="updateNotice" class="notice-form">
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
            <button type="submit" class="submit-button">수정</button>
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
  name: 'NoticeUpdate',
  data() {
    return {
      notice: {
        title: '',
        content: ''
      },
      token: localStorage.getItem('token'),
      noticeId: this.$route.query.noticeId,
      isAdmin: false,
      isLogin: false,
    }
  },
  methods: {
    updateNotice() {
      const headers = {
        'Authorization': this.token
      };
      axios.post(`/api/notice/update/${this.noticeId}`,{
        title: this.notice.title,
        content: this.notice.content
      } ,{headers})
          .then((res) => {
            alert("수정되었습니다.");
            this.$router.push(`/notice/${this.noticeId}`);
            console.log(res, "등록");
          })
          .catch((err) => {
            console.log(err, "ERROR");
          })
    },

    fetchNotice(){
      axios.get(`/api/notice/${this.noticeId}`)
          .then((res) => {
            this.notice = res.data;
            console.log(res, "FETCH NOTICE");
          })
          .catch((err) => {
            alert("ERROR!")
            console.log(err, "ERROR");
          })
    },
    checkRole(){
      const headers = {
        'Authorization': this.token
      };
      axios.get('/api/user/isAdmin', {headers})
          .then((res) => {
            this.isAdmin = res.data.admin;
            if(!this.isAdmin) {
              alert("권한이 없습니다!");
              this.$router.go(-1);
            }
            console.log(res, "check admin");
          })
          .catch((err) => {
            alert("ERROR OCCURRED!");
            console.log(err, "ERROR");
          });

    },
    cancel() {
      this.$router.go(-1);
    },
    notValid() {
      alert("아직 구현되지 않은 기능입니다.");
    },
    checkLogin() {
      this.isLogin = !!localStorage.getItem('token');
    },
    logout() {
      alert("로그아웃 되었습니다.");
      localStorage.removeItem("token");
      this.$router.go(0);
    },
  },
  created() {
    // 관리자 권한 확인 (필요시)
    this.checkRole();
    this.fetchNotice();
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