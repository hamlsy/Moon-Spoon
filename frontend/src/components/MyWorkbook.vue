<template>
  <div class="main-page">
    <nav class="navbar">
      <div class="navbar-brand"><router-link to="/mainPage">Moon-Spoon</router-link></div>
      <ul class="navbar-menu">
        <li><router-link to="/mainPage">홈</router-link></li>
        <li><router-link to="/user/login" v-if="!isLogin">로그인</router-link></li>
        <li v-if="isLogin"><a @click="logout">로그아웃</a></li>
        <li><router-link to="/user/signup">회원가입</router-link></li>
        <li><a @click="notValid">프로필</a></li>
      </ul>
    </nav>
    <div class="title">
      <h1 style="height: 50px">내 문제집</h1>
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

      <div class="workbook-container">
        <div v-for="workbook in filteredWorkbooks" :key="workbook.id" class="workbook-card">
          <div v-if="updateIndex !== workbook.id" @click="goWorkbookDetail(workbook.id)">
            <button class="edit-btn" @click.stop="startUpdate(workbook.id)">
              <i class="fas fa-edit"></i>
            </button>
            <button class="delete-btn" @click="confirmDelete(workbook.id)">
              <i class="fas fa-trash"></i>
            </button>
              <h3>{{ workbook.title }}</h3>
              <p>{{ workbook.content }}</p>
              <p>문제 수: {{ workbook.problemCount }}</p>
              <p>생성일: {{ formatDate(workbook.createDate) }}</p>
              <p v-if="workbook.updateDate">수정일: {{ formatDate(workbook.updateDate) }}</p>
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
        <div class="workbook-card add-workbook" @click="showAddWorkbookPopup">
          <span class="plus-icon">+</span>
          <p>새 문제집 추가</p>
        </div>
      </div>
    </main>

    <footer class="footer">
      <p>&copy; 2024 Moon-Spoon. GitHub: https://github.com/hamlsy</p>
    </footer>

    <!-- 새 문제집 추가 팝업 -->
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

    <!-- 삭제 확인 팝업 -->
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
      newWorkbook: { title: '', content: '' },
      workbookToDelete: null,
      searchQuery: '',
      filteredWorkbooks: [],
      showSortDropdown: false,
      sortOrder: 'newest',
      isLogin: false,
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
    saveUpdate(workbook) {
      //axios
      const headers = {
        'Authorization': this.token
      }

      axios.post(`/workbook/update/${workbook.id}`,
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
    checkLogin(){
      this.isLogin = !!localStorage.getItem('token');
    },
    logout(){
      alert("로그아웃 되었습니다.");
      localStorage.removeItem("token");
      this.$router.push("/mainPage");
    },
    goWorkbookDetail(workbookId){
      this.$router.push(`/workbookDetail/${workbookId}`);
    },
    getWorkbook(){
      const headers = {
        'Authorization': this.token
      }
      axios.get("/workbook/all", {headers})
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
    navigateTo(page) {
      console.log('Navigating to:', page);
    },
    showAddWorkbookPopup() {
      this.showAddPopup = true;
    },
    addWorkbook() {
      const headers = {
        'Authorization': this.token
      }
      axios.post("/workbook/create",
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
      axios.delete("/workbook/delete/" + this.workbookToDelete, {headers})
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
    }
  },

}
</script>

<style scoped>
@import url("https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css");
body, html {
  margin: 0;
  padding: 0;
  height: 100%;
}

.main-page {
  font-family: 'Arial', sans-serif;
  line-height: 1.6;
  color: #333;
  background-color: #FFFAF0;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.navbar {
  background-color: #1B2A49;
  color: #fff;
  padding: 0.1rem 2rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
}

.navbar-brand {
  font-size: 1.5rem;
  font-weight: bold;
  color: #FFD700;

}

.navbar-brand::after {
  content: "🥄";
  margin-right: 5px;
}

.navbar-menu {
  list-style-type: none;
  display: flex;
}

.navbar-menu li {
  margin-left: 1rem;
}

.navbar-menu a {
  color: #fff;
  text-decoration: none;
  transition: color 0.3s;
}

.navbar-menu a:hover {
  color: #FFD700;
}

.content {
  max-width: 1200px;
  margin: 0px auto 100px;
  flex: 1;
  overflow-y: auto;

}
.title {
  max-width: 1200px;
  margin: 80px auto 0px;
  padding: 10px;

  display: flex;

}
.workbook-container {
  display: flex;
  flex-wrap: wrap;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 20px;
  margin-top: 2rem;

}

.workbook-card {
  position: relative;
  background-color: #FFFFFF;
  border-radius: 8px;
  padding: 1.5rem;
  box-shadow: 0 2px 5px rgba(0,0,0,0.1);
  border-left: 4px solid #FFD700;
  transition: all 0.3s;
  min-height: 200px;
  width: 300px;
  overflow: hidden;
}

.edit-input,
.edit-textarea {
  width: 100%;
  padding: 0.5rem;
  margin-bottom: 0.5rem;
  border: 1px solid #ccc;
  border-radius: 8px; /* 둥근 모서리 추가 */
  font-size: 1rem;
  box-sizing: border-box;
  transition: border-color 0.3s, box-shadow 0.3s;
}
.edit-btn {
  position: absolute;
  top: 10px;
  right: 40px;
  background: none;
  border: none;
  cursor: pointer;
  font-size: 1rem;
  color: #1B2A49;
  opacity: 0;
  transition: opacity 0.3s, color 0.3s;
}
.workbook-card:hover .edit-btn {
  opacity: 0.7;
}
.edit-btn:hover {
  opacity: 1;
  color: navy;
}

input, textarea {
  width: 100%;
  margin: 5px 0;
  padding: 10px;
  box-sizing: border-box;
}

.popup-buttons {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.popup-buttons button {
  padding: 10px 20px;
  background-color: #1B2A49;
  border-radius: 6px;
  color: white;
  border: none;
  cursor: pointer;
  transition: opacity 0.3s, color 0.3s;
}

.popup-buttons button:hover {
  background-color: rgba(120,119,2,0.9);
  opacity: 1;
}

.delete-btn {
  position: absolute;
  top: 10px;
  right: 10px;
  background: none;
  border: none;
  cursor: pointer;
  font-size: 1rem;
  color: #1B2A49;
  opacity: 0;
  transition: opacity 0.3s, color 0.3s;
}

.workbook-card:hover .delete-btn {
  opacity: 0.7;
}

.delete-btn:hover {
  opacity: 1;
  color: #ff0000;
}

.add-workbook {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  cursor: pointer;
}

.plus-icon {
  font-size: 3rem;
  color: #1B2A49;
}

h1, h2, h3 {
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

.workbook-card:hover {
  background-color: #FFD700;
  box-shadow: 0 4px 8px rgba(0,0,0,0.15);
}

.footer {
  background-color: #1B2A49;
  color: #fff;
  text-align: center;
  padding: 0.1rem;
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
}

.popup-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1001;
}

.popup {
  background-color: #FFFFFF;
  padding: 2rem;
  border-radius: 8px;
  width: 300px;
}

.popup input, .popup textarea {
  width: 100%;
  margin-bottom: 1rem;
  padding: 0.5rem;
}

.popup-buttons {
  display: flex;
  justify-content: space-between;
}

.popup button {
  padding: 0.5rem 1rem;
  background-color: #FFD700;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.popup button:hover {
  background-color: #FFC000;
}
a{
  text-decoration: none;
  color: inherit;
}


</style>