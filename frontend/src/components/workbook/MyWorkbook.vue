<template>
  <div class="main-page">
    <router-link to="/mainPage" class="back-button">
      <i class="fas fa-arrow-left"></i> 뒤로가기
    </router-link>
    <div class="title">
      <h1><div class="card-icon slide-in-fade">📝 내 문제집</div></h1>
    </div>

    <div class="search-sort-container">
      <input class="search-input-box" v-model="searchQuery" placeholder="문제집 검색" @input="filterWorkbooks($event)"/>
      <div class="sort-dropdown">
        <button @click="toggleSortDropdown">정렬 <i class="fas fa-caret-down"></i></button>
        <div v-if="showSortDropdown" class="dropdown-content">
          <a href="#" @click="sortWorkbooks('newest')">최신순</a>
          <a href="#" @click="sortWorkbooks('oldest')">오래된순</a>
          <a href="#" @click="sortWorkbooks('alphabetical')">가나다순</a>
        </div>
      </div>

    </div>
    <main class="content">
      <div class="workbook-list">
        <div v-for="workbook in filteredWorkbooks" :key="workbook.id" class="workbook-item">
          <div v-if="updateIndex !== workbook.id" @click="goWorkbookDetail(workbook.id)">
            <div class="workbook-content">
              <div class="workbook-main">
                <h3>{{ workbook.title }}</h3>
                <p>{{ workbook.content }}</p>
              </div>
              <div class="workbook-info">
                <p>문제 수: {{ workbook.problemCount }}</p>
                <p>생성일: {{ formatDate(workbook.createDate) }}</p>
                <p v-if="workbook.updateDate">수정일: {{ formatDate(workbook.updateDate) }}</p>
              </div>
            </div>
            <div class="workbook-actions">
              <button class="action-btn edit-btn" @click.stop="startUpdate(workbook.id)">
                <i class="fas fa-edit"></i>
              </button>
              <button class="action-btn delete-btn" @click.stop="confirmDelete(workbook.id)">
                <i class="fas fa-trash"></i>
              </button>
              <button class="action-btn share-btn" @click.stop="showSharePopup(workbook)">
                <i class="fas fa-share"></i>
              </button>
            </div>
          </div>
          <div v-else>
            <input v-model="updateWorkbook.title" class="edit-input" />
            <textarea v-model="updateWorkbook.content" class="edit-textarea"></textarea>
            <div class="popup-buttons">
              <button @click="cancelUpdate">취소</button>
              <button @click="saveUpdate(workbook)">저장</button>
            </div>
          </div>
        </div>
        <button class="add-workbook-btn" @click="showAddWorkbookPopup">
          <span class="plus-icon">+</span>
          <span>새 문제집 추가</span>
        </button>
      </div>
    </main>

    <!-- 새 문제집 추가 팝업 -->
    <transition name="fade">
      <div v-if="showAddPopup" class="popup-overlay" @click.self="cancelAddWorkbook">
        <div class="popup">
          <h2>새 문제집 추가</h2>
          <input v-model="newWorkbook.title" placeholder="문제집 이름" />
          <textarea v-model="newWorkbook.content" placeholder="설명"></textarea>
          <div class="popup-buttons">
            <button @click="cancelAddWorkbook">취소</button>
            <button @click="addWorkbook">추가</button>
          </div>
        </div>
      </div>
    </transition>

    <!-- 삭제 확인 팝업 -->
    <transition name="fade">
      <div v-if="showDeletePopup" class="popup-overlay" @click.self="cancelDelete">
        <div class="popup">
          <h2>문제집 삭제</h2>
          <p>정말로 이 문제집을 삭제하시겠습니까?</p>
          <div class="popup-buttons">
            <button @click="cancelDelete">취소</button>
            <button @click="deleteWorkbook">삭제</button>
          </div>
        </div>
      </div>
    </transition>

    <!-- 공유 팝업 -->
    <transition name="fade">
      <div v-if="showShareWorkbookPopup" class="popup-overlay" @click.self="cancelShare">
        <div class="popup">
          <h2>문제집 공유</h2>
          <input v-model="shareWorkbookDetail.title" placeholder="제목" />
          <textarea v-model="shareWorkbookDetail.content" placeholder="내용"></textarea>
          <div class="checkbox-group">
            <label>
              <input type="checkbox" v-model="shareWorkbookDetail.random"> 랜덤 출제
            </label>
          </div>
          <div class="popup-buttons">
            <button @click="cancelShare">취소</button>
            <button @click="shareWorkbook">공유</button>
          </div>
        </div>
      </div>
    </transition>

  </div>
</template>

<script>
import axios from "axios";
import dayjs from 'dayjs';

export default {
  name: 'MyWorkbooksPage',

  data() {
    return {
      workbooks: [],
      showAddPopup: false,
      showDeletePopup: false,
      showShareWorkbookPopup: false,
      shareWorkbookDetail: {
        workbookId: '',
        title: '',
        content: '',
        random: false,
      },
      newWorkbook: { title: '', content: '' },
      workbookToDelete: null,
      searchQuery: '',
      filteredWorkbooks: [],
      showSortDropdown: false,
      sortOrder: 'newest',
      token: localStorage.getItem('token'),
      updateIndex: null,
      updateWorkbook: {title: '', content: ''}
    }
  },
  created(){
    this.getWorkbook();
    this.checkLogin();
  },
  methods: {
    startUpdate(workbookId) {
      this.updateWorkbook = this.workbooks.find(workbook => workbook.id === workbookId);
      this.updateIndex = workbookId;
    },
    cancelUpdate() {
      this.updateIndex = null;
    },
    notImplemented() {
      alert("이 기능은 아직 구현되지 않았습니다.");
    },
    saveUpdate(workbook) {
      const headers = {
        'Authorization': this.token
      }

      axios.post(`/api/workbook/update/${workbook.id}`,
          {
            title: this.updateWorkbook.title,
            content: this.updateWorkbook.content
          }, { headers })
          .then((res) => {
            this.getWorkbook();
            console.log(res);
          })
          .catch((error) => {
            alert(error.response.data.message);
            console.log("ERROR", error);
          })
      const index = this.workbooks.findIndex(w => w.id === this.updateIndex);
      this.workbooks[index] = { ...this.updateWorkbook };
      this.updateIndex = null;
    },
    notValid(){
      alert("아직 구현되지 않은 기능입니다.");
    },
    goWorkbookDetail(workbookId){
      this.$router.push(`/workbookDetail/${workbookId}`);
    },
    getWorkbook(){
      const headers = {
        'Authorization': this.token
      }
      axios.get("/api/workbook/all", {headers})
          .then((res) => {
            this.workbooks = res.data;
            this.filterWorkbooks();
            console.log("workbook loaded", res);
          }).catch((error) => {
        if(error.response.data.message ===  "JWT token is expired"){
          console.log(error.response.data.message);
          alert("토큰이 만료되었습니다. 다시 로그인하세요.");
          localStorage.removeItem("token");
          this.$router.push("/mainPage");
        }
        else if(error.response.status === 404){
          console.log("문제집 없음");
        }
        else{
          alert(error.response.data.message);
          this.$router.push("/mainPage");
        }
        console.log("ERROR", error);
      })
    },
    showAddWorkbookPopup() {
      this.showAddPopup = true;
    },
    addWorkbook() {
      const headers = {
        'Authorization': this.token
      }
      axios.post("/api/workbook/create",
          {
            title: this.newWorkbook.title,
            content: this.newWorkbook.content
          },{headers})
          .then((res) => {
            //성공시 새로고침
            this.$router.go(0);
            console.log("OK", res);
          })
          .catch((error) => {
            alert(error.response.data.message);
            console.log("ERROR", error);
          })
      this.showAddPopup = false;
      this.newWorkbook = { title: '', content: '' };
      this.filterWorkbooks();
    },
    cancelAddWorkbook() {
      this.showAddPopup = false;
      this.newWorkbook = { title: '', content: '' };
    },
    confirmDelete(workbookId) {
      this.workbookToDelete = workbookId;
      this.showDeletePopup = true;
    },
    deleteWorkbook() {
      const headers = {
        'Authorization': this.token
      }
      axios.delete("/api/workbook/delete/" + this.workbookToDelete, {headers})
          .then((res) => {
            alert("삭제되었습니다.");
            this.$router.go(0);
            console.log("삭제", res);
          })
          .catch((error) => {
            alert(error.response.data.message);
            console.log("ERROR", error);
          })
      this.showDeletePopup = false;
      this.workbookToDelete = null;
      this.filterWorkbooks();
    },
    cancelDelete() {
      this.showDeletePopup = false;
      this.workbookToDelete = null;
    },
    filterWorkbooks() {
      this.filteredWorkbooks = this.workbooks.filter(w =>
          w.title.toLowerCase().includes(this.searchQuery.toLowerCase())
      );
      this.sortWorkbooks(this.sortOrder);
    },
    toggleSortDropdown() {
      this.showSortDropdown = !this.showSortDropdown;
    },
    sortWorkbooks(order) {
      this.sortOrder = order;
      switch(order) {
        case 'newest':
          this.filteredWorkbooks.sort((a, b) => new Date(b.createDate) - new Date(a.createDate));
          break;
        case 'oldest':
          this.filteredWorkbooks.sort((a, b) => new Date(a.createDate) - new Date(b.createDate));
          break;
        case 'alphabetical':
          this.filteredWorkbooks.sort((a, b) => a.title.localeCompare(b.title));
          break;
      }
      this.showSortDropdown = false;
    },
    formatDate(dateString) {
      return dayjs(dateString).format('YYYY년 MM월 DD일 HH:mm');
    },
    showSharePopup(workbook){
      this.shareWorkbookDetail = {
        workbookId: workbook.id,
        title: workbook.title,
        content: workbook.content,

      };
      this.showShareWorkbookPopup = true;
    },
    cancelShare(){
      this.showShareWorkbookPopup = false;
    },
    shareWorkbook(){
      const headers = {
        'Authorization': this.token
      }
      if(confirm("공유 문제집에 등록하시겠습니까?")){
        axios.post(`/api/sharedWorkbook/create`,
            this.shareWorkbookDetail,
            {headers})
            .then((res) => {
              console.log(res, "등록");
              alert("등록되었습니다.");
              this.showShareWorkbookPopup = false;
            })
            .catch((err) => {
              console.log(err, "ERROR");
              alert("ERROR!");
            })
      }
    },
  },


}
</script>

<style scoped>
@import url("https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css");
body, html {
  margin: 0;
  padding: 0;
  height: 100%;
  font-family: 'Noto Sans KR', sans-serif;
}
/** slide fade **/
@keyframes slideInFade {
  0% {
    opacity: 0;
    transform: translateY(-20%);
  }
  100% {
    opacity: 1;
    transform: translateY(0);
  }
}
.slide-in-fade {
  display: inline-block;
  animation: slideInFade 0.9s cubic-bezier(0.5, 0.01, 0.115, 0.5);
}
.main-page {
  background: linear-gradient(rgba(255,244,255,0.05) 60%, rgba(232,221,0,0.23));
  color: #191f28;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}



.title {
  max-width: 1200px;
  margin: 80px auto 0px;
  padding: 10px;
}

.content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem;
  flex: 1;
}



.action-btn:hover {
  opacity: 1 !important;
}


h3 {
  margin-top: 0;
  margin-bottom: 10px;
}

p {
  margin: 5px 0;
}


.action-btn {
  background: none;
  border: none;
  cursor: pointer;
  font-size: 1rem;
  color: #1B2A49;
  opacity: 0;
  transition: opacity 0.3s, color 0.3s;
}

input, textarea {
  width: 100%;
  margin: 5px 0;
  padding: 10px;
  box-sizing: border-box;
}


.delete-btn {
  position: absolute;
  background: none;
  border: none;
  cursor: pointer;
  font-size: 1rem;
  color: #1B2A49;
  opacity: 0;
  transition: opacity 0.3s, color 0.3s;
}



.delete-btn:hover {
  opacity: 1;
  color: #ff0000;
}

.add-workbook-btn {
  position: fixed;
  bottom: 30px;
  right: 30px;
  background-color: #FFD700;
  color: #191f28;
  border: none;
  border-radius: 24px;
  padding: 12px 24px;
  font-size: 16px;
  font-weight: bold;
  cursor: pointer;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
  transition: all 0.3s ease;
}
.add-workbook-btn:hover {
  background-color: #FFC000;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
}

.add-workbook-btn .plus-icon {
  font-size: 24px;
}

.add-workbook-btn span:last-child {
  display: none;
}

@media (min-width: 768px) {
  .add-workbook-btn {
    width: auto;
    height: auto;
    border-radius: 30px;
    padding: 10px 20px;
  }

  .add-workbook-btn .plus-icon {
    margin-right: 10px;
  }

  .add-workbook-btn span:last-child {
    display: inline;
  }
}
.plus-icon {
  font-size: 3rem;
  color: #1B2A49;
}

h1, h2 {
  color: #1B2A49;
}

h1::after, h2::after, h3::after {
  content: "";
  display: block;
  width: 50px;
  height: 3px;
  background-color: #FFD700;
  margin-top: 10px;
}

.search-sort-container {
  display: flex;
  justify-content: center;
  margin-bottom: 1rem;
  gap: 20px;
}

.search-sort-container input {
  flex-grow: 1;
  min-width: 200px;
  max-width: 500px; /* 최대 너비를 고정 */
  box-sizing: border-box;
  padding: 0.5rem;
  border-radius: 4px;
  border: 1px solid #ccc;

}

.sort-dropdown {
  position: relative;
  flex-shrink: 0;

}

.sort-dropdown button {
  padding: 0.5rem 1rem;
  background-color: #FFD700;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.dropdown-content {
  position: absolute;
  background-color: #f9f9f9;
  min-width: 160px;
  box-shadow: 0px 8px 16px 0px rgba(0,0,0,0.2);
  z-index: 1;
}

.dropdown-content a {
  color: black;
  padding: 12px 16px;
  text-decoration: none;
  display: block;
}

.dropdown-content a:hover {
  background-color: #f1f1f1;
}




a{
  text-decoration: none;
  color: inherit;
}

/* 페이드 애니메이션 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease, transform 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
  transform: translateY(-20px);
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
/** **/

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

</style>