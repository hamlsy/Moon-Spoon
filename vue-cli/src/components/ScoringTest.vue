<template>
  <div class="test-page" >
    <!-- 왼쪽 사이드바 -->
    <div class="sidebar">
      <button class="exit-btn" @click="showExitPopup = true">나가기</button>
      <div class="problem-list" >
        <div
            v-for="(problem, index) in problems"
            :key="index"
            :class="['problem-item', {
            'correct': problem.result === 'correct',
            'incorrect': problem.result === 'incorrect',
            // 'unanswered': !problem.result
          }]"
            @click="goToproblem(index)"
        >
          <span class="problem-number">{{ index + 1 }}</span>
          <span class="answer-preview">
            {{ getproblemPreview(index) }}
          </span>
          <span class="result-preview" :class="problem.result">
            {{ problem.result === 'correct' ? '정답' : problem.result === 'incorrect' ? '오답' : '' }}
          </span>
        </div>
      </div>
    </div>

    <!-- 메인 콘텐츠 -->
    <div class="main-content">
      <div class="problem-header">
        <h2>문제 {{ currentproblemIndex + 1 }}</h2>
        <span class="correct-rate" v-if="currentproblem.correctRate !== undefined">
          정답률: {{ currentproblem.correctRate }}%
        </span>
      </div>
      <p>{{ currentproblem.problem }}</p>
      <div class="answer-section">
        <p><strong>문제:</strong> {{ currentproblem.question }}</p>
        <p><strong>정답:</strong> {{ currentproblem.solution }}</p>
        <p><strong>작성한 답안:</strong> {{ currentproblem.input }}</p>
        <div class="grading-cards">
          <div
              @click="gradeproblem('correct')"
              :class="['grade-card', 'correct-card', { 'selected': problems[currentproblemIndex].result === 'correct' }]"
          >
            <div class="card-content">
              <span class="card-icon">✓</span>
              <span class="card-text">정답</span>
            </div>
          </div>
          <div
              @click="gradeproblem('incorrect')"
              :class="['grade-card', 'incorrect-card', { 'selected': problems[currentproblemIndex].result === 'incorrect' }]"
          >
            <div class="card-content">
              <span class="card-icon">✗</span>
              <span class="card-text">오답</span>
            </div>
        </div>
        </div>
      </div>
      <div class="navigation-buttons">
        <button
            v-if="currentproblemIndex > 0"
            @click="goToPreviousproblem"
        >
          이전
        </button>
        <button
            v-if="currentproblemIndex < problems.length - 1"
            @click="goToNextproblem"
        >
          다음
        </button>
      </div>
    </div>
    <!-- 제출 버튼 -->
    <button class="submit-btn" @click="showSubmitPopup = true">결과 제출</button>

    <!-- 나가기 확인 팝업 -->
    <div v-if="showExitPopup" class="popup-overlay">
      <div class="popup">
        <h3>채점을 종료하시겠습니까?</h3>
        <p>저장되지 않은 채점 결과는 사라집니다.</p>
        <div class="popup-buttons">
          <button @click="showExitPopup = false">취소</button>
          <button @click="exitGrading">확인</button>
        </div>
      </div>
    </div>

    <!-- 제출 확인 팝업 -->
    <div v-if="showSubmitPopup" class="popup-overlay">
      <div class="popup">
        <h3>채점 결과를 제출하시겠습니까?</h3>
        <p>제출 후에는 결과를 수정할 수 없습니다.</p>
        <div class="popup-buttons">
          <button @click="showSubmitPopup = false">취소</button>
          <button @click="submitResults">확인</button>
        </div>
      </div>
    </div>

    <!-- 결과 팝업 -->
    <div v-if="showResultPopup" class="popup-overlay">
      <div class="popup result-popup">
        <h3>채점 결과</h3>
        <p>문제집: {{ workbookTitle }}</p>
        <p>맞힌 개수: {{ resultInfo.correctCount }}</p>
        <p>틀린 개수: {{ resultInfo.incorrectCount }}</p>
        <p>점수: {{ resultInfo.score }}점</p>
        <div class="popup-buttons">
          <button @click="finishGrading">확인</button>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      problems: [],
      showResultPopup: false,
      resultInfo:{
        correctCount: 0,
        incorrectCount: 0,
        score: 0,
      },
      currentproblemIndex: 0,
      showExitPopup: false,
      showSubmitPopup: false,
      token: localStorage.getItem('token'),
      workbookId : this.$route.query.workbookId,
      workbookTitle : this.$route.query.workbookTitle
    }
  },
  computed: {
    currentproblem() {
      return this.problems[this.currentproblemIndex];
    },
    correctAnswerRate() {
      const correctCount = this.problems.result.filter(result => result === 'correct').length;
      return (correctCount / this.problems.length * 100).toFixed(2) + '%';
    }
  },
  created() {
    this.fetchResults(); // 컴포넌트 생성 시 결과 가져오기
  },
  methods: {
    getproblemPreview(index) {
      const problem = this.problems[index].question;
      return problem.length > 13 ? problem.substring(0, 13) + '...' : problem;
    },
    finishGrading() {
      this.showResultPopup = false;
      this.$router.push(`/workBookDetail/${this.workbookId}`);
    },
    goToproblem(index) {
      this.currentproblemIndex = index;
    },
    goToPreviousproblem() {
      if (this.currentproblemIndex > 0) {
        this.currentproblemIndex--;
      }
    },
    goToNextproblem() {
      if (this.currentproblemIndex < this.problems.length - 1) {
        this.currentproblemIndex++;
      }
    },
    gradeproblem(result) {
      this.problems[this.currentproblemIndex].result = result;
    },
    exitGrading() {
      // 채점 종료 로직
      this.$router.push(`/workBookDetail/${this.workbookId}`);
    },
    submitResults() {
      // 채점 결과 제출 로직
      const headers = {
        'Authorization': this.token
      };
      axios.post(`/workbook/${this.workbookId}/problem/submitTestResult`,
          this.problems,
          {headers})
          .then((res) => {
            this.resultInfo.correctCount = res.data.correctCount;
            this.resultInfo.incorrectCount = res.data.incorrectCount;
            this.resultInfo.score = res.data.score;
            this.showSubmitPopup = false;
            this.showResultPopup = true;

            console.log(res);
          })
          .catch((error) => {
            alert(error.data.response.message);
            console.log(error);
          })
      console.log("Grading results submitted:", this.problems);
      console.log("Correct answer rate:", this.correctAnswerRate);
      this.$router.push(`/workBookDetail/${this.workbookId}`);
    },
    async fetchResults() {
      const headers = {
        'Authorization': this.token
      };
      axios.get(`/workbook/${this.workbookId}/problem/getTestResult`, {headers})
          .then((res) => {
            this.problems = res.data;
            console.log("FETCH DATA", res);
          })
          .catch((error) => {
            alert("ERROR OCCURRED!");
            console.log("ERROR", error);
          })
    },

  }
}
</script>

<style scoped>

.problem-item.correct {
  border-left: 3px solid green;
}

.problem-item.incorrect {
  border-left: 3px solid red;
}

.problem-item.unanswered {
  border-left: 3px solid rgb(128, 128, 128);
}

.answer-section {
  margin-bottom: 20px;
  max-width: 100%; /* 최대 너비 제한 */
  word-wrap: break-word; /* 긴 단어 줄바꿈 */
  overflow-wrap: break-word; /* 모든 브라우저 지원을 위한 속성 */
  white-space: pre-wrap; /* 공백과 줄바꿈 유지 */
  max-width: 100%;
}

.grading-cards {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-top: 20px;
}

.grade-card {
  width: 100px;
  height: 80px;
  border-radius: 8px;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  background-color: white;
}

.grade-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
}

.correct-card {
  color: #4CAF50;
  border: 2px solid #4CAF50;
}

.incorrect-card {
  color: #f44336;
  border: 2px solid #f44336;
}



.grade-card:hover, .grade-card.selected {
  color: white;
}

.correct-card:hover, .correct-card.selected {
  background-color: rgba(76, 175, 80, 0.1);
}

.incorrect-card:hover, .incorrect-card.selected {
  background-color: rgba(244, 67, 54, 0.1);
}

.correct-card:hover, .correct-card.selected {
  background-color: #4CAF50;
}

.incorrect-card:hover, .incorrect-card.selected {
  background-color: #f44336;
}
.card-content {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.card-icon {
  font-size: 1.8em;
  margin-bottom: 5px;
}

.card-text {
  font-size: 1em;
  font-weight: bold;
}
/* 기존 스타일 유지 */
/* ... */
body, html {
  margin: 0;
  padding: 0;
  height: 100%;
}
.test-page{
  background-color: #FFFAF0;
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


.search-sort-container input {
  flex-grow: 1;
  min-width: 200px;
  max-width: 500px;
  box-sizing: border-box;
  padding: 0.5rem;
  border-radius: 4px;
  border: 1px solid #ccc;
}


.sort-dropdown button {
  padding: 0.5rem 1rem;
  background-color: #FFD700;
  border: none;
  border-radius: 4px;
  cursor: pointer;
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

a {
  text-decoration: none;
  color: inherit;
}


.back-button i {
  margin-right: 0.5rem;
}

.add-problem-form input,
.add-problem-form textarea {
  flex-grow: 1;
  padding: 0.5rem;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.problem-list {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}

.problem-item {
  background-color: #FFFFFF;
  border-radius: 8px;
  box-shadow: 0 2px 5px rgba(0,0,0,0.1);
  border-left: 4px solid #FFD700;
  padding: 1rem;
  transition: all 0.3s;
  position: relative;
  display: flex;
  justify-content: space-between;
  align-items: center;
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

.popup {
  width: 400px;
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

.test-page {
  display: flex;
  height: 100vh;
  font-family: Arial, sans-serif;
}

.sidebar {
  min-width: 303.25px;
  background-color: #f0f0f0;
  padding: 20px;
  overflow-y: auto;
}

.exit-btn {
  margin-bottom: 20px;
}

.problem-list {
  display: flex;
  flex-direction: column;
}

.problem-item {
  display: flex;
  align-items: center;
  padding: 10px;
  margin-bottom: 5px;
  cursor: pointer;
}

.problem-item.unanswered {
  border-left: 3px solid red;
}

.problem-item.answered {
  border-left: 3px solid green;
}


.problem-number {
  font-weight: bold;
  margin-right: 10px;
}

.main-content {
  flex-grow: 1;
  padding: 20px;
  display: flex;
  flex-direction: column;
  overflow-y: auto;
  max-height: 100vh;
}

.problem-content {
  margin-bottom: 20px;
}

textarea {
  width: 100%; /* 부모 요소의 너비에 맞춤 */
  max-width: 100%; /* 최대 너비를 부모 요소에 맞춤 */
  box-sizing: border-box; /* 패딩과 테두리를 너비에 포함 */
  word-wrap: break-word; /* 긴 단어 줄바꿈 */
  overflow-wrap: break-word
}

.navigation-buttons {
  display: flex;
  justify-content: space-between;
}

.submit-btn {
  position: fixed;
  bottom: 20px;
  right: 20px;
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
}

.popup {
  background-color: white;
  padding: 20px;
  border-radius: 5px;
  text-align: center;
}

.popup-buttons {
  display: flex;
  justify-content: space-around;
  margin-top: 20px;

}
.navigation-buttons {
  display: flex;
  justify-content: space-between;

}
/** **/
.exit-btn, .navigation-buttons button, .submit-btn {
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  font-size: 16px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.exit-btn {
  background-color: #f44336;
  color: white;
}
.exit-btn:hover {
  background-color: #d32f2f;
}

.navigation-buttons button {
  background-color: #2196F3;
  color: white;
}

.navigation-buttons button:hover {
  background-color: #1976D2;
}

.submit-btn {
  background-color: #FFD700;
  color: black;
}
.submit-btn:hover {
  background-color: rgba(188,154,0,0.9);
}

textarea {
  height: 200px;
  resize: none;
  margin-bottom: 20px;
  padding: 15px;
  border: 2px solid #ddd;
  border-radius: 5px;
  font-size: 16px;
  line-height: 1.5;
  transition: border-color 0.3s ease;
}

textarea:focus {
  outline: none;
  border-color: #2196F3;
  box-shadow: 0 0 5px rgba(33, 150, 243, 0.5);
}
.navigation-buttons {
  display: flex;
  justify-content: space-between;
}
.problem-item:hover {
  background-color: #e0e0e0;
}

.result-preview {
  font-weight: bold;
}

.result-preview.correct {
  color: green;
}

.result-preview.incorrect {
  color: red;
}

.problem-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 15px;
}

.correct-rate {
  font-size: 0.9em;
  color: #666;
  background-color: #f0f0f0;
  padding: 5px 10px;
  border-radius: 15px;
}
.result-popup {
  width: 300px;
}

.result-popup h3 {
  margin-bottom: 20px;
}

.result-popup p {
  text-align: left;
  margin-bottom: 10px;
}
</style>