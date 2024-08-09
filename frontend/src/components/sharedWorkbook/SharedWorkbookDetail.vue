<template>
  <div class="workbook-detail-page">
    <main class="content">
      <section class="workbook-info">
        <h1 class="workbook-title">{{ sharedWorkbook.title }}</h1>
        <div class="workbook-meta">
          <span>작성자: {{ sharedWorkbook.author }}</span>
          <span>작성일: {{ formatDate(sharedWorkbook.sharedDate) }}</span>
<!--          <span>조회수: {{ sharedWorkbook.views }}</span>-->
          <div v-if="isUser" class="author-actions">
            <button @click="editWorkbook" class="edit-button">수정</button>
            <button @click="deleteWorkbook" class="delete-button">삭제</button>
          </div>
        </div>
        <div class="workbook-actions">
          <!-- <button @click="likeWorkbook" class="like-button" :class="{ liked: workbook.isLiked }">
            👍 좋아요 {{ sharedWorkbook.likes }}
          </button> -->
        </div>
      </section>
      <section class="workbook-content">
        <p>{{ sharedWorkbook.content }}</p>
      </section>
      <button @click="startTest" class="start-test-button">테스트 시작</button>
      <section class="comments-section">
        <h2>댓글 ({{ comments.length }})</h2>
        <div class="comment-form">
          <textarea v-model="newComment" placeholder="댓글을 입력하세요"></textarea>
          <button @click="addComment">댓글 작성</button>
        </div>
        <div class="comments-list">
          <div v-for="comment in sharedWorkbook.comments" :key="comment.id" class="comment">
            <div class="comment-header">
              <span class="comment-author">{{ comment.author }}</span>
              <span class="comment-date">{{ comment.date }}</span>
            </div>
            <p class="comment-content">{{ comment.content }}</p>
          </div>
        </div>
      </section>
    </main>

    <footer class="footer">
      <p>&copy; 2024 문제집 공유 플랫폼. <a href="https://github.com/your-github" target="_blank" rel="noopener noreferrer">GitHub</a></p>
    </footer>
  </div>
</template>

<script>
import axios from "axios";
import dayjs from "dayjs";

export default {
  name: 'WorkbookDetailPage',
  data() {
    return {
      sharedWorkbook: {
        id: '',
        title: "",
        author: "",
        sharedDate: "",
        random: "",
        content: "",
      },
      comments: [],
      newComment: "",
      sharedWorkbookId: this.$route.fullPath.split("/").pop(),
      token: localStorage.getItem("token"),
      isUser: false
    }
  },
  methods: {
    // likeWorkbook() {
    //   this.workbook.isLiked = !this.workbook.isLiked;
    //   this.workbook.likes += this.workbook.isLiked ? 1 : -1;
    // },

    getSharedWorkbook(){
      axios.get(`/api/sharedWorkbook/${this.sharedWorkbookId}`)
          .then((res) => {
            this.sharedWorkbook = res.data
            console.log(res, "fetch data");
          })
          .catch((err) => {
            console.log(err, "ERROR");
          })
    },
    startTest() {
      // 테스트 시작 로직 구현
      console.log("테스트 시작");
    },
    editWorkbook() {
      // 문제집 수정 페이지로 이동 또는 수정 모달 표시
      console.log("문제집 수정");
    },
    deleteWorkbook() {
      // 문제집 삭제 확인 및 삭제 로직 구현
      if (confirm("정말로 이 문제집을 삭제하시겠습니까?")) {
        console.log("문제집 삭제");
      }
    },
    formatDate(dateString) {
      return dayjs(dateString).format('YY.MM.DD HH:mm');
    },
    addComment() {

    },
    getUser(){
      const headers = {
        'Authorization': this.token
      };
      axios.get(`/api/sharedWorkbook/${this.sharedWorkbookId}/getUser`, {headers})
          .then((res) => {
            this.isUser = res.data.user;
            console.log(res, "");

          })
          .catch((err) => {
            console.log(err, "ERROR");
          })
    }
  },
  created() {
    this.getSharedWorkbook();
    this.getUser();
  }
}
</script>

<style scoped>
.workbook-detail-page {
  background-color: #f9f9f9;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  font-family: 'Noto Sans KR', sans-serif;
}

.content {
  max-width: 800px;
  margin: 5rem auto;
  min-width: 600px;
  padding: 2rem 4rem;
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}

.workbook-title {
  font-size: 2rem;
  text-align: left;
  margin-bottom: 1rem;
}

.workbook-meta {
  display: flex;
  justify-content: space-between;
  margin-bottom: 1rem;
  color: #666;
}

.workbook-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
}

.like-button, .start-test-button, .edit-button, .delete-button {
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 20px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.like-button {
  background-color: #f0f0f0;
}

.like-button.liked {
  background-color: #FFD700;
}

.start-test-button {
  background-color: #4CAF50;
  color: white;
  margin-left: 80%;
}

.edit-button {
  background-color: #2196F3;
  color: white;
}

.delete-button {
  background-color: #f44336;
  color: white;
}

.workbook-content {
  margin-bottom: 2rem;
  line-height: 1.6;
}

.comments-section {
  border-top: 1px solid #e0e0e0;
  padding-top: 2rem;
}

.comment-form {
  margin-bottom: 2rem;
}

.comment-form textarea {
  width: 100%;
  padding: 0.5rem;
  margin-bottom: 0.5rem;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
}

.comment-form button {
  padding: 0.5rem 1rem;
  background-color: #2196F3;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.comment {
  background-color: #f9f9f9;
  padding: 1rem;
  margin-bottom: 1rem;
  border-radius: 4px;
}

.comment-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 0.5rem;
  font-size: 0.9rem;
  color: #666;
}

.footer {
  background-color: #f2f4f6;
  color: #191f28;
  text-align: center;
  padding: 1rem;
  margin-top: auto;
}

.footer a {
  color: black;
  text-decoration: none;
}
</style>