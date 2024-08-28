<template>
  <div class="workbook-detail-page">
    <router-link to="/sharedWorkbookList" class="back-button">
      <i class="fas fa-arrow-left"></i> 뒤로가기
    </router-link>
    <main class="content">

      <section class="workbook-info">
        <div v-if="sharedWorkbook.isUser" class="author-actions">
          <button @click="showEditForm" class="edit-button">수정</button>
          <button @click="deleteWorkbook" class="delete-button">삭제</button>
        </div>
        <h1 class="workbook-title">{{ sharedWorkbook.title }}</h1>
        <div class="workbook-meta">
          <span>작성자: {{ sharedWorkbook.author }}</span>
<!--          <span>조회수: {{ sharedWorkbook.views }}</span>-->
          <span>문제 수: {{ sharedWorkbook.problemCount }} </span>
          <span>랜덤 여부: {{ sharedWorkbook.random ? 'O' : 'X' }} </span>
          <span>작성일: {{ formatDate(sharedWorkbook.createDate) }} </span>

        </div>
        <div class="workbook-actions">
          <!-- <button @click="likeWorkbook" class="like-button" :class="{ liked: workbook.isLiked }">
            👍 좋아요 {{ sharedWorkbook.likes }}
          </button> -->
        </div>
      </section>
      <section class="workbook-content">
        <pre>{{ sharedWorkbook.content }}</pre>
      </section>
      <button @click="startTest" class="button button--ujarak button--border-thin button--text-thick">
        <span>테스트 시작</span>
      </button>
      <section class="comments-section">
        <h2>댓글 ({{ sharedWorkbook.comments.length }})</h2>
        <div class="comment-form">
          <textarea v-model="commentContent" placeholder="댓글을 입력하세요"></textarea>
          <button @click="addComment" class="comment-submit-button">댓글 작성</button>
        </div>
        <div class="comments-list">
          <div v-for="comment in sharedWorkbook.comments" :key="comment.id" class="comment">
            <div class="comment-header">
              <span class="comment-author">{{ comment.author }}</span>
              <span class="comment-date">{{ formatDate(comment.createDate) }}</span>
            </div>
            <pre class="comment-content">{{ comment.content }}</pre>
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
            <input type="checkbox" v-model="editForm.random" /> 랜덤 여부
          </label>
        </div>
        <div class="edit-popup-actions">
          <button @click="cancelEdit" class="cancel-button">취소</button>
          <button @click="submitEdit" class="submit-button">수정</button>
        </div>
      </div>
    </div>


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
        createDate: "",
        random: "",
        content: "",
        problemCount: "",
        hasSolution: "",
        isUser: false,
        comments: []
      },
      commentContent: "",
      sharedWorkbookId: this.$route.fullPath.split("/").pop(),
      token: localStorage.getItem("token"),
      showEditPopup: false,
      editForm: {
        title: "",
        content: "",
        random: false,
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
      if (!this.token) {
        alert("로그인이 필요한 서비스입니다.");
      }else{
        this.$router.push({
          path: `/sharedProblemTest/${this.sharedWorkbookId}`,
          query: {
            sharedWorkbookId: this.sharedWorkbookId,
            sharedWorkbookTitle: this.sharedWorkbook.title,
            random: this.sharedWorkbook.random
          }
        })
      }
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
    showEditForm() {
      this.editForm = {
        title: this.sharedWorkbook.title,
        content: this.sharedWorkbook.content,
        random: this.sharedWorkbook.random,
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
  width: 100%;
  word-break: break-all;
}

.content {
  max-width: 1200px;
  margin: 6rem auto 0px;
  /** margin-left: 4rem;
  margin-right: 4rem; **/
  padding: 2rem 6rem;
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
  word-break: break-all;
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
  display: block;
  margin: 2rem auto;
  font-size: 1.2rem;
  position: relative;
  overflow: hidden;

  background-color: #f39c12; /* 짙은 노란색 */
  color: white;
  border: none;
  padding: 15px 30px;
  font-size: 18px;

  border-radius: 5px;
  cursor: pointer;
  box-shadow: 0 4px #e67e22; /* 좀 더 짙은 노란색 */
  transition: all 0.2s ease-in-out;

}

.start-test-button span {
  position: relative;
  z-index: 1;
}

.start-test-button:hover {
  background-color: #e67e22; /* 클릭 시 배경 색상 */
  box-shadow: 0 2px #d35400; /* 클릭 시 그림자 색상 */
  transform: translateY(2px);
}
.start-test-button:active {
  background-color: #d35400; /* 클릭 시 배경 색상 */
  box-shadow: 0 1px #c0392b; /* 클릭 시 그림자 색상 */
  transform: translateY(4px);
}

.edit-button, .delete-button {
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s ease;
  margin-left: 0.5rem;
  position: relative;
  top: 4.5rem;
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
.back-button {
  position: absolute;
  top: 70px;
  left: 20px;
  font-size: 1rem;
  color: #1B2A49;
  text-decoration: none;
  display: flex;
  align-items: center;
  transition: color 0.3s;
}

/** start button **/

.button {
  min-width: 150px;
  max-width: 250px;
  display: block;
  margin: 1em;
  padding: 1em 2em;
  color: inherit;
  vertical-align: middle;
  position: relative;
  background: none;
  font-size: 1rem;
  margin: 2rem auto;
  z-index: 0;
  -webkit-backface-visibility: hidden;
  -moz-osx-font-smoothing: grayscale;
  border: 1px solid;
  font-weight: 600;
}

.button--ujarak {
  -webkit-transition: border-color 0.4s, color 0.4s;
  transition: border-color 0.4s, color 0.4s;
}
.button--ujarak::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: #37474f;
  z-index: -1;
  opacity: 0;
  -webkit-transform: scale3d(0.7, 1, 1);
  transform: scale3d(0.7, 1, 1);
  -webkit-transition: -webkit-transform 0.4s, opacity 0.4s;
  transition: transform 0.4s, opacity 0.4s;
  -webkit-transition-timing-function: cubic-bezier(0.2, 1, 0.3, 1);
  transition-timing-function: cubic-bezier(0.2, 1, 0.3, 1);
}
.button--ujarak.button--round-s::before {
  border-radius: 2px;
}
.button--ujarak.button--inverted::before {
  background: #7986CB;
}
.button--ujarak,
.button--ujarak::before {
  -webkit-transition-timing-function: cubic-bezier(0.2, 1, 0.3, 1);
  transition-timing-function: cubic-bezier(0.2, 1, 0.3, 1);
}
.button--ujarak:hover {
  color: #fff;
  border-color: #37474f;
}
.button--ujarak.button--inverted:hover {
  color: #37474F;
  border-color: #fff;
}
.button--ujarak:hover::before {
  opacity: 1;
  -webkit-transform: translate3d(0, 0, 0);
  transform: translate3d(0, 0, 0);
}
.checkbox-group label, .radio-group label {
  display: flex;
  align-items: center;
  margin-bottom: 5px;
}

.checkbox-group input[type="checkbox"], .radio-group input[type="radio"] {
  margin-right: 10px;
  width: 20px;
  height: 20px;
}

pre{
  word-break: break-word;
  white-space: pre-wrap;
}


@media (max-width: 600px){
  .workbook-title{
    font-size: 1.3rem;
  }
  .content{
    margin-left: 2rem;
    margin-right: 2rem;
    padding: 2rem 2rem;
    background-color: white;
  }
  .workbook-meta{
    font-size: 0.7rem;
  }
}
</style>