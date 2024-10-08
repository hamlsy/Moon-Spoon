<template>
  <div class="test-page">
    <!-- 왼쪽 사이드바 -->
    <div class="sidebar">
      <h2 class="test-name">{{ truncateText(sharedWorkbookTitle) }}</h2>
      <button class="exit-btn" @click="showExitPopup = true">나가기</button>
      <div class="problem-list">
        <div
            v-for="(problem, index) in problems"
            :key="index"
            :class="['problem-item', {
            'unanswered': !userAnswers[index]?.input,
            'answered': userAnswers[index]?.input,
          }]"
            @click="goToproblem(index)"
        >
          <span class="problem-number">{{ index + 1 }}</span>
          <div class="preview-container">
            <div class="problem-preview">{{ getProblemPreview(index) }}</div>
            <div class="answer-preview">{{ getAnswerPreview(index) }}</div>
          </div>
        </div>
      </div>
    </div>

    <!-- 메인 콘텐츠 -->
    <div class="main-content">
      <div class="problem-header">
        <h2>문제 {{ currentproblemIndex + 1 }}</h2>
<!--        <span class="correct-rate" v-if="currentproblem.correctRate !== undefined">-->
<!--          정답률: {{ getCorrectRate(currentproblem.correctRate) }}%-->
<!--        </span>-->
      </div>
      <div class="problem-content">
        <p><strong>문제:</strong> {{ currentproblem.question }}</p>
        <div v-if="hide">
          <p @click="toggleSolution" class="hidden-solution">
            <strong>정답:</strong> (클릭하여 정답 보기)
          </p>
        </div>
        <div v-else>
          <p><strong>정답:</strong> {{ currentproblem.solution }}</p>
        </div>
      </div>
      <textarea
          v-model="userAnswers[currentproblemIndex].input"
          placeholder="연습용 텍스트 박스"
      ></textarea>
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

    <!-- 나가기 확인 팝업 -->
    <div v-if="showExitPopup" class="popup-overlay">
      <div class="popup">
        <h3>연습 모드를 종료하시겠습니까?</h3>
        <div class="popup-buttons">
          <button @click="showExitPopup = false">취소</button>
          <button @click="exitTest">확인</button>
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
      problems: [
      ],
      userAnswers: [],
      currentproblemIndex: 0,
      showExitPopup: false,
      showSubmitPopup: false,
      token: localStorage.getItem('token'),
      sharedWorkbookId: this.$route.query.sharedWorkbookId,
      sharedWorkbookTitle: this.$route.query.sharedWorkbookTitle,
      hide: true,
      hideSolution: this.$route.query.hideSolution
    }
  },
  computed: {
    currentproblem() {
      return this.problems[this.currentproblemIndex];
    }
  },
  created(){
    this.getPracticeProblems();
  },
  methods: {
    getPracticeProblems(){
      const headers = {
        'Authorization': this.token
      };

      axios.get(`/api/test/${this.sharedWorkbookId}/getPractice`,
          {headers})
          .then((res) => {
            this.problems = res.data;
            this.initializeUserAnswers();
            if(this.hideSolution == "false"){
              this.hideSolution = false
            }
            this.hide = this.hideSolution
            console.log("FETCH PROBLEMS", res);
          })
          .catch((error) => {
            alert(error.data.response.message);
            this.$router.push(`/workbookDetail/${this.workbookId}`);
            console.log("ERROR!", error);
          })
    },
    initializeUserAnswers() {
      this.userAnswers = this.problems.map(problem => ({
        id: problem.id,
        input: ''
      }));
    },
    getProblemPreview(index) {
      const problem = this.problems[index].question;
      return problem.length > 13 ? problem.substring(0, 13) + '...' : problem;
      // this.hide = this.hideSolution
    },
    getAnswerPreview(index) {
      const answer = this.userAnswers[index].input;
      if (!answer) return '';
      return answer.length > 13 ? answer.substring(0, 13) + '...' : answer;
      // this.hide = this.hideSolution
    },
    getproblemPreview(index){
      const problem = this.problems[index].question;
      if (!problem) return '';
      return problem.length > 13 ? problem.substring(0, 13) + '...' : problem;
      // this.hide = this.hideSolution
    },
    goToproblem(index) {
      this.currentproblemIndex = index;
      this.hide = this.hideSolution
    },
    goToPreviousproblem() {
      if (this.currentproblemIndex > 0) {
        this.currentproblemIndex--;
        this.hide = this.hideSolution
      }
    },
    goToNextproblem() {
      if (this.currentproblemIndex < this.problems.length - 1) {
        this.currentproblemIndex++;
        this.hide = this.hideSolution
      }
    },
    exitTest() {
      // 테스트 종료 로직
      this.$router.push(`/sharedWorkBook/${this.sharedWorkbookId}`); // 적절한 라우트로 변경
    },
    truncateText(text, maxLength = 40) {
      return text.length > maxLength ? text.slice(0, maxLength) + '...' : text;
    },
    getCorrectRate(correctRate){
      return (correctRate*100).toFixed(2);
    },
    toggleSolution() {
      this.hide = !this.hide
    }
  }
}
</script>

<style scoped>
body, html {
  margin: 0;
  padding: 0;
  height: 100%;
}
.test-page{
  display: flex;
  height: 100vh;
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
  align-items: flex-start;
  padding: 10px;
  margin-bottom: 5px;
  cursor: pointer;
  height: 60px; /* 고정 높이 설정 */
  overflow: hidden;
}


.problem-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 10px rgba(0,0,0,0.1);
}
.preview-container {
  display: flex;
  flex-direction: column;
  overflow: hidden;
  flex-grow: 1;
}
.problem-preview, .answer-preview {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.answer-section {
  margin-bottom: 20px;
  max-width: 100%; /* 최대 너비 제한 */
  word-wrap: break-word; /* 긴 단어 줄바꿈 */
  overflow-wrap: break-word; /* 모든 브라우저 지원을 위한 속성 */
  white-space: pre-wrap; /* 공백과 줄바꿈 유지 */
  max-width: 100%; /* 최대 너비를 부모 요소에 맞춤*/
}
.answer-section p {
  word-wrap: break-word; /* 긴 단어 줄바꿈 */
  overflow-wrap: break-word; /* 모든 브라우저 지원을 위한 속성 */
  white-space: pre-wrap; /* 공백과 줄바꿈 유지 */
  max-width: 100%; /* 최대 너비를 부모 요소에 맞춤 */
}
.problem-preview {
  font-weight: bold;
  margin-bottom: 5px;
}

.answer-preview {
  font-size: 0.8em;
  color: #666;
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
  min-width: 2rem;
  margin-top: 60px;
  max-width: 22rem;
  background-color: #f0f0f0;
  padding: 20px;
  overflow-y: auto;
  word-break: break-all;
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
  max-height: 2rem;
}

.problem-item.unanswered {
  border-left: 3px solid red;
}

.problem-item.answered {
  border-left: 3px solid skyblue;
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
  overflow-y: auto; /* 세로 스크롤 추가 */
  width: 90%;
  max-height: 100vh; /* 뷰포트 높이를 최대로 설정 */
  min-width: 12rem;
}

.problem-content {
  margin-bottom: 20px;
  margin-top: 70px;
}


.submit-btn {
  position: relative;
  bottom: -2rem;
  margin-bottom: 1rem;
  width: 13rem;
  margin-left: auto;
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
  padding: 10px 25px;
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
  width: 100%; /* 부모 요소의 너비에 맞춤 */
  max-width: 100%; /* 최대 너비를 부모 요소에 맞춤 */
  box-sizing: border-box; /* 패딩과 테두리를 너비에 포함 */
  word-wrap: break-word; /* 긴 단어 줄바꿈 */
  overflow-wrap: break-word; /* 모든 브라우저 지원을 위한 속성 */
  white-space: pre-wrap; /* 공백과 줄바꿈 유지 */
  min-height: 100px; /* 최소 높이 설정 */
  max-height: 300px; /* 최대 높이 설정 */
  overflow-y: auto; /* 내용이 넘칠 경우 스크롤바 표시 */
}

textarea:focus {
  outline: none;
  border-color: #2196F3;
  box-shadow: 0 0 5px rgba(33, 150, 243, 0.5);
}
.problem-item:hover {
  background-color: #e0e0e0;
}
.test-name {
  margin-bottom: 20px;
  text-align: center;
  color: #333;
}
.problem-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 15px;
  margin-top: 70px;
}
.correct-rate {
  font-size: 0.9em;
  color: #666;
  background-color: #f0f0f0;
  padding: 5px 10px;
  border-radius: 15px;
}
.hidden-solution {
  cursor: pointer;
  color: gray;
  text-decoration: underline;
}
</style>