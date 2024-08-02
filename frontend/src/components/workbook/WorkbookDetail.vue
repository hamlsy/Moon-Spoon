<template>
  <div class="main-page">
    <main class="content">
      <router-link to="/myWorkbook" class="back-button">
        <i class="fas fa-arrow-left"></i> 뒤로가기
      </router-link>
      <section class="hero">
        <h1 class="main-title slide-in-fade">{{ workbookTitle }}</h1>
      </section>


      <section class="features">
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
            <div v-if="updateIndex !== index" @click="showProblemDetail(problem, $event)">
              <div class="problem-content">
                <div class="problem-main">
                  <h3>P{{ problem.displayNumber }}</h3>
                  <span class="problem-text"><strong>문제:</strong> {{ truncateText(problem.question) }}</span>
                </div>
                <div class="problem-info">
                  <span><strong>정답률:</strong> {{ problem.correctRate }}%</span>
                  <span><strong>생성일:</strong> {{ formatDate(problem.createDate) }}</span>
                </div>
              </div>
              <div class="problem-actions">
                <button @click.stop="startUpdate(index)" class="action-btn edit-btn"><i class="fas fa-edit"></i></button>
                <button @click.stop="confirmDelete(problem.id)" class="action-btn delete-btn"><i class="fas fa-trash"></i></button>
              </div>
            </div>
            <div v-else class="problem-edit-form">
              <input v-model="updateProblem.question" placeholder="문제" />
              <textarea v-model="updateProblem.solution" placeholder="답"></textarea>
              <button @click="cancelUpdate" class="cancel-btn">취소</button>
              <button @click="saveUpdate" class="save-btn">저장</button>
            </div>
          </div>
        </div>
      </section>
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
    </main>

    <footer class="footer">
      <p>&copy; 2024 Moon-Spoon. <a href="https://github.com/hamlsy" target="_blank" rel="noopener noreferrer">GitHub</a></p>
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
      workbookId: this.$route.fullPath.split("/").pop(),
      showDetailPopup: false,
      selectedProblem: null,

    }
  },
  methods: {
    notValid(){
      alert("아직 구현되지 않은 기능입니다.");
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
  /** background: linear-gradient(rgba(255,244,255,0.05) 40%, rgba(232,221,0,0.53)); **/
  background: rgba(255,244,255,0.35);
  /** background: white; **/
  color: #191f28;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}


.content {
  max-width: 1800px;
/**  margin: 0 auto; **/
  padding: 2rem;
  flex: 1;
}
.action-btn {
  background: none;
  border: none;
  cursor: pointer;
  font-size: 1rem;
  color: #1B2A49;
  opacity: 0.6;
  transition: opacity 0.3s ease;

}
.action-btn:hover {
  opacity: 1;
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
  margin-top: 1px;
}

.search-sort-container {
  display: flex;
  justify-content: space-between;
  margin-bottom: 1rem;
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
  gap: 1rem;
  margin-bottom: 2rem;
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


.start-test-btn {
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
.start-test-btn:hover{
  background-color: #FFC000;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
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

problem-main {
  flex-grow: 1;
  display: flex;
  align-items: center;
  gap: 1rem;
}
.problem-info {
  display: flex;
  gap: 1rem;
  font-size: 0.9em;
  color: #666;
}

problem-content{
  display: flex;
  justify-content: space-between;
  height: 100%;
}

.problem-detail-header h3 {
  margin-top: 0;
  text-align: left;
  margin-bottom: 20px;
  color: #1B2A49;
}
.problem-main h3 {
  min-width: 40px;
  margin: 0;
}
.problem-detail-content {
  text-align: left;
}
.problem-detail-content p {
  word-wrap: break-word;
  margin-bottom: 10px;
}

.problem-list {
  /** display: flex; **/
  /** padding: 10px; /* 컨테이너 내부 여백  **/
  /** flex-direction: column; **/
  /** gap: 0.5rem;
  margin-top: 2rem; **/

  display: grid; /* Grid 레이아웃을 활성화 */
  grid-template-columns: repeat(2, 1fr); /* 두 개의 동일한 너비의 열을 생성 */
  gap: 10px; /* 열과 열 사이의 간격 설정 */
  width: 100%; /* 컨테이너의 너비를 100%로 설정 (부모 요소에 따라 조정 가능) */
  box-sizing: border-box; /* 패딩 및 보더를 포함한 전체 크기 계산 */
}

.problem-item {
  /** flex: 1; **/
  width: 100%;
  background-color: white;
  border-radius: 10px;
  padding: 0.75rem 1rem;
  transition: all 0.3s ease;
  /** position: relative; **/
  box-sizing: border-box;
  border: 1px solid skyblue;

}

.problem-item:hover {
  background-color: #e0e0e0;
  transform: translateX(5px);
}

.truncate {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 100%;
}
.problem-actions {
  position: absolute;
  top: 0.5rem;
  right: 0.5rem;
  display: flex;
  gap: 0.5rem;
}
.problem-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.problem-text {
  flex-grow: 1;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
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

.edit-btn {
  left: -50px;
}
.delete-btn{
  left: -20px;
}

.edit-btn:hover {
  color: #4CAF50;
}

.delete-btn:hover {
  color: #F44336;
}
.hero {
  text-align: center;
  padding: 4rem 0;
  border-radius: 12px;

}
.main-title {
  font-size: 2.5rem;
  margin-bottom: 1rem;
  color: black;
}
</style>