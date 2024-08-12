<template>
  <div class="workbook-detail-page">
    <main class="content">
      <section class="workbook-info">
        <h1 class="workbook-title">{{ sharedWorkbook.title }}</h1>
        <div v-if="isUser" class="author-actions">
          <button @click="showEditForm" class="edit-button">수정</button>
          <button @click="deleteWorkbook" class="delete-button">삭제</button>
        </div>
        <div class="workbook-meta">
          <span>작성자: {{ sharedWorkbook.author }}</span>
<!--          <span>조회수: {{ sharedWorkbook.views }}</span>-->
          <span>문제 수: {{ sharedWorkbook.problemCount }}</span>
          <span>랜덤 여부: {{ sharedWorkbook.random ? 'O' : 'X' }}</span>
          <span>정답 공개: {{ sharedWorkbook.hasSolution ? 'O' : 'X' }}</span>
          <span>작성일: {{ formatDate(sharedWorkbook.sharedDate) }}</span>

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
      <button @click="startTest" class="start-test-button">
        <span>테스트 시작</span>
      </button>
      <section class="comments-section">
        <h2>댓글 ({{ comments.length }})</h2>
        <div class="comment-form">
          <textarea v-model="commentContent" placeholder="댓글을 입력하세요"></textarea>
          <button @click="addComment" class="comment-submit-button">댓글 작성</button>
        </div>
        <div class="comments-list">
          <div v-for="comment in comments" :key="comment.id" class="comment">
            <div class="comment-header">
              <span class="comment-author">{{ comment.author }}</span>
              <span class="comment-date">{{ formatDate(comment.createDate) }}</span>
            </div>
            <p class="comment-content">{{ comment.content }}</p>
          </div>
        </div>
      </section>
    </main>

    <!-- 수정 폼 팝업 -->
    <div v-if="showEditPopup" class="edit-popup">
      <div class="edit-popup-content">
        <h2>문제집 수정</h2>
        <input v-model="editForm.title" placeholder="제목" />
        <textarea v-model="editForm.content" placeholder="내용"></textarea>
        <div class="checkbox-group">
          <label>
            <input type="checkbox" v-model="editForm.isRandom" /> 랜덤 여부
          </label>
          <label>
            <input type="checkbox" v-model="editForm.hasSolution" /> 정답 공개 여부
          </label>
        </div>
        <div class="edit-popup-actions">
          <button @click="cancelEdit" class="cancel-button">취소</button>
          <button @click="submitEdit" class="submit-button">수정</button>
        </div>
      </div>
    </div>


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
        problemCount: "",
        hasSolution: "",
      },
      comments: [],
      commentContent: "",
      sharedWorkbookId: this.$route.fullPath.split("/").pop(),
      token: localStorage.getItem("token"),
      isUser: false,
      showEditPopup: false,
      editForm: {
        title: "",
        content: "",
        isRandom: false,
        hasSolution: false
      }
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
    deleteWorkbook() {
      const headers = {
        'Authorization': this.token
      };
      if (confirm("공유 문제집을 삭제하시겠습니까?(기존 문제집은 유지됩니다.)")) {
        axios.delete(`/api/sharedWorkbook/${this.sharedWorkbookId}`, {headers})
            .then((res) => {
              alert("삭제되었습니다.")
              this.$router.push("/sharedWorkbookList")
              console.log(res, "deleted!")
            })
            .catch((err) => {
              alert("ERROR!")
              console.log(err, "ERROR")
            })
      }
    },
    formatDate(dateString) {
      return dayjs(dateString).format('YY.MM.DD HH:mm');
    },
    addComment() {
      const headers = {
        'Authorization': this.token
      };
      axios.post("/api/comment/create", {
        content: this.commentContent,
        sharedWorkbookId: this.sharedWorkbookId
      }, {headers})
          .then((res) => {
            this.$router.go(0);
            console.log(res, "등록");
          })
          .catch((err) => {
            alert(err.message)
            console.log(err, "ERROR")
          })
    },
    getComments(){
      axios.get(`/api/comment/${this.sharedWorkbookId}/all`)
          .then((res) => {
            this.comments = res.data;
            console.log(res, "Get Comments");
          })
          .catch((err) => {
            console.log(err, "ERROR");
          })
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
    },
    showEditForm() {
      this.editForm = {
        title: this.sharedWorkbook.title,
        content: this.sharedWorkbook.content,
        isRandom: this.sharedWorkbook.isRandom,
        hasSolution: this.sharedWorkbook.hasSolution
      };
      this.showEditPopup = true;
    },
    cancelEdit() {
      this.showEditPopup = false;
    },
    submitEdit() {
      const headers = {
        'Authorization': this.token
      };
      axios.post(`/api/sharedWorkbook/${this.sharedWorkbookId}`,this.editForm,
          {headers})
          .then((res) => {
            alert("수정되었습니다.")
            this.showEditPopup = false;
            this.$router.go(0);
            console.log(res, "UPDATE!")
          })
          .catch((err) => {
            alert("ERROR!")
            console.log(err, "ERROR");
          })

    }
  },
  created() {
    this.getSharedWorkbook();
    this.getUser();
    this.getComments();
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
  z-index: 0;
  display: block;
  margin: 2rem auto;
  padding: 1rem 2rem;
  font-size: 1.2rem;
  background-color: white;
  color: #FFC000;
  border: 2px solid black;
  border-radius: 4px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: color 0.3s ease;
}

.start-test-button span {
  position: relative;
  z-index: 1;
}

.start-test-button::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 0;
  background-color: #FFD700;
  transition: height 0.3s ease;
}

.start-test-button:hover::before {
  height: 100%;
}

.start-test-button:hover {
  color: #000;
}


.edit-button, .delete-button {
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s ease;
  margin-left: 0.5rem;
}

.edit-button {
  background-color: #2196F3;
  color: white;
}

.delete-button {
  background-color: #f44336;
  color: white;
}

.edit-button:hover, .delete-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 2px 5px rgba(0,0,0,0.2);
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
  display: flex;
  margin-bottom: 2rem;
}

.comment-form textarea {
  flex-grow: 1;
  padding: 0.5rem;
  margin-right: 1rem;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  resize: vertical;
}

.comment-form button {
  padding: 0.5rem 1rem;
  background-color: #2196F3;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.3s ease;
  align-self: flex-start;
}

.comment {
  background-color: #f9f9f9;
  padding: 1rem;
  margin-bottom: 1rem;
  border-radius: 4px;
}

.comment-submit-button:hover {
  background-color: #1976D2;
  transform: translateY(-2px);
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


.author-actions {
  position: relative;
  top: -4rem;
  right: -1rem;
}


.edit-popup {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0,0,0,0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.edit-popup-content {
  background-color: white;
  padding: 2rem;
  border-radius: 8px;
  width: 80%;
  max-width: 500px;
}

.edit-popup-content h2 {
  margin-bottom: 1rem;
}

.edit-popup-content input,
.edit-popup-content textarea {
  width: 100%;
  padding: 0.5rem;
  margin-bottom: 1rem;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.checkbox-group {
  display: flex;
  justify-content: space-between;
  margin-bottom: 1rem;
}

.edit-popup-actions {
  display: flex;
  justify-content: flex-end;
}

.cancel-button, .submit-button {
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
  margin-left: 0.5rem;
}

.cancel-button {
  background-color: #f0f0f0;
}

.submit-button {
  background-color: #4CAF50;
  color: white;
}

</style>