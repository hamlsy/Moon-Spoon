<template>
  <div class="test-page">
    <!-- 왼쪽 사이드바 -->
    <div class="sidebar">
      <button class="exit-btn" @click="showExitPopup = true">나가기</button>
      <div class="question-list">
        <div
            v-for="(question, index) in questions"
            :key="index"
            :class="['question-item', {
            'correct': results[index] === 'correct',
            'incorrect': results[index] === 'incorrect',
            'unanswered': !results[index]
          }]"
            @click="goToQuestion(index)"
        >
          <span class="question-number">{{ index + 1 }}</span>
          <span class="answer-preview">
            {{ getQuestionPreview(index) }}
          </span>
          <span class="result-preview" :class="results[index]">
            {{ results[index] === 'correct' ? '정답' : results[index] === 'incorrect' ? '오답' : '' }}
          </span>
        </div>
      </div>
    </div>

    <!-- 메인 콘텐츠 -->
    <div class="main-content">
      <div class="question-header">
        <h2>문제 {{ currentQuestionIndex + 1 }}</h2>
        <span class="correct-rate" v-if="currentQuestion.correctRate !== undefined">
          정답률: {{ currentQuestion.correctRate }}%
        </span>
      </div>
      <p>{{ currentQuestion.question }}</p>
      <div class="answer-section">
        <p><strong>정답:</strong> {{ currentQuestion.correctAnswer }}</p>
        <p><strong>작성한 답안:</strong> {{ userAnswers[currentQuestionIndex] }}</p>
        <div class="grading-cards">
          <div
              @click="gradeQuestion('correct')"
              :class="['grade-card', 'correct-card', { 'selected': results[currentQuestionIndex] === 'correct' }]"
          >
            <div class="card-content">
              <span class="card-icon">✓</span>
              <span class="card-text">정답</span>
            </div>
          </div>
          <div
              @click="gradeQuestion('incorrect')"
              :class="['grade-card', 'incorrect-card', { 'selected': results[currentQuestionIndex] === 'incorrect' }]"
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
            v-if="currentQuestionIndex > 0"
            @click="goToPreviousQuestion"
        >
          이전
        </button>
        <button
            v-if="currentQuestionIndex < questions.length - 1"
            @click="goToNextQuestion"
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
  </div>
</template>

<script>
export default {
  data() {
    return {
      questions: [
        { question: "1 + 1 = ?", correctAnswer: "2", correctRate: 80 },
        { question: "2 * 3 = ?", correctAnswer: "6", correctRate: null },
        { question: "10 / 2 = ?", correctAnswer: "5", correctRate: null },
        // 더 많은 문제 추가...
      ],
      userAnswers: ["2", "6", "4"], // 사용자가 제출한 답안
      results: [], // 채점 결과 ('correct', 'incorrect', or '')
      currentQuestionIndex: 0,
      showExitPopup: false,
      showSubmitPopup: false
    }
  },
  computed: {
    currentQuestion() {
      return this.questions[this.currentQuestionIndex];
    },
    correctAnswerRate() {
      const correctCount = this.results.filter(result => result === 'correct').length;
      return (correctCount / this.questions.length * 100).toFixed(2) + '%';
    }
  },
  methods: {
    getQuestionPreview(index) {
      const question = this.questions[index].question;
      return question.length > 13 ? question.substring(0, 13) + '...' : question;
    },
    goToQuestion(index) {
      this.currentQuestionIndex = index;
    },
    goToPreviousQuestion() {
      if (this.currentQuestionIndex > 0) {
        this.currentQuestionIndex--;
      }
    },
    goToNextQuestion() {
      if (this.currentQuestionIndex < this.questions.length - 1) {
        this.currentQuestionIndex++;
      }
    },
    gradeQuestion(result) {
      this.$set(this.results, this.currentQuestionIndex, result);
    },
    exitGrading() {
      // 채점 종료 로직
      this.$router.push('/workBookDetail'); // 적절한 라우트로 변경
    },
    submitResults() {
      // 채점 결과 제출 로직
      console.log("Grading results submitted:", this.results);
      console.log("Correct answer rate:", this.correctAnswerRate);
      this.$router.push('/workBookDetail'); // 적절한 라우트로 변경
    },
    async fetchResults() {
      // 서버에서 결과를 가져오는 로직
      // 예시:
      // const response = await axios.get('/api/results');
      // this.results = response.data.results;

      try {
        // 실제 구현에서는 이 부분을 서버 API 호출로 대체해야 합니다.
        // const response = await axios.get('/api/results');
        // this.results = response.data.results;
        // this.questions.forEach((q, index) => {
        //   q.correctRate = response.data.correctRates[index];
        // });

        // 임시로 랜덤하게 결과와 정답률 생성
        this.results = this.questions.map(() => Math.random() > 0.5 ? 'correct' : 'incorrect');
        this.questions.forEach(q => {
          q.correctRate = Math.floor(Math.random() * 100);
        });
      } catch (error) {
        console.error("Error fetching results:", error);
      }
    },
    created() {
      this.fetchResults(); // 컴포넌트 생성 시 결과 가져오기
    }
  }
}
</script>

<style scoped>
/* 기존 스타일을 유지하고 새로운 스타일 추가 */

.question-item.correct {
  border-left: 3px solid green;
}

.question-item.incorrect {
  border-left: 3px solid red;
}

.question-item.unanswered {
  border-left: 3px solid rgb(128, 128, 128);
}

.answer-section {
  margin-bottom: 20px;
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

.add-question-form input,
.add-question-form textarea {
  flex-grow: 1;
  padding: 0.5rem;
  border: 1px solid #ccc;
  border-radius: 4px;
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
  display: flex;
  justify-content: space-between;
  align-items: center;
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
  width: 250px;
  background-color: #f0f0f0;
  padding: 20px;
  overflow-y: auto;
}

.exit-btn {
  margin-bottom: 20px;
}

.question-list {
  display: flex;
  flex-direction: column;
}

.question-item {
  display: flex;
  align-items: center;
  padding: 10px;
  margin-bottom: 5px;
  cursor: pointer;
}

.question-item.unanswered {
  border-left: 3px solid red;
}

.question-item.answered {
  border-left: 3px solid green;
}


.question-number {
  font-weight: bold;
  margin-right: 10px;
}

.main-content {
  flex-grow: 1;
  padding: 20px;
  display: flex;
  flex-direction: column;
}

.question-content {
  margin-bottom: 20px;
}

textarea {
  height: 200px;
  resize: none;
  margin-bottom: 20px;
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
.question-item:hover {
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

.question-header {
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
</style>