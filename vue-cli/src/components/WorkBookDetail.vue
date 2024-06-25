<template>
  <div class="main-page">
    <nav class="navbar">
      <div class="navbar-brand"><router-link to="/mainPage">Moon-Spoon</router-link></div>
      <ul class="navbar-menu">
        <li><router-link to="/mainPage">홈</router-link></li>
        <li><router-link to="/user/login">로그인</router-link></li>
        <li><router-link to="/user/signup">회원가입</router-link></li>
        <li><a href="#" >로그아웃</a></li>
        <li><a href="#" >프로필</a></li>
      </ul>
    </nav>

    <div class="content">
      <router-link to="/myWorkbooks" class="back-button">
        <i class="fas fa-arrow-left"></i> 뒤로가기
      </router-link>

      <div class="title">
        <h1>{{ workbook.name }}</h1>
      </div>


      <div class="search-sort-container">
        <input v-model="searchQuery" placeholder="문제 검색" @input="filterQuestions" />
        <div class="sort-dropdown">
          <button @click="toggleSortDropdown">정렬 <i class="fas fa-caret-down"></i></button>
          <div v-if="showSortDropdown" class="dropdown-content">
            <a href="#" @click="sortQuestions('newest')">최신순</a>
            <a href="#" @click="sortQuestions('oldest')">오래된순</a>
            <a href="#" @click="sortQuestions('correctRateAsc')">정답률 낮은순</a>
            <a href="#" @click="sortQuestions('correctRateDesc')">정답률 높은순</a>
          </div>
        </div>
      </div>

      <div class="add-question-form">
        <input v-model="newQuestion.question" placeholder="문제를 입력하세요" />
        <textarea v-model="newQuestion.answer" placeholder="답을 입력하세요"></textarea>
        <button @click="addQuestion" class="add-btn">+</button>
      </div>

      <div class="question-list">
        <div v-for="(question, index) in filteredQuestions" :key="question.id" class="question-item">
          <div v-if="editingIndex !== index" class="question-content">
            <div class="question-actions">
              <button @click="startEditing(index)" class="edit-btn">수정</button>
              <button @click="confirmDelete(question.id)" class="delete-btn">삭제</button>
            </div>
            <h3>문제 {{ question.id }}</h3>
            <p>{{ question.question }}</p>
            <p><strong>답:</strong> {{ question.answer }}</p>
            <p><strong>정답률:</strong> {{ question.correctRate }}%</p>
          </div>
          <div v-else class="question-edit-form">
            <input v-model="editingQuestion.question" placeholder="문제" />
            <textarea v-model="editingQuestion.answer" placeholder="답"></textarea>
            <button @click="cancelEdit" class="cancel-btn">취소</button>
            <button @click="saveEdit" class="save-btn">저장</button>
          </div>
        </div>
      </div>

      <button @click="showTestPopup" class="start-test-btn">테스트 시작</button>
    </div>

    <footer class="footer">
      <p>&copy; 2024 Moon-Spoon. GitHub: https://github.com/hamlsy.</p>
    </footer>
    <!-- 테스트 시작 팝업 -->
    <div v-if="showPopup" class="popup-overlay" @click.self="cancelTest">
      <div class="popup">
        <h2>테스트 설정</h2>
        <div class="form-group">
          <label for="questionCount">
            문제 수:
            <button @click="setMaxQuestionCount" class="max-count-btn">최대</button>
          </label>
          <input id="questionCount" v-model.number="testSettings.questionCount" type="number" min="1" :max="questions.length" />
        </div>
        <div class="form-group checkbox-group">
          <label>
            <input type="checkbox" v-model="testSettings.isRandom" />
            <span>랜덤</span>
          </label>
        </div>
        <div class="form-group">
          <p>출제 순서:</p>
          <div class="radio-group">
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
          <button @click="deleteQuestion">삭제</button>
        </div>
      </div>
    </div>
  </div>


</template>

<script>
export default {
  name: 'WorkbookDetailPage',
  data() {
    return {
      workbook: { name: '수학 문제집' },
      questions: [
        { id: 1, question: '1 + 1은?', answer: '2', correctRate: 95 },
        { id: 2, question: '2 * 3은?', answer: '6', correctRate: 88 },
        { id: 3, question: '5 - 2는?', answer: '3', correctRate: 92 },
        { id: 4, question: '10 / 2는?', answer: '5', correctRate: 85 },
      ],
      newQuestion: { question: '', answer: '' },
      showPopup: false,
      testSettings: {
        questionCount: 1,
        isRandom: false,
        sortOrder: 'asc'
      },
      showDeletePopup: false,
      questionToDelete: null,
      editingIndex: null,
      editingQuestion: { question: '', answer: '' },
      searchQuery: '',
      filteredQuestions: [],
      showSortDropdown: false,
      sortOrder: 'newest'
    }
  },
  methods: {
    cancelDelete(){
      this.showDeletePopup = false;
      this.questionToDelete = null;
    },
    addQuestion() {
      if (this.newQuestion.question && this.newQuestion.answer) {
        const newId = Math.max(...this.questions.map(q => q.id)) + 1;
        this.questions.push({
          id: newId,
          ...this.newQuestion,
          correctRate: 0
        });
        this.newQuestion = { question: '', answer: '' };
        this.filterQuestions();
      }
    },
    startEditing(index) {
      this.editingIndex = index;
      this.editingQuestion = { ...this.filteredQuestions[index] };
    },
    saveEdit() {
      if (this.editingQuestion.question && this.editingQuestion.answer) {
        const index = this.questions.findIndex(q => q.id === this.editingQuestion.id);
        this.questions[index] = { ...this.editingQuestion };
        this.editingIndex = null;
        this.filterQuestions();
      }
    },
    confirmDelete(questionId) {
      this.questionToDelete = questionId;
      this.showDeletePopup = true;
    },
    cancelEdit(){
      this.editingIndex = null;
    },
    deleteQuestion() {
      if (this.questionToDelete) {
        this.questions = this.questions.filter(q => q.id !== this.questionToDelete);
        this.filterQuestions();
        this.showDeletePopup = false;
        this.questionToDelete = null;
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
    },
    filterQuestions() {
      this.filteredQuestions = this.questions.filter(q =>
          q.question.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
          q.answer.toLowerCase().includes(this.searchQuery.toLowerCase())
      );
      this.sortQuestions(this.sortOrder);
    },
    toggleSortDropdown() {
      this.showSortDropdown = !this.showSortDropdown;
    },
    sortQuestions(order) {
      this.sortOrder = order;
      switch(order) {
        case 'newest':
          this.filteredQuestions.sort((a, b) => b.id - a.id);
          break;
        case 'oldest':
          this.filteredQuestions.sort((a, b) => a.id - b.id);
          break;
        case 'correctRateAsc':
          this.filteredQuestions.sort((a, b) => a.correctRate - b.correctRate);
          break;
        case 'correctRateDesc':
          this.filteredQuestions.sort((a, b) => b.correctRate - a.correctRate);
          break;
      }
      this.showSortDropdown = false;
    },
    setMaxQuestionCount() {
      this.testSettings.questionCount = this.questions.length;
    }
  },
  mounted() {
    this.filterQuestions();
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
  margin: 0px 100px 100px;
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

.add-question-form {
  display: flex;
  margin-bottom: 2rem;
  gap: 10px;
}

.add-question-form input,
.add-question-form textarea {
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

.question-list {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}

.question-item {
  background-color: #FFFFFF;
  border-radius: 8px;
  box-shadow: 0 2px 5px rgba(0,0,0,0.1);
  border-left: 4px solid #FFD700;
  padding: 1rem;
  transition: all 0.3s;
  position: relative;
}

.question-actions {
  position: absolute;
  top: 10px;
  right: 10px;
  display: flex;
  gap: 5px;
}

.question-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 10px rgba(0,0,0,0.1);
}

.question-edit-form input,
.question-edit-form textarea {
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
</style>