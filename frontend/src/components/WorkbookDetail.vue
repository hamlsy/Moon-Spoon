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

    <div class="content">
      <router-link to="/myWorkbook" class="back-button">
        <i class="fas fa-arrow-left"></i> 뒤로가기
      </router-link>

      <div class="title">
        <h1>{{ workbookTitle }}</h1>
      </div>


      <div class="search-sort-container">
        <input v-model="searchQuery" placeholder="문제 검색" @input="filterproblems" />
        <div class="sort-dropdown">
          <button @click="toggleSortDropdown">{{ sortValue }}<i class="fas fa-caret-down"></i></button>
          <div v-if="showSortDropdown" class="dropdown-content">
            <a href="#" @click="sortproblems('newest')">최신순</a>
            <a href="#" @click="sortproblems('oldest')">오래된순</a>
            <a href="#" @click="sortproblems('correctRateAsc')">정답률 낮은순</a>
            <a href="#" @click="sortproblems('correctRateDesc')">정답률 높은순</a>
          </div>
        </div>
      </div>

      <div class="add-problem-form">
        <input v-model="newproblem.question" placeholder="문제를 입력하세요" />
        <textarea v-model="newproblem.solution" placeholder="답을 입력하세요"></textarea>
        <button @click="addproblem" class="add-btn">+</button>
      </div>

      <div class="problem-list">
        <div v-for="(problem, index) in filteredproblems" :key="problem.id" class="problem-item">
          <div v-if="updateIndex !== index" class="problem-content" @click="showProblemDetail(problem, $event)">
            <div class="problem-actions">
              <button @click="startUpdate(index)" class="icon-btn edit-btn"><i class="fas fa-edit"></i></button>
              <button @click="confirmDelete(problem.id)" class="icon-btn delete-btn"><i class="fas fa-trash"></i></button>
            </div>
            <h3>문제 {{ problem.displayNumber }}</h3>
            <p><span class="problem-text">{{ truncateText(problem.question) }} </span></p>
            <p><strong>답:</strong> <span class="problem-text">{{ truncateText(problem.solution) }} </span></p>
            <p><strong>정답률:</strong> {{ problem.correctRate }}%</p>
            <p><strong>생성일:</strong> {{ formatDate(problem.createDate) }}</p>
          </div>
          <div v-else class="problem-edit-form">
            <input v-model="updateProblem.question" placeholder="문제" />
            <textarea v-model="updateProblem.solution" placeholder="답"></textarea>
            <button @click="cancelUpdate" class="cancel-btn">취소</button>
            <button @click="saveUpdate" class="save-btn">저장</button>
          </div>
        </div>
      </div>
      <!-- 문제 상세 팝업 -->
      <div v-if="showDetailPopup" class="popup-overlay" @click="closeDetailPopup">
        <div class="popup problem-detail-popup" @click.stop>
          <button @click="closeDetailPopup" class="close-btn"><i class="fas fa-times"></i></button>
          <h2>문제 상세</h2>
          <h3>문제 {{ selectedProblem.displayNumber }}</h3>
          <div class="problem-detail-content">
            <p><strong>문제:</strong> {{ selectedProblem.question }}</p>
            <p><strong>답:</strong> {{ selectedProblem.solution }}</p>
            <p><strong>정답률:</strong> {{ selectedProblem.correctRate }}%</p>
            <p><strong>생성일:</strong> {{ formatDate(selectedProblem.createdAt) }}</p>
          </div>
        </div>
      </div>

      <button @click="showTestPopup" class="start-test-btn">테스트 시작</button>
    </div>

    <footer class="footer">
      <p>&copy; 2024 Moon-Spoon. GitHub: https://github.com/hamlsy</p>
    </footer>
    <!-- 테스트 시작 팝업 -->
    <div v-if="showPopup" class="popup-overlay" @click.self="cancelTest">
      <div class="popup">
        <h2>테스트 설정</h2>
        <div class="form-group">
          <label for="problemCount">
            문제 수:
            <button @click="setMaxproblemCount" class="max-count-btn">최대</button>
          </label>
          <input id="problemCount" v-model.number="testSettings.problemCount" type="number" min="1" :max="problems.length" />
        </div>
        <div class="form-group checkbox-group">
          <label>
            <input type="checkbox" v-model="testSettings.random" />
            <span>랜덤</span>
          </label>
        </div>
        <div class="form-group">
          <p>출제 순서:</p>
          <div class="radio-group">
            <label>
              <input type="radio" v-model="testSettings.order" value="none" />
              <span>기본 값</span>
            </label>
            <label>
              <input type="radio" v-model="testSettings.order" value="correctRateAsc" />
              <span>정답률 낮은 순</span>
            </label>
            <label>
              <input type="radio" v-model="testSettings.order" value="correctRateDesc" />
              <span>정답률 높은 순</span>
            </label>
            <label>
              <input type="radio" v-model="testSettings.order" value="asc" />
              <span>오름차순</span>
            </label>
            <label>
              <input type="radio" v-model="testSettings.order" value="desc" />
              <span>내림차순</span>
            </label>
          </div>
        </div>
        <div class="popup-buttons">
          <button @click="cancelTest">취소</button>
          <button @click="startTest">테스트 시작</button>
        </div>
      </div>
    </div>
    <!-- 삭제 확인 팝업 -->
    <div v-if="showDeletePopup" class="popup-overlay" @click.self="cancelDelete">
      <div class="popup">
        <h2>문제집 삭제</h2>
        <p>정말로 이 문제를 삭제하시겠습니까?</p>
        <div class="popup-buttons">
          <button @click="cancelDelete">취소</button>
          <button @click="deleteproblem">삭제</button>
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
      workbookTitle: "",
      problems: [],
      newproblem: { question: '', solution: '' },
      showPopup: false,
      testSettings: {
        problemCount: 1,
        random: false,
        sortOrder: 'asc'
      },
      isLogin: false,
      showDeletePopup: false,
      problemToDelete: null,
      updateIndex: null,
      updateProblem: { question: '', solution: '' },
      searchQuery: '',
      filteredproblems: [],
      showSortDropdown: false,
      sortOrder: 'newest',
      sortValue: '최신순',
      token: localStorage.getItem('token'),
      workbookId: "",
      showDetailPopup: false,
      selectedProblem: null,

    }
  },
  methods: {
    notValid(){
      alert("아직 구현되지 않은 기능입니다.");
    },
    checkLogin(){
      this.isLogin = !!localStorage.getItem('token');
    },
    logout(){
      alert("로그아웃 되었습니다.");
      localStorage.removeItem("token");
      this.$router.go(0);
    },
    getProblems(){
      const headers = {
        'Authorization': this.token
      };
      this.workbookId = this.$route.fullPath.split("/").pop();
      axios.get(`/api/workbook/${this.workbookId}/problem/all`, {headers})
          .then((res) => {
            this.problems = res.data.problems.map((problem, index) => ({
              ...problem, displayNumber: index + 1
            }));
            this.workbookTitle = res.data.workbookTitle;
            this.filterproblems();
          })
          .catch((error) => {
            if(error.response.data.message ===  "JWT token is expired"){
              console.log(error.response.data.message);
              alert("토큰이 만료되었습니다. 다시 로그인하세요.");
              localStorage.removeItem("token");
            }else{
              alert(error.response.data.message);
            }
            this.$router.push("/mainPage");
            console.log("ERROR", error);
          })
    },
    cancelDelete(){
      this.showDeletePopup = false;
      this.problemToDelete = null;
    },
    addproblem() {
      const headers = {
        'Authorization': this.token
      }
      if (this.newproblem.question && this.newproblem.solution) {
        axios.post(`/api/workbook/${this.workbookId}/problem/create`,
            {
              question: this.newproblem.question,
              solution: this.newproblem.solution
            },{headers})
            .then((res) => {
              this.$router.go(0);
              console.log("CREATE", res);
            })
            .catch((error) => {
              alert(error.data.response.message);
              this.$router.go(0);
              console.log("ERROR", error);
            })
        this.newproblem = { question: '', problem: '' };
        this.filterproblems();
      }
    },
    startUpdate(index) {
      this.updateProblem = { ...this.filteredproblems[index] };
      //해당 위치에서 수정창 열림
      this.updateIndex = index;
    },
    saveUpdate() {
      const headers = {
        'Authorization': this.token
      }
      if (this.updateProblem.question && this.updateProblem.solution) {
        axios.post(`/api/workbook/${this.workbookId}/problem/update/${this.updateProblem.id}`,
            {
              question: this.updateProblem.question,
              solution: this.updateProblem.solution
            }, {headers}
        ).then((res) => {
          console.log("UPDATE", this.updateProblem.id ,res);
        }).catch((error) => {
          alert(error.data.response.message);
          console.log("ERROR", error);
        })
        //창 닫기
        this.updateIndex = null;
        const index = this.problems.findIndex(q => q.id === this.updateProblem.id);
        this.problems[index] = { ...this.updateProblem };
        this.filterproblems();
      }
    },
    confirmDelete(problemId) {
      this.problemToDelete = problemId;
      this.showDeletePopup = true;
    },
    cancelUpdate(){
      this.updateIndex = null;
    },
    deleteproblem() {
      if (this.problemToDelete) {
        const headers = {
          'Authorization': this.token
        }
        axios.delete(`/api/workbook/${this.workbookId}/problem/delete/${this.problemToDelete}`, {headers})
            .then((res) => {
              alert("삭제되었습니다.");
              console.log("DELETE", res);
            })
            .catch((error) => {
              alert(error.data.response.message);
              console.log("ERROR", error);
            })
        this.problems = this.problems.filter(q => q.id !== this.problemToDelete);
        this.filterproblems();
        this.showDeletePopup = false;
        this.problemToDelete = null;
      }
    },
    showTestPopup() {
      this.showPopup = true;
    },
    cancelTest() {
      this.showPopup = false;
    },
    startTest() {
      console.log('Start test with settings:', this.testSettings);
      this.showPopup = false;
      this.$router.push({
        path: '/problemTest',
        query: {
          problemCount: this.testSettings.problemCount,
          random: this.testSettings.random,
          sortOrder: this.testSettings.sortOrder,
          workbookId: this.workbookId,
          workbookTitle: this.workbookTitle
        }
      })
    },
    filterproblems() {
      this.filteredproblems = this.problems.filter(q =>
          q.question.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
          q.solution.toLowerCase().includes(this.searchQuery.toLowerCase())
      );
      this.sortproblems(this.sortOrder);
    },
    toggleSortDropdown() {
      this.showSortDropdown = !this.showSortDropdown;
    },
    sortproblems(order) {
      this.sortOrder = order;
      switch(order) {
        case 'newest':
          this.sortValue = "최신순";
          this.filteredproblems.sort((a, b) => b.id - a.id);
          break;
        case 'oldest':
          this.sortValue = "오래된순";
          this.filteredproblems.sort((a, b) => a.id - b.id);
          break;
        case 'correctRateAsc':
          this.sortValue = "정답률 낮은 순";
          this.filteredproblems.sort((a, b) => a.correctRate - b.correctRate);
          break;
        case 'correctRateDesc':
          this.sortValue = "정답률 높은 순";
          this.filteredproblems.sort((a, b) => b.correctRate - a.correctRate);
          break;
      }
      this.showSortDropdown = false;
    },
    setMaxproblemCount() {
      this.testSettings.problemCount = this.problems.length;
    },
    truncateText(text, maxLength = 30) {
      return text.length > maxLength ? text.slice(0, maxLength) + '...' : text;
    },

    showProblemDetail(problem, event) {
      // 버튼 영역을 클릭한 경우 상세 팝업을 열지 않음
      if (event.target.closest('.problem-actions')) return;
      this.selectedProblem = problem;
      this.showDetailPopup = true;
    },

    closeDetailPopup() {
      this.showDetailPopup = false;
      this.selectedProblem = null;
    },
    formatDate(dateString) {
      return dayjs(dateString).format('YYYY년 MM월 DD일 HH:mm');
    },
  },
  mounted() {
    this.checkLogin();
    this.getProblems();
  }
}
</script>

<style scoped>
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
  padding: 20px;
  flex: 1;
  overflow-y: auto;
}

.title {
  max-width: 1200px;
  margin: 80px auto 0px;
  padding: 10px;
  display: flex;
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
  max-width: 500px;
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
  box-shadow: 0 4px 6px rgba(0,0,0,0.1);
  text-align: center;
}

.popup input, .popup textarea {
  width: 100%;
  margin-bottom: 1rem;
  padding: 0.5rem;
}

.popup-buttons {
  display: flex;
  justify-content: space-around;
  margin-top: 1rem;
}

.popup button {
  padding: 0.5rem 1rem;
  background-color: #FFD700;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.popup-buttons button {
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.popup button:hover {
  background-color: #FFC000;
}
.popup-buttons button:first-child {
  background-color: #4CAF50;
  color: white;
}

.popup-buttons button:last-child {
  background-color: #f44336;
  color: white;
}

.popup-buttons button:hover {
  opacity: 0.8;
}
a {
  text-decoration: none;
  color: inherit;
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

.back-button:hover {
  color: #FFD700;
}

.back-button i {
  margin-right: 0.5rem;
}

.add-problem-form {
  display: flex;
  margin-bottom: 2rem;
  gap: 10px;
}

.add-problem-form input,
.add-problem-form textarea {
  flex-grow: 1;
  padding: 0.5rem;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.add-btn {
  background-color: #FFD700;
  border: none;
  border-radius: 4px;
  padding: 0.5rem 1rem;
  cursor: pointer;
}

.problem-list {
  display: grid;
  /** grid-template-columns: repeat(auto-fit, minmax(300px, 1fr)); **/
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  max-height: 70vh;
  overflow-y: auto;
  padding-right: 15px;
}

.problem-item {
  background-color: #FFFFFF;
  border-radius: 8px;
  box-shadow: 0 2px 5px rgba(0,0,0,0.1);
  border-left: 4px solid #FFD700;
  padding: 1rem;
  transition: all 0.3s;
  position: relative;
  height: 250px; /* 고정 높이 설정 */
  min-height: 200px;
  overflow: hidden;
  cursor: pointer;
}
.truncate {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 100%;
}
.problem-actions {
  position: absolute;
  top: 10px;
  right: 10px;
  display: flex;
  gap: 5px;
}
.problem-text {
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
  word-break: break-word;
}
.problem-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 10px rgba(0,0,0,0.1);
}

.problem-edit-form input,
.problem-edit-form textarea {
  width: 100%;
  margin-bottom: 0.5rem;
  padding: 0.5rem;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.edit-btn, .save-btn, .delete-btn, .cancel-btn {
  padding: 5px 10px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.edit-btn, .save-btn, .cancel-btn {
  background-color: #4CAF50;
  color: white;
}

.delete-btn {
  background-color: #f44336;
  color: white;
}

.start-test-btn {
  position: fixed;
  bottom: 80px;
  right: 20px;
  background-color: #FFD700;
  color: #1B2A49;
  border: none;
  border-radius: 50px;
  padding: 1rem 2rem;
  font-size: 1.2rem;
  cursor: pointer;
  box-shadow: 0 4px 6px rgba(0,0,0,0.1);
}

.popup {
  width: 400px;
}

.form-group {
  margin-bottom: 1rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
}

.form-group input[type="number"],
.form-group select {
  width: 100%;
  padding: 0.5rem;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.max-count-btn {
  margin-left: 10px;
  padding: 2px 5px;
  background-color: #FFD700;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.8em;
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

.radio-group {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.icon-btn {
  background: none;
  border: none;
  cursor: pointer;
  font-size: 1rem;
  color: #1B2A49;
  transition: color 0.3s;
}

.icon-btn:hover {
  color: #FFD700;
}

.problem-detail-popup {
  position: relative;
  width: 80%;
  max-width: 600px;
  max-height: 80vh;
  overflow-y: auto;
  padding: 40px 20px 20px; /* 상단 패딩 증가 */
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
}

.problem-detail-header h3 {
  margin-top: 0;
  text-align: left;
  margin-bottom: 20px;
  color: #1B2A49;
}
.problem-detail-content {
  text-align: left;
}
.problem-detail-content p {
  word-wrap: break-word;
  margin-bottom: 10px;
}
.close-btn {
  position: absolute;
  top: 10px;
  right: 10px;
  background: none;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
  color: #333;
  padding: 5px;
  z-index: 1;
}
</style>