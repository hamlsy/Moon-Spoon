<template>
  <div class="main-page">

    <main class="content">
      <button @click="goBack" class="back-button">← 뒤로가기</button>
      <div class="notice-container">

        <div class="notice-detail">
          <div v-if="isAdmin" class="admin-actions">
            <button v-if="isAdmin" @click="editNotice" class="edit-button">수정</button>
            <button v-if="isAdmin" @click="deleteNotice" class="delete-button">삭제</button>
          </div>
          <h1 class="notice-title">
            <span class="notice-tag">[공지]</span>
            {{ notice.title }}
          </h1>
          <div class="notice-info">
            <span>작성자: {{ notice.author }}</span>
            <span>작성시간: {{ formatDate(notice.createDate) }}</span>
            <span v-if="notice.updateDate">수정시간: {{ formatDate(notice.updateDate) }}</span>
          </div>

          <pre class="notice-content" v-html="notice.content"></pre>
        </div>

      </div>
    </main>


  </div>
</template>

<script>
import axios from "axios";
import dayjs from "dayjs";

export default {
  name: 'NoticeDetail',
  data() {
    return {
      notice: {
        title: '',
        author: '',
        createDate: '',
        updateDate: '',
        content: '',
      },
      noticeId: this.$route.fullPath.split("/").pop(),
      token: localStorage.getItem("token"),
      isAdmin: false,
      isLogin: false,
    }
  },
  methods: {
    goBack() {
      this.$router.push("/noticeList")
    },
    editNotice() {
      this.$router.push({
        path: "/noticeUpdateForm",
        query: {
          noticeId: this.noticeId
        }
      })
    },
    deleteNotice() {
      const headers = {
        'Authorization': this.token
      };
      if(confirm("삭제하시겠습니까?")){
        axios.delete(`/api/notice/delete/${this.noticeId}`, {headers})
            .then((res) => {
              alert("삭제되었습니다.");
              console.log(res,"삭제되었습니다.");
              this.$router.push("/noticeList");
            })
            .catch((err) => {
              console.log(err, "ERROR");
            })
      }

    },
    fetchNoticeDetail() {
      axios.get(`/api/notice/${this.noticeId}`)
          .then((res) => {
            this.notice = res.data;
          })
          .catch((err) => {
            console.log(err, "ERROR");
          })
    },
    checkAdmin() {
      const headers = {
        'Authorization': this.token
      };
      axios.get("/api/user/isAdmin", {headers})
          .then((res) => {
            this.isAdmin = res.data.admin;
          })
          .catch((err) => {
            console.log(err, "ERROR");
          })
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
    formatDate(dateString) {
      return dayjs(dateString).format('YY.MM.DD HH:mm');
    }
  },
  created() {
    this.fetchNoticeDetail();
    this.checkAdmin();
    this.checkLogin();
  }
}
</script>

<style scoped>

body, html {
  margin: 0;
  padding: 0;
  height: 100%;
  font-family: 'Noto Sans KR', sans-serif;
}

.main-page {
  /** background: linear-gradient(rgba(255,244,255,0.05) 40%, rgba(232,221,0,0.53)); **/
  color: #191f28;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.content {
  max-width: 1200px;
  margin: 80px 40px 0;
  padding: 2rem;
  flex: 1;
}


.feature-card h2 {
  color: black;
  margin-bottom: 1rem;
}


.additional-features h3 {
  color: black;
  margin-bottom: 1.5rem;
}

.additional-features ul {
  list-style-type: none;
  padding-left: 0;
}

.additional-features li {
  margin-bottom: 1rem;
  padding: 1rem;
  border-radius: 8px;
  transition: background-color 0.3s;
  background-color: white;
}

.additional-features li.feature-hovered {
  background-color: white;
}

.additional-features a {
  color: black;
  text-decoration: none;
  font-weight: bold;
}

.notice-container {
  width: 100%;
  max-width: 1000px;
  background-color: white;
  border-radius: 12px;
  position: relative;
  margin: auto;
  min-height: calc(100vh - 200px); /* 대략적인 높이 조정 */
}
.back-button {
  position: relative;
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
  border-top: 3px solid darkgray;
  word-break: break-all;
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
  height: 100%;
  white-space: pre-wrap; /* 공백과 줄 바꿈을 유지하면서 줄 바꿈 */
  word-wrap: break-word; /* 긴 단어가 넘치지 않도록 줄 바꿈 */
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
.edit-button, .delete-button{
  position: relative;
  top: -1rem;
}

@media (max-width: 500px){
  .content {
    padding: 0px;
  }

}
</style>